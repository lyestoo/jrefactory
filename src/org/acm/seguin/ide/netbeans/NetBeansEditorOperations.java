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

import org.acm.seguin.ide.common.*;
import org.openide.*;
import org.openide.cookies.*;
import org.openide.filesystems.*;
import org.openide.loaders.*;
import org.openide.nodes.*;
import org.openide.windows.*;

/**
 *  Description of Class
 *
 *@author     unknown
 *@created    October 18, 2001
 */
public class NetBeansEditorOperations extends EditorOperations {

    private final int NEW_LINE_LENGTH = 1;


    /**
     *  Sets the line number
     *
     *@param  lineNumber  The new lineNumber value
     */
    public void setLineNumber(int lineNumber) {
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

        getCurrentEditorPane().setCaretPosition(targetOffset);
    }


    /**
     *  Set the text of the file being edited
     *
     *@param  text
     */
    public void setStringInIDE(String text) {
        getCurrentEditorPane().setText(text);
    }


    /**
     *  Gets the currentEditorPane attribute of the NetBeansEditorOperations
     *  object
     *
     *@return    The currentEditorPane value
     */
    private JEditorPane getCurrentEditorPane() {
        TopComponent comp =
                TopManager.getDefault().getWindowManager().getRegistry().getActivated();
        Node[] nodes = comp.getRegistry().getActivatedNodes();

        EditorCookie cookie = null;
        if (nodes.length == 1) {
            cookie = (EditorCookie) nodes[0].getCookie(EditorCookie.class);
        }

        JEditorPane[] panes = cookie.getOpenedPanes();
        if (panes.length == 1) {
            return panes[0];
        }

        return null;
    }


    /**
     *  Gets the documentTextReader attribute of the NetBeansEditorOperations
     *  object
     *
     *@return    The documentTextReader value
     */
    private BufferedReader getDocumentTextReader() {
        BufferedReader reader = new BufferedReader(new StringReader(
                getCurrentEditorPane().getText()));
        return reader;
    }


    /**
     *  Returns the frame that contains the editor. If this is not available or
     *  you want dialog boxes to be centered on the screen return null from this
     *  operation.
     *
     *@return    The frame that contains the editor
     */
    public JFrame getEditorFrame() {
        return null;
    }


    /**
     *  Return the current File being edited
     *
     *@return    Current file being edited or null if nothing selected
     */
    public File getFile() {
        return new File(getFileObject().getNameExt());
    }


    /**
     *  Gets the fileObject attribute of the NetBeansEditorOperations object
     *
     *@return    The fileObject value
     */
    private FileObject getFileObject() {
        Node[] nodes = TopComponent.getRegistry().getCurrentNodes();
        for (int i = 0; i < nodes.length; i++) {
            Node.Cookie cookie = nodes[i].getCookie(DataObject.class);
            if (cookie != null) {
                return (FileObject) ((DataObject) cookie).files().iterator().next();
            }
        }

        //(PENDING) should be an exception
        return null;
    }


    /**
     *  Return the current line number
     *
     *@return    The 1-based line number
     */
    public int getLineNumber() {
        BufferedReader reader = getDocumentTextReader();

        int offset = getCurrentEditorPane().getCaretPosition();

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
     *  Return the current text selected in the IDE
     *
     *@return    currently selected text
     */
    public String getSelectionFromIDE() {
        return getCurrentEditorPane().getSelectedText();
    }


    /**
     *  Return the text of the file being edited
     *
     *@return    Text of file being edited
     */
    public String getStringFromIDE() {
        return getCurrentEditorPane().getText();
    }


    /**
     *  Returns true if the current file being edited is a java file
     *
     *@return    true if the current file is a java file
     */
    public boolean isJavaFile() {
        return (getFileObject().getExt().equals("java"));
    }

}
//  EOF
