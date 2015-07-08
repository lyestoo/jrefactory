/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.pretty.sort;

import org.acm.seguin.parser.ast.ASTInterfaceMemberDeclaration;
import org.acm.seguin.parser.ast.ASTClassBodyDeclaration;
import org.acm.seguin.parser.ast.ASTInitializer;
import org.acm.seguin.parser.ast.ASTFieldDeclaration;
import org.acm.seguin.parser.ast.ASTNestedInterfaceDeclaration;
import org.acm.seguin.parser.ast.ASTMethodDeclaration;
import org.acm.seguin.parser.ast.ASTNestedClassDeclaration;
import org.acm.seguin.parser.ast.ASTConstructorDeclaration;
import org.acm.seguin.parser.ast.ASTEnumDeclaration;
import org.acm.seguin.parser.ast.SimpleNode;
import org.acm.seguin.pretty.ModifierHolder;

/**
 *  Sorts fields and method by whether they are marked final
 *
 *@author    Chris Seguin
 *@author    Mike Atkinson
 */
class FinalOrder extends Ordering {
	private boolean finalOnTop;

	public FinalOrder(boolean way) { finalOnTop = way; }

	/**
	 *  Gets the Index attribute of the FinalOrder object
	 *
	 *@param  object  Description of Parameter
	 *@return         The Index value
	 */
	protected int getIndex(Object object) {
		Object data = ((SimpleNode) object).jjtGetChild(0);
		if (data instanceof ASTClassBodyDeclaration) {
			data = ((ASTClassBodyDeclaration) data).jjtGetChild(0);
		}
		else if (data instanceof ASTInterfaceMemberDeclaration) {
			data = ((ASTInterfaceMemberDeclaration) data).jjtGetChild(0);
		}

		int finalCode = 0;

		//  Now that we have data, determine the type of data
		if (data instanceof ASTEnumDeclaration) {
			finalCode = getFinalCode(((ASTEnumDeclaration) data).getModifiers());
		}
		else if (data instanceof ASTFieldDeclaration) {
			finalCode = getFinalCode(((ASTFieldDeclaration) data).getModifiers());
		}
		else if (data instanceof ASTConstructorDeclaration) {
			finalCode = getFinalCode(((ASTConstructorDeclaration) data).getModifiers());
		}
		else if (data instanceof ASTMethodDeclaration) {
			finalCode = getFinalCode(((ASTMethodDeclaration) data).getModifiers());
		}
		else if (data instanceof ASTNestedInterfaceDeclaration) {
			finalCode = getFinalCode(((ASTNestedInterfaceDeclaration) data).getModifiers());
		}
		else if (data instanceof ASTNestedClassDeclaration) {
			finalCode = getFinalCode(((ASTNestedClassDeclaration) data).getModifiers());
		}
		else {
			return 100;
		}

		if (finalOnTop) {
			return -finalCode;
		}
		else {
			return finalCode;
		}
	}


	/**
	 *  Gets the Protection attribute of the ProtectionOrder object
	 *
	 *@param  mods  Description of Parameter
	 *@return       The Protection value
	 */
	private int getFinalCode(ModifierHolder mods) {
		if (mods.isFinal()) {
			return 1;
		}
		else {
			return 0;
		}
	}
}
