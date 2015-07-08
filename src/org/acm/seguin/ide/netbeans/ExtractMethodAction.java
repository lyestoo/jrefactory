package org.acm.seguin.ide.netbeans;

import javax.swing.*;
import org.acm.seguin.refactor.*;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import org.openide.cookies.*;

public class ExtractMethodAction extends CookieAction {

    protected Class[] cookieClasses () {
        return new Class[] { EditorCookie.class };
    }

    protected int mode () {
        return MODE_EXACTLY_ONE;
    }

    protected void performAction(Node[] nodes) {
        EditorCookie cookie =
                (EditorCookie) nodes[0].getCookie(EditorCookie.class);
        try {
            // (new NetBeansExtractMethodDialog(cookie)).show();
            (new NetBeansExtractMethodDialog()).show();
        } catch (RefactoringException re) {
            //(PENDING) NetBeans specific exception
            JOptionPane.showMessageDialog(null, re.getMessage(), 
             "Refactoring Exception", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String getName () {
        return NbBundle.getMessage(ExtractMethodAction.class, "LBL_ExtractMethodAction");
    }

    protected String iconResource () {
        return null;
    }

    public HelpCtx getHelpCtx () {
        return HelpCtx.DEFAULT_HELP;
        // return new HelpCtx (ExtractMethodAction.class);
    }

    /** 
     * Perform special enablement check in addition to the normal one.
     */
    protected boolean enable(Node[] nodes) {
        if (!super.enable(nodes)) {
            return false;
        }
        // Any additional checks ...
        return true;
    }

    /**
     *  Perform extra initialization of this action's singleton. PLEASE do not
     *  use constructors for this purpose!
     */
    protected void initialize() {
        super.initialize();
        putProperty(PrettyPrinterAction.SHORT_DESCRIPTION,
                NbBundle.getMessage(ExtractMethodAction.class,
                "HINT_ExtractMethodAction"));
    }

}
