/*
 *  ====================================================================
 *  The JRefactory License, Version 1.0
 *
 *  Copyright (c) 2001 JRefactory.  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  JRefactory (http://www.sourceforge.org/projects/jrefactory)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "JRefactory" must not be used to endorse or promote
 *  products derived from this software without prior written
 *  permission. For written permission, please contact seguin@acm.org.
 *
 *  5. Products derived from this software may not be called "JRefactory",
 *  nor may "JRefactory" appear in their name, without prior written
 *  permission of Chris Seguin.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE CHRIS SEGUIN OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of JRefactory.  For more information on
 *  JRefactory, please see
 *  <http://www.sourceforge.org/projects/jrefactory>.
 */
package org.acm.seguin.uml.refactor;


import org.acm.seguin.summary.VariableSummary;



/**
 *  Names the variable based on hungarian notation
 *
 *@author     Chris Seguin
 *@created    July 16, 2002
 */
public class HungarianNamer
{
    /**
     *  Sets the suggested name of this parameter
     *
     *@param  initVariable  The new defaultName value
     *@param  prefix        Description of the Parameter
     *@return               The defaultName value
     */
    public String getDefaultName(VariableSummary initVariable, String prefix)
    {
        String name = initVariable.getName();
        if ((name.length() > 1) && (name.charAt(1) == '_')) {
            return name;
        }

        if (isAllCaps(name)) {
            return name;
        }

        StringBuffer buffer = new StringBuffer(prefix);
        String type = initVariable.getType();
        if (type.equals("String")) {
            buffer.append("sz");
        }
        else {
            useCapitalLettersFromType(type, buffer);
        }

        if (buffer.length() == 2) {
            buffer.append(type.charAt(0));
        }
        else if (buffer.length() == 3) {
            insureMinimumLettersInTypeCode(buffer, type);
        }

        int first = 0;
        if (name.charAt(0) == '_') {
            first++;
        }

        buffer.append(Character.toUpperCase(name.charAt(first)));
        if (name.length() > first + 1) {
            buffer.append(name.substring(first + 1));
        }

        return buffer.toString();
    }


    /**
     *  Gets the allCaps attribute of the HungarianNamer object
     *
     *@param  name  Description of the Parameter
     *@return       The allCaps value
     */
    private boolean isAllCaps(String name)
    {
        for (int ndx = 0; ndx < name.length(); ndx++) {
            char ch = name.charAt(ndx);
            if (ch == '_') {
                //  OK
            }
            else if (Character.isUpperCase(ch)) {
                //  OK
            }
            else {
                return false;
            }
        }

        return true;
    }


    /**
     *  Determines if the character is a vowel (a, e, i, o, or u)
     *
     *@param  ch  Description of the Parameter
     *@return     Description of the Return Value
     */
    private boolean isVowel(char ch)
    {
        ch = Character.toLowerCase(ch);
        return (ch == 'a') || (ch == 'e') || (ch == 'i') || (ch == 'o') || (ch == 'u');
    }


    /**
     *  Insures that we have the appropriate number of characters
     *
     *@param  buffer  Description of the Parameter
     *@param  type    Description of the Parameter
     */
    private void insureMinimumLettersInTypeCode(StringBuffer buffer, String type)
    {
        buffer.append(type.charAt(1));
        int ndx = 2;
        char ch = type.charAt(ndx);
        while (isVowel(ch)) {
            buffer.append(ch);
            ndx++;
            ch = type.charAt(ndx);
        }
        buffer.append(ch);
    }


    /**
     *  Selects the capital letters from the type
     *
     *@param  type    Description of the Parameter
     *@param  buffer  Description of the Parameter
     */
    private void useCapitalLettersFromType(String type, StringBuffer buffer)
    {
        for (int ndx = 0; ndx < type.length(); ndx++) {
            char ch = type.charAt(ndx);
            if (Character.isUpperCase(ch)) {
                buffer.append(Character.toLowerCase(ch));
            }
        }
    }
}
