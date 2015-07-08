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
import java.awt.Component;
import java.awt.Graphics;
import org.acm.seguin.util.FileSettings;
import org.acm.seguin.util.MissingSettingsException;

/**
 *  Icon that draws the protection symbol
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public class ProtectionIcon extends UMLIcon {
    private int protection;
    private int type;

    private final static int CIRCLE = 0;
    private final static int LETTER = 1;


    /**
     *  Constructor for the ProtectionIcon object
     *
     *@param  wide  the size of the icon
     *@param  high  the size of the icon
     */
    public ProtectionIcon(int wide, int high) {
        super(wide, high);

        try {
            FileSettings umlBundle = FileSettings.getRefactorySettings("uml");
            umlBundle.setContinuallyReload(false);
            String pattern = umlBundle.getString("icon.type");
            if (pattern.equalsIgnoreCase("letter")) {
                type = LETTER;
            } else {
                type = CIRCLE;
            }
        } catch (MissingSettingsException mse) {
            type = CIRCLE;
        }

    }


    /**
     *  Sets the Protection attribute of the ProtectionIcon object
     *
     *@param  value  The new Protection value
     */
    public void setProtection(int value) {
        protection = value;
    }


    /**
     *  Draws the icon
     *
     *@param  c  The component on which we are drawing
     *@param  g  The graphics object
     *@param  x  the x location
     *@param  y  the y location
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (type == LETTER) {
            drawLetterIcon(g, x, y);
        } else {
            drawCircleIcon(g, x, y);
        }
    }


    /**
     *  Draws the protection icon like a circle
     *
     *@param  g  Description of Parameter
     *@param  x  Description of Parameter
     *@param  y  Description of Parameter
     */
    private void drawCircleIcon(Graphics g, int x, int y) {
        g.setColor(UMLLine.getProtectionColor(protection));

        int wide = Math.max(1, (int) (iconWidth * scale));
        int high = Math.max(1, (int) (iconHeight * scale));
        int margin = (int) (scale);

        g.fillOval(x + margin, y, wide, high);

        if ((wide > 1) && (high > 1)) {
            g.setColor(Color.black);
            g.drawOval(x + margin, y, wide, high);
        }
    }


    /**
     *  Draws the protection icon like a letter
     *
     *@param  g  Description of Parameter
     *@param  x  Description of Parameter
     *@param  y  Description of Parameter
     */
    private void drawLetterIcon(Graphics g, int x, int y) {
        g.setColor(Color.black);

        int wide = Math.max(1, (int) (iconWidth * scale));
        int high = Math.max(1, (int) (iconHeight * scale));
        int margin = (int) (scale);

        int halfHigh = high / 2;
        int halfWide = wide / 2;

        if (protection == UMLLine.PUBLIC) {
            g.drawLine(x + margin, y + halfHigh, x + margin + wide, y + halfHigh);
            g.drawLine(x + margin + halfWide, y, x + margin + halfWide, y + high);
        } else if (protection == UMLLine.PROTECTED) {
            g.drawLine(x + margin, y + halfHigh + 1, x + margin + wide, y + halfHigh + 1);
            g.drawLine(x + margin, y + halfHigh - 1, x + margin + wide, y + halfHigh - 1);
            g.drawLine(x + margin + halfWide + 1, y, x + margin + halfWide + 1, y + high);
            g.drawLine(x + margin + halfWide - 1, y, x + margin + halfWide - 1, y + high);
        } else if (protection == UMLLine.DEFAULT) {
            g.drawLine(x + margin, y + halfHigh + 1, x + margin + wide, y + halfHigh + 1);
            g.drawLine(x + margin, y + halfHigh - 1, x + margin + wide, y + halfHigh - 1);
        } else if (protection == UMLLine.PRIVATE) {
            g.drawLine(x + margin, y + halfHigh, x + margin + wide, y + halfHigh);
        }
    }
}
