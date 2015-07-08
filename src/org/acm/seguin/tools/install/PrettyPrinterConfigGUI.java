/*
 *  ====================================================================
 *  The JRefactory License, Version 1.0
 *
 *  Copyright (c) 2001 JRefactory.  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  JRefactory (http://www.sourceforge.org/projects/jrefactory)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "JRefactory" must not be used to endorse or promote
 *  products derived from this software without prior written
 *  permission. For written permission, please contact seguin@acm.org.
 *
 *  5. Products derived from this software may not be called "JRefactory",
 *  nor may "JRefactory" appear in their name, without prior written
 *  permission of Chris Seguin.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE CHRIS SEGUIN OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of JRefactory.  For more information on
 *  JRefactory, please see
 *  <http://www.sourceforge.org/projects/jrefactory>.
 */
package org.acm.seguin.tools.install;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.*;
import org.acm.seguin.ide.common.ExitOnCloseAdapter;
import org.acm.seguin.io.FileCopy;
import org.acm.seguin.util.FileSettings;
import org.acm.seguin.util.MissingSettingsException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *  The GUI for configuring the pretty printer
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public class PrettyPrinterConfigGUI {
    private JTabbedPane tabs;
    private LinkedList list;
    private boolean isFromCommandLine;
    private JFrame frame = null;
    private PrettyPrintBuffer prettyPrinter;


    /**
     *  Constructor for the PrettyPrinterConfigGUI object
     *
     *@param  commandLine  Description of Parameter
     */
    public PrettyPrinterConfigGUI(boolean commandLine) {
        list = new LinkedList();
        isFromCommandLine = commandLine;
    }


    /**
     *  Main processing method for the PrettyPrinterConfigGUI object
     */
    public void run() {
        backup();
        if (frame == null) {
            initializeFrame(true);
        }
        else {
            reload();
        }
        frame.setVisible(true);
    }


    /**
     *  Create a backup
     */
    public void backup() {
        FileSettings bundle = FileSettings.getRefactoryPrettySettings();
        File file = bundle.getFile();
        File backup = new File(file.getParentFile(), file.getName() + ".backup");
        (new FileCopy(file, backup, false)).run();
    }


    /**
     *  Restore the backups
     */
    public void restore() {
        FileSettings bundle = FileSettings.getRefactoryPrettySettings();
        File file = bundle.getFile();
        File backup = new File(file.getParentFile(), file.getName() + ".backup");
        (new FileCopy(backup, file, false)).run();
    }


    /**
     *  Save the pretty printer stuff to a file
     */
    public void save() {
        try {
            //File directory = new File(FileSettings.getSettingsRoot(), ".Refactory");
            File file = new File(FileSettings.getRefactorySettingsRoot(), "pretty.settings");
            System.out.println("Saving:  " + file.getPath());
            PrintWriter output = new PrintWriter(new FileWriter(file));
            output.println("#  Version");
            output.println("version=" + RefactoryInstaller.PRETTY_CURRENT_VERSION);
            output.println(" ");

            Iterator iter = list.iterator();
            while (iter.hasNext()) {
                SettingGroup next = (SettingGroup) iter.next();
                next.generateSetting(output);
            }

            output.close();
        } catch (IOException ioe) {
            ioe.printStackTrace(System.out);
        }
    }


    /**
     *  Description of the Method
     *
     *@param  item  Description of Parameter
     */
    private void add(SettingGroup item) {
        tabs.add(item.getGroupName(), new JScrollPane(item));
        list.add(item);
    }


    /**
     *  Description of the Method
     */
    private void createTabs() {
        tabs = new JTabbedPane();
        add(new SpacingGroup());
        add(new BlockGroup());
        add(new SingleLineGroup());
        add(new CStyleGroup());
        add(new JavadocGroup());
        add(new DescriptionGroup());
        add(new MiscGroup());
        add(new SortGroup());
    }


    /**
     *  Loads the editor pane with the contents
     *
     *@param  editor  the editor
     *@return         the contents of the editor
     */
    private String load(JEditorPane editor) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("package test;\n");
        buffer.append("\n");
        buffer.append("import java.io.*;\n");
        buffer.append("import java.util.*;\n");
        buffer.append("\n");
        buffer.append("public class TestClass extends Object {\n");
        buffer.append("  private String privateField;\n");
        buffer.append("  public String getField() { return privateField; }\n");
        buffer.append("  public void setField(String value) { privateField = value; }\n");
        buffer.append("  public void run() {\n");
        buffer.append("    int length = privateField.length();\n");
        buffer.append("    switch (length) {\n");
        buffer.append("      case 1:\n");
        buffer.append("        System.out.println(\"Length is one\");\n");
        buffer.append("        break;\n");
        buffer.append("      default:\n");
        buffer.append("        System.out.println(\"Length is not one\");\n");
        buffer.append("        break;\n");
        buffer.append("    }\n");
        buffer.append("  }");
        buffer.append("  private int getCode(int value) {");
        buffer.append("    if (value > 0)");
        buffer.append("      System.out.println(\"positive\");");
        buffer.append("    else");
        buffer.append("      System.out.println(\"negative\");");
        buffer.append("    ");
        buffer.append("    if (value % 2 == 0) {");
        buffer.append("      System.out.println(\"even\");");
        buffer.append("    }");
        buffer.append("    else {");
        buffer.append("      System.out.println(\"odd\");");
        buffer.append("    }");
        buffer.append("    ");
        buffer.append("    try {");
        buffer.append("       invokeOther();");
        buffer.append("    } catch (Exception exc) {");
        buffer.append("       exc.printStackTrace();");
        buffer.append("    }");
        buffer.append("  }");
        buffer.append("}\n");

        String result = buffer.toString();
        editor.setText(result);
        return result;
    }


    /**
     *  Initialize the frame
     *
     *@param  live  Description of the Parameter
     */
    protected void initializeFrame(boolean live) {
        frame = new JFrame("Pretty Printer Configuration");
        createTabs();
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.anchor = constraints.WEST;
        constraints.fill = constraints.BOTH;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.ipadx = 0;
        constraints.ipady = 0;
        panel.add(tabs, constraints);

        JEditorPane editor = new JEditorPane();
        editor.setFont(new Font("Monospaced", Font.PLAIN, 10));
        String contents = load(editor);
        JScrollPane scrollPane = new JScrollPane(editor);
        constraints.gridx = 1;
        panel.add(scrollPane, constraints);
        if (live) {
            prettyPrinter = new PrettyPrintBuffer(editor, contents);
            prettyPrinter.prettyPrintCurrentWindow();
        }

        frame.getContentPane().add(panel);
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menuBar.add(menu);
        JMenuItem item = new JMenuItem("Save");
        item.addActionListener(new SaveAdapter());
        menu.add(item);
        item = new JMenuItem("Restore");
        item.addActionListener(new RestoreAdapter());
        menu.add(item);
        item = new JMenuItem("Close");
        item.addActionListener(new CloseAdapter());
        menu.add(item);
        frame.setJMenuBar(menuBar);
        frame.pack();

        if (isFromCommandLine) {
            frame.addWindowListener(new ExitOnCloseAdapter());
        }
        else {
            frame.addWindowListener(new CloseFrameAdapter());
        }
    }


    /**
     *  The main program for the PrettyPrinterConfigGUI class
     *
     *@param  args  The command line arguments
     */
    public static void main(String[] args) {
        (new PrettyPrinterConfigGUI(true)).run();
    }


    /**
     *  Description of the Class
     *
     *@author     Chris Seguin
     *@created    September 12, 2001
     */
    private class SaveAdapter implements ActionListener {
        /**
         *  Description of the Method
         *
         *@param  evt  Description of Parameter
         */
        public void actionPerformed(ActionEvent evt) {
            save();
            prettyPrinter.prettyPrintCurrentWindow();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     Chris Seguin
     *@created    September 12, 2001
     */
    private class RestoreAdapter implements ActionListener {
        /**
         *  Description of the Method
         *
         *@param  evt  Description of Parameter
         */
        public void actionPerformed(ActionEvent evt) {
            restore();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     Chris Seguin
     *@created    September 12, 2001
     */
    private class CloseAdapter implements ActionListener {
        /**
         *  Description of the Method
         *
         *@param  evt  Description of Parameter
         */
        public void actionPerformed(ActionEvent evt) {
            if (isFromCommandLine) {
                System.exit(0);
            }
            else {
                frame.setVisible(false);
            }
        }
    }


    /**
     *  Simple adapter that exits the application when the frame is closed
     *
     *@author     Chris Seguin
     *@created    September 12, 2001
     */
    class CloseFrameAdapter extends WindowAdapter {
        /**
         *  The window is closing
         *
         *@param  evt  Description of Parameter
         */
        public void windowClosing(WindowEvent evt) {
            frame.setVisible(false);
        }
    }


    /**
     *  Reloads all the values from the file
     */
    public void reload() {
    	FileSettings bundle = FileSettings.getRefactoryPrettySettings();
    	bundle.setReloadNow(true);

        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            SettingGroup next = (SettingGroup) iter.next();
            next.reload();
        }

        prettyPrinter.prettyPrintCurrentWindow();
    }
}
