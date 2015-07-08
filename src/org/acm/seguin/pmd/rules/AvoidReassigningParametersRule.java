package org.acm.seguin.pmd.rules;

import org.acm.seguin.pmd.AbstractRule;
import org.acm.seguin.pmd.RuleContext;
import org.acm.seguin.parser.ast.ASTMethodDeclarator;
import org.acm.seguin.pmd.symboltable.NameOccurrence;
import org.acm.seguin.pmd.symboltable.Scope;
import org.acm.seguin.pmd.symboltable.VariableNameDeclaration;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AvoidReassigningParametersRule extends AbstractRule {
    public Object visit(ASTMethodDeclarator node, Object data) {
        Scope scope = node.getScope();
        Map params = scope.getVariableDeclarations(true);
        for (Iterator i = params.keySet().iterator(); i.hasNext();) {
            VariableNameDeclaration decl = (VariableNameDeclaration) i.next();
            List usages = (List) params.get(decl);
            for (Iterator j = usages.iterator(); j.hasNext();) {
                NameOccurrence occ = (NameOccurrence) j.next();
                if (occ.isOnLeftHandSide() && !occ.isArrayAccess() && (occ.getNameForWhichThisIsAQualifier() == null)) {
                    RuleContext ctx = (RuleContext) data;
                    String msg = MessageFormat.format(getMessage(), new Object[]{decl.getImage()});
                    ctx.getReport().addRuleViolation(createRuleViolation(ctx, decl.getLine(), msg));
                }
            }
        }
        return super.visit(node, data);
    }
}
