/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.pretty.sort;

import org.acm.seguin.parser.ast.ASTClassBodyDeclaration;
import org.acm.seguin.parser.ast.ASTConstructorDeclaration;
import org.acm.seguin.parser.ast.ASTFieldDeclaration;
import org.acm.seguin.parser.ast.ASTInitializer;
import org.acm.seguin.parser.ast.ASTUnmodifiedClassDeclaration;
import org.acm.seguin.parser.ast.ASTUnmodifiedInterfaceDeclaration;
import org.acm.seguin.parser.ast.ASTMethodDeclarator;
import org.acm.seguin.parser.ast.ASTVariableDeclarator;
import org.acm.seguin.parser.ast.ASTVariableDeclaratorId;
import org.acm.seguin.parser.ast.ASTInterfaceMemberDeclaration;
import org.acm.seguin.parser.ast.ASTMethodDeclaration;
import org.acm.seguin.parser.ast.ASTNestedClassDeclaration;
import org.acm.seguin.parser.ast.ASTNestedInterfaceDeclaration;
import org.acm.seguin.parser.ast.ASTEnumDeclaration;
import org.acm.seguin.parser.ast.ASTLiteral;
import org.acm.seguin.parser.ast.SimpleNode;
import org.acm.seguin.parser.ast.ASTTypeParameters;

/**
 *  Description of the Class
 *
 *@author    Chris Seguin
 *@author    Mike Atkinson
 */
class AlphabeticalOrder extends Ordering {
	/**
	 *  Description of the Method
	 *
	 *@param  one  Description of Parameter
	 *@param  two  Description of Parameter
	 *@return      Description of the Returned Value
	 */
	public int compare(Object one, Object two)
	{
		String nameOne = getName(one);
		String nameTwo = getName(two);

		return nameOne.compareTo(nameTwo);
	}


	/**
	 *  Gets the Index attribute of the AlphabeticalOrder object
	 *
	 *@param  object  Description of Parameter
	 *@return         The Index value
	 */
	protected int getIndex(Object object)
	{
		return 0;
	}


	/**
	 *  Gets the Name attribute of the AlphabeticalOrder object
	 *
	 *@param  obj  Description of Parameter
	 *@return      The Name value
	 */
	private String getName(Object object)
	{
		Object data = ((SimpleNode) object).jjtGetChild(0);
		if (data instanceof ASTClassBodyDeclaration) {
			data = ((ASTClassBodyDeclaration) data).jjtGetChild(0);
		}
		else if (data instanceof ASTInterfaceMemberDeclaration) {
			data = ((ASTInterfaceMemberDeclaration) data).jjtGetChild(0);
		}

		//  Now that we have data, determine the type of data
		if (data instanceof ASTEnumDeclaration) {
			ASTEnumDeclaration field = (ASTEnumDeclaration) data;
			ASTLiteral id = (ASTLiteral) field.jjtGetChild(0);
			return id.getName();
		}
		else if (data instanceof ASTFieldDeclaration) {
			ASTFieldDeclaration field = (ASTFieldDeclaration) data;
			ASTVariableDeclarator declar = (ASTVariableDeclarator) field.jjtGetChild(1);
			return ((ASTVariableDeclaratorId) declar.jjtGetChild(0)).getName();
		}
		else if (data instanceof ASTConstructorDeclaration) {
			return "";
		}
		else if (data instanceof ASTMethodDeclaration) {
			ASTMethodDeclaration method = (ASTMethodDeclaration) data;
                        if (method.jjtGetChild(0) instanceof ASTTypeParameters) {
			   ASTMethodDeclarator decl = (ASTMethodDeclarator) method.jjtGetChild(2);
			   return decl.getName();
                        } else {
			   ASTMethodDeclarator decl = (ASTMethodDeclarator) method.jjtGetChild(1);
			   return decl.getName();
                        }
		}
		else if (data instanceof ASTNestedInterfaceDeclaration) {
			ASTNestedInterfaceDeclaration nestedInterface = (ASTNestedInterfaceDeclaration) data;
			ASTUnmodifiedInterfaceDeclaration unmodified = (ASTUnmodifiedInterfaceDeclaration) nestedInterface.jjtGetChild(0);
			return unmodified.getName();
		}
		else if (data instanceof ASTNestedClassDeclaration) {
			ASTNestedClassDeclaration nestedClass = (ASTNestedClassDeclaration) data;
			ASTUnmodifiedClassDeclaration unmodified = (ASTUnmodifiedClassDeclaration) nestedClass.jjtGetChild(0);
			return unmodified.getName();
		}
		else {
			return "";
		}
	}
}
