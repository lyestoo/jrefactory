package org.acm.seguin.pmd.rules;

import org.acm.seguin.pmd.AbstractRule;
import org.acm.seguin.pmd.RuleContext;
import org.acm.seguin.parser.ast.ASTCompilationUnit;
import org.acm.seguin.parser.ast.ASTIfStatement;

public class AvoidDeeplyNestedIfStmtsRule extends AbstractRule {

    private int depth;

    public Object visit(ASTCompilationUnit node, Object data) {
        depth = 0;
        return super.visit(node, data);
    }

    public Object visit(ASTIfStatement node, Object data) {
        if (!node.hasElse()) {
            depth++;
        }
        super.visit(node, data);
        if (depth == getIntProperty("problemDepth")) {
            RuleContext ctx = (RuleContext) data;
            ctx.getReport().addRuleViolation(createRuleViolation(ctx, node.getBeginLine()));
        }
        depth--;
        return data;
    }

}
