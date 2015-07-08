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
package org.acm.seguin.ide.elixir;

import com.elixirtech.fx.DocManager;
import com.elixirtech.fx.FrameManager;
import com.elixirtech.fx.ViewManager;
import com.elixirtech.ui.ActionEx;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import org.acm.seguin.summary.PackageSummary;
import org.acm.seguin.uml.UMLPackage;
import org.acm.seguin.ide.common.SummaryLoaderThread;
import org.acm.seguin.ide.common.PackageSelectorDialog;
import org.acm.seguin.ide.common.PackageNameLoader;

/**
 *  View manager for a particular UML file
 *
 *@author     Chris Seguin
 *@created    October 18, 2001
 */
public class UMLViewManager implements ViewManager {
    private UMLDocManager docManager;
    private String filename;
    private String packageName;
    private UMLPackage packagePanel;
    private JScrollPane pane;
    private PackageSummary summary;


    /**
     *  Constructor for the UMLViewManager object
     *
     *@param  parent  the parent document manager
     *@param  name    the name of the file to view
     *@param  base    Description of Parameter
     */
    public UMLViewManager(UMLDocManager parent, String name, String base) {
        /*
         *  Creating this instance requires that the summaries
         *  have been loaded at least once, but shouldn't
         *  block further opertions.
         */
        SummaryLoaderThread.waitForLoading();

        docManager = parent;

        if (name != null) {
            filename = name;
            packagePanel = new UMLPackage(filename);
        }
        else {
            PackageSelectorDialog dialog =
                    new PackageSelectorDialog(FrameManager.current().getFrame());
            dialog.setVisible(true);
            summary = dialog.getSummary();

            filename = null;
            packagePanel = new UMLPackage(summary);
        }

        parent.getReloader().add(packagePanel);

        pane = new JScrollPane(packagePanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        JScrollBar horiz = pane.getHorizontalScrollBar();
        horiz.setUnitIncrement(400);
        JScrollBar vert = pane.getVerticalScrollBar();
        vert.setUnitIncrement(400);

        loadPackageName();
    }


    /**
     *  Get the actions currently supported (may vary with state)
     *
     *@return    The Actions value
     */
    public ActionEx[] getActions() {
        return new ActionEx[0];
    }


    /**
     *  Gets the Diagram attribute of the UMLViewManager object
     *
     *@return    The Diagram value
     */
    public UMLPackage getDiagram() {
        return packagePanel;
    }


    /**
     *  Get the document manager responsible for this view
     *
     *@return    The DocManager value
     */
    public DocManager getDocManager() {
        return docManager;
    }


    /**
     *  Get the title of the document being viewed
     *
     *@return    The Title value
     */
    public String getTitle() {
        if (packageName.length() > 0) {
            return packageName;
        }
        else {
            return "<Top Level Package>";
        }
    }


    /**
     *  Get the view component which renders/edits the document
     *
     *@return    The View value
     */
    public JComponent getView() {
        return pane;
    }


    /**
     *  Notify the view manager that it has been closed
     */
    public void closed() {
        save();
    }


    /**
     *  Notify the view manager that it is about to close
     */
    public void closing() { }


    /**
     *  Loads the package name from the file
     */
    private void loadPackageName() {
        if (filename == null) {
            packageName = summary.getName();
            return;
        }

        PackageNameLoader pnl = new PackageNameLoader();
        packageName = pnl.load(filename);
    }


    /**
     *  Determine whether it is ok to close the view.
     *
     *@return    Description of the Returned Value
     */
    public boolean okToClose() {
        return true;
    }


    /**
     *  Reload the document from its storage (if it has one).
     *
     *@return    Description of the Returned Value
     */
    public boolean reload() {
        packagePanel.reload();
        return true;
    }


    /**
     *  Save the current document.
     *
     *@return    Description of the Returned Value
     */
    public boolean save() {
        try {
            packagePanel.save();
        }
        catch (IOException ioe) {
            return false;
        }
        return true;
    }
}
//  EOF
