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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import org.acm.seguin.uml.UMLType;

/**
 *  SegmentedLine. <P>
 *
 *  For future builds, it may be worth considering caching the points above and
 *  below the line for an efficiency increase when there are lots of lines to
 *  paint.
 *
 *@author     Chris Seguin
 *@author     Mike Atkinson
 *@created    July 28, 1999
 */
public class SegmentedLine implements ComponentListener {
    /**
     *  The set of vertices on the line
     */
    protected Vertex[] vertices;

    /**
     *  This array is used only during the paint method for drawing polylines
     */
    protected int[] Xs;
    /**
     *  This array is used only during the paint method for drawing polylines
     */
    protected int[] Ys;
    private EndPointPanel startPanel;
    private EndPointPanel endPanel;
    private int activeVertex;
    private Segment workingSegment;
    /**
     *  Description of the Field
     */
    protected double scalingFactor;

    private static final double SHORT_BACK = 17.32050807568877;
    private static final Stroke FULL_STROKE = new BasicStroke(1, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL, 20);

    //  8.660254037844386;


    /**
     *  Constructor for the SegmentedLine object
     *
     *@param  start  Panel that the segmented line starts at
     *@param  end    End point of the panel
     */
    public SegmentedLine(EndPointPanel start, EndPointPanel end) {
        //  Remember the start and end panels
        startPanel = start;
        endPanel = end;

        scalingFactor = 1.0;

        //  Create and initialize vertices
        vertices = new Vertex[2];
        workingSegment = new Segment();
        initEndPoints();

        //  No vertices are active
        activeVertex = -1;

        //  Create buffers for drawing polylines
        Xs = new int[5];
        Ys = new int[5];

        //  Add this as a listener
        startPanel.addComponentListener(this);
        endPanel.addComponentListener(this);

        if (startPanel == endPanel) {
            Rectangle rect = startPanel.getBounds();

            int x = rect.x + rect.width / 2;
            int y = rect.y + rect.height + 10;
            insertAt(1, new Vertex(new Point(x, y)));

            x = rect.x - 10;
            y = rect.y + rect.height + 10;
            insertAt(2, new Vertex(new Point(x, y)));

            x = rect.x - 10;
            y = rect.y + rect.height / 2;
            insertAt(3, new Vertex(new Point(x, y)));

            updateEnd();
        }
    }


    /**
     *  Determines if both the start and end points are selected
     *
     *@return    true when both are selected
     */
    public boolean isBothEndsSelected() {
        return startPanel.isSelected() && endPanel.isSelected();
    }

    public EndPointPanel getStartPanel() {
        return startPanel;
    }
    
    public EndPointPanel getEndPanel() {
        return endPanel;
    }
    
    
    /**
     *  Draws the segmented line
     *
     *@param  g  Description of Parameter
     */
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        for (int ndx = 0; ndx < vertices.length; ndx++) {
            Xs[ndx] = vertices[ndx].getX();
            Ys[ndx] = vertices[ndx].getY();
            vertices[ndx].paint(g);
        }
        Point shortPoint = getShortPoint();
        Xs[vertices.length - 1] = shortPoint.x;
        Ys[vertices.length - 1] = shortPoint.y;

        //  Use a dashed stroke
        Stroke stroke = g2d.getStroke();
        g2d.setStroke(getStroke());

        //  Draw lines
        g2d.setColor(getColor());
        g2d.drawPolyline(Xs, Ys, vertices.length);

        //  Reset the stroke
        g2d.setStroke(stroke);
        drawArrow(g2d);
    }

    protected Stroke getStroke() {
       return FULL_STROKE;
    }

    /**
     *  Description of the Method
     *
     *@param  way  Description of Parameter
     */
    public void select(boolean way) {
        for (int ndx = 0; ndx < vertices.length; ndx++) {
            vertices[ndx].select(way);
            vertices[ndx].active(false);
        }
    }


    /**
     *  Description of the Method
     *
     *@param  attempt  Description of Parameter
     *@return          Description of the Returned Value
     */
    public boolean hit(Point attempt) {
        activeVertex = hitVertex(attempt);
        if (activeVertex == -1) {
            int insertAt = hitSegment(attempt);
            if (insertAt == -1) {
                select(false);
                return false;
            }

            Vertex newOne = new Vertex(attempt);
            insertAt(insertAt, newOne);
            select(true);
            newOne.active(true);
            activeVertex = insertAt;
        } else {
            select(true);
            vertices[activeVertex].active(true);
        }

        return (activeVertex >= 0);
    }


    /**
     *  Drag the current vertex
     *
     *@param  point  New location of the current vertex
     */
    public void drag(Point point) {
        if ((activeVertex > 0) && (activeVertex < vertices.length - 1)) {
            vertices[activeVertex].move(point);
            if (activeVertex == 1) {
                updateStart();
            }
            if (activeVertex == vertices.length - 2) {
                updateEnd();
            }
        }
    }


    /**
     *  The point was dropped
     */
    public void drop() {
        if ((activeVertex > 0) && (activeVertex < vertices.length - 1)) {
            vertices[activeVertex].active(false);

            if (shouldDelete(activeVertex)) {
                deleteVertex(activeVertex);
            }

            activeVertex = -1;
        }
    }


    /**
     *  Description of the Method
     *
     *@param  cevt  Description of Parameter
     */
    public void componentHidden(ComponentEvent cevt) { }


    /**
     *  Description of the Method
     *
     *@param  cevt  Description of Parameter
     */
    public void componentMoved(ComponentEvent cevt) {
        Component moved = cevt.getComponent();
        if (vertices.length == 2) {
            updateStart();
            updateEnd();
        } else if (moved.equals(startPanel)) {
            updateStart();
        } else if (moved.equals(endPanel)) {
            updateEnd();
        }
    }


    /**
     *  Description of the Method
     *
     *@param  cevt  Description of Parameter
     */
    public void componentResized(ComponentEvent cevt) {
        Component moved = cevt.getComponent();
        if (moved.equals(startPanel)) {
            updateStart();
        } else if (moved.equals(endPanel)) {
            updateEnd();
        }
    }


    /**
     *  Description of the Method
     *
     *@param  cevt  Description of Parameter
     */
    public void componentShown(ComponentEvent cevt) { }


    /**
     *  Saves a segmented to the output stream
     *
     *@param  output  the output stream
     */
    public void save(PrintWriter output) {
        output.print("S[");
        saveStartPanel(output);
        output.print(",");
        saveEndPanel(output);
        output.print("]");
        saveVertices(output);
        output.println("");
    }


    /**
     *  Determines whether this panel matches the two desired end points
     *
     *@param  start  the starting panel to be matched
     *@param  end    the ending panel to be matched
     *@return        true if it matches
     */
    public boolean match(EndPointPanel start, EndPointPanel end) {
        return (start.equals(startPanel) && end.equals(endPanel));
    }


    /**
     *  Loads a segmented line from a buffer
     *
     *@param  buffer  the buffer containing the vertices
     */
    public void load(String buffer) {
        StringTokenizer tok = new StringTokenizer(buffer, ":");
        String countString = tok.nextToken();
        int count;
        try {
            count = Integer.parseInt(countString);
        } catch (NumberFormatException nfe) {
            return;
        }
        vertices = new Vertex[count];

        String rest = tok.nextToken();
        tok = new StringTokenizer(rest, "(),");

        for (int ndx = 0; ndx < count; ndx++) {
            String strX = tok.nextToken();
            String strY = tok.nextToken();

            try {
                int x = Integer.parseInt(strX);
                int y = Integer.parseInt(strY);

                vertices[ndx] = new Vertex(new Point(x, y));
            } catch (NumberFormatException nfe) {
                vertices[ndx] = new Vertex(new Point(0, 0));
            }
        }

        int modFiveSize = count / 5 + 1;
        Xs = new int[modFiveSize * 5];
        Ys = new int[modFiveSize * 5];
    }


    /**
     *  Shifts the entire line
     *
     *@param  x  the amount to shift in the x coordinate
     *@param  y  the amount to shift in the y coordinate
     */
    public void shift(int x, int y) {
        for (int ndx = 0; ndx < vertices.length; ndx++) {
            vertices[ndx].shift(x, y);
        }
    }


    /**
     *  Scales the entire line
     *
     *@param  value  the amount to scale
     */
    public void scale(double value) {
        for (int ndx = 0; ndx < vertices.length; ndx++) {
            vertices[ndx].scale(value);
        }
        scalingFactor = value;
    }


    /**
     *  Determine the point at which the last segment should stop
     *
     *@return    the point
     */
    protected Point getShortPoint() {
        int last = vertices.length;
        workingSegment.reset(vertices[last - 2].getPoint(),
                vertices[last - 1].getPoint());

        double t = workingSegment.findFromEnd(SHORT_BACK * scalingFactor);

        return workingSegment.getPoint(t);
    }


    /**
     *  Determine the point at which the last segment should stop
     *
     *@return    the point
     */
    protected Point getArrowPointAbove() {
        int last = vertices.length;
        workingSegment.reset(vertices[last - 2].getPoint(),
                vertices[last - 1].getPoint());

        double t = workingSegment.findFromEnd(SHORT_BACK * scalingFactor);

        return workingSegment.aboveLine(t, 10 * scalingFactor);
    }


    /**
     *  Determine the point at which the last segment should stop
     *
     *@return    the point
     */
    protected Point getArrowPointBelow() {
        int last = vertices.length;
        workingSegment.reset(vertices[last - 2].getPoint(),
                vertices[last - 1].getPoint());

        double t = workingSegment.findFromEnd(SHORT_BACK * scalingFactor);

        return workingSegment.belowLine(t, 10 * scalingFactor);
    }


    /**
     *  Updates the location of the end vertex
     */
    protected void updateEnd() {
        int last = vertices.length;
        Rectangle right = endPanel.getBounds();
        workingSegment.reset(vertices[last - 2].getPoint(), right);

        double t1 = workingSegment.intersect(right);

        vertices[last - 1].move(workingSegment.getPoint(t1));
    }




    protected Color getColor() {
       return Color.BLACK;
    }

    /**
     *  Draws the arrow and the last segment
     *
     *@param  g  the graphics object
     */
    protected void drawArrow(Graphics2D g) {
        Point shortPt = getShortPoint();

        int last = vertices.length;

        Point end = vertices[last - 1].getPoint();
        Point above = getArrowPointAbove();
        Point below = getArrowPointBelow();

        Xs[0] = (int) end.getX();
        Xs[1] = (int) above.getX();
        Xs[2] = (int) below.getX();
        Xs[3] = (int) end.getX();

        Ys[0] = (int) end.getY();
        Ys[1] = (int) above.getY();
        Ys[2] = (int) below.getY();
        Ys[3] = (int) end.getY();

        g.drawPolyline(Xs, Ys, 4);
    }


    /**
     *  Saves the start panel
     *
     *@param  output  the output stream
     */
    protected void saveStartPanel(PrintWriter output) {
        savePanel(output, startPanel);
    }


    /**
     *  Saves the end panel
     *
     *@param  output  the output stream
     */
    protected void saveEndPanel(PrintWriter output) {
        savePanel(output, endPanel);
    }


    /**
     *  Saves a panel
     *
     *@param  output  the output stream
     *@param  panel   the panel to be saved
     */
    protected void savePanel(PrintWriter output, EndPointPanel panel) {
        if (panel instanceof UMLType) {
            output.print(((UMLType) panel).getID());
        } else {
            output.print("???");
        }
    }


    /**
     *  Saves the vertices
     *
     *@param  output  the output stream
     */
    protected void saveVertices(PrintWriter output) {
        output.print("{");
        output.print(vertices.length);
        output.print(":");
        vertices[0].save(output);
        for (int ndx = 1; ndx < vertices.length; ndx++) {
            output.print(",");
            vertices[ndx].save(output);
        }
        output.print("}");
    }


    /**
     *  Initialize the end points of the segmented line.
     */
    private void initEndPoints() {
        Rectangle left = startPanel.getBounds();
        Rectangle right = endPanel.getBounds();
        workingSegment.reset(left, right);

        double t0 = workingSegment.intersect(left);
        double t1 = workingSegment.intersect(right);

        vertices[0] = new Vertex(workingSegment.getPoint(t0));
        vertices[vertices.length - 1] = new Vertex(workingSegment.getPoint(t1));
    }


    /**
     *  Updates the location the start vertex
     */
    private void updateStart() {
        Rectangle left = startPanel.getBounds();
        workingSegment.reset(left, vertices[1].getPoint());

        double t0 = workingSegment.intersect(left);

        vertices[0].move(workingSegment.getPoint(t0));
    }


    /**
     *  Description of the Method
     *
     *@param  ndx     Description of Parameter
     *@param  newOne  Description of Parameter
     */
    private void insertAt(int ndx, Vertex newOne) {
        Vertex[] newArray = new Vertex[vertices.length + 1];
        System.arraycopy(vertices, 0, newArray, 0, ndx);
        System.arraycopy(vertices, ndx, newArray, ndx + 1, vertices.length - ndx);
        newArray[ndx] = newOne;
        vertices = newArray;

        if (vertices.length > Xs.length) {
            Xs = new int[Xs.length + 5];
            Ys = new int[Ys.length + 5];
        }
    }


    /**
     *  Have we hit a particular vertex
     *
     *@param  point  the mouse input
     *@return        the index of the vertex that we have hit
     */
    private int hitVertex(Point point) {
        for (int ndx = 1; ndx < vertices.length - 1; ndx++) {
            if (vertices[ndx].hit(point)) {
                return ndx;
            }
        }

        return -1;
    }


    /**
     *  Description of the Method
     *
     *@param  attempt  Description of Parameter
     *@return          Description of the Returned Value
     */
    private int hitSegment(Point attempt) {
        for (int ndx = 0; ndx < vertices.length - 1; ndx++) {
            Point start = vertices[ndx].getPoint();
            Point end = vertices[ndx + 1].getPoint();
            workingSegment.reset(start, end);

            double distance = workingSegment.distanceToPoint(attempt);
            if ((distance > 0) && (distance < 3)) {
                return ndx + 1;
            }
        }

        return -1;
    }


    /**
     *  Should delete the vertices
     *
     *@param  active  Description of Parameter
     *@return         Description of the Returned Value
     */
    private boolean shouldDelete(int active) {
        Point pt = vertices[active].getPoint();

        //  Check if they are in the end rectangle
        if (startPanel.getBounds().contains(pt) || endPanel.getBounds().contains(pt)) {
            return true;
        }

        //  Check the other vertices
        for (int ndx = 0; ndx < vertices.length; ndx++) {
            if (ndx != active) {
                if (vertices[ndx].hit(pt)) {
                    return true;
                }
            }
        }

        //  Don't delete it
        return false;
    }


    /**
     *  Delete a vertex
     *
     *@param  dead  Description of Parameter
     */
    private void deleteVertex(int dead) {
        Vertex[] newArray = new Vertex[vertices.length - 1];
        System.arraycopy(vertices, 0, newArray, 0, dead);
        System.arraycopy(vertices, dead + 1, newArray, dead, vertices.length - dead - 1);
        vertices = newArray;
    }
}
