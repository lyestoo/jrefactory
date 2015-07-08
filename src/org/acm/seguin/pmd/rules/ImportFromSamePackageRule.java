package org.acm.seguin.pmd.rules;

import org.acm.seguin.pmd.AbstractRule;
import org.acm.seguin.pmd.RuleContext;
import org.acm.seguin.parser.ast.ASTCompilationUnit;
import org.acm.seguin.parser.ast.ASTImportDeclaration;
import org.acm.seguin.parser.ast.ASTName;
import org.acm.seguin.parser.ast.ASTPackageDeclaration;

public class ImportFromSamePackageRule extends AbstractRule {

    private String packageName;

    public Object visit(ASTCompilationUnit node, Object data) {
        packageName = null;
        return super.visit(node, data);
    }

    public Object visit(ASTPackageDeclaration node, Object data) {
        packageName = ((ASTName) node.jjtGetFirstChild()).getImage();
        return data;
    }

    public Object visit(ASTImportDeclaration node, Object data) {
        ASTName nameNode = node.getImportedNameNode();
        RuleContext ctx = (RuleContext) data;
        if (packageName != null && !node.isImportOnDemand() && packageName.equals(getPackageName(nameNode.getImage()))) {
            addViolation(ctx, node);
        }

        // special case
        if (packageName == null && getPackageName(nameNode.getImage()).equals("")) {
            addViolation(ctx, node);
        }
        return data;
    }

    private void addViolation(RuleContext ctx, ASTImportDeclaration node) {
        ctx.getReport().addRuleViolation(createRuleViolation(ctx, node.getBeginLine()));
    }

    private String getPackageName(String importName) {
        if (importName.indexOf('.') == -1) {
            return "";
        }
        int lastDot = importName.lastIndexOf('.');
        return importName.substring(0, lastDot);
    }

}
