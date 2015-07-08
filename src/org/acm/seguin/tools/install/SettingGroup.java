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


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JPanel;


/**
 *  Stores a group of settings
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public abstract class SettingGroup extends JPanel
{
    /**
     *  Contains the list of dividers
     */
    private ArrayList dividerList = new ArrayList();
    private String groupName;
    private LinkedList list;


    /**
     *  Constructor for the SettingGroup object
     *
     *@param  value  the group name
     */
    public SettingGroup(String value)
    {
        super();

        setGroupName(value);
        list = new LinkedList();
    }


    /**
     *  Sets the GroupName attribute of the SettingGroup object
     *
     *@param  value  The new GroupName value
     */
    public void setGroupName(String value)
    {
        groupName = value;
    }


    /**
     *  Gets the GroupName attribute of the SettingGroup object
     *
     *@return    The GroupName value
     */
    public String getGroupName()
    {
        return groupName;
    }


    /**
     *  Generate the settings file for this particular setting
     *
     *@param  output  the output stream
     */
    public void generateSetting(PrintWriter output)
    {
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            SettingPanel next = (SettingPanel) iter.next();
            next.generateSetting(output);
        }
    }


    /**
     *  Reload all the settings from the file
     */
    public void reload()
    {
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            SettingPanel next = (SettingPanel) iter.next();
            next.reload();
        }
    }


    /**
     *  Description of the Method
     *
     *@param  panel        Description of Parameter
     *@param  constraints  Description of the Parameter
     */
    protected void add(SettingPanel panel, GridBagConstraints constraints)
    {
        add(panel, constraints, true);
    }


    /**
     *  Adds the panel to the screen and the list
     *
     *@param  panel        Description of Parameter
     *@param  addToPanel   Description of Parameter
     *@param  constraints  Description of the Parameter
     */
    protected void add(SettingPanel panel, GridBagConstraints constraints, boolean addToPanel)
    {
        list.add(panel);
        if (addToPanel) {
            super.add(panel, constraints);
        }
    }


    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    protected Divider createDivider()
    {
        Divider divider = new Divider();
        dividerList.add(divider);
        return divider;
    }


    /**
     *  Description of the Method
     */
    protected void updateDividers()
    {
        Dimension dim = getPreferredSize();
        int wide = (int) (dim.width * 0.8);
        Iterator iter = dividerList.iterator();
        while (iter.hasNext()) {
            Divider divider = (Divider) iter.next();
            divider.adjustSize(wide);
        }
    }
}

//  This is the end of the file

