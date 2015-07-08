package org.acm.seguin.pmd.symboltable;

import org.acm.seguin.parser.ast.ASTMethodDeclarator;
import org.acm.seguin.parser.ast.ASTVariableDeclaratorId;
import org.acm.seguin.parser.ChildrenVisitor;

public class DeclarationFinder extends ChildrenVisitor {

    public Object visit(ASTVariableDeclaratorId node, Object data) {
        node.getScope().addDeclaration(new VariableNameDeclaration(node));
        return super.visit(node, data);
    }

    public Object visit(ASTMethodDeclarator node, Object data) {
        node.getScope().getEnclosingClassScope().addDeclaration(new MethodNameDeclaration(node));
        return super.visit(node, data);
    }
}
