package org.acm.seguin.pmd.rules;

import org.acm.seguin.pmd.AbstractRule;
import org.acm.seguin.pmd.Rule;
import org.acm.seguin.pmd.RuleContext;
import org.acm.seguin.parser.ast.ASTAllocationExpression;
import org.acm.seguin.parser.ast.ASTName;
import org.acm.seguin.parser.ast.ASTPrimaryExpression;
import org.acm.seguin.parser.ast.ASTPrimarySuffix;
import org.acm.seguin.parser.ast.SimpleNode;

import java.util.HashSet;
import java.util.Set;

public class UnnecessaryConversionTemporaryRule extends AbstractRule implements Rule {

    private boolean inPrimaryExpressionContext;
    private boolean usingPrimitiveWrapperAllocation;
    private Set primitiveWrappers = new HashSet();

    public UnnecessaryConversionTemporaryRule() {
        primitiveWrappers.add("Integer");
        primitiveWrappers.add("Boolean");
        primitiveWrappers.add("Double");
        primitiveWrappers.add("Long");
        primitiveWrappers.add("Short");
        primitiveWrappers.add("Byte");
        primitiveWrappers.add("Float");
    }

    public Object visit(ASTPrimaryExpression node, Object data) {
        if (node.jjtGetNumChildren() == 0 || (node.jjtGetFirstChild()).jjtGetNumChildren() == 0 || !(node.jjtGetFirstChild().jjtGetFirstChild() instanceof ASTAllocationExpression)) {
            return super.visit(node, data);
        }
        // TODO... hmmm... is this inPrimaryExpressionContext gibberish necessary?
        inPrimaryExpressionContext = true;
        Object report = super.visit(node, data);
        inPrimaryExpressionContext = false;
        usingPrimitiveWrapperAllocation = false;
        return report;
    }

    public Object visit(ASTAllocationExpression node, Object data) {
        if (!inPrimaryExpressionContext || !(node.jjtGetFirstChild() instanceof ASTName)) {
            return super.visit(node, data);
        }
        if (!primitiveWrappers.contains(((SimpleNode) node.jjtGetFirstChild()).getImage())) {
            return super.visit(node, data);
        }
        usingPrimitiveWrapperAllocation = true;
        return super.visit(node, data);
    }

    public Object visit(ASTPrimarySuffix node, Object data) {
        if (!inPrimaryExpressionContext || !usingPrimitiveWrapperAllocation) {
            return super.visit(node, data);
        }
        if (node.getImage() != null && node.getImage().equals("toString")) {
            RuleContext ctx = (RuleContext) data;
            ctx.getReport().addRuleViolation(createRuleViolation(ctx, node.getBeginLine()));
        }
        return super.visit(node, data);
    }

}
