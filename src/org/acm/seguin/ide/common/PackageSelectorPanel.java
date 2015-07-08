/*
 *  ====================================================================
 *  The JRefactory License, Version 1.0
 *
 *  Copyright (c) 2003 JRefactory.  All rights reserved.
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
package org.acm.seguin.ide.common;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.acm.seguin.ide.command.UMLFrame;
import org.acm.seguin.io.Saveable;
import org.acm.seguin.summary.PackageSummary;
import org.acm.seguin.uml.loader.Reloader;


/**
 *  Creates a panel for the selection of packages to view.
 *
 * @author     Mike Atkinson
 * @created    June 26, 2003
 */
public class PackageSelectorPanel extends org.acm.seguin.ide.command.PackageSelectorPanel
       implements ActionListener, Saveable, Reloader {

   /**
    *  Constructor for the PackageSelectorPanel object
    *
    * @param  root  The root directory
    */
   protected PackageSelectorPanel(String root) {
      super(root);
   }


   /**
    *  Handle the button press events
    *
    * @param  evt  the event
    */
   public void actionPerformed(ActionEvent evt) {
      String command = evt.getActionCommand();
      //if (command.equals("xxx")) {
      //} else {
      super.actionPerformed(evt);
      //}
   }


   /**
    *  Get the package from the central store
    *
    * @param  summary  The package summary that we are looking for
    * @return          The UML package
    */
   protected UMLFrame getPackage(PackageSummary summary) {
      return (UMLFrame)viewList.get(summary);
   }



   /**
    *  Creates the frame
    *
    * @return    Description of the Returned Value
    */
   protected JFrame createFrame() {
      JFrame frame = new JFrame("Package Selector");
      frame.getContentPane().add(panel);
      return frame;
   }


   /**
    *  Creates the content panel
    *
    * @return    Description of the Returned Value
    */
   protected JPanel createMainPanel() {
      JPanel panel = new JPanel();
      panel.setLayout(new BorderLayout());

      JScrollPane scrollPane = getScrollPane();
      //scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
      panel.add(scrollPane, BorderLayout.CENTER);
      panel.add(buttons, BorderLayout.NORTH);
      return panel;
   }


   /**
    *  Create the panel holding the buttons
    *
    * @param  listener  Description of Parameter
    * @return           Description of the Returned Value
    */
   protected JPanel createButtons(ActionListener listener) {
      JPanel panel = new JPanel();
      JButton showButton = new JButton("Show");
      showButton.setBounds(0, 10, 100, 25);
      panel.add(showButton);
      showButton.addActionListener(listener);

      JButton hideButton = new JButton("Hide");
      hideButton.setBounds(0, 50, 100, 25);
      panel.add(hideButton);
      hideButton.addActionListener(listener);

      JButton reloadButton = new JButton("Reload");
      reloadButton.setBounds(0, 90, 100, 25);
      panel.add(reloadButton);
      reloadButton.addActionListener(listener);
      /*
       *  JButton reloadAllButton = new JButton("Reload All");
       *  reloadAllButton.setBounds(0, 130, 100, 25);
       *  reloadAllButton.setEnabled(false);
       *  panel.add(reloadAllButton);
       */
      return panel;
   }


   /**
    *  Get the main panel
    *
    * @param  directory  Description of Parameter
    * @return            The MainPanel value
    */
   public static org.acm.seguin.ide.command.PackageSelectorPanel getMainPanel(String directory) {
      if (mainPanel == null) {
         if (directory == null) {
            return null;
         }

         mainPanel = new PackageSelectorPanel(directory);
      }
      return mainPanel;
   }
}

