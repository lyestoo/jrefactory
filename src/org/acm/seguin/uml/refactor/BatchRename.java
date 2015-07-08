package org.acm.seguin.uml.refactor;


import java.awt.event.ActionEvent;
import java.util.Iterator;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import org.acm.seguin.awt.ExceptionPrinter;
import org.acm.seguin.refactor.Refactoring;
import org.acm.seguin.refactor.RefactoringException;
import org.acm.seguin.refactor.RefactoringFactory;
import org.acm.seguin.refactor.field.RenameFieldRefactoring;
import org.acm.seguin.refactor.method.RenameParameterRefactoring;
import org.acm.seguin.summary.*;
import org.acm.seguin.uml.PopupMenuListener;
import org.acm.seguin.uml.loader.ReloaderSingleton;


/**
 *  Performs the hungarian name renaming to an entire class
 *
 *@author     Chris Seguin
 *@created    July 16, 2002
 */
public class BatchRename extends PopupMenuListener
{
    private TypeSummary type;


    /**
     *  Constructor for the BatchRename object
     *
     *@param  initMenu     The popup menu
     *@param  initItem     The current item
     *@param  typeSummary  Description of the Parameter
     */
    public BatchRename(JPopupMenu initMenu, JMenuItem initItem, TypeSummary typeSummary)
    {
        super(initMenu, initItem);
        type = typeSummary;
    }


    /**
     *  A menu item has been selected, display the dialog box
     *
     *@param  evt  the action event
     */
    public void actionPerformed(ActionEvent evt)
    {
        super.actionPerformed(evt);
        run(type);

        //  Update the GUIs
        ReloaderSingleton.reload();
    }


    /**
     *  Main processing method for the BatchRename object
     *
     *@param  typeSummary  Description of the Parameter
     */
    public void run(TypeSummary typeSummary)
    {
        //  Over the fields
        Iterator iter = typeSummary.getFields();
        if (iter != null) {
            while (iter.hasNext()) {
                FieldSummary nextFieldSummary = (FieldSummary) iter.next();
                System.out.println("Renaming:  " + nextFieldSummary.getName());
                try {
                    renameField(typeSummary, nextFieldSummary);
                }
                catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        }

        //  Over the methods
        iter = typeSummary.getMethods();
        if (iter != null) {
            while (iter.hasNext()) {
                MethodSummary next = (MethodSummary) iter.next();
                visit(next);
            }
        }

        //  Over the types
        iter = typeSummary.getTypes();
        if (iter != null) {
            while (iter.hasNext()) {
                TypeSummary next = (TypeSummary) iter.next();
                run(next);
            }
        }
    }


    /**
     *  Renames a field to use hungarian notation
     *
     *@param  fieldSummary  Description of the Parameter
     *@param  typeSummary   Description of the Parameter
     */
    private void renameField(TypeSummary typeSummary, FieldSummary fieldSummary)
    {
        HungarianNamer namer = new HungarianNamer();
        String newName = namer.getDefaultName(fieldSummary, "m_");

        if (newName.equals(fieldSummary.getName()))
        	return;

        RenameFieldRefactoring rfr = RefactoringFactory.get().renameField();
        rfr.setClass(typeSummary);
        rfr.setField(fieldSummary.getName());
        rfr.setNewName(newName);
        runRefactoring(rfr);
    }


    /**
     *  Renames a parameter to have hungarian notation
     *
     *@param  method  Description of the Parameter
     *@param  param   Description of the Parameter
     */
    private void renameParameter(MethodSummary method, ParameterSummary param)
    {
        HungarianNamer namer = new HungarianNamer();
        RenameParameterRefactoring rpr = RefactoringFactory.get().renameParameter();
        rpr.setMethodSummary(method);
        rpr.setParameterSummary(param);
        rpr.setNewName(namer.getDefaultName(param, "a_"));
        runRefactoring(rpr);
    }



    /**
     *  Description of the Method
     *
     *@param  refactoring  Description of the Parameter
     */
    private void runRefactoring(Refactoring refactoring)
    {
        //  Update the code
        try {
            refactoring.run();
        }
        catch (RefactoringException re) {
            JOptionPane.showMessageDialog(null, re.getMessage(), "Refactoring Exception",
                    JOptionPane.ERROR_MESSAGE);
        }
        catch (Throwable thrown) {
            ExceptionPrinter.print(thrown, true);
        }
    }


    /**
     *  Description of the Method
     *
     *@param  methodSummary  Description of the Parameter
     */
    private void visit(MethodSummary methodSummary)
    {
        Iterator iter = methodSummary.getParameters();
        if (iter != null) {
            while (iter.hasNext()) {
                ParameterSummary nextParameterSummary = (ParameterSummary) iter.next();
                System.out.println("Renaming:  " + nextParameterSummary.getName());
                try {
                    renameParameter(methodSummary, nextParameterSummary);
                }
                catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        }
    }
}
