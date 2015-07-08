/*
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more detaProjectTreeSelectionListenerils.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.acm.seguin.ide.jedit.event;

import java.util.EventObject;

import org.acm.seguin.ide.jedit.JRefactory;
import org.acm.seguin.ide.jedit.UMLProject;

/**
 *  A project viewer event.
 *
 *@author     <a href="mailto:JRefactoryPlugin@ladyshot.demon.co.uk">Mike
 *      Atkinson</a>
 *@created    23 July 2003
 *@version    $Id: JRefactoryEvent.java,v 1.1 2003/09/17 19:52:50 mikeatkinson Exp $
 *@since      0.0.1
 */
public final class JRefactoryEvent extends EventObject {

    private UMLProject project;
    private JRefactory viewer;


    /**
     *  Create a new <code>JRefactoryEvent</code>.
     *
     *@param  src  the project viewer instance that fired the event.
     *@param  prj  the project loaded (null if "All Projects").
     */
    public JRefactoryEvent(JRefactory src, UMLProject prj) {
        this(src, src, prj);
    }


    /**
     *  Create a new <code>JRefactoryEvent</code>.
     *
     *@param  src  the project viewer instance that fired the event.
     *@param  prj  the project loaded (null if "All Projects").
     */
    public JRefactoryEvent(Object src, UMLProject prj) {
        this(src, (src instanceof JRefactory) ? (JRefactory) src : null, prj);
    }


    /**
     *  Create a new <code>JRefactoryEvent</code>.
     *
     *@param  src     the project viewer instance that fired the event.
     *@param  viewer  Description of the Parameter
     *@param  prj     the project loaded (null if "All Projects").
     */
    public JRefactoryEvent(Object src, JRefactory viewer, UMLProject prj) {
        super(src);
        this.viewer = viewer;
        project = prj;
    }


    /**
     *  Returns the {@link JRefactory}.
     *
     *@return    The viewer where the event occurred.
     */
    public JRefactory getJRefactory() {
        return viewer;
    }


    /**
     *  Returns the {@link UMLProject Project}. It is important to noticed that
     *  this value can be <code>null</code>, which means that the "All Projects"
     *  mode has been activated.
     *
     *@return    The activated project, or null if "All Projects" was chosen.
     */
    public UMLProject getProject() {
        return project;
    }

}

