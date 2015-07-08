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

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.ListCellRenderer;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import org.acm.seguin.awt.OrderableList;
//import org.acm.seguin.util.InsertionSortArray;
//import org.acm.seguin.util.Comparator;

/**
 *  This class is responsible for ordering the methods and fields.
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
class SortControlSettingPanel extends SettingPanel {
    private OrderableList list;
    private EtchedBorder etch = new EtchedBorder();
    private BevelBorder bevel = new BevelBorder(BevelBorder.RAISED);
    private GridBagConstraints constraints;
    private SortSettingPanel[] data;


    /**
     *  Constructor for the SortControlSettingPanel object
     */
    public SortControlSettingPanel() {
        super();
        setLayout(new GridBagLayout());

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = -1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.anchor = constraints.WEST;
        constraints.fill = constraints.NONE;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        addDescription("The following controls the order of methods, fields,");
        addDescription("classes, etc inside a class.");
        addControl();
    }


    /**
     *  Gets the Key attribute of the SortControlSettingPanel object
     *
     *@return    The Key value
     */
    public String getKey() {
        return "sort";
    }


    /**
     *  Adds a feature to the Control attribute of the SortControlSettingPanel
     *  object
     */
    public void addControl() {
        data = new SortSettingPanel[]
                {
                new TypeOrderPanel(),
                new GetterSetterOrderPanel(),
                new FinalOrderPanel(),
                new ProtectionOrderPanel(),
                new StaticOrderPanel(),
                new BeanOrderPanel(),
                new AlphabeticalOrderPanel(),
                new InitializerOrderPanel()
                };

        //InsertionSortArray isa = new InsertionSortArray();
        //isa.sort(data, new ComparePanels());
        Arrays.sort(data, new ComparePanels());
        list = new OrderableList(data, null);
        constraints.gridy = constraints.gridy + 1;
        add(list, constraints);

        for (int ndx = 0; ndx < data.length; ndx++) {
            constraints.gridy = constraints.gridy + 1;
            add(data[ndx], constraints);
        }
    }



    /**
     *  Generate the settings file for this particular setting
     *
     *@param  output  the output stream
     */
    public void generateSetting(PrintWriter output) {
        printDescription(output);
        Object[] data = list.getData();

        int current = 1;
        for (int ndx = 0; ndx < data.length; ndx++) {
            SortSettingPanel next = (SortSettingPanel) data[ndx];
            if (next.isSortEnabled()) {
                next.generateSetting(output, current);
                current++;
            }
        }

        for (int ndx = 0; ndx < data.length; ndx++) {
            SortSettingPanel next = (SortSettingPanel) data[ndx];
            if (!next.isSortEnabled()) {
                next.generateSetting(output, current);
                current++;
            }
        }
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
     *  Description of the Class
     *
     *@author     Chris Seguin
     *@created    September 12, 2001
     */
    private class ComparePanels implements Comparator {
        /**
         *  Description of the Method
         *
         *@param  obj1  Description of the Parameter
         *@param  obj2  Description of the Parameter
         *@return       Description of the Return Value
         */
        public int compare(Object obj1, Object obj2) {
            SortSettingPanel ssp1 = (SortSettingPanel) obj1;
            SortSettingPanel ssp2 = (SortSettingPanel) obj2;

            if (ssp1.getOrder() < ssp2.getOrder()) {
                return -1;
            }
            if (ssp2.getOrder() < ssp1.getOrder()) {
                return 1;
            }
            return 0;
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
     *  Description of the Method
     */
    public void reload() {
        for (int ndx = 0; ndx < data.length; ndx++) {
            data[ndx].setOrder(1000);
        }

        for (int ndx = 0; ndx < data.length; ndx++) {
            data[ndx].reload();
        }

        //InsertionSortArray isa = new InsertionSortArray();
        //isa.sort(data, new ComparePanels());
        Arrays.sort(data, new ComparePanels());

        list.resetModel(data);
    }
}

//  This is the end of the file

