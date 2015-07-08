/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.tools.builder;


import java.io.File;
import java.util.Iterator;
import javax.swing.JFileChooser;
import org.acm.seguin.ide.command.CommandLineSourceBrowser;
import org.acm.seguin.ide.common.SourceBrowser;
import org.acm.seguin.ide.command.PackageSelectorPanel;
import org.acm.seguin.io.AllFileFilter;
import org.acm.seguin.summary.*;
import org.acm.seguin.tools.RefactoryInstaller;
import org.acm.seguin.uml.loader.ReloaderSingleton;
import org.acm.seguin.util.FileSettings;

/**
 *  Draws a UML diagram for all the classes in a package
 *
 *@author    Chris Seguin
 */
public class Refactory {
	/**
	 *  The main program
	 *
	 *@param  args  the command line arguments
	 */
	public static void main(String[] args)
	{
        for (int ndx = 0; ndx < args.length; ndx++) {
            if (args[ndx].equals("-config")) {
                String dir = args[ndx + 1];
                ndx++;
                FileSettings.setSettingsRoot(dir);
            }
        }

		//  Make sure everything is installed properly
		(new RefactoryInstaller(true)).run();
		SourceBrowser.set(new CommandLineSourceBrowser());

		if (args.length == 0) {
			elixir();
		}
		else {
			selectionPanel(args[0]);
		}
	}


	/**
	 *  Creates the selection panel
	 *
	 *@param  directory  Description of Parameter
	 */
	public static void selectionPanel(String directory)
	{
		PackageSelectorPanel panel = PackageSelectorPanel.openMainFrame(directory);
		ReloaderSingleton.register(panel);
	}


	/**
	 *  Insertion point for elixir
	 */
	public static void elixir()
	{
		if (PackageSelectorPanel.openMainFrame(null) != null) {
			return;
		}

		JFileChooser chooser = new JFileChooser();

		//  Add other file filters - All
		AllFileFilter allFilter = new AllFileFilter();
		chooser.addChoosableFileFilter(allFilter);

		//  Set it so that files and directories can be selected
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		//  Set the directory to the current directory
		chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

		//  Get the user's selection
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			selectionPanel(chooser.getSelectedFile().getAbsolutePath());
		}
	}
}
