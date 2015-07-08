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
package org.acm.seguin.ide.jbuilder;

import com.borland.primetime.ide.Context;
import com.borland.primetime.node.Node;
import com.borland.primetime.viewer.AbstractNodeViewer;
import java.io.InputStreamReader;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import org.acm.seguin.awt.ExceptionPrinter;
import org.acm.seguin.ide.common.ClassDiagramReloader;
import org.acm.seguin.ide.common.DividedSummaryPanel;
import org.acm.seguin.summary.PackageSummary;
import org.acm.seguin.uml.UMLPackage;

/**
 *  Stores a view of a UML class diagram
 *
 *@author     Chris Seguin
 *@author     <a href="JRefactory@ladyshot.demon.co.uk">Mike Atkinson</a>
 *@version    $Id: UMLNodeViewer.java,v 1.4 2003/07/29 20:51:52 mikeatkinson Exp $ 
 *@created    October 18, 2001
 */
public class UMLNodeViewer extends AbstractNodeViewer {
    private UMLPackage diagram;
    private ClassDiagramReloader reloader;


    /**
     *  Constructor for the UMLNodeViewer object
     *
     *@param  summary  Description of Parameter
     *@param  init     Description of Parameter
     */
    public UMLNodeViewer(PackageSummary summary, ClassDiagramReloader init) {
        super(null);
        diagram = new UMLPackage(summary);
        reloader = init;
        reloader.add(diagram);
    }


    /**
     *  Constructor for the UMLNodeViewer object
     *
     *@param  context  Description of Parameter
     *@param  init     Description of Parameter
     */
    public UMLNodeViewer(Context context, ClassDiagramReloader init) {
        super(context);

        Node node = context.getNode();
        if (node instanceof UMLNode) {
            UMLNode umlNode = (UMLNode) node;
            diagram = umlNode.getDiagram();
            if (diagram == null) {
                try {
                    diagram = new UMLPackage(new InputStreamReader(umlNode.getInputStream()));
                }
                catch (IOException ioe) {
                    ExceptionPrinter.print(ioe, false);
                    diagram = null;
                }
                umlNode.setDiagram(diagram);
            }
        }
        else {
            diagram = null;
        }

        reloader = init;
        reloader.add(diagram);
    }


    /**
     *  Gets the Diagram attribute of the UMLNodeViewer object
     *
     *@return    The Diagram value
     */
    public UMLPackage getDiagram() {
        return diagram;
    }


    /**
     *  Gets the ViewerTitle attribute of the UMLNodeViewer object
     *
     *@return    The ViewerTitle value
     */
    public String getViewerTitle() {
        return "Class Diagram";
    }


    /**
     *  Creates a summary component, which is blank
     *
     *@return    the component
     */
    public JComponent createStructureComponent() {
        DividedSummaryPanel dsp =
                new DividedSummaryPanel(diagram.getSummary(), diagram);
        return dsp.getPane();
    }


    /**
     *  Creates the main viewer
     *
     *@return    the viewer
     */
    public JComponent createViewerComponent() {
        if (diagram == null) {
            return null;
        }
        JScrollPane pane = new JScrollPane(diagram,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        JScrollBar horiz = pane.getHorizontalScrollBar();
        horiz.setUnitIncrement(400);
        JScrollBar vert = pane.getVerticalScrollBar();
        vert.setUnitIncrement(400);

        diagram.setScrollPane(pane);

        return pane;
    }


    /**
     *  Releases the viewer
     */
    public void releaseViewer() {
        try {
            diagram.save();
        }
        catch (IOException ioe) {
        }
        reloader.remove(diagram);
    }
}
//  EOF
