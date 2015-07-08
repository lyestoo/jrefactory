/*
 * User: frank
 * Date: Jun 21, 2002
 * Time: 11:26:34 AM
 */
package org.acm.seguin.pmd.rules;

import org.acm.seguin.pmd.AbstractRule;
import org.acm.seguin.pmd.RuleContext;
import org.acm.seguin.parser.ast.ASTMethodDeclarator;
import org.acm.seguin.parser.ast.ASTUnmodifiedClassDeclaration;
import org.acm.seguin.pmd.symboltable.VariableNameDeclaration;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class BeanMembersShouldSerializeRule extends AbstractRule {

    public Object visit(ASTUnmodifiedClassDeclaration node, Object data) {
       //System.err.println("BeanMembersShouldSerializeRule: "+node.dumpString("\r\n"));
      ArrayList methList = new ArrayList();
      node.findChildrenOfType(ASTMethodDeclarator.class, methList);

      ArrayList getSetMethList = new ArrayList();
      for (int i = 0; i < methList.size(); i++){
        ASTMethodDeclarator meth = (ASTMethodDeclarator)methList.get(i);
        String methName = meth.getImage();
        if (methName.startsWith("get") || methName.startsWith("set")){
          getSetMethList.add(meth);
        }
      }
      String[] methNameArray = new String[getSetMethList.size()];
      for (int i = 0; i < getSetMethList.size(); i++){
        ASTMethodDeclarator meth = (ASTMethodDeclarator)getSetMethList.get(i);
        String methName = meth.getImage();
        methNameArray[i] = methName;
      }

      Arrays.sort(methNameArray);
      //System.out.println("methNameArray="+java.util.Arrays.asList(methNameArray));

      for (Iterator i = node.getScope().getVariableDeclarations(true).keySet().iterator();i.hasNext();) {
            VariableNameDeclaration decl = (VariableNameDeclaration)i.next();
            if (decl.getAccessNodeParent().isTransient()){
             //System.err.println("It's Transient!");
              continue;
            }
            if (decl.getAccessNodeParent().isStatic()){
              //System.err.println("It's Static!");
              continue;
            }
            String varName = decl.getImage();
            String firstChar = varName.substring(0,1);
              //System.err.println("firstChar = " + firstChar);
              varName = firstChar.toUpperCase() + varName.substring(1,varName.length());
              //System.err.println("varName = " + varName);
            boolean hasGetMethod =false;
            if (Arrays.binarySearch(methNameArray,"get" + varName) >= 0 ){
              hasGetMethod = true;
            }
            boolean hasSetMethod = false;
            if (Arrays.binarySearch(methNameArray,"set" + varName) >= 0 ){
              hasSetMethod = true;
            }
            if (!hasGetMethod || !hasSetMethod) {
              //System.err.println("decl.getImage = "+decl.getImage());
              RuleContext ctx = (RuleContext)data;
              ctx.getReport().addRuleViolation(createRuleViolation(ctx, decl.getLine(), MessageFormat.format(getMessage(), new Object[] {decl.getImage()})));
            }
/*
            if (decl.getAccessNodeParent().isPrivate() && !decl.getImage().equals("serialVersionUID") && !decl.getImage().equals("serialPersistentFields")) {

              RuleContext ctx = (RuleContext)data;
                ctx.getReport().addRuleViolation(createRuleViolation(ctx, decl.getLine(), MessageFormat.format(getMessage(), new Object[] {decl.getImage()})));
            }*/

        }
        return super.visit(node, data);
    }


}
