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

import com.borland.primetime.node.TextStructure;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.MouseEvent;
import java.lang.Class;
import com.borland.primetime.node.FileNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.List;
import java.awt.event.KeyEvent;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import java.awt.event.MouseListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.Component;
import javax.swing.JPopupMenu;
import javax.swing.Icon;
import java.awt.event.KeyListener;
import com.borland.primetime.editor.EditorPane;
import java.lang.Object;
import java.lang.String;
import javax.swing.text.Document;

/**
 *  Description of Class
 *
 *@author     unknown
 *@created    October 18, 2001
 */
public class TextStructureDelegate extends TextStructure {
    private TextStructure delegate;

    // Constructors

    /**
     *  Constructor for the TextStructureDelegate object
     *
     *@param  init  Description of the Parameter
     */
    public TextStructureDelegate(TextStructure init) {
        delegate = init;
    }


    /**
     *  Sets the caretOffset attribute of the TextStructureDelegate object
     *
     *@param  p0  The new caretOffset value
     *@param  p1  The new caretOffset value
     */
    public void setCaretOffset(int p0, boolean p1) {
        delegate.setCaretOffset(p0, p1);
    }


    /**
     *  Sets the caretPosition attribute of the TextStructureDelegate object
     *
     *@param  p0  The new caretPosition value
     *@param  p1  The new caretPosition value
     */
    public void setCaretPosition(int p0, boolean p1) {
        delegate.setCaretPosition(p0, p1);
    }


    /**
     *  Sets the caretPosition attribute of the TextStructureDelegate object
     *
     *@param  p0  The new caretPosition value
     *@param  p1  The new caretPosition value
     *@param  p2  The new caretPosition value
     */
    public void setCaretPosition(int p0, int p1, boolean p2) {
        delegate.setCaretPosition(p0, p1, p2);
    }


    // Methods
    /**
     *  Sets the expandState attribute of the TextStructureDelegate object
     *
     *@param  p0  The new expandState value
     */
    public void setExpandState(List p0) {
        delegate.setExpandState(p0);
    }


    /**
     *  Sets the fileNode attribute of the TextStructureDelegate object
     *
     *@param  p0  The new fileNode value
     */
    public void setFileNode(FileNode p0) {
        delegate.setFileNode(p0);
    }


    /**
     *  Sets the tree attribute of the TextStructureDelegate object
     *
     *@param  p0  The new tree value
     */
    public void setTree(JTree p0) {
        delegate.setTree(p0);
    }


    /**
     *  Gets the editorPane attribute of the TextStructureDelegate object
     *
     *@return    The editorPane value
     */
    public EditorPane getEditorPane() {
        return delegate.getEditorPane();
    }


    /**
     *  Gets the expandState attribute of the TextStructureDelegate object
     *
     *@return    The expandState value
     */
    public List getExpandState() {
        return delegate.getExpandState();
    }


    /**
     *  Gets the popup attribute of the TextStructureDelegate object
     *
     *@return    The popup value
     */
    public JPopupMenu getPopup() {
        return delegate.getPopup();
    }


    /**
     *  Gets the structureIcon attribute of the TextStructureDelegate object
     *
     *@param  p0  Description of the Parameter
     *@return     The structureIcon value
     */
    public Icon getStructureIcon(Object p0) {
        return delegate.getStructureIcon(p0);
    }


    /**
     *  Gets the tree attribute of the TextStructureDelegate object
     *
     *@return    The tree value
     */
    public JTree getTree() {
        return delegate.getTree();
    }


    /**
     *  Gets the treeCellRendererComponent attribute of the
     *  TextStructureDelegate object
     *
     *@param  p0  Description of the Parameter
     *@param  p1  Description of the Parameter
     *@param  p2  Description of the Parameter
     *@param  p3  Description of the Parameter
     *@param  p4  Description of the Parameter
     *@param  p5  Description of the Parameter
     *@param  p6  Description of the Parameter
     *@return     The treeCellRendererComponent value
     */
    public Component getTreeCellRendererComponent(JTree p0, Object p1, boolean p2, boolean p3, boolean p4, int p5, boolean p6) {
        return delegate.getTreeCellRendererComponent(p0, p1, p2, p3, p4, p5, p6);
    }


    /**
     *  Description of the Method
     *
     *@param  p0  Description of the Parameter
     */
    public void keyPressed(KeyEvent p0) {
        delegate.keyPressed(p0);
    }


    /**
     *  Description of the Method
     *
     *@param  p0  Description of the Parameter
     */
    public void keyReleased(KeyEvent p0) {
        delegate.keyReleased(p0);
    }


    /**
     *  Description of the Method
     *
     *@param  p0  Description of the Parameter
     */
    public void keyTyped(KeyEvent p0) {
        delegate.keyTyped(p0);
    }


    /**
     *  Description of the Method
     *
     *@param  p0  Description of the Parameter
     */
    public void nodeActivated(DefaultMutableTreeNode p0) {
        delegate.nodeActivated(p0);
    }


    /**
     *  Description of the Method
     *
     *@param  p0  Description of the Parameter
     */
    public void nodeSelected(DefaultMutableTreeNode p0) {
        delegate.nodeSelected(p0);
    }


    /**
     *  Description of the Method
     *
     *@param  p0  Description of the Parameter
     */
    public void updateStructure(Document p0) {
        delegate.updateStructure(p0);
    }
}

