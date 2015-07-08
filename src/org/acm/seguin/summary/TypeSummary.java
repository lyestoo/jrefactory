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

import java.util.Iterator;
import java.util.LinkedList;
import org.acm.seguin.parser.ast.ModifierHolder;
import org.acm.seguin.parser.ast.SimpleNode;

/**
 *  Stores the summary of a type (either class or interface)
 *
 *@author     Chris Seguin
 *@author     Mike Atkinson
 *@created    June 6, 1999
 */
public class TypeSummary extends Summary {
    //  Instance Variables
    private String name;
    private boolean bInterface;
    private TypeDeclSummary parentClass;
    private LinkedList interfaceList;
    private LinkedList methodList;
    private LinkedList fieldList;
    private LinkedList typeList;


    /**
     *  Creates a TypeSummary object
     *
     *@param  parentSummary  the parent summary
     *@param  typeDecl       the type declaration
     */
    public TypeSummary(Summary parentSummary, SimpleNode typeDecl) {
        //  Invoke parent class constructor
        super(parentSummary);

        //  Initialize the variables
        name = "";
        bInterface = false;
        methodList = null;
        fieldList = null;
        typeList = null;
        interfaceList = null;
        parentClass = null;
    }


    /**
     *  Set the name of this object
     *
     *@param  newName  the name
     */
    public void setName(String newName) {
        name = newName;
    }


    /**
     *  Check to see if this is an interface or a class
     *
     *@return    true if this is an interface
     */
    public boolean isInterface() {
        return bInterface;
    }


    /**
     *  Get the name of this object
     *
     *@return    the name
     */
    public String getName() {
        return name;
    }


    /**
     *  Return the list of methods
     *
     *@return    an iterator of methods
     */
    public Iterator getMethods() {
        if (methodList == null) {
            return null;
        }

        return methodList.iterator();
    }


    /**
     *  Get the total number of methods - class and instance - that are
     *  associated with this object.
     *
     *@return    the number of methods
     */
    public int getMethodCount() {
        if (methodList == null) {
            return 0;
        }

        return methodList.size();
    }


    /**
     *  Return a list of fields
     *
     *@return    an iterator of fields
     */
    public Iterator getFields() {
        if (fieldList == null) {
            return null;
        }

        return fieldList.iterator();
    }


    /**
     *  Get the total number of fields - class and instance - that are
     *  associated with this object.
     *
     *@return    the number of fields
     */
    public int getFieldCount() {
        if (fieldList == null) {
            return 0;
        }
        return fieldList.size();
    }


    /**
     *  Return the iterator over the types
     *
     *@return    an iterator full of types
     */
    public Iterator getTypes() {
        if (typeList == null) {
            return null;
        }

        return typeList.iterator();
    }


    /**
     *  Get the total number of nested class and interface declarations that are
     *  associated with this object.
     *
     *@return    the number of nested classes and interfaces
     */
    public int getTypeCount() {
        if (typeList == null) {
            return 0;
        }
        return typeList.size();
    }


    /**
     *  Return a list of the types that this class/interface implements
     *
     *@return    an iterator of the types
     */
    public Iterator getImplementedInterfaces() {
        if (interfaceList == null) {
            return null;
        }

        return interfaceList.iterator();
    }
/*
    public boolean implementsInterface(String intf) {
       if (interfaceList==null) {
          return false;
       }
       Iterator i = interfaceList.iterator();
       while (i.hasNext()) {
          TypeDeclSummary x = (TypeDeclSummary)i.next();
          
          if (intf.equals(x.name)) {
             return true;
          } else if (x.interfaceList != null) {
             if (x.implementsInterface(intf)) {
                return true;
             }
          }
       }
       if (parentClass != null) {
       }
    }
*/
    /**
     *  Return the parent class
     *
     *@return    the type declaration representing the parent class
     */
    public TypeDeclSummary getParentClass() {
        return parentClass;
    }


    /**
     *  Finds the package summary associated with this type
     *
     *@return    the package summary
     */
    public PackageSummary getPackageSummary() {
        Summary current = getParent();
        while ((current != null) && !(current instanceof PackageSummary)) {
            current = current.getParent();
        }

        return (PackageSummary) current;
    }


    /**
     *  Gets a field by a name
     *
     *@param  name  the name of the field
     *@return       the field summary
     */
    public FieldSummary getField(String name) {
        Iterator iter = getFields();
        if (iter != null) {
            while (iter.hasNext()) {
                FieldSummary next = (FieldSummary) iter.next();
                if (next.getName().equals(name)) {
                    return next;
                }
            }
        }

        return null;
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
     *  Convert this object to a string
     *
     *@return    a string
     */
    public String toString() {
        return getName();
    }



    /**
     *  Add a method summary
     *
     *@param  methodSummary  the method summary
     */
    protected void add(MethodSummary methodSummary) {
        if (methodSummary != null) {
            if (methodList == null) {
                initMethodList();
            }

            methodList.add(methodSummary);
        }
    }


    /**
     *  Add a field summary
     *
     *@param  fieldSummary  the field summary
     */
    protected void add(FieldSummary fieldSummary) {
        if (fieldSummary != null) {
            if (fieldList == null) {
                initFieldList();
            }

            fieldList.add(fieldSummary);
        }
    }


    /**
     *  Add a type summary
     *
     *@param  typeSummary  the type summary
     */
    protected void add(TypeSummary typeSummary) {
        if (typeSummary != null) {
            if (typeList == null) {
                initTypeList();
            }

            typeList.add(typeSummary);
        }
    }


    /**
     *  Add an interface summary
     *
     *@param  typeDeclSummary  the interface summary
     */
    protected void add(TypeDeclSummary typeDeclSummary) {
        if (typeDeclSummary != null) {
            if (interfaceList == null) {
                initInterfaceList();
            }

            interfaceList.add(typeDeclSummary);
        }
    }


    /**
     *  Mark this as an interface or a class
     *
     *@param  way  the way to set the interface variable
     */
    void setInterface(boolean way) {
        bInterface = way;
    }


    /**
     *  Set the parent class
     *
     *@param  tds  the type declaration representing the parent class
     */
    void setParentClass(TypeDeclSummary tds) {
        parentClass = tds;
    }


    /**
     *  Gets the Initializer attribute of the TypeSummary object
     *
     *@param  isStatic  Description of Parameter
     *@return           The Initializer value
     */
    MethodSummary getInitializer(boolean isStatic) {
        Iterator iter = getMethods();
        if (iter != null) {
            while (iter.hasNext()) {
                MethodSummary next = (MethodSummary) iter.next();
                if (next.isInitializer()) {
                    if (next.isStatic() == isStatic) {
                        return next;
                    }
                }
            }
        }

        return createInitializer(isStatic);
    }


    /**
     *  Initialize the interface list
     */
    private void initInterfaceList() {
        interfaceList = new LinkedList();
    }


    /**
     *  Initialize the method list
     */
    private void initMethodList() {
        methodList = new LinkedList();
    }


    /**
     *  Initialize the field list
     */
    private void initFieldList() {
        fieldList = new LinkedList();
    }


    /**
     *  Initialize the type list
     */
    private void initTypeList() {
        typeList = new LinkedList();
    }


    /**
     *  Description of the Method
     *
     *@param  isStatic  Description of Parameter
     *@return           Description of the Returned Value
     */
    private MethodSummary createInitializer(boolean isStatic) {
        //  Get the current type summary
        MethodSummary methodSummary = new MethodSummary(this);
        add(methodSummary);

        //  Load the method summary
        //  Remember the modifiers
        methodSummary.setStatic(isStatic);
        methodSummary.setPrivate(true);

        //  Load the method names
        methodSummary.setName("***Initializer***");

        return methodSummary;
    }
}
