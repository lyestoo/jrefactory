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
package org.acm.seguin.tools.install;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.acm.seguin.util.MissingSettingsException;

/**
 *  Tag editor setting panel
 *
 *@author     Chris Seguin
 *@author     Mike Atkinson
 *@created    September 12, 2001
 */
public class TagEditorSettingPanel extends SettingPanel {
    private JList listBox;
    private TagEditorPanel editor;
    private TagListModel listModel;
    private JButton upButton;
    private JButton downButton;
    private final static int CLASS_TYPE = 1;
    private final static int METHOD_TYPE = 2;
    private final static int FIELD_TYPE = 3;
    private final static int ENUM_TYPE = 3;


    /**
     *  Constructor for the TagEditorSettingPanel object
     */
    public TagEditorSettingPanel() {
        super();

        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.anchor = constraints.WEST;
        constraints.fill = constraints.NONE;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        add(new JLabel("The following are javadoc tags that are required by the system."), constraints);
        constraints.gridy = 1;
        add(new JLabel("To make the javadoc tag required, add the tag and include the description."), constraints);
        constraints.gridy = 2;
        add(new JLabel("Use the up and down buttons to adjust the order that javadoc tags are."), constraints);
        constraints.gridy = 3;
        add(new JLabel("sorted in."), constraints);

        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridy = 4;
        editor = new TagEditorPanel();
        editor.addUpdateListener(new UpdateTagListener());
        editor.addClearListener(new ClearTagListener());
        add(editor, constraints);

        listModel = new TagListModel();
        load();
        listBox = new JList(listModel);
        listBox.addListSelectionListener(new TagListener());

        constraints.gridy = 5;
        constraints.fill = constraints.BOTH;
        add(new JScrollPane(listBox), constraints);

        constraints.gridwidth = 1;
        constraints.gridy = 6;
        constraints.fill = constraints.NONE;
        constraints.anchor = constraints.EAST;
        upButton = new JButton("Up");
        upButton.addActionListener(new MoveUpListener());
        add(upButton, constraints);

        constraints.gridx = 1;
        constraints.anchor = constraints.WEST;
        downButton = new JButton("Down");
        downButton.addActionListener(new MoveDownListener());
        add(downButton, constraints);
    }


    /**
     *  Dummy method
     *
     *@return    The Key value
     */
    public String getKey() {
        return "";
    }


    /**
     *  Generate the settings file for this particular setting
     *
     *@param  output  the output stream
     */
    public void generateSetting(PrintWriter output) {
        StringBuffer classBuffer = new StringBuffer("class.tags=");
        boolean classComma = false;
        StringBuffer methodBuffer = new StringBuffer("method.tags=");
        boolean methodComma = false;
        StringBuffer fieldBuffer = new StringBuffer("field.tags=");
        boolean fieldComma = false;
        StringBuffer enumBuffer = new StringBuffer("enum.tags=");
        boolean enumComma = false;

        output.println("#");
        output.println("#  The following are the tags and the order");
        output.println("#  that are required in javadocs.  If there is");
        output.println("#  description, then they are not required and the");
        output.println("#  system is only specifying the order in which they");
        output.println("#  should appear.  If a description is provided, then");
        output.println("#  the tag is required.");
        output.println("#");

        Iterator iter = listModel.iterator();
        while (iter.hasNext()) {
            TagLinePanel tlp = (TagLinePanel) iter.next();
            if (tlp.getDescription().length() > 0) {
                output.println(tlp.getTagName() + ".descr=" + tlp.getDescription());
                output.println("");
            }
            if (tlp.isClassType()) {
                if (classComma) {
                    classBuffer.append(",");
                }
                classBuffer.append(tlp.getTagName());
                classComma = true;
            }
            if (tlp.isMethodType()) {
                if (methodComma) {
                    methodBuffer.append(",");
                }
                methodBuffer.append(tlp.getTagName());
                methodComma = true;
            }
            if (tlp.isFieldType()) {
                if (fieldComma) {
                    fieldBuffer.append(",");
                }
                fieldBuffer.append(tlp.getTagName());
                fieldComma = true;
            }
            if (tlp.isEnumType()) {
                if (enumComma) {
                    enumBuffer.append(",");
                }
                enumBuffer.append(tlp.getTagName());
                enumComma = true;
            }
        }
        output.println(classBuffer.toString());
        output.println(methodBuffer.toString());
        output.println(fieldBuffer.toString());
        output.println(enumBuffer.toString());
        output.println(" ");
    }


    /**
     *  Loads the list box from the property file
     */
    private void load() {
        TagLinePanel tlp;

        try {
            loadTags(bundle.getString("class.tags"), CLASS_TYPE);
        } catch (MissingSettingsException mse) {
            tlp = new TagLinePanel();
            tlp.setTagName("author");
            tlp.setDescription("{0}");
            tlp.setClassType(true);
            listModel.add(tlp);

            tlp = new TagLinePanel();
            tlp.setTagName("created");
            tlp.setDescription("{1}");
            listModel.add(tlp);
            tlp.setClassType(true);
        }


        try {
            loadTags(bundle.getString("enum.tags"), ENUM_TYPE);
        } catch (MissingSettingsException mse) {
            tlp = new TagLinePanel();
            tlp.setTagName("author");
            tlp.setDescription("{0}");
            tlp.setEnumType(true);
            listModel.add(tlp);

            tlp = new TagLinePanel();
            tlp.setTagName("since");
            tlp.setDescription("{1}");
            listModel.add(tlp);
            tlp.setEnumType(true);
        }

        try {
            loadTags(bundle.getString("method.tags"), METHOD_TYPE);

            String param = bundle.getString("param.descr");
            String rtn = bundle.getString("return.descr");
            String exc = bundle.getString("exception.descr");
        } catch (MissingSettingsException mse) {
            tlp = new TagLinePanel();
            tlp.setTagName("param");
            tlp.setDescription("Description of the Parameter");
            tlp.setMethodType(true);
            listModel.add(tlp);

            tlp = new TagLinePanel();
            tlp.setTagName("return");
            tlp.setDescription("Description of the Return Value");
            tlp.setMethodType(true);
            listModel.add(tlp);

            tlp = new TagLinePanel();
            tlp.setTagName("exception");
            tlp.setDescription("Description of the Exception");
            tlp.setMethodType(true);
            listModel.add(tlp);
        }

        try {
            loadTags(bundle.getString("field.tags"), FIELD_TYPE);
        } catch (MissingSettingsException mse) {
        }
    }


    /**
     *  Description of the Method
     *
     *@param  tagNames  Description of Parameter
     *@param  type      Description of Parameter
     */
    private void loadTags(String tagNames, int type) {
        StringTokenizer tok = new StringTokenizer(tagNames, " \t,");
        while (tok.hasMoreTokens()) {
            String next = tok.nextToken();
            TagLinePanel tlp = listModel.find(next);
            if (tlp == null) {
                tlp = new TagLinePanel();
                tlp.setTagName(next);
                listModel.add(tlp);
                try {
                    tlp.setDescription(bundle.getString(next + ".descr"));
                } catch (MissingSettingsException mse) {
                    tlp.setDescription("");
                }
            }
            if (type == CLASS_TYPE) {
                tlp.setClassType(true);
            }
            if (type == METHOD_TYPE) {
                tlp.setMethodType(true);
            }
            if (type == FIELD_TYPE) {
                tlp.setFieldType(true);
            }
            if (type == ENUM_TYPE) {
                tlp.setEnumType(true);
            }
        }
    }


    /**
     *  Description of the Class
     *
     *@author     Chris Seguin
     *@created    September 12, 2001
     */
    public class TagListener implements ListSelectionListener {
        /**
         *  Description of the Method
         *
         *@param  e  Description of Parameter
         */
        public void valueChanged(ListSelectionEvent e) {
            int index = listBox.getSelectedIndex();
            TagLinePanel tlp = (TagLinePanel) listModel.getElementAt(index);
            editor.load(tlp);
        }
    }


    /**
     *  Responsible for storing the tag back into the list
     *
     *@author     Chris Seguin
     *@created    September 12, 2001
     */
    public class UpdateTagListener implements ActionListener {
        /**
         *  Description of the Method
         *
         *@param  evt  Description of Parameter
         */
        public void actionPerformed(ActionEvent evt) {
            String name = editor.getTagName();
            TagLinePanel tlp = (TagLinePanel) listModel.find(name);
            boolean isNew = false;
            if (tlp == null) {
                tlp = new TagLinePanel();
                listModel.add(tlp);
                isNew = true;
            }
            editor.save(tlp);
            if (isNew) {
                try {
                    listBox.setSelectedIndex(0);
                    listBox.setListData(new String[]{"Please wait..."});
                    listBox.setModel(listModel);
                    listBox.setSelectedIndex(listModel.getSize() - 1);
                } catch (Throwable thrown) {
                    thrown.printStackTrace(System.out);
                }
            }
            else {
                listBox.repaint();
            }
        }
    }


    /**
     *  Responsible for removing the tag from the list
     *
     *@author     Chris Seguin
     *@created    September 12, 2001
     */
    public class ClearTagListener implements ActionListener {
        /**
         *  Description of the Method
         *
         *@param  evt  Description of Parameter
         */
        public void actionPerformed(ActionEvent evt) {
            String name = editor.getTagName();
            listModel.remove(name);
        }
    }


    /**
     *  Description of the Class
     *
     *@author     Chris Seguin
     *@created    September 12, 2001
     */
    public class MoveUpListener implements ActionListener {
        /**
         *  Description of the Method
         *
         *@param  evt  Description of Parameter
         */
        public void actionPerformed(ActionEvent evt) {
            int index = listBox.getSelectedIndex();
            if (index == 0) {
                return;
            }
            listModel.swap(index, index - 1);
            listBox.setSelectedIndex(index - 1);
        }
    }


    /**
     *  Description of the Class
     *
     *@author     Chris Seguin
     *@created    September 12, 2001
     */
    public class MoveDownListener implements ActionListener {
        /**
         *  Description of the Method
         *
         *@param  evt  Description of Parameter
         */
        public void actionPerformed(ActionEvent evt) {
            int index = listBox.getSelectedIndex();
            if (index == listModel.getSize() - 1) {
                return;
            }
            listModel.swap(index, index + 1);
            listBox.setSelectedIndex(index + 1);
        }
    }


    /**
     *  Gets the initial value if it is not defined
     *
     *@return    The InitialValue value
     */
    protected String getInitialValue() {
        return "";
    }


    /**
     *  Reload from the file
     */
    public void reload() {
        listModel.clearAll();
        load();
    }
}

//  This is the end of the file

