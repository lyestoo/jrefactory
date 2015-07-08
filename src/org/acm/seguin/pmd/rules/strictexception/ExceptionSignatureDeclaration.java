package org.acm.seguin.pmd.rules.strictexception;

import org.acm.seguin.pmd.AbstractRule;
import org.acm.seguin.pmd.RuleContext;
import org.acm.seguin.parser.ast.ASTConstructorDeclaration;
import org.acm.seguin.parser.ast.ASTMethodDeclaration;
import org.acm.seguin.parser.ast.ASTName;
import org.acm.seguin.parser.Node;

import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * @author <a mailto:trondandersen@c2i.net>Trond Andersen</a>
 * @version 1.0
 * @since 1.2
 */
public class ExceptionSignatureDeclaration extends AbstractRule {

    public Object visit(ASTMethodDeclaration methodDeclaration, Object o) {
        List exceptionList = methodDeclaration.findChildrenOfType(ASTName.class);
        if (!hasContent(exceptionList)) {
            return super.visit(methodDeclaration, o);
        }

        evaluateExceptions(exceptionList, (RuleContext)o);
        return super.visit(methodDeclaration, o);
    }


    public Object visit(ASTConstructorDeclaration constructorDeclaration, Object o) {
        List exceptionList = constructorDeclaration.findChildrenOfType(ASTName.class);
        if (!hasContent(exceptionList)) {
            return super.visit(constructorDeclaration, o);
        }

        evaluateExceptions(exceptionList, (RuleContext)o);
        return super.visit(constructorDeclaration, o);
    }

    /**
     * Checks all exceptions for possible violation on the exception declaration.
     * @param exceptionList containing all exception for declaration
     * @param context
     */
    private void evaluateExceptions(List exceptionList, RuleContext context) {
        ASTName exception = null;
        for (Iterator iter = exceptionList.iterator(); iter.hasNext();) {
            exception = (ASTName)iter.next();
            if (hasDeclaredExceptionInSignature(exception)) {
                context.getReport().addRuleViolation(createRuleViolation(context, exception.getBeginLine()));
            }
        }
    }

    /**
     * Checks if the given value is defined as <code>Exception</code> and the parent is either
     * a method or constructor declaration.
     * @param exception to evaluate
     * @return true if <code>Exception</code> is declared and has proper parents
     */
    private boolean hasDeclaredExceptionInSignature(ASTName exception) {
        String image = exception.getImage();
        return ("Exception".equals(image) || "java.lang.Exception".equals(image)) && isParentSignatureDeclaration(exception);
    }

    /**
     * @param exception to evaluate
     * @return true if parent node is either a method or constructor declaration
     */
    private boolean isParentSignatureDeclaration(ASTName exception) {
        Node parent = exception.jjtGetParent().jjtGetParent();
        return parent instanceof ASTMethodDeclaration || parent instanceof ASTConstructorDeclaration;
    }


    private boolean hasContent(List nameList) {
        return (nameList != null && nameList.size() > 0);
    }
}
