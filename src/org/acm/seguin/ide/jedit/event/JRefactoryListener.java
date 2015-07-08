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

import java.util.EventListener;

/**
 *  A listener for {@link JRefactoryEvent}s.
 *
 *@author     <a href="mailto:JRefactoryPlugin@ladyshot.demon.co.uk">Mike
 *      Atkinson</a>
 *@created    23 July 2003
 *@version    $Id: JRefactoryListener.java,v 1.1 2003/09/17 19:52:50 mikeatkinson Exp $
 *@since      0.0.1
 */
public interface JRefactoryListener extends EventListener {

    /**
     *  Notifies the changing of the active project.
     *
     *@param  evt  Description of the Parameter
     */
    public void projectLoaded(JRefactoryEvent evt);


    /**
     *  Notifies the creation of a project.
     *
     *@param  evt  Description of the Parameter
     */
    public void projectAdded(JRefactoryEvent evt);


    /**
     *  Notifies the removal of a project.
     *
     *@param  evt  Description of the Parameter
     */
    public void projectRemoved(JRefactoryEvent evt);

}

