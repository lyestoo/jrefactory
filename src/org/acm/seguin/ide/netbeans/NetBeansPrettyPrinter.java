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
package org.acm.seguin.ide.netbeans;

import java.io.*;
import javax.swing.*;
import org.acm.seguin.pretty.PrettyPrintFromIDE;
import org.openide.*;
import org.openide.cookies.*;
import org.openide.nodes.*;
import org.openide.windows.*;

/**
 *  Description of the Class
 *
 *@author     Chris Seguin
 *@created    October 18, 2001
 */
public class NetBeansPrettyPrinter extends PrettyPrintFromIDE {
    // NOTE: A new line is actually 2 characters long but 1 reflects how the
    // caret positioning works
    private final int NEW_LINE_LENGTH = 1;

    private JEditorPane _editorPane;


    /**
     *  Constructor for the NetBeansPrettyPrinter object
     *
     *@param  editorCookie  Description of Parameter
     */
    public NetBeansPrettyPrinter(EditorCookie editorCookie) {
        super();

        _editorPane = getCurrentEditorPane(editorCookie);
    }


    /**
     *  Sets the LineNumber attribute of the NetBeansPrettyPrinter object
     *
     *@param  lineNumber  The new LineNumber value
     */
    protected void setLineNumber(int lineNumber) {
        if (lineNumber < 1) {
            throw new IllegalArgumentException(
                    "lineNumber must be 1 or greater: " + lineNumber);
        }

        int targetOffset = 0;
        int lineCount = 1;

        BufferedReader reader = getDocumentTextReader();

        String currLine = null;
        try {
            currLine = reader.readLine();
            while (currLine != null && lineCount < lineNumber) {
                targetOffset += currLine.length() + NEW_LINE_LENGTH;
                lineCount++;
                currLine = reader.readLine();
            }
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }

        if (currLine == null) {
            if (lineCount < lineNumber) {
                throw new IllegalArgumentException(
                        "lineNumber is past end of document!: " + lineNumber);
            }

            if (lineCount > 0) {
                // no new line after last line
                targetOffset--;
            }
        }
        _editorPane.setCaretPosition(targetOffset);
    }


    /**
     *  Sets the string in the IDE
     *
     *@param  text  The new StringInIDE value
     */
    protected void setStringInIDE(String text) {
        _editorPane.setText(text);
    }


    /**
     *  Gets the CurrentEditorPane attribute of the NetBeansPrettyPrinter object
     *
     *@param  cookie  Description of Parameter
     *@return         The CurrentEditorPane value
     */
    private JEditorPane getCurrentEditorPane(EditorCookie cookie) {
        JEditorPane[] panes = cookie.getOpenedPanes();
        System.err.println("Panes: " + panes);
        if (panes.length == 1) {
            return panes[0];
        }

        return null;
    }


    /**
     *  Gets the DocumentTextReader attribute of the NetBeansPrettyPrinter
     *  object
     *
     *@return    The DocumentTextReader value
     */
    private BufferedReader getDocumentTextReader() {
        BufferedReader reader = new BufferedReader(new StringReader(
                _editorPane.getText()));
        return reader;
    }


    /**
     *@return    the initial line number, -1 if failed
     */
    protected int getLineNumber() {
        BufferedReader reader = getDocumentTextReader();

        int offset = _editorPane.getCaretPosition();

        int lineNumber = 0;
        int currOffset = 0;

        while (currOffset <= offset) {
            String currLine = null;
            try {
                currLine = reader.readLine();
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
                return -1;
            }
            currOffset += currLine.length() + NEW_LINE_LENGTH;
            lineNumber++;
        }

        return lineNumber;
    }


    /**
     *  Gets the initial string from the IDE
     *
     *@return    The file in string format
     */
    protected String getStringFromIDE() {
        return _editorPane.getText();
    }

}
//  EOF
