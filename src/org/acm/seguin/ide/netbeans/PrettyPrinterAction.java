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

/**
 *  Applies the JRefactory pretty printer to the currently selected editor. Will
 *  only be applied if only one editor is selected.
 *
 *@author     unknown
 *@created    October 18, 2001
 */
public class PrettyPrinterAction extends CookieAction implements Presenter.Menu {

    /**
     *  Gets the helpCtx attribute of the PrettyPrinterAction object
     *
     *@return    The helpCtx value
     */
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
        // (PENDING) context help
        // return new HelpCtx (PrettyPrinterAction.class);
    }


    /**
     *  Gets the menuPresenter attribute of the PrettyPrinterAction object
     *
     *@return    The menuPresenter value
     */
    public JMenuItem getMenuPresenter() {
        JMenuItem item = new JMenuItem(getName());
        item.addActionListener(this);
        return item;
    }


    /**
     *  Gets the name attribute of the PrettyPrinterAction object
     *
     *@return    The name value
     */
    public String getName() {
        return NbBundle.getMessage(PrettyPrinterAction.class,
                "LBL_PrettyPrinterAction");
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
        return null;
    }


    /**
     *  Perform extra initialization of this action's singleton. PLEASE do not
     *  use constructors for this purpose!
     */
    protected void initialize() {
        super.initialize();
        putProperty(PrettyPrinterAction.SHORT_DESCRIPTION,
                NbBundle.getMessage(PrettyPrinterAction.class,
                "HINT_PrettyPrinterAction"));
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
        EditorCookie cookie =
                (EditorCookie) nodes[0].getCookie(EditorCookie.class);
//(PENDING) check for null editor pane
        NetBeansPrettyPrinter prettyPrinter = new NetBeansPrettyPrinter(cookie);
        prettyPrinter.prettyPrintCurrentWindow();
    }

}
//  EOF
