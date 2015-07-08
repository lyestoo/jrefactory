package org.acm.seguin.ide.command;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.*;
import org.acm.seguin.summary.PackageSummary;
import org.acm.seguin.summary.SummaryTraversal;
import org.acm.seguin.io.Saveable;
import org.acm.seguin.uml.loader.Reloader;
import org.acm.seguin.uml.loader.ReloaderSingleton;
import org.acm.seguin.uml.UMLPackage;
import org.acm.seguin.uml.PackageSelectorArea;

/**
 *  Creates a panel for the selection of packages to view.
 *
 *@author     Chris Seguin
 *@created    August 10, 1999
 */
public class PackageSelectorPanel extends PackageSelectorArea
		 implements ActionListener, Saveable, Reloader {
	/**
	 *  The root directory
	 */
	protected String rootDir = null;

	//  Instance Variables
	private HashMap viewList;

	//  Class Variables
	private static PackageSelectorPanel mainPanel;


	/**
	 *  Constructor for the PackageSelectorPanel object
	 *
	 *@param  root  The root directory
	 */
	protected PackageSelectorPanel(String root) {
		super();

		//  Setup the instance variables
		setRootDirectory(root);

		ReloaderSingleton.register(this);
		ReloaderSingleton.reload();

		//  Add the buttons
		JButton showButton = new JButton("Show");
		showButton.setBounds(225, 10, 100, 25);
		add(showButton);
		showButton.addActionListener(this);

		JButton hideButton = new JButton("Hide");
		hideButton.setBounds(225, 50, 100, 25);
		add(hideButton);
		hideButton.addActionListener(this);

		JButton reloadButton = new JButton("Reload");
		reloadButton.setBounds(225, 90, 100, 25);
		add(reloadButton);
		reloadButton.addActionListener(this);

		JButton reloadAllButton = new JButton("Reload All");
		reloadAllButton.setBounds(225, 130, 100, 25);
		reloadAllButton.setEnabled(false);
		add(reloadAllButton);
		createFrame();
	}


	/**
	 *  Handle the button press events
	 *
	 *@param  evt  the event
	 */
	public void actionPerformed(ActionEvent evt) {
		String command = evt.getActionCommand();
		if (command.equals("Show")) {
			Object[] selection = listbox.getSelectedValues();
			for (int ndx = 0; ndx < selection.length; ndx++) {
				PackageSummary next = (PackageSummary) selection[ndx];
				showSummary(next);
			}
		}
		else if (command.equals("Hide")) {
			Object[] selection = listbox.getSelectedValues();
			for (int ndx = 0; ndx < selection.length; ndx++) {
				PackageSummary next = (PackageSummary) selection[ndx];
				hideSummary(next);
			}
		}
		else if (command.equals("Reload")) {
			ReloaderSingleton.reload();
		}
	}


	/**
	 *  Reloads the package information
	 */
	public void reload() {
		loadPackages();
	}


	/**
	 *  Saves the diagrams
	 *
	 *@exception  IOException  Description of Exception
	 */
	public void save() throws IOException {
		Iterator iter = viewList.keySet().iterator();
		while (iter.hasNext()) {
			PackageSummary packageSummary = (PackageSummary) iter.next();
			UMLPackage view = getPackage(packageSummary).getUmlPackage();
			view.save();
		}
	}


	/**
	 *  Loads the packages into the listbox and refreshes the UML diagrams
	 */
	public void loadPackages() {
		loadSummaries();

		super.loadPackages();

		//  Reloads the screens
		UMLPackage view = null;
		PackageSummary packageSummary = null;

		if (viewList == null) {
			viewList = new HashMap();
			return;
		}

		Iterator iter = viewList.keySet().iterator();
		while (iter.hasNext()) {
			packageSummary = (PackageSummary) iter.next();
			view = getPackage(packageSummary).getUmlPackage();
			view.reload();
		}
	}


	/**
	 *  Load the summaries
	 */
	public void loadSummaries() {
		//  Load the summaries
		(new SummaryTraversal(rootDir)).go();
	}


	/**
	 *  Set the root directory
	 *
	 *@param  root  the new root directory
	 */
	protected void setRootDirectory(String root) {
		if (root == null) {
			rootDir = System.getProperty("user.dir");
		}
		else {
			rootDir = root;
		}
	}


	/**
	 *  Get the package from the central store
	 *
	 *@param  summary  The package summary that we are looking for
	 *@return          The UML package
	 */
	protected UMLFrame getPackage(PackageSummary summary) {
		return (UMLFrame) viewList.get(summary);
	}


	/**
	 *  Add package to central store
	 *
	 *@param  summary  the summary we are adding
	 *@param  view     the associated view
	 */
	protected void addPackage(PackageSummary summary, UMLFrame view) {
		viewList.put(summary, view);
	}


	/**
	 *  Shows the summary
	 *
	 *@param  packageSummary  the summary to show
	 */
	private void showSummary(PackageSummary packageSummary) {
		UMLFrame view = getPackage(packageSummary);
		if ((view == null) && (packageSummary.getFileSummaries() != null)) {
			createNewView(packageSummary);
		}
		else if (packageSummary.getFileSummaries() == null) {
			//  Nothing to view
		}
		else {
			view.getUmlPackage().reload();
			view.setVisible(true);
		}
	}


	/**
	 *  Hide the summary
	 *
	 *@param  packageSummary  the summary to hide
	 */
	private void hideSummary(PackageSummary packageSummary) {
		UMLFrame view = getPackage(packageSummary);
		view.setVisible(false);
	}


	/**
	 *  Creates a new view
	 *
	 *@param  packageSummary  The packages summary
	 */
	private void createNewView(PackageSummary packageSummary) {
		UMLFrame frame = new UMLFrame(packageSummary);
		addPackage(packageSummary, frame);
	}


	/**
	 *  Get the main panel
	 *
	 *@param  directory  Description of Parameter
	 *@return            The MainPanel value
	 */
	public static PackageSelectorPanel getMainPanel(String directory) {
		if (mainPanel == null) {
			if (directory == null) {
				return null;
			}

			mainPanel = new PackageSelectorPanel(directory);
		}

		mainPanel.setVisible(true);
		return mainPanel;
	}


	/**
	 *  Main program for testing purposes
	 *
	 *@param  args  The command line arguments
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Syntax:  java org.acm.seguin.uml.PackageSelectorPanel <dir>");
			return;
		}

		PackageSelectorPanel panel = PackageSelectorPanel.getMainPanel(args[0]);
		ReloaderSingleton.register(panel);
	}


	/**
	 *  Creates the frame
	 */
	private void createFrame() {
		JFrame frame = new JFrame("Package Selector");
		frame.getContentPane().add(this);
		CommandLineMenu clm = new CommandLineMenu();
		frame.setJMenuBar(clm.getMenuBar(this));
		frame.addWindowListener(new ExitMenuSelection());
		frame.setSize(350, 350);
		frame.setVisible(true);
	}
}
