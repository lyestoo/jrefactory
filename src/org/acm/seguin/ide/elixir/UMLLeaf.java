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
package org.acm.seguin.ide.elixir;

import com.elixirtech.fx.FrameManager;
import com.elixirtech.tree.TNode;
import com.elixirtech.tree.TModel;
import com.elixirtech.tree.TParent;
import com.elixirtech.util.SortUtil;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Enumeration;
import javax.swing.Icon;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeNode;
import java.util.Vector;
import java.util.StringTokenizer;
import org.acm.seguin.ide.common.UMLIcon;

/**
 *  Stores a leaf node for a UML class diagram
 *
 *@author     Chris Seguin
 *@created    October 18, 2001
 */
class UMLLeaf implements TNode {
    private UMLDocManager docManager;
    private File file;
    private String packageName;
    private TNode parent;


    /**
     *  Constructor for the UMLLeaf object
     *
     *@param  parent      the parent file
     *@param  file        the file
     *@param  docManager  the document manager
     */
    public UMLLeaf(TNode parent, File file, UMLDocManager docManager) {
        this.parent = parent;
        this.file = file;
        this.docManager = docManager;

        loadPackageName();
    }


    /**
     *  Sets a new name for the node.
     *
     *@param  name  The new Name value
     */
    public void setName(String name) {
        file = new File(name);
    }


    /**
     *  Sets a new parent for the node.
     *
     *@param  value  The new Parent value
     */
    public void setParent(TParent value) {
        parent = value;
    }


    /**
     *  Can we add children to this
     *
     *@return    The AllowsChildren value
     */
    public boolean getAllowsChildren() {
        return false;
    }


    /**
     *  Return the child from an index
     *
     *@param  idx  Description of Parameter
     *@return      The ChildAt value
     */
    public TreeNode getChildAt(int idx) {
        return null;
    }


    /**
     *  Count the children
     *
     *@return    The ChildCount value
     */
    public int getChildCount() {
        return 0;
    }


    /**
     *  Get the full name of the node, usually composed by walking the TreePath
     *  a/b/c etc.
     *
     *@return    The FullName value
     */
    public String getFullName() {
        try {
            return file.getCanonicalPath();
        }
        catch (IOException ioe) {
            return file.getPath();
        }
    }


    /**
     *  Get the icon to show in the tree
     *
     *@param  expanded  Description of Parameter
     *@return           The Icon value
     */
    public Icon getIcon(boolean expanded) {
        return new UMLIcon();
    }


    /**
     *  Return the index of a child. This has no children so always returns -1.
     *
     *@param  child  Description of Parameter
     *@return        The Index value
     */
    public int getIndex(TreeNode child) {
        return -1;
    }


    /**
     *  Get the name to show in the tree
     *
     *@return    The Name value
     */
    public String getName() {
        return packageName + " Class Diagram";
    }


    /**
     *  Gets the Parent attribute of the UMLLeaf object
     *
     *@return    The Parent value
     */
    public TreeNode getParent() {
        return parent;
    }


    /**
     *  Get the popup menu to show for this node
     *
     *@return    The PopupMenu value
     */
    public JPopupMenu getPopupMenu() {
        JPopupMenu result = new JPopupMenu();
        JMenuItem item = new JMenuItem("Open");
        item.addActionListener(new OpenFileAdapter(getFullName()));
        result.add(item);
        return result;
    }


    /**
     *  Return the tooltip help to be shown when the mouse is over this node.
     *
     *@return    The ToolTipText value
     */
    public String getToolTipText() {
        if (packageName.length() > 0) {
            return "The class diagram for the package " + packageName;
        }
        else {
            return "The class diagram for the top level package";
        }
    }


    /**
     *  Return the model which this node belongs to
     *
     *@return    The TreeModel value
     */
    public TModel getTreeModel() {
        return parent.getTreeModel();
    }


    /**
     *  Get the TreePath which represents this node
     *
     *@return    The TreePath value
     */
    public TreePath getTreePath() {
        return parent.getTreePath().pathByAddingChild(this);
    }


    /**
     *  Gets the Leaf attribute of the UMLLeaf object
     *
     *@return    The Leaf value
     */
    public boolean isLeaf() {
        return true;
    }


    /**
     *  Gets an enumeration of the children
     *
     *@return    An empty enumeration
     */
    public Enumeration children() {
        Vector result = new Vector();
        return result.elements();
    }


    /**
     *  Perform double-click action. Hopefully this will open the file.
     */
    public void doDoubleClick() {
        FrameManager.current().open(getFullName());
    }


    /**
     *  Notify the TreeModel and hence the TreeModel listeners that this node
     *  has changed
     */
    public void fireChanged() { }


    /**
     *  Loads the package name from the file
     */
    private void loadPackageName() {
        try {
            packageName = "Unknown";

            BufferedReader input = new BufferedReader(new FileReader(file));

            String line = input.readLine();
            if (line.charAt(0) == 'V') {
                StringTokenizer tok = new StringTokenizer(line, "[:]");
                if (tok.hasMoreTokens()) {
                    // Skip the first - it is the letter v
                    String temp = tok.nextToken();
                    if (tok.hasMoreTokens()) {
                        // Skip the second - it is the version (1.1)
                        temp = tok.nextToken();
                        if (tok.hasMoreTokens()) {
                            //  Third item is the package name
                            packageName = tok.nextToken();
                        }
                    }
                }
            }

            input.close();
        }
        catch (IOException ioe) {
        }
    }


    /**
     *  Sort the children of this node based on the comparator. Since there are
     *  no children, this does nothing.
     *
     *@param  c  the comparator
     */
    public void sortChildren(SortUtil.Comparator c) { }


    /**
     *  Return the name of the node as its String representation
     *
     *@return    Gets the string representation of this node
     */
    public String toString() {
        return getName();
    }


    /**
     *  Opens a file when the button is pressed
     *
     *@author     Chris Seguin
     *@created    October 18, 2001
     */
    private class OpenFileAdapter implements ActionListener {
        private String name;


        /**
         *  Constructor for the OpenFileAdapter object
         *
         *@param  init  the name of the file
         */
        public OpenFileAdapter(String init) {
            name = init;
        }


        /**
         *  Opens the file
         *
         *@param  evt  the action event
         */
        public void actionPerformed(ActionEvent evt) {
            FrameManager.current().open(name);
        }
    }
}
//  EOF
