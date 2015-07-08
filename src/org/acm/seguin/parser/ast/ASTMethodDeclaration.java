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
import org.acm.seguin.pretty.ai.MethodAnalyzer;
import org.acm.seguin.pretty.ForceJavadocComments;
import org.acm.seguin.pretty.ai.RequiredTags;
import org.acm.seguin.parser.JavaParserVisitor;
import org.acm.seguin.parser.JavaParser;
import org.acm.seguin.util.FileSettings;
import java.text.MessageFormat;

/**
 *  Holds a method declaration in a class
 *
 *@author     Chris Seguin
 *@author     Mike Atkinson
 *@created    October 13, 1999
 */
public class ASTMethodDeclaration extends AccessNode implements JavaDocable {
	// Instance Variables
	private JavaDocableImpl jdi = null;


	/**
	 *  Constructor for the ASTMethodDeclaration object
	 *
	 *@param  id  Description of Parameter
	 */
	public ASTMethodDeclaration(int id) {
		super(id);
	}


	/**
	 *  Constructor for the ASTMethodDeclaration object
	 *
	 *@param  p   Description of Parameter
	 *@param  id  Description of Parameter
	 */
	public ASTMethodDeclaration(JavaParser p, int id) {
		super(p, id);
	}


	/**
	 *  Checks to see if it was printed
	 *
	 *@return    true if it still needs to be printed
	 */
	public boolean isRequired() {
                if (jdi==null) {
                    jdi = new JavaDocableImpl();
                }
		return jdi.isRequired() && isRequired("method");
	}


	/**
	 *  Allows you to add a java doc component
	 *
	 *@param  component  the component that can be added
	 */
	public void addJavaDocComponent(JavaDocComponent component) {
                if (jdi==null) {
                    jdi = new JavaDocableImpl();
                }
		jdi.addJavaDocComponent(component);
	}


	/**
	 *  Prints all the java doc components
	 *
	 *@param  printData  the print data
	 */
	public void printJavaDocComponents(PrintData printData) {
                if (jdi==null) {
                    jdi = new JavaDocableImpl();
                }
		FileSettings bundle = FileSettings.getRefactoryPrettySettings(); // getSettings("Refactory", "pretty");
		jdi.printJavaDocComponents(printData, bundle.getString("method.tags"));
	}


	/**
	 *  Makes sure all the java doc components are present. For methods and
	 *  constructors we need to do more work - checking parameters, return types,
	 *  and exceptions.
	 */
	public void finish() {
		finish("");
	}


	/**
	 *  Makes sure all the java doc components are present. For methods and
	 *  constructors we need to do more work - checking parameters, return types,
	 *  and exceptions.
	 *
	 *@param  className  Description of Parameter
	 */
	public void finish(String className) {
                if (jdi==null) {
                    jdi = new JavaDocableImpl();
                }
		MethodAnalyzer ai = new MethodAnalyzer(this, jdi);
		ai.finish(className);

		//  Require the other tags
		FileSettings bundle = FileSettings.getRefactoryPrettySettings(); // getSettings("Refactory", "pretty");
                int child =0;
                if (jjtGetFirstChild() instanceof ASTAttribute) {
                    child++; // skip possible attributes
                }
                if (jjtGetChild(child) instanceof ASTTypeParameters) {
                    child++; // skip possible type parameters
                }
                ASTMethodDeclarator method = (ASTMethodDeclarator) jjtGetChild(child+1);
                RequiredTags.getTagger().addTags(bundle, "method", method.getName(), jdi);
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
