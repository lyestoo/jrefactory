/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.uml.refactor;

import org.acm.seguin.uml.UMLPackage;
import org.acm.seguin.summary.TypeSummary;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JDialog;

/**
 *  Adds a rename class listener 
 *
 *@author    Chris Seguin 
 */
public class AddRenameClassListener extends DialogViewListener {
	private UMLPackage current;
	private TypeSummary typeSummary;


	/**
	 *  Constructor for the AddRenameClassListener object 
	 *
	 *@param  initPackage  Description of Parameter 
	 *@param  initType     Description of Parameter 
	 *@param  initMenu     The popup menu 
	 *@param  initItem     The current item 
	 */
	public AddRenameClassListener(UMLPackage initPackage, TypeSummary initType, 
			JPopupMenu initMenu, JMenuItem initItem) {
		super(initMenu, initItem);
		current = initPackage;
		typeSummary = initType;
	}


	/**
	 *  Creates an appropriate dialog to prompt the user for additional input 
	 *
	 *@return    the dialog box 
	 */
	protected JDialog createDialog() {
		return new RenameClassDialog(current, typeSummary);
	}
}
