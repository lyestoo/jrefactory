/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.parser.ast;

import org.acm.seguin.parser.JavaParserVisitor;
import org.acm.seguin.parser.JavaParser;

/**
 *  Contains a constant or literal value in the code.
 *
 *@author     Chris Seguin
 *@created    October 13, 1999
 */
public class ASTLiteral extends SimpleNode {
	//  Instance Variables
	private String name = "";


	/**
	 *  Constructor for the ASTLiteral object
	 *
	 *@param  id  Description of Parameter
	 */
	public ASTLiteral(int id) {
		super(id);
	}


	/**
	 *  Constructor for the ASTLiteral object
	 *
	 *@param  p   Description of Parameter
	 *@param  id  Description of Parameter
	 */
	public ASTLiteral(JavaParser p, int id) {
		super(p, id);
	}


	/**
	 *  Set the object's name
	 *
	 *@param  newName  the new name
	 */
	public void setName(String newName) {
        //System.out.println("Literal.setName("+newName+")");
		name = newName.intern();
	}


	/**
	 *  Get the object's name
	 *
	 *@return    the name
	 */
	public String getName() {
        //System.out.println("Literal.getName() -> "+name);
		return name;
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
