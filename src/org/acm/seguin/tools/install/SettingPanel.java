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

import java.awt.Font;
import java.awt.GridLayout;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.acm.seguin.util.FileSettings;
import org.acm.seguin.util.MissingSettingsException;

/**
 *  Generic base class containing the setting panel
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public abstract class SettingPanel extends JPanel {
    private List descriptions;
    private GridLayout gridLayout;
    private int items;
    /**
     *  The file settings
     */
    protected static FileSettings bundle = null;
    /**
     *  Description of the Field
     */
    protected static Font teletype = null;


    /**
     *  Constructor for the SettingPanel object
     */
    public SettingPanel() {
        descriptions = new ArrayList();
        gridLayout = new GridLayout(1, 1);
        setLayout(gridLayout);
        items = 0;
    }


    /**
     *  Gets the Key attribute of the SettingPanel object
     *
     *@return    The Key value
     */
    public abstract String getKey();


    /**
     *  Gets the Value attribute of the SettingPanel object
     *
     *@return    The Value value
     */
    public String getValue() {
        return getDefaultValue();
    }


    /**
     *  Gets the DefaultValue attribute of the SettingPanel object
     *
     *@return    The DefaultValue value
     */
    public String getDefaultValue() {
        if (SettingPanel.bundle == null) {
            SettingPanel.init();
        }
        try {
            return SettingPanel.bundle.getString(getKey());
        } catch (MissingSettingsException mse) {
            return getInitialValue();
        }
    }


    /**
     *  Adds a feature to the Description attribute of the SettingPanel object
     *
     *@param  value  The feature to be added to the Description attribute
     */
    public void addDescription(String value) {
        addDescription(value, true);
    }


    /**
     *  Adds a feature to the Description attribute of the SettingPanel object
     *
     *@param  value  The feature to be added to the Description attribute
     */
    public void addCodeDescription(String value) {
        addCodeDescription(value, true);
    }


    /**
     *  Generate the settings file for this particular setting
     *
     *@param  output  the output stream
     */
    public void generateSetting(PrintWriter output) {
        printDescription(output);
        output.println(getKey() + "=" + getValue());
        output.println("");
    }


    /**
     *  Gets the initial value if it is not defined
     *
     *@return    The InitialValue value
     */
    protected abstract String getInitialValue();


    /**
     *  Adds a feature to the Description attribute of the SettingPanel object
     *
     *@param  value  The feature to be added to the Description attribute
     *@param  show   true if this should be displayed
     */
    protected void addDescription(String value, boolean show) {
        descriptions.add(value);
        if (show) {
            addLabel(value);
        }
    }


    /**
     *  Adds a feature to the Description attribute of the SettingPanel object
     *
     *@param  value  The feature to be added to the Description attribute
     *@param  show   true if this should be displayed
     */
    protected void addCodeDescription(String value, boolean show) {
        descriptions.add(value);
        if (show) {
            addCodeLabel(value);
        }
    }


    /**
     *  Increment the number of items in the grid
     */
    protected void incrItems() {
        items++;
        gridLayout.setRows(items);
    }


    /**
     *  Description of the Method
     *
     *@param  output  Description of Parameter
     */
    protected void printDescription(PrintWriter output) {
        Iterator iter = descriptions.iterator();
        while (iter.hasNext()) {
            output.println("# " + iter.next());
        }
    }


    /**
     *  Adds a feature to the Label attribute of the SettingPanel object
     *
     *@param  value  The feature to be added to the Label attribute
     */
    protected void addLabel(String value) {
        items++;
        gridLayout.setColumns(items);
        add(new JLabel(value));
    }


    /**
     *  Adds a feature to the Label attribute of the SettingPanel object
     *
     *@param  value  The feature to be added to the Label attribute
     */
    protected void addCodeLabel(String value) {
        items++;
        gridLayout.setColumns(items);
        JLabel temp = new JLabel(value);
        temp.setFont(teletype);
        add(temp);
    }


    /**
     *  Initialize the file setting singleton
     */
    private static synchronized void init() {
        if (bundle == null) {
            bundle = FileSettings.getRefactoryPrettySettings();
            teletype = new Font("monospaced", Font.PLAIN, 12);
        }
    }


    /**
     *  Reload the value from the pretty.settings file
     */
    public abstract void reload();
}
//  This is the end of the file
