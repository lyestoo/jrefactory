/* ====================================================================
 * The JRefactory License, Version 1.0
 *
 * Copyright (c) 2001 JRefactory.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        JRefactory (http://www.sourceforge.org/projects/jrefactory)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "JRefactory" must not be used to endorse or promote
 *    products derived from this software without prior written
 *    permission. For written permission, please contact seguin@acm.org.
 *
 * 5. Products derived from this software may not be called "JRefactory",
 *    nor may "JRefactory" appear in their name, without prior written
 *    permission of Chris Seguin.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE CHRIS SEGUIN OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of JRefactory.  For more information on
 * JRefactory, please see
 * <http://www.sourceforge.org/projects/jrefactory>.
 */
package org.acm.seguin.tools.international;

import org.acm.seguin.parser.ast.ASTLiteral;
import org.acm.seguin.parser.ast.ASTPrimaryExpression;
import org.acm.seguin.parser.ast.ASTPrimaryPrefix;
import org.acm.seguin.parser.ast.ASTName;
import org.acm.seguin.parser.ChildrenVisitor;

/**
 *  Creates a list of strings in the directory that aren't used for internal
 *  information.
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public class StringListVisitor extends ChildrenVisitor {
    /**
     *  Prints out the literal if it is a string literal
     *
     *@param  node  The node we are visiting
     *@param  data  The rename type data
     *@return       The rename type data
     */
    public Object visit(ASTLiteral node, Object data) {
        String name = node.getName();
        if ((name != null) && (name.length() > 0) && (name.charAt(0) == '\"') && !name.equals("\"\"")) {
            System.out.println("\t" + name);
        }

        return node.childrenAccept(this, data);
    }


    /**
     *  To visit a node
     *
     *@param  node  The node we are visiting
     *@param  data  The rename type data
     *@return       The rename type data
     */
    public Object visit(ASTPrimaryExpression node, Object data) {
        ASTPrimaryPrefix prefix = (ASTPrimaryPrefix) node.jjtGetChild(0);
        if (prefix.jjtGetChild(0) instanceof ASTName) {
            ASTName name = (ASTName) prefix.jjtGetChild(0);
            int count = name.getNameSize();
            if (name.getNamePart(0).equals("Debug")) {
                return data;
            } else {
                String part = name.getNamePart(count - 1);
                if (part.equals("getBundle")) {
                    return data;
                } else if (part.equals("getCachedBundle")) {
                    return data;
                } else if (part.equals("getString")) {
                    return data;
                }
            }
        }
        return node.childrenAccept(this, data);
    }
}
