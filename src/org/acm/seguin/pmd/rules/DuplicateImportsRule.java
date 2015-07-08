package org.acm.seguin.pmd.rules;

import org.acm.seguin.pmd.AbstractRule;
import org.acm.seguin.pmd.RuleContext;
import org.acm.seguin.parser.ast.ASTCompilationUnit;
import org.acm.seguin.parser.ast.ASTImportDeclaration;
import org.acm.seguin.parser.ast.SimpleNode;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DuplicateImportsRule extends AbstractRule {

    private Set singleTypeImports;
    private Set importOnDemandImports;

    public Object visit(ASTCompilationUnit node, Object data) {
        RuleContext ctx = (RuleContext) data;
        singleTypeImports = new HashSet();
        importOnDemandImports = new HashSet();
        super.visit(node, data);

        // this checks for things like:
        // import java.io.*;
        // import java.io.File;
        for (Iterator i = importOnDemandImports.iterator(); i.hasNext();) {
            ImportWrapper thisImportOnDemand = (ImportWrapper) i.next();
            for (Iterator j = singleTypeImports.iterator(); j.hasNext();) {
                ImportWrapper thisSingleTypeImport = (ImportWrapper) j.next();
                String singleTypePkg = thisSingleTypeImport.getName().substring(0, thisSingleTypeImport.getName().lastIndexOf("."));
                if (thisImportOnDemand.getName().equals(singleTypePkg)) {
                    String msg = MessageFormat.format(getMessage(), new Object[]{thisSingleTypeImport.getName()});
                    ctx.getReport().addRuleViolation(createRuleViolation(ctx, thisSingleTypeImport.getLine(), msg));
                }
            }
        }
        singleTypeImports.clear();
        importOnDemandImports.clear();
        return data;
    }

    public Object visit(ASTImportDeclaration node, Object data) {
        ImportWrapper wrapper = new ImportWrapper(node.getImportedNameNode().getImage(), node.getImportedNameNode().getBeginLine());

        // blahhhh... this really wants to be ASTImportDeclaration to be polymorphic...
        if (node.isImportOnDemand()) {
            if (importOnDemandImports.contains(wrapper)) {
                createRV((RuleContext) data, node.getImportedNameNode());
            } else {
                importOnDemandImports.add(wrapper);
            }
        } else {
            if (singleTypeImports.contains(wrapper)) {
                createRV((RuleContext) data, node.getImportedNameNode());
            } else {
                singleTypeImports.add(wrapper);
            }
        }
        return data;
    }

    private void createRV(RuleContext ctx, SimpleNode importNameNode) {
        String msg = MessageFormat.format(getMessage(), new Object[]{importNameNode.getImage()});
        ctx.getReport().addRuleViolation(createRuleViolation(ctx, importNameNode.getBeginLine(), msg));
    }
}
