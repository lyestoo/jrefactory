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
import com.borland.primetime.ide.Browser;
import com.borland.primetime.node.Node;
import com.borland.primetime.vfs.Buffer;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.acm.seguin.ide.common.MultipleDirClassDiagramReloader;
import org.acm.seguin.ide.common.action.SelectedFileSet;
import org.acm.seguin.ide.jbuilder.UMLNodeViewerFactory;
import org.acm.seguin.summary.FileSummary;
import org.acm.seguin.summary.TypeSummary;

/**
 *  The concrete implementation of this class for JBuilder
 *
 *@author     Chris Seguin
 *@created    October 18, 2001
 */
public class JBuilderSelectedFileSet extends SelectedFileSet {
    private Node[] initialNodes;


    /**
     *  Constructor for the JBuilderSelectedFileSet object
     *
     *@param  init  Description of Parameter
     */
    public JBuilderSelectedFileSet(Node[] init) {
        initialNodes = init;
    }


    /**
     *  Gets the Nodes attribute of the JBuilderRefactoringAction object
     *
     *@return    The Nodes value
     */
    private Node[] getNodes() {
        if (initialNodes == null) {
            Node[] nodeArray = new Node[1];
            Browser browser = Browser.getActiveBrowser();
            nodeArray[0] = browser.getActiveNode();
            return nodeArray;
        }
        else {
            return initialNodes;
        }
    }


    /**
     *  Gets the TypeSummaryArray attribute of the SelectedFileSet object
     *
     *@return    The TypeSummaryArray value
     */
    public TypeSummary[] getTypeSummaryArray() {
        Node[] nodeArray = getNodes();

        TypeSummary[] typeSummaryArray = new TypeSummary[nodeArray.length];

        for (int ndx = 0; ndx < nodeArray.length; ndx++) {
            TypeSummary typeSummary = getTypeSummaryFromNode(nodeArray[ndx]);
            if (typeSummary == null) {
                return null;
            }
            typeSummaryArray[ndx] = typeSummary;
        }

        return typeSummaryArray;
    }


    /**
     *  Gets the TypeSummaryFromNode attribute of the AddParentClassAction
     *  object
     *
     *@param  node  Description of Parameter
     *@return       The TypeSummaryFromNode value
     */
    private TypeSummary getTypeSummaryFromNode(Node node) {
        FileSummary fileSummary = reloadNode(node);
        if (fileSummary == null) {
            return null;
        }

        return getTypeSummary(fileSummary);
    }


    /**
     *  Gets the AllJava attribute of the SelectedFileSet object
     *
     *@return    The AllJava value
     */
    public boolean isAllJava() {
        Node[] nodeArray = getNodes();
        for (int ndx = 0; ndx < nodeArray.length; ndx++) {
            if (!(nodeArray[0] instanceof JavaFileNode)) {
                return false;
            }
        }

        return true;
    }



    /**
     *  Gets the SingleJavaFile attribute of the SelectedFileSet object
     *
     *@return    The SingleJavaFile value
     */
    public boolean isSingleJavaFile() {
        Node[] nodeArray = getNodes();
        return (nodeArray.length == 1) && (nodeArray[0] instanceof JavaFileNode);
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@return       Description of the Returned Value
     */
    private FileSummary reloadNode(Node node) {
        try {
            if (node instanceof JavaFileNode) {
                JavaFileNode jtn = (JavaFileNode) node;

                Buffer buffer = jtn.getBuffer();
                byte[] contents = buffer.getContent();
                ByteArrayInputStream bais = new ByteArrayInputStream(contents);

                return reloadFile(jtn.getUrl().getFileObject(), bais);
            }
        }
        catch (IOException ioe) {
            //  Unable to get the buffer for that node, so fail
        }

        return null;
    }
}
//  EOF
