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
import org.acm.seguin.pretty.ModifierHolder;
import org.acm.seguin.summary.TypeSummary;
import org.acm.seguin.summary.TypeDeclSummary;
import org.acm.seguin.summary.FieldSummary;

/**
 *  Finds a field associated with a particular type summary. A permission is
 *  also specified to insure that we find something interesting.
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public class FieldQuery {
    /**
     *  The field we are looking for can have any protection level
     */
    public final static int PRIVATE = 1;
    /**
     *  The field we are looking for must have default protection level or
     *  higher
     */
    public final static int DEFAULT = 2;
    /**
     *  The field we are looking for must have protected protection level or
     *  higher
     */
    public final static int PROTECTED = 3;
    /**
     *  The field we are looking for must be public
     */
    public final static int PUBLIC = 4;


    /**
     *  Finds an associated field
     *
     *@param  typeSummary  the type
     *@param  name         the name
     *@return              the field found or null if none
     */
    public static FieldSummary find(TypeSummary typeSummary, String name) {
        FieldSummary result = query(typeSummary, name, PRIVATE);
        if (result == null) {
            result = queryAncestors(typeSummary, name, PROTECTED);
        }
        return result;
    }


    /**
     *  Finds the field associated with a type
     *
     *@param  typeSummary  the type to search
     *@param  name         the name of the field
     *@param  protection   the minimum protection level
     *@return              the field summary if one is found, null if none is
     *      found
     */
    public static FieldSummary query(TypeSummary typeSummary,
            String name, int protection) {
        Iterator iter = typeSummary.getFields();
        if (iter != null) {
            while (iter.hasNext()) {
                FieldSummary next = (FieldSummary) iter.next();
                if (appropriate(next, name, protection)) {
                    return next;
                }
            }
        }

        return null;
    }


    /**
     *  Finds the field associated with a type in the ancestors of that type
     *
     *@param  typeSummary  the type to search
     *@param  name         the name of the field
     *@param  protection   the minimum protection level
     *@return              the field summary if one is found, null if none is
     *      found
     */
    public static FieldSummary queryAncestors(TypeSummary typeSummary,
            String name, int protection) {
        TypeDeclSummary next = typeSummary.getParentClass();
        TypeSummary current = GetTypeSummary.query(next);

        while (current != null) {
            FieldSummary attempt = query(current, name, protection);
            if (attempt != null) {
                return attempt;
            }

            next = current.getParentClass();
            current = GetTypeSummary.query(next);
        }

        return null;
    }


    /**
     *  Checks if the field we are considering is the correct type
     *
     *@param  fieldSummary  the summary of the field
     *@param  name          the name of the field
     *@param  protection    the protection level of the field
     *@return               true if the field has the appropriate name and the
     *      appropriate protection level.
     */
    private static boolean appropriate(FieldSummary fieldSummary,
            String name, int protection) {
        if (fieldSummary.getName().equals(name)) {
            ModifierHolder mods = fieldSummary.getModifiers();
            if (protection == PRIVATE) {
                return true;
            } else if ((protection == DEFAULT) && !mods.isPrivate()) {
                return true;
            } else if ((protection == PROTECTED) && (mods.isPublic() || mods.isProtected())) {
                return true;
            } else if ((protection == PUBLIC) && mods.isPublic()) {
                return true;
            }
        }

        return false;
    }
}
