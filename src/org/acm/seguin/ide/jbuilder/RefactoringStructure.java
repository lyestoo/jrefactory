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

import com.borland.primetime.viewer.TextStructureView;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import javax.swing.JMenu;
import javax.swing.tree.TreePath;
import com.borland.primetime.node.TextStructure;

/**
 *  Structure view that provides access to the refactorings
 *
 *@author     Chris Seguin
 *@created    October 18, 2001
 */
public class RefactoringStructure extends TextStructureDelegate {
    private Object currentNode = null;


    /**
     *  Constructor for the RefactoringStructure object
     *
     *@param  init  Description of the Parameter
     */
    public RefactoringStructure(TextStructure init) {
        super(init);
    }


    /**
     *  Gets the Popup attribute of the RefactoringStructureView object
     *
     *@return    The Popup value
     */
    public JPopupMenu getPopup() {
        JPopupMenu menu = super.getPopup();
        menu.addSeparator();
        menu.add(new JMenu("Refactor"));
        return menu;
    }


    /**
     *  The mouse was pressed
     *
     *@param  evt  the mouse event
     */
    public void mousePressed(MouseEvent evt) {
        TreePath path = getTree().getClosestPathForLocation(evt.getX(), evt.getY());
        currentNode = path.getLastPathComponent();

        System.out.println("Hit:  " + currentNode.toString() +
                "  is a " + currentNode.getClass().getName());
        super.mousePressed(evt);
    }
}
//  EOF
