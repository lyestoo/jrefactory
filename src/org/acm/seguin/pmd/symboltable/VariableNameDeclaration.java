package org.acm.seguin.pmd.symboltable;

import org.acm.seguin.parser.ast.ASTVariableDeclaratorId;
import org.acm.seguin.parser.ast.AccessNode;

public class VariableNameDeclaration extends AbstractNameDeclaration implements NameDeclaration {

    public VariableNameDeclaration(ASTVariableDeclaratorId node) {
        super(node);
    }

    public Scope getScope() {
        return node.getScope().getEnclosingClassScope();
    }

    public boolean isExceptionBlockParameter() {
        return ((ASTVariableDeclaratorId) node).isExceptionBlockParameter();
    }

    public AccessNode getAccessNodeParent() {
        return (AccessNode) node.jjtGetParent().jjtGetParent();
    }

    public boolean equals(Object o) {
        VariableNameDeclaration n = (VariableNameDeclaration) o;
        return n.node.getImage().equals(node.getImage());
    }

    public int hashCode() {
        return node.getImage().hashCode();
    }

    public String toString() {
        return "Variable symbol " + node.getImage() + " line " + node.getBeginLine();
    }
}
