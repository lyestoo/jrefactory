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
package org.acm.seguin.awt;

import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListDataListener;

/**
 *  Description of the Class
 *
 *@author     Chris Seguin
 *@created    October 16, 2001
 */
public class OrderableList extends JPanel {
    private JButton downButton;
    private JList list;
    private OrderableListModel olm;
    private JButton upButton;


    /**
     *  Constructor for the OrderableList object
     *
     *@param  data    Description of Parameter
     *@param  render  Description of Parameter
     */
    public OrderableList(Object[] data, ListCellRenderer render) {
        setLayout(null);

        olm = new OrderableListModel();
        olm.setData(data);
        list = new JList(olm);
        olm.setList(list);
        if (render != null) {
            list.setCellRenderer(render);
        }
        Dimension dim = list.getPreferredSize();
        list.setSize(dim);
        list.setLocation(10, 10);
        add(list);

        upButton = new JButton("Up");
        upButton.addActionListener(new MoveItemAdapter(olm, list, -1));
        Dimension buttonSize = upButton.getPreferredSize();
        upButton.setSize(buttonSize);
        int top = Math.max(10, 10 + dim.height / 2 - 3 * buttonSize.height / 2);
        int bottom = top + buttonSize.height;
        upButton.setLocation(dim.width + 20, top);
        add(upButton);

        downButton = new JButton("Down");
        downButton.addActionListener(new MoveItemAdapter(olm, list, 1));
        buttonSize = downButton.getPreferredSize();
        downButton.setSize(buttonSize);
        upButton.setSize(buttonSize);
        top = Math.max(bottom + 10, 10 + dim.height / 2 + buttonSize.height / 2);
        bottom = top + buttonSize.height;
        downButton.setLocation(dim.width + 20, top);
        add(downButton);

        Dimension panelSize = new Dimension(
                30 + dim.width + buttonSize.width,
                Math.max(10 + bottom, 20 + dim.height));

        setPreferredSize(panelSize);

        list.setLocation(10, (panelSize.height - dim.height) / 2);
    }


    /**
     *  Sets the Enabled attribute of the OrderableList object
     *
     *@param  way  The new Enabled value
     */
    public void setEnabled(boolean way) {
        super.setEnabled(way);

        list.setEnabled(way);
        upButton.setEnabled(way);
        downButton.setEnabled(way);
    }


    /**
     *  Gets the correctly ordered data
     *
     *@return    The Data value
     */
    public Object[] getData() {
        return olm.getData();
    }


    /**
     *  Adds a feature to the ListDataListener attribute of the OrderableList
     *  object
     *
     *@param  l  The feature to be added to the ListDataListener attribute
     */
    public void addListDataListener(ListDataListener l) {
        olm.addListDataListener(l);
    }


    /**
     *  The main program for the OrderableList class
     *
     *@param  args  The command line arguments
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Object[] data = {"one", "two", "three"};
        frame.getContentPane().add(new OrderableList(data, null));
        frame.pack();
        frame.show();
    }


    /**
     *  Description of the Method
     *
     *@param  l  Description of Parameter
     */
    public void removeListDataListener(ListDataListener l) {
        olm.removeListDataListener(l);
    }


    /**
     *  Resets the list model
     *
     *@param  data  Description of the Parameter
     */
    public void resetModel(Object[] data) {
        olm = new OrderableListModel();
        olm.setData(data);
        list.setModel(olm);
    }
}
//  EOF
