/*
 *  ====================================================================
 *  The JRefactory License, Version 1.0
 *
 *  Copyright (c) 2001 JRefactory.  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  JRefactory (http://www.sourceforge.org/projects/jrefactory)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "JRefactory" must not be used to endorse or promote
 *  products derived from this software without prior written
 *  permission. For written permission, please contact seguin@acm.org.
 *
 *  5. Products derived from this software may not be called "JRefactory",
 *  nor may "JRefactory" appear in their name, without prior written
 *  permission of Chris Seguin.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE CHRIS SEGUIN OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of JRefactory.  For more information on
 *  JRefactory, please see
 *  <http://www.sourceforge.org/projects/jrefactory>.
 */
package org.acm.seguin.uml.refactor;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.acm.seguin.uml.UMLPackage;


/**
 *  Prompts the user for a class name. The class name can then be used to rename
 *  a class, add an abstract parent, or add a child.
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public abstract class ClassNameDialog extends RefactoringDialog
{
    //  Instance Variables
    private JTextField newName;


    /**
     *  Constructor for ClassNameDialog
     *
     *@param  init      The package where this operation is occuring
     *@param  startRow  Description of Parameter
     */
    public ClassNameDialog(UMLPackage init, int startRow)
    {
        super(init);

        //  Set the window size and layout
        setTitle(getWindowTitle());

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        getContentPane().setLayout(gridbag);
        setSize(310, 120);

        //  Add components
        JLabel newNameLabel = new JLabel(getLabelText());
        gbc.gridx = 1;
        gbc.gridy = startRow;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gridbag.setConstraints(newNameLabel, gbc);
        getContentPane().add(newNameLabel);

        newName = new JTextField();
        newName.setColumns(25);
        gbc.gridx = 2;
        gbc.gridy = startRow;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gridbag.setConstraints(newName, gbc);
        getContentPane().add(newName);

        JButton okButton = new JButton("OK");
        gbc.gridx = 2;
        gbc.gridy = startRow + 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.NONE;
        gridbag.setConstraints(okButton, gbc);
        okButton.addActionListener(this);
        getContentPane().add(okButton);

        JButton cancelButton = new JButton("Cancel");
        gbc.gridx = 3;
        gbc.gridy = startRow + 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gridbag.setConstraints(cancelButton, gbc);
        cancelButton.addActionListener(this);
        getContentPane().add(cancelButton);

        pack();

        org.acm.seguin.awt.CenterDialog.center(this, init);
    }


    /**
     *  Gets the label for the text
     *
     *@return    the text for the label
     */
    public abstract String getLabelText();


    /**
     *  Returns the window title
     *
     *@return    the title
     */
    public abstract String getWindowTitle();


    /**
     *  Sets the className attribute of the ClassNameDialog object
     *
     *@param  name  The new className value
     */
    protected void setClassName(String name)
    {
        newName.setText(name);
        newName.setSelectionStart(0);
        newName.setSelectionStart(name.length());
    }


    /**
     *  Gets the ClassName attribute of the ClassNameDialog object Gets the
     *  ClassName attribute of the ClassNameDialog object
     *
     *@return    The ClassName value
     */
    protected String getClassName()
    {
        return newName.getText();
    }
}
