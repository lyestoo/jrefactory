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
import org.acm.seguin.parser.JavaParser;
import org.acm.seguin.parser.JavaParserVisitor;
import org.acm.seguin.pretty.ForceJavadocComments;
import org.acm.seguin.pretty.JavaDocComponent;
import org.acm.seguin.pretty.JavaDocable;
import org.acm.seguin.pretty.JavaDocableImpl;
import org.acm.seguin.pretty.PrintData;
import org.acm.seguin.pretty.ai.RequiredTags;
import org.acm.seguin.util.FileSettings;
import org.acm.seguin.util.MissingSettingsException;
import org.acm.seguin.pretty.DescriptionPadder;

/**
 *  Holds an interface declaration. Essentially this is the method declaration
 *  inside an interface.
 *
 *@author     Chris Seguin
 *@created    October 13, 1999
 */
public class ASTInterfaceDeclaration extends AccessNode implements JavaDocable {
	// Instance Variables
	private JavaDocableImpl jdi;


	/**
	 *  Constructor for the ASTInterfaceDeclaration object
	 *
	 *@param  id  Description of Parameter
	 */
	public ASTInterfaceDeclaration(int id)
	{
		super(id);
		jdi = new JavaDocableImpl();
	}


	/**
	 *  Constructor for the ASTInterfaceDeclaration object
	 *
	 *@param  p   Description of Parameter
	 *@param  id  Description of Parameter
	 */
	public ASTInterfaceDeclaration(JavaParser p, int id)
	{
		super(p, id);
		jdi = new JavaDocableImpl();
	}


	/**
	 *  Checks to see if it was printed
	 *
	 *@return    true if it still needs to be printed
	 */
	public boolean isRequired()
	{
		return jdi.isRequired() && isRequired("class");
	}


	/**
	 *  Allows you to add a java doc component
	 *
	 *@param  component  the component that can be added
	 */
	public void addJavaDocComponent(JavaDocComponent component)
	{
		jdi.addJavaDocComponent(component);
	}


	/**
	 *  Prints all the java doc components
	 *
	 *@param  printData  the print data
	 */
	public void printJavaDocComponents(PrintData printData)
	{
		FileSettings bundle = FileSettings.getRefactoryPrettySettings(); // getSettings("Refactory", "pretty");
		jdi.printJavaDocComponents(printData, bundle.getString("class.tags"));
	}


	/**
	 *  Makes sure all the java doc components are present. For classes and
	 *  interfaces, this means a date and an author.
	 */
	public void finish()
	{
		//  Get the resource bundle
		FileSettings bundle = FileSettings.getRefactoryPrettySettings(); // getSettings("Refactory", "pretty");

		//  Description of the interface
		jdi.require("", DescriptionPadder.find(bundle, "interface.descr"));

		//  Require the other tags
		ASTUnmodifiedInterfaceDeclaration child = (ASTUnmodifiedInterfaceDeclaration) jjtGetFirstChild();
		RequiredTags.getTagger().addTags(bundle, "class", child.getName(), jdi);
	}


	/**
	 *  Accept the visitor.
	 *
	 *@param  visitor  Description of Parameter
	 *@param  data     Description of Parameter
	 *@return          Description of the Returned Value
	 */
	public Object jjtAccept(JavaParserVisitor visitor, Object data)
	{
		return visitor.visit(this, data);
	}

    public ASTUnmodifiedInterfaceDeclaration getUnmodifedInterfaceDeclaration() {
        return (ASTUnmodifiedInterfaceDeclaration) jjtGetFirstChild();
    }

}
