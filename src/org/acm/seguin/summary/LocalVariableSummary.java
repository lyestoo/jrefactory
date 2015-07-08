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
package org.acm.seguin.summary;

import org.acm.seguin.parser.Node;
import org.acm.seguin.parser.ast.ASTLocalVariableDeclaration;
import org.acm.seguin.parser.ast.ASTType;
import org.acm.seguin.parser.ast.ASTVariableDeclaratorId;

/**
 *  Stores information about the formal parameter
 *
 *@author     Chris Seguin
 *@created    June 13, 1999
 */
public class LocalVariableSummary extends VariableSummary {
    /**
     *  Creates a parameter summary
     *
     *@param  parentSummary  the parent summary
     *@param  typeNode       the type of parameter
     *@param  id             the id of the parameter
     */
    public LocalVariableSummary(Summary parentSummary, ASTType typeNode, ASTVariableDeclaratorId id) {
        super(parentSummary, typeNode, id);
    }


    /**
     *  Constructor for the LocalVariableSummary object
     *
     *@param  parentSummary  Description of Parameter
     *@param  type           Description of Parameter
     *@param  name           Description of Parameter
     */
    public LocalVariableSummary(Summary parentSummary, TypeDeclSummary type, String name) {
        super(parentSummary, type, name);
    }



    /**
     *  Provide method to visit a node
     *
     *@param  visitor  the visitor
     *@param  data     the data for the visit
     *@return          some new data
     */
    public Object accept(SummaryVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }


    /**
     *  Factory method
     *
     *@param  parentSummary  the parent summary
     *@param  field          the field declarator
     *@return                Description of the Returned Value
     */
    public static LocalVariableSummary[] createNew(Summary parentSummary, ASTLocalVariableDeclaration field) {
        //  Local Variables
        int last = field.jjtGetNumChildren();
        LocalVariableSummary[] result = new LocalVariableSummary[last - 1];
        ASTType type = (ASTType) field.jjtGetChild(0);

        //  Create a summary for each field
        for (int ndx = 1; ndx < last; ndx++) {
            Node next = field.jjtGetChild(ndx);
            result[ndx - 1] = new LocalVariableSummary(parentSummary, type,
                    (ASTVariableDeclaratorId) next.jjtGetChild(0));
        }

        //  Return the result
        return result;
    }
}
