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
import com.borland.primetime.ide.NodeViewerFactory;
import com.borland.primetime.ide.NodeViewer;
import com.borland.primetime.node.Node;
import org.acm.seguin.ide.common.MultipleDirClassDiagramReloader;
import org.acm.seguin.summary.PackageSummary;

/**
 *  Factory for node viewers
 *
 *@author     Chris Seguin
 *@created    October 18, 2001
 */
public class UMLNodeViewerFactory implements NodeViewerFactory {

    private static UMLNodeViewerFactory factory = null;
    private MultipleDirClassDiagramReloader reloader;


    /**
     *  Constructor for the UMLNodeViewerFactory object
     */
    private UMLNodeViewerFactory() {
        reloader = new JBuilderClassDiagramLoader();
    }


    /**
     *  Gets the Factory attribute of the UMLNodeViewerFactory class
     *
     *@return    The Factory value
     */
    public static UMLNodeViewerFactory getFactory() {
        if (factory == null) {
            factory = new UMLNodeViewerFactory();
        }

        return factory;
    }


    /**
     *  Gets the class diagram reloader
     *
     *@return    the reloader
     */
    public MultipleDirClassDiagramReloader getReloader() {
        return reloader;
    }


    /**
     *  Determines if this factory can view this type of file
     *
     *@param  node  the type of file
     *@return       true if it can be displayed
     */
    public boolean canDisplayNode(Node node) {
        return node instanceof UMLNode;
    }


    /**
     *  Creates the node viewer
     *
     *@param  context  the information about what is to be displayed
     *@return          the viewer
     */
    public NodeViewer createNodeViewer(Context context) {
        if (canDisplayNode(context.getNode())) {
            if (!reloader.isNecessary()) {
                reloader.setNecessary(true);
                reloader.reload();
            }

            UMLNodeViewer viewer = new UMLNodeViewer(context, reloader);
            return viewer;
        }

        return null;
    }


    /**
     *  Creates the node viewer
     *
     *@param  summary  Description of Parameter
     *@return          the viewer
     */
    public NodeViewer createNodeViewer(PackageSummary summary) {
        if (!reloader.isNecessary()) {
            reloader.setNecessary(true);
            reloader.reload();
        }

        UMLNodeViewer viewer = new UMLNodeViewer(summary, reloader);
        return viewer;
    }
}
//  EOF
