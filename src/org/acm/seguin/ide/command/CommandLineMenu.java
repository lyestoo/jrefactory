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
package org.acm.seguin.ide.command;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.event.MenuListener;
import javax.swing.event.MenuEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.acm.seguin.ide.common.UndoAdapter;
import org.acm.seguin.io.Saveable;
import org.acm.seguin.uml.SaveMenuSelection;
import org.acm.seguin.uml.UMLPackage;
import org.acm.seguin.uml.jpg.SaveAdapter;
import org.acm.seguin.uml.line.LinedPanel;
import org.acm.seguin.uml.print.PrintAdapter;
import org.acm.seguin.uml.print.PrintSetupAdapter;
import org.acm.seguin.refactor.undo.UndoStack;


/**
 *  Creates the menubar for the command line program
 *
 *@author     Chris Seguin
 *@author     <a href="mailto:JRefactory@ladyshot.demon.co.uk">Mike Atkinson</a>
 *@version    $Id: CommandLineMenu.java,v 1.5 2003/07/29 20:51:52 mikeatkinson Exp $
 *@created    October 18, 2001
 */
public class CommandLineMenu {
    private JMenuItem undoMenuItem;
    /**
     *  Gets the MenuBar attribute of the CommandLineMenu object
     *
     *@param  panel  Description of Parameter
     *@return        The MenuBar value
     */
    public JMenuBar getMenuBar(JPanel panel) {
        JMenuBar menubar = new JMenuBar();
        menubar.add(createFileMenu(panel));
        menubar.add(createEditMenu());

        if (panel instanceof LinedPanel) {
            menubar.add(createZoomMenu(panel));
        }

        if (panel instanceof UMLPackage) {
            menubar.add(createRearangeMenu(panel));
        }

        return menubar;
    }


    /**
     *  Creates edit menu
     *
     *@return    returns the edit menu
     */
    private JMenu createEditMenu() {
        JMenu editMenu = new JMenu("Edit");
        editMenu.addMenuListener( new MenuListener() {
            public void menuCanceled(MenuEvent e) {}
            public void menuDeselected(MenuEvent e) {}
            public void menuSelected(MenuEvent e) {
               undoMenuItem.setEnabled(!UndoStack.get().isStackEmpty());
            }
        });
        undoMenuItem = new JMenuItem("Undo Refactoring");
        undoMenuItem.addActionListener(new UndoAdapter());
        editMenu.add(undoMenuItem);
        return editMenu;
    }


    /**
     *  Creates the file menu
     *
     *@param  panel  the panel
     *@return        the file menu
     */
    private JMenu createFileMenu(JPanel panel) {
        JMenu fileMenu = new JMenu("File");

        JMenuItem saveMenuItem = new JMenuItem("Save");
        if (panel instanceof Saveable) {
            saveMenuItem.addActionListener(new SaveMenuSelection((Saveable) panel));
        }
        else {
            saveMenuItem.setEnabled(false);
        }
        fileMenu.add(saveMenuItem);

        JMenuItem jpgMenuItem = new JMenuItem("JPG");
        if (panel instanceof UMLPackage) {
            jpgMenuItem.addActionListener(new SaveAdapter((UMLPackage) panel));
        }
        else {
            jpgMenuItem.setEnabled(false);
        }
        fileMenu.add(jpgMenuItem);

        fileMenu.addSeparator();

        JMenuItem printSetupMenuItem = new JMenuItem("Print Setup");
        printSetupMenuItem.addActionListener(new PrintSetupAdapter());
        fileMenu.add(printSetupMenuItem);

        JMenuItem printMenuItem = new JMenuItem("Print");
        if (panel instanceof UMLPackage) {
            printMenuItem.addActionListener(new PrintAdapter((UMLPackage) panel));
        }
        else {
            printMenuItem.setEnabled(false);
        }
        fileMenu.add(printMenuItem);

        fileMenu.addSeparator();

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ExitMenuSelection());
        fileMenu.add(exitMenuItem);
        return fileMenu;
    }


    /**
     *  Creates the zoom menu
     *
     *@param  panel  the panel
     *@return        the zoom menu
     */
    private JMenu createZoomMenu(JPanel panel) {
        LinedPanel linedPanel = (LinedPanel) panel;
        JMenu zoomMenu = new JMenu("Zoom");
        JMenuItem tenPercent = new JMenuItem("10%");
        tenPercent.addActionListener(new ZoomAdapter(linedPanel, 0.1));
        zoomMenu.add(tenPercent);
        JMenuItem twentyFivePercent = new JMenuItem("25%");
        twentyFivePercent.addActionListener(new ZoomAdapter(linedPanel, 0.25));
        zoomMenu.add(twentyFivePercent);
        JMenuItem thirtyThreePercent = new JMenuItem("33%");
        thirtyThreePercent.addActionListener(new ZoomAdapter(linedPanel, 0.33));
        zoomMenu.add(thirtyThreePercent);
        JMenuItem fiftyPercent = new JMenuItem("50%");
        fiftyPercent.addActionListener(new ZoomAdapter(linedPanel, 0.5));
        zoomMenu.add(fiftyPercent);
        JMenuItem sixtySixPercent = new JMenuItem("66%");
        sixtySixPercent.addActionListener(new ZoomAdapter(linedPanel, 0.66));
        zoomMenu.add(sixtySixPercent);
        JMenuItem normal = new JMenuItem("100%");
        normal.addActionListener(new ZoomAdapter(linedPanel, 1.0));
        zoomMenu.add(normal);
        JMenuItem twoHunderd = new JMenuItem("200%");
        twoHunderd.addActionListener(new ZoomAdapter(linedPanel, 2.0));
        zoomMenu.add(twoHunderd);
        return zoomMenu;
    }


    /**
     *  Creates the zoom menu
     *
     *@param  panel  the panel
     *@return        the zoom menu
     */
    private JMenu createRearangeMenu(JPanel panel) {
        final UMLPackage linedPanel = (UMLPackage) panel;
        JMenu rearangeMenu = new JMenu("Rearange");
        JMenuItem rearange = new JMenuItem("Rearange UML");
        rearange.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                linedPanel.rearragePositions(2000, 50, 1.0);
                linedPanel.reset();
            }
        });
        rearangeMenu.add(rearange);
        
        JMenuItem nudge = new JMenuItem("Nudge UML");
        nudge.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                linedPanel.rearragePositions(10, 50, 0.2);
                linedPanel.reset();
            }
        });
        rearangeMenu.add(nudge);
        return rearangeMenu;
    }
}
//  EOF
