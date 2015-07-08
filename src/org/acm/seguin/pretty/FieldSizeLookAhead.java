/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.pretty;
import org.acm.seguin.parser.JavaParserConstants;
import org.acm.seguin.parser.Token;

import org.acm.seguin.parser.ast.ASTFieldDeclaration;
import org.acm.seguin.parser.ast.ASTName;
import org.acm.seguin.parser.ast.ASTPrimitiveType;
import org.acm.seguin.parser.ast.ASTType;
import org.acm.seguin.parser.ast.ASTVariableDeclaratorId;
import org.acm.seguin.parser.ast.SimpleNode;

import org.acm.seguin.parser.ast.ASTReferenceType;
import org.acm.seguin.parser.ast.ASTTypeArguments;
import org.acm.seguin.parser.ast.ASTVariance;
import org.acm.seguin.parser.ast.ASTAttribute;

/**
 *  Helps determine the size of a field for spacing purposes
 *
 *@author    Chris Seguin
 *@author    Mike Atkinson
 */
class FieldSizeLookAhead {
	private FieldSize fieldSize;
	private int code;


	/**
	 *  Constructor for the FieldSizeLookAhead object
	 *
	 *@param  init  Description of Parameter
	 */
	public FieldSizeLookAhead(int init)
	{
		fieldSize = new FieldSize();
		code = init;
	}


	/**
	 *  Main processing method for the FieldSizeLookAhead object
	 *
	 *@param  body  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public FieldSize run(SimpleNode body)
	{
		int last = body.jjtGetNumChildren();
		for (int ndx = 0; ndx < last; ndx++) {
			SimpleNode child = (SimpleNode) body.jjtGetChild(ndx);
			if (child.jjtGetChild(0) instanceof ASTFieldDeclaration) {
				ASTFieldDeclaration field = (ASTFieldDeclaration) child.jjtGetChild(0);
				if ((code != PrintData.DFS_NOT_WITH_JAVADOC) || !isJavadocAttached(field)) {
					int equalsLength = computeEqualsLength(field);
					fieldSize.setMinimumEquals(equalsLength);
				}
			}
		}

		return fieldSize;
	}


	/**
	 *  Compute the size of the modifiers, type, and name
	 *
	 *@param  field  the field in question
	 *@return        the size of the modifiers, type, and name
	 */
	public int computeEqualsLength(ASTFieldDeclaration field)
	{
		int modifierLength = computeModifierLength(field);
		int typeLength = computeTypeLength(field);
		int nameLength = computeNameLength(field);

		int equalsLength = modifierLength + typeLength + nameLength;
		return equalsLength;
	}


	/**
	 *  Computes the length of the field declaration type
	 *
	 *@param  field  the field
	 *@return        the number
	 */
	public int computeTypeLength(ASTFieldDeclaration field)
	{
                int child =0;
		if (field.jjtGetChild(0) instanceof ASTAttribute) {
                    child = 1;
                }
		ASTType typeNode = (ASTType) field.jjtGetChild(child);
		int typeLength = 0; //2 * typeNode.getArrayCount();
		if (typeNode.jjtGetChild(0) instanceof ASTPrimitiveType) {
			ASTPrimitiveType primitive = (ASTPrimitiveType) typeNode.jjtGetChild(0);
			typeLength += primitive.getName().length();
		}
		else if (typeNode.jjtGetChild(0) instanceof ASTReferenceType) {
			typeLength += computeReferenceTypeLength((ASTReferenceType) typeNode.jjtGetChild(0));
		}
		else {
                    System.out.println("FieldSizeLookAhead.computeTypeLength(): ASTName: typeNode.jjtGetChild("+0+")="+typeNode.jjtGetChild(0));
			ASTName name = (ASTName) typeNode.jjtGetChild(0);
			typeLength += name.getName().length();
		}
		fieldSize.setTypeLength(typeLength);
		return typeLength;
	}

	/**
	 *  Computes the length of the reference type declaration
	 *
	 *@param  reference  the field
	 *@return        the number
         *@since         JRefactory 2.7.00
	 */
	public int computeReferenceTypeLength(ASTReferenceType reference)
	{
                int typeLength = 0;
                int child =0;
		if (reference.jjtGetChild(0) instanceof ASTAttribute) {
                    child = 1;
                }
                if (reference.jjtGetChild(child) instanceof ASTPrimitiveType) {
                        ASTPrimitiveType primitive = (ASTPrimitiveType) reference.jjtGetChild(child);
                        typeLength += primitive.getName().length();
                } else {
                        ASTName name = (ASTName) reference.jjtGetChild(child);
                        typeLength += name.getName().length();
                }
                int last = reference.jjtGetNumChildren();
                for (int ndx = child+1; ndx < last; ndx++) {
                        if (reference.jjtGetChild(ndx) instanceof ASTVariance) {
                                ASTVariance variance = (ASTVariance)reference.jjtGetChild(ndx);
                                typeLength += 2 + variance.getName().length();
                        } else if (reference.jjtGetChild(ndx) instanceof ASTTypeArguments) {
                                // FIXME: handle TypeArguments length
                        }
                }
                                
                return typeLength;
        }


	/**
	 *  Gets the JavadocAttached attribute of the FieldSizeLookAhead object
	 *
	 *@param  node  Description of Parameter
	 *@return       The JavadocAttached value
	 */
	private boolean isJavadocAttached(ASTFieldDeclaration node)
	{
                int child = 0;
                if (node.jjtGetChild(0) instanceof ASTAttribute) {
                        child++;
                }
                ASTType type = (ASTType)node.jjtGetChild(child);
		return
				hasJavadoc(node.getSpecial("static")) ||
				hasJavadoc(node.getSpecial("transient")) ||
				hasJavadoc(node.getSpecial("volatile")) ||
				hasJavadoc(node.getSpecial("final")) ||
				hasJavadoc(node.getSpecial("public")) ||
				hasJavadoc(node.getSpecial("protected")) ||
				hasJavadoc(node.getSpecial("private")) ||
				hasJavadoc(getInitialToken(type));
	}


	/**
	 *  Check the initial token, and removes it from the object.
	 *
	 *@param  top  the type
	 *@return      the initial token
	 */
	private Token getInitialToken(ASTType top)
	{
		if (top.jjtGetChild(0) instanceof ASTPrimitiveType) {
			ASTPrimitiveType primitiveType = (ASTPrimitiveType) top.jjtGetChild(0);
			return primitiveType.getSpecial("primitive");
		}
                else if (top.jjtGetChild(0) instanceof ASTReferenceType) {
                        ASTReferenceType reference = (ASTReferenceType) top.jjtGetChild(0);
                        if (reference.jjtGetChild(0) instanceof ASTPrimitiveType) {
                                ASTPrimitiveType primitiveType = (ASTPrimitiveType) reference.jjtGetChild(0);
                                return primitiveType.getSpecial("primitive");
                        } else {
                                ASTName name = (ASTName) reference.jjtGetChild(0);
                                return name.getSpecial("id0");
                        }
                }
		else {
                        // this should not occur now!
			System.out.println("FieldSizeLookAhead.getInitialToken(): ASTName: top.jjtGetChild(0)="+top.jjtGetChild(0));
			ASTName name = (ASTName) top.jjtGetChild(0);
			return name.getSpecial("id0");
		}
	}


	/**
	 *  Description of the Method
	 *
	 *@param  field  Description of Parameter
	 *@return        Description of the Returned Value
	 */
	private int computeNameLength(ASTFieldDeclaration field)
	{
		int child = 0;
                if (field.jjtGetChild(0) instanceof ASTAttribute) {
                        child++;
                }
                ASTVariableDeclaratorId id = (ASTVariableDeclaratorId) field.jjtGetChild(1).jjtGetChild(child);
		int nameLength = id.getName().length();
		fieldSize.setNameLength(nameLength);
		return nameLength;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  field  Description of Parameter
	 *@return        Description of the Returned Value
	 */
	private int computeModifierLength(ASTFieldDeclaration field)
	{
		int fieldLength = field.getModifiersString(PrintData.STANDARD_ORDER).length();
		fieldSize.setModifierLength(fieldLength);
		return fieldLength;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  tok  Description of Parameter
	 *@return      Description of the Returned Value
	 */
	private boolean hasJavadoc(Token tok)
	{
		Token current = tok;
		while (current != null) {
			if (current.kind == JavaParserConstants.FORMAL_COMMENT) {
				return true;
			}

			current = current.specialToken;
		}

		return false;
	}
}
