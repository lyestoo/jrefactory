package org.acm.seguin.pmd.symboltable;

import org.acm.seguin.parser.ast.ASTConstructorDeclaration;
import org.acm.seguin.parser.ast.ASTMethodDeclaration;
import org.acm.seguin.parser.ast.SimpleNode;

public class MethodScopeEvaluator extends AbstractScopeEvaluator {
         public MethodScopeEvaluator() {
             triggers.add(ASTConstructorDeclaration.class);
             triggers.add(ASTMethodDeclaration.class);
         }
         public Scope getScopeFor(SimpleNode node) {
             return new MethodScope();
         }
     }

