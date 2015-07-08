package org.acm.seguin.refactor.type;

import org.acm.seguin.parser.ChildrenVisitor;
import org.acm.seguin.parser.ast.ASTName;
import org.acm.seguin.parser.ast.ASTClassOrInterfaceType;
import org.acm.seguin.parser.ast.ASTUnmodifiedClassDeclaration;
import org.acm.seguin.parser.JavaParserTreeConstants;

/**
 *  Description of the Class 
 *
 *@author    Chris Seguin 
 */
public class RenameParentVisitor extends ChildrenVisitor {
	/**
	 *  To visit a node 
	 *
	 *@param  node  The node we are visiting 
	 *@param  data  The rename type data 
	 *@return       The rename type data 
	 */
	public Object visit(ASTUnmodifiedClassDeclaration node, Object data) {
		RenameTypeData rtd = (RenameTypeData) data;
		String oldName = rtd.getOld().getName();

		if (oldName.equals(node.getName())) {
			if (node.jjtGetFirstChild() instanceof ASTClassOrInterfaceType) {
				node.jjtAddChild(new ASTClassOrInterfaceType(rtd.getNew()),0);
			}
			else {
				node.jjtInsertChild(new ASTClassOrInterfaceType(rtd.getNew()),0);
			}
		}

		return data;
	}
}
