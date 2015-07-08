package org.acm.seguin.ide.netbeans;

import javax.swing.*;
import org.acm.seguin.refactor.*;
import org.acm.seguin.uml.refactor.*;
import org.openide.*;
import org.openide.cookies.*;
import org.openide.nodes.*;
import org.openide.windows.*;

public class NetBeansExtractMethodDialog extends ExtractMethodDialog {

    private JEditorPane _editorPane;

    public NetBeansExtractMethodDialog() throws RefactoringException {
        super(null);
    }

    // (PENDING) This constructor doesn't work?
    public NetBeansExtractMethodDialog(EditorCookie editorCookie)
      throws RefactoringException {
        super(null);
        _editorPane = getCurrentEditorPane(editorCookie);

        if (_editorPane == null) {
            System.out.println("constructor: Editor pane is null!");
        }

    }

    protected String getSelectionFromIDE() {
        //return _editorPane.getSelectedText();
        return getCurrentEditorPane().getSelectedText();
    }

    /**
     * Gets the initial string from the IDE
     * @return    The file in string format
     */
    protected String getStringFromIDE() {
        //return _editorPane.getText();
        return getCurrentEditorPane().getText();
    }

    /**
     * Sets the string in the IDE
     * @param  value  The new file contained in a string
     */
    protected void setStringInIDE(String text) {
        //_editorPane.setText(text);
        getCurrentEditorPane().setText(text);
    }

    private JEditorPane getCurrentEditorPane() {
        return getCurrentEditorPane(null);
    }

    private JEditorPane getCurrentEditorPane(EditorCookie cookie) {
        TopComponent comp =
            TopManager.getDefault().getWindowManager().getRegistry().getActivated();
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

}
