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
package org.acm.seguin.uml.line;

import java.awt.LayoutManager;
import java.awt.Point;
import javax.swing.JPanel;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

/**
 *  A panel that contains segmented lineList
 *
 *@author     Chris Seguin
 *@created    August 6, 1999
 */
public abstract class LinedPanel extends JPanel {
    private LinkedList endPoints;
    private LinkedList lineList;


    /**
     *  Constructor for the LinedPanel object
     */
    public LinedPanel() {
        endPoints = new LinkedList();
        lineList = new LinkedList();
    }


    /**
     *  Constructor for the LinedPanel object
     *
     *@param  doubleBuffered  Description of Parameter
     */
    public LinedPanel(boolean doubleBuffered) {
        super(doubleBuffered);
        endPoints = new LinkedList();
        lineList = new LinkedList();
    }


    /**
     *  Constructor for the LinedPanel object
     *
     *@param  layout  Description of Parameter
     */
    public LinedPanel(LayoutManager layout) {
        super(layout);
        endPoints = new LinkedList();
        lineList = new LinkedList();
    }


    /**
     *  Constructor for the LinedPanel object
     *
     *@param  layout          Description of Parameter
     *@param  doubleBuffered  Description of Parameter
     */
    public LinedPanel(LayoutManager layout, boolean doubleBuffered) {
        super(layout, doubleBuffered);
        endPoints = new LinkedList();
        lineList = new LinkedList();
    }


    /**
     *  Sends a message to determine if any line has been hit
     *
     *@param  point  The point that the mouse is at
     */
    public abstract void hit(Point point);


    /**
     *  Dragging action
     *
     *@param  point  The mouse's current position
     */
    public abstract void drag(Point point);


    /**
     *  User dropped an item
     */
    public abstract void drop();


    /**
     *  Description of the Method
     *
     *@param  panel  Description of Parameter
     */
    public void add(EndPointPanel panel) {
        endPoints.add(panel);
        super.add(panel);
    }

    public void remove(EndPointPanel panel) {
       endPoints.remove(panel);
       super.remove(panel);
    }


    /**
     *  Description of the Method
     *
     *@param  value  Description of Parameter
     */
    public void add(SegmentedLine value) {
        lineList.add(value);
    }


    /**
     *  Description of the Method
     *
     *@param  value  Description of Parameter
     */
    public void remove(SegmentedLine value) {
        lineList.remove(value);
    }


    /**
     *  Description of the Method
     *
     *@param  value  Description of Parameter
     */
    public void scale(double value) {
        Iterator iter = endPoints.iterator();
        while (iter.hasNext()) {
            ((EndPointPanel) iter.next()).scale(value);
        }

        iter = lineList.iterator();
        while (iter.hasNext()) {
            ((SegmentedLine) iter.next()).scale(value);
        }
    }


    /**
     *  Shifts the components of this lined panel
     *
     *@param  x  the amount to shift in the X axis
     *@param  y  the amount to shift in the Y axis
     */
    public void shift(int x, int y) {
        Iterator iter = endPoints.iterator();
        while (iter.hasNext()) {
            EndPointPanel next = (EndPointPanel) iter.next();
            if (next.isSelected()) {
                next.shift(x, y);
            }
        }

        iter = lineList.iterator();
        while (iter.hasNext()) {
            SegmentedLine next = (SegmentedLine) iter.next();
            if (next.isBothEndsSelected()) {
                next.shift(x, y);
            }
        }

        repaint();
    }


    /**
     *  Removes all the lihnes and classes from this diagram
     */
    public void clear() {
        endPoints.clear();
        lineList.clear();
    }


    /**
     *  Deselects all the end points
     */
    public void deselectAll() {
        Iterator iter = getEndPointIterator();
        while (iter.hasNext()) {
            ((EndPointPanel) iter.next()).setSelected(false);
        }
    }


    /**
     *  Returns a list of lines
     *
     *@return    the lines
     */
    public List getLines() {
        return lineList;
    }


    /**
     *  Returns an iterator into the list of lines
     *
     *@return    the lines
     */
    public Iterator getLineIterator() {
        return lineList.iterator();
    }


    /**
     *  Return the list of panels
     *
     *@return    the panels
     */
    protected List getEndPoints() {
        return endPoints;
    }


    /**
     *  Return an iterator into the list of panels
     *
     *@return    the panels
     */
    protected Iterator getEndPointIterator() {
        return endPoints.iterator();
    }
}
