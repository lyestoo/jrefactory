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

import java.io.Serializable;

/**
 *  Basic summary class. Provides a single point for a visitor to encounter and
 *  a parent summary. All summaries have a parent except package summaries.
 *
 *@author     Chris Seguin
 *@created    May 12, 1999
 */
public abstract class Summary implements Serializable {
    //  Local Variables
    private Summary parent;
    private int start;
    private int end;


    /**
     *  Create a summary object
     *
     *@param  initParent  the parent
     */
    public Summary(Summary initParent) {
        parent = initParent;
        start = -1;
        end = -1;
    }


    /**
     *  Return the parent object
     *
     *@return    the parent object
     */
    public Summary getParent() {
        return parent;
    }


    /**
     *  Gets the StartLine attribute of the Summary object
     *
     *@return    The StartLine value
     */
    public int getStartLine() {
        return start;
    }


    /**
     *  Gets the EndLine attribute of the Summary object
     *
     *@return    The EndLine value
     */
    public int getEndLine() {
        return end;
    }


    /**
     *  Gets the DeclarationLine attribute of the MethodSummary object
     *
     *@return    The DeclarationLine value
     */
    public int getDeclarationLine() {
        return Math.min(start + 1, end);
    }


    /**
     *  Returns the name
     *
     *@return    the name
     */
    public abstract String getName();


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
     *  Sets the StartLine attribute of the Summary object
     *
     *@param  value  The new StartLine value
     */
    protected void setStartLine(int value) {
        start = value;
    }


    /**
     *  Sets the EndLine attribute of the Summary object
     *
     *@param  value  The new EndLine value
     */
    protected void setEndLine(int value) {
        end = value;
        if (end < start) {
            start = end;
        }
    }
}
