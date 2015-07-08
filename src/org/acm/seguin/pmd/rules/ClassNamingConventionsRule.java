package org.acm.seguin.pmd.rules;

import org.acm.seguin.pmd.AbstractRule;
import org.acm.seguin.pmd.RuleContext;
import org.acm.seguin.parser.ast.ASTUnmodifiedClassDeclaration;

public class ClassNamingConventionsRule extends AbstractRule {

  public Object visit(ASTUnmodifiedClassDeclaration node, Object data) {

    if (Character.isLowerCase(node.getImage().charAt(0))) {
      RuleContext ctx = (RuleContext)data;
      ctx.getReport().addRuleViolation(createRuleViolation(ctx, node.getBeginLine(), getMessage()));
    }

    if (node.getImage().indexOf("_") >= 0) {
        RuleContext ctx = (RuleContext)data;
      ctx.getReport().addRuleViolation(createRuleViolation(ctx, node.getBeginLine(), "Class names should not contain underscores"));

    }

    return data;
  }
}
