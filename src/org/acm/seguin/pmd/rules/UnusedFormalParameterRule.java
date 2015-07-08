package org.acm.seguin.pmd.rules;

import org.acm.seguin.pmd.AbstractRule;
import org.acm.seguin.pmd.RuleContext;
import org.acm.seguin.parser.ast.ASTMethodDeclaration;
import org.acm.seguin.pmd.symboltable.VariableNameDeclaration;

import java.text.MessageFormat;
import java.util.Iterator;

public class UnusedFormalParameterRule extends AbstractRule {

    public Object visit(ASTMethodDeclaration node, Object data) {
        if (node.isPrivate() && !node.isNative()) {  // make sure it's both private and not native
            RuleContext ctx = (RuleContext) data;
            for (Iterator i = node.getScope().getVariableDeclarations(false).keySet().iterator(); i.hasNext();) {
                VariableNameDeclaration nameDecl = (VariableNameDeclaration) i.next();
                ctx.getReport().addRuleViolation(createRuleViolation(ctx, node.getBeginLine(), MessageFormat.format(getMessage(), new Object[]{nameDecl.getImage()})));
            }
        }
        return data;
    }
}
