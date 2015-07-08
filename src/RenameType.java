import org.acm.seguin.refactor.RefactoringException;
import org.acm.seguin.refactor.RefactoringFactory;
import org.acm.seguin.refactor.type.RenameClassRefactoring;
import org.acm.seguin.tools.RefactoryInstaller;
import org.acm.seguin.util.FileSettings;


/**
 *  Main program for repackaging. This object simply stores the main program and
 *  interprets the command line arguments for repackaging one or more files.
 *
 *@author     Chris Seguin
 *@created    February 22, 2002
 */
public class RenameType
{
    //  Instance Variables
    private RenameClassRefactoring renameClass;


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
                renameClass.setDirectory(args[nCurrentArg + 1]);
                nCurrentArg += 2;
            }
            else if ((args[nCurrentArg].equalsIgnoreCase("-help")) ||
                    (args[nCurrentArg].equalsIgnoreCase("--help"))) {
                printHelpMessage();
                nCurrentArg++;
                return false;
            }
            else if (args[nCurrentArg].equalsIgnoreCase("-from")) {
                renameClass.setOldClassName(args[nCurrentArg + 1]);
                nCurrentArg += 2;
            }
            else if (args[nCurrentArg].equalsIgnoreCase("-to")) {
                renameClass.setNewClassName(args[nCurrentArg + 1]);
                nCurrentArg += 2;
            }
            else {
                System.out.println("Unknown argument:  " + args[nCurrentArg]);
                nCurrentArg++;
            }
        }

        return true;
    }


    /**
     *  Actual work of the main program occurs here
     *
     *@param  args  the command line arguments
     */
    public void run(String[] args)
    {
        renameClass = RefactoringFactory.get().renameClass();
        if (args.length == 0) {
            printHelpMessage();
        }
        try {
            if (init(args)) {
                try {
                    renameClass.run();
                }
                catch (RefactoringException re) {
                    System.out.println(re.getMessage());
                }
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
        System.out.println("Syntax:  java RenameType \\ ");
        System.out.println("        [-dir <dir>] [-help | --help] ");
        System.out.println("        -from <className> -to <className>");
        System.out.println("");
        System.out.println("  where:");
        System.out.println("    <dir>        is the name of the directory containing the files");
        System.out.println("    <className>  is the name of the class");
        System.out.println("    The syntax is case insensitive. ");
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

            (new RenameType()).run(args);
        }
        catch (Throwable thrown) {
            thrown.printStackTrace();
        }

        System.exit(0);
    }
}
