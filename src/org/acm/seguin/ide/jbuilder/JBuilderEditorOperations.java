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
package org.acm.seguin.ide.jbuilder;

import com.borland.jbuilder.node.JavaFileNode;
import com.borland.primetime.editor.EditorPane;
import com.borland.primetime.ide.Browser;
import com.borland.primetime.node.FileNode;
import com.borland.primetime.node.Node;
import com.borland.primetime.vfs.Buffer;
import com.borland.primetime.vfs.ReadOnlyException;
import com.borland.primetime.viewer.AbstractTextNodeViewer;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.acm.seguin.ide.common.EditorOperations;

/**
 *  The implementation of the editor operations for JBuilder
 *
 *@author     Chris Seguin
 *@created    October 18, 2001
 */
public class JBuilderEditorOperations extends EditorOperations {
    private Buffer buffer;


    /**
     *  Sets the line number
     *
     *@param  value  The new LineNumber value
     */
    public void setLineNumber(int value) {
        Browser browser = Browser.getActiveBrowser();
        Node active = browser.getActiveNode();
        AbstractTextNodeViewer sourceViewer =
                (AbstractTextNodeViewer) browser.getViewerOfType(active, AbstractTextNodeViewer.class);
        EditorPane editor = sourceViewer.getEditor();
        editor.gotoPosition(value, 1, false, EditorPane.CENTER_ALWAYS);
    }


    /**
     *  Sets the string in the IDE
     *
     *@param  value  The new file contained in a string
     */
    public void setStringInIDE(String value) {
        if (value != null) {
            try {
                buffer.setContent(value.getBytes());
            }
            catch (ReadOnlyException roe) {
                JOptionPane.showMessageDialog(null,
                        "The file that you ran the pretty printer on is read only.",
                        "Read Only Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    /**
     *  Returns the frame that contains the editor. If this is not available or
     *  you want dialog boxes to be centered on the screen return null from this
     *  operation.
     *
     *@return    the frame
     */
    public JFrame getEditorFrame() {
        return Browser.getActiveBrowser();
    }


    /**
     *  Gets the file that is being edited
     *
     *@return    The File value
     */
    public File getFile() {
        Browser browser = Browser.getActiveBrowser();
        Node active = browser.getActiveNode();

        if (active instanceof FileNode) {
            return ((FileNode) active).getUrl().getFileObject();
        }
        else {
            return null;
        }
    }


    /**
     *  Returns the initial line number
     *
     *@return    The LineNumber value
     */
    public int getLineNumber() {
        Browser browser = Browser.getActiveBrowser();
        Node active = browser.getActiveNode();
        if (active == null) {
            return -1;
        }

        AbstractTextNodeViewer sourceViewer =
                (AbstractTextNodeViewer) browser.getViewerOfType(active, AbstractTextNodeViewer.class);
        if (sourceViewer == null) {
            return -1;
        }

        EditorPane editor = sourceViewer.getEditor();
        if (editor == null) {
            return -1;
        }

        int pos = editor.getCaretPosition();
        return editor.getLineNumber(pos);
    }


    /**
     *  Gets the SelectionFromIDE attribute of the JBuilderExtractMethod object
     *
     *@return    The SelectionFromIDE value
     */
    public String getSelectionFromIDE() {
        Browser browser = Browser.getActiveBrowser();
        Node active = browser.getActiveNode();
        AbstractTextNodeViewer sourceViewer =
                (AbstractTextNodeViewer) Browser.getActiveBrowser().getViewerOfType(active, AbstractTextNodeViewer.class);
        EditorPane editor = sourceViewer.getEditor();
        return editor.getSelectedText();
    }


    /**
     *  Gets the initial string from the IDE
     *
     *@return    The file in string format
     */
    public String getStringFromIDE() {
        Browser browser = Browser.getActiveBrowser();
        Node active = browser.getActiveNode();
        if (active instanceof JavaFileNode) {
            JavaFileNode jtn = (JavaFileNode) active;
            try {
                buffer = jtn.getBuffer();
                byte[] contents = buffer.getContent();
                return new String(contents);
            }
            catch (java.io.IOException ioex) {
                ioex.printStackTrace();
            }
        }

        return null;
    }


    /**
     *  Returns true if the current file being edited is a java file
     *
     *@return    true if the current file is a java file
     */
    public boolean isJavaFile() {
        Browser browser = Browser.getActiveBrowser();
        Node active = browser.getActiveNode();
        return (active instanceof JavaFileNode);
    }
}
//  EOF
