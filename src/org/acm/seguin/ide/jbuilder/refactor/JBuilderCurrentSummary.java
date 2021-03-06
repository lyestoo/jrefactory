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
package org.acm.seguin.ide.jbuilder.refactor;

import com.borland.jbuilder.node.JavaFileNode;
import com.borland.primetime.editor.EditorPane;
import com.borland.primetime.ide.Browser;
import com.borland.primetime.node.Node;
import com.borland.primetime.vfs.Buffer;
import com.borland.primetime.viewer.AbstractTextNodeViewer;
import org.acm.seguin.ide.common.action.CurrentSummary;
import org.acm.seguin.ide.common.MultipleDirClassDiagramReloader;
import org.acm.seguin.ide.jbuilder.UMLNodeViewerFactory;

/**
 *  The object that determines the current summary for the JBuilder IDE.
 *
 *@author     Chris Seguin
 *@created    October 18, 2001
 */
public class JBuilderCurrentSummary extends CurrentSummary {
    private EditorPane editor;
    private Node lastActive;


    /**
     *  Constructor for the JBuilderCurrentSummary object
     */
    public JBuilderCurrentSummary() {
        super();
        editor = null;
    }


    /**
     *  Gets the ActiveNode attribute of the CurrentSummary object
     *
     *@return    The ActiveNode value
     */
    protected Node getActiveNode() {
        Browser browser = Browser.getActiveBrowser();
        return browser.getActiveNode();
    }


    /**
     *  Gets the reloader
     *
     *@return    The MetadataReloader value
     */
    protected MultipleDirClassDiagramReloader getMetadataReloader() {
        return UMLNodeViewerFactory.getFactory().getReloader();
    }


    /**
     *  Register the current summary listener with the current document
     */
    protected void registerWithCurrentDocument() {
        Browser browser = Browser.getActiveBrowser();
        Node temp = getActiveNode();

        if (temp != lastActive) {
            lastActive = temp;
            upToDate = false;

            if (editor != null) {
                editor.getDocument().removeDocumentListener(this);
            }

            if ((lastActive == null) || !(lastActive instanceof JavaFileNode)) {
                return;
            }

            AbstractTextNodeViewer sourceViewer =
                    (AbstractTextNodeViewer) browser.getViewerOfType(lastActive, AbstractTextNodeViewer.class);
            editor = sourceViewer.getEditor();
            editor.getDocument().addDocumentListener(this);
        }
    }
}
//  EOF
