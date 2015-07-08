package org.acm.seguin.pmd.rules;

import org.acm.seguin.pmd.AbstractRule;
import org.acm.seguin.pmd.RuleContext;
import org.acm.seguin.parser.ast.ASTAssignmentOperator;
import org.acm.seguin.parser.ast.ASTExpression;
import org.acm.seguin.parser.ast.ASTName;
import org.acm.seguin.parser.ast.ASTPrimaryExpression;
import org.acm.seguin.parser.ast.ASTStatementExpression;
import org.acm.seguin.parser.ast.SimpleNode;

public class IdempotentOperationsRule extends AbstractRule {

    public Object visit(ASTStatementExpression node, Object data) {
        if (node.jjtGetNumChildren() != 3
                || !(node.jjtGetFirstChild() instanceof ASTPrimaryExpression)
                || !(node.jjtGetChild(1) instanceof ASTAssignmentOperator)
                || !(node.jjtGetChild(2) instanceof ASTExpression)
        ) {
            return super.visit(node, data);
        }

        SimpleNode lhs = (SimpleNode)node.jjtGetFirstChild().jjtGetFirstChild().jjtGetFirstChild();
        if (!(lhs instanceof ASTName)) {
            return super.visit(node, data);
        }

        SimpleNode rhs = (SimpleNode)node.jjtGetChild(2);
        java.util.List rhsName = rhs.findChildrenOfType(ASTName.class);
        //if (!(rhs instanceof ASTName)) {
        if (rhsName.size()!=1) {
            return super.visit(node, data);
        }

        String rhsImage = ((ASTName)rhsName.get(0)).getImage();
        if (!lhs.getImage().equals(rhsImage)) {
            return super.visit(node, data);
        }

        RuleContext ctx = (RuleContext) data;
        ctx.getReport().addRuleViolation(createRuleViolation(ctx, node.getBeginLine(), "Avoid idempotent operations"));
        return data;
    }
}
