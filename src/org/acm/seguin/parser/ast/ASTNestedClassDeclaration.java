/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.parser.ast;

import org.acm.seguin.pretty.JavaDocComponent;
import org.acm.seguin.pretty.JavaDocable;
import org.acm.seguin.pretty.JavaDocableImpl;
import org.acm.seguin.pretty.PrintData;
import org.acm.seguin.parser.JavaParserVisitor;
import org.acm.seguin.parser.JavaParser;
import org.acm.seguin.pretty.ForceJavadocComments;
import org.acm.seguin.pretty.ai.RequiredTags;
import org.acm.seguin.util.FileSettings;
import org.acm.seguin.pretty.DescriptionPadder;

/**
 *  Holds a nested class declaration
 *
 *@author     Chris Seguin
 *@created    October 13, 1999
 */
public class ASTNestedClassDeclaration extends AccessNode implements JavaDocable {
	// Instance Variables
	private JavaDocableImpl jdi;


	/**
	 *  Constructor for the ASTNestedClassDeclaration object
	 *
	 *@param  id  Description of Parameter
	 */
	public ASTNestedClassDeclaration(int id) {
		super(id);
		jdi = new JavaDocableImpl();
	}


	/**
	 *  Constructor for the ASTNestedClassDeclaration object
	 *
	 *@param  p   Description of Parameter
	 *@param  id  Description of Parameter
	 */
	public ASTNestedClassDeclaration(JavaParser p, int id) {
		super(p, id);
		jdi = new JavaDocableImpl();
	}


	/**
	 *  Checks to see if it was printed
	 *
	 *@return    true if it still needs to be printed
	 */
	public boolean isRequired() {
		return jdi.isRequired() && isRequired("class");
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
		FileSettings bundle = FileSettings.getRefactoryPrettySettings(); // getSettings("Refactory", "pretty");
		jdi.printJavaDocComponents(printData, bundle.getString("class.tags"));
	}


	/**
	 *  Makes sure all the java doc components are present
	 */
	public void finish() {
		//  Get the resource bundle
		FileSettings bundle = FileSettings.getRefactoryPrettySettings(); // getSettings("Refactory", "pretty");

		//  Description of the class
		jdi.require("", DescriptionPadder.find(bundle, "class.descr"));

		//  Require the other tags
		ASTUnmodifiedClassDeclaration child = (ASTUnmodifiedClassDeclaration) jjtGetFirstChild();
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
