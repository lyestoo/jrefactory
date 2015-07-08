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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *  Creates a label that can be resized by setting it's scaling factor.
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public class SizableLabel extends ScalablePanel {
    private boolean bufferCreated = false;
    private Image scaledImage;
    private Color foreground;
    private Color background;
    private Font font;
    private int height = 200;
    private int width = 25;
    private String text;
    private int align;
    private int ascent;

    private static Font defaultFont = null;
    private static Color defaultColor = null;


    /**
     *  Constructor for the SizableLabel object
     */
    public SizableLabel() {
        this(" ");
    }


    /**
     *  Constructor for the SizableLabel object
     *
     *@param  init  Description of Parameter
     */
    public SizableLabel(String init) {
        if (defaultFont == null) {
            SizableLabel.init();
        }

        font = defaultFont;
        foreground = defaultColor;

        setText(init);
        determineSize();
        setSize(getPreferredSize());

        setDoubleBuffered(false);
    }


    /**
     *  Sets the Foreground attribute of the SizableLabel object
     *
     *@param  value  The new Foreground value
     */
    public void setSLForeground(Color value) {
        foreground = value;
        bufferCreated = false;
    }


    /**
     *  Sets the Font attribute of the SizableLabel object
     *
     *@param  value  The new Font value
     */
    public void setSLFont(Font value) {
        font = value;
        determineSize();
        setSize(getPreferredSize());
    }


    /**
     *  Sets the Text attribute of the SizableLabel object
     *
     *@param  value  The new Text value
     */
    public void setText(String value) {
        if ((value == null) || (value == "")) {
            text = " ";
        } else {
            text = value;
        }
        bufferCreated = false;
        determineSize();
        setSize(getPreferredSize());
    }


    /**
     *  Sets the HorizontalAlignment attribute of the SizableLabel object
     *
     *@param  value  The new HorizontalAlignment value
     */
    public void setSLHorizontalAlignment(int value) {
        align = value;
    }


    /**
     *  Gets the Text attribute of the SizableLabel object
     *
     *@return    The Text value
     */
    public String getText() {
        return text;
    }


    /**
     *  Gets the PreferredSize attribute of the SizableLabel object
     *
     *@return    The PreferredSize value
     */
    public Dimension getPreferredSize() {
        Dimension dim = new Dimension();
        dim.width = scaleInteger(width);
        dim.height = scaleInteger(height);

        bufferCreated = false;

        return dim;
    }


    /**
     *  Paints the sizable label
     *
     *@param  g  the graphics area to print in
     */
    public void paint(Graphics g) {
        print(g, 0, 0);
    }


    /**
     *  Prints the label at the proper location
     *
     *@param  g  the graphics object
     *@param  x  the x coordinate
     *@param  y  the y coordinate
     */
    public void print(Graphics g, int x, int y) {
        if (!bufferCreated || !getParentBackground().equals(background)) {
            createBuffer();
        }
        x = unscaleInteger(x);
        y = unscaleInteger(y);
        Dimension dim = getSize();
        //g.setColor(background);
        //g.fillRect(x, y, dim.width, dim.height);
        if (align == JLabel.LEFT) {
            //g.drawImage(scaledImage, x, y, this);
        } else if (align == JLabel.RIGHT) {
            int deltax = (int) (dim.width - scaleInteger(width));
            //g.drawImage(scaledImage, x + deltax, y, this);
            x += deltax;
        } else if (align == JLabel.CENTER) {
            int deltax = (int) ((dim.width - scaleInteger(width)) * 0.5);
            //g.drawImage(scaledImage, x + deltax, y, this);
            x += deltax;
        }
        determineSize();
        dim = new Dimension(width, height);
        
        Graphics2D graphics = (Graphics2D) g;
        java.awt.geom.AffineTransform transform = graphics.getTransform();
        graphics.scale(getScale(), getScale());

        //int tempWidth = Math.max(1, scaleInteger(width + 10));
        //int tempHeight = Math.max(1, scaleInteger(height + 10));
        //background = getParentBackground();
        //g.setColor(background);
        //g.fillRect(0, 0, (int)(1+tempWidth/getScale()), (int)(1+tempHeight/getScale()));

        //  Draw the text
        g.setColor(foreground);
        g.setFont(font);
        g.drawString(text, x, y+ascent);
        graphics.setTransform(transform);
    }


    /**
     *  Scales the image
     *
     *@param  value  the amount to scale
     */
    public void scale(double value) {
        if (Math.abs(getScale() - value) > 0.001) {
            super.scale(value);
            bufferCreated = false;
        }
        //setSize(getPreferredSize());
    }


    /**
     *  Gets the ParentBackground attribute of the SizableLabel object
     *
     *@return    The ParentBackground value
     */
    private Color getParentBackground() {
        Component parent = getParent();
        if (parent == null) {
            return Color.gray;
        } else {
            return parent.getBackground();
        }
    }


    /**
     *  Creates the buffer from which to draw the resized text
     */
    private void createBuffer() {
        bufferCreated = true;
        determineSize();
        Dimension dim = new Dimension(width, height);

        int tempWidth = Math.max(1, scaleInteger(width + 10));
        int tempHeight = Math.max(1, scaleInteger(height + 10));
        scaledImage = new BufferedImage(
                tempWidth,
                tempHeight,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) scaledImage.getGraphics();
        g.scale(getScale(), getScale());

        //  Fill in the background
        background = getParentBackground();
        g.setColor(background);
        g.fillRect(0, 0, (int)(1+tempWidth/getScale()), (int)(1+tempHeight/getScale()));

        //  Draw the text
        g.setColor(foreground);
        g.setFont(font);
        g.drawString(text, 0, ascent);
    }


    /**
     *  Description of the Method
     */
    private void determineSize() {
        TextInfo ti = LabelSizeComputation.get().compute(text, font);

        height = ti.height;
        width = ti.width;
        ascent = ti.ascent;
    }


    /**
     *  Prints some debug information about this label
     */
    private void debug() {
        System.out.println("Label:  " + text);
        System.out.println("    Scale:  " + getScale());
        System.out.println("    Height:  " + height);
        System.out.println("    Width:   " + width);
        System.out.println("    Align:   " + align);
        System.out.println("    Color:   " + foreground);
        System.out.println("    Shape:   " + getBounds());
    }


    /**
     *  Initializes the static font and color
     */
    private static synchronized void init() {
        if (defaultFont == null) {
            defaultFont = new Font("Serif", Font.PLAIN, 12);
            defaultColor = new Color(0, 80, 180);
        }
    }
}
