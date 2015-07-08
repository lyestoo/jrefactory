/*
 * JSStubsJUnitOptionPane.java - JavaStyle option pane for JavaDoc JUnit stubs
 * Copyright (C) 2001 Dirk Moebius
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.acm.seguin.ide.common.options;


import java.util.Hashtable;
import javax.swing.JTextField;


/**
 *@author     Mike Atkinson (<a href="mailto:javastyle@ladyshot.demon.co.uk">
 *      Mike@ladyshot.demon.co.uk</a> )
 *@author     Dirk Moebius (<a href="mailto:dmoebius@gmx.net">dmoebius@gmx.net
 *      </a>)
 *@created    04 September 2003
 *@version    $Version: $
 *@since      1.0
 */
public class JSStubsJUnitOptionPane extends JSHelpOptionPane {

    private final static String[] STUBS = {
            "junit.test",
            "junit.setUp",
            "junit.tearDown",
            "junit.suite",
            "junit.suite.return",
            };

    private Hashtable components = new Hashtable();


    /**
     *  Constructor for the JSStubsJUnitOptionPane object
     *
     *@param  project  Description of the Parameter
     */
    public JSStubsJUnitOptionPane(String project) {
        super("javastyle.stubs_junit", "pretty", project);
    }


    /**
     *  Description of the Method
     */
    public void _init() {
        for (int i = 0; i < STUBS.length; i++) {
            components.put(STUBS[i], addStub(STUBS[i]));
        }
        addHelpArea();
    }


    /**
     *  Called when the options dialog's `OK' button is pressed. This should
     *  save any properties saved in this option pane.
     */
    public void _save() {
        for (int i = 0; i < STUBS.length; i++) {
            ((SelectedPanel) components.get(STUBS[i])).save();
        }
    }


    /**
     *  Adds a feature to the Stub attribute of the JSStubsJUnitOptionPane
     *  object
     *
     *@param  compName  The feature to be added to the Stub attribute
     *@return           Description of the Return Value
     */
    private SelectedPanel addStub(String compName) {
        return addComponent(compName + ".descr", "stubs." + compName, new JTextField());
    }

}

