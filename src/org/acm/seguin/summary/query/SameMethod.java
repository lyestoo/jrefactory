/* ====================================================================
 * The JRefactory License, Version 1.0
 *
 * Copyright (c) 2001 JRefactory.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        JRefactory (http://www.sourceforge.org/projects/jrefactory)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "JRefactory" must not be used to endorse or promote
 *    products derived from this software without prior written
 *    permission. For written permission, please contact seguin@acm.org.
 *
 * 5. Products derived from this software may not be called "JRefactory",
 *    nor may "JRefactory" appear in their name, without prior written
 *    permission of Chris Seguin.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE CHRIS SEGUIN OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of JRefactory.  For more information on
 * JRefactory, please see
 * <http://www.sourceforge.org/projects/jrefactory>.
 */
package org.acm.seguin.summary.query;

import java.util.Iterator;
import org.acm.seguin.summary.MethodSummary;
import org.acm.seguin.summary.ParameterSummary;
import org.acm.seguin.summary.TypeDeclSummary;
import org.acm.seguin.summary.TypeSummary;

/**
 *  Checks that two methods are the same. Also provides a search feature to find
 *  a method with a specific signature in a type
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public class SameMethod {
    private final static int SAME = 0;
    private final static int ONE_ANCESTOR = 1;
    private final static int TWO_ANCESTOR = 2;
    private final static int ERROR = 3;
    private final static int ANCESTOR = 4;


    /**
     *  Checks if two methods are the same
     *
     *@param  one  Description of Parameter
     *@param  two  Description of Parameter
     *@return      Description of the Returned Value
     */
    public static boolean query(MethodSummary one, MethodSummary two) {
        return check(one, two, SAME);
    }


    /**
     *  Description of the Method
     *
     *@param  one  Description of Parameter
     *@param  two  Description of Parameter
     *@return      Description of the Returned Value
     */
    public static boolean conflict(MethodSummary one, MethodSummary two) {
        return check(one, two, ANCESTOR);
    }


    /**
     *  Finds the method with the same signature in the other type
     *
     *@param  type    Description of Parameter
     *@param  method  Description of Parameter
     *@return         Description of the Returned Value
     */
    public static MethodSummary find(TypeSummary type, MethodSummary method) {
        Iterator iter = type.getMethods();
        if (iter != null) {
            while (iter.hasNext()) {
                MethodSummary next = (MethodSummary) iter.next();
                if (query(next, method)) {
                    return next;
                }
            }
        }
        return null;
    }


    /**
     *  Finds the method with a conflicting in the other type
     *
     *@param  type    Description of Parameter
     *@param  method  Description of Parameter
     *@return         Description of the Returned Value
     */
    public static MethodSummary findConflict(TypeSummary type, MethodSummary method) {
        Iterator iter = type.getMethods();
        if (iter != null) {
            while (iter.hasNext()) {
                MethodSummary next = (MethodSummary) iter.next();
                if (conflict(next, method)) {
                    return next;
                }
            }
        }
        return null;
    }


    /**
     *  Checks the types
     *
     *@param  one  Description of Parameter
     *@param  two  Description of Parameter
     *@param  way  Description of Parameter
     *@return      Description of the Returned Value
     */
    private static int compareTypes(TypeSummary one, TypeSummary two, int way) {
        if (one == two) {
            return way;
        }

        if (way == ANCESTOR) {
            if (Ancestor.query(one, two)) {
                return ONE_ANCESTOR;
            } else if (Ancestor.query(two, one)) {
                return TWO_ANCESTOR;
            }
        }

        if ((ONE_ANCESTOR == way) && (Ancestor.query(two, one))) {
            return way;
        }

        if ((TWO_ANCESTOR == way) && (Ancestor.query(one, two))) {
            return way;
        }

        return ERROR;
    }


    /**
     *  Work horse that actually checks the methods
     *
     *@param  one   Description of Parameter
     *@param  two   Description of Parameter
     *@param  test  Description of Parameter
     *@return       Description of the Returned Value
     */
    private static boolean check(MethodSummary one, MethodSummary two, int test) {
        if (!one.getName().equals(two.getName())) {
            return false;
        }

        Iterator oneIter = one.getParameters();
        Iterator twoIter = two.getParameters();

        if (oneIter == null) {
            return twoIter == null;
        }

        if (twoIter == null) {
            return false;
        }

        while (oneIter.hasNext() && twoIter.hasNext()) {
            ParameterSummary oneParam = (ParameterSummary) oneIter.next();
            ParameterSummary twoParam = (ParameterSummary) twoIter.next();

            TypeDeclSummary oneDecl = oneParam.getTypeDecl();
            TypeDeclSummary twoDecl = twoParam.getTypeDecl();

            TypeSummary typeOne = GetTypeSummary.query(oneDecl);
            TypeSummary typeTwo = GetTypeSummary.query(twoDecl);

            if (compareTypes(typeOne, typeTwo, test) == ERROR) {
                return false;
            }
        }

        return !(oneIter.hasNext() || twoIter.hasNext());
    }
}
