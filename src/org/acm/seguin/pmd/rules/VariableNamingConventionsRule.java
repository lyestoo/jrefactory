package org.acm.seguin.pmd.rules;

import org.acm.seguin.pmd.AbstractRule;
import org.acm.seguin.pmd.RuleContext;
import org.acm.seguin.parser.ast.ASTFieldDeclaration;
import org.acm.seguin.parser.ast.ASTLocalVariableDeclaration;
import org.acm.seguin.parser.ast.ASTReferenceType;
import org.acm.seguin.parser.ast.ASTPrimitiveType;
import org.acm.seguin.parser.ast.ASTType;
import org.acm.seguin.parser.ast.ASTVariableDeclarator;
import org.acm.seguin.parser.ast.ASTVariableDeclaratorId;
import org.acm.seguin.parser.ast.AccessNode;
import org.acm.seguin.parser.Node;

public class VariableNamingConventionsRule extends AbstractRule {

  public Object visit(ASTLocalVariableDeclaration node, Object data) {
    return checkNames(node, data);
  }

  public Object visit(ASTFieldDeclaration node, Object data) {
    return checkNames(node, data);
  }

  public Object checkNames(Node node, Object data) {

    boolean isFinal = false;
    if (node instanceof AccessNode) {
      isFinal = ((AccessNode)node).isFinal();
    }

    ASTType childNodeType = (ASTType)node.jjtGetChild(0);
    String varType = "";
    if (childNodeType.jjtGetChild(0)instanceof ASTReferenceType ) {
       varType = ((ASTReferenceType)childNodeType.jjtGetChild(0)).getImage();
    } else if (childNodeType.jjtGetChild(0) instanceof ASTPrimitiveType) {
       varType = ((ASTPrimitiveType)childNodeType.jjtGetChild(0)).getImage();
    }
      if (varType != null && varType.length() > 0) {
        //Get the variable name
        ASTVariableDeclarator childNodeName = (ASTVariableDeclarator)node.jjtGetChild(1);
        ASTVariableDeclaratorId childNodeId = (ASTVariableDeclaratorId)childNodeName.jjtGetChild(0);
        String varName = childNodeId.getImage();


        if (isFinal) {
          if (!varName.equals(varName.toUpperCase())) {
            String msg = "Variables that are final should be in all caps.";
            RuleContext ctx = (RuleContext)data;
            ctx.getReport().addRuleViolation(createRuleViolation(ctx, childNodeName.getBeginLine(), msg));

          }
        } else {
          if (varName.indexOf("_") >= 0) {
            String msg = "Variables that are not final should not contain underscores.";
            RuleContext ctx = (RuleContext)data;
            ctx.getReport().addRuleViolation(createRuleViolation(ctx, childNodeName.getBeginLine(), msg));
          }
          if (Character.isUpperCase(varName.charAt(0))) {
            String msg = "Variables should start with a lowercase character";
            RuleContext ctx = (RuleContext)data;
            ctx.getReport().addRuleViolation(createRuleViolation(ctx, childNodeName.getBeginLine(), msg));
          }

        }
      }
    return data;
  }
}
