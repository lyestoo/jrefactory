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

import org.acm.seguin.parser.ast.ModifierHolder;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import org.acm.seguin.uml.line.DragPanelAdapter;
import org.acm.seguin.uml.line.EndPointPanel;
import org.acm.seguin.uml.line.SizableLabel;

/**
 *  Displays a single UML line
 *
 *@author     Chris Seguin
 *@created    July 6, 1999
 */
public class UMLLine extends EndPointPanel {
    //  Instance Variables
    /**
     *  Description of the Field
     */
    protected SizableLabel label;
    /**
     *  Description of the Field
     */
    protected int wide;
    /**
     *  Description of the Field
     */
    protected int high;
    /**
     *  Stores the icon for this object
     */
    protected UMLIcon icon;
    /**
     *  Description of the Field
     */
    private int iconWidth = 8;
    /**
     *  Description of the Field
     */
    private int iconHeight = 8;
    private int protection;
    private UMLType parent;
    private Font font;

    //  Class Variables
    /**
     *  Description of the Field
     */
    public final static int PUBLIC = 0;
    /**
     *  Description of the Field
     */
    public final static int PROTECTED_PRIVATE = 1;
    /**
     *  Description of the Field
     */
    public final static int PROTECTED = 2;
    /**
     *  Description of the Field
     */
    public final static int DEFAULT = 3;
    /**
     *  Description of the Field
     */
    public final static int PRIVATE = 4;
    /**
     *  Description of the Field
     */
    protected static Color[] protectionColors = null;
    /**
     *  Description of the Field
     */
    protected static Font defaultFont = null;
    /**
     *  Description of the Field
     */
    protected static Font staticFont = null;
    /**
     *  Description of the Field
     */
    protected static Font abstractFont = null;
    /**
     *  Description of the Field
     */
    protected static Font titleFont = null;
    /**
     *  Description of the Field
     */
    protected static Font abstractTitleFont = null;
    /**
     *  Description of the Field
     */
    protected final static int iconMargin = 1;
    /**
     *  Description of the Field
     */
    protected final static int labelMargin = 1;


    /**
     *  Create a new instance of a UMLLine
     *
     *@param  parent   Description of Parameter
     *@param  adapter  Description of Parameter
     */
    public UMLLine(UMLType parent, DragPanelAdapter adapter) {
        super(null, true);
        this.parent = parent;
        label = new SizableLabel("");
        label.setLocation(iconWidth + 2 * iconMargin + labelMargin, labelMargin);
        add(label);

        //  Add listeners
        addMouseListener(adapter);
        addMouseMotionListener(adapter);

        icon = new ProtectionIcon(iconWidth, iconHeight);
    }


    /**
     *  Set the protection code
     *
     *@param  code  the code
     */
    public void setProtection(int code) {
        protection = code;
        if (icon instanceof ProtectionIcon) {
            ((ProtectionIcon) icon).setProtection(code);
        }
    }


    /**
     *  Set the text
     *
     *@param  msg  the message
     */
    public void setLabelText(String msg) {
        label.setText(msg);

        //  Reset the height and width
        Dimension labelSize = label.getPreferredSize();
        label.setSize(labelSize);
        high = Math.max(iconWidth + 2 * iconMargin, labelSize.height + 2 * labelMargin);
        wide = labelSize.width + iconWidth + 2 * iconMargin + 2 * labelMargin;
    }


    /**
     *  Set the font
     *
     *@param  font  the new font
     */
    public void setLabelFont(Font font) {
        label.setSLFont(font);

        //  Reset the height and width
        Dimension labelSize = label.getPreferredSize();
        label.setSize(labelSize);
        high = Math.max(iconWidth + 2 * iconMargin, labelSize.height + 2 * labelMargin);
        wide = labelSize.width + iconWidth + 2 * iconMargin + 2 * labelMargin;

        //  Save the font
        this.font = font;
    }


    /**
     *  Sets the Selected attribute of the UMLLine object
     *
     *@param  value  The new Selected value
     */
    public void setSelected(boolean value) {
        parent.setSelected(value);
    }


    /**
     *  Returns the minimum size
     *
     *@return    The size
     */
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }


    /**
     *  Returns the preferred size
     *
     *@return    The size
     */
    public Dimension getPreferredSize() {
        return new Dimension(wide, high);
    }


    /**
     *  Gets the ParentType attribute of the UMLLine object
     *
     *@return    The ParentType value
     */
    public UMLType getParentType() {
        return parent;
    }


    /**
     *  Gets the Selected attribute of the UMLLine object
     *
     *@return    The Selected value
     */
    public boolean isSelected() {
        return parent.isSelected();
    }


    /**
     *  Paint this object
     *
     *@param  g  the graphics object
     */
    public void paint(Graphics g) {
        setBackground(getDefaultBackground());
        super.paint(g);
        drawIcon(g, 0, 0);
    }


    /**
     *  Print this object
     *
     *@param  g  the graphics object
     *@param  x  the x coordinate
     *@param  y  the y coordinate
     */
    public void print(Graphics g, int x, int y) {
        Point pt = label.getLocation();
        label.print(g, x + pt.x, y + pt.y);
        drawIcon(g, x, y);
    }


    /**
     *  Add a mouse listener
     *
     *@param  listener  the new listener
     */
    public void addMouseListener(MouseListener listener) {
        label.addMouseListener(listener);
        super.addMouseListener(listener);
    }


    /**
     *  Remove a mouse listener
     *
     *@param  listener  the new listener
     */
    public void removeMouseListener(MouseListener listener) {
        label.removeMouseListener(listener);
        super.removeMouseListener(listener);
    }


    /**
     *  Add a mouse listener
     *
     *@param  listener  the new listener
     */
    public void addMouseMotionListener(MouseMotionListener listener) {
        label.addMouseMotionListener(listener);
        super.addMouseMotionListener(listener);
    }


    /**
     *  Remove a mouse listener
     *
     *@param  listener  the new listener
     */
    public void removeMouseMotionListener(MouseMotionListener listener) {
        label.removeMouseMotionListener(listener);
        super.removeMouseMotionListener(listener);
    }


    /**
     *  Sets the scaling factor
     *
     *@param  value  scaling factor
     */
    public void scale(double value) {
        super.scale(value);
        label.scale(value);
    }


    /**
     *  Return the default background color
     *
     *@return    the color
     */
    protected Color getDefaultBackground() {
        return parent.getBackground();
    }


    /**
     *  Draws the icon
     *
     *@param  g  the graphics object
     *@param  x  Description of Parameter
     *@param  y  Description of Parameter
     */
    protected void drawIcon(Graphics g, int x, int y) {
        //  Initialize local variables
        int nY = y + (high - icon.getIconHeight()) / 2;
        double scale = getScale();

        //  Draw the icons
        icon.setScale(scale);
        icon.paintIcon(this, g, x, nY);
    }


    /**
     *  Add the protection information for this field or method
     *
     *@param  modifiers  the modifier information
     *@return            Description of the Returned Value
     */
    protected static int getProtectionCode(ModifierHolder modifiers) {
        if (modifiers.isPublic()) {
            return PUBLIC;
        } else if (modifiers.isProtected() && modifiers.isPrivate()) {
            return PROTECTED_PRIVATE;
        } else if (modifiers.isPrivate()) {
            return PRIVATE;
        } else if (modifiers.isProtected()) {
            return PROTECTED;
        } else {
            return DEFAULT;
        }
    }


    /**
     *  Get the font appropriate for the level of protection
     *
     *@param  title      is this a title
     *@param  modifiers  the modifiers
     *@return            Description of the Returned Value
     */
    protected static Font getProtectionFont(boolean title, ModifierHolder modifiers) {
        if (staticFont == null) {
            initFonts();
        }

        if (modifiers == null) {
            return defaultFont;
        }

        if (modifiers.isAbstract()) {
            if (title) {
                return abstractTitleFont;
            } else {
                return abstractFont;
            }
        } else if (modifiers.isStatic()) {
            return staticFont;
        } else if (title) {
            return titleFont;
        } else {
            return defaultFont;
        }
    }


    /**
     *  Get the color associated with a level of protection
     *
     *@param  level  the level that we need to know
     *@return        the color
     */
    protected static Color getProtectionColor(int level) {
        if (protectionColors == null) {
            //  Initialize
            protectionColors = new Color[5];

            protectionColors[0] = Color.green;
            protectionColors[1] = Color.blue;
            protectionColors[2] = Color.yellow;
            protectionColors[3] = Color.orange;
            protectionColors[4] = Color.red;
        }

        return protectionColors[level];
    }


    /**
     *  Initialize the fonts
     */
    private static void initFonts() {
        defaultFont = new Font("Serif", Font.PLAIN, 12);
        staticFont = new Font("Serif", Font.BOLD, 12);
        abstractFont = new Font("Serif", Font.ITALIC, 12);
        titleFont = new Font("Serif", Font.PLAIN, 16);
        abstractTitleFont = new Font("Serif", Font.ITALIC, 16);
    }
}
