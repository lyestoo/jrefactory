/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.parser.query;

import java.util.Enumeration;
import org.acm.seguin.parser.ast.*;
import org.acm.seguin.pretty.ModifierHolder;

/**
 *  This software compares a parse tree to insure that they are identical.
 *  @FIXME: add in comparisons for Enum, Generics, etc.
 *@author    Chris Seguin
 *@author    Mike Atkinson
 */
public class EqualTree extends CompareParseTreeVisitor {
    
    
	/**
	 *  Visit a node, comparing it with that supplied in data.
	 *
         *  It used the CompareParseTreeVisitor.defaultComparison() method to do the comparison.
	 *  
	 *@param  node  The node we are visiting
	 *@param  data  The simple node to compare
	 *@return       Boolean.TRUE or Boolean.FALSE
         *@since        JRefactory 2.7.00
         */
        public Object visit(ASTTypeParameterList node, Object data) {
		return defaultComparison(node, data);
        }
    
	/**
	 *  Visit a node, comparing it with that supplied in data.
	 *
         *  It used the CompareParseTreeVisitor.defaultComparison() method to do the comparison.
	 *  
	 *@param  node  The node we are visiting
	 *@param  data  The simple node to compare
	 *@return       Boolean.TRUE or Boolean.FALSE
         *@since        JRefactory 2.7.00
         */
        public Object visit(ASTTypeParameter node, Object data) {
		return defaultComparison(node, data);
        }
    
	/**
	 *  Visit a node, comparing it with that supplied in data.
	 *
         *  It used the CompareParseTreeVisitor.defaultComparison() method to do the comparison.
	 *  
	 *@param  node  The node we are visiting
	 *@param  data  The simple node to compare
	 *@return       Boolean.TRUE or Boolean.FALSE
         *@since        JRefactory 2.7.00
         */
        public Object visit(ASTTypeArguments node, Object data) {
		return defaultComparison(node, data);
        }
    
	/**
	 *  Visit a node, comparing it with that supplied in data.
	 *
         *  It used the CompareParseTreeVisitor.defaultComparison() method to do the comparison.
	 *  
	 *@param  node  The node we are visiting
	 *@param  data  The simple node to compare
	 *@return       Boolean.TRUE or Boolean.FALSE
         *@since        JRefactory 2.7.00
         */
        public Object visit(ASTReferenceTypeList node, Object data) {
		return defaultComparison(node, data);
        }
    
	/**
	 *  Visit a node, comparing it with that supplied in data.
	 *
	 *  
	 *@param  node  The node we are visiting
	 *@param  data  The simple node to compare
	 *@return       Boolean.TRUE or Boolean.FALSE
         *@since        JRefactory 2.7.00
         */
        public Object visit(ASTReferenceType node, Object data) {
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			if (node.getArrayCount() == ((ASTReferenceType) data).getArrayCount()) {
				return Boolean.TRUE;
			}
		}

		//System.out.println("Different type:  different structure");
		return Boolean.FALSE;
        }
    
	/**
	 *  Visit a node, comparing it with that supplied in data.
	 *
         *  It used the CompareParseTreeVisitor.defaultComparison() method to do the comparison.
	 *  
	 *@param  node  The node we are visiting
	 *@param  data  The simple node to compare
	 *@return       Boolean.TRUE or Boolean.FALSE
         *@since        JRefactory 2.7.00
         */
        public Object visit(ASTTypeParameters node, Object data) {
		return defaultComparison(node, data);
        }  
    
	/**
	 *  Visit a node, comparing it with that supplied in data.
	 *
         *  It used the CompareParseTreeVisitor.defaultComparison() method to do the comparison.
	 *  
	 *@param  node  The node we are visiting
	 *@param  data  The simple node to compare
	 *@return       Boolean.TRUE or Boolean.FALSE
         *@since        JRefactory 2.7.00
         */
        public Object visit(ASTGenericNameList node, Object data) {
		return defaultComparison(node, data);
        }  
    
	/**
	 *  Visit a node, comparing it with that supplied in data.
	 *
	 *  
	 *@param  node  The node we are visiting
	 *@param  data  The simple node to compare
	 *@return       Boolean.TRUE or Boolean.FALSE
         *@since        JRefactory 2.7.00
         */
        public Object visit(ASTVariance node, Object data) {
		if (super.visit(node, data).equals(Boolean.TRUE)) {
                        String nodeName = node.getName();
                        String otherName = ((ASTVariance) data).getName();
			if (nodeName== null && otherName==null) {
				return Boolean.TRUE;
			} else if (nodeName!=null && nodeName.equals(otherName)) {
                                return Boolean.TRUE;
                        }
		}

		//System.out.println("Different type:  different structure");
		return Boolean.FALSE;
        }
    
	/**
	 *  Visit a node, comparing it with that supplied in data.
	 *
         *  It used the CompareParseTreeVisitor.defaultComparison() method to do the comparison.
	 *  
	 *@param  node  The node we are visiting
	 *@param  data  The simple node to compare
	 *@return       Boolean.TRUE or Boolean.FALSE
         *@since        JRefactory 2.7.00
         */
        public Object visit(ASTEnumDeclaration node, Object data) {
		return defaultComparison(node, data);
        }
    
	/**
	 *  Visit a node, comparing it with that supplied in data.
	 *
         *  It used the CompareParseTreeVisitor.defaultComparison() method to do the comparison.
	 *  
	 *@param  node  The node we are visiting
	 *@param  data  The simple node to compare
	 *@return       Boolean.TRUE or Boolean.FALSE
         *@since        JRefactory 2.7.00
         */
        public Object visit(ASTEnumElement node, Object data) {
		return defaultComparison(node, data);
        }
    
	/**
	 *  Visit a node, comparing it with that supplied in data.
	 *
         *  It used the CompareParseTreeVisitor.defaultComparison() method to do the comparison.
	 *  
	 *@param  node  The node we are visiting
	 *@param  data  The simple node to compare
	 *@return       Boolean.TRUE or Boolean.FALSE
         *@since        JRefactory 2.7.00
         */
        public Object visit(ASTIdentifier node, Object data) {
		return defaultComparison(node, data);
        }
    
	/**
	 *  Visit a node, comparing it with that supplied in data.
	 *
         *  It used the CompareParseTreeVisitor.defaultComparison() method to do the comparison.
	 *  
	 *@param  node  The node we are visiting
	 *@param  data  The simple node to compare
	 *@return       Boolean.TRUE or Boolean.FALSE
         *@since        JRefactory 2.7.00
         */
        public Object visit(ASTAttribute node, Object data) {
		return defaultComparison(node, data);
        }
                
	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTAdditiveExpression node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			ASTAdditiveExpression other = (ASTAdditiveExpression) data;
			Enumeration enum1 = node.getNames();
			Enumeration enum2 = other.getNames();
			while (enum1.hasMoreElements() && enum2.hasMoreElements()) {
				Object next1 = enum1.nextElement();
				Object next2 = enum2.nextElement();

				if (!next1.equals(next2)) {
					//System.out.println("Different additive expression:  term different");
					return Boolean.FALSE;
				}
			}

			if (enum1.hasMoreElements() || enum2.hasMoreElements()) {
				//System.out.println("Different additive expression:  different number of terms");
				return Boolean.FALSE;
			}

			return Boolean.TRUE;
		}

		//System.out.println("Different additive expression:  structure different");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTArrayDimsAndInits node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			if (node.getArrayCount() == ((ASTArrayDimsAndInits) data).getArrayCount()) {
				return Boolean.TRUE;
			}
		}

		//System.out.println("Different array dim and inits:  structure different");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTArrayInitializer node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			if (node.isFinalComma() == ((ASTArrayInitializer) data).isFinalComma()) {
				return Boolean.TRUE;
			}
		}

		//System.out.println("Different array initializer:  structure different");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTAssignmentOperator node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			if (node.getName().equals(((ASTAssignmentOperator) data).getName())) {
				return Boolean.TRUE;
			}
		}

		//System.out.println("Different assignment operator:  structure different");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTBooleanLiteral node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			if (node.getName().equals(((ASTBooleanLiteral) data).getName())) {
				return Boolean.TRUE;
			}
		}

		//System.out.println("Different boolean literal:  structure different");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTBreakStatement node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			if (node.getName().equals(((ASTBreakStatement) data).getName())) {
				return Boolean.TRUE;
			}
		}

		//System.out.println("Different break statement:  structure different");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTClassDeclaration node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			if (node.getModifiers().equals(((ASTClassDeclaration) data).getModifiers())) {
				return Boolean.TRUE;
			}
		}

		//System.out.println("Different class declaration:  structure different");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTConstructorDeclaration node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			if (node.getModifiers().equals(((ASTConstructorDeclaration) data).getModifiers()) &&
				node.getName().equals(((ASTConstructorDeclaration) data).getName())) {
				return Boolean.TRUE;
			}
		}

		//System.out.println("Different constructor declaration:  structure different");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTContinueStatement node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			if (node.getName().equals(((ASTContinueStatement) data).getName())) {
				return Boolean.TRUE;
			}
		}

		//System.out.println("Different continue statement:  structure different");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTEqualityExpression node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			ASTEqualityExpression other = (ASTEqualityExpression) data;
			Enumeration enum1 = node.getNames();
			Enumeration enum2 = other.getNames();
			while (enum1.hasMoreElements() && enum2.hasMoreElements()) {
				Object next1 = enum1.nextElement();
				Object next2 = enum2.nextElement();

				if (!next1.equals(next2)) {
					//System.out.println("Different equality expression:  different term");
					return Boolean.FALSE;
				}
			}

			if (enum1.hasMoreElements() || enum2.hasMoreElements()) {
				//System.out.println("Different equality expression:  different number of terms");
				return Boolean.FALSE;
			}

			return Boolean.TRUE;
		}

		//System.out.println("Different equality expression:  structure different");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTExplicitConstructorInvocation node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			if (node.getName().equals(((ASTExplicitConstructorInvocation) data).getName())) {
				return Boolean.TRUE;
			}
		}

		//System.out.println("Different explicit constructor invoke:  structure different");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTFieldDeclaration node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			ModifierHolder nodeModifiers = node.getModifiers();
			ModifierHolder dataModifiers = ((ASTFieldDeclaration) data).getModifiers();
			if (nodeModifiers.equals(dataModifiers)) {
				return Boolean.TRUE;
			}
			//System.out.println("Different field modifiers:  [" + nodeModifiers + "] [" + dataModifiers + "}");
		}

		//System.out.println("Different field declaration:  structure different");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTFormalParameter node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			if (node.isUsingFinal() == ((ASTFormalParameter) data).isUsingFinal()) {
				return Boolean.TRUE;
			}
		}

		//System.out.println("Different formal parameter:  structure different");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTImportDeclaration node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			if (node.isImportingPackage() == ((ASTImportDeclaration) data).isImportingPackage()) {
				return Boolean.TRUE;
			}
		}

		//System.out.println("Different import:  structure different");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTInitializer node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			if (node.isUsingStatic() == ((ASTInitializer) data).isUsingStatic()) {
				return Boolean.TRUE;
			}
		}

		//System.out.println("Different initializer:  structure different");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTInterfaceDeclaration node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			if (node.getModifiers().equals(((ASTInterfaceDeclaration) data).getModifiers())) {
				return Boolean.TRUE;
			}
		}

		//System.out.println("Different interface declaration:  structure different");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTLabeledStatement node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			if (node.getName().equals(((ASTLabeledStatement) data).getName())) {
				return Boolean.TRUE;
			}
		}

		//System.out.println("Different labeled statement:  structure different");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTLiteral node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			if (node.getName().equals(((ASTLiteral) data).getName())) {
				return Boolean.TRUE;
			}
		}

		//System.out.println("Different literal:  structure different");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTLocalVariableDeclaration node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			if (node.isUsingFinal() == ((ASTLocalVariableDeclaration) data).isUsingFinal()) {
				return Boolean.TRUE;
			}
		}

		//System.out.println("Different local variable declaration:  structure different");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTMethodDeclaration node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			ModifierHolder nodeModifiers = node.getModifiers();
			ModifierHolder dataModifiers = ((ASTMethodDeclaration) data).getModifiers();
			if (nodeModifiers.equals(dataModifiers)) {
				return Boolean.TRUE;
			}
			//System.out.println("Different method modifiers:  [" + nodeModifiers + "] [" + dataModifiers + "}");
		}

		//System.out.println("Different method declaration:  structure different");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTMethodDeclarator node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			if ((node.getName().equals(((ASTMethodDeclarator) data).getName())) &&
					(node.getArrayCount() == ((ASTMethodDeclarator) data).getArrayCount())) {
				return Boolean.TRUE;
			}
		}

		//System.out.println("Different method declarator:  structure different");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTMultiplicativeExpression node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			ASTMultiplicativeExpression other = (ASTMultiplicativeExpression) data;
			Enumeration enum1 = node.getNames();
			Enumeration enum2 = other.getNames();
			while (enum1.hasMoreElements() && enum2.hasMoreElements()) {
				Object next1 = enum1.nextElement();
				Object next2 = enum2.nextElement();

				if (!next1.equals(next2)) {
					//System.out.println("Different multiplicative expression:  different term");
					return Boolean.FALSE;
				}
			}

			if (enum1.hasMoreElements() || enum2.hasMoreElements()) {
				//System.out.println("Different multiplicative expression:  different number of terms");
				return Boolean.FALSE;
			}

			return Boolean.TRUE;
		}

		//System.out.println("Different multiplicative expression:  structure different");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTName node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			ASTName other = (ASTName) data;

			if (other.getNameSize() != node.getNameSize()) {
				//System.out.println("Different name:  different size");
				return Boolean.FALSE;
			}

			for (int ndx = 0; ndx < node.getNameSize(); ndx++) {
				String next1 = node.getNamePart(ndx);
				String next2 = other.getNamePart(ndx);
				if (!next1.equals(next2)) {
					//System.out.println("Different name:  different term " + ndx);
					return Boolean.FALSE;
				}
			}

			return Boolean.TRUE;
		}

		//System.out.println("Different name:  different structure");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTNestedClassDeclaration node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			if (node.getModifiers().equals(((ASTNestedClassDeclaration) data).getModifiers())) {
				return Boolean.TRUE;
			}
		}

		//System.out.println("Different nested class declaration:  different structure");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTNestedInterfaceDeclaration node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			if (node.getModifiers().equals(((ASTNestedInterfaceDeclaration) data).getModifiers())) {
				return Boolean.TRUE;
			}
		}

		//System.out.println("Different nested interface declaration:  different structure");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTPostfixExpression node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			if (node.getName().equals(((ASTPostfixExpression) data).getName())) {
				return Boolean.TRUE;
			}
		}

		//System.out.println("Different postfix expression:  different structure");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTPrimaryPrefix node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			if (node.getName().equals(((ASTPrimaryPrefix) data).getName())) {
				return Boolean.TRUE;
			}
		}

		//System.out.println("Different name:  primary prefix structure");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTPrimarySuffix node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			if (node.getName().equals(((ASTPrimarySuffix) data).getName())) {
				return Boolean.TRUE;
			}
		}

		//System.out.println("Different primary suffix:  different structure");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTPrimitiveType node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			if (node.getName().equals(((ASTPrimitiveType) data).getName())) {
				return Boolean.TRUE;
			}
		}

		//System.out.println("Different primitive type:  different structure");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTRelationalExpression node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			ASTRelationalExpression other = (ASTRelationalExpression) data;
			Enumeration enum1 = node.getNames();
			Enumeration enum2 = other.getNames();
			while (enum1.hasMoreElements() && enum2.hasMoreElements()) {
				Object next1 = enum1.nextElement();
				Object next2 = enum2.nextElement();

				if (!next1.equals(next2)) {
					//System.out.println("Different relational expression:  different term");
					return Boolean.FALSE;
				}
			}

			if (enum1.hasMoreElements() || enum2.hasMoreElements()) {
				//System.out.println("Different relational expression:  different number of terms");
				return Boolean.FALSE;
			}

			return Boolean.TRUE;
		}

		//System.out.println("Different relational expression:  different structure");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTShiftExpression node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			ASTShiftExpression other = (ASTShiftExpression) data;
			Enumeration enum1 = node.getNames();
			Enumeration enum2 = other.getNames();
			while (enum1.hasMoreElements() && enum2.hasMoreElements()) {
				Object next1 = enum1.nextElement();
				Object next2 = enum2.nextElement();

				if (!next1.equals(next2)) {
					//System.out.println("Different shift expression:  different term");
					return Boolean.FALSE;
				}
			}

			if (enum1.hasMoreElements() || enum2.hasMoreElements()) {
				//System.out.println("Different shift expression:  different number of terms");
				return Boolean.FALSE;
			}

			return Boolean.TRUE;
		}

		//System.out.println("Different relational expression:  different structure");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTStatementExpression node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			if (node.getName().equals(((ASTStatementExpression) data).getName())) {
				return Boolean.TRUE;
			}
		}

		//System.out.println("Different expression statement:  different structure");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTType node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			if (node.getArrayCount() == ((ASTType) data).getArrayCount()) {
				return Boolean.TRUE;
			}
		}

		//System.out.println("Different type:  different structure");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTUnaryExpression node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			if (node.getName().equals(((ASTUnaryExpression) data).getName())) {
				return Boolean.TRUE;
			}
		}

		//System.out.println("Different unary expression:  different structure");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTUnaryExpressionNotPlusMinus node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			if (node.getName().equals(((ASTUnaryExpressionNotPlusMinus) data).getName())) {
				return Boolean.TRUE;
			}
		}

		//System.out.println("Different unary expression (not +/-):  different structure");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTUnmodifiedClassDeclaration node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			if (node.getName().equals(((ASTUnmodifiedClassDeclaration) data).getName())) {
				return Boolean.TRUE;
			}
		}

		//System.out.println("Different unmodified class decl:  different structure");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTUnmodifiedInterfaceDeclaration node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			if (node.getName().equals(((ASTUnmodifiedInterfaceDeclaration) data).getName())) {
				return Boolean.TRUE;
			}
		}

		//System.out.println("Different unmodified interface decl:  different structure");
		return Boolean.FALSE;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  node  Description of Parameter
	 *@param  data  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public Object visit(ASTVariableDeclaratorId node, Object data)
	{
		if (super.visit(node, data).equals(Boolean.TRUE)) {
			if ((node.getName().equals(((ASTVariableDeclaratorId) data).getName())) &&
					(node.getArrayCount() == ((ASTVariableDeclaratorId) data).getArrayCount())) {
				return Boolean.TRUE;
			}
		}

		//System.out.println("Different variable declarator id:  different structure");
		return Boolean.FALSE;
	}
}
