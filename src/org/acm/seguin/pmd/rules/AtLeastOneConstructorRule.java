package org.acm.seguin.pmd.rules;


import org.acm.seguin.pmd.AbstractRule;
import org.acm.seguin.pmd.RuleContext;
import org.acm.seguin.parser.ast.ASTConstructorDeclaration;
import org.acm.seguin.parser.ast.ASTUnmodifiedClassDeclaration;
import org.acm.seguin.parser.ast.ASTGenericNameList;
import org.acm.seguin.parser.ast.ASTClassOrInterfaceType;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class AtLeastOneConstructorRule extends AbstractRule {

    public Object visit(ASTUnmodifiedClassDeclaration node, Object data) {
        List constructors = new ArrayList();
        node.findChildrenOfType(ASTConstructorDeclaration.class, constructors, false);
        if (constructors.isEmpty()) {
           if (checkForSpecalCases(node)) {
               RuleContext ctx = (RuleContext) data;
               ctx.getReport().addRuleViolation(createRuleViolation(ctx, node.getBeginLine()));
           }
        }
        return super.visit(node, data);
    }
    
    private boolean checkForSpecalCases(ASTUnmodifiedClassDeclaration node) {
        List interfaces = new ArrayList();
        node.findChildrenOfType(ASTGenericNameList.class, interfaces, false);
        if (interfaces.size()==0) {
           return true;
        }
        ASTGenericNameList nameList = (ASTGenericNameList)interfaces.get(0);
        List interfaceNames = new ArrayList();
        nameList.findChildrenOfType(ASTClassOrInterfaceType.class, interfaceNames, false);
        for (Iterator i=interfaceNames.iterator(); i.hasNext(); ) {
           ASTClassOrInterfaceType type = (ASTClassOrInterfaceType)i.next();
           if (type.getImage().equals("Comparator")) {
              return false;
           } else if (type.getImage().equals("java.util.Comparator")) {
              return false;
           }
        }
        return true;
    }
}
