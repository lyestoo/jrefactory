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
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.acm.seguin.awt.ExceptionPrinter;
import org.acm.seguin.awt.GUIExceptionPrinter;
import org.acm.seguin.awt.TextExceptionPrinter;
import org.acm.seguin.io.AllFileFilter;
import org.acm.seguin.io.DirectoryTreeTraversal;
import org.acm.seguin.io.ExtensionFileFilter;
import org.acm.seguin.io.FileCopy;
import org.acm.seguin.pretty.PrettyPrintFile;
import org.acm.seguin.tools.install.RefactoryInstaller;
import org.acm.seguin.util.FileSettings;
import org.acm.seguin.util.MissingSettingsException;


/**
 *  Traverses a directory structure and performs all refactorings on the files.
 *
 *@author     Chris Seguin
 *@created    May 12, 1999
 */
public class PrettyPrinter extends DirectoryTreeTraversal
{
    //  Instance Variables
    private PrettyPrintFile ppf;


    /**
     *  Creates a refactory
     *
     *@param  init   the initial directory or file
     *@param  quiet  Description of Parameter
     */
    public PrettyPrinter(String init, boolean quiet)
    {
        super(init);

        if (init == null) {
            return;
        }

        ppf = new PrettyPrintFile();
        ppf.setAsk(!quiet && (new File(init)).isDirectory());
    }


    /**
     *  Determines if this file should be handled by this traversal
     *
     *@param  currentFile  the current file
     *@return              true if the file should be handled
     */
    protected boolean isTarget(File currentFile)
    {
        return (currentFile.getName().endsWith(".java"));
    }


    /**
     *  Visits the current file
     *
     *@param  currentFile  the current file
     */
    protected void visit(File currentFile)
    {
        if (ppf.isApplicable(currentFile)) {
            System.out.println("Applying the Pretty Printer:  " + currentFile.getPath());
            makeBackup(currentFile);
            ppf.apply(currentFile);
        }
    }


    /**
     *  Make a backup of the file before applying the pretty printer
     *
     *@param  currentFile  Description of Parameter
     */
    private void makeBackup(File currentFile)
    {
        String backupExt;
        try {
            backupExt = FileSettings.getRefactoryPrettySettings().getString("pretty.printer.backup.ext");
            backupExt = (backupExt == null) ? "" : backupExt.trim();
        }
        catch (MissingSettingsException mse) {
            backupExt = "";
        }

        if ((backupExt != null) && (backupExt.length() > 0)) {
            File parentDir = currentFile.getParentFile();
            String name = currentFile.getName();
            File dest = new File(parentDir, name + backupExt);
            (new FileCopy(currentFile, dest, false)).run();
        }
    }


    /**
     *  The main program
     *
     *@param  args  Description of Parameter
     */
    public static void main(String[] args)
    {
        try {
            int lastOption = -1;
            boolean quiet = false;

            for (int ndx = 0; ndx < args.length; ndx++) {
                if (args[ndx].equals("-quiet") || args[ndx].equals("-u")) {
                    quiet = true;
                    lastOption = ndx;
                    ExceptionPrinter.register(new TextExceptionPrinter());
                }
                else if (args[ndx].equals("-?") || args[ndx].equalsIgnoreCase("-h") || args[ndx].equalsIgnoreCase("-help")) {
                    printHelpMessage();
                    return;
                }
                else if (args[ndx].equals("-config")) {
                    String dir = args[ndx + 1];
                    ndx++;
                    FileSettings.setSettingsRoot(dir);
                }
            }

            //  Make sure everything is installed properly
            (new RefactoryInstaller(false)).run();

            if (lastOption + 1 >= args.length) {
                // no more arguments left
                if (quiet) {
                    prettyPrinter(quiet);
                }
                else {
                    prettyPrinter(System.getProperty("user.dir"), quiet);
                }
            }
            else {
                // process remaining arguments as file / dir names
                for (int ndx = lastOption + 1; ndx < args.length; ++ndx) {
                    prettyPrinter(args[ndx], quiet);
                }
            }
        }
        catch (Throwable thrown) {
            thrown.printStackTrace(System.out);
            System.exit(1);
        }

        //  Exit
        System.exit(ExceptionPrinter.getExceptionsPrinted());
    }


    /**
     *  Refactor the current file
     *
     *@param  filename  Description of Parameter
     *@param  quiet     Description of Parameter
     */
    public static void prettyPrinter(String filename, boolean quiet)
    {
        (new PrettyPrinter(filename, quiet)).run();
    }


    /**
     *  Refactor the current file
     *
     *@param  quiet  Description of Parameter
     */
    public static void prettyPrinter(boolean quiet)
    {
        JFileChooser chooser = new JFileChooser();

        //  Create the java file filter
        ExtensionFileFilter filter = new ExtensionFileFilter();
        filter.addExtension(".java");
        filter.setDescription("Java Source Files (.java)");
        chooser.setFileFilter(filter);

        //  Add other file filters - All
        FileFilter allFilter = new AllFileFilter();
        chooser.addChoosableFileFilter(allFilter);

        //  Set it so that files and directories can be selected
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        //  Set the directory to the current directory
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

        //  Get the user's selection
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            (new PrettyPrinter(chooser.getSelectedFile().getAbsolutePath(), quiet)).run();
        }
    }


    /**
     *  Print a help message
     */
    private static void printHelpMessage()
    {
        System.out.println("Syntax:  java PrettyPrinter file   //  means refactor this file");
        System.out.println("   OR    java PrettyPrinter [-quiet|-u] dir   //  means refactor this directory");
        System.out.println("   OR    java PrettyPrinter [-quiet|-u]   //  means refactor the current directory");
        System.out.println("  the -quiet or the -u flag tells the pretty printer not to prompt the user");
    }
}
