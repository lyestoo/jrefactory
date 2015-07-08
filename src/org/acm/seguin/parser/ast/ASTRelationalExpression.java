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
import java.util.Vector;
import java.util.Enumeration;

/**
 *  Contains a set of inequality relationships
 *
 *@author     Chris Seguin
 *@created    October 13, 1999
 */
public class ASTRelationalExpression extends SimpleNode {
	//  Instance Variables
	private Vector names;


	/**
	 *  Constructor for the ASTRelationalExpression object
	 *
	 *@param  id  Description of Parameter
	 */
	public ASTRelationalExpression(int id) {
		super(id);
		names = new Vector();
	}


	/**
	 *  Constructor for the ASTRelationalExpression object
	 *
	 *@param  p   Description of Parameter
	 *@param  id  Description of Parameter
	 */
	public ASTRelationalExpression(JavaParser p, int id) {
		super(p, id);
		names = new Vector();
	}


	/**
	 *  Get the object's names
	 *
	 *@return    the names in an enumeration
	 */
	public Enumeration getNames() {
		return names.elements();
	}


	/**
	 *  Set the object's name
	 *
	 *@param  newName  the new name
	 */
	public void addName(String newName) {
		if (newName != null) {
			names.addElement(newName.intern());
		}
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
