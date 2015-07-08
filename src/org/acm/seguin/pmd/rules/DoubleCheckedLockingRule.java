package org.acm.seguin.pmd.rules;

import org.acm.seguin.pmd.RuleContext;
import org.acm.seguin.parser.ast.ASTAssignmentOperator;
import org.acm.seguin.parser.ast.ASTClassDeclaration;
import org.acm.seguin.parser.ast.ASTIfStatement;
import org.acm.seguin.parser.ast.ASTInterfaceDeclaration;
import org.acm.seguin.parser.ast.ASTLiteral;
import org.acm.seguin.parser.ast.ASTMethodDeclaration;
import org.acm.seguin.parser.ast.ASTReferenceType;
import org.acm.seguin.parser.ast.ASTNestedClassDeclaration;
import org.acm.seguin.parser.ast.ASTNestedInterfaceDeclaration;
import org.acm.seguin.parser.ast.ASTNullLiteral;
import org.acm.seguin.parser.ast.ASTPrimaryExpression;
import org.acm.seguin.parser.ast.ASTPrimaryPrefix;
import org.acm.seguin.parser.ast.ASTResultType;
import org.acm.seguin.parser.ast.ASTReturnStatement;
import org.acm.seguin.parser.ast.ASTStatementExpression;
import org.acm.seguin.parser.ast.ASTSynchronizedStatement;
import org.acm.seguin.parser.ast.ASTName;
import org.acm.seguin.parser.ast.ASTType;
import org.acm.seguin.parser.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * void method() {
 *    if(x == null) {
 *        synchronize(this){
 *            if(x == null) {
 *                x = new | method();
 *            }
 *         }
 *  }
 *  1.  The error is when one uses the value assigned within a synchronized
 *      section, outside of a synchronized section.
 *      if(x == null) is outside of synchronized section
 *      x = new | method();
 *
 *
 * Very very specific check for double checked locking.
 *
 * @author  CL Gilbert (dnoyeb@users.sourceforge.net)
 */
public class DoubleCheckedLockingRule extends org.acm.seguin.pmd.AbstractRule {

    private boolean interfaceSkipper;

    public Object visit(ASTMethodDeclaration node, Object data) {
        if (interfaceSkipper == true) {//skip methods in interfaces
            return super.visit(node, data);
        }
        ASTResultType rt = (ASTResultType) node.jjtGetFirstChild();
        if (rt.isVoid() == true) {
            return super.visit(node, data);
        }
        ASTType t = (ASTType) rt.jjtGetFirstChild();
        if (t.jjtGetNumChildren() == 0 || !(t.jjtGetFirstChild() instanceof ASTReferenceType)) {
            return super.visit(node, data);
        }
        String returnVariableName = null;
        List finder = new ArrayList();
        GET_RETURN_VARIABLE_NAME:{
            node.findChildrenOfType(ASTReturnStatement.class, finder, true);
            if (finder.size() != 1) {
                return super.visit(node, data);
            }
            ASTReturnStatement rs = (ASTReturnStatement) finder.get(0);//EXPLODES IF THE CLASS IS AN INTERFACE SINCE NO RETURN

            finder.clear();
            rs.findChildrenOfType(ASTPrimaryExpression.class, finder, true);
            ASTPrimaryExpression ape = (ASTPrimaryExpression) finder.get(0);
            Node lastChild = ape.jjtGetChild(ape.jjtGetNumChildren() - 1);
            if (lastChild instanceof ASTPrimaryPrefix) {
                returnVariableName = getNameFromPrimaryPrefix((ASTPrimaryPrefix) lastChild);
            }
            if (returnVariableName == null) {
                return super.visit(node, data);
            }
        }
        CHECK_OUTER_IF:{
            finder.clear();
            node.findChildrenOfType(ASTIfStatement.class, finder, true);
            if (finder.size() == 2) {
                ASTIfStatement is = (ASTIfStatement) finder.get(0);
                if (ifVerify(is, returnVariableName)) {
                    //find synchronize
                    finder.clear();
                    is.findChildrenOfType(ASTSynchronizedStatement.class, finder, true);
                    if (finder.size() == 1) {
                        ASTSynchronizedStatement ss = (ASTSynchronizedStatement) finder.get(0);
                        finder.clear();
                        ss.findChildrenOfType(ASTIfStatement.class, finder, true);
                        if (finder.size() == 1) {
                            ASTIfStatement is2 = (ASTIfStatement) finder.get(0);
                            if (ifVerify(is2, returnVariableName)) {
                                finder.clear();
                                is2.findChildrenOfType(ASTStatementExpression.class, finder, true);
                                if (finder.size() == 1) {
                                    ASTStatementExpression se = (ASTStatementExpression) finder.get(0);
                                    if (se.jjtGetNumChildren() == 3) { //primaryExpression, AssignmentOperator, Expression
                                        if (se.jjtGetFirstChild() instanceof ASTPrimaryExpression) {
                                            ASTPrimaryExpression pe = (ASTPrimaryExpression) se.jjtGetFirstChild();
                                            if (matchName(pe, returnVariableName)) {
                                                if (se.jjtGetChild(1) instanceof ASTAssignmentOperator) {
                                                    RuleContext ctx = (RuleContext) data;
                                                    ctx.getReport().addRuleViolation(createRuleViolation(ctx, node.getBeginLine()));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return super.visit(node, data);
    }

    private boolean ifVerify(ASTIfStatement is, String varname) {
        List finder = new ArrayList();
        is.findChildrenOfType(ASTPrimaryExpression.class, finder, true);
        if (finder.size() > 1) {
            ASTPrimaryExpression apeLeft = (ASTPrimaryExpression) finder.get(0);
            if (matchName(apeLeft, varname)) {
                ASTPrimaryExpression apeRight = (ASTPrimaryExpression) finder.get(1);
                if ((apeRight.jjtGetNumChildren() == 1) && (apeRight.jjtGetFirstChild() instanceof ASTPrimaryPrefix)) {
                    ASTPrimaryPrefix pp2 = (ASTPrimaryPrefix) apeRight.jjtGetFirstChild();
                    if ((pp2.jjtGetNumChildren() == 1) && (pp2.jjtGetFirstChild() instanceof ASTLiteral)) {
                        ASTLiteral lit = (ASTLiteral) pp2.jjtGetFirstChild();
                        if ((lit.jjtGetNumChildren() == 1) && (lit.jjtGetFirstChild() instanceof ASTNullLiteral)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public Object visit(ASTClassDeclaration node, Object data) {
        boolean temp = interfaceSkipper;
        interfaceSkipper = false;
        //		String className = ((ASTUnmodifiedClassDeclaration)node.jjtGetFirstChild()).getImage();
        //		System.out.println("classname = " + className);
        Object o = super.visit(node, data);
        interfaceSkipper = temp;
        return o;
    }

    public Object visit(ASTNestedClassDeclaration node, Object data) {
        boolean temp = interfaceSkipper;
        interfaceSkipper = false;
        //		String className = ((ASTUnmodifiedNestedClassDeclaration)node.jjtGetFirstChild()).getImage();
        //		System.out.println("classname = " + className);
        Object o = super.visit(node, data);
        interfaceSkipper = temp;
        return o;
    }

    public Object visit(ASTInterfaceDeclaration node, Object data) {
        boolean temp = interfaceSkipper;
        interfaceSkipper = true;
        Object o = super.visit(node, data);
        interfaceSkipper = temp;
        return o;
    }

    public Object visit(ASTNestedInterfaceDeclaration node, Object data) {
        boolean temp = interfaceSkipper;
        interfaceSkipper = true;
        Object o = super.visit(node, data);
        interfaceSkipper = temp;
        return o;
    }

    public boolean matchName(ASTPrimaryExpression ape, String name) {
        if ((ape.jjtGetNumChildren() == 1) && (ape.jjtGetFirstChild() instanceof ASTPrimaryPrefix)) {
            ASTPrimaryPrefix pp = (ASTPrimaryPrefix) ape.jjtGetFirstChild();
            String name2 = getNameFromPrimaryPrefix(pp);
            if (name2 != null && name2.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public String getNameFromPrimaryPrefix(ASTPrimaryPrefix pp) {
        if ((pp.jjtGetNumChildren() == 1) && (pp.jjtGetFirstChild() instanceof ASTName)) {
            String name2 = ((ASTName) pp.jjtGetFirstChild()).getImage();
            return name2;
        }
        return null;
    }
    //Testing Section
    //    public Object visit(ASTCompilationUnit node, Object data) {
    //		interfaceSkipper = false; //safety
    //		try {
    //			return super.visit(node,data);
    //		}
    //		catch(Exception e){
    //			e.printStackTrace();
    //			throw new RuntimeException(e.getMessage());
    //		}
    //    }
    //	public Object visit(ASTMethodDeclarator node, Object data) {
    //		System.out.println("method = " + node.getImage());
    //		return super.visit(node,data);
    //	}
    //
    //	public Object visit(ASTPackageDeclaration node, Object data){
    //		String name = ((ASTName)node.jjtGetFirstChild()).getImage();
    //		System.out.println("package = " + name);
    //		return super.visit(node, data);
    //	}
}