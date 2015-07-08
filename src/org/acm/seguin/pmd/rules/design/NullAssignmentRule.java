/**
 * <copyright>
 *  Copyright 1997-2002 BBNT Solutions, LLC
 *  under sponsorship of the Defense Advanced Research Projects Agency (DARPA).
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the Cougaar Open Source License as published by
 *  DARPA on the Cougaar Open Source Website (www.cougaar.org).
 *
 *  THE COUGAAR SOFTWARE AND ANY DERIVATIVE SUPPLIED BY LICENSOR IS
 *  PROVIDED 'AS IS' WITHOUT WARRANTIES OF ANY KIND, WHETHER EXPRESS OR
 *  IMPLIED, INCLUDING (BUT NOT LIMITED TO) ALL IMPLIED WARRANTIES OF
 *  MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, AND WITHOUT
 *  ANY WARRANTIES AS TO NON-INFRINGEMENT.  IN NO EVENT SHALL COPYRIGHT
 *  HOLDER BE LIABLE FOR ANY DIRECT, SPECIAL, INDIRECT OR CONSEQUENTIAL
 *  DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE OF DATA OR PROFITS,
 *  TORTIOUS CONDUCT, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 *  PERFORMANCE OF THE COUGAAR SOFTWARE.
 * </copyright>
 *
 * Created on Dec 13, 2002
 */
package org.acm.seguin.pmd.rules.design;

import org.acm.seguin.pmd.AbstractRule;
import org.acm.seguin.pmd.RuleContext;
import org.acm.seguin.parser.ast.ASTAssignmentOperator;
import org.acm.seguin.parser.ast.ASTNullLiteral;
import org.acm.seguin.parser.ast.ASTStatementExpression;
import org.acm.seguin.parser.ast.ASTCompilationUnit;
import org.acm.seguin.parser.ast.SimpleNode;

import org.acm.seguin.parser.ast.ASTLiteral;

/**
 * @author dpeugh
 *
 * This checks for excessive Null Assignments.
 *
 * For instance:
 *
 * public void foo() {
 *   Object x = null; // OK
 *   // Some stuff
 *   x = new Object(); // Also OK
 *   // Some more stuff
 *   x = null; // BAD
 * }
 */

public class NullAssignmentRule extends AbstractRule {

    public Object visit(ASTStatementExpression expr, Object data) {
        if (expr.jjtGetNumChildren() <= 2) {
            return expr.childrenAccept(this, data);
        }

        if (expr.jjtGetChild(1) instanceof ASTAssignmentOperator) {
            SimpleNode curr = (SimpleNode) expr.jjtGetChild(2);

            for (int i=0; i<19; i++) {
               if (curr.jjtGetNumChildren()==0) {
                  return data;
               }
               curr = (SimpleNode)curr.jjtGetFirstChild();
               if (curr==null) {
                  return data;
               }
               if (curr instanceof ASTNullLiteral) {
                   RuleContext ctx = (RuleContext) data;
                   ctx.getReport().addRuleViolation(createRuleViolation(ctx, expr.getBeginLine()));
               }
            }

            return data;
        } else {
            return expr.childrenAccept(this, data);
        }
    }
}
