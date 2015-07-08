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
package org.acm.seguin.uml;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.acm.seguin.uml.line.DragPanelAdapter;
import org.acm.seguin.uml.line.SizableLabel;
import org.acm.seguin.uml.line.ScalablePanel;

/**
 *  Holds the roles associated with a type
 *
 *@author     Chris Seguin
 *@author     Mike Atkinson
 *@created    September 30, 1999
 */
public class RoleHolder extends ScalablePanel {
    private LinkedList labels;
    private int wide;
    private int high;
    private UMLMouseAdapter popupMenuListener;
    private DragPanelAdapter panelDragAdapter;


    /**
     *  Constructor for the RoleHolder object
     *
     *@param  popupMenuListener  listener that launches the popup menu
     *@param  panelDragAdapter   listener that drags the type
     */
    public RoleHolder(UMLMouseAdapter popupMenuListener, DragPanelAdapter panelDragAdapter) {
        setLayout(null);
        labels = new LinkedList();
        wide = 0;
        high = 0;
        this.popupMenuListener = popupMenuListener;
        this.panelDragAdapter = panelDragAdapter;
    }


    /**
     *  Gets the preferred size
     *
     *@return    the preferred size for this object
     */
    public Dimension getPreferredSize() {
        return new Dimension(wide, high);
    }


    /**
     *  Gets the minimum size
     *
     *@return    The minimum size for this object
     */
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }


    /**
     *  Adds a role
     *
     *@param  msg  the role name
     */
    public void add(String msg) {
        SizableLabel roleLabel = new SizableLabel(msg);
        roleLabel.setSLFont(UMLLine.defaultFont);
        roleLabel.setSLHorizontalAlignment(JLabel.CENTER);
        roleLabel.setLocation(0, high);
        add(roleLabel);

        Dimension dim = roleLabel.getPreferredSize();
        roleLabel.setSize(dim);
        wide = Math.max(wide, dim.width);
        high = high + dim.height;

        roleLabel.addMouseListener(popupMenuListener);
        roleLabel.addMouseListener(panelDragAdapter);
        roleLabel.addMouseMotionListener(panelDragAdapter);

        labels.add(roleLabel);
        setSize(getPreferredSize());
    }


    /**
     *  Determines if there are any roles
     *
     *@return    Description of the Returned Value
     */
    public boolean hasAny() {
        return (high > 0);
    }


    /**
     *  Reset width
     *
     *@param  newWidth  the new width
     */
    public void resetWidth(int newWidth) {
        Dimension temp = getPreferredSize();
        temp.width = newWidth;
        setSize(temp);

        Iterator iter = labels.iterator();
        while (iter.hasNext()) {
            SizableLabel next = (SizableLabel) iter.next();
            temp = next.getPreferredSize();
            temp.width = newWidth;
            next.setSize(temp);
        }
    }


    /**
     *  Print the roles
     *
     *@param  g  Description of Parameter
     *@param  x  Description of Parameter
     *@param  y  Description of Parameter
     */
    public void print(Graphics g, int x, int y) {
        Rectangle bounds = getBounds();
        g.setFont(UMLLine.defaultFont);
        FontMetrics fm = g.getFontMetrics();

        Iterator iter = labels.iterator();
        while (iter.hasNext()) {
            SizableLabel roleLabel = (SizableLabel) iter.next();
            Point pt = roleLabel.getLocation();
            roleLabel.print(g, x + pt.x, y + pt.y);
        }
    }


    /**
     *  Sets the scaling factor
     *
     *@param  value  scaling factor
     */
    public void scale(double value) {
        //wide = 0;
        //high = 0;
        Iterator iter = labels.iterator();
        while (iter.hasNext()) {
            SizableLabel roleLabel = (SizableLabel) iter.next();
            roleLabel.scale(value);
        }
        super.scale(value);
    }

}
