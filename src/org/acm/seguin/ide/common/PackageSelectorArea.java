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
package org.acm.seguin.ide.common;

import java.awt.Dimension;
import java.io.IOException;
import java.util.Iterator;
import javax.swing.*;
import org.acm.seguin.summary.PackageSummary;
import org.acm.seguin.uml.PackageSummaryListModel;
import org.acm.seguin.uml.UMLPackage;

/**
 *  Just the package selector area
 *
 *@author     Chris Seguin
 *@created    October 18, 2001
 */
public class PackageSelectorArea extends JPanel {
    /**
     *  The list box of packages
     */
    protected JList listbox;
    private JScrollPane pane;


    /**
     *  Constructor for the PackageSelectorArea object
     */
    public PackageSelectorArea() {
        //  Setup the UI
        setLayout(null);
        super.setSize(220, 300);

        //  Create the list
        listbox = new JList();

        pane = new JScrollPane(listbox);
        pane.setBounds(10, 10, 200, 280);
        add(pane);
    }


    /**
     *  Gets the ScrollPane attribute of the PackageSelectorArea object
     *
     *@return    The ScrollPane value
     */
    public JScrollPane getScrollPane() {
        return pane;
    }


    /**
     *  Gets the Selection attribute of the PackageSelectorArea object
     *
     *@return    The Selection value
     */
    public PackageSummary getSelection() {
        Object[] selection = listbox.getSelectedValues();
        return (PackageSummary) selection[0];
    }


    /**
     *  Loads the package into the listbox
     */
    public void loadPackages() {
        PackageSummaryListModel model = new PackageSummaryListModel();

        //  Get the list of packages
        Iterator iter = PackageSummary.getAllPackages();
        if (iter == null) {
            return;
        }

        //  Add in the packages
        UMLPackage view = null;
        PackageSummary packageSummary = null;
        PackageListFilter filter = PackageListFilter.get();
        while (iter.hasNext()) {
            packageSummary = (PackageSummary) iter.next();
            if (filter.isIncluded(packageSummary)) {
                model.add(packageSummary);
            }
        }

        //  Set the model
        listbox.setModel(model);
    }


    /**
     *  Load the summaries
     */
    public void loadSummaries() { }
}
//  EOF
