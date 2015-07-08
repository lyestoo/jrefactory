/* ====================================================================
 * The JRefactory License, Version 1.0
 *
 * Copyright (c) 2001 JRefactory.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        JRefactory (http://www.sourceforge.org/projects/jrefactory)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "JRefactory" must not be used to endorse or promote
 *    products derived from this software without prior written
 *    permission. For written permission, please contact seguin@acm.org.
 *
 * 5. Products derived from this software may not be called "JRefactory",
 *    nor may "JRefactory" appear in their name, without prior written
 *    permission of Chris Seguin.
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
package org.acm.seguin.tools.stub;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import org.acm.seguin.awt.CenterDialog;
import org.acm.seguin.io.ExtensionFileFilter;

/**
 *  Asks the user where to start loading the JDK source code files
 *
 *@author     Chris Seguin
 *@author     mike Atkinson
 *@created    September 12, 2001
 */
public class StubPrompter extends JDialog implements ActionListener {
    private JTextArea filename;
    private File result;

    private String jdk = null;
    

    private String[] JDK_instructions = new String[] {
        "To effectively use this tool it is necessary to have",
        "some overview of the Java Development Kit's",
        "source code.",
        "Please enter the jar or zip file that contains the",
        "source.  It is usually called src.zip."
    };
    
    private String[] generalInstructions = new String[] {
        "A stub may be obtained for any set of source files.",
        "These are used to generate UML diagrams of ",
    };
    
    /**
     *  Constructor for the StubPrompter object
     *
     *@param  frame   Description of Parameter
     *@param  output  Description of Parameter
     *@param  findJDK get the stubs for the JDK
     */
    public StubPrompter(JFrame frame, File output, boolean findJDK) {
        super(frame, (findJDK) ? "JDK Summary Generator" : "Summary Generator", true);

        result = output;

        setSize(300, 205);
        getContentPane().setLayout(null);

        String[] instructionText = (findJDK) ? JDK_instructions : generalInstructions;

        int location = 0;
        int height = 0;
        for (int i=0; i<instructionText.length; i++) {
            JLabel instructions = new JLabel(instructionText[i]);
            instructions.setLocation(5, 5 + location);
            instructions.setSize(instructions.getPreferredSize());
            height = instructions.getPreferredSize().height;
            location += height;
            getContentPane().add(instructions);
        }
        location += height;

        filename = new JTextArea();
        filename.setLocation(5, 15 + location);
        filename.setSize(190, 25);
        getContentPane().add(filename);

        JButton browse = new JButton("Browse");
        browse.setLocation(200, 15 + location);
        browse.setSize(85, 25);
        getContentPane().add(browse);
        browse.addActionListener(this);

        JButton okButton = new JButton("OK");
        okButton.setLocation(5, 45 + location);
        okButton.setSize(85, 25);
        getContentPane().add(okButton);
        okButton.addActionListener(this);

        CenterDialog.center(this, frame);
    }


    /**
     *  The user has pressed a button. Handle the action appropriately.
     *
     *@param  evt  A description of the action
     */
    public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand().equals("OK")) {
            //System.out.println("OK button:  " + filename.getText());
            String name = filename.getText();
            File file = new File(name);
            boolean fileOK = checkJDK(name);
            if (fileOK && file.exists()) {
                setModal(false);
                dispose();
                new StubGenerator(name, result).run();
            } else if (!fileOK) {
                JOptionPane.showMessageDialog(this,
                        "The file you entered does not seem to be the source\n" + 
                        "for the Java JDK. It should be in a directory containing\n" +
                        "a string \"1.3.x\", \"1.4.x\" or \"1.5.x\"\n"+
                        "The JDKs for versions before 1.3 do not conform to the\n" +
                        "current Java language specification",
                        "File not correct",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "The file you entered does not exist.\nPlease select the source code for the JDK.",
                        "File does not exist",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else if (evt.getActionCommand().equals("Browse")) {
            //System.out.println("Browse button");
            JFileChooser chooser = new JFileChooser();

            ExtensionFileFilter jarFilter = new ExtensionFileFilter();
            jarFilter.addExtension(".jar");
            jarFilter.setDescription("Jar files");
            chooser.addChoosableFileFilter(jarFilter);

            ExtensionFileFilter zipFilter = new ExtensionFileFilter();
            zipFilter.addExtension(".zip");
            zipFilter.setDescription("Zip files");
            chooser.setFileFilter(zipFilter);

            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selected = chooser.getSelectedFile();
                String path = null;
                try {
                    path = selected.getCanonicalPath();
                } catch (IOException ioe) {
                    path = selected.getPath();
                }
                filename.setText(path);
            }
        }
    }

    private boolean checkJDK(String name) {
        if (name.indexOf("1.5") >=0) {
            if (name.indexOf("1.5.1")>=0) {
                jdk="1.5.1";
            } else if (name.indexOf("1.5.2")>=0) {
                jdk="1.5.2";
            } else {
                jdk="1.5.0";
            }
        } else if (name.indexOf("1.4") >=0) {
            if (name.indexOf("1.4.1")>=0) {
                jdk="1.4.1";
            } else if (name.indexOf("1.4.2")>=0) {
                jdk="1.4.2";
            } else if (name.indexOf("1.4.3")>=0) {
                jdk="1.4.3";
            } else {
                jdk="1.4.0";
            }
        } else if (name.indexOf("1.3") >=0) {
            if (name.indexOf("1.3.1")>=0) {
                jdk="1.3.1";
            } else {
                jdk="1.3.0";
            }
        } else if (name.indexOf("1.2") >=0) {
            if (name.indexOf("1.2.1")>=0) {
                jdk="1.2.2";
            } else if (name.indexOf("1.2.2")>=0) {
                jdk="1.2.2";
            } else {
                jdk="1.2.0";
            }
            return false;
        } else {
            jdk="1.1";
            return false;
        }
        return true;
    }

    /**
     *  The main program for the StubPrompter class
     *
     *@param  args  The command line arguments
     */
    public static void main(String[] args) {
        (new StubPrompter(null, new File("c:\\temp\\test.stub"), false)).setVisible(true);
    }
}
