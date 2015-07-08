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

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Enumeration;
import java.awt.event.ActionEvent;

import org.gjt.sp.jedit.jEdit;

import org.acm.seguin.ide.jedit.JRefactory;
import org.acm.seguin.ide.jedit.UMLProject;

/**
 *  Removes all the files below the root from a project, and then calls the
 *  initial project importer.
 *
 *@author     <a href="mailto:JRefactoryPlugin@ladyshot.demon.co.uk">Mike
 *      Atkinson</a>
 *@created    23 July 2003
 *@version    $Id: ShowAction.java,v 1.1 2003/09/17 19:52:50 mikeatkinson Exp $
 *@since      0.0.1
 */
public class ShowAction extends Action {

    /**
     *  Returns the text to be shown on the button and/or menu item.
     *
     *@return    The text value
     */
    public String getText() {
        return jEdit.getProperty("jrefactory.action.show");
    }


    /**
     *  Reimports files below the project root.
     *
     *@param  ae  Description of the Parameter
     */
    public void actionPerformed(ActionEvent ae) {
    }

}

