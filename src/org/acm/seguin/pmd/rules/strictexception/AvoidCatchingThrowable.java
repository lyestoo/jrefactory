package org.acm.seguin.pmd.rules.strictexception;

import org.acm.seguin.pmd.AbstractRule;
import org.acm.seguin.pmd.RuleContext;
import org.acm.seguin.parser.ast.ASTFormalParameter;
import org.acm.seguin.parser.ast.ASTReferenceType;
import org.acm.seguin.parser.ast.ASTTryStatement;
import org.acm.seguin.parser.ast.ASTClassOrInterfaceType;
import org.acm.seguin.parser.ast.ASTType;
import org.acm.seguin.parser.Node;

/**
 * PMD rule which is going to find <code>catch</code> statements
 * containing <code>throwable</code> as the type definition.
 * <p>
 * @author <a mailto:trondandersen@c2i.net>Trond Andersen</a>
 */
public class AvoidCatchingThrowable extends AbstractRule {


    public Object visit(ASTTryStatement astTryStatement, Object ruleContext) {
        // Requires a catch statement
        if (!astTryStatement.hasCatch()) {
            return super.visit(astTryStatement, ruleContext);
        }

        /* Checking all catch statements */
        int lastIndex = astTryStatement.jjtGetNumChildren();
        for (int ndx = 0; ndx < lastIndex; ndx++) {
            Node next = astTryStatement.jjtGetChild(ndx);
            if (next instanceof ASTFormalParameter) {
               evaluateCatch((ASTFormalParameter)next, (RuleContext)ruleContext);
            }
        }
        return super.visit(astTryStatement, ruleContext);
    }

    /**
     * Checking the catch statement
     * @param aCatch CatchBlock
     * @param ruleContext
     */
    private void evaluateCatch(ASTFormalParameter aCatch, RuleContext ruleContext) {
        ASTType type = (ASTType)aCatch.findChildrenOfType(ASTType.class).get(0);
        ASTReferenceType ref = (ASTReferenceType)type.findChildrenOfType(ASTReferenceType.class).get(0);
        ASTClassOrInterfaceType name = (ASTClassOrInterfaceType)ref.findChildrenOfType(ASTClassOrInterfaceType.class).get(0);

        String image = name.getImage();
        if ("Throwable".equals(image) || "java.lang.Throwable".equals(image)) {
            ruleContext.getReport().addRuleViolation(createRuleViolation(ruleContext, name.getBeginLine()));
        }
    }
}
