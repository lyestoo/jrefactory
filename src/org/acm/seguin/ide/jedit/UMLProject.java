/*
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.acm.seguin.ide.jedit;

import java.io.File;
import java.io.IOException;

import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Collection;
import java.util.Collections;

import javax.swing.Icon;

import org.gjt.sp.util.Log;
import org.gjt.sp.jedit.GUIUtilities;

/**
 *  <p>
 *
 *  Note: this class is not thread safe!</p>
 *
 *@author     <a href="mailto:JRefactoryPlugin@ladyshot.demon.co.uk">Mike
 *      Atkinson</a>
 *@created    23 July 2003
 *@version    $Id: UMLProject.java,v 1.1 2003/09/17 19:52:32 mikeatkinson Exp $
 *@since      0.0.1
 */
public class UMLProject {

    private final static Icon projectIcon = GUIUtilities.loadIcon("DriveSmall.png");

    private ArrayList listeners;
    private Properties properties;
    private String name = "";
    private String root = "";



    /**
     *  Constructor for the UMLProject object
     *
     *@param  name  Description of the Parameter
     */
    public UMLProject(String name) {
        listeners = new ArrayList();
        properties = new Properties();
    }


    /**
     *  Gets the name attribute of the UMLProject object
     *
     *@return    The name value
     */
    public String getName() {
        return name;
    }


    /**
     *  Sets the name attribute of the UMLProject object
     *
     *@param  name  The new name value
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     *  Gets the rootPath attribute of the UMLProject object
     *
     *@return    The rootPath value
     */
    public String getRootPath() {
        return root;
    }


    /**
     *  Sets the rootPath attribute of the UMLProject object
     *
     *@param  root  The new rootPath value
     */
    public void setRootPath(String root) {
        this.root = root;
    }


    /**
     *  Returns the property set for the project.
     *
     *@param  property  Description of the Parameter
     *@return           The property value
     */
    public String getProperty(String property) {
        return properties.getProperty(property);
    }


    /**
     *  Sets a property.
     *
     *@param  name   The new property value
     *@param  value  The new property value
     *@return        The old value for the property (can be null).
     */
    public String setProperty(String name, String value) {
        String old = properties.getProperty(name);
        properties.setProperty(name, value);
        return old;
    }


    /**
     *  Returns a set containing all property names for this project.
     *
     *@return    The propertyNames value
     */
    public Set getPropertyNames() {
        return properties.keySet();
    }


    /**
     *  Removes the given property from the project.
     *
     *@param  property  Description of the Parameter
     *@return           Description of the Return Value
     */
    public Object removeProperty(String property) {
        return properties.remove(property);
    }


    /**
     *  Return the project's property set.
     *
     *@return    The properties value
     */
    public Properties getProperties() {
        return properties;
    }


    /**
     *  Returns the icon to be shown on the tree next to the node name.
     *
     *@param  expanded  If the node is currently expanded or not.
     *@return           The icon value
     */
    public Icon getIcon(boolean expanded) {
        return projectIcon;
    }


    /**
     *  Returns a string representation of the current node.
     *
     *@return    Description of the Return Value
     */
    public String toString() {
        return "UMLProject []";
    }

}

