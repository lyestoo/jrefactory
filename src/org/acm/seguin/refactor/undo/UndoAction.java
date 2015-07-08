/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.refactor.undo;

import java.io.File;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import org.acm.seguin.util.FileSettings;
import org.acm.seguin.util.MissingSettingsException;

/**
 *  Stores the undo operation. The undo operation consists of a description of
 *  the refactoring that was performed to create this UndoAction and a list of
 *  files that have changed. <P>
 *
 *  The files that have changed are indexed files, in that they have an index
 *  appended to the name of the file.
 *
 *@author     Mike Atkinson (<a href="mailto:javastyle@ladyshot.demon.co.uk">Mike</a>)
 *@version    $Version: $
 *@since      2.7.05
 */
public interface UndoAction {
	/**
	 *  Sets the Description attribute of the UndoAction object
	 *
	 *@param description    The Description value
	 */
	public void setDescription(String description);


	/**
	 *  Gets the Description attribute of the UndoAction object
	 *
	 *@return    The Description value
	 */
	public String getDescription();


	/**
	 *  Adds a file to this action
	 *
	 *@param  oldFile  the original file
	 *@param  newFile  the new file
	 */
	public void add(File oldFile, File newFile);


	/**
	 *  Undo the current action
	 */
	public void undo();

}
