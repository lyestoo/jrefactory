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
package org.acm.seguin.uml;

import org.acm.seguin.summary.TypeSummary;

/**
 *  This class allows the user to select a series of classes in the UML class
 *  diagram and this converts the selected classes into an array of TypeSummary
 *  objects. This will allow us to decouple the user interface dialog boxes for
 *  entering information about the refactoring from the UML class diagrams.
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public class SelectedSummaryList {
    /**
     *  Get the selected type summaries
     *
     *@param  umlPackage  the package
     *@param  umlType     the type
     *@return             the list of type summaruies
     */
    public static TypeSummary[] list(UMLPackage umlPackage, UMLType umlType) {
        TypeSummary[] result;

        //  Add the types to the result array
        if (umlType.isSelected()) {
            result = getSelectedTypes(umlPackage);
        } else {
            result = new TypeSummary[1];
            result[0] = umlType.getSummary();
        }

        //  Return the result array
        return result;
    }


    /**
     *  Gets the SelectedTypes attribute of the SelectedSummaryList class
     *
     *@param  umlPackage  Description of Parameter
     *@return             The SelectedTypes value
     */
    private static TypeSummary[] getSelectedTypes(UMLPackage umlPackage) {
        TypeSummary[] result;

        int count = 0;
        UMLType[] types = umlPackage.getTypes();
        for (int ndx = 0; ndx < types.length; ndx++) {
            if (types[ndx].isSelected()) {
                count++;
            }
        }

        result = new TypeSummary[count];
        count = 0;

        for (int ndx = 0; ndx < types.length; ndx++) {
            if (types[ndx].isSelected()) {
                result[count] = types[ndx].getSummary();
                count++;
            }
        }
        return result;
    }
}
