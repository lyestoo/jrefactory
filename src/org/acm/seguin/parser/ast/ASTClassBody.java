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
import org.acm.seguin.pretty.sort.FixupFinalStaticOrder;

/**
 *  Stores the class body.  This has the ability to sort the
 *  children nodes.  The order depends on the order set in the
 *  pretty.settings file.
 *
 *@author     Chris Seguin
 *@author     Mike Atkinson
 *@created    October 13, 1999
 */
public class ASTClassBody extends SimpleNode {
	/**
	 *  Constructor for the ASTClassBody object
	 *
	 *@param  id  Description of Parameter
	 */
	public ASTClassBody(int id) {
		super(id);
	}


	/**
	 *  Constructor for the ASTClassBody object
	 *
	 *@param  p   Description of Parameter
	 *@param  id  Description of Parameter
	 */
	public ASTClassBody(JavaParser p, int id) {
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
                        // ensure that static final constants are defined in the correct order
			Arrays.sort(children, new FixupFinalStaticOrder());
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
