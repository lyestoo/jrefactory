package org.acm.seguin.pmd.symboltable;

import org.acm.seguin.parser.ast.SimpleNode;

public abstract class AbstractNameDeclaration {

    protected SimpleNode node;

    public AbstractNameDeclaration(SimpleNode node) {
        this.node = node;
    }

    public Scope getScope() {
        return node.getScope();
    }

    public int getLine() {
        return node.getBeginLine();
    }

    public String getImage() {
        return node.getImage();
    }
}
