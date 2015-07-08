package org.acm.seguin.pmd.symboltable;

import org.acm.seguin.parser.ast.ASTFormalParameter;
import org.acm.seguin.parser.ast.ASTFormalParameters;
import org.acm.seguin.parser.ast.ASTMethodDeclarator;
import org.acm.seguin.parser.ast.SimpleNode;

public class MethodNameDeclaration extends AbstractNameDeclaration implements NameDeclaration {

    public MethodNameDeclaration(ASTMethodDeclarator node) {
        super(node);
    }

    public boolean equals(Object o) {
        MethodNameDeclaration otherMethodDecl = (MethodNameDeclaration) o;

        // compare method name
        if (!otherMethodDecl.node.getImage().equals(node.getImage())) {
            return false;
        }

        // compare parameter count - this catches the case where there are no params, too
        if (((ASTMethodDeclarator) (otherMethodDecl.node)).getParameterCount() != ((ASTMethodDeclarator) node).getParameterCount()) {
            return false;
        }

        // compare parameter types
        ASTFormalParameters myParams = (ASTFormalParameters) node.jjtGetFirstChild();
        ASTFormalParameters otherParams = (ASTFormalParameters) otherMethodDecl.node.jjtGetFirstChild();
        for (int i = 0; i < ((ASTMethodDeclarator) node).getParameterCount(); i++) {
            ASTFormalParameter myParam = (ASTFormalParameter) myParams.jjtGetChild(i);
            ASTFormalParameter otherParam = (ASTFormalParameter) otherParams.jjtGetChild(i);
            SimpleNode myTypeNode = (SimpleNode) myParam.jjtGetFirstChild().jjtGetFirstChild();
            SimpleNode otherTypeNode = (SimpleNode) otherParam.jjtGetFirstChild().jjtGetFirstChild();

            // simple comparison of type images
            // this can be fooled by one method using "String"
            // and the other method using "java.lang.String"
            // once we get real types in here that should get fixed
            if (!myTypeNode.getImage().equals(otherTypeNode.getImage())) {
                return false;
            }

            // if type is ASTPrimitiveType and is an array, make sure the other one is also
        }
        return true;
    }

    public int hashCode() {
        return node.getImage().hashCode() + ((ASTMethodDeclarator) node).getParameterCount();
    }

    public String toString() {
        return "Method " + node.getImage() + ":" + node.getBeginLine();
    }
}
