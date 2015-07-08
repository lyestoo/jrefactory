package org.acm.seguin.pmd.rules;

import org.acm.seguin.pmd.AbstractRule;
import org.acm.seguin.pmd.RuleContext;
import org.acm.seguin.parser.ast.ASTBlock;
import org.acm.seguin.parser.ast.ASTBlockStatement;
import org.acm.seguin.parser.ast.ASTBooleanLiteral;
import org.acm.seguin.parser.ast.ASTIfStatement;
import org.acm.seguin.parser.ast.ASTReturnStatement;
import org.acm.seguin.parser.ast.ASTStatement;
import org.acm.seguin.parser.ast.SimpleNode;

public class SimplifyBooleanReturnsRule extends AbstractRule {

    public Object visit(ASTIfStatement node, Object data) {
        // only deal with if..then..else stmts
        if (node.jjtGetNumChildren() != 3) {
            return super.visit(node, data);
        }

        // don't bother if either the if or the else block is empty
        if (node.jjtGetChild(1).jjtGetNumChildren() == 0 || node.jjtGetChild(2).jjtGetNumChildren() == 0) {
            return super.visit(node, data);
        }

        // first case:
        // If
        //  Expr
        //  Statement
        //   ReturnStatement
        //  Statement
        //   ReturnStatement
        // i.e.,
        // if (foo)
        //  return true;
        // else
        //  return false;

        // second case
        // If
        // Expr
        // Statement
        //  Block
        //   BlockStatement
        //    Statement
        //     ReturnStatement
        // Statement
        //  Block
        //   BlockStatement
        //    Statement
        //     ReturnStatement
        // i.e.,
        // if (foo) {
        //  return true;
        // } else {
        //  return false;
        // }
        if (node.jjtGetChild(1).jjtGetFirstChild() instanceof ASTReturnStatement && node.jjtGetChild(2).jjtGetFirstChild() instanceof ASTReturnStatement && terminatesInBooleanLiteral((SimpleNode) node.jjtGetChild(1).jjtGetFirstChild()) && terminatesInBooleanLiteral((SimpleNode) node.jjtGetChild(2).jjtGetFirstChild())) {
            RuleContext ctx = (RuleContext) data;
            ctx.getReport().addRuleViolation(createRuleViolation(ctx, node.getBeginLine()));
        } else if (hasOneBlockStmt((SimpleNode) node.jjtGetChild(1)) && hasOneBlockStmt((SimpleNode) node.jjtGetChild(2)) && terminatesInBooleanLiteral((SimpleNode) node.jjtGetChild(1).jjtGetFirstChild()) && terminatesInBooleanLiteral((SimpleNode) node.jjtGetChild(2).jjtGetFirstChild())) {
            RuleContext ctx = (RuleContext) data;
            ctx.getReport().addRuleViolation(createRuleViolation(ctx, node.getBeginLine()));
        }

        return super.visit(node, data);
    }

    private boolean hasOneBlockStmt(SimpleNode node) {
        return node.jjtGetFirstChild() instanceof ASTBlock && node.jjtGetFirstChild().jjtGetNumChildren() == 1 && node.jjtGetFirstChild().jjtGetFirstChild() instanceof ASTBlockStatement && node.jjtGetFirstChild().jjtGetFirstChild().jjtGetFirstChild() instanceof ASTStatement && node.jjtGetFirstChild().jjtGetFirstChild().jjtGetFirstChild().jjtGetFirstChild() instanceof ASTReturnStatement;
    }

    private boolean terminatesInBooleanLiteral(SimpleNode node) {
        return eachNodeHasOneChild(node) && (getLastChild(node) instanceof ASTBooleanLiteral);
    }

    private boolean eachNodeHasOneChild(SimpleNode node) {
        if (node.jjtGetNumChildren() > 1) {
            return false;
        }
        if (node.jjtGetNumChildren() == 0) {
            return true;
        }
        return eachNodeHasOneChild((SimpleNode) node.jjtGetFirstChild());
    }

    private SimpleNode getLastChild(SimpleNode node) {
        if (node.jjtGetNumChildren() == 0) {
            return node;
        }
        return getLastChild((SimpleNode) node.jjtGetFirstChild());
    }
}
