package org.acm.seguin.pmd.rules.junit;

import org.acm.seguin.pmd.AbstractRule;
import org.acm.seguin.pmd.Rule;
import org.acm.seguin.pmd.RuleContext;
import org.acm.seguin.parser.ast.ASTArguments;
import org.acm.seguin.parser.ast.ASTName;
import org.acm.seguin.parser.ast.ASTPrimaryExpression;
import org.acm.seguin.parser.ast.ASTPrimaryPrefix;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JUnitAssertionsShouldIncludeMessageRule extends AbstractRule implements Rule {

    private static class AssertionCall {
        public int args;
        public String name;

        public AssertionCall(int args, String name) {
            this.args = args;
            this.name = name;
        }
    }

    private List checks = new ArrayList();

    public JUnitAssertionsShouldIncludeMessageRule() {
        checks.add(new AssertionCall(2, "assertEquals"));
        checks.add(new AssertionCall(1, "assertTrue"));
        checks.add(new AssertionCall(1, "assertNull"));
        checks.add(new AssertionCall(2, "assertSame"));
        checks.add(new AssertionCall(1, "assertNotNull"));
    }

    public Object visit(ASTArguments node, Object data) {
        for (Iterator i = checks.iterator(); i.hasNext();) {
            AssertionCall call = (AssertionCall) i.next();
            check((RuleContext) data, node, call.args, call.name);
        }
        return super.visit(node, data);
    }

    private void check(RuleContext ctx, ASTArguments node, int args, String targetMethodName) {
        if (node.getArgumentCount() == args && node.jjtGetParent().jjtGetParent() instanceof ASTPrimaryExpression) {
            ASTPrimaryExpression primary = (ASTPrimaryExpression) node.jjtGetParent().jjtGetParent();
            if (primary.jjtGetFirstChild() instanceof ASTPrimaryPrefix && primary.jjtGetFirstChild().jjtGetNumChildren() > 0 && primary.jjtGetFirstChild().jjtGetFirstChild() instanceof ASTName) {
                ASTName name = (ASTName) primary.jjtGetFirstChild().jjtGetFirstChild();
                if (name.getImage().equals(targetMethodName)) {
                    ctx.getReport().addRuleViolation(createRuleViolation(ctx, name.getBeginLine()));
                }
            }
        }
    }
}
