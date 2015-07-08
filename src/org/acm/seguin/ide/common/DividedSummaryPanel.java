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

import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import org.acm.seguin.summary.PackageSummary;
import org.acm.seguin.summary.SummaryTraversal;
import org.acm.seguin.uml.UMLPackage;

/**
 *  Creates an object that holds the divided summary panel
 *
 *@author     Chris Seguin
 *@created    October 18, 2001
 */
public class DividedSummaryPanel {
    JScrollPane keyPane;
    JSplitPane splitPane;
    JScrollPane summaryPane;


    /**
     *  Constructor for the DividedSummaryPanel object
     *
     *@param  summary     Description of Parameter
     *@param  umlPackage  Description of Parameter
     */
    public DividedSummaryPanel(PackageSummary summary, UMLPackage umlPackage) {
        init(summary, umlPackage);
    }


    /**
     *  Gets the Pane attribute of the DividedSummaryPanel object
     *
     *@return    The Pane value
     */
    public JComponent getPane() {
        return splitPane;
    }


    /**
     *  Initializes the splitpane
     *
     *@param  summary     Description of Parameter
     *@param  umlPackage  Description of Parameter
     */
    private void init(PackageSummary summary, UMLPackage umlPackage) {
        keyPane = new JScrollPane(new KeyPanel());
        summaryPane = new JScrollPane(new ClassListPanel(summary, umlPackage));
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, keyPane, summaryPane);
        splitPane.setDividerLocation(150);
        splitPane.setOneTouchExpandable(true);
        keyPane.setMinimumSize(new Dimension(50, 50));
        summaryPane.setMinimumSize(new Dimension(50, 50));
    }


    /**
     *  The main program for the DividedSummaryPanel class
     *
     *@param  args  The command line arguments
     */
    public static void main(String[] args) {
        (new SummaryTraversal("c:\\temp\\download")).run();
        javax.swing.JFrame frame = new javax.swing.JFrame("Divided Summary");
        DividedSummaryPanel dsp = new DividedSummaryPanel(PackageSummary.getPackageSummary("java.lang"), null);
        frame.getContentPane().add(dsp.getPane());
        frame.pack();
        frame.setSize(200, 400);
        frame.setVisible(true);
        frame.addWindowListener(new ExitOnCloseAdapter());
    }
}
//  EOF
