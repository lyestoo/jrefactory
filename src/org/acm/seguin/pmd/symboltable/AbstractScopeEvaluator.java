package org.acm.seguin.pmd.symboltable;

import org.acm.seguin.parser.ast.SimpleNode;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractScopeEvaluator implements ScopeEvaluator {
         protected Set triggers = new HashSet();
         public abstract Scope getScopeFor(SimpleNode node);
         public boolean isScopeCreatedBy(SimpleNode node) {
             return triggers.contains(node.getClass());
         }
     }

