package org.acm.seguin.ide.jedit;

import java.io.*;
import javax.swing.*;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.acm.seguin.ide.common.EditorOperations;

public class JEditEditorOperations extends EditorOperations {

    /**
     * Return the current text selected in the IDE
     *
     * @return currently selected text
     */
    public String getSelectionFromIDE() {
        return getCurrentView().getTextArea().getSelectedText();
    }

    /**
     * Returns true if the current file being edited is a java file
     *
     * @return true if the current file is a java file
     */
    public boolean isJavaFile() {
      return getCurrentView().getBuffer().getMode().getName().equals("java");
    }

    /**
     * Return the current File being edited
     *
     * @return Current file being edited or null if nothing selected
     */
    public File getFile() {
      return getCurrentView().getBuffer().getFile();

    }

    private View getCurrentView(){
      return jEdit.getLastView();
      //  OR ?
      //  return myEditAction.getView();
      }

    /**
     * Return the current line number
     *
     * @return The 1-based line number
     */
    public int getLineNumber() {
        return getCurrentView().getTextArea().getCaretLine()+1;
        /*
        return getCurrentView.getBuffer().virtualToPhysical(lineNo);
        int lineNo = getCurrentView().getTextArea().getCaretLine();
        */
    }


    /**
     * Sets the line number
     *
     * @param  value  New 1-based line number
     */
    public void setLineNumber(int lineNumber) {
        JEditTextArea textArea = getCurrentView().getTextArea();
        /*
          Using code from org.gjt.sp.jedit.textarea.JTextArea.showGoToLineDialog()
        */
		try
		{
			textArea.setCaretPosition(textArea.getLineStartOffset(lineNumber-1));
		}
		catch(Exception e)
		{
			textArea.getToolkit().beep();
		}
    }


    /**
     * Returns the frame that contains the editor. If this is not available or
     * you want dialog boxes to be centered on the screen return null from this
     * operation.
     *
     * @return  The frame that contains the editor
     */
    public JFrame getEditorFrame() {
        return (JFrame) getCurrentView();
    }

    /**
     * Return the text of the file being edited
     *
     * @return Text of file being edited
     */
    public String getStringFromIDE() {
        return getCurrentView().getTextArea().getText();
    }

    /**
     * Set the text of the file being edited
     *
     * @param  text
     */
    public void setStringInIDE(String text) {
        getCurrentView().getTextArea().setText(text);
    }

}
