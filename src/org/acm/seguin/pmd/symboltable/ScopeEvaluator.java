package org.acm.seguin.pmd.symboltable;

import org.acm.seguin.parser.ast.SimpleNode;

public interface ScopeEvaluator {
    public Scope getScopeFor(SimpleNode node);
    public boolean isScopeCreatedBy(SimpleNode node);
}
