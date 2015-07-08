package org.acm.seguin.refactor.method;

import org.acm.seguin.parser.ast.ASTMethodDeclaration;
import org.acm.seguin.parser.ast.ASTBlock;
import org.acm.seguin.parser.Node;

class EMDigger {
	Node dig(ASTMethodDeclaration start) {
		ASTBlock block = (ASTBlock) start.jjtGetChild(start.jjtGetNumChildren() - 1);
		Node current = block.jjtGetFirstChild();
		while (current.jjtGetNumChildren() == 1) {
			current = current.jjtGetFirstChild();
		}
		return current;
	}

	Node last(ASTMethodDeclaration start) {
		ASTBlock block = (ASTBlock) start.jjtGetChild(start.jjtGetNumChildren() - 1);
		return block;
	}
}
