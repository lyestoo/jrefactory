package org.acm.seguin.pmd.rules;

import org.acm.seguin.pmd.AbstractRule;
import org.acm.seguin.pmd.RuleContext;
import org.acm.seguin.parser.ast.ASTClassDeclaration;
import org.acm.seguin.parser.ast.ASTCompilationUnit;
import org.acm.seguin.parser.ast.ASTFieldDeclaration;
import org.acm.seguin.parser.ast.ASTFormalParameter;
import org.acm.seguin.parser.ast.ASTLocalVariableDeclaration;
import org.acm.seguin.parser.ast.ASTReferenceType;
import org.acm.seguin.parser.ast.ASTResultType;
import org.acm.seguin.parser.ast.ASTType;
import org.acm.seguin.parser.ast.SimpleNode;

import java.util.HashSet;
import java.util.Set;


/**
 *  CouplingBetweenObjectsRule attempts to capture all unique Class attributes, local variables, and return types to
 *  determine how many objects a class is coupled to. This is only a guage and isn't a hard and fast rule. The threshold
 *  value is configurable and should be determined accordingly
 *
 *@author    aglover
 *@since     Feb 20, 2003
 */
public class CouplingBetweenObjectsRule extends AbstractRule {

   private String className;
   private int couplingCount;
   private Set typesFoundSoFar;
   private boolean withinClass = false;


   /**
    *  handles the source file
    *
    *@param  cu                  Description of Parameter
    *@param  data                Description of Parameter
    *@return                     Object
    */
   public Object visit(ASTCompilationUnit cu, Object data) {
      this.typesFoundSoFar = new HashSet();
      this.couplingCount = 0;

      Object returnObj = cu.childrenAccept(this, data);

      if (this.couplingCount > getIntProperty("threshold")) {
         RuleContext ctx = (RuleContext) data;
         ctx.getReport().addRuleViolation(createRuleViolation(ctx, cu.getBeginLine(), "A value of " + this.couplingCount + " may denote a high amount of coupling within the class"));
      }

      return returnObj;
   }


   /**
    *  handles class declaration. I need this to capture class name. I think there is probably a better way to capture
    *  it; however, I don't know the framework well enough yet...
    *
    *@param  node                 Description of Parameter
    *@param  data                 Description of Parameter
    *@return                      Object
    */
   public Object visit(ASTClassDeclaration node, Object data) {
      boolean oldWithinClass = withinClass;
      withinClass = true;
      SimpleNode firstStmt = (SimpleNode) node.jjtGetFirstChild();
      this.className = firstStmt.getImage();
      Object d = super.visit(node, data);
      withinClass = oldWithinClass;
      return d;
   }


   /**
    *  handles a return type of a method
    *
    *@param  node           Description of Parameter
    *@param  data           Description of Parameter
    *@return                Object
    */
   public Object visit(ASTResultType node, Object data) {
      if (withinClass) {
         for (int x = 0; x < node.jjtGetNumChildren(); x++) {
            SimpleNode tNode = (SimpleNode) node.jjtGetChild(x);
            if (tNode instanceof ASTType) {
               SimpleNode nameNode = (SimpleNode) tNode.jjtGetFirstChild();
               if (nameNode instanceof ASTReferenceType) {
                  nameNode = (SimpleNode) nameNode.jjtGetFirstChild();
               }
               this.checkVariableType(nameNode.getImage());
            }
         }
      }
      return super.visit(node, data);
   }


   /**
    *  handles a local variable found in a method block
    *
    *@param  node                         Description of Parameter
    *@param  data                         Description of Parameter
    *@return                              Object
    */
   public Object visit(ASTLocalVariableDeclaration node, Object data) {
      if (withinClass) {
         this.handleASTTypeChildren(node);
      }
      return super.visit(node, data);
   }


   /**
    *  handles a method parameter
    *
    *@param  node                Description of Parameter
    *@param  data                Description of Parameter
    *@return                     Object
    */
   public Object visit(ASTFormalParameter node, Object data) {
      if (withinClass) {
         this.handleASTTypeChildren(node);
      }
      return super.visit(node, data);
   }


   /**
    *  handles a field declaration - i.e. an instance variable. Method doesn't care if variable is public/private/etc
    *
    *@param  node                 Description of Parameter
    *@param  data                 Description of Parameter
    *@return                      Object
    */
   public Object visit(ASTFieldDeclaration node, Object data) {
      if (withinClass) {
         for (int x = 0; x < node.jjtGetNumChildren(); ++x) {
            SimpleNode firstStmt = (SimpleNode) node.jjtGetChild(x);
            if (firstStmt instanceof ASTType) {
               ASTType tp = (ASTType) firstStmt;
               SimpleNode nd = (SimpleNode) tp.jjtGetFirstChild();
               if (nd instanceof ASTReferenceType) {
                  nd = (SimpleNode) nd.jjtGetFirstChild();
               }
               this.checkVariableType(nd.getImage());
            }
         }
      }
      return super.visit(node, data);
   }


   /**
    *  convience method to handle hiearchy. This is probably too much work and will go away once I figure out the
    *  framework
    *
    *@param  node  Description of Parameter
    */
   private void handleASTTypeChildren(SimpleNode node) {
      for (int x = 0; x < node.jjtGetNumChildren(); x++) {
         SimpleNode sNode = (SimpleNode) node.jjtGetChild(x);
         if (sNode instanceof ASTType) {
            SimpleNode nameNode = (SimpleNode) sNode.jjtGetFirstChild();
            if (nameNode instanceof ASTReferenceType) {
               nameNode = (SimpleNode) nameNode.jjtGetFirstChild();
            }
            this.checkVariableType(nameNode.getImage());
         }
      }
   }


   /**
    *  performs a check on the variable and updates the couter. Counter is instance for a class and is reset upon new
    *  class scan.
    *
    *@param  variableType  Description of Parameter
    */
   private void checkVariableType(String variableType) {
      //if the field is of any type other than the class type
      //increment the count
      if (!this.className.equals(variableType) && (!this.filterTypes(variableType)) && !this.typesFoundSoFar.contains(variableType)) {
         this.couplingCount++;
         this.typesFoundSoFar.add(variableType);
      }
   }


   /**
    *  Filters variable type - we don't want primatives, wrappers, strings, etc. This needs more work. I'd like to
    *  filter out super types and perhaps interfaces
    *
    *@param  variableType  Description of Parameter
    *@return               boolean true if variableType is not what we care about
    */
   private boolean filterTypes(String variableType) {
      return variableType.startsWith("java.lang.") || (variableType.equals("String")) || filterPrimativesAndWrappers(variableType);
   }


   /**
    *@param  variableType  Description of Parameter
    *@return               boolean true if variableType is a primative or wrapper
    */
   private boolean filterPrimativesAndWrappers(String variableType) {
      return (variableType.equals("int") || variableType.equals("Integer") || variableType.equals("char") || variableType.equals("Character") || variableType.equalsIgnoreCase("double") || variableType.equalsIgnoreCase("long") || variableType.equalsIgnoreCase("short") || variableType.equalsIgnoreCase("float") || variableType.equalsIgnoreCase("byte") || variableType.equalsIgnoreCase("boolean"));
   }
}

