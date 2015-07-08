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

import java.awt.*;
import java.awt.print.PageFormat;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import javax.swing.*;
import org.acm.seguin.awt.ExceptionPrinter;
import org.acm.seguin.ide.common.ClassListPanel;
import org.acm.seguin.io.Saveable;
import org.acm.seguin.summary.*;
import org.acm.seguin.uml.line.AssociationRelationship;
import org.acm.seguin.uml.line.LineMouseAdapter;
import org.acm.seguin.uml.line.LinedPanel;
import org.acm.seguin.uml.line.SegmentedLine;
import org.acm.seguin.uml.line.Vertex;
import org.acm.seguin.uml.print.PrintAdapter;
import org.acm.seguin.uml.print.UMLPagePrinter;
import org.acm.seguin.util.FileSettings;
import org.acm.seguin.util.MissingSettingsException;
import org.acm.seguin.uml.line.ImplementsRelationship;
import org.acm.seguin.uml.line.InheretenceRelationship;
import org.acm.seguin.uml.line.EndPointPanel;

/**
 *  Draws a UML diagram for all the classes in a package
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public class UMLPackage extends LinedPanel implements Saveable {
    //  Instance Variables
    private PackageSummary summary;
    private SegmentedLine currentLine = null;
    private boolean hasChanged;
    private String packageName;
    private JScrollPane scrollPane;
    private ClassListPanel classListPanel = null;
    private boolean first = false;
    private boolean loading = false;
    private double scale = 1.0;


    /**
     *  Constructor for UMLPackage
     *
     *@param  packageSummary  the summary of the package
     */
    public UMLPackage(PackageSummary packageSummary) {
        setSummary(packageSummary);
        initialise(new PackageLoader(this, summary));
    }
    
    /**
     *  Constructor for UMLPackage
     *
     *@param  filename  the name of the file
     */
    public UMLPackage(String filename) {
        initialise(new PackageLoader(this, filename));
    }


    /**
     *  Constructor for UMLPackage
     *
     *@param  input  the input stream
     */
    public UMLPackage(InputStream input) {
        initialise(new PackageLoader(this, input));
    }


    private void initialise(PackageLoader loader) {
        //  Initialize the instance variables
        defaultValues();

        //  Don't use a layout manager
        setLayout(null);

        //  Reset the size
        setSize(getPreferredSize());

        addMouseAdapter();

        //  Load the summaries
        new Thread(loader).start();
    }


    /**
     *  Sets the Dirty attribute of the UMLPackage object
     */
    public void setDirty() {
        hasChanged = true;
    }


    /**
     *  Sets the ScrollPane attribute of the UMLPackage object
     *
     *@param  value  The new ScrollPane value
     */
    public void setScrollPane(JScrollPane value) {
        scrollPane = value;
    }


    /**
     *  Sets the class list panel
     *
     *@param  value  the new list
     */
    public void setClassListPanel(ClassListPanel value) {
        classListPanel = value;
        first = true;
    }


    /**
     *  Sets the loading value
     *
     *@param  value  The new Loading value
     */
    public void setLoading(boolean value) {
        loading = value;
    }


    /**
     *  Gets the PackageName attribute of the UMLPackage object
     *
     *@return    The PackageName value
     */
    public String getPackageName() {
        return packageName;
    }


    /**
     *  Get the components that are UMLTypes
     *
     *@return    Description of the Returned Value
     */
    public UMLType[] getTypes() {
        //  Instance Variables
        Component[] children = getComponents();
        int last = children.length;
        int count = 0;

        //  Count the UMLTypes
        for (int ndx = 0; ndx < last; ndx++) {
            if (children[ndx] instanceof UMLType) {
                count++;
            }
        }

        //  Count the UMLTypes
        UMLType[] results = new UMLType[count];
        int item = 0;
        for (int ndx = 0; ndx < last; ndx++) {
            if (children[ndx] instanceof UMLType) {
                results[item] = (UMLType) children[ndx];
                item++;
            }
        }

        //  Return the result
        return results;
    }


    /**
     *  Sets the scale and resizes the panel.
     *
     *@param  value  Description of Parameter
     */
    public void scale(double value) {
       super.scale(value);
       this.scale = value;
       setSize(getPreferredSize());
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
        //  Initialize local variables
        int wide = 10;
        int high = 10;
        Component[] children = getComponents();
        int last = children.length;

        //  Deselect the children
        for (int ndx = 0; ndx < last; ndx++) {
            Rectangle bounds = children[ndx].getBounds();
            wide = Math.max(wide, 20 + bounds.x + bounds.width);
            high = Math.max(high, 20 + bounds.y + bounds.height);
        }

        return new Dimension(wide, high);
    }


    /**
     *  Get the summary
     *
     *@return    The package summary
     */
    public PackageSummary getSummary() {
        return summary;
    }


    /**
     *  Gets the File attribute of the UMLPackage object
     *
     *@return    The File value
     */
    public File getFile() {
        return PackageLoader.getFile(this);
    }


    /**
     *  Gets the Dirty attribute of the UMLPackage object
     *
     *@return    The Dirty value
     */
    public boolean isDirty() {
        return hasChanged;
    }


    /**
     *  Determines the title
     *
     *@return    the title
     */
    public String getTitle() {
        return ("UML Diagram for " + packageName);
    }


    /**
     *  Remove the association
     *
     *@param  field  Description of Parameter
     */
    public void removeAssociation(UMLField field) {
        Iterator iter = getLines();
        while (iter.hasNext()) {
            Object next = iter.next();
            if (next instanceof AssociationRelationship) {
                AssociationRelationship assoc = (AssociationRelationship) next;
                if (assoc.getField().equals(field)) {
                    assoc.delete();
                    iter.remove();
                    return;
                }
            }
        }
    }


    /**
     *  Paint this object
     *
     *@param  g  the graphics object
     */
    public void paint(Graphics g) {
        setBackground(Color.lightGray);
        g.setColor(Color.lightGray);
        Dimension size = getSize();
        g.fillRect(0, 0, size.width, size.height);

        //  Draw the grid
        PageFormat pf = UMLPagePrinter.getPageFormat(false);
        if (pf != null) {
            int pageHeight = (int) UMLPagePrinter.getPageHeight();
            int pageWidth = (int) UMLPagePrinter.getPageWidth();

            g.setColor(Color.gray);
            for (int x = pageWidth; x < size.width; x += pageWidth) {
                g.drawLine(x, 0, x, size.height);
            }

            for (int y = pageHeight; y < size.width; y += pageHeight) {
                g.drawLine(0, y, size.width, y);
            }
        }

        //  Abort once we are loading
        if (loading) {
            return;
        }

        //  Draw the segmented lines
        Iterator iter = getLines();
        while (iter.hasNext()) {
            ((SegmentedLine) iter.next()).paint(g);
        }

        //  Draw the components
        paintChildren(g);
    }


    /**
     *  Print this object
     *
     *@param  g  the graphics object
     *@param  x  the x coordinate
     *@param  y  the y coordinate
     */
    public void print(Graphics g, int x, int y) {
        Component[] children = getComponents();
        int last = children.length;

        for (int ndx = 0; ndx < last; ndx++) {
            if (children[ndx] instanceof UMLType) {
                Point pt = children[ndx].getLocation();
                ((UMLType) children[ndx]).print(g, x + pt.x, y + pt.y);
            } else if (children[ndx] instanceof UMLLine) {
                Point pt = children[ndx].getLocation();
                ((UMLLine) children[ndx]).print(g, x + pt.x, y + pt.y);
            }
        }

        Iterator iter = getLines();
        while (iter.hasNext()) {
            ((SegmentedLine) iter.next()).paint(g);
        }
    }


    /**
     *  Reloads the UML class diagrams
     */
    public void reload() {
        //  Save the image
        try {
            save();
        } catch (IOException ioe) {
            ExceptionPrinter.print(ioe, false);
        }

        //  Reload it
        PackageLoader loader = new PackageLoader(this, summary);
        new Thread(loader).start();

        //  Reset the size
        setSize(getPreferredSize());

        reset();

        //  Nothing has changed
        hasChanged = false;
    }


    /**
     *  Description of the Method
     */
    public void clear() {
        removeAll();
        super.clear();
    }


    /**
     *  Determine what you hit
     *
     *@param  actual  The hit location
     */
    public void hit(Point actual) {
        currentLine = null;
        Iterator iter = getLines();
        while ((currentLine == null) && iter.hasNext()) {
            SegmentedLine next = (SegmentedLine) iter.next();
            if (next.hit(actual)) {
                currentLine = next;
            }
        }

        while (iter.hasNext()) {
            SegmentedLine next = (SegmentedLine) iter.next();
            next.select(false);
        }

        repaint();
    }


    /**
     *  Dragging a segmented line point
     *
     *@param  actual  The mouse's current location
     */
    public void drag(Point actual) {
        if (currentLine != null) {
            currentLine.drag(actual);
            repaint();
        }
    }


    /**
     *  User dropped an item
     */
    public void drop() {
        if (currentLine != null) {
            currentLine.drop();

            hasChanged = true;

            currentLine = null;
        }

        reset();
    }


    /**
     *  Save the files
     *
     *@exception  IOException  Description of Exception
     */
    public void save() throws IOException {
        //  Make sure we have something that has changed
        if (!hasChanged) {
            return;
        }

        //  Local Variables
        Component[] children = getComponents();
        int last = children.length;

        File outputFile = PackageLoader.getFile(summary);
        //File dir = summary.getDirectory();
        //File outputFile;
        //if (dir == null) {
        //    dir = new File(FileSettings.getRefactorySettingsRoot(), "UML");
        //    dir.mkdirs();
        //    outputFile = new File(dir +
        //            File.separator + summary.getName() + ".uml");
        //} else {
        //    outputFile = new File(summary.getDirectory(), "package.uml");
        //}
        PrintWriter output = new PrintWriter(new FileWriter(outputFile));

        output.println("V[1.1:" + summary.getName() + "]");

        //  Save the line segments
        Iterator iter = getLines();
        while (iter.hasNext()) {
            ((SegmentedLine) iter.next()).save(output);
        }

        //  Save the types
        for (int ndx = 0; ndx < last; ndx++) {
            if (children[ndx] instanceof UMLType) {
                ((UMLType) children[ndx]).save(output);
            }
        }

        output.close();

        //  Nothing has changed
        hasChanged = false;
    }


    /**
     *  Tells the scrollbar to jump to this location
     *
     *@param  type  Description of Parameter
     */
    public void jumpTo(TypeSummary type) {
        UMLType umlType = findType(type);
        if (umlType == null) {
            return;
        }

        Point pt = umlType.getLocation();

        JScrollBar horiz = scrollPane.getHorizontalScrollBar();
        horiz.setValue(pt.x - 10);
        JScrollBar vert = scrollPane.getVerticalScrollBar();
        vert.setValue(pt.y - 10);
    }


    /**
     *  Find the type based on a summary
     *
     *@param  searching  the variable we are searching for
     *@return            the UML type object
     */
    protected UMLType findType(TypeSummary searching) {
        //  Instance Variables
        Component[] children = getComponents();
        int last = children.length;
        int count = 0;
        TypeSummary current;

        if (searching == null) {
            return null;
        }

        //  Count the UMLTypes
        for (int ndx = 0; ndx < last; ndx++) {
            if (children[ndx] instanceof UMLType) {
                current = ((UMLType) children[ndx]).getSummary();
                if (searching.equals(current)) {
                    return (UMLType) children[ndx];
                }
            }
        }

        //  Not found
        return null;
    }


    /**
     *  Find the type based on a id code
     *
     *@param  id  the code we are searching for
     *@return     the UML type object
     */
    protected UMLType find(String id) {
        //  Instance Variables
        Component[] children = getComponents();
        int last = children.length;
        int count = 0;
        String current;

        if (id == null) {
            return null;
        }

        //  Find the id that matches
        for (int ndx = 0; ndx < last; ndx++) {
            if (children[ndx] instanceof UMLType) {
                current = ((UMLType) children[ndx]).getID();
                if (id.equals(current)) {
                    return (UMLType) children[ndx];
                }
            }
        }

        //  Not found
        return null;
    }


    /**
     *  Find the type based on a id code
     *
     *@param  panel1  Description of Parameter
     *@param  panel2  Description of Parameter
     *@return         the UML type object
     */
    protected SegmentedLine find(String panel1, String panel2) {
        UMLType first = find(panel1);
        UMLType second = find(panel2);

        if ((first == null) || (second == null)) {
            return null;
        }

        Iterator iter = getLines();
        while (iter.hasNext()) {
            SegmentedLine line = (SegmentedLine) iter.next();
            if (line.match(first, second)) {
                return line;
            }
        }

        return null;
    }


    /**
     *  Sets the summary
     *
     *@param  value  The package summary
     */
    void setSummary(PackageSummary value) {
        summary = value;
        if (summary != null) {
            packageName = summary.getName();
        }
    }


    /**
     *  Tells the class list panel to laod itself
     */
    void updateClassListPanel() {
        setSize(getPreferredSize());
        if (classListPanel == null) {
            return;
        }

        if (first) {
            first = false;
            return;
        }

        classListPanel.load(summary);
    }


    /**
     *  Set up the default values
     */
    private void defaultValues() {
        packageName = "Unknown Package";
        hasChanged = false;

        try {
            FileSettings umlBundle = FileSettings.getRefactorySettings("uml");
            umlBundle.setContinuallyReload(true);
            Vertex.setVertexSize(umlBundle.getInteger("sticky.point.size"));
            Vertex.setNear(umlBundle.getDouble("halo.size"));
        } catch (MissingSettingsException mse) {
            Vertex.setNear(3.0);
            Vertex.setVertexSize(5);
        }
    }


    /**
     *  Adds a feature to the MouseAdapter attribute of the UMLPackage object
     */
    private void addMouseAdapter() {
        LineMouseAdapter adapter = new LineMouseAdapter(this);
        addMouseListener(adapter);
        addMouseMotionListener(adapter);
    }


    /**
     *  Resets the scroll panes
     */
    public void reset() {
        if (scrollPane == null) {
            setSize(getPreferredSize());
            repaint();
        } else {
            Dimension panelSize = getPreferredSize();
            JViewport view = scrollPane.getViewport();
            Dimension viewSize = view.getSize();
            setSize(Math.max(panelSize.width, viewSize.width),
                    Math.max(panelSize.height, viewSize.height));
            view.setViewSize(getSize());
            scrollPane.repaint();
        }
    }
    
    
    /**
     * Initialises the positions of all types randomly.
     *
     *@since 2.7.04
     */
    public void resetPositions() {
        UMLType[] types = getTypes();
        int size = (int)(Math.sqrt(types.length)*1000);
        Random r = new Random();
        for (int i=0; i<types.length; i++) {
            types[i].setLocation(r.nextInt(size), r.nextInt(size));
        }
        //scale(1.0);
    }
    private Map forces = new HashMap();

    /**
     * Uses a mixture of Simulated Annealling, springs along relationships and
     * gravity (to position subclasses below their parents).
     *
     *@param temperature notional initial temperature of the diagram, a value of 2000 works well.
     *@param iterations  number of times to reduce temperature
     *@since 2.7.04
     */
    public void rearragePositions(int temperature, int iterations, double springStrength) {
        //System.out.println("rearragePositions()");
        double oldScale = scale;
        scale(1.0);
        Map groups = group();
        Random r = new Random();
        UMLType[] types = getTypes();
            
        for (int times=1; times<=iterations; times++) {
            for (int i=0; i<types.length; i++) {
                types[i].shift(r.nextInt(temperature)/(times*times), r.nextInt(temperature)/(times*times*times));
            }
            forces = new HashMap();
            Iterator iter = getLines();
            while (iter.hasNext()) {
                SegmentedLine line = (SegmentedLine) iter.next();
                EndPointPanel start = line.getStartPanel();
                EndPointPanel end = line.getEndPanel();
                if (times<(iterations-10) ) {
                    addForce(start, end, start.getBounds(), end.getBounds(), -springStrength*0.5/times);
                    if (line instanceof InheretenceRelationship || line instanceof ImplementsRelationship) {
                       addGravity(start, temperature/(times*times));
                       addGravity(end, -temperature/(times*times));
                    }
                }
            }
            for (int i=0; i<types.length; i++) {
                for (int j=i+1; j<types.length; j++) {
                    EndPointPanel start = types[i];
                    EndPointPanel end = types[j];
                    int startGroup = ((Integer)groups.get(start)).intValue();
                    int endGroup = ((Integer)groups.get(end)).intValue();
                    double pushStrength = (startGroup == endGroup) ? springStrength : springStrength*4.0;
                    double pullStrength = (startGroup == endGroup) ? springStrength*0.3 : springStrength*0.5/times;

                    Rectangle boundsStart = start.getBounds();
                    Rectangle boundsEnd = end.getBounds();
                    //if (boundsStart.intersects(boundsEnd)) {
                    //    //System.out.println("intersects inner");
                    //    addForce(start, end, boundsStart, boundsEnd, pushStrength*5.0/times);
                    //} else {
                        boundsStart.x -= 60;
                        boundsStart.y -= 60;
                        boundsStart.width += 120;
                        boundsStart.height += 120;
                        boundsEnd.x -= 60;
                        boundsEnd.y -= 60;
                        boundsEnd.width += 120;
                        boundsEnd.height += 120;
                        if (boundsStart.intersects(boundsEnd)) {
                            //System.out.println("intersects outer");
                            addInvSqForce(start, end, boundsStart, boundsEnd, pushStrength*40.0);
                        } else {
                            if (times<(iterations-10) ) {
                                addForce(start, end, start.getBounds(), end.getBounds(), -pullStrength/(times*types.length));
                            }
                        }
                    //}
                }
            }
            Iterator i = forces.keySet().iterator();
            while (i.hasNext()) {
                EndPointPanel t = (EndPointPanel)i.next();
                Pair pair = (Pair)forces.get(t);
                t.shift((int)pair.x, (int)pair.y);
                //System.out.println("t="+t+" shift by("+(int)pair.x+", "+(int)pair.y+")");
            }
        }
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        for (int i=0; i<types.length; i++) {
           Rectangle bounds = types[i].getBounds();
           minX = Math.min(minX, bounds.x);
           minY = Math.min(minY, bounds.y);
        }
        for (int i=0; i<types.length; i++) {
           types[i].shift(20-minX, 20-minY);
        }
        forces = null;
        scale(oldScale);
    }

    private static class Pair {
       double x = 0.0;
       double y = 0.0;
    }
    private void addGravity(EndPointPanel start, double strength) {
          //System.out.println("  addGravity()");
          Pair pair = (Pair)forces.get(start);
          if (pair == null) {
              pair = new Pair();
              forces.put(start, pair);
          }
          pair.y += strength;
    }
    private void addForce(EndPointPanel start, EndPointPanel end, Rectangle boundsStart, Rectangle boundsEnd, double strength) {
          //System.out.println("  addForce()");
          int startMiddleX = boundsStart.x + boundsStart.width/2;
          int startMiddleY = boundsStart.y + boundsStart.height/2;
          int endMiddleX = boundsEnd.x + boundsEnd.width/2;
          int endMiddleY = boundsEnd.y + boundsEnd.height/2;
          int diffX = startMiddleX - endMiddleX;
          int diffY = startMiddleY - endMiddleY;
          //double force = Math.sqrt(diffX*diffX + diffY*diffY);
          Pair pair = (Pair)forces.get(start);
          if (pair == null) {
              pair = new Pair();
              forces.put(start, pair);
          }
          
          pair.x += strength * diffX;
          pair.y += strength * diffY;
          //System.out.println("    start="+start+", x="+(strength * diffX)+", y="+(strength * diffY) +" => force=("+pair.x+","+pair.y+")");
          pair = (Pair)forces.get(end);
          if (pair == null) {
              pair = new Pair();
              forces.put(end, pair);
          }
          pair.x -= strength * diffX;
          pair.y -= strength * diffY;
          //System.out.println("    end="+end+", x="+(strength * diffX)+", y="+(strength * diffY) +" => force=("+pair.x+","+pair.y+")");
    }
    
    private void addInvSqForce(EndPointPanel start, EndPointPanel end, Rectangle boundsStart, Rectangle boundsEnd, double strength) {
          //System.out.println("  addForce()");
          int startMiddleX = boundsStart.x + boundsStart.width/2;
          int startMiddleY = boundsStart.y + boundsStart.height/2;
          int endMiddleX = boundsEnd.x + boundsEnd.width/2;
          int endMiddleY = boundsEnd.y + boundsEnd.height/2;
          int diffX = startMiddleX - endMiddleX;
          int diffY = startMiddleY - endMiddleY;
          double dSquared = diffX*diffX + diffY*diffY;
          double d = Math.sqrt(dSquared);
          if (dSquared<100) { dSquared = 100; }
          strength *= Math.sqrt(boundsStart.width*boundsStart.height);
          strength *= Math.sqrt(boundsEnd.width*boundsEnd.height);
          strength /= dSquared;
          Pair pair = (Pair)forces.get(start);
          if (pair == null) {
              pair = new Pair();
              forces.put(start, pair);
          }
          
          pair.x += strength * diffX/d;
          pair.y += strength * diffY/d;
          //System.out.println("    start="+start+", x="+(strength * diffX)+", y="+(strength * diffY) +" => force=("+pair.x+","+pair.y+")");
          pair = (Pair)forces.get(end);
          if (pair == null) {
              pair = new Pair();
              forces.put(end, pair);
          }
          pair.x -= strength * diffX/d;
          pair.y -= strength * diffY/d;
          //System.out.println("    end="+end+", x="+(strength * diffX)+", y="+(strength * diffY) +" => force=("+pair.x+","+pair.y+")");
    }
    
    private Map group() {
        UMLType[] types = getTypes();
        Map groups = new HashMap();
        for (int i=0; i<types.length; i++) {
            groups.put(types[i], new Integer(i));
        }
        Iterator iter = getLines();
        while (iter.hasNext()) {
            SegmentedLine line = (SegmentedLine) iter.next();
            EndPointPanel start = line.getStartPanel();
            EndPointPanel end = line.getEndPanel();
            int startGroup = ((Integer)groups.get(start)).intValue();
            int endGroup = ((Integer)groups.get(end)).intValue();
            if (startGroup != endGroup) {
                // join groups
                Iterator g = groups.entrySet().iterator();
                while (g.hasNext()) {
                    Map.Entry entry = (Map.Entry)g.next();
                    int v = ((Integer)entry.getValue()).intValue();
                    if (v==endGroup) {
                        entry.setValue(new Integer(startGroup));
                    }
                }
            }
        }
        return groups;
    }
}
