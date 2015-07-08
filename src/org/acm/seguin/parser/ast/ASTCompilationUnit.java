/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.parser.ast;

import java.util.Arrays;
import java.util.Comparator;
import org.acm.seguin.parser.JavaParserVisitor;
import org.acm.seguin.parser.JavaParser;

/**
 *  Top level unit for compilation which is able to sort
 *  the import statements.
 *
 *@author     Chris Seguin
 *@created    October 13, 1999
 */
public class ASTCompilationUnit extends SimpleNode {
	/**
	 *  Constructor for the ASTCompilationUnit object
	 *
	 *@param  id  Description of Parameter
	 */
	public ASTCompilationUnit(int id) {
		super(id);
	}


	/**
	 *  Constructor for the ASTCompilationUnit object
	 *
	 *@param  p   Description of Parameter
	 *@param  id  Description of Parameter
	 */
	public ASTCompilationUnit(JavaParser p, int id) {
		super(p, id);
	}


	/**
	 *  Sorts the arrays
	 *
	 *@param  order  the order of items
	 */
	public void sort(Comparator order) {
		if (children != null) {
			Arrays.sort(children, order);
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
