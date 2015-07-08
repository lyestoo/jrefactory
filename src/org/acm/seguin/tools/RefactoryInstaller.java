/*
 * ====================================================================
 * The JRefactory License, Version 1.0
 *
 * Copyright (c) 2001 JRefactory.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 * if any, must include the following acknowledgment:
 * "This product includes software developed by the
 * JRefactory (http://www.sourceforge.org/projects/jrefactory)."
 * Alternately, this acknowledgment may appear in the software itself,
 * if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "JRefactory" must not be used to endorse or promote
 * products derived from this software without prior written
 * permission. For written permission, please contact seguin@acm.org.
 *
 * 5. Products derived from this software may not be called "JRefactory",
 * nor may "JRefactory" appear in their name, without prior written
 * permission of Chris Seguin.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE CHRIS SEGUIN OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of JRefactory.  For more information on
 * JRefactory, please see
 * <http://www.sourceforge.org/projects/jrefactory>.
 */
package org.acm.seguin.tools;

import java.io.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;
import org.acm.seguin.awt.ExceptionPrinter;
import org.acm.seguin.io.FileCopy;
import org.acm.seguin.print.PrintingSettings;
import org.acm.seguin.project.Project;
import org.acm.seguin.tools.stub.StubPrompter;
import org.acm.seguin.util.FileSettings;
import org.acm.seguin.util.MissingSettingsException;

/**
 *  Installs the refactory
 *
 * @author    Chris Seguin
 * @author    Mike Atkinson
 * @created   October 1, 1999
 */
public class RefactoryInstaller implements Runnable {
    private boolean refactory;
    /**  Which version do we have */
    public final static double PRETTY_CURRENT_VERSION = 3.9;
    private final static double UML_CURRENT_VERSION = 1.2;


    /**
     *  Constructor for the RefactoryInstaller object
     *
     * @param forRefactory  is true when we are installing software for a
     *      refactory
     */
    public RefactoryInstaller(boolean forRefactory) {
        refactory = forRefactory;
    }


    /**
     *  Main procedure - actually does the work of installing the various
     *  settings files if they are not present.
     */
    public void run() {
        try {
            File homeDir = FileSettings.getSettingsRoot();
            File directory = FileSettings.getRefactorySettingsRoot();

            if (!directory.exists())
                directory.mkdirs();


            File oldUndoStack = new File(homeDir, "undo.stk");

            if (oldUndoStack.exists()) {
                File dest = new File(directory, "undo.stk");

                (new FileCopy(oldUndoStack, dest)).run();
                oldUndoStack.delete();
            }

            File oldLogFile = new File(homeDir, "log.txt");

            if (oldLogFile.exists()) {
                File dest = new File(directory, "log.txt");

                (new FileCopy(oldLogFile, dest)).run();
                oldLogFile.delete();
            }

            File file = new File(directory, "pretty.settings");
            FileWriter output;
            PrintWriter printer;

            if (!file.exists()) {
                InputStream inputStream = getClass().getResourceAsStream("/settings/.Refactory/pretty.settings");
                byte[] data = new byte[1024 * 16];
                int len = inputStream.read(data);
                OutputStream outputStream = new FileOutputStream(file);

                while (len >= 0) {
                    outputStream.write(data, 0, len);
                    len = inputStream.read(data);
                }
                outputStream.close();

                FileSettings.getRefactoryPrettySettings().setReloadNow(true);
            }
            else {
                FileSettings bundle = FileSettings.getRefactoryPrettySettings();

                bundle.setReloadNow(true);

                double version = 1.0;

                try {
                    String temp = bundle.getString("indent");
                }
                catch (MissingSettingsException mse) {
                    version = 0.5;
                }
                try {
                    version = bundle.getDouble("version");
                }
                catch (MissingSettingsException mse) {
                }

                if (version < (PRETTY_CURRENT_VERSION - 0.05)) {
                    output = new FileWriter(file.getPath(), true);
                    printer = new PrintWriter(output);
                    prettySettings(printer, version);
                    printer.flush();
                    output.flush();
                    printer.close();
                    output.close();
                }

                bundle.setReloadNow(true);
            }

            file = new File(directory, "uml.settings");
            if (!file.exists()) {
                output = new FileWriter(file);
                printer = new PrintWriter(output);
                umlSettings(printer, 0.0, homeDir);
                printer.close();
                output.close();
            }
            else {
                FileSettings bundle = FileSettings.getRefactorySettings("uml");

                bundle.setReloadNow(true);

                double version = 1.0;

                try {
                    String temp = bundle.getString("stub.dir");
                }
                catch (MissingSettingsException mse) {
                    version = 0.5;
                }
                try {
                    version = bundle.getDouble("version");
                }
                catch (MissingSettingsException mse) {
                }

                if (version < (UML_CURRENT_VERSION - 0.05)) {
                    output = new FileWriter(file.getPath(), true);
                    printer = new PrintWriter(output);
                    umlSettings(printer, version, homeDir);
                    printer.flush();
                    printer.close();
                }
            }

            file = new File(directory, "printing.settings");
            if (!file.exists())
                (new PrintingSettings()).save();

            file = new File(directory, "vss.settings");
            if (!file.exists()) {
                output = new FileWriter(file);
                printer = new PrintWriter(output);
                vssSettings(printer);
                printer.close();
                output.close();
            }

            file = new File(directory, "process.settings");
            if (!file.exists()) {
                output = new FileWriter(file);
                printer = new PrintWriter(output);
                processSettings(printer);
                printer.close();
                output.close();
            }

            file = new File(directory, "creation.txt");
            if (!file.exists())
                generateCreationText(file);
            else {
                GregorianCalendar created = new GregorianCalendar();

                created.setTime(new Date(file.lastModified()));

                GregorianCalendar lastMonth = new GregorianCalendar();

                lastMonth.add(GregorianCalendar.MONTH, -1);

                File logFile = new File(directory, "log.txt");

                if (lastMonth.after(created) && logFile.exists() && (logFile.length() > 0)) {
                    generateCreationText(file);

                    String message =
                            "Chris Seguin wrote JRefactory to discover\n" +
                            "how people use refactorings.  While you have\n" +
                            "used this tool, it has created a log of which\n" +
                            "refactorings you used.  This log contains a\n" +
                            "number representing the refactoring and a date.\n" +
                            "\n" +
                            "I would really appreciate it if you could e-mail\n" +
                            "the following file to seguin@acm.org.\n" +
                            "\n" +
                            logFile.toString() + "\n" +
                            "\n" +
                            "Thank you for taking the time to do this.\n";

                    JOptionPane.showMessageDialog(null, message,
                            "Research request",
                            JOptionPane.QUESTION_MESSAGE);
                }
            }
            file = new File(directory, "projects");
            if (!file.exists())
                Project.storeProjects();
            else
                Project.loadProjects();

        }
        catch (IOException ioe) {
            ExceptionPrinter.print(ioe, false);
        }

        if (refactory)
            jsdkStubInstall();

    }


    /**
     *  Installs properties for the pretty printer
     *
     * @param printer  The pretty printer
     * @param version  Description of Parameter
     */
    private void prettySettings(PrintWriter printer, double version) {
        System.out.println("updating pretty.settings version=" + version);
        if (version < 3.9) {
            printer.println("#  new in version 3.9 of the pretty printer ");
            printer.println(" ");
            printer.println("version=3.9");
            printer.println("enum.descr=Description of Enumeration");
            printer.println("enum.tags=author");
            printer.println("jdk=1.4.2");
            //printer.println("singleline.comment.ownline=true");
            //printer.println("date.required=false");// obsolete
            //printer.println("space.before.javadoc=true");// default (false) looks odd
            //printer.println("reformat.comments=false");// too many users complained
            //printer.println("import.sort.important=java,javax");

        }
    }


    /**
     *  Installs properties for visual source safe
     *
     * @param printer  the printer
     */
    private void vssSettings(PrintWriter printer) {
        printer.println("#  This is the full path the visual source safe's executable ");
        printer.println("vss=c:\\\\program files\\\\microsoft visual studio\\\\win32\\\\ss.exe");
        printer.println(" ");
        printer.println("#  The following are the extensions of files which are");
        printer.println("#  stored in visual source safe");
        printer.println("extension.1=.java");
        printer.println("extension.2=.properties");
        printer.println("extension.3=.xml");
        printer.println("extension.4=.html");
        printer.println("extension.5=.htm");
        printer.println(" ");
        printer.println("#  The following shows how the projects in Visual Source");
        printer.println("#  Safe map to directories on the hard disk");
        printer.println("source.1=c:\\\\java\\\\src");
        printer.println("project.1=$/Source");
        printer.println(" ");
        printer.println("source.2=c:\\\\java\\\\properties");
        printer.println("project.2=$/Properties");
        printer.println(" ");
        printer.println("source.3=c:\\\\public_html");
        printer.println("project.3=$/HTML");
        printer.println(" ");
        printer.println("source.4=c:\\\\public_html\\\\xml");
        printer.println("project.4=$/XML");
    }


    /**
     *  Installs properties for process tracking
     *
     * @param printer  the printer
     */
    private void processSettings(PrintWriter printer) {
        printer.println("#  The following settings are used to set");
        printer.println("#  up the process panel.");
        printer.println("#");
        printer.println("#  The button.name is the value that appears on the button");
        printer.println("#  The button.cmd is the value that is saved to the process");
        printer.println("#      tracking file");
        printer.println("#");
        printer.println("#  The system loads all properties starting with index 0");
        printer.println("#  and continuing until one or both of the next pair of");
        printer.println("#  values is missing.");
        printer.println(" ");
        printer.println("button.name.0=Design");
        printer.println("button.cmd.0=Design");
        printer.println(" ");
        printer.println("button.name.1=Coding");
        printer.println("button.cmd.1=Coding");
        printer.println(" ");
        printer.println("button.name.2=Unit Testing");
        printer.println("button.cmd.2=Unit Testing");
        printer.println(" ");
        printer.println("button.name.3=Verification");
        printer.println("button.cmd.3=Verification");
        printer.println(" ");
        printer.println("button.name.4=Meeting");
        printer.println("button.cmd.4=Meeting");
        printer.println(" ");
        printer.println("button.name.5=Interrupt");
        printer.println("button.cmd.5=Interrupt");
        printer.println(" ");
        printer.println("#  The name of the file to store the process data in");
        printer.println("process.file=c:\\tools\\process.txt");
    }


    /**
     *  Installs properties for process tracking
     *
     * @param printer  the printer
     * @param version  Description of Parameter
     * @param dir      Description of Parameter
     */
    private void umlSettings(PrintWriter printer, double version, File dir) {
        if (version < (UML_CURRENT_VERSION - 0.05)) {
            printer.println("");
            printer.println("#  UML File Version");
            printer.println("version=" + UML_CURRENT_VERSION);
            printer.println("");
        }

        if (version < 0.95) {
            printer.println("#  The following settings are used to set");
            printer.println("#  up the uml diagrams.");
            printer.println("");
            printer.println("#");
            printer.println("#  The directory containing the stub files");
            printer.println("#");
            printer.println("stub.dir=" + doubleBackslashes(dir.getPath()));
            printer.println("");
            printer.println("#");
            printer.println("#  Size of the box where a segmented line changes direction");
            printer.println("#");
            printer.println("sticky.point.size=3");
            printer.println("");
            printer.println("#");
            printer.println("#  Size of the area where you must be to select the sticky point");
            printer.println("#");
            printer.println("halo.size=6");
            printer.println("");
            printer.println("#");
            printer.println("#  The type of icon for the UML diagram.  The valid types are:");
            printer.println("#    colored circle - the original for specifying scope");
            printer.println("#    letter - a letter + for public, # for protected, etc");
            printer.println("#");
            printer.println("icon.type=colored circle");
            printer.println("");
        }

        if (version < 1.05) {
            printer.println("#");
            printer.println("#  A pattern to cause the loading to skip");
            printer.println("#  a particular directory.  For instance,");
            printer.println("#  .cvs means that JRefactory will skip loading");
            printer.println("#  any directory that matches *.cvs*.  Additional");
            printer.println("#  patterns can be separated by the path separator");
            printer.println("#  character");
            printer.println("#");
            printer.println("skip.dir=");
            printer.println("");
            printer.println("#  The extension to add to the existing file when it is");
            printer.println("#  refactored.  The # represents the number of the copy");
            printer.println("#  of the file");
            printer.println("#");
            printer.println("backup.ext=.#");
            printer.println("");
        }

        if (version < 1.15) {
            printer.println("#");
            printer.println("#  This is used by the command line version");
            printer.println("#  of the program to launch a source code editor");
            printer.println("#  The command line program can get either 1 or ");
            printer.println("#  2 arguments.  These are:");
            printer.println("#     $FILE - the path to the file for the editor");
            printer.println("#     $LINE - the line number");
            printer.println("#  If your editor cannot accept the line number");
            printer.println("#  command line, leave out that variable");
            printer.println("#");
            printer.println("#source.editor=notepad $FILE");
            printer.println("#source.editor=gnuclientw -F +$LINE $FILE");
        }
    }


    /**  Description of the Method */
    private void jsdkStubInstall() {
        FileSettings bundle = FileSettings.getRefactoryPrettySettings();

        bundle.setContinuallyReload(true);

        File directory;

        try {
            FileSettings umlBundle = FileSettings.getRefactorySettings("uml");

            directory = new File(umlBundle.getString("stub.dir") + File.separator + ".Refactory");
        }
        catch (MissingSettingsException mse) {
            directory = FileSettings.getRefactorySettingsRoot();
        }

        if (!directory.exists())
            directory.mkdirs();


        File outFile = new File(directory, "JDK.stub");

        if (!outFile.exists())
            (new StubPrompter(null, outFile, true)).setVisible(true);

        bundle.setContinuallyReload(false);
    }


    /**
     *  Description of the Method
     *
     * @param value  Description of Parameter
     * @return       Description of the Returned Value
     */
    private String doubleBackslashes(String value) {
        StringBuffer buffer = new StringBuffer();
        int last = value.length();

        for (int ndx = 0; ndx < last; ndx++) {
            char ch = value.charAt(ndx);

            if (ch == '\\')
                buffer.append("\\\\");
            else
                buffer.append(ch);

        }
        return buffer.toString();
    }


    /**
     *  Creates a file that marks when the refactory was installed
     *
     * @param file             The file to create
     * @exception IOException  Description of Exception
     */
    private void generateCreationText(File file) throws IOException {
        FileWriter output = new FileWriter(file);
        PrintWriter printer = new PrintWriter(output);

        printer.println("Created on " + DateFormat.getDateTimeInstance().format(new Date()));
        printer.close();
        output.close();
    }


    /**
     *  The main program
     *
     * @param args  command line arguments
     */
    public static void main(String[] args) {
        (new RefactoryInstaller(false)).run();
    }
}

