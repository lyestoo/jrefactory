package org.acm.seguin.pmd.rules;

import org.acm.seguin.pmd.AbstractRule;
import org.acm.seguin.pmd.RuleContext;
import org.acm.seguin.parser.ast.ASTArguments;
import org.acm.seguin.parser.ast.ASTClassBody;
import org.acm.seguin.parser.ast.ASTCompilationUnit;
import org.acm.seguin.parser.ast.ASTInterfaceDeclaration;
import org.acm.seguin.parser.ast.ASTMethodDeclarator;
import org.acm.seguin.parser.ast.ASTName;
import org.acm.seguin.parser.ast.ASTPrimaryExpression;
import org.acm.seguin.parser.ast.ASTPrimaryPrefix;
import org.acm.seguin.parser.ast.ASTPrimarySuffix;
import org.acm.seguin.parser.ast.AccessNode;
import org.acm.seguin.parser.ast.SimpleNode;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class UnusedPrivateMethodRule extends AbstractRule {

    private Set privateMethodNodes = new HashSet();

    // TODO - What I need is a Visitor that does a breadth first search
    private boolean trollingForDeclarations;
    private int depth;

    // Skip interfaces because they have no implementation
    public Object visit(ASTInterfaceDeclaration node, Object data) {
        return data;
    }

    // Reset state when we leave an ASTCompilationUnit
    public Object visit(ASTCompilationUnit node, Object data) {
        depth = 0;
        super.visit(node, data);
        privateMethodNodes.clear();
        depth = 0;
        trollingForDeclarations = false;
        return data;
    }

    public Object visit(ASTClassBody node, Object data) {
        depth++;

        // first troll for declarations, but only in the top level class
        if (depth == 1) {
            trollingForDeclarations = true;
            super.visit(node, null);
            trollingForDeclarations = false;
        } else {
            trollingForDeclarations = false;
        }

        // troll for usages, regardless of depth
        super.visit(node, null);

        // if we're back at the top level class, harvest
        if (depth == 1) {
            RuleContext ctx = (RuleContext) data;
            harvestUnused(ctx);
        }

        depth--;
        return data;
    }

    //ASTMethodDeclarator
    // FormalParameters
    //  FormalParameter
    //  FormalParameter
    public Object visit(ASTMethodDeclarator node, Object data) {
        if (!trollingForDeclarations) {
            return super.visit(node, data);
        }

        AccessNode parent = (AccessNode) node.jjtGetParent();
        if (!parent.isPrivate()) {
            return super.visit(node, data);
        }
        // exclude these serializable things
        if (node.getImage().equals("readObject") || node.getImage().equals("writeObject") || node.getImage().equals("readResolve")) {
            return super.visit(node, data);
        }
        privateMethodNodes.add(node);
        return super.visit(node, data);
    }

    //PrimarySuffix
    // Arguments
    //  ArgumentList
    //   Expression
    //   Expression
    public Object visit(ASTPrimarySuffix node, Object data) {
        if (!trollingForDeclarations && (node.jjtGetParent() instanceof ASTPrimaryExpression) && !(node.getImage().equals("")) ) {  // MRA
           if (node.jjtGetNumChildren() > 0) {
                ASTArguments args = (ASTArguments) node.jjtGetFirstChild();
                removeIfUsed(node.getImage(), args.getArgumentCount());
                return super.visit(node, data);
            }
            // to handle this.foo()
            //PrimaryExpression
            // PrimaryPrefix
            // PrimarySuffix <-- this node has "foo"
            // PrimarySuffix <-- this node has null
            //  Arguments
            ASTPrimaryExpression parent = (ASTPrimaryExpression) node.jjtGetParent();
            int pointer = 0;
            while (true) {
                if (parent.jjtGetChild(pointer).equals(node)) {
                    break;
                }
                pointer++;
            }
            // now move to the next PrimarySuffix and get the number of arguments
            pointer++;
            // this.foo = foo;
            // yields this:
            // PrimaryExpression
            //  PrimaryPrefix
            //  PrimarySuffix
            // so we check for that
            if (parent.jjtGetNumChildren() <= pointer) {
                return super.visit(node, data);
            }
            if (!(parent.jjtGetChild(pointer) instanceof ASTPrimarySuffix)) {
                return super.visit(node, data);
            }
            ASTPrimarySuffix actualMethodNode = (ASTPrimarySuffix) parent.jjtGetChild(pointer);
            // when does this happen?
            if (actualMethodNode.jjtGetNumChildren() == 0 || !(actualMethodNode.jjtGetFirstChild() instanceof ASTArguments)) {
                return super.visit(node, data);
            }
            ASTArguments args = (ASTArguments) actualMethodNode.jjtGetFirstChild();
            removeIfUsed(node.getImage(), args.getArgumentCount());
            // what about Outer.this.foo()?
        }
        return super.visit(node, data);
    }

    //PrimaryExpression
    // PrimaryPrefix
    //  Name
    // PrimarySuffix
    //  Arguments
    public Object visit(ASTName node, Object data) {
        if (!trollingForDeclarations && (node.jjtGetParent() instanceof ASTPrimaryPrefix)) {
            ASTPrimaryExpression primaryExpression = (ASTPrimaryExpression) node.jjtGetParent().jjtGetParent();
            if (primaryExpression.jjtGetNumChildren() > 1) {
                ASTPrimarySuffix primarySuffix = (ASTPrimarySuffix) primaryExpression.jjtGetChild(1);
                if (primarySuffix.jjtGetNumChildren() > 0 && (primarySuffix.jjtGetFirstChild() instanceof ASTArguments)) {
                    ASTArguments arguments = (ASTArguments) primarySuffix.jjtGetFirstChild();
                    removeIfUsed(node.getImage(), arguments.getArgumentCount());
                }
            }
        }
        return super.visit(node, data);
    }

    private void removeIfUsed(String nodeImage, int args) {
        String img = (nodeImage.indexOf('.') == -1) ? nodeImage : nodeImage.substring(nodeImage.indexOf('.') + 1, nodeImage.length());
        for (Iterator i = privateMethodNodes.iterator(); i.hasNext();) {
            ASTMethodDeclarator methodNode = (ASTMethodDeclarator) i.next();
            // are name and number of parameters the same?
            if (methodNode.getImage().equals(img) && methodNode.getParameterCount() == args) {
                // should check parameter types here, this misses some unused methods
                i.remove();
            }
        }
    }

    private void harvestUnused(RuleContext ctx) {
        for (Iterator i = privateMethodNodes.iterator(); i.hasNext();) {
            SimpleNode node = (SimpleNode) i.next();
            ctx.getReport().addRuleViolation(createRuleViolation(ctx, node.getBeginLine(), MessageFormat.format(getMessage(), new Object[]{node.getImage()})));
        }
    }

    /*
    TODO this uses the symbol table
        public Object visit(ASTUnmodifiedClassDeclaration node, Object data) {
            for (Iterator i = node.getScope().getUnusedMethodDeclarations();i.hasNext();) {
                VariableNameDeclaration decl = (VariableNameDeclaration)i.next();

                // exclude non-private methods and serializable methods
                if (!decl.getAccessNodeParent().isPrivate() || decl.getImage().equals("readObject") || decl.getImage().equals("writeObject")|| decl.getImage().equals("readResolve")) {
                    continue;
                }

                RuleContext ctx = (RuleContext)data;
                ctx.getReport().addRuleViolation(createRuleViolation(ctx, decl.getNode().getBeginLine(), MessageFormat.format(getMessage(), new Object[] {decl.getNode().getImage()})));
            }
            return super.visit(node, data);
        }

    */
}
