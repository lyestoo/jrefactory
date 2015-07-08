package org.acm.seguin.pmd.rules.design;

import org.acm.seguin.pmd.AbstractRule;
import org.acm.seguin.pmd.RuleContext;
import org.acm.seguin.parser.ast.ASTClassDeclaration;
import org.acm.seguin.parser.ast.ASTCompilationUnit;
import org.acm.seguin.parser.ast.ASTConstructorDeclaration;
import org.acm.seguin.parser.ast.ASTMethodDeclaration;
import org.acm.seguin.parser.ast.ASTUnmodifiedClassDeclaration;

public class UseSingletonRule extends AbstractRule {

    private boolean isOK;
    private int methodCount;

    public Object visit(ASTConstructorDeclaration decl, Object data) {
        if (decl.isPrivate()) {
            isOK = true;
        }
        return data;
    }

    public Object visit(ASTUnmodifiedClassDeclaration decl, Object data) {
        if (decl.jjtGetParent() instanceof ASTClassDeclaration && ((ASTClassDeclaration)decl.jjtGetParent()).isAbstract()) {
            isOK = true;
        }
        return super.visit(decl, data);
    }

    public Object visit(ASTMethodDeclaration decl, Object data) {
        methodCount++;

        if (!isOK && !decl.isStatic()) {
            isOK = true;
        }

        return data;
    }

    public Object visit(ASTCompilationUnit cu, Object data) {
        methodCount = 0;
        isOK = false;
        Object RC = cu.childrenAccept(this, data);

        if (!isOK && methodCount > 0) {
            RuleContext ctx = (RuleContext) data;
            ctx.getReport().addRuleViolation(createRuleViolation(ctx, cu.getBeginLine()));
        }

        return RC;
    }
}
