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
import org.acm.seguin.summary.FieldSummary;
import org.acm.seguin.summary.LocalVariableSummary;
import org.acm.seguin.summary.MethodSummary;
import org.acm.seguin.summary.ParameterSummary;
import org.acm.seguin.summary.Summary;
import org.acm.seguin.summary.TypeDeclSummary;
import org.acm.seguin.summary.TypeSummary;
import org.acm.seguin.summary.VariableSummary;

/**
 *  Performs a local variable lookup
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public class LookupVariable {
    /**
     *  Looks up the variable
     *
     *@param  method  the method summary
     *@param  name    the name of the variable to find
     *@return         the variable summary if found or null otherwise
     */
    public static VariableSummary query(MethodSummary method, String name) {
        VariableSummary result = getLocal(method, name);
        if (result != null) {
            return result;
        }

        TypeSummary currentType = (TypeSummary) method.getParent();
        return queryFieldSummary(currentType, name);
    }


    /**
     *  Get a field summary
     *
     *@param  currentType  the type to search in
     *@param  name         the name of the field
     *@return              the field summary found or null if none was found
     */
    public static VariableSummary queryFieldSummary(TypeSummary currentType, String name) {
        VariableSummary result = getField(currentType, name, true);
        if (result != null) {
            return result;
        }

        TypeDeclSummary parentType = currentType.getParentClass();
        currentType = GetTypeSummary.query(parentType);
        while (currentType != null) {
            result = getField(currentType, name, false);
            if (result != null) {
                return result;
            }

            parentType = currentType.getParentClass();
	        currentType = GetTypeSummary.query(parentType);
        }

        return null;
    }


    /**
     *  Finds a field in a type summary
     *
     *@param  type              the type to search
     *@param  name              the name of the variable
     *@param  isPrivateAllowed  is the field allowed to be private
     *@return                   The FieldSummary if found, null otherwise
     */
    private static VariableSummary getField(TypeSummary type, String name, boolean isPrivateAllowed) {
        Iterator iter = type.getFields();
        if (iter == null) {
            return null;
        }

        while (iter.hasNext()) {
            FieldSummary next = (FieldSummary) iter.next();
            if (!isPrivateAllowed || next.getModifiers().isPrivate()) {
                if (next.getName().equals(name)) {
                    return next;
                }
            }
        }

        return null;
    }


    /**
     *  Looks up the variable inside the method
     *
     *@param  method  the method summary
     *@param  name    the name of the variable to find
     *@return         the variable summary if found or null otherwise
     */
    public static VariableSummary getLocal(MethodSummary method, String name) {
        Iterator iter = method.getParameters();
        if (iter != null) {
            while (iter.hasNext()) {
                ParameterSummary param = (ParameterSummary) iter.next();
                if (param.getName().equals(name)) {
                    return param;
                }
            }
        }

        iter = method.getDependencies();
        if (iter != null) {
            while (iter.hasNext()) {
                Summary next = (Summary) iter.next();
                if ((next instanceof LocalVariableSummary) && (next.getName().equals(name))) {
                    return (VariableSummary) next;
                }
            }
        }

        return null;
    }
}
