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

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import org.acm.seguin.util.MissingSettingsException;

/**
 *  Allows the user to select true or false
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public abstract class SortOptionPanel extends SortSettingPanel {
    private ButtonGroup group;
    private LinkedList list;


    /**
     *  Constructor for the OptionPanel object
     */
    public SortOptionPanel() {
        super();

        group = new ButtonGroup();
        list = new LinkedList();
    }


    /**
     *  Gets the Value attribute of the TogglePanel object
     *
     *@return    The Value value
     */
    public String getValue() {
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Object[] pair = (Object[]) iter.next();
            if (((JRadioButton) pair[1]).isSelected()) {
                return (String) pair[0];
            }
        }
        return "";
    }


    /**
     *  Adds a feature to the Control attribute of the TogglePanel object
     */
    public void addControl() {
        findProperty(getSortName()).setSelected(true);
    }
    public void reload() {
        setSortEnabled(false);
        findProperty(getSortName()).setSelected(true);
    }



    /**
     *  Add an option
     *
     *@param  key    The feature to be added to the Option attribute
     *@param  descr  The feature to be added to the Option attribute
     */
    public void addOption(String key, String descr) {
        incrItems();
        addDescription("* " + key + " - " + descr, false);
        JRadioButton button = new JRadioButton(descr);
        button.setSelected(key.equals(getDefaultValue()));
        constraints.gridy = constraints.gridy + 1;
        add(button, constraints);
        group.add(button);
        Object[] pair = new Object[2];
        pair[0] = key;
        pair[1] = button;
        list.add(pair);
    }


    /**
     *  Generates the section of the setting file described by this list.
     *
     *@param  output  the output stream
     *@param  index   the index
     */
    public void generateSetting(PrintWriter output, int index) {
        printDescription(output);
        if (!isSortEnabled()) {
            output.print("#");
        }
        output.print("sort." + index + "=" + getValue());
        output.println("");
    }


    /**
     *  Finds the property that should be enabled
     *
     *@param  key  Description of Parameter
     *@return      Description of the Returned Value
     */
    protected JRadioButton findProperty(String key) {
        Iterator iter;

        try {
            int index = 1;
            while (index < 50) {
                String value = SettingPanel.bundle.getString("sort." + index);
                if (value.startsWith(key)) {
                    setOrder(index);
                    iter = list.iterator();
                    while (iter.hasNext()) {
                        Object[] pair = (Object[]) iter.next();
                        if (value.equals(pair[0])) {
                        	setSortEnabled(true);
                            return (JRadioButton) pair[1];
                        }
                    }
                }
                index++;
            }
        } catch (MissingSettingsException mse) {
            //  Handle this below
        }
        setSortEnabled(false);
        iter = list.iterator();
        Object[] pair = (Object[]) iter.next();
        return (JRadioButton) pair[1];
    }
}

//  This is the end of the file

