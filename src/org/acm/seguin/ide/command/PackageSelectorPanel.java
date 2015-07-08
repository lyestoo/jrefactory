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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import org.acm.seguin.ide.common.PackageSelectorArea;
import org.acm.seguin.io.Saveable;
import org.acm.seguin.summary.PackageSummary;
import org.acm.seguin.summary.SummaryTraversal;
import org.acm.seguin.uml.UMLPackage;
import org.acm.seguin.uml.loader.Reloader;
import org.acm.seguin.uml.loader.ReloaderSingleton;

/**
 *  Creates a panel for the selection of packages to view.
 *
 *@author     Chris Seguin
 *@author     Mike Atkinson
 *@created    August 10, 1999
 */
public class PackageSelectorPanel extends PackageSelectorArea
         implements ActionListener, Saveable, Reloader, Runnable {
    protected JPanel buttons;
    protected JPanel panel;
    protected JFrame frame;

    //  Class Variables
    public static PackageSelectorPanel mainPanel;
    /**
     *  The root directory
     */
    protected String rootDir = null;

    //  Instance Variables
    protected HashMap viewList;


    /**
     *  Constructor for the PackageSelectorPanel object
     *
     *@param  root  The root directory
     */
    protected PackageSelectorPanel(String root) {
        super();
        setRootDirectory(root);  //  Setup the instance variables

        ReloaderSingleton.register(this);
        ReloaderSingleton.reload();
        buttons = createButtons(this);
        panel = createMainPanel();
        frame = createFrame();
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = listbox.locationToIndex(e.getPoint());
                    //System.out.println("Double clicked on Item " + index);
                    PackageSummary next = (PackageSummary) listbox.getModel().getElementAt(index);
                    showSummary(next);
                 }
            }
        };
        listbox.addMouseListener(mouseListener);
    }


    /**
     *  Set the root directory
     *
     *@param  root  the new root directory
     */
    protected void setRootDirectory(String root) {
        if (root == null) {
            rootDir = System.getProperty("user.dir");
        }
        else {
            rootDir = root;
        }
    }


    /**
     *  Get the main panel (as a window).
     *
     *@param  directory  Description of Parameter
     *@return            The MainPanel value
     */
    public static PackageSelectorPanel getMainPanel(String directory) {
        if (mainPanel == null) {
            if (directory == null) {
                return null;
            }

            mainPanel = new PackageSelectorPanel(directory);
        }
        return mainPanel;
    }

    /**
     *  Get the main panel.
     *
     *@return            The main panel
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     *  Get the main panel (as a window), setting it visible.
     *
     *@param  directory  Description of Parameter
     *@return            The MainPanel value
     */
    public static PackageSelectorPanel openMainFrame(String directory) {
        getMainPanel(directory);
        mainPanel.setVisible(true);
        return mainPanel;
    }


    /**
     *  Get the package from the central store
     *
     *@param  summary  The package summary that we are looking for
     *@return          The UML package
     */
    protected UMLFrame getPackage(PackageSummary summary) {
        return (UMLFrame) viewList.get(summary);
    }


    /**
     *  Handle the button press events
     *
     *@param  evt  the event
     */
    public void actionPerformed(ActionEvent evt) {
        String command = evt.getActionCommand();
        if (command.equals("Show")) {
            Object[] selection = listbox.getSelectedValues();
            for (int ndx = 0; ndx < selection.length; ndx++) {
                PackageSummary next = (PackageSummary) selection[ndx];
                showSummary(next);
            }
        }
        else if (command.equals("Hide")) {
            Object[] selection = listbox.getSelectedValues();
            for (int ndx = 0; ndx < selection.length; ndx++) {
                PackageSummary next = (PackageSummary) selection[ndx];
                hideSummary(next);
            }
        }
        else if (command.equals("Reload")) {
            ReloaderSingleton.reload();
        }
    }


    /**
     *  Add package to central store
     *
     *@param  summary  the summary we are adding
     *@param  view     the associated view
     */
    protected void addPackage(PackageSummary summary, UMLFrame view) {
        viewList.put(summary, view);
    }


    /**
     *  Creates the frame
     */
    protected JFrame createFrame() {
        JFrame frame = new JFrame("Package Selector");

        frame.getContentPane().add(panel);

        CommandLineMenu clm = new CommandLineMenu();
        frame.setJMenuBar(clm.getMenuBar(this));
        frame.addWindowListener(new ExitMenuSelection());
        frame.setSize(350, 350);
        frame.setVisible(true);
        return frame;
    }

    /**
     *  Creates the content panel
     */
    protected JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JScrollPane scrollPane = getScrollPane();
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.EAST);
        return panel;
    }

    /**
     *  Creates a new view
     *
     *@param  packageSummary  The packages summary
     */
    private void createNewView(PackageSummary packageSummary) {
        UMLFrame frame = new UMLFrame(packageSummary);
        addPackage(packageSummary, frame);
    }


    /**
     *  Hide the summary
     *
     *@param  packageSummary  the summary to hide
     */
    private void hideSummary(PackageSummary packageSummary) {
        UMLFrame view = getPackage(packageSummary);
        view.setVisible(false);
    }


    /**
     *  Loads the packages into the listbox and refreshes the UML diagrams
     */
    public void loadPackages() {
        new Thread(this).start();
    }
    
    public void run() {
        try {
            loadSummaries();
    
            super.loadPackages();
    
            javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    //  Reloads the screens
                    UMLPackage view = null;
                    PackageSummary packageSummary = null;
            
                    if (viewList == null) {
                        viewList = new HashMap();
                        return;
                    }
    
                    Iterator iter = viewList.keySet().iterator();
                    while (iter.hasNext()) {
                        packageSummary = (PackageSummary) iter.next();
                        view = getPackage(packageSummary).getUmlPackage();
                        view.reload();
                    }
                }
            });
        } catch (Exception e) {
        }
    }


    /**
     *  Load the summaries
     */
    public void loadSummaries() {
        //  Load the summaries
        (new SummaryTraversal(rootDir)).run();
    }


    /**
     *  Main program for testing purposes
     *
     *@param  args  The command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Syntax:  java org.acm.seguin.uml.PackageSelectorPanel <dir>");
            return;
        }

        PackageSelectorPanel panel = PackageSelectorPanel.openMainFrame(args[0]);
        ReloaderSingleton.register(panel);
    }


    /**
     *  Reloads the package information
     */
    public void reload() {
        loadPackages();
    }


    /**
     *  Saves the diagrams
     *
     *@exception  IOException  Description of Exception
     */
    public void save() throws IOException {
        Iterator iter = viewList.keySet().iterator();
        while (iter.hasNext()) {
            PackageSummary packageSummary = (PackageSummary) iter.next();
            UMLPackage view = getPackage(packageSummary).getUmlPackage();
            view.save();
        }
    }


    /**
     *  Shows the summary
     *
     *@param  packageSummary  the summary to show
     */
    private void showSummary(PackageSummary packageSummary) {
        UMLFrame view = getPackage(packageSummary);
        if ((view == null) && (packageSummary.getFileSummaries() != null)) {
            createNewView(packageSummary);
        }
        else if (packageSummary.getFileSummaries() == null) {
            //  Nothing to view
        }
        else {
            view.getUmlPackage().reload();
            view.setVisible(true);
        }
    }


    protected JPanel createButtons(ActionListener listener) {
        return new ButtonPanel(listener);
    }
    
    /**
     *  Description of the Class
     *
     *@author     Chris Seguin
     *@created    October 18, 2001
     */
    private static class ButtonPanel extends JPanel {
        private ActionListener listener;


        /**
         *  Constructor for the ButtonPanel object
         *
         *@param  listener  Description of Parameter
         */
        public ButtonPanel(ActionListener listener) {
            this.listener = listener;
            init();
            this.setSize(getPreferredSize());
        }


        /**
         *  Gets the MaximumSize attribute of the ButtonPanel object
         *
         *@return    The MaximumSize value
         */
        public Dimension getMaximumSize() {
            return getPreferredSize();
        }


        /**
         *  Gets the PreferredSize attribute of the ButtonPanel object
         *
         *@return    The PreferredSize value
         */
        public Dimension getPreferredSize() {
            return new Dimension(110, 170);
        }


        /**
         *  Description of the Method
         */
        private void init() {
            this.setLayout(null);

            //  Add the buttons
            JButton showButton = new JButton("Show");
            showButton.setBounds(0, 10, 100, 25);
            this.add(showButton);
            showButton.addActionListener(listener);

            JButton hideButton = new JButton("Hide");
            hideButton.setBounds(0, 50, 100, 25);
            this.add(hideButton);
            hideButton.addActionListener(listener);

            JButton reloadButton = new JButton("Reload");
            reloadButton.setBounds(0, 90, 100, 25);
            this.add(reloadButton);
            reloadButton.addActionListener(listener);
/*
            JButton reloadAllButton = new JButton("Reload All");
            reloadAllButton.setBounds(0, 130, 100, 25);
            reloadAllButton.setEnabled(false);
            this.add(reloadAllButton);
*/
        }
    }
}
//  EOF
