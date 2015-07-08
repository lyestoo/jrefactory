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

import javax.swing.*;
import org.openide.cookies.*;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.*;
import org.openide.util.Lookup;
//import org.openide.cookies.EditorCookie;
import org.openide.windows.WindowManager;
import org.openide.windows.TopComponent;


import org.acm.seguin.ide.common.CodingStandardsViewer;
import org.acm.seguin.pmd.Report;
import org.acm.seguin.pmd.RuleContext;
import org.acm.seguin.pmd.RuleSetNotFoundException;
import org.acm.seguin.pmd.RuleViolation;
import org.acm.seguin.ide.netbeans.JRefactory;

/**
 *  Checks for Cut and paste in the current buffer
 *
 *@author     Mike Atkinson
 *@created    October 1, 2003
 */
public class CPDCheckBufferAction extends CookieAction {

    /**
     *  Gets the helpCtx attribute of the CPDCheckBufferAction object
     *
     *@return    The helpCtx value
     */
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
        // (PENDING) context help
        // return new HelpCtx (CPDCheckBufferAction.class);
    }


    /**
     *  Gets the menuPresenter attribute of the CPDCheckBufferAction object
     *
     *@return    The menuPresenter value
     */
//    public JMenuItem getMenuPresenter() {
//        JMenuItem item = new JMenuItem(getName());
//        item.addActionListener(this);
//        return item;
//    }


    /**
     *  Gets the name attribute of the CPDCheckBufferAction object
     *
     *@return    The name value
     */
    public String getName() {
        return NbBundle.getMessage(CPDCheckBufferAction.class,
                "LBL_CPDCheckBufferAction");
    }


    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    protected Class[] cookieClasses() {
        return new Class[]{EditorCookie.class};
    }


    /**
     *  Perform special enablement check in addition to the normal one.
     *
     *@param  nodes  Description of the Parameter
     *@return        Description of the Return Value
     */
    protected boolean enable(Node[] nodes) {
        if (!super.enable(nodes)) {
            return false;
        }
        // Any additional checks ...
        return true;
    }


    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    protected String iconResource() {
        return "CPDCheckBufferActionIcon.gif";
    }


    /**
     *  Perform extra initialization of this action's singleton. PLEASE do not
     *  use constructors for this purpose!
     */
    protected void initialize() {
        super.initialize();
        putProperty(CPDCheckBufferAction.SHORT_DESCRIPTION,
                NbBundle.getMessage(CPDCheckBufferAction.class,
                "HINT_CPDCheckBufferAction"));
    }


    /**
     *@return    MODE_EXACTLY_ONE
     */
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }


    /**
     *  Description of the Method
     *
     *@param  nodes  Description of the Parameter
     */
    protected void performAction(Node[] nodes) {
        EditorCookie cookie = (EditorCookie) nodes[0].getCookie(EditorCookie.class);
        JEditorPane pane = getCurrentEditorPane(cookie);
        if (pane != null) {
           RuleContext ctx = null;
           JRefactory refactory = JRefactory.jsPlugin;
           if (refactory != null) {
              CodingStandardsViewer csViewer = refactory.getCodingStandardsViewer();
              ctx = csViewer.check(refactory.getFrame(), pane);
           } else {
               System.err.println("WARNING: refactory==null");
           }
        } else {
            System.err.println("WARNING: pane==null");
        }
    }

    /**
     *  Sets the string in the IDE
     *
     *@param  text  The new StringInIDE value
     */
    protected void setStringInIDE(String text) {
        //_editorPane.setText(text);
        getCurrentEditorPane().setText(text);
    }


    /**
     *  Gets the CurrentEditorPane attribute of the NetBeansExtractMethodDialog
     *  object
     *
     *@return    The CurrentEditorPane value
     */
    private JEditorPane getCurrentEditorPane() {
        return getCurrentEditorPane(null);
    }


    /**
     *  Gets the CurrentEditorPane attribute of the NetBeansExtractMethodDialog
     *  object
     *
     *@param  cookie  Description of Parameter
     *@return         The CurrentEditorPane value
     */
    private JEditorPane getCurrentEditorPane(EditorCookie cookie) {
        WindowManager wm = (WindowManager)Lookup.getDefault().lookup(WindowManager.class);
        TopComponent comp = wm.getRegistry().getActivated();
        Node[] nodes = comp.getRegistry().getActivatedNodes();

        //(NOTE) This is a hack fix
        cookie = null;
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
     *  Gets the initial string from the IDE
     *
     *@return    The file in string format
     */
    protected String getStringFromIDE() {
        //return _editorPane.getText();
        JEditorPane pane = getCurrentEditorPane();
        return (pane==null) ? "" : pane.getText();
    }
}

