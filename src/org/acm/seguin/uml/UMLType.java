/*
 *  ====================================================================
 *  The JRefactory License, Version 1.0
 *
 *  Copyright (c) 2001 JRefactory.  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  JRefactory (http://www.sourceforge.org/projects/jrefactory)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "JRefactory" must not be used to endorse or promote
 *  products derived from this software without prior written
 *  permission. For written permission, please contact seguin@acm.org.
 *
 *  5. Products derived from this software may not be called "JRefactory",
 *  nor may "JRefactory" appear in their name, without prior written
 *  permission of Chris Seguin.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE CHRIS SEGUIN OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of JRefactory.  For more information on
 *  JRefactory, please see
 *  <http://www.sourceforge.org/projects/jrefactory>.
 */
package org.acm.seguin.uml;

import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.acm.seguin.summary.*;
import org.acm.seguin.uml.line.AssociationRelationship;
import org.acm.seguin.uml.line.DragPanelAdapter;
import org.acm.seguin.uml.line.EndPointPanel;
import org.acm.seguin.uml.line.LabelSizeComputation;
import org.acm.seguin.uml.line.SizableLabel;

/**
 *  Displays the summary of a type object
 *
 *@author     Chris Seguin
 *@created    June 7, 1999
 *@version    SDR 3.0 $Id: UMLType.java,v 1.3 2003/07/18 16:14:54 mikeatkinson Exp $
 *@since      empty
 */
public class UMLType extends EndPointPanel implements ISourceful {
   /**
    *  Description of the Field
    */
   protected int borderWidth = 2;
   private int high;
   /**
    *  Description of the Field
    */
   protected int lineSize = 0;
   private SizableLabel nameLabel;
   //  Instance Variables
   private UMLPackage parent;
   private RoleHolder roles;
   private int state;
   private int titleHeight;
   private TypeSummary type;
   private int wide;

   //  States
   private final static int DEFAULT = 0;
   private final static int FOREIGN = 2;
   private final static int SELECTED = 1;

   private final static int TITLE_BORDER = 4;

   //  Colors
   private static Color defaultBG = null;
   private static Color foreignBG;
   private static Color selectedBG;
   private static Color selectedForeignBG;


   /**
    *  Create a new instance of a UMLType
    *
    *@param  initParent  the parent
    *@param  initType    the initial type data
    *@param  foreign     Description of Parameter
    */
   public UMLType(UMLPackage initParent, TypeSummary initType, boolean foreign) {
      super(null, true);

      //  Remember local variables
      parent = initParent;
      type = initType;
      wide = 0;
      high = 0;

      if (foreign) {
         state = FOREIGN;
      } else {
         state = DEFAULT;
      }

      //  Create a mouse listener
      UMLMouseAdapter listener = new UMLMouseAdapter(parent, this, null);
      addMouseListener(listener);

      //  Create another adapter for draging this
      DragPanelAdapter adapter = new DragPanelAdapter(this, parent);
      addMouseListener(adapter);
      addMouseMotionListener(adapter);

      //  Add the name label
      nameLabel = new SizableLabel(type.getName());
      nameLabel.setLocation(borderWidth, borderWidth);
      nameLabel.setSLHorizontalAlignment(JLabel.CENTER);
      nameLabel.setSLFont(UMLLine.getProtectionFont(true, type.getModifiers()));
      add(nameLabel);
      nameLabel.addMouseListener(listener);
      nameLabel.addMouseListener(adapter);
      nameLabel.addMouseMotionListener(adapter);

      //  Check to see if we need a role
      roles = new RoleHolder(listener, adapter);
      if (type.isInterface()) {
         roles.add(((char)171) + "Interface" + ((char)187));
      }
      if (foreign) {
         roles.add("Package:  " + getPackageName());
      }

      if (roles.hasAny()) {
         add(roles);
      }

      //  Add attribute labels
      Iterator iter = type.getFields();
      if (iter != null) {
         while (iter.hasNext()) {
            UMLField field = new UMLField(parent, this, (FieldSummary)iter.next(), adapter);
            add(field);
         }
      }

      iter = type.getMethods();
      if (iter != null) {
         while (iter.hasNext()) {
            MethodSummary nextMethod = (MethodSummary)iter.next();
            if (!nextMethod.isInitializer()) {
               UMLMethod method = new UMLMethod(parent, this, nextMethod, adapter);
               add(method);
            }
         }
      }

      //  Add nested types label
      int nestedTypes = type.getTypeCount();
      if (nestedTypes > 0) {
         iter = type.getTypes();
         if (iter != null) {
            while (iter.hasNext()) {
               UMLNestedType nestedType = new UMLNestedType(parent, this, (TypeSummary)iter.next(), adapter);
               add(nestedType);
            }
         }
      }

      computeSizes();//  Compute the sizes for the whole thing
   }


   /**
    *  Sets the Selected attribute of the UMLType object
    *
    *@param  way  The new Selected value
    */
   public void setSelected(boolean way) {
      if (way) {
         select();
      } else {
         deselect();
      }
   }


   /**
    *  Count the number of attributes
    *
    *@return    the number of attributes
    */
   private int getAttributeCount() {
      int result = 0;

      Component[] children = getComponents();
      for (int ndx = 0; ndx < children.length; ndx++) {
         if (children[ndx] instanceof UMLField) {
            result++;
         }
      }

      return result;
   }


   /**
    *  Return the background color
    *
    *@return    the background color
    */
   public Color getBackgroundColor() {
      if (defaultBG == null) {
         UMLType.initColors();
      }

      if (state == SELECTED) {
         return selectedBG;
      }
      if (state == FOREIGN) {
         return foreignBG;
      }
      if (isSelected() && isForeign()) {
         return selectedForeignBG;
      }
      return defaultBG;
   }


   /**
    *  Count the number of attributes
    *
    *@param  name  Description of Parameter
    *@return       the number of attributes
    */
   public UMLField getField(String name) {
      if (name == null) {
         return null;
      }

      Component[] children = getComponents();
      int last = children.length;

      for (int ndx = 0; ndx < last; ndx++) {
         if (children[ndx] instanceof UMLField) {
            UMLField field = (UMLField)children[ndx];
            if (name.equals(field.getSummary().getName())) {
               return field;
            }
         }
      }

      return null;
   }


   /**
    *  Returns an identifier for a type
    *
    *@return    an identifier for this panel
    */
   public String getID() {
      return type.getPackageSummary().getName() + ":" + type.getName();
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
    *  Get the UML package that is holding this
    *
    *@return    the package
    */
   public UMLPackage getPackage() {
      return parent;
   }


   /**
    *  Get the name of the package
    *
    *@return    the package name
    */
   private String getPackageName() {
      Summary current = type;
      while (!(current instanceof PackageSummary)) {
         current = current.getParent();
      }

      return ((PackageSummary)current).getName();
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
    *  Returns the type summary for this class
    *
    *@return    the type summary
    */
   public Summary getSourceSummary() {
      return type;
   }


   /**
    *  Get the summary
    *
    *@return    the summary
    */
   public TypeSummary getSummary() {
      return type;
   }


   /**
    *  Determine if this is foreign
    *
    *@return    true if this is foreign
    */
   public boolean isForeign() {
      return ((state & FOREIGN) > 0);
   }


   /**
    *  Determine if this is selected
    *
    *@return    true if this is selected
    */
   public boolean isSelected() {
      return ((state & SELECTED) > 0);
   }


   /**
    *  Compute the line size
    *
    *@return    Description of the Returned Value
    */
   private int computeLineSize() {
      LabelSizeComputation lsc = LabelSizeComputation.get();
      int height = lsc.computeHeight("Temp", UMLLine.defaultFont);
      return height + 2 * UMLLine.labelMargin;
   }


   /**
    *  Resizes and repositions the compontents
    */
   private void computeSizes() {
      //  Local Variables
      Component[] children = getComponents();
      int last = children.length;

      //  Set the default sizes
      wide = 0;
      high = 0;

      if (lineSize <= 0) {
         lineSize = computeLineSize();
      }

      //  Get the size of the title
      Dimension titleSize = nameLabel.getPreferredSize();
      titleHeight = titleSize.height;
      wide = titleSize.width + 2 * borderWidth;
      if (roles.hasAny()) {
         roles.setLocation(borderWidth, borderWidth + titleSize.height);
         Dimension roleSize = roles.getPreferredSize();
         titleHeight += roleSize.height;
         wide = Math.max(roleSize.width, wide);
      }

      //  Add attribute labels
      int nY = titleHeight + 2 * borderWidth;
      boolean foundField = false;

      for (int ndx = 0; ndx < last; ndx++) {
         if (children[ndx] instanceof UMLField) {
            UMLField field = (UMLField)children[ndx];
            field.setLocation(borderWidth, nY);
            nY += lineSize;
            wide = Math.max(wide, field.getPreferredSize().width);
            foundField = true;
         }
      }
      if (!foundField) {
         nY += lineSize;
      }

      //  Add operation label
      nY += borderWidth;
      boolean foundMethod = false;

      for (int ndx = 0; ndx < last; ndx++) {
         if (children[ndx] instanceof UMLMethod) {
            UMLMethod method = (UMLMethod)children[ndx];
            method.setLocation(borderWidth, nY);
            nY += lineSize;
            wide = Math.max(wide, method.getPreferredSize().width);
            foundMethod = true;
         }
      }

      if (!foundMethod) {
         nY += lineSize;
      }

      //  Add nested types label
      int nestedTypes = type.getTypeCount();
      if (nestedTypes > 0) {
         nY += borderWidth;
         for (int ndx = 0; ndx < last; ndx++) {
            if (children[ndx] instanceof UMLNestedType) {
               UMLNestedType nestedType = (UMLNestedType)children[ndx];
               nestedType.setLocation(borderWidth, nY);
               nY += lineSize;
               wide = Math.max(wide, nestedType.getPreferredSize().width);
            }
         }
      }

      //  Add the final extra space at the bottom
      high = nY + 3 * borderWidth;

      //  Set the size
      nameLabel.setSize(wide, titleSize.height);
      if (roles.hasAny()) {
         roles.resetWidth(wide);
      }

      //  Revise the width
      wide += (2 * borderWidth);

      //  Set the size for the whole thing
      setSize(getPreferredSize());
   }


   /**
    *  Convert an attribute to an association
    *
    *@param  packagePanel  the package panel
    *@param  fieldPanel    the field panel
    *@return               the new segmented line
    */
   public AssociationRelationship convertToAssociation(UMLPackage packagePanel, UMLField fieldPanel) {
      remove(fieldPanel);
      resize();
      packagePanel.add(fieldPanel);

      TypeSummary typeSummary = fieldPanel.getType();
      JPanel endPanel = packagePanel.findType(typeSummary);
      if (endPanel == null) {
         endPanel = new UMLType(packagePanel, typeSummary, true);
         packagePanel.add(endPanel);
         endPanel.setLocation(0, 0);
      }

      AssociationRelationship result = new AssociationRelationship(this, (EndPointPanel)endPanel, fieldPanel);
      packagePanel.add(result);

      return result;
   }


   /**
    *  Convert from an association to an attribute
    *
    *@param  packagePanel  the package panel
    *@param  fieldPanel    the field panel
    */
   public void convertToAttribute(UMLPackage packagePanel, UMLField fieldPanel) {
      packagePanel.remove(fieldPanel);
      packagePanel.removeAssociation(fieldPanel);
      add(fieldPanel);
      resize();
   }


   /**
    *  Select this item
    */
   public void deselect() {
      state = state & ~SELECTED;
      repaint();
   }


   /**
    *  Draws the frame
    *
    *@param  g  the graphics object
    *@param  x  the x coordinate
    *@param  y  the y coordinate
    */
   private void drawFrame(Graphics g, int x, int y) {
      g.setColor(UMLType.getFrameColor());
      Dimension size = getSize();
      double scalingFactor = getScale();

      //  Draw outer edge
      g.drawRect(x, y, size.width - 1, size.height - 1);
      if (scalingFactor > 0.5) {
         g.drawRect(x + 1, y + 1, size.width - 3, size.height - 3);
      }

      //  Separate name from field
      g.drawLine(x, (int)(4 + y + scalingFactor * (titleHeight)),
         x + size.width - 1, (int)(4 + y + scalingFactor * (titleHeight)));
      if (scalingFactor > 0.5) {
         g.drawLine(x, (int)(y + scalingFactor * (titleHeight + 5)),
            x + size.width - 1, (int)(5 + y + scalingFactor * (titleHeight)));
      }

      //  Separate field from methods
      int high = (int)(5 + scalingFactor * (titleHeight + lineSize * Math.max(1, getAttributeCount())));
      g.drawLine(x, y + high, x + size.width - 1, y + high);
      if (scalingFactor > 0.5) {
         g.drawLine(x, y + high + 1, x + size.width - 1, y + high + 1);
      }

      //  Check if there are any nested types - if so draw their frame
      int typeCount = type.getTypeCount();
      if (typeCount > 0) {
         int previousLabels = Math.max(1, getAttributeCount()) + Math.max(1, type.getMethodCount());
         high = (int)(4 + scalingFactor * (titleHeight + lineSize * previousLabels));
         g.drawLine(x, y + high, x + size.width - 1, y + high);
         if (scalingFactor > 0.5) {
            g.drawLine(x, y + high + 1, x + size.width - 1, y + high + 1);
         }
      }
   }


   /**
    *  Load the type
    *
    *@param  buffer  the buffer
    */
   public void load(String buffer) {
      StringTokenizer tok = new StringTokenizer(buffer, ",");
      String strX = tok.nextToken();
      String strY = tok.nextToken();

      try {
         setLocation(Integer.parseInt(strX), Integer.parseInt(strY));
      } catch (NumberFormatException nfe) {
      }
   }


   /**
    *  Paint this object
    *
    *@param  g  the graphics object
    */
   public void paint(Graphics g) {
      //  Set the background color
      Color bg = getBackgroundColor();
      setBackground(bg);
      roles.setBackground(bg);

      //  Paint the components
      super.paint(g);

      drawFrame(g, 0, 0);
   }


   /**
    *  Print this object
    *
    *@param  g  the graphics object
    *@param  x  the x coordinate
    *@param  y  the y coordinate
    */
   public void print(Graphics g, int x, int y) {
      //  Set the background color
      Rectangle bounds = getBounds();
      g.setColor(getBackgroundColor());
      g.fillRect(x, y, bounds.width, bounds.height);

      //  Draw the title
      Point pt = nameLabel.getLocation();
      nameLabel.print(g, x + pt.x, y + pt.y);

      //  Draw the role
      if (roles.hasAny()) {
         pt = roles.getLocation();
         roles.print(g, x + pt.x, y + pt.y);
      }

      //  Paint the components
      Component[] children = getComponents();
      int last = children.length;

      for (int ndx = 0; ndx < last; ndx++) {
         if (children[ndx] instanceof UMLLine) {
            pt = children[ndx].getLocation();
            ((UMLLine)children[ndx]).print(g, x + pt.x, y + pt.y);
         }
      }

      drawFrame(g, x, y);
   }


   /**
    *  Resizes and repositions the compontents, and repaints them.
    */
   public void resize() {
      computeSizes();
      parent.repaint();
   }


   /**
    *  Save the files
    *
    *@param  output  the output stream
    */
   public void save(PrintWriter output) {
      Point pt = getUnscaledLocation();
      output.println("P[" + getID() + "]{" + pt.x + "," + pt.y + "}");
   }


   /**
    *  Sets the scaling factor
    *
    *@param  value  scaling factor
    */
   public void scale(double value) {
      super.scale(value);
      nameLabel.scale(value);
      roles.scale(value);

      //  Rescale the children
      Component[] children = getComponents();
      for (int ndx = 0; ndx < children.length; ndx++) {
         if (children[ndx] instanceof UMLLine) {
            ((UMLLine)children[ndx]).scale(value);
         }
      }
   }


   /**
    *  Select this item
    */
   public void select() {
      state = state | SELECTED;
      repaint();
   }


   /**
    *  Toggle the selected state
    */
   public void toggleSelect() {
      state = state ^ SELECTED;
      repaint();
   }


   /**
    *  Return the frame color
    *
    *@return    the frame color
    */
   private static Color getFrameColor() {
      return Color.black;
   }


   /**
    *  Initializes the background colors for the various classes
    */
   private static synchronized void initColors() {
      if (defaultBG == null) {
         defaultBG = Color.white;
         selectedBG = new Color(250, 255, 220);
         foreignBG = new Color(200, 200, 255);
         selectedForeignBG = new Color(220, 255, 220);
      }
   }
}

