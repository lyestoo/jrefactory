/*
 *  Copyright 1999
 *
 *  Chris Seguin
 *  All rights reserved
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import org.acm.seguin.refactor.RefactoringException;
import org.acm.seguin.refactor.RefactoringFactory;
import org.acm.seguin.refactor.type.MoveClass;
import org.acm.seguin.tools.RefactoryInstaller;
import org.acm.seguin.util.FileSettings;


/**
 *  Main program for repackaging. This object simply stores the main program and
 *  interprets the command line arguments for repackaging one or more files. It
 *  has two options --package or nopackage. package builds a specified package
 *  hierarchy and nopackage removes the package hierarchy and updates the java
 *  file(s) accordingly This program uses relative pathname to create a new
 *  package. So it may give unexpectd results if run on a file whose package
 *  name is not in accordance with its package hierarchy
 *
 *@author      Chris Seguin
 *@created     June 2, 1999
 *@Modified    by Praveen on Feb 2002 Incorporated better messages
 */
public class Repackage
{
    private boolean setPackage = false;
    private boolean atLeastOneClass = false;
    //  Instance Variables
    private MoveClass moveClass;


    /**
     *  Initialize the variables with command line arguments
     *
     *@param  args  the command line arguments
     *@return       true if we should continue processing
     */
    public boolean init(String[] args)
    {
        int nCurrentArg = 0;

        while (nCurrentArg < args.length) {
            if (args[nCurrentArg].equalsIgnoreCase("-dir")) {
                moveClass.setDirectory(args[nCurrentArg + 1]);
                nCurrentArg += 2;
            }
            else if (args[nCurrentArg].equalsIgnoreCase("-package")) {
                moveClass.setDestinationPackage(args[nCurrentArg + 1]);
                nCurrentArg += 2;
                setPackage = true;
            }
            else if (args[nCurrentArg].equalsIgnoreCase("-nopackage")) {
                moveClass.setDestinationPackage("");
                nCurrentArg++;
                setPackage = true;
            }
            else if (args[nCurrentArg].equalsIgnoreCase("-file")) {
                String filename = args[nCurrentArg + 1];
                load(filename);
                nCurrentArg += 2;
                atLeastOneClass = true;
            }
            else if ((args[nCurrentArg].equalsIgnoreCase("-help")) ||
                    (args[nCurrentArg].equalsIgnoreCase("--help"))) {
                printHelpMessage();
                nCurrentArg++;
                return false;
            }
            else {
                moveClass.add(args[nCurrentArg]);
                nCurrentArg++;
                atLeastOneClass = true;
            }
        }

        return atLeastOneClass && setPackage;
    }


    /**
     *  Actual work of the main program occurs here
     *
     *@param  args                      the command line arguments
     *@exception  RefactoringException  Description of Exception
     */
    public void run(String[] args) throws RefactoringException
    {
        moveClass = RefactoringFactory.get().moveClass();
        if (args.length == 0) {
            printHelpMessage();
        }
        try {
            if (init(args)) {
                moveClass.run();
            }
            //catches if the number of arguments are not as expected e.g. no dir for -dir
        }
        catch (Exception aoe) {
            printHelpMessage();
        }
    }


    /**
     *  Print the help message
     */
    protected void printHelpMessage()
    {
        System.out.println("Syntax:  java Repackage \\ ");
        System.out.println("        [-dir <dir>] [-help | --help] ");
        System.out.println("        [-package <packagename> | -nopackage] (<file.java>)*");
        System.out.println("");
        System.out.println("  where:");
        System.out.println("    <dir>        is the name of the directory containing the files");
        System.out.println("    <package>    is the name of the new package");
        System.out.println("    <nopackage>  is the way of flatening package hieracrhy");
        System.out.println("    <file.java>  is the name of the java file(s) separated");
        System.out.println("                 by whitespace to be moved");
        System.out.println("    The syntax is case insensitive. ");
    }


    /**
     *  Loads a file listing the names of java files to be moved
     *
     *@param  filename  the name of the file
     */
    private void load(String filename)
    {
        try {
            BufferedReader input = new BufferedReader(new FileReader(filename));
            String line = input.readLine();
            if (line == null) {
                System.out.println("Should have atleast one file name");
                printHelpMessage();
            }
            while (line != null) {
                StringTokenizer tok = new StringTokenizer(line);
                while (tok.hasMoreTokens()) {
                    String next = tok.nextToken();
                    System.out.println("Adding:  " + next);
                    moveClass.add(next);
                }
                line = input.readLine();
            }
            input.close();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    /**
     *  Main program
     *
     *@param  args  the command line arguments
     */
    public static void main(String[] args)
    {
        try {
            for (int ndx = 0; ndx < args.length; ndx++) {
                if (args[ndx].equals("-config")) {
                    String dir = args[ndx + 1];
                    ndx++;
                    FileSettings.setSettingsRoot(dir);
                }
            }

            //  Make sure everything is installed properly
            (new RefactoryInstaller(true)).run();

            (new Repackage()).run(args);
        }
        catch (Throwable thrown) {
            thrown.printStackTrace(System.out);
        }

        System.exit(0);
    }
}

