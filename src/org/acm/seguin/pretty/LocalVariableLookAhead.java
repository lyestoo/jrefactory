/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.pretty;

import org.acm.seguin.parser.ast.ASTBlockStatement;
import org.acm.seguin.parser.ast.ASTLocalVariableDeclaration;
import org.acm.seguin.parser.ast.ASTName;
import org.acm.seguin.parser.ast.ASTPrimitiveType;
import org.acm.seguin.parser.ast.ASTReferenceType;
import org.acm.seguin.parser.ast.ASTClassOrInterfaceType;
import org.acm.seguin.parser.ast.ASTTypeArguments;
import org.acm.seguin.parser.ast.ASTType;
import org.acm.seguin.parser.ast.ASTVariableDeclaratorId;
import org.acm.seguin.parser.ast.SimpleNode;

/**
 *  Helps determine the size of a local variable for spacing purposes
 *
 *@author    Chris Seguin
 *@author    Mike Atkinson
 */
class LocalVariableLookAhead {
	private FieldSize size;


	/**
	 *  Constructor for the LocalVariableLookAhead object
	 */
	public LocalVariableLookAhead()
	{
		size = new FieldSize();
	}


	/**
	 *  Main processing method for the LocalVariableLookAhead object
	 *
	 *@param  body  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public FieldSize run(SimpleNode body)
	{
		int last = body.jjtGetNumChildren();
		for (int ndx = 0; ndx < last; ndx++) {
			SimpleNode child = (SimpleNode) body.jjtGetChild(ndx);
			if ((child instanceof ASTBlockStatement) &&
					(child.jjtGetFirstChild() instanceof ASTLocalVariableDeclaration)) {
				ASTLocalVariableDeclaration localVariable = (ASTLocalVariableDeclaration) child.jjtGetFirstChild();
				int equalsLength = computeEqualsLength(localVariable);
				size.setMinimumEquals(equalsLength);
			}
		}

		return size;
	}


	/**
	 *  Compute the length of the equals
	 *
	 *@param  localVariable  the local variable
	 *@return                the length
	 */
	public int computeEqualsLength(ASTLocalVariableDeclaration localVariable)
	{
		int modifierLength = computeModifierLength(localVariable);
		int typeLength = computeTypeLength(localVariable);
		int nameLength = computeNameLength(localVariable);
		int equalsLength = modifierLength + typeLength + nameLength;
		return equalsLength;
	}


	/**
	 *  Compute the length of the type
	 *
	 *@param  localVariable  the local variable
	 *@return                the type length
	 */
	public int computeTypeLength(SimpleNode localVariable)
	{
		ASTType typeNode = (ASTType) localVariable.jjtGetFirstChild();
		int typeLength = 0; //2 * typeNode.getArrayCount();
		if (typeNode.jjtGetFirstChild() instanceof ASTPrimitiveType) {
			ASTPrimitiveType primitive = (ASTPrimitiveType) typeNode.jjtGetFirstChild();
			typeLength += primitive.getName().length();
		} else if (typeNode.jjtGetFirstChild() instanceof ASTReferenceType) {
                        typeLength += computeReferenceTypeLength((ASTReferenceType) typeNode.jjtGetFirstChild());
		} else {
			ASTName name = (ASTName) typeNode.jjtGetFirstChild();
			typeLength += name.getName().length();
		}
		size.setTypeLength(typeLength);
		return typeLength;
	}

	/**
	 *  Compute the length of the type
	 *
	 *@param  reference  the reference type
	 *@return            the type length
         *@since             JRefactory 2.7.00
	 */
	public int computeReferenceTypeLength(ASTReferenceType reference)
	{
                int typeLength = 0;
                if (reference.jjtGetFirstChild() instanceof ASTPrimitiveType) {
                        ASTPrimitiveType primitive = (ASTPrimitiveType) reference.jjtGetFirstChild();
                        typeLength += primitive.getName().length();
                } else if (reference.jjtGetFirstChild() instanceof ASTClassOrInterfaceType) {
                        ASTClassOrInterfaceType name = (ASTClassOrInterfaceType) reference.jjtGetFirstChild();
                        typeLength += name.getName().length();
                } else {
                    // FIXME: sometimes the child is a CompilationUnit which should not be a child of a ASTReferenceType
                    //System.out.println("LocalVariableLookAhead.computeReferenceTypeLength: Error: reference.jjtGetFirstChild()="+reference.jjtGetFirstChild());
                }
                typeLength += reference.getArrayCount()*2;       
                return typeLength;
        }

	/**
	 *  Compute the length of the modifier
	 *
	 *@param  localVariable  the local variable
	 *@return                the length of the modifier
	 */
	private int computeModifierLength(ASTLocalVariableDeclaration localVariable)
	{
		int modifierLength = localVariable.isUsingFinal() ? 6 : 0;
		size.setModifierLength(modifierLength);
		return modifierLength;
	}


	/**
	 *  Compute the length of the name
	 *
	 *@param  localVariable  the local variable
	 *@return                the length
	 */
	private int computeNameLength(ASTLocalVariableDeclaration localVariable)
	{
		ASTVariableDeclaratorId id = (ASTVariableDeclaratorId) localVariable.jjtGetChild(1).jjtGetFirstChild();
		int nameLength = id.getName().length();
		size.setNameLength(nameLength);
		return nameLength;
	}
}
