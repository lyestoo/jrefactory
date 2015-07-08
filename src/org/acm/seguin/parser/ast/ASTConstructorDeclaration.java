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
package org.acm.seguin.parser.ast;

import org.acm.seguin.pretty.ModifierHolder;
import org.acm.seguin.pretty.JavaDocComponent;
import org.acm.seguin.pretty.JavadocTags;
import org.acm.seguin.pretty.JavaDocable;
import org.acm.seguin.pretty.JavaDocableImpl;
import org.acm.seguin.pretty.PrintData;
import org.acm.seguin.pretty.ForceJavadocComments;
import org.acm.seguin.pretty.ai.RequiredTags;
import org.acm.seguin.parser.JavaParserVisitor;
import org.acm.seguin.parser.JavaParser;
import org.acm.seguin.util.FileSettings;
import java.text.MessageFormat;

/**
 *  Description of the Class
 *
 *@author     Chris Seguin
 *@author     Mike Atkinson
 *@created    October 13, 1999
 *@since      2.6.34
 */
public class ASTConstructorDeclaration extends SimpleNode implements JavaDocable {
    private JavaDocableImpl jdi;
    private ModifierHolder modifiers;
    //  Instance Variables
    private String name;


    /**
     *  Constructor for the ASTConstructorDeclaration object
     *
     *@param  id  Description of Parameter
     *@since      2.6.34
     */
    public ASTConstructorDeclaration(int id) {
        super(id);
        modifiers = new ModifierHolder();
        jdi = new JavaDocableImpl();
    }


    /**
     *  Constructor for the ASTConstructorDeclaration object
     *
     *@param  p   Description of Parameter
     *@param  id  Description of Parameter
     *@since      2.6.34
     */
    public ASTConstructorDeclaration(JavaParser p, int id) {
        super(p, id);
        modifiers = new ModifierHolder();
        jdi = new JavaDocableImpl();
    }


    /**
     *  Set the object's name
     *
     *@param  newName  the new name
     *@since           2.6.34
     */
    public void setName(String newName) {
        name = newName.intern();
    }


    /**
     *  Returns the modifier holder
     *
     *@return    the holder
     *@since     2.6.34
     */
    public ModifierHolder getModifiers() {
        return modifiers;
    }


	/**
	 *  Returns a string containing all the modifiers
	 *
	 *@param code the code used to determine the order of the modifiers
	 *@return    the string representationof the order
	 */
	public String getModifiersString(int code) {
		if (code == PrintData.ALPHABETICAL_ORDER)
			return modifiers.toString();
		else
			return modifiers.toStandardOrderString();
	}


    /**
     *  Get the object's name
     *
     *@return    the name
     *@since     2.6.34
     */
    public String getName() {
        return name;
    }


    /**
     *  Determine if the object is abstract
     *
     *@return    true if this stores an ABSTRACT flag
     *@since     2.6.34
     */
    public boolean isAbstract() {
        return modifiers.isAbstract();
    }


    /**
     *  Determine if the object is explicit
     *
     *@return    true if this stores an EXPLICIT flag
     *@since     2.6.34
     */
    public boolean isExplicit() {
        return modifiers.isExplicit();
    }


    /**
     *  Determine if the object is final
     *
     *@return    true if this stores an FINAL flag
     *@since     2.6.34
     */
    public boolean isFinal() {
        return modifiers.isFinal();
    }


    /**
     *  Determine if the object is interface
     *
     *@return    true if this stores an INTERFACE flag
     *@since     2.6.34
     */
    public boolean isInterface() {
        return modifiers.isInterface();
    }


    /**
     *  Determine if the object is native
     *
     *@return    true if this stores an NATIVE flag
     *@since     2.6.34
     */
    public boolean isNative() {
        return modifiers.isNative();
    }


    /**
     *  Determine if the object is private
     *
     *@return    true if this stores an PRIVATE flag
     *@since     2.6.34
     */
    public boolean isPrivate() {
        return modifiers.isPrivate();
    }


    /**
     *  Determine if the object is protected
     *
     *@return    true if this stores an PROTECTED flag
     *@since     2.6.34
     */
    public boolean isProtected() {
        return modifiers.isProtected();
    }


    /**
     *  Determine if the object is public
     *
     *@return    true if this stores an PUBLIC flag
     *@since     2.6.34
     */
    public boolean isPublic() {
        return modifiers.isPublic();
    }


    /**
     *  Checks to see if it was printed
     *
     *@return    true if it still needs to be printed
     *@since     2.6.34
     */
    public boolean isRequired() {
        //  Check if it is required
        ForceJavadocComments fjc = new ForceJavadocComments();
        return jdi.isRequired() &&
                fjc.isJavaDocRequired("method", modifiers);
    }


    /**
     *  Determine if the object is static
     *
     *@return    true if this stores an static flag
     *@since     2.6.34
     */
    public boolean isStatic() {
        return modifiers.isStatic();
    }


    /**
     *  Determine if the object is strict
     *
     *@return    true if this stores an STRICT flag
     *@since     2.6.34
     */
    public boolean isStrict() {
        return modifiers.isStrict();
    }


    /**
     *  Determine if the object is synchronized
     *
     *@return    true if this stores an SYNCHRONIZED flag
     *@since     2.6.34
     */
    public boolean isSynchronized() {
        return modifiers.isSynchronized();
    }


    /**
     *  Determine if the object is transient
     *
     *@return    true if this stores an TRANSIENT flag
     *@since     2.6.34
     */
    public boolean isTransient() {
        return modifiers.isTransient();
    }


    /**
     *  Determine if the object is volatile
     *
     *@return    true if this stores an VOLATILE flag
     *@since     2.6.34
     */
    public boolean isVolatile() {
        return modifiers.isVolatile();
    }



    /**
     *  Allows you to add a java doc component
     *
     *@param  component  the component that can be added
     *@since             2.6.34
     */
    public void addJavaDocComponent(JavaDocComponent component) {
        jdi.addJavaDocComponent(component);
    }


    /**
     *  Adds a modifier to a class
     *
     *@param  modifier  the next modifier
     *@since            2.6.34
     */
    public void addModifier(String modifier) {
        modifiers.add(modifier);
    }


    /**
     *  Makes sure all the java doc components are present. For methods and
     *  constructors we need to do more work - checking parameters, return
     *  types, and exceptions.
     *
     *@since    2.6.34
     */
    public void finish() {
        //  Local Variables
        int ndx;
        int childCount;

        //  Get the tags
        JavadocTags tags = JavadocTags.get();

        //  Description of the constructor
        Object[] nameArray = new Object[1];
        nameArray[0] = getName();
        String msg = MessageFormat.format(tags.getConstructorDescr(), nameArray);
        jdi.require("", msg);

        //  Check for parameters
        int child = 0;
        if (jjtGetChild(0) instanceof ASTAttribute) {
            child++;  // skip possible attribute
        }
        ASTFormalParameters params = (ASTFormalParameters) jjtGetChild(child);
        childCount = params.jjtGetNumChildren();
        String[] constructorParams = new String[childCount];
        for (ndx = 0; ndx < childCount; ndx++) {
            ASTFormalParameter nextParam = (ASTFormalParameter) params.jjtGetChild(ndx);
            ASTVariableDeclaratorId id = (ASTVariableDeclaratorId) nextParam.jjtGetChild(1);
            jdi.require("@param", id.getName(), tags.getParamDescr());
            constructorParams[ndx] = id.getName();
        }
        // sort @param tags into order of constructor parameters
        jdi.sort("@param", constructorParams);

        //  Check for exceptions
        if ((jjtGetNumChildren() > child+1) && (jjtGetChild(child+1) instanceof ASTNameList)) {
            ASTNameList exceptions = (ASTNameList) jjtGetChild(child+1);
            childCount = exceptions.jjtGetNumChildren();
            for (ndx = 0; ndx < childCount; ndx++) {
                ASTName name = (ASTName) exceptions.jjtGetChild(ndx);
                jdi.require(tags.getExceptionTag(), name.getName(), tags.getExceptionDescr());
            }
        }

        //  Require the other tags
        FileSettings bundle = FileSettings.getSettings("Refactory", "pretty");
        RequiredTags.getTagger().addTags(bundle, "method", getName(), jdi);
    }


    /**
     *  Accept the visitor.
     *
     *@param  visitor  Description of Parameter
     *@param  data     Description of Parameter
     *@return          Description of the Returned Value
     *@since           2.6.34
     */
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }


    /**
     *  Prints all the java doc components
     *
     *@param  printData  the print data
     *@since             2.6.34
     */
    public void printJavaDocComponents(PrintData printData) {
        FileSettings bundle = FileSettings.getSettings("Refactory", "pretty");
        jdi.printJavaDocComponents(printData, bundle.getString("method.tags"));
    }


    /**
     *  Convert this object to a string
     *
     *@return    a string representing this object
     *@since     2.6.34
     */
    public String toString() {
        return super.toString() + " [" + getModifiersString(PrintData.ALPHABETICAL_ORDER) + " " +
                getName() + "]";
    }
}
//  EOF
