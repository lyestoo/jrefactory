package org.acm.seguin.ide.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import org.acm.seguin.parser.JavaParser;
import org.acm.seguin.parser.ParseException;

import org.acm.seguin.parser.ast.ASTCompilationUnit;
import org.acm.seguin.parser.ast.SimpleNode;
import org.acm.seguin.pmd.jaxen.DocumentNavigator;

import org.jaxen.BaseXPath;
import org.jaxen.JaxenException;
import org.jaxen.XPath;

/**
 *  Description of the Class
 *
 *@author    Mike Atkinson
 */
public class ASTViewerPane extends JPanel {

   private JTextArea astArea = new JTextArea();
   private JTextArea xpathResultArea = new JTextArea();
   private JTextArea xpathQueryArea = new JTextArea(6, 40);

   private JSplitPane upperSplitPane;
   private Frame view;

   private final static String SETTINGS_FILE_NAME = System.getProperty("user.home") + System.getProperty("file.separator") + ".pmd_astviewer";


   /**
    *  Constructor for the ASTViewerPane object
    *
    *@param  aView  Description of Parameter
    */
   public ASTViewerPane(Frame aView) {
      this.view = aView;
      setLayout(new BorderLayout());

      JSmartPanel astPanel = new JSmartPanel();
      astArea.setRows(20);
      astArea.setColumns(20);
      JScrollPane astScrollPane = new JScrollPane(astArea);
      astPanel.add(astScrollPane, 0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0));

      JSmartPanel xpathResultPanel = new JSmartPanel();
      xpathResultArea.setRows(20);
      xpathResultArea.setColumns(20);
      JScrollPane xpathResultScrollPane = new JScrollPane(xpathResultArea);
      xpathResultPanel.add(xpathResultScrollPane, 0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0));

      JButton goButton = new JButton("Go");
      goButton.setMnemonic('g');
      goButton.addActionListener(new ShowListener());
      goButton.addActionListener(new XPathListener());

      JPanel controlPanel = new JPanel();
      controlPanel.setLayout(new BorderLayout());
      controlPanel.add(new JLabel("XPath Query (if any) ->"), BorderLayout.NORTH);
      xpathQueryArea.setBorder(BorderFactory.createLineBorder(Color.black));
      controlPanel.add(new JScrollPane(xpathQueryArea));
      controlPanel.add(goButton, BorderLayout.EAST);

      upperSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, astPanel, xpathResultPanel);

      add(upperSplitPane);
      add(controlPanel, BorderLayout.SOUTH);
   }

   public void setView(Frame view) {
      this.view = view;
   }
   

   /**  Description of the Method */
   public void initDividers() {
      upperSplitPane.setDividerLocation(2 * upperSplitPane.getMaximumDividerLocation() / 3);
   }


   /**
    *  Description of the Class
    *
    *@author    Mike Atkinson
    */
   private class ShowListener implements ActionListener {
      /**
       *  Description of the Method
       *
       *@param  ae  Description of Parameter
       */
      public void actionPerformed(ActionEvent ae) {
         //Buffer buffer = view.getBuffer();
         //StringReader sr = new StringReader(buffer.getText(0, buffer.getLength()));
         StringReader sr = new StringReader(IDEPlugin.getText(view, null));
         JavaParser parser = new JavaParser(sr);
         //MyPrintStream ps = new MyPrintStream();
         //System.setOut(ps);
         try {
            ASTCompilationUnit c = parser.CompilationUnit();
            //c.dump("");
            astArea.setText(c.dumpString("\r\n"));
         } catch (ParseException pe) {
            astArea.setText(pe.fillInStackTrace().getMessage());
         }
      }
   }


   /**
    *  Description of the Class
    *
    *@author    Mike Atkinson
    */
   private class XPathListener implements ActionListener {
      /**
       *  Description of the Method
       *
       *@param  ae  Description of Parameter
       */
      public void actionPerformed(ActionEvent ae) {
         if (xpathQueryArea.getText().length() == 0) {
            xpathResultArea.setText("XPath query field is empty");
            return;
         }
         //Buffer buffer = view.getBuffer();
         //StringReader sr = new StringReader(buffer.getText(0, buffer.getLength()));
         StringReader sr = new StringReader(IDEPlugin.getText(view, null));
         JavaParser parser = new JavaParser(sr);
         try {
            XPath xpath = new BaseXPath(xpathQueryArea.getText(), new DocumentNavigator());
            ASTCompilationUnit c = parser.CompilationUnit();
            StringBuffer sb = new StringBuffer();
            for (Iterator iter = xpath.selectNodes(c).iterator(); iter.hasNext(); ) {
               SimpleNode node = (SimpleNode) iter.next();
               String name = node.getClass().getName().substring(node.getClass().getName().lastIndexOf('.') + 1);
               String line = " at line " + String.valueOf(node.getBeginLine());
               sb.append(name).append(line).append(System.getProperty("line.separator"));
            }
            xpathResultArea.setText(sb.toString());
            if (sb.length() == 0) {
               xpathResultArea.setText("No results returned " + System.currentTimeMillis());
            }
         } catch (ParseException pe) {
            xpathResultArea.setText(pe.fillInStackTrace().getMessage());
         } catch (JaxenException je) {
            xpathResultArea.setText(je.fillInStackTrace().getMessage());
         }
         xpathQueryArea.requestFocus();
      }
   }


   /**
    *  Description of the Class
    *
    *@author    Mike Atkinson
    */
   public static class JSmartPanel extends JPanel {

      private GridBagConstraints constraints = new GridBagConstraints();


      /**  Constructor for the JSmartPanel object */
      public JSmartPanel() {
         super(new GridBagLayout());
      }


      /**
       *  Description of the Method
       *
       *@param  comp        Description of Parameter
       *@param  gridx       Description of Parameter
       *@param  gridy       Description of Parameter
       *@param  gridwidth   Description of Parameter
       *@param  gridheight  Description of Parameter
       *@param  weightx     Description of Parameter
       *@param  weighty     Description of Parameter
       *@param  anchor      Description of Parameter
       *@param  fill        Description of Parameter
       *@param  insets      Description of Parameter
       */
      public void add(Component comp, int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor, int fill, Insets insets) {
         constraints.gridx = gridx;
         constraints.gridy = gridy;
         constraints.gridwidth = gridwidth;
         constraints.gridheight = gridheight;
         constraints.weightx = weightx;
         constraints.weighty = weighty;
         constraints.anchor = anchor;
         constraints.fill = fill;
         constraints.insets = insets;

         add(comp, constraints);
      }
   }


   /**
    *  Description of the Class
    *
    *@author    Mike Atkinson
    */
   private static class MyPrintStream extends PrintStream {

      private StringBuffer buf = new StringBuffer();


      /**  Constructor for the MyPrintStream object */
      public MyPrintStream() {
         super(System.out);
      }


      /**
       *  Gets the String attribute of the MyPrintStream object
       *
       *@return    The String value
       */
      public String getString() {
         return buf.toString();
      }


      /**
       *  Description of the Method
       *
       *@param  s  Description of Parameter
       */
      public void println(String s) {
         super.println(s);
         buf.append(s);
         buf.append(System.getProperty("line.separator"));
      }
   }

}

