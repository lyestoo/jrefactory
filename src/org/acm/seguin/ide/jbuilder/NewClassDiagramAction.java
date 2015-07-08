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

import com.borland.primetime.ide.Browser;
import com.borland.primetime.ide.NodeViewer;
import com.borland.primetime.node.Node;
import com.borland.primetime.node.Project;
import com.borland.primetime.vfs.Url;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import org.acm.seguin.ide.common.MultipleDirClassDiagramReloader;
import org.acm.seguin.ide.common.PackageSelectorDialog;
import org.acm.seguin.summary.PackageSummary;
import org.acm.seguin.uml.PackageLoader;

/**
 *  Package selector dialog box
 *
 *@author     Chris Seguin
 *@created    October 18, 2001
 */
public class NewClassDiagramAction extends JBuilderAction {
    /**
     *  Constructor for the PrettyPrinterAction object
     */
    public NewClassDiagramAction() {
        putValue(NAME, "New UML Class Diagram");
        putValue(SHORT_DESCRIPTION, "New UML Class Diagram");
        putValue(LONG_DESCRIPTION, "Creates a new UML class diagram");
    }


    /**
     *  Gets the Enabled attribute of the PrettyPrinterAction object
     *
     *@return    The Enabled value
     */
    public boolean isEnabled() {
        MultipleDirClassDiagramReloader reloader =
                UMLNodeViewerFactory.getFactory().getReloader();

        return reloader.isNecessary();
    }


    /**
     *  The pretty printer action
     *
     *@param  evt  the action that occurred
     */
    public void actionPerformed(ActionEvent evt) {
        Browser browser = Browser.getActiveBrowser();
        PackageSelectorDialog psd =
                new PackageSelectorDialog(browser);
        psd.setVisible(true);
        PackageSummary summary = psd.getSummary();
        if (summary == null) {
            return;
        }

        Project proj = browser.getActiveProject();
        Node parent = proj;
        File diagramFile = PackageLoader.getFile(summary);
        createFile(diagramFile, summary);
        Url url = new Url(diagramFile);
        UMLNode node = (UMLNode) proj.findNode(url);

        if (node == null) {
            try {
                node = new UMLNode(proj, parent, url);
            }
            catch (com.borland.primetime.node.DuplicateNodeException dne) {
                dne.printStackTrace(System.out);
            }
        }

        try {
            browser.setActiveNode(node, true);
//			NodeViewer[] viewers = browser.getViewers(node);
//			for (int ndx = 0; ndx < viewers.length; ndx++) {
//				if (viewers[ndx] instanceof UMLNodeViewer) {
//					browser.setActiveViewer(node, viewers[ndx], true);
//				}
//			}
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     *  Creates a file if one does not yet exist
     *
     *@param  diagramFile  the file to create
     *@param  summary      the associated package
     */
    private void createFile(File diagramFile, PackageSummary summary) {
        if (!diagramFile.exists()) {
            try {
                FileWriter output = new FileWriter(diagramFile);
                output.write("V[1.1:" + summary.getName() + "]\n");
                output.close();
            }
            catch (IOException ioe) {
            }
        }
    }
}
//  EOF
