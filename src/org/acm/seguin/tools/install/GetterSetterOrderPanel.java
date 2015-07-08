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
package org.acm.seguin.tools.install;

import java.io.PrintWriter;
import java.util.StringTokenizer;

/**
 *  Description of the Class
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public class GetterSetterOrderPanel extends ListOrderPanel {
    /**
     *  Constructor for the GetterSetterOrderPanel object
     */
    public GetterSetterOrderPanel() {
        super();
        addDescription("Order getters, setters, and other methods");
        addDescription("Setters are methods that start with the word 'set'");
        addDescription("Getters are methods that start with the word 'get' or 'is'");
        addControl();
        addListener();
    }


    /**
     *  Print this setting
     *
     *@param  output  Description of Parameter
     *@param  index   Description of Parameter
     */
    public void generateSetting(PrintWriter output, int index) {
        generateSetting(output, index, "Method");
    }


    /**
     *  Description of the Method
     *
     *@return    Description of the Returned Value
     */
    protected String[] loadOriginalArray() {
        StringTokenizer tok = findProperty("Method", "(setter,getter,other)");
        String[] result = new String[]{"setter", "getter", "other"};
        int index = 0;
        while (tok.hasMoreTokens()) {
            result[index] = tok.nextToken();
            index++;
        }
        return result;
    }


    /**
     *  Gets the SortName attribute of the SortSettingPanel object
     *
     *@return    The SortName value
     */
    protected String getSortName() {
        return "Method";
    }
}

//  This is the end of the file

