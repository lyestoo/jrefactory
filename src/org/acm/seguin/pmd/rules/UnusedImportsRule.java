package org.acm.seguin.pmd.rules;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.acm.seguin.parser.ast.ASTClassOrInterfaceType;
import org.acm.seguin.parser.ast.ASTCompilationUnit;
import org.acm.seguin.parser.ast.ASTImportDeclaration;
import org.acm.seguin.parser.ast.ASTName;

import org.acm.seguin.pmd.AbstractRule;
import org.acm.seguin.pmd.RuleContext;

/**
 *  Description of the Class
 *
 * @author    Chris Seguin
 */
public class UnusedImportsRule extends AbstractRule {

   private Set imports = new HashSet();

   /**
    *  Description of the Method
    *
    * @param  node  Description of Parameter
    * @param  data  Description of Parameter
    * @return       Description of the Returned Value
    */
   public Object visit(ASTCompilationUnit node, Object data) {
      imports.clear();

      super.visit(node, data);

      RuleContext ctx = (RuleContext)data;
      for (Iterator i = imports.iterator(); i.hasNext(); ) {
         ImportWrapper wrapper = (ImportWrapper)i.next();
         String msg = MessageFormat.format(getMessage(), new Object[]{wrapper.getName()});
         ctx.getReport().addRuleViolation(createRuleViolation(ctx, wrapper.getLine(), msg));
      }
      return data;
   }

   /**
    *  Description of the Method
    *
    * @param  node  Description of Parameter
    * @param  data  Description of Parameter
    * @return       Description of the Returned Value
    */
   public Object visit(ASTImportDeclaration node, Object data) {
      if (!node.isImportOnDemand()) {
         ASTName importedType = (ASTName)node.jjtGetFirstChild();
         String className;
         if (importedType.getImage().indexOf('.') != -1) {
            int lastDot = importedType.getImage().lastIndexOf('.') + 1;
            className = importedType.getImage().substring(lastDot);
         } else {
            className = importedType.getImage();
         }
         ImportWrapper wrapper = new ImportWrapper(className, node.getBeginLine());
         imports.add(wrapper);
      }

      return data;
   }

   /**
    *  Description of the Method
    *
    * @param  node  Description of Parameter
    * @param  data  Description of Parameter
    * @return       Description of the Returned Value
    */
   public Object visit(ASTName node, Object data) {
      String name;
      if (node.getImage().indexOf('.') == -1) {
         name = node.getImage();
      } else {
         name = node.getImage().substring(0, node.getImage().indexOf('.'));
      }
      ImportWrapper candidate = new ImportWrapper(name, -1);
      if (imports.contains(candidate)) {
         imports.remove(candidate);
      }
      return data;
   }

   /**
    *  Description of the Method
    *
    * @param  node  Description of Parameter
    * @param  data  Description of Parameter
    * @return       Description of the Returned Value
    */
   public Object visit(ASTClassOrInterfaceType node, Object data) {
      String name;
      if (node.getImage().indexOf('.') == -1) {
         name = node.getImage();
      } else {
         name = node.getImage().substring(0, node.getImage().indexOf('.'));
      }
      ImportWrapper candidate = new ImportWrapper(name, -1);
      if (imports.contains(candidate)) {
         imports.remove(candidate);
      }
      return data;
   }

}

