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
package org.acm.seguin.tools.install;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *  Edits a tag line
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public class TagEditorPanel extends JPanel {
    private JLabel tagNameLabel = new JLabel("Tag Name:");
    private JTextField tagNameTextField = new JTextField(10);
    private JLabel descriptionLabel = new JLabel("Description:");
    private JTextField descriptionTextField = new JTextField(20);
    private JCheckBox classCheckBox = new JCheckBox("Class");
    private JCheckBox methodCheckBox = new JCheckBox("Method");
    private JCheckBox fieldCheckBox = new JCheckBox("Field");
    private JCheckBox enumCheckBox = new JCheckBox("Enum");
    private JButton updateButton = new JButton("Add/Update");
    private JButton clearButton = new JButton("Delete");


    /**
     *  Constructor for the TagEditorPanel object
     */
    public TagEditorPanel() {
        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.anchor = constraints.WEST;
        constraints.fill = constraints.NONE;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        constraints.gridx = 0;
        constraints.gridy = 0;
        add(tagNameLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        add(tagNameTextField, constraints);

        constraints.gridx = 2;
        constraints.gridy = 0;
        add(descriptionLabel, constraints);

        constraints.gridx = 3;
        constraints.gridy = 0;
        add(descriptionTextField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        add(classCheckBox, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        add(methodCheckBox, constraints);

        constraints.gridx = 2;
        constraints.gridy = 1;
        add(fieldCheckBox, constraints);

        constraints.gridx = 3;
        constraints.gridy = 1;
        add(enumCheckBox, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        add(updateButton, constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        add(clearButton, constraints);
    }


    /**
     *  Description of the Method
     *
     *@param  tlp  Description of Parameter
     */
    public void load(TagLinePanel tlp) {
        if (tlp == null) {
            tagNameTextField.setText("");
            descriptionTextField.setText("");
            classCheckBox.setSelected(false);
            fieldCheckBox.setSelected(false);
            methodCheckBox.setSelected(false);
            enumCheckBox.setSelected(false);
            return;
        }

        tagNameTextField.setText(tlp.getTagName());
        descriptionTextField.setText(tlp.getDescription());
        classCheckBox.setSelected(tlp.isClassType());
        fieldCheckBox.setSelected(tlp.isFieldType());
        methodCheckBox.setSelected(tlp.isMethodType());
        enumCheckBox.setSelected(tlp.isEnumType());
    }


    /**
     *  Description of the Method
     *
     *@param  tlp  Description of Parameter
     */
    public void save(TagLinePanel tlp) {
        tlp.setTagName(tagNameTextField.getText());
        tlp.setDescription(descriptionTextField.getText());
        tlp.setClassType(classCheckBox.isSelected());
        tlp.setMethodType(methodCheckBox.isSelected());
        tlp.setFieldType(fieldCheckBox.isSelected());
        tlp.setEnumType(enumCheckBox.isSelected());
    }


    /**
     *  Adds a feature to the UpdateListener attribute of the TagEditorPanel
     *  object
     *
     *@param  listener  The feature to be added to the UpdateListener attribute
     */
    public void addUpdateListener(ActionListener listener) {
        updateButton.addActionListener(listener);
    }


    /**
     *  Description of the Method
     *
     *@param  listener  Description of Parameter
     */
    public void removeUpdateListener(ActionListener listener) {
        updateButton.removeActionListener(listener);
    }


    /**
     *  Adds a feature to the ClearListener attribute of the TagEditorPanel
     *  object
     *
     *@param  listener  The feature to be added to the ClearListener attribute
     */
    public void addClearListener(ActionListener listener) {
        clearButton.addActionListener(listener);
    }


    /**
     *  Description of the Method
     *
     *@param  listener  Description of Parameter
     */
    public void removeClearListener(ActionListener listener) {
        clearButton.removeActionListener(listener);
    }


    /**
     *  Gets the tagName attribute of the TagEditorPanel object
     *
     *@return    The tagName value
     */
    public String getTagName() {
        return tagNameTextField.getText();
    }
}

//  This is the end of the file

