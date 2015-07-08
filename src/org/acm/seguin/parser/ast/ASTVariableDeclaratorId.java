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
import org.acm.seguin.parser.Node;

/**
 *  Declares a variable.  Contains the name of the variable and
 *  the number of [] afterwards.
 *
 *@author     Chris Seguin
 *@created    October 13, 1999
 */
public class ASTVariableDeclaratorId extends SimpleNode {
	//  Instance Variables
	private String name;
	private int arrayCount = 0;


	/**
	 *  Constructor for the ASTVariableDeclaratorId object
	 *
	 *@param  id  Description of Parameter
	 */
	public ASTVariableDeclaratorId(int id) {
		super(id);
	}


	/**
	 *  Constructor for the ASTVariableDeclaratorId object
	 *
	 *@param  p   Description of Parameter
	 *@param  id  Description of Parameter
	 */
	public ASTVariableDeclaratorId(JavaParser p, int id) {
		super(p, id);
	}


	/**
	 *  Set the object's name
	 *
	 *@param  newName  the new name
	 */
	public void setName(String newName) {
		name = newName.intern();
	}


	/**
	 *  Set the object's name (used for PMD testing)
	 *
	 *@param  newName  the new name
	 */
	public void setImage(String newName) {
		name = newName.intern();
	}


	/**
	 *  Set the number of indirection for the array
	 *
	 *@param  count  the count
	 */
	public void setArrayCount(int count) {
		arrayCount = count;
	}


	/**
	 *  Get the object's name
	 *
	 *@return    the name
	 */
	public String getName() {
		return name;
	}


	/**
	 *  Get the number of indirection for the array
	 *
	 *@return    the count
	 */
	public int getArrayCount() {
		return arrayCount;
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

    public boolean isExceptionBlockParameter() {
        return jjtGetParent().jjtGetParent() instanceof ASTTryStatement;
    }

    public SimpleNode getTypeNameNode() {
        if (jjtGetParent().jjtGetParent() instanceof ASTLocalVariableDeclaration) {
            return findTypeNameNode(jjtGetParent().jjtGetParent());
        } else if (jjtGetParent() instanceof ASTFormalParameter) {
            return findTypeNameNode(jjtGetParent());
        } else if (jjtGetParent().jjtGetParent() instanceof ASTFieldDeclaration) {
            return findTypeNameNode(jjtGetParent().jjtGetParent());
        }
        throw new RuntimeException("Don't know how to get the type for anything other than a ASTLocalVariableDeclaration/ASTFormalParameterASTFieldDeclaration");
    }

    private SimpleNode findTypeNameNode(Node node) {
        ASTType typeNode = (ASTType) node.jjtGetFirstChild();
        SimpleNode refNode = (SimpleNode) typeNode.jjtGetFirstChild();
        if (refNode instanceof ASTReferenceType) {
           return (SimpleNode) refNode.jjtGetFirstChild();
        }
        return (SimpleNode) typeNode.jjtGetFirstChild();
    }

}
