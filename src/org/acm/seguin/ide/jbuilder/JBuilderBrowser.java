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

import com.borland.primetime.viewer.AbstractTextNodeViewer;
import com.borland.primetime.node.FileNode;
import com.borland.primetime.ide.Browser;
import com.borland.primetime.node.Project;
import com.borland.primetime.vfs.Url;
import com.borland.primetime.editor.EditorPane;
import java.io.File;
import org.acm.seguin.ide.common.SourceBrowser;

/**
 *  Base class for source browsing. This is the generic base class.
 *
 *@author     Chris Seguin
 *@created    October 18, 2001
 */
public class JBuilderBrowser extends SourceBrowser {
    /**
     *  Determines if the system is in a state where it can browse the source
     *  code
     *
     *@return    true if the source code browsing is enabled
     */
    public boolean canBrowseSource() {
        return true;
    }


    /**
     *  Get the FileNode that matches a File (in this project).
     *
     *@param  file  File to look for in this project.
     *@return       FileNode The FileNode. *duh
     */
    protected FileNode findSourceFileNode(File file) {
        Browser browser = Browser.getActiveBrowser();
        Project project = browser.getActiveProject();
        if (project == null) {
            project = browser.getDefaultProject();
        }
        Url url = new Url(file);
        return project.getNode(url);
    }


    /**
     *  Go to a specific line in a source file.
     *
     *@param  lineNumber  Line number to go to.
     *@param  sourceNode  Source file node.
     */
    protected void gotoLine(int lineNumber, FileNode sourceNode) {
        AbstractTextNodeViewer sourceViewer =
                (AbstractTextNodeViewer) Browser.getActiveBrowser().getViewerOfType(sourceNode, AbstractTextNodeViewer.class);
        EditorPane editor = sourceViewer.getEditor();
        editor.gotoPosition(lineNumber, 1, false, EditorPane.CENTER_ALWAYS);
    }


    /**
     *  Actually browses to the file
     *
     *@param  file  the file
     *@param  line  the line in the file
     */
    public void gotoSource(File file, int line) {
        if (file != null) {
            FileNode sourceNode = findSourceFileNode(file);
            showNode(sourceNode);
            gotoLine(line, sourceNode);
        }
    }


    /**
     *  Show a source file.
     *
     *@param  node  Source file node to show.
     */
    protected void showNode(FileNode node) {
        Browser browser = Browser.getActiveBrowser();
        try {
            browser.setActiveNode(node, true);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
//  EOF
