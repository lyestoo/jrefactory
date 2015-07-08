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
package org.acm.seguin.ide.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Iterator;
import java.util.TreeMap;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.acm.seguin.summary.FileSummary;
import org.acm.seguin.summary.PackageSummary;
import org.acm.seguin.summary.SummaryTraversal;
import org.acm.seguin.summary.TypeSummary;
import org.acm.seguin.uml.ClassIcon;
import org.acm.seguin.uml.InterfaceIcon;
import org.acm.seguin.uml.UMLPackage;

/**
 *  Holds the list of classes
 *
 *@author     Chris Seguin
 *@created    October 18, 2001
 */
public class ClassListPanel extends JPanel {
    private PackageSummary summary;
    private UMLPackage umlPackage;


    /**
     *  Constructor for the ClassListPanel object
     *
     *@param  init         Description of Parameter
     *@param  initPackage  Description of Parameter
     */
    public ClassListPanel(PackageSummary init, UMLPackage initPackage) {
        summary = init;
        umlPackage = initPackage;
        umlPackage.setClassListPanel(this);

        init();
    }


    /**
     *  Adds a feature to the TypeToPanel attribute of the ClassListPanel object
     *
     *@param  nextType  The feature to be added to the TypeToPanel attribute
     *@param  gbc       The feature to be added to the TypeToPanel attribute
     *@param  count     The feature to be added to the TypeToPanel attribute
     */
    private void addTypeToPanel(TypeSummary nextType, GridBagConstraints gbc, int count) {
        JumpToTypeAdapter jumpToType = new JumpToTypeAdapter(umlPackage, nextType);

        Icon icon;
        if (nextType.isInterface()) {
            icon = new InterfaceIcon(8, 8);
        }
        else {
            icon = new ClassIcon(8, 8);
        }
        IconPanel classPanel = new IconPanel(icon);
        gbc.gridx = 0;
        gbc.gridy = count;
        add(classPanel, gbc);
        classPanel.addMouseListener(jumpToType);

        JLabel classLabel = new JLabel(nextType.getName(), JLabel.LEFT);
        gbc.gridx = 1;
        add(classLabel, gbc);
        classLabel.addMouseListener(jumpToType);
    }


    /**
     *  Initializes the panel
     */
    private void init() {
        setLayout(new GridBagLayout());
        setBackground(Color.white);

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel title;
        if (summary == null) {
            title = new JLabel("Unknown");
        }
        else {
            title = new JLabel(summary.getName());
        }
        title.setFont(new Font("Dialog", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 10, 0, 10);
        add(title, gbc);

        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;

        int count = 1;
        Iterator iter = listTypes();
        while (iter.hasNext()) {
            TypeSummary next = (TypeSummary) iter.next();
            addTypeToPanel(next, gbc, count);
            count++;
        }

        repaint();
    }


    /**
     *  Creates a list of type summaries
     *
     *@return    Description of the Returned Value
     */
    private Iterator listTypes() {
        TreeMap map = new TreeMap();

        Iterator iter = null;
        if (summary != null) {
            iter = summary.getFileSummaries();
        }
        while ((iter != null) && iter.hasNext()) {
            FileSummary nextFileSummary = (FileSummary) iter.next();
            Iterator iter2 = nextFileSummary.getTypes();
            while ((iter2 != null) && iter2.hasNext()) {
                TypeSummary nextType = (TypeSummary) iter2.next();
                map.put(nextType.getName(), nextType);
            }
        }

        return map.values().iterator();
    }


    /**
     *  Used to reload the class list
     *
     *@param  init  Description of Parameter
     */
    public void load(PackageSummary init) {
        summary = init;
        removeAll();
        init();
    }


    /**
     *  The main program for the ClassListPanel class
     *
     *@param  args  The command line arguments
     */
    public static void main(String[] args) {
        (new SummaryTraversal("c:\\temp\\download")).run();
        javax.swing.JFrame frame = new javax.swing.JFrame("Class List");
        frame.getContentPane().add(new ClassListPanel(PackageSummary.getPackageSummary("java.lang"), null));
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(new ExitOnCloseAdapter());
    }
}
//  EOF
