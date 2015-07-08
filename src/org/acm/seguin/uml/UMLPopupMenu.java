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
package org.acm.seguin.uml;

import java.util.Iterator;
import javax.swing.*;
import org.acm.seguin.ide.common.SourceBrowser;
import org.acm.seguin.ide.common.SourceBrowserAdapter;
import org.acm.seguin.summary.ParameterSummary;
import org.acm.seguin.summary.MethodSummary;
import org.acm.seguin.summary.PackageSummary;
import org.acm.seguin.summary.TypeSummary;
import org.acm.seguin.uml.refactor.AddChildClassListener;
import org.acm.seguin.uml.refactor.AddMoveClassListener;
import org.acm.seguin.uml.refactor.AddParentClassListener;
import org.acm.seguin.uml.refactor.AddRenameClassListener;
import org.acm.seguin.uml.refactor.DialogViewListener;
import org.acm.seguin.uml.refactor.ExtractInterfaceListener;
import org.acm.seguin.uml.refactor.MoveMethodListener;
import org.acm.seguin.uml.refactor.PushDownFieldListener;
import org.acm.seguin.uml.refactor.RenameFieldListener;
import org.acm.seguin.uml.refactor.PushDownMethodListener;
import org.acm.seguin.uml.refactor.PushUpAbstractMethodListener;
import org.acm.seguin.uml.refactor.PushUpFieldListener;
import org.acm.seguin.uml.refactor.PushUpMethodListener;
import org.acm.seguin.uml.refactor.RemoveClassListener;
import org.acm.seguin.uml.refactor.RenameParameterListener;
import org.acm.seguin.uml.refactor.BatchRename;

/**
 *  UMLPopupMenu
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public class UMLPopupMenu {
    //  Instance Variables
    private JPopupMenu popupMenu;
    private JPanel activeComponent;
    private UMLPackage current;


    /**
     *  Constructor for the UMLPopupMenu object
     *
     *@param  top   the package diagram
     *@param  init  the specific panel
     */
    public UMLPopupMenu(UMLPackage top, JPanel init) {
        //  Set the variables
        activeComponent = init;
        current = top;

        popupMenu = createPopupMenu();
        popupMenu.setInvoker(activeComponent);
    }


    /**
     *  Get the popup menu
     *
     *@return    the popup menu
     */
    public JPopupMenu getMenu() {
        return popupMenu;
    }


    /**
     *  Add in the metrics
     *
     *@param  menu  Description of Parameter
     *@return       the metrics
     */
    protected JMenuItem getMetricsMenu(JPopupMenu menu) {
        //  Add in metrics
        JMenu metrics = new JMenu("Metrics");

        JMenuItem item = new JMenuItem("Project Metrics");
        metrics.add(item);
        ProjectMetricsListener projectML = new ProjectMetricsListener(menu, item);
        item.addMouseListener(projectML);
        item.addActionListener(projectML);

        item = new JMenuItem("Package Metrics");
        metrics.add(item);
        PackageMetricsListener packageML = new PackageMetricsListener(current, menu, item);
        item.addMouseListener(packageML);
        item.addActionListener(packageML);

        item = new JMenuItem("Class Metrics");
        metrics.add(item);
        TypeMetricsListener tml = new TypeMetricsListener(activeComponent, menu, item);
        item.addMouseListener(tml);
        item.addActionListener(tml);

        if (activeComponent == null) {
            //  Do nothing
        } else if (activeComponent instanceof UMLMethod) {
            UMLMethod umlMethod = (UMLMethod) activeComponent;
            item = new JMenuItem("Method Metrics");
            metrics.add(item);
            MethodMetricsListener listener =
                    new MethodMetricsListener(umlMethod.getSummary(), menu, item);
            item.addMouseListener(listener);
            item.addActionListener(listener);
        }

        //  Return the value
        return metrics;
    }


    /**
     *  Create the popup menu
     *
     *@return    Description of the Returned Value
     */
    protected JPopupMenu createPopupMenu() {
        JMenuItem item;
        JPopupMenu menu = new JPopupMenu("UML Diagram");

        if (activeComponent == null) {
            //  Do nothing
        } else if (activeComponent instanceof UMLField) {
            UMLField field = (UMLField) activeComponent;

            if (field.isAssociation()) {
                item = new JMenuItem("Convert to Attribute");
            } else {
                item = new JMenuItem("Convert to Association");
            }
            item.setEnabled(field.isConvertable());
            menu.add(item);
            item.addMouseListener(new PopupMenuListener(menu, item));
            item.addActionListener(new ConvertAdapter(current, field));
            menu.addSeparator();
        }

        //  Add refactorings
        addRefactorings(menu);
        menu.addSeparator();

        //  Add metrics
        item = getMetricsMenu(menu);
        menu.add(item);

        // Add source link
        if (SourceBrowser.get().canBrowseSource()) {
            menu.addSeparator();
            item = new JMenuItem("Show source");
            SourceBrowserAdapter adapter =
                    new SourceBrowserAdapter((ISourceful) activeComponent);
            item.addActionListener(adapter);
            menu.add(item);
        }

        //  Return the menu
        return menu;
    }


    /**
     *  Refactorings
     *
     *@param  menu  The feature to be added to the Refactorings attribute
     */
    protected void addRefactorings(JPopupMenu menu) {
        addTypeRefactorings(menu);

        if (activeComponent == null) {
            //  Do nothing
        } else if (activeComponent instanceof UMLMethod) {
            addMethodRefactorings(menu);
        } else if (activeComponent instanceof UMLField) {
            addFieldRefactorings(menu);
        }
    }


    /**
     *  Gets the Type attribute of the UMLPopupMenu object
     *
     *@return    The Type value
     */
    private UMLType getType() {
        if (activeComponent instanceof UMLType) {
            return (UMLType) activeComponent;
        } else if (activeComponent instanceof UMLLine) {
            return ((UMLLine) activeComponent).getParentType();
        }
        return null;
    }


    /**
     *  Gets the Type attribute of the UMLPopupMenu object
     *
     *@return    The Type value
     */
    private TypeSummary getTypeSummary() {
        UMLType umlType = getType();
        if (umlType == null) {
            return null;
        }

        return umlType.getSummary();
    }


    /**
     *  Adds a feature to the FieldRefactorings attribute of the UMLPopupMenu
     *  object
     *
     *@param  menu  The feature to be added to the FieldRefactorings attribute
     */
    private void addFieldRefactorings(JPopupMenu menu) {
        //  Add in metrics
        JMenu fieldRefactorings = new JMenu("Field Refactorings");
        menu.add(fieldRefactorings);

        JMenuItem item = new JMenuItem("Rename");
        fieldRefactorings.add(item);
        item.setEnabled(true);
        RenameFieldListener renameListener = new RenameFieldListener(current,
                ((UMLField) activeComponent).getSummary(),
                menu,
                item);
        item.addMouseListener(renameListener);
        item.addActionListener(renameListener);

        item = new JMenuItem("Push Up");
        fieldRefactorings.add(item);
        item.setEnabled(true);
        PushUpFieldListener pushUpListener = new PushUpFieldListener(
                current,
                getTypeSummary(),
                ((UMLField) activeComponent).getSummary(),
                menu,
                item);
        item.addMouseListener(pushUpListener);
        item.addActionListener(pushUpListener);

        item = new JMenuItem("Push Down");
        fieldRefactorings.add(item);
        item.setEnabled(true);
        PushDownFieldListener pushDownListener = new PushDownFieldListener(
                current,
                getTypeSummary(),
                ((UMLField) activeComponent).getSummary(),
                menu,
                item);
        item.addMouseListener(pushDownListener);
        item.addActionListener(pushDownListener);
    }


    /**
     *  Adds a feature to the MethodRefactorings attribute of the UMLPopupMenu
     *  object
     *
     *@param  menu  The feature to be added to the MethodRefactorings attribute
     */
    private void addMethodRefactorings(JPopupMenu menu) {
        MethodSummary methodSummary = ((UMLMethod) activeComponent).getSummary();

        //  Add in metrics
        JMenu methodRefactorings = new JMenu("Method Refactorings");
        menu.add(methodRefactorings);

        JMenuItem item = new JMenuItem("Rename");
        item.setEnabled(false);
        methodRefactorings.add(item);
        item.addMouseListener(new PopupMenuListener(menu, item));

        item = new JMenuItem("Push Up");
        methodRefactorings.add(item);
        item.setEnabled(true);
        PushUpMethodListener pushUpListener = new PushUpMethodListener(
                current,
                ((UMLMethod) activeComponent).getSummary(),
                menu,
                item);
        item.addMouseListener(pushUpListener);
        item.addActionListener(pushUpListener);

        item = new JMenuItem("Push Up (Abstract)");
        methodRefactorings.add(item);
        item.setEnabled(true);
        PushUpAbstractMethodListener pushUpAbsListener = new PushUpAbstractMethodListener(
                current,
                ((UMLMethod) activeComponent).getSummary(),
                menu,
                item);
        item.addMouseListener(pushUpAbsListener);
        item.addActionListener(pushUpAbsListener);

        item = new JMenuItem("Push Down");
        methodRefactorings.add(item);
        item.setEnabled(true);
        PushDownMethodListener pushDownListener = new PushDownMethodListener(
                current,
                getTypeSummary(),
                methodSummary,
                menu,
                item);
        item.addMouseListener(pushDownListener);
        item.addActionListener(pushDownListener);

        item = new JMenuItem("Move Method");
        methodRefactorings.add(item);

        item.setEnabled(methodSummary.getParameterCount() > 0);
        MoveMethodListener moveListener = new MoveMethodListener(
                current,
                getTypeSummary(),
                methodSummary,
                menu,
                item);
        item.addMouseListener(moveListener);
        item.addActionListener(moveListener);

        if (methodSummary.getParameterCount() == 0) {
            item = new JMenuItem("Rename Parameters");
            item.setEnabled(false);
            methodRefactorings.add(item);
        } else {
            JMenu rename = new JMenu("Rename Parameter:");
            methodRefactorings.add(rename);

            Iterator iter = methodSummary.getParameters();
            while (iter.hasNext()) {
                ParameterSummary next = (ParameterSummary) iter.next();
                item = new JMenuItem(next.getName());
                rename.add(item);

                RenameParameterListener rpl = new RenameParameterListener(
                        menu,
                        item,
                        current,
                        next);
                item.addMouseListener(rpl);
                item.addActionListener(rpl);
            }
        }
    }


    /**
     *  Adds a feature to the TypeRefactorings attribute of the UMLPopupMenu
     *  object
     *
     *@param  menu  The feature to be added to the TypeRefactorings attribute
     */
    private void addTypeRefactorings(JPopupMenu menu) {
        TypeSummary[] typeArray = SelectedSummaryList.list(current, getType());

        //  Add in metrics
        JMenu typeRefactorings = new JMenu("Type Refactorings");
        menu.add(typeRefactorings);

        JMenuItem item = new JMenuItem("Rename Class");
        typeRefactorings.add(item);
        DialogViewListener rcl = new AddRenameClassListener(
                current,
                getTypeSummary(),
                menu,
                item);
        item.addMouseListener(rcl);
        item.addActionListener(rcl);

        //  Add in moving a class
        item = new JMenuItem("Move Class To");
        item.setEnabled(true);
        typeRefactorings.add(item);
        DialogViewListener ncl = new AddMoveClassListener(
                typeArray,
                menu,
                item);
        item.addMouseListener(ncl);
        item.addActionListener(ncl);

        //  Add in a parent class
        item = new JMenuItem("Add Abstract Parent Class");
        item.setEnabled(true);
        typeRefactorings.add(item);
        DialogViewListener aapl = new AddParentClassListener(
                current,
                typeArray,
                menu,
                item);
        item.addMouseListener(aapl);
        item.addActionListener(aapl);

        //  Add in a child class
        item = new JMenuItem("Add Child Class");
        typeRefactorings.add(item);
        item.setEnabled(true);
        DialogViewListener accl = new AddChildClassListener(
                current,
                getTypeSummary(),
                menu,
                item);
        item.addMouseListener(accl);
        item.addActionListener(accl);

        //  Remove a child class
        item = new JMenuItem("Remove Class");
        typeRefactorings.add(item);
        item.setEnabled(true);
        RemoveClassListener removeListener = new RemoveClassListener(
                current,
                getTypeSummary(),
                menu,
                item);
        item.addMouseListener(removeListener);
        item.addActionListener(removeListener);

        //  Extract Interface
        item = new JMenuItem("Extract Interface");
        typeRefactorings.add(item);
        item.setEnabled(true);
        DialogViewListener eil = new ExtractInterfaceListener(
                current,
                typeArray,
                menu,
                item);
        item.addMouseListener(eil);
        item.addActionListener(eil);

        //  Extract Interface
        item = new JMenuItem("Rename Variables Using Hungarian Notation");
        typeRefactorings.add(item);
        item.setEnabled(true);
        BatchRename brl = new BatchRename(
                menu,
                item,
                getTypeSummary());
        item.addMouseListener(brl);
        item.addActionListener(brl);
    }
}
