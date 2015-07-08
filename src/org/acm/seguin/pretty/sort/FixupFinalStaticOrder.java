/*
 *  Author:  Mike Atkinson
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.pretty.sort;

import org.acm.seguin.parser.ast.ASTInterfaceMemberDeclaration;
import org.acm.seguin.parser.ast.ASTName;
import org.acm.seguin.parser.ast.ASTLiteral;
import org.acm.seguin.parser.ast.ASTClassBodyDeclaration;
import org.acm.seguin.parser.ast.ASTFieldDeclaration;
import org.acm.seguin.parser.ast.ASTVariableDeclarator;
import org.acm.seguin.parser.ast.ASTVariableDeclaratorId;
import org.acm.seguin.parser.ast.SimpleNode;
import org.acm.seguin.parser.Node;
import org.acm.seguin.pretty.ModifierHolder;


/**
 *  Orders the items in a class according to dependencies for final static constants.
 *
 *@author     Mike Atkinson
 *@created    Jun 19, 2003
 *@since      JRefactory 2.7.00
 */
public class FixupFinalStaticOrder extends Ordering {

	/**
	 *  Constructor for the StaticOrder object <P>
	 *
	 *  The string should either be "instance", "static", or "class". "instance"
	 *  means that instance variables should go first. Either of the other two
	 *  ordering strings indicate that the class variables or methods should go
	 *  first.
	 *
	 *@param  ordering  A user specified string that describes the order.
	 */
	public FixupFinalStaticOrder() {
	}


	/**
	 *  Description of the Method
	 *
	 *@param  obj1  Description of Parameter
	 *@param  obj2  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public int compare(Object obj1, Object obj2)
	{
		boolean obj1IsStatic = false;
		boolean obj1IsFinal  = false;
		boolean obj2IsStatic = false;
		boolean obj2IsFinal  = false;

                // only process obj1 if it is a final static Field Declaration.
		Object data = ((SimpleNode) obj1).jjtGetChild(0);
		if (data instanceof ASTClassBodyDeclaration) {
			data = ((ASTClassBodyDeclaration) data).jjtGetChild(0);
		} else if (data instanceof ASTInterfaceMemberDeclaration) {
			data = ((ASTInterfaceMemberDeclaration) data).jjtGetChild(0);
		}

		//  Now that we have data, determine the type of data
		if (data instanceof ASTFieldDeclaration) {
			obj1IsStatic = ((ASTFieldDeclaration) data).isStatic();
			obj1IsFinal = getFinalCode(((ASTFieldDeclaration) data).getModifiers());
		} else {
			return 0;
		}
                
                if (!obj1IsStatic || !obj1IsFinal) {
                    return 0;
                }
                ASTFieldDeclaration field1 = (ASTFieldDeclaration) data;
                ASTVariableDeclarator declar1 = (ASTVariableDeclarator) field1.jjtGetChild(1);
                String name1 = ((ASTVariableDeclaratorId) declar1.jjtGetChild(0)).getName();

                
                // only process obj2 if it is a final static Field Declaration.
		data = ((SimpleNode) obj2).jjtGetChild(0);
		if (data instanceof ASTClassBodyDeclaration) {
			data = ((ASTClassBodyDeclaration) data).jjtGetChild(0);
		} else if (data instanceof ASTInterfaceMemberDeclaration) {
			data = ((ASTInterfaceMemberDeclaration) data).jjtGetChild(0);
		}


		//  Now that we have data, determine the type of data
		if (data instanceof ASTFieldDeclaration) {
			obj2IsStatic = ((ASTFieldDeclaration) data).isStatic();
			obj2IsFinal = getFinalCode(((ASTFieldDeclaration) data).getModifiers());
		} else {
			return 0;
		}
                
                if (!obj2IsStatic || !obj2IsFinal) {
                    return 0;
                }

                ASTFieldDeclaration field2 = (ASTFieldDeclaration) data;
                ASTVariableDeclarator declar2 = (ASTVariableDeclarator) field2.jjtGetChild(1);
                String name2 = ((ASTVariableDeclaratorId) declar2.jjtGetChild(0)).getName();
                
                // search for name1 in elements of obj2
                for (int i=1; i<declar2.jjtGetNumChildren(); i++) {
                    Node node = declar2.jjtGetChild(i);
                    if (contains(node, name1)) {
                        return -1;
                    }
                }

                // search for name2 in elements of obj1
                for (int i=1; i<declar1.jjtGetNumChildren(); i++) {
                    Node node = declar1.jjtGetChild(i);
                    if (contains(node, name2)) {
                        return 1;
                    }
                }

		return 0;
	}

        private boolean contains(Node node, String name) {
            if (node instanceof ASTLiteral) {
                if (name.equals(((ASTLiteral)node).getName())) {
                    return true;
                }
            } else if (node instanceof ASTName) {
                if (name.equals(((ASTName)node).getName())) {
                    return true;
                }
            }
            for (int i=0; i<node.jjtGetNumChildren(); i++) {
                Node childNode = node.jjtGetChild(i);
                if (contains(childNode, name)) {
                    return true;
                }
            }

                return false;
        }
	/**
	 *  Return the index of the item in the order array
	 *
	 *@param  object  the object we are checking
	 *@return         the objects index if it is found or 7 if it is not
	 */
	protected int getIndex(Object object) {
            return 0;
	}
        
	/**
	 *  Gets the Protection attribute of the ProtectionOrder object
	 *
	 *@param  mods  Description of Parameter
	 *@return       The Protection value
	 */
	private boolean getFinalCode(ModifierHolder mods) {
		if (mods.isFinal()) {
			return true;
		}
		else {
			return false;
		}
	}
        
}
