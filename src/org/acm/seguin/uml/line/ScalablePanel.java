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

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JPanel;

/**
 *  Panel that can be scaled and shifted
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public abstract class ScalablePanel extends JPanel {
    private boolean inShapeChange;
    private boolean inScaling = false;

    private double scale;

    private int absoluteWidth = -1;
    private int absoluteHeight = -1;
    private int absoluteX = -1;
    private int absoluteY = -1;


    /**
     *  Constructor for the ScalablePanel object
     */
    public ScalablePanel() {
        init();
    }


    /**
     *  Constructor for the ScalablePanel object
     *
     *@param  doubleBuffered  Description of Parameter
     */
    public ScalablePanel(boolean doubleBuffered) {
        super(doubleBuffered);
        init();
    }


    /**
     *  Constructor for the ScalablePanel object
     *
     *@param  layout  Description of Parameter
     */
    public ScalablePanel(LayoutManager layout) {
        super(layout);
        init();
    }


    /**
     *  Constructor for the ScalablePanel object
     *
     *@param  layout          Description of Parameter
     *@param  doubleBuffered  Description of Parameter
     */
    public ScalablePanel(LayoutManager layout, boolean doubleBuffered) {
        super(layout, doubleBuffered);
        init();
    }


    /**
     *  Sets the Location attribute of the ScalablePanel object
     *
     *@param  x  The new Location value
     *@param  y  The new Location value
     */
    public void setLocation(int x, int y) {
        if (inShapeChange) {
            super.setLocation(x, y);
        } else {
            inShapeChange = true;
            absoluteX = x;
            absoluteY = y;
            super.setLocation(scaleInteger(x), scaleInteger(y));
            inShapeChange = false;
        }
    }


    /**
     *  Sets the Location attribute of the ScalablePanel object
     *
     *@param  pt  The new Location value
     */
    public void setLocation(Point pt) {
        if (inShapeChange) {
            super.setLocation(pt);
        } else {
            inShapeChange = true;
            absoluteX = pt.x;
            absoluteY = pt.y;
            super.setLocation(scaleInteger(pt.x), scaleInteger(pt.y));
            inShapeChange = false;
        }
    }


    /**
     *  Sets the Size attribute of the ScalablePanel object
     *
     *@param  w  The new Size value
     *@param  h  The new Size value
     */
    public void setSize(int w, int h) {
        if (inShapeChange) {
            super.setSize(w, h);
        } else {
            inShapeChange = true;
            absoluteWidth = w;
            absoluteHeight = h;
            super.setSize(scaleInteger(w), scaleInteger(h));
            inShapeChange = false;
        }
    }


    /**
     *  Sets the Size attribute of the ScalablePanel object
     *
     *@param  dim  The new Size value
     */
    public void setSize(Dimension dim) {
        if (inShapeChange) {
            super.setSize(dim);
        } else {
            inShapeChange = true;
            absoluteWidth = dim.width;
            absoluteHeight = dim.height;
            super.setSize(scaleInteger(dim.width), scaleInteger(dim.height));
            inShapeChange = false;
        }
    }


    /**
     *  Sets the Bounds attribute of the ScalablePanel object
     *
     *@param  x  The new Bounds value
     *@param  y  The new Bounds value
     *@param  w  The new Bounds value
     *@param  h  The new Bounds value
     */
    public void setBounds(int x, int y, int w, int h) {
        if (inShapeChange) {
            super.setBounds(x, y, w, h);
        } else {
            inShapeChange = true;
            absoluteX = x;
            absoluteY = y;
            absoluteWidth = w;
            absoluteHeight = h;
            super.setBounds(scaleInteger(x), scaleInteger(y),
                    scaleInteger(h), scaleInteger(h));
            inShapeChange = false;
        }
    }


    /**
     *  Sets the Bounds attribute of the ScalablePanel object
     *
     *@param  rect  The new Bounds value
     */
    public void setBounds(Rectangle rect) {
        if (inShapeChange) {
            super.setBounds(rect);
        } else {
            inShapeChange = true;
            absoluteX = rect.x;
            absoluteY = rect.y;
            absoluteWidth = rect.width;
            absoluteHeight = rect.height;
            super.setBounds(scaleInteger(rect.x), scaleInteger(rect.y),
                    scaleInteger(rect.width), scaleInteger(rect.height));
            inShapeChange = false;
        }
    }


    /**
     *  This method moves the class diagram around on the screen
     *
     *@param  x  the x coordinate (scaled value)
     *@param  y  the y coordinate (scaled value)
     */
    public void shift(int x, int y) {
        Point pt = getLocation();
        inShapeChange = true;
        absoluteX = Math.max(0, unscaleInteger(x + pt.x));
        absoluteY = Math.max(0, unscaleInteger(y + pt.y));
        setLocation(Math.max(0, x + pt.x), Math.max(0, y + pt.y));
        inShapeChange = false;
    }


    /**
     *  Scales the image
     *
     *@param  value  the amount to scale
     */
    public void scale(double value) {
        if (Math.abs(scale - value) > 0.001) {
            Rectangle rect = getUnscaledBounds();

            scale = value;

            inScaling = true;
            setBounds(rect);
            inScaling = false;
        }
    }


    /**
     *  Invokes old version of setLocation
     *
     *@param  x  Description of Parameter
     *@param  y  Description of Parameter
     */
    public void move(int x, int y) {
        if (inShapeChange) {
            super.move(x, y);
        } else {
            inShapeChange = true;
            absoluteX = x;
            absoluteY = y;
            super.move(scaleInteger(x), scaleInteger(y));
            inShapeChange = false;
        }
    }


    /**
     *  Description of the Method
     *
     *@param  w  Description of Parameter
     *@param  h  Description of Parameter
     */
    public void resize(int w, int h) {
        if (inShapeChange) {
            super.resize(w, h);
        } else {
            inShapeChange = true;
            absoluteWidth = w;
            absoluteHeight = h;
            super.resize(scaleInteger(w), scaleInteger(h));
            inShapeChange = false;
        }
    }


    /**
     *  Description of the Method
     *
     *@param  dim  Description of Parameter
     */
    public void resize(Dimension dim) {
        if (inShapeChange) {
            super.resize(dim);
        } else {
            inShapeChange = true;
            absoluteWidth = dim.width;
            absoluteHeight = dim.height;
            super.resize(scaleInteger(dim.width), scaleInteger(dim.height));
            inShapeChange = false;
        }
    }


    /**
     *  Description of the Method
     *
     *@param  x  Description of Parameter
     *@param  y  Description of Parameter
     *@param  w  Description of Parameter
     *@param  h  Description of Parameter
     */
    public void reshape(int x, int y, int w, int h) {
        if (inShapeChange) {
            super.reshape(x, y, w, h);
        } else {
            inShapeChange = true;
            absoluteX = x;
            absoluteY = y;
            absoluteWidth = w;
            absoluteHeight = h;
            super.reshape(scaleInteger(x), scaleInteger(y),
                    scaleInteger(h), scaleInteger(h));
            inShapeChange = false;
        }
    }


    /**
     *  Return the scaling factor
     *
     *@return    the scaling factor
     */
    protected double getScale() {
        return scale;
    }


    /**
     *  Scale the integer
     *
     *@param  value  the value to be converted
     *@return        the scaled version
     */
    protected int scaleInteger(int value) {
        return (int) Math.round(1.0 + scale * value);
    }


    /**
     *  Get the bounds without scaling factors
     *
     *@return    the rectangle containing the boundaries
     */
    public Rectangle getUnscaledBounds() {
        return new Rectangle(absoluteX, absoluteY, absoluteWidth, absoluteHeight);
    }

    /**
     *  Computes the location without the scaling factor
     *
     *@return    the unscaled location
     */
    public Point getUnscaledLocation() {
        return new Point(absoluteX, absoluteY);
    }


    /**
     *  Inverse of the scaleInteger operation
     *
     *@param  value  the input value
     *@return        the result of the unscape operation
     */
    protected int unscaleInteger(int value) {
        return (int) (value / scale);
    }


    /**
     *  Initialize the valuse
     */
    private void init() {
        inShapeChange = false;
        scale = 1.0;
    }
}
