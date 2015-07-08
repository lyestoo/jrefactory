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
package org.acm.seguin.ide.jedit.action;


import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JComponent;

import org.gjt.sp.util.Log;
import org.gjt.sp.jedit.gui.RolloverButton;

import org.acm.seguin.ide.jedit.JRefactory;


/**
 *  An action defines an action to be taken when the user presses some menu item
 *  in the tree's context menu or a button on the toolbar.
 *
 *@author     <a href="mailto:JRefactoryPlugin@ladyshot.demon.co.uk">Mike
 *      Atkinson</a>
 *@created    23 July 2003
 *@version    $Id: Action.java,v 1.1 2003/09/17 19:52:50 mikeatkinson Exp $
 *@since      0.0.1
 */
public abstract class Action implements ActionListener, Cloneable {

    /**
     *  Description of the Field
     */
    protected JRefactory viewer;
    /**
     *  Description of the Field
     */
    protected RolloverButton tbButton;
    /**
     *  Description of the Field
     */
    protected JComponent cmItem;


    /**
     *  Returns a String that will be shown as the text of the menu item or the
     *  tooltip of the toolbar button.
     *
     *@return    The text value
     */
    public abstract String getText();


    /**
     *  Returns the icon to be shown on the toolbar button. The default
     *  implementation returns "null" so that actions that will only be used in
     *  the context menu don't need to implement this.
     *
     *@return    The icon value
     */
    public Icon getIcon() {
        return null;
    }


    /**
     *  Returns the menu item that triggers this action. This returns a
     *  JComponent, which makes it possible to add virtually anything to the
     *  menu. For example, it's possible to return a sub-menu instead of a
     *  simple menu item. The default implementation returns a menu item, which
     *  is stored in the "cmItem" variable.
     *
     *@return    The menuItem value
     */
    public JComponent getMenuItem() {
        if (cmItem == null) {
            cmItem = new JMenuItem(getText());
            ((JMenuItem) cmItem).addActionListener(this);
        }
        return cmItem;
    }


    /**
     *  Returns the toolbar button that triggers this action.
     *
     *@return    The button value
     */
    public RolloverButton getButton() {
        if (tbButton == null) {
            Icon i = getIcon();
            if (i != null) {
                tbButton = new RolloverButton(getIcon());
            } else {
                tbButton = new RolloverButton();
                tbButton.setText(getText());
            }
            tbButton.setToolTipText(getText());
            tbButton.addActionListener(this);
        }
        return tbButton;
    }


    /**
     *  Clones the current action, returning a copy of it.
     *
     *@return    Description of the Return Value
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException cnse) {
            // should not happen
            Log.log(Log.ERROR, this, cnse);
            return null;
        }
    }


    /**
     *  Sets the viewer where this action is being used.
     *
     *@param  viewer  The new viewer value
     */
    public void setViewer(JRefactory viewer) {
        this.viewer = viewer;
    }

}

