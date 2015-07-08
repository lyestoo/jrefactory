/*
 * Author:  Chris Seguin
 *
 * This software has been developed under the copyleft
 * rules of the GNU General Public License.  Please
 * consult the GNU General Public License for more
 * details about use and distribution of this software.
 */
package org.acm.seguin.uml;

import java.io.IOException;
import java.util.Iterator;
import javax.swing.*;
import org.acm.seguin.summary.PackageSummary;

/**
 *  Just the package selector area
 *
 *@author    Chris Seguin
 */
public class PackageSelectorArea extends JPanel {
	/**
	 *  Description of the Field
	 */
	protected JList listbox;


	/**
	 *  Constructor for the PackageSelectorArea object
	 *
	 */
	public PackageSelectorArea() {
		//  Setup the UI
		setLayout(null);
		setSize(350, 350);

		//  Create the list
		listbox = new JList();

		JScrollPane pane = new JScrollPane(listbox);
		pane.setBounds(10, 10, 200, 280);
		add(pane);
	}


	/**
	 *  Gets the Selection attribute of the PackageSelectorArea object
	 *
	 *@return    The Selection value
	 */
	public PackageSummary getSelection() {
		Object[] selection = listbox.getSelectedValues();
		return (PackageSummary) selection[0];
	}


	/**
	 *  Load the summaries
	 */
	public void loadSummaries() {
		//  Do nothing
	}


	/**
	 *  Loads the package into the listbox
	 */
	public void loadPackages() {
		PackageSummaryListModel model = new PackageSummaryListModel();

		//  Get the list of packages
		Iterator iter = PackageSummary.getAllPackages();
		if (iter == null) {
			return;
		}

		//  Add in the packages
		UMLPackage view = null;
		PackageSummary packageSummary = null;
		while (iter.hasNext()) {
			packageSummary = (PackageSummary) iter.next();
			if (packageSummary.getFileSummaries() != null) {
				model.add(packageSummary);
			}
		}

		//  Set the model
		listbox.setModel(model);
	}
}
