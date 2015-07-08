package org.acm.seguin.pmd.rules;

import org.acm.seguin.pmd.AbstractRule;
import org.acm.seguin.pmd.RuleContext;
import org.acm.seguin.parser.ast.ASTUnmodifiedClassDeclaration;
import org.acm.seguin.pmd.symboltable.VariableNameDeclaration;

import java.text.MessageFormat;
import java.util.Iterator;

public class UnusedPrivateFieldRule extends AbstractRule {

    public Object visit(ASTUnmodifiedClassDeclaration node, Object data) {
        for (Iterator i = node.getScope().getVariableDeclarations(false).keySet().iterator(); i.hasNext();) {
            VariableNameDeclaration decl = (VariableNameDeclaration) i.next();
            if (decl.getAccessNodeParent().isPrivate() && !decl.getImage().equals("serialVersionUID") && !decl.getImage().equals("serialPersistentFields") && !decl.getImage().equals("IDENT")) {
                RuleContext ctx = (RuleContext) data;
                ctx.getReport().addRuleViolation(createRuleViolation(ctx, decl.getLine(), MessageFormat.format(getMessage(), new Object[]{decl.getImage()})));
            }
        }
        return super.visit(node, data);
    }

}
