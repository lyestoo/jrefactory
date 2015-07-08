package org.acm.seguin.pmd.rules.design;

import org.acm.seguin.pmd.AbstractRule;
import org.acm.seguin.pmd.RuleContext;
import org.acm.seguin.parser.ast.ASTName;
import org.acm.seguin.parser.ast.ASTWhileStatement;
import org.acm.seguin.parser.ast.SimpleNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PositionalIteratorRule extends AbstractRule {

    public Object visit(ASTWhileStatement node, Object data) {
        if (hasNameAsChild((SimpleNode) node.jjtGetFirstChild())) {
            String exprName = getName((SimpleNode) node.jjtGetFirstChild());
            if (exprName.indexOf(".hasNext") != -1 && node.jjtGetNumChildren() > 1) {

                SimpleNode loopBody = (SimpleNode) node.jjtGetChild(1);
                List names = new ArrayList();
                collectNames(getVariableName(exprName), names, loopBody);
                int nextCount = 0;
                for (Iterator i = names.iterator(); i.hasNext();) {
                    String name = (String) i.next();
                    if (name.indexOf(".next") != -1) {
                        nextCount++;
                    }
                }

                if (nextCount > 1) {
                    RuleContext ctx = (RuleContext) data;
                    ctx.getReport().addRuleViolation(createRuleViolation(ctx, node.getBeginLine()));
                }

            }
        }
        return null;
    }

    private String getVariableName(String exprName) {
        return exprName.substring(0, exprName.indexOf('.'));
    }

    private void collectNames(String target, List names, SimpleNode node) {
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) node.jjtGetChild(i);
            if (child instanceof ASTName && child.getImage().indexOf(".") != -1 && target.equals(getVariableName(child.getImage()))) {
                names.add(child.getImage());
            } else if (child.jjtGetNumChildren() > 0) {
                collectNames(target, names, child);
            } else {
            }
        }
    }

    private boolean hasNameAsChild(SimpleNode node) {
        SimpleNode child = node;
        while (child.jjtGetNumChildren() > 0) {
            if (child instanceof ASTName) {
                return true;
            }
            child = (SimpleNode)child.jjtGetFirstChild();
        }
        return false;
    }

    private String getName(SimpleNode node) {
        SimpleNode child = node;
        while (child.jjtGetNumChildren() > 0) {
            if (child instanceof ASTName) {
                return ((ASTName) child).getImage();
            }
            child = (SimpleNode)child.jjtGetFirstChild();
        }
        throw new IllegalArgumentException("Check with hasNameAsChild() first!");
    }
}
