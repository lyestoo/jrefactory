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
import com.elixirtech.ui.LineEditorPane;
import java.awt.Component;
import java.awt.Container;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;
import javax.swing.JOptionPane;
import org.acm.seguin.awt.ExceptionPrinter;
import org.acm.seguin.refactor.Refactoring;
import org.acm.seguin.refactor.method.ExtractMethodRefactoring;
import org.acm.seguin.refactor.RefactoringException;
import org.acm.seguin.uml.refactor.ExtractMethodDialog;

/**
 *  ExtractMethod for the elixir editor.
 *
 *@author     Chris Seguin
 *@created    October 18, 2001
 *@date       May 31, 1999
 */
public class ElixirExtractMethod extends ExtractMethodDialog {
    private BasicViewManager bvm;


    /**
     *  Create an ElixirPrettyPrinter object
     *
     *@exception  RefactoringException  Description of the Exception
     */
    public ElixirExtractMethod() throws RefactoringException {
        super(FrameManager.current().getFrame());
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
     *  Gets the SelectionFromIDE attribute of the ElixirExtractMethod object
     *
     *@return    The SelectionFromIDE value
     */
    protected String getSelectionFromIDE() {
        try {
            Object view = bvm.getView();
            System.out.println("View is a :  " + view.getClass().getName());
            JPanel panel = (JPanel) view;

            Component editor = searchPanels(panel, " ");

            if (editor instanceof JTextComponent) {
                JTextComponent comp = (JTextComponent) editor;
                return comp.getSelectedText();
            }

            System.out.println("Not a text component");
            return null;
        }
        catch (Throwable thrown) {
            thrown.printStackTrace(System.out);
        }
        return null;
    }


    /**
     *  Gets the initial string from the IDE
     *
     *@return    The file in string format
     */
    protected String getStringFromIDE() {
        FrameManager fm = FrameManager.current();
        bvm = (BasicViewManager) fm.getViewSite().getCurrentViewManager();
        if (bvm == null) {
            return null;
        }

        return bvm.getContentsString();
    }


    /**
     *  Reformats the current source code
     */
    public static void extractMethod() {
        try {
            System.out.println("extract method #1");
            ElixirExtractMethod eem = new ElixirExtractMethod();
            System.out.println("extract method #2");
            eem.show();
            System.out.println("extract method #3");
        }
        catch (RefactoringException re) {
            JOptionPane.showMessageDialog(null, re.getMessage(), "Refactoring Exception",
                    JOptionPane.ERROR_MESSAGE);
        }
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


    /**
     *  Useful program that searches through the different panels to find the
     *  text panel that we can get the selected code from.
     *
     *@param  jPanel  Description of Parameter
     *@param  prefix  Description of Parameter
     *@return         Description of the Returned Value
     */
    private Component searchPanels(Container jPanel, String prefix) {
        int last = jPanel.getComponentCount();
        for (int ndx = 0; ndx < last; ndx++) {
            Component next = jPanel.getComponent(ndx);
            System.out.println(prefix + ":" + ndx + "  " + next.getClass().getName());
            if (next instanceof LineEditorPane) {
                return next;
            }
            if (next instanceof Container) {
                Component result = searchPanels((Container) next, prefix + ":" + ndx);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}
//  EOF
