/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.parser.ast;

import java.text.DateFormat;
import java.util.Date;
import org.acm.seguin.pretty.ModifierHolder;
import org.acm.seguin.pretty.JavaDocComponent;
import org.acm.seguin.pretty.JavaDocable;
import org.acm.seguin.pretty.JavaDocableImpl;
import org.acm.seguin.pretty.PrintData;
import org.acm.seguin.pretty.ai.RequiredTags;
import org.acm.seguin.parser.JavaParserVisitor;
import org.acm.seguin.parser.JavaParser;
import org.acm.seguin.pretty.ForceJavadocComments;
import org.acm.seguin.util.FileSettings;
import org.acm.seguin.util.MissingSettingsException;
import org.acm.seguin.pretty.DescriptionPadder;

/**
 *  Holds a class declaration.  Contains the list of modifiers
 *  for the class and the javadoc comments.
 *
 *@author     Chris Seguin
 *@created    October 13, 1999
 */
public class ASTClassDeclaration extends SimpleNode implements JavaDocable {
	// Instance Variables
	private ModifierHolder modifiers;
	private JavaDocableImpl jdi;


	/**
	 *  Constructor for the ASTClassDeclaration object
	 *
	 *@param  id  Description of Parameter
	 */
	public ASTClassDeclaration(int id) {
		super(id);
		modifiers = new ModifierHolder();
		jdi = new JavaDocableImpl();
	}


	/**
	 *  Constructor for the ASTClassDeclaration object
	 *
	 *@param  p   Description of Parameter
	 *@param  id  Description of Parameter
	 */
	public ASTClassDeclaration(JavaParser p, int id) {
		super(p, id);
		modifiers = new ModifierHolder();
		jdi = new JavaDocableImpl();
	}


	/**
	 *  Determine if the object is abstract
	 *
	 *@return    true if this stores an ABSTRACT flag
	 */
	public boolean isAbstract() {
		return modifiers.isAbstract();
	}


	/**
	 *  Determine if the object is explicit
	 *
	 *@return    true if this stores an EXPLICIT flag
	 */
	public boolean isExplicit() {
		return modifiers.isExplicit();
	}


	/**
	 *  Determine if the object is final
	 *
	 *@return    true if this stores an FINAL flag
	 */
	public boolean isFinal() {
		return modifiers.isFinal();
	}


	/**
	 *  Determine if the object is interface
	 *
	 *@return    true if this stores an INTERFACE flag
	 */
	public boolean isInterface() {
		return modifiers.isInterface();
	}


	/**
	 *  Determine if the object is native
	 *
	 *@return    true if this stores an NATIVE flag
	 */
	public boolean isNative() {
		return modifiers.isNative();
	}


	/**
	 *  Determine if the object is private
	 *
	 *@return    true if this stores an PRIVATE flag
	 */
	public boolean isPrivate() {
		return modifiers.isPrivate();
	}


	/**
	 *  Determine if the object is protected
	 *
	 *@return    true if this stores an PROTECTED flag
	 */
	public boolean isProtected() {
		return modifiers.isProtected();
	}


	/**
	 *  Determine if the object is public
	 *
	 *@return    true if this stores an PUBLIC flag
	 */
	public boolean isPublic() {
		return modifiers.isPublic();
	}


	/**
	 *  Determine if the object is static
	 *
	 *@return    true if this stores an static flag
	 */
	public boolean isStatic() {
		return modifiers.isStatic();
	}


	/**
	 *  Determine if the object is strict
	 *
	 *@return    true if this stores an STRICT flag
	 */
	public boolean isStrict() {
		return modifiers.isStrict();
	}


	/**
	 *  Determine if the object is synchronized
	 *
	 *@return    true if this stores an SYNCHRONIZED flag
	 */
	public boolean isSynchronized() {
		return modifiers.isSynchronized();
	}


	/**
	 *  Determine if the object is transient
	 *
	 *@return    true if this stores an TRANSIENT flag
	 */
	public boolean isTransient() {
		return modifiers.isTransient();
	}


	/**
	 *  Determine if the object is volatile
	 *
	 *@return    true if this stores an VOLATILE flag
	 */
	public boolean isVolatile() {
		return modifiers.isVolatile();
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
	 *  Returns the modifier holder
	 *
	 *@return    the holder
	 */
	public ModifierHolder getModifiers() {
		return modifiers;
	}


	/**
	 *  Checks to see if it was printed
	 *
	 *@return    true if it still needs to be printed
	 */
	public boolean isRequired() {
		ForceJavadocComments fjc = new ForceJavadocComments();

		return jdi.isRequired() &&
				fjc.isJavaDocRequired("class", modifiers);
	}


	/**
	 *  Adds a modifier to a class
	 *
	 *@param  modifier  the next modifier
	 */
	public void addModifier(String modifier) {
		modifiers.add(modifier);
	}


	/**
	 *  Convert this object to a string
	 *
	 *@return    a string representing this object
	 */
	public String toString() {
		return super.toString() + " [" + getModifiersString(PrintData.ALPHABETICAL_ORDER) + "]";
	}



	/**
	 *  Allows you to add a java doc component
	 *
	 *@param  component  the component that can be added
	 */
	public void addJavaDocComponent(JavaDocComponent component) {
		jdi.addJavaDocComponent(component);
	}


	/**
	 *  Prints all the java doc components
	 *
	 *@param  printData  the print data
	 */
	public void printJavaDocComponents(PrintData printData) {
		FileSettings bundle = FileSettings.getSettings("Refactory", "pretty");
		jdi.printJavaDocComponents(printData, bundle.getString("class.tags"));
	}


	/**
	 *  Makes sure all the java doc components are present. For classes and
	 *  interfaces, this means a date and an author.
	 */
	public void finish() {
		//  Get the resource bundle
		FileSettings bundle = FileSettings.getSettings("Refactory", "pretty");

		//  Description of the class
		jdi.require("", DescriptionPadder.find(bundle, "class.descr"));

		//  Require the other tags
		ASTUnmodifiedClassDeclaration child = (ASTUnmodifiedClassDeclaration) jjtGetChild(0);
		RequiredTags.getTagger().addTags(bundle, "class", child.getName(), jdi);
	}


	/**
	 *  Accept the visitor.
	 *
	 *@param  visitor  Description of Parameter
	 *@param  data     Description of Parameter
	 *@return          Description of the Returned Value
	 */
	public Object jjtAccept(JavaParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
