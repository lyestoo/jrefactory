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
import org.acm.seguin.summary.FieldSummary;
import org.acm.seguin.summary.TypeSummary;
import org.acm.seguin.summary.TypeDeclSummary;
import org.acm.seguin.uml.line.DragPanelAdapter;
import org.acm.seguin.summary.query.GetTypeSummary;
import org.acm.seguin.parser.ast.ModifierHolder;

/**
 *  Displays a single UML field in a line
 *
 *@author     Chris Seguin
 *@created    July 6, 1999
 */
public class UMLField extends UMLLine implements ISourceful {
    //  Instance Variables
    private FieldSummary summary;
    private UMLPackage current;
    private boolean association;
    private DragPanelAdapter parentDragAdapter;
    private DragPanelAdapter fieldDragAdapter;


    /**
     *  Create a new instance of a UMLLine
     *
     *@param  initCurrent  Description of Parameter
     *@param  parent       Description of Parameter
     *@param  field        Description of Parameter
     *@param  adapter      Description of Parameter
     */
    public UMLField(UMLPackage initCurrent, UMLType parent, FieldSummary field, DragPanelAdapter adapter) {
        super(parent, adapter);

        //  Set the instance variables
        summary = field;
        current = initCurrent;
        association = false;

        //  Reset the parent data
        //ModifierHolder modifiers = summary.getModifiers();
        //setProtection(UMLLine.getProtectionCode(modifiers));
        setProtection(UMLLine.getProtectionCode(summary));
        setLabelText(summary.toString());
        setLabelFont(UMLLine.getProtectionFont(false, summary));

        //  Reset the size
        setSize(getPreferredSize());

        //  Create another adapter for draging this
        parentDragAdapter = adapter;
        fieldDragAdapter = new DragPanelAdapter(this, initCurrent);

        //  Add a mouse listener
        addMouseListener(new UMLMouseAdapter(current, parent, this));
    }


    /**
     *  Transform into an association
     *
     *@param  way  Description of Parameter
     */
    public void setAssociation(boolean way) {
        association = way;
        if (association) {
            setLabelText(summary.getName());
            addMouseListener(fieldDragAdapter);
            addMouseMotionListener(fieldDragAdapter);
            removeMouseListener(parentDragAdapter);
            removeMouseMotionListener(parentDragAdapter);
            label.addMouseListener(fieldDragAdapter);
            label.addMouseMotionListener(fieldDragAdapter);
            label.removeMouseListener(parentDragAdapter);
            label.removeMouseMotionListener(parentDragAdapter);
        } else {
            setLabelText(summary.toString());
            addMouseListener(parentDragAdapter);
            addMouseMotionListener(parentDragAdapter);
            removeMouseListener(fieldDragAdapter);
            removeMouseMotionListener(fieldDragAdapter);
            label.addMouseListener(parentDragAdapter);
            label.addMouseMotionListener(parentDragAdapter);
            label.removeMouseListener(fieldDragAdapter);
            label.removeMouseMotionListener(fieldDragAdapter);
        }

        setSize(getPreferredSize());
    }


    /**
     *  Return the summary
     *
     *@return    Description of the Returned Value
     */
    public FieldSummary getSummary() {
        return summary;
    }


    /**
     *  Is this object represented as an association
     *
     *@return    Description of the Returned Value
     */
    public boolean isAssociation() {
        return association;
    }


    /**
     *  Is this object represented as an association
     *
     *@return    Description of the Returned Value
     */
    public boolean isConvertable() {
        TypeDeclSummary typeDecl = summary.getTypeDecl();
        if (typeDecl.isPrimitive()) {
            return false;
        }

        TypeSummary typeSummary = GetTypeSummary.query(typeDecl);
        return (typeSummary != null);
    }


    /**
     *  Description of the Method
     *
     *@return    Description of the Returned Value
     */
    public TypeSummary getType() {
        TypeDeclSummary typeDecl = summary.getTypeDecl();
        return GetTypeSummary.query(typeDecl);
    }


    /**
     *  Return the default background color
     *
     *@return    the color
     */
    protected Color getDefaultBackground() {
        if (association) {
            return Color.lightGray;
        } else {
            return super.getDefaultBackground();
        }
    }


    /**
     *  Gets the sourceSummary attribute of the UMLField object
     *
     *@return    The sourceSummary value
     */
    public org.acm.seguin.summary.Summary getSourceSummary() {
        return summary;
    }
}
