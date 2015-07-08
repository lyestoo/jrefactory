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
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.io.PrintWriter;
import javax.swing.JPanel;
import org.acm.seguin.uml.UMLField;

/**
 *  AssociationRelationship
 *
 *@author     Chris Seguin
 *@author     Mike Atkinson
 *@created    August 1, 1999
 */
public class AssociationRelationship extends SegmentedLine {
    private UMLField field;
    private boolean dead;


    /**
     *  Constructor for the InheretenceRelationship object
     *
     *@param  start  Description of Parameter
     *@param  end    Description of Parameter
     *@param  init   Description of Parameter
     */
    public AssociationRelationship(EndPointPanel start, EndPointPanel end, UMLField init) {
        super(start, end);

        field = init;
        dead = false;

        Point startPt = vertices[vertices.length - 2].getPoint();
        Point endPt = vertices[vertices.length - 1].getPoint();
        Dimension fieldSize = field.getSize();

        int nX = (startPt.x < endPt.x) ? endPt.x - fieldSize.width - 10 : endPt.x + 10;
        int nY = (startPt.y < endPt.y) ? endPt.y - fieldSize.height - 10 : endPt.y + 10;

        field.setLocation(nX, nY);
    }


    /**
     *  Gets the Field attribute of the AssociationRelationship object
     *
     *@return    The Field value
     */
    public UMLField getField() {
        return field;
    }


    ///**
    // *  Draws the segmented line
    // *
    // *@param  g  Description of Parameter
    // */
    //public void paint(Graphics g) {
    //    super.paint(g);
    //}


    /**
     *  Saves a segmented to the output stream
     *
     *@param  output  the output stream
     */
    public void save(PrintWriter output) {
        output.print("A[");
        saveStartPanel(output);
        output.print(",");
        output.print(field.getSummary().getName());
        output.print("]");
        saveVertices(output);
        Point pt = field.getUnscaledLocation();
        output.println("{" + pt.x + "," + pt.y + "}");
    }


    /**
     *  Delete this segmented line
     */
    public void delete() {
        dead = true;
    }


    /**
     *  Draws the arrow and the last segment
     *
     *@param  g  the graphics object
     */
    protected void drawArrow(Graphics2D g) {
        Point shortPt = getShortPoint();
        int last = vertices.length;
        //double X0 = vertices[last - 2].getPoint().getX();
        //double Y0 = vertices[last - 2].getPoint().getY();
        double X0 = shortPt.getX();
        double Y0 = shortPt.getY();

        Point end = vertices[last - 1].getPoint();
        Point above = getArrowPointAbove();
        Point below = getArrowPointBelow();

        Xs[0] = (int) X0;
        Xs[1] = (int) end.getX();
        Xs[2] = (int) below.getX();
        Xs[3] = (int) end.getX();
        Xs[4] = (int) above.getX();

        Ys[0] = (int) Y0;
        Ys[1] = (int) end.getY();
        Ys[2] = (int) below.getY();
        Ys[3] = (int) end.getY();
        Ys[4] = (int) above.getY();

        g.drawPolyline(Xs, Ys, 5);
    }


    /**
     *  Updates the location of the end vertex
     */
    protected void updateEnd() {
        if (dead) {
            return;
        }

        int last = vertices.length;

        //  Remember where we came from
        Point before = vertices[last - 1].getPoint();
        int beforeX = before.x;
        int beforeY = before.y;

        //  Update the end point
        super.updateEnd();

        //  Learn where we went to
        Point after = vertices[last - 1].getPoint();

        // shift field the difference between before and after points
        field.shift(after.x - beforeX, after.y - beforeY);
    }
}
