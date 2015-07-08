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
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *  Holds a sort setting
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public abstract class SortSettingPanel extends SettingPanel {
    /**
     *  The constraints
     */
    protected GridBagConstraints constraints;
    private JCheckBox enabledCheckbox;
    private LinkedList list;
    private int order;


    /**
     *  Constructor for the SortSettingPanel object
     */
    public SortSettingPanel() {
        super();
        setLayout(new GridBagLayout());

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.anchor = constraints.WEST;
        constraints.fill = constraints.NONE;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        enabledCheckbox = new JCheckBox(getSortName() + " Enabled");
        enabledCheckbox.setSelected(true);
        add(enabledCheckbox, constraints);
        list = new LinkedList();

        order = 1000;
    }


    /**
     *  Determines if the sort is enabled
     *
     *@return    The SortEnabled value
     */
    public boolean isSortEnabled() {
        return enabledCheckbox.isSelected();
    }


    /**
     *  Gets the Key attribute of the SortSettingPanel object
     *
     *@return    The Key value
     */
    public String getKey() {
        return "sort";
    }


    /**
     *  Gets the Order attribute of the SortSettingPanel object
     *
     *@return    The Order value
     */
    public int getOrder() {
        return order;
    }


    /**
     *  Description of the Method
     *
     *@param  output  Description of Parameter
     *@param  index   Description of Parameter
     */
    public abstract void generateSetting(PrintWriter output, int index);


    /**
     *  Sets the SortEnabled attribute of the SortSettingPanel object
     *
     *@param  way  The new SortEnabled value
     */
    protected void setSortEnabled(boolean way) {
        enabledCheckbox.setSelected(way);
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            ((JComponent) iter.next()).setEnabled(way);
        }
    }


    /**
     *  Sets the Order attribute of the SortSettingPanel object
     *
     *@param  value  The new Order value
     */
    protected void setOrder(int value) {
        order = value;
    }


    /**
     *  Gets the SortName attribute of the SortSettingPanel object
     *
     *@return    The SortName value
     */
    protected abstract String getSortName();


    /**
     *  Returns a string representing this item
     *
     *@return    the string
     */
    public String toString() {
        return getSortName();
    }


    /**
     *  Adds a feature to the Label attribute of the SortSettingPanel object
     *
     *@param  value  The feature to be added to the Label attribute
     */
    protected void addLabel(String value) {
        constraints.gridy = constraints.gridy + 1;
        add(new JLabel(value), constraints);
    }


    /**
     *  Description of the Method
     *
     *@param  comp         Description of Parameter
     *@param  constraints  Description of Parameter
     */
    protected void add(JComponent comp, GridBagConstraints constraints) {
        super.add(comp, constraints);
        if (list != null) {
            list.add(comp);
        }
    }


    /**
     *  Adds a feature to the Listener attribute of the SortSettingPanel object
     */
    protected void addListener() {
        enabledCheckbox.addChangeListener(new EnabledListener());
    }


    /**
     *  Generates the section of the setting file described by this list.
     *
     *@param  output  the output stream
     *@param  index   the index
     *@param  key     the key
     */
    protected void generateSetting(PrintWriter output, int index, String key) {
        printDescription(output);
        if (!isSortEnabled()) {
            output.print("#");
        }
        output.println("sort." + index + "=" + getValue());
    }


    /**
     *  Description of the Class
     *
     *@author     Chris Seguin
     *@created    September 12, 2001
     */
    private class EnabledListener implements ChangeListener {
        /**
         *  Description of the Method
         *
         *@param  e  Description of Parameter
         */
        public void stateChanged(ChangeEvent e) {
            boolean way = enabledCheckbox.isSelected();
            Iterator iter = list.iterator();
            while (iter.hasNext()) {
                ((JComponent) iter.next()).setEnabled(way);
            }
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
}

//  This is the end of the file

