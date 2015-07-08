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
package org.acm.seguin.ide.elixir;

import com.elixirtech.fx.*;
import com.elixirtech.ide.edit.*;
import org.acm.seguin.pretty.PrettyPrintFromIDE;
import org.acm.seguin.tools.install.PrettyPrinterConfigGUI;

/**
 *  Pretty printer for the elixir editor.
 *
 *@author     Chris Seguin
 *@created    October 18, 2001
 *@date       May 31, 1999
 */
public class ElixirPrettyPrinter extends PrettyPrintFromIDE {
    private BasicViewManager bvm;
    private static PrettyPrinterConfigGUI gui = null;


    /**
     *  Create an ElixirPrettyPrinter object
     */
    public ElixirPrettyPrinter() {
        super();
    }


    /**
     *  Sets the line number
     *
     *@param  value  The new LineNumber value
     */
    protected void setLineNumber(int value) {
        bvm.setLineNo(value);
    }


    /**
     *  Sets the string in the IDE
     *
     *@param  value  The new file contained in a string
     */
    protected void setStringInIDE(String value) {
        bvm.setContentsString(value);
    }


    /**
     *  Returns the initial line number
     *
     *@return    The LineNumber value
     */
    protected int getLineNumber() {
        return bvm.getLineNo();
    }


    /**
     *  Get the output buffer
     *
     *@return    a string containing the results
     */
    protected String getOutputBuffer() {
        return removeCR(super.getOutputBuffer());
    }


    /**
     *  Gets the initial string from the IDE
     *
     *@return    The file in string format
     */
    protected String getStringFromIDE() {
        FrameManager fm = FrameManager.current();
        bvm = (BasicViewManager) fm.getViewSite().getCurrentViewManager();
        return bvm.getContentsString();
    }


    /**
     *  Reformats the current source code
     */
    public static void prettyPrint() {
        ElixirPrettyPrinter epp = new ElixirPrettyPrinter();
        epp.prettyPrintCurrentWindow();
    }


    /**
     *  Description of the Method
     */
    public static void prettyPrintConfig() {
        if (gui == null) {
            gui = new PrettyPrinterConfigGUI(false);
        }
        gui.run();
    }


    /**
     *  Remove \r from buffer
     *
     *@param  input  Description of Parameter
     *@return        a string containing the results
     */
    public String removeCR(String input) {
        StringBuffer buffer = new StringBuffer();
        int last = input.length();

        for (int ndx = 0; ndx < last; ndx++) {
            char ch = input.charAt(ndx);
            if (ch == '\r') {
                //  Do nothing
            }
            else {
                buffer.append(ch);
            }
        }

        return buffer.toString();
    }
}
//  EOF
