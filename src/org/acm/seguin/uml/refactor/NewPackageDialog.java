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
package org.acm.seguin.uml.refactor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import org.acm.seguin.awt.ExceptionPrinter;
import org.acm.seguin.refactor.RefactoringException;
import org.acm.seguin.refactor.RefactoringFactory;
import org.acm.seguin.refactor.type.MoveClass;
import org.acm.seguin.summary.*;
import org.acm.seguin.uml.UMLPackage;
import org.acm.seguin.uml.loader.ReloaderSingleton;

/**
 *  Creates a dialog box to prompt for the new package name
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public class NewPackageDialog extends JDialog implements ActionListener {
    //  Instance Variables
    private JComboBox packageName;
    private TypeSummary[] typeArray;


    /**
     *  Constructor for NewPackageDialog
     *
     *@param  initTypes  Description of Parameter
     */
    public NewPackageDialog(TypeSummary initTypes[]) {
        super();

        typeArray = initTypes;

        //  Set the window size and layout
        setTitle("Move class to new package");
        GridBagLayout gridbag = new GridBagLayout();
        getContentPane().setLayout(gridbag);
        setSize(310, 150);

        //  Add components
        PackageList packageList = new PackageList();
        packageName = packageList.add(this);

        JButton okButton = new JButton("OK");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gridbag.setConstraints(okButton, gbc);
        okButton.addActionListener(this);
        getContentPane().add(okButton);

        JButton cancelButton = new JButton("Cancel");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gridbag.setConstraints(cancelButton, gbc);
        cancelButton.addActionListener(this);
        getContentPane().add(cancelButton);

        JPanel blank = new JPanel();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        Dimension blankDim = new Dimension(50, cancelButton.getPreferredSize().height * 4);
        blank.setMinimumSize(blankDim);
        blank.setPreferredSize(blankDim);
        getContentPane().add(blank, gbc);

        pack();

        org.acm.seguin.awt.CenterDialog.center(this);
    }


    /**
     *  Respond to a button press
     *
     *@param  evt  the action event
     */
    public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand().equals("OK")) {
            dispose();
            String result = (String) packageName.getSelectedItem();
            if (result.startsWith("<")) {
                result = "";
            }
            repackage(result);
        } else if (evt.getActionCommand().equals("Cancel")) {
            dispose();
        }
    }


    /**
     *  Repackage the files
     *
     *@param  destinationPackage  the new package name
     */
    public void repackage(String destinationPackage) {
        //  Create the move class
        MoveClass moveClass = RefactoringFactory.get().moveClass();

        //  Set the destination package
        moveClass.setDestinationPackage(destinationPackage);

        //  Get the files
        String parentDir = null;
        for (int ndx = 0; ndx < typeArray.length; ndx++) {
            parentDir = addType(typeArray[ndx], moveClass);
        }

        //  Run it
        try {
            moveClass.run();
        } catch (RefactoringException re) {
            JOptionPane.showMessageDialog(null, re.getMessage(), "Refactoring Exception",
                    JOptionPane.ERROR_MESSAGE);
        }

        ReloaderSingleton.reload();
    }


    /**
     *  Adds a type to the refactoring
     *
     *@param  moveClass  the refactoring
     *@param  type       The feature to be added to the Type attribute
     *@return            Description of the Returned Value
     */
    private String addType(TypeSummary type, MoveClass moveClass) {
        String parentDir = null;

        FileSummary parent = (FileSummary) type.getParent();
        File file = parent.getFile();
        if (file == null) {
            return null;
        }

        try {
            String canonicalPath = file.getCanonicalPath();
            parentDir = (new File(canonicalPath)).getParent();
        } catch (IOException ioe) {
            ExceptionPrinter.print(ioe, false);
        }

        moveClass.setDirectory(parentDir);
        moveClass.add(file.getName());

        return parentDir;
    }
}
