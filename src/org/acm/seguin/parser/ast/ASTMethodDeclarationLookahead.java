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
 *  Checks to see if the next item is a method declaration
 *
 *@author     Chris Seguin
 *@created    October 13, 1999
 */
public class ASTMethodDeclarationLookahead extends SimpleNode {
	/**
	 *  Constructor for the ASTMethodDeclarationLookahead object
	 *
	 *@param  id  Description of Parameter
	 */
	public ASTMethodDeclarationLookahead(int id) {
		super(id);
	}


	/**
	 *  Constructor for the ASTMethodDeclarationLookahead object
	 *
	 *@param  p   Description of Parameter
	 *@param  id  Description of Parameter
	 */
	public ASTMethodDeclarationLookahead(JavaParser p, int id) {
		super(p, id);
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
