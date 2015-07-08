package org.acm.seguin.pmd.symboltable;

import org.acm.seguin.parser.ast.SimpleNode;

import java.util.Stack;

public interface ScopeFactory {
    void openScope(Stack scopes, SimpleNode node);
}
