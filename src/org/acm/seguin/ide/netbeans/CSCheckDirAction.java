/*
 * CSCheckAllBuffersAction.java
 *
 * Created on 01 October 2003, 22:09
 */

package org.acm.seguin.ide.netbeans;

import javax.swing.*;
import org.openide.cookies.*;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.*;

/**
 *  Check all the Java files in the selected directory.
 *
 *@author     unknown
 *@created    October 18, 2001
 */
public class CSCheckDirAction extends CallableSystemAction {

    /**
     *  Gets the helpCtx attribute of the PrettyPrinterAction object
     *
     *@return    The helpCtx value
     */
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
        // (PENDING) context help
        // return new HelpCtx (CSCheckDirAction.class);
    }


    /**
     *  Gets the name attribute of the CSCheckDirAction object
     *
     *@return    The name value
     */
    public String getName() {
        return NbBundle.getMessage(CSCheckDirAction.class, "LBL_CSCheckDirAction");
    }


    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    protected String iconResource() {
        return "CSCheckDirActionIcon.gif";
    }


    /**
     *  Perform extra initialization of this action's singleton. PLEASE do not
     *  use constructors for this purpose!
     */
    protected void initialize() {
        super.initialize();
        putProperty(CSCheckDirAction.SHORT_DESCRIPTION,
                NbBundle.getMessage(CSCheckDirAction.class,
                "HINT_CSCheckDirAction"));
    }


    /**
     *  Description of the Method
     *
     *@param  nodes  Description of the Parameter
     */
    public void performAction() {

//(PENDING) check for null editor pane
        //NetBeansPrettyPrinter prettyPrinter = new NetBeansPrettyPrinter(cookie);
        //prettyPrinter.prettyPrintCurrentWindow();
    }

}