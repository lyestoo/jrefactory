package org.acm.seguin.pmd.symboltable;

import org.acm.seguin.parser.ast.ASTClassBodyDeclaration;
import org.acm.seguin.parser.ast.ASTUnmodifiedClassDeclaration;
import org.acm.seguin.parser.ast.ASTUnmodifiedInterfaceDeclaration;
import org.acm.seguin.parser.ast.SimpleNode;

public class ClassScopeEvaluator extends AbstractScopeEvaluator {
         public ClassScopeEvaluator() {
             triggers.add(ASTUnmodifiedClassDeclaration.class);
             triggers.add(ASTUnmodifiedInterfaceDeclaration.class);
             triggers.add(ASTClassBodyDeclaration.class);
         }
         public Scope getScopeFor(SimpleNode node) {
             if (node instanceof ASTClassBodyDeclaration) {
                 return new ClassScope();
             }
             return new ClassScope(node.getImage());
         }
     }

