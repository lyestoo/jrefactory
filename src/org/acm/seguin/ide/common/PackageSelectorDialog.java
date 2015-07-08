/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.ide.common;

import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JDialog;
import javax.swing.JButton;
import org.acm.seguin.summary.PackageSummary;
import org.acm.seguin.uml.PackageSelectorArea;

/**
 *  The package selector dialog box
 *
 *@author    Chris Seguin
 */
public class PackageSelectorDialog extends JDialog implements ActionListener {
	private PackageSelectorArea selection;
	private PackageSummary summary;


	/**
	 *  Constructor for the PackageSelectorDialog object
	 *
	 *@param  parent  the parent dialog rame
	 */
	public PackageSelectorDialog(Frame parent) {
		super(parent, "Select package to view", true);

		getContentPane().setLayout(null);
		setSize(350, 350);

		selection = new PackageSelectorArea();
		selection.loadPackages();
		selection.setSize(224, 350);
		getContentPane().add(selection);

		JButton okButton = new JButton("OK");
		okButton.setBounds(225, 10, 100, 25);
		getContentPane().add(okButton);
		okButton.addActionListener(this);
	}


	/**
	 *  Gets the summary that has been selected
	 *
	 *@return    the selected package summary
	 */
	public PackageSummary getSummary() {
		return summary;
	}


	/**
	 *  Selects the package when the user presses OK
	 *
	 *@param  evt  the action event
	 */
	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals("OK")) {
			summary = selection.getSelection();
			dispose();
		}
	}
}
