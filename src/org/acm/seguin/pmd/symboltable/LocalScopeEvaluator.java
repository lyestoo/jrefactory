package org.acm.seguin.pmd.symboltable;

import org.acm.seguin.parser.ast.ASTBlock;
import org.acm.seguin.parser.ast.ASTForStatement;
import org.acm.seguin.parser.ast.ASTIfStatement;
import org.acm.seguin.parser.ast.ASTSwitchStatement;
import org.acm.seguin.parser.ast.ASTTryStatement;
import org.acm.seguin.parser.ast.SimpleNode;

public class LocalScopeEvaluator extends AbstractScopeEvaluator {
         public LocalScopeEvaluator() {
             triggers.add(ASTBlock.class);
             triggers.add(ASTTryStatement.class);
             triggers.add(ASTForStatement.class);
             triggers.add(ASTSwitchStatement.class);
             triggers.add(ASTIfStatement.class);
         }
         public Scope getScopeFor(SimpleNode node) {
             return new LocalScope();
         }
     }

