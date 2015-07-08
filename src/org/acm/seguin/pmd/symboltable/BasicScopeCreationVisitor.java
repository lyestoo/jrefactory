package org.acm.seguin.pmd.symboltable;

import org.acm.seguin.parser.ast.ASTBlock;
import org.acm.seguin.parser.ast.ASTClassBodyDeclaration;
import org.acm.seguin.parser.ast.ASTCompilationUnit;
import org.acm.seguin.parser.ast.ASTConstructorDeclaration;
import org.acm.seguin.parser.ast.ASTForStatement;
import org.acm.seguin.parser.ast.ASTIfStatement;
import org.acm.seguin.parser.ast.ASTMethodDeclaration;
import org.acm.seguin.parser.ast.ASTSwitchStatement;
import org.acm.seguin.parser.ast.ASTTryStatement;
import org.acm.seguin.parser.ast.ASTUnmodifiedClassDeclaration;
import org.acm.seguin.parser.ast.ASTUnmodifiedInterfaceDeclaration;
import org.acm.seguin.parser.ChildrenVisitor;
import org.acm.seguin.parser.ast.SimpleNode;

import java.util.Stack;

/**
 * Serves as a sort of adaptor between the AST nodes and the symbol table scopes
 */
public class BasicScopeCreationVisitor extends ChildrenVisitor {

    private ScopeFactory sf;
    private Stack scopes = new Stack();

    public BasicScopeCreationVisitor(ScopeFactory sf) {
        this.sf = sf;
    }

    public Object visit(ASTCompilationUnit node, Object data) {
        sf.openScope(scopes, node);
        cont(node);
        return data;
    }

    public Object visit(ASTUnmodifiedClassDeclaration node, Object data) {
        sf.openScope(scopes, node);
        cont(node);
        return data;
    }

    public Object visit(ASTClassBodyDeclaration node, Object data) {
        if (node.isAnonymousInnerClass()) {
            sf.openScope(scopes, node);
            cont(node);
        } else {
            super.visit(node, data);
        }
        return data;
    }

    public Object visit(ASTUnmodifiedInterfaceDeclaration node, Object data) {
        sf.openScope(scopes, node);
        cont(node);
        return data;
    }

    public Object visit(ASTBlock node, Object data) {
        sf.openScope(scopes, node);
        cont(node);
        return data;
    }

    public Object visit(ASTConstructorDeclaration node, Object data) {
        sf.openScope(scopes, node);
        cont(node);
        return data;
    }

    public Object visit(ASTMethodDeclaration node, Object data) {
        sf.openScope(scopes, node);
        cont(node);
        return data;
    }

    public Object visit(ASTTryStatement node, Object data) {
        sf.openScope(scopes, node);
        cont(node);
        return data;
    }

    public Object visit(ASTForStatement node, Object data) {
        sf.openScope(scopes, node);
        cont(node);
        return data;
    }

    public Object visit(ASTIfStatement node, Object data) {
        sf.openScope(scopes, node);
        cont(node);
        return data;
    }

    public Object visit(ASTSwitchStatement node, Object data) {
        sf.openScope(scopes, node);
        cont(node);
        return data;
    }

    private void cont(SimpleNode node) {
        super.visit(node, null);
        scopes.pop();
    }
}
