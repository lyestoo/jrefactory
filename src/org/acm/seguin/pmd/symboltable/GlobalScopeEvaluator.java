package org.acm.seguin.pmd.symboltable;

import org.acm.seguin.parser.ast.ASTCompilationUnit;
import org.acm.seguin.parser.ast.SimpleNode;

public class GlobalScopeEvaluator extends AbstractScopeEvaluator {
         public GlobalScopeEvaluator() {
             triggers.add(ASTCompilationUnit.class);
         }
         public Scope getScopeFor(SimpleNode node) {
             return new GlobalScope();
         }
     }

