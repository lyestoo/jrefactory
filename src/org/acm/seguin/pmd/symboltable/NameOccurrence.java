package org.acm.seguin.pmd.symboltable;

import org.acm.seguin.parser.ast.ASTAssignmentOperator;
import org.acm.seguin.parser.ast.ASTPrimaryExpression;
import org.acm.seguin.parser.ast.ASTPrimarySuffix;
import org.acm.seguin.parser.ast.ASTExpression;
import org.acm.seguin.parser.ast.SimpleNode;

public class NameOccurrence {

    private SimpleNode location;
    private String image;
    private NameOccurrence qualifiedName;
    private boolean isMethodOrConstructorInvocation;

    public NameOccurrence(SimpleNode location, String image) {
        this.location = location;
        this.image = image;
    }

    public void setIsMethodOrConstructorInvocation() {
        isMethodOrConstructorInvocation = true;
    }

    public boolean isMethodOrConstructorInvocation() {
        return isMethodOrConstructorInvocation;
    }

    public void setNameWhichThisQualifies(NameOccurrence qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public NameOccurrence getNameForWhichThisIsAQualifier() {
        return qualifiedName;
    }

    public SimpleNode getLocation() {
      return location;
    }

    public boolean isOnLeftHandSide() {
        SimpleNode statementExpression;
        if (location.jjtGetParent() instanceof ASTPrimaryExpression) {
            statementExpression = (SimpleNode) location.jjtGetParent().jjtGetParent();
        } else if (location.jjtGetParent().jjtGetParent() instanceof ASTPrimaryExpression) {
            statementExpression = (SimpleNode) location.jjtGetParent().jjtGetParent().jjtGetParent();
        } else {
            throw new RuntimeException("Found a NameOccurrence that didn't have an ASTPrimary Expression as parent or grandparent.  Parent = " + location.jjtGetParent() + " and grandparent = " + location.jjtGetParent().jjtGetParent());
        }

        return statementExpression.jjtGetNumChildren() > 1 && statementExpression.jjtGetChild(1) instanceof ASTAssignmentOperator;
    }

    public boolean isArrayAccess() {
        SimpleNode primaryExpression;
        if (location.jjtGetParent() instanceof ASTPrimaryExpression) {
            primaryExpression = (SimpleNode) location.jjtGetParent();
        } else if (location.jjtGetParent().jjtGetParent() instanceof ASTPrimaryExpression) {
            primaryExpression = (SimpleNode) location.jjtGetParent().jjtGetParent();
        } else {
            throw new RuntimeException("Found a NameOccurrence that didn't have an ASTPrimary Expression as parent or grandparent.  Parent = " + location.jjtGetParent() + " and grandparent = " + location.jjtGetParent().jjtGetParent());
        }

        if (primaryExpression.jjtGetNumChildren() > 1 && primaryExpression.jjtGetChild(1) instanceof ASTPrimarySuffix) {
           ASTPrimarySuffix suffix = (ASTPrimarySuffix)primaryExpression.jjtGetChild(1);
           if (suffix.jjtGetNumChildren() > 0 && suffix.jjtGetChild(0) instanceof ASTExpression) {
              return true;
           }
        }
        return false;
    }

    public Scope getScope() {
        return location.getScope();
    }


    public int getBeginLine() {
        return location.getBeginLine();
    }

    public boolean isThisOrSuper() {
        return image.equals("this") || image.equals("super");
    }

    public boolean equals(Object o) {
        NameOccurrence n = (NameOccurrence) o;
        return n.getImage().equals(getImage());
    }

    public String getImage() {
        return image;
    }

    public int hashCode() {
        return getImage().hashCode();
    }

    public String toString() {
        return getImage() + ":" + location.getBeginLine() + ":" + location.getClass();
    }
}
