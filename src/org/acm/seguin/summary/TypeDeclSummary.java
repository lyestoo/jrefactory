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
package org.acm.seguin.summary;

import org.acm.seguin.parser.Node;
import org.acm.seguin.parser.ast.ASTResultType;
import org.acm.seguin.parser.ast.ASTPrimitiveType;
import org.acm.seguin.parser.ast.ASTReferenceType;
import org.acm.seguin.parser.ast.ASTType;
import org.acm.seguin.parser.ast.ASTName;
import org.acm.seguin.summary.query.GetTypeSummary;

/**
 *  Summarize a type declaration. This object is meant to store the name and
 *  package of some type. This will be used in variable summaries, as well as
 *  for return values and exceptions.
 *
 *@author     Chris Seguin
 *@created    May 11, 1999
 */
public class TypeDeclSummary extends Summary {
    //  Instance Variables
    private String typeName;
    private String packageName;
    private boolean primitive;
    private int arrayCount;


    /**
     *  Creates a type declaration summary.
     *
     *@param  parentSummary  the parent summary
     */
    public TypeDeclSummary(Summary parentSummary) {
        //  Initialize the parent class
        super(parentSummary);

        //  Remember the type name
        typeName = "void".intern();

        //  The package name doesn't apply
        packageName = null;

        //  This is a primitive value
        primitive = true;

        //  This isn't an array (yet)
        arrayCount = 0;
    }


    /**
     *  Creates a type declaration summary from an ASTName object.
     *
     *@param  parentSummary  the parent summary
     *@param  nameNode       the ASTName object
     */
    public TypeDeclSummary(Summary parentSummary, ASTName nameNode) {
        //  Initialize the parent class
        super(parentSummary);
        
        //  Local Variables
        int numChildren = nameNode.getNameSize();

        //  Determine the name type
        typeName = nameNode.getNamePart(numChildren - 1).intern();

        //  Extract the package
        if (numChildren > 1) {
            StringBuffer buffer = new StringBuffer(nameNode.getNamePart(0));
            for (int ndx = 1; ndx < numChildren - 1; ndx++) {
                buffer.append(".");
                buffer.append(nameNode.getNamePart(ndx));
            }
            packageName = buffer.toString().intern();
        } else {
            packageName = null;
        }

        //  This isn't a primitive value
        primitive = false;

        //  This isn't an array (yet)
        arrayCount = 0;
    }


    /**
     *  Creates a type declaration summary from an ASTReferenceType object.
     *
     *@param  parentSummary  the parent summary
     *@param  nameNode       the ASTName object
     */
    public TypeDeclSummary(Summary parentSummary, ASTReferenceType refNode) {
        //  Initialize the parent class
        super(parentSummary);

        if (refNode.jjtGetChild(0) instanceof ASTName) {
            ASTName nameNode = (ASTName)refNode.jjtGetChild(0);
            //  Local Variables
            int numChildren = nameNode.getNameSize();
    
            //  Determine the name type
            typeName = nameNode.getNamePart(numChildren - 1).intern();
    
            //  Extract the package
            if (numChildren > 1) {
                StringBuffer buffer = new StringBuffer(nameNode.getNamePart(0));
                for (int ndx = 1; ndx < numChildren - 1; ndx++) {
                    buffer.append(".");
                    buffer.append(nameNode.getNamePart(ndx));
                }
                packageName = buffer.toString().intern();
            } else {
                packageName = null;
            }
    
            //  This isn't a primitive value
            primitive = false;
    
            //  This isn't an array (yet)
            arrayCount = 0;
        } else {
            ASTPrimitiveType primitiveType = (ASTPrimitiveType)refNode.jjtGetChild(0);
            //  Remember the type name
            typeName = primitiveType.getName().intern();
    
            //  The package name doesn't apply
            packageName = null;
    
            //  This is a primitive value
            primitive = true;
    
            //  This isn't an array (yet)
            arrayCount = 0;
        }
    }


    /**
     *  Creates a type declaration summary from an ASTPrimitiveType object.
     *
     *@param  parentSummary  the parent summary
     *@param  primitiveType  the ASTPrimitiveType object
     */
    public TypeDeclSummary(Summary parentSummary, ASTPrimitiveType primitiveType) {
        //  Initialize the parent class
        super(parentSummary);

        //  Remember the type name
        typeName = primitiveType.getName().intern();

        //  The package name doesn't apply
        packageName = null;

        //  This is a primitive value
        primitive = true;

        //  This isn't an array (yet)
        arrayCount = 0;
    }


    /**
     *  Creates a type declaration summary from an ASTName object.
     *
     *@param  parentSummary  the parent summary
     *@param  initPackage    the package name
     *@param  initType       the type name
     */
    public TypeDeclSummary(Summary parentSummary, String initPackage, String initType) {
        super(parentSummary);

        typeName = initType;
        packageName = initPackage;
        primitive = false;
        arrayCount = 0;
    }


    /**
     *  Set the array count
     *
     *@param  count  the number of "[]" pairs
     */
    public void setArrayCount(int count) {
        if (count >= 0) {
            arrayCount = count;
        }
    }


    /**
     *  Return the number of "[]" pairs
     *
     *@return    the array count
     */
    public int getArrayCount() {
        return arrayCount;
    }


    /**
     *  Is this an array?
     *
     *@return    true if this is an array
     */
    public boolean isArray() {
        return (arrayCount > 0);
    }


    /**
     *  Get the package name
     *
     *@return    a string containing the name of the package
     */
    public String getPackage() {
        return packageName;
    }


    /**
     *  Get the name of the type
     *
     *@return    a string containing the name of the type
     */
    public String getType() {
        return typeName;
    }


    /**
     *  Check if this is a primitive node
     *
     *@return    true if this is a primitive value
     */
    public boolean isPrimitive() {
        return primitive;
    }


    /**
     *  Get long name
     *
     *@return    the long version of the name (type + package)
     */
    public String getLongName() {
        if (packageName == null) {
            return typeName;
        } else {
            return packageName + "." + typeName;
        }
    }


    /**
     *  Convert this into a string
     *
     *@return    a string representation of the type
     */
    public String toString() {
        //  Add the package and type names
        if (!isArray()) {
            return getLongName();
        }

        //  Start with the long name
        StringBuffer buffer = new StringBuffer(getLongName());

        //  Append the array counts
        for (int ndx = 0; ndx < arrayCount; ndx++) {
            buffer.append("[]");
        }

        //  Return the result
        return buffer.toString();
    }


    /**
     *  Provide method to visit a node
     *
     *@param  visitor  the visitor
     *@param  data     the data for the visit
     *@return          some new data
     */
    public Object accept(SummaryVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }


    /**
     *  Check to see if it is equal
     *
     *@param  other  the other item
     *@return        true if they are equal
     */
    public boolean equals(Object other) {
        if (other instanceof TypeDeclSummary) {
            TypeDeclSummary tds = (TypeDeclSummary) other;

            boolean sameType = ((typeName == null) && (tds.typeName == null)) ||
                    ((typeName != null) && typeName.equals(tds.typeName));

            boolean samePackage = ((packageName == null) && (tds.packageName == null)) ||
                    ((packageName != null) && packageName.equals(tds.packageName));

            boolean samePrimitive = (primitive == tds.primitive);

            boolean sameArray = (arrayCount == tds.arrayCount);

            return sameType && samePackage && samePrimitive && sameArray;
        }
        return super.equals(other);
    }


    /**
     *  Gets the same attribute of the TypeDeclSummary object
     *
     *@param  other  Description of the Parameter
     *@return        The same value
     */
    public boolean isSame(TypeDeclSummary other) {
        if (primitive) {
            if (!other.primitive) {
                return false;
            }

            return typeName.equals(other.typeName);
        }

        TypeSummary type1 = GetTypeSummary.query(this);
        TypeSummary type2 = GetTypeSummary.query(other);

        return (type1 == type2);
    }


    /**
     *  Factory method. Creates a type decl summary object from the type node.
     *
     *@param  parentSummary  the parent summary
     *@param  typeNode       the AST node containing the type
     *@return                the new node
     */
    public static TypeDeclSummary getTypeDeclSummary(Summary parentSummary, ASTType typeNode) {
        //  Local Variables
        TypeDeclSummary result;
        Node typeChild = typeNode.jjtGetChild(0);

        if (typeChild instanceof ASTPrimitiveType) {
            result = new TypeDeclSummary(parentSummary, (ASTPrimitiveType) typeChild);
        } else {
            ASTReferenceType reference = (ASTReferenceType) typeChild;
            result = new TypeDeclSummary(parentSummary, reference);
            result.setArrayCount(reference.getArrayCount());
        }

        return result;
    }


    /**
     *  Factory method. Creates a type decl summary object from the type node.
     *
     *@param  parentSummary  the parent summary
     *@param  typeNode       the AST node containing the type
     *@return                the new node
     */
    public static TypeDeclSummary getTypeDeclSummary(Summary parentSummary, ASTReferenceType typeNode) {
        //  Local Variables
        TypeDeclSummary result = new TypeDeclSummary(parentSummary, typeNode);
        result.setArrayCount(typeNode.getArrayCount());

        return result;
    }


    /**
     *  Factory method. Creates a type decl summary object from the type node.
     *
     *@param  parentSummary  the parent summary
     *@param  typeNode       the AST node containing the result type
     *@return                the new node
     */
    public static TypeDeclSummary getTypeDeclSummary(Summary parentSummary, ASTResultType typeNode) {
        if (typeNode.hasAnyChildren()) {
            return getTypeDeclSummary(parentSummary, (ASTType) typeNode.jjtGetChild(0));
        } else {
            return new TypeDeclSummary(parentSummary);
        }
    }


    /**
     *  Gets the name attribute of the TypeDeclSummary object
     *
     *@return    The name value
     */
    public String getName() {
        return toString();
    }
}
