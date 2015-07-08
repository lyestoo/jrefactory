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

/**
 *  All items that want to visit a summary tree should implement this interface.
 *
 *@author     Chris Seguin
 *@created    May 12, 1999
 */
public interface SummaryVisitor {
    /**
     *  Visit a summary node. This is the default method.
     *
     *@param  node  the summary that we are visiting
     *@param  data  the data that was passed in
     *@return       the result
     */
    public Object visit(Summary node, Object data);


    /**
     *  Visit a package summary.
     *
     *@param  node  the summary that we are visiting
     *@param  data  the data that was passed in
     *@return       the result
     */
    public Object visit(PackageSummary node, Object data);


    /**
     *  Visit a file summary.
     *
     *@param  node  the summary that we are visiting
     *@param  data  the data that was passed in
     *@return       the result
     */
    public Object visit(FileSummary node, Object data);


    /**
     *  Visit a import summary.
     *
     *@param  node  the summary that we are visiting
     *@param  data  the data that was passed in
     *@return       the result
     */
    public Object visit(ImportSummary node, Object data);


    /**
     *  Visit a type summary.
     *
     *@param  node  the summary that we are visiting
     *@param  data  the data that was passed in
     *@return       the result
     */
    public Object visit(TypeSummary node, Object data);


    /**
     *  Visit a method summary.
     *
     *@param  node  the summary that we are visiting
     *@param  data  the data that was passed in
     *@return       the result
     */
    public Object visit(MethodSummary node, Object data);


    /**
     *  Visit a field summary.
     *
     *@param  node  the summary that we are visiting
     *@param  data  the data that was passed in
     *@return       the result
     */
    public Object visit(FieldSummary node, Object data);


    /**
     *  Visit a parameter summary.
     *
     *@param  node  the summary that we are visiting
     *@param  data  the data that was passed in
     *@return       the result
     */
    public Object visit(ParameterSummary node, Object data);


    /**
     *  Visit a local variable summary.
     *
     *@param  node  the summary that we are visiting
     *@param  data  the data that was passed in
     *@return       the result
     */
    public Object visit(LocalVariableSummary node, Object data);


    /**
     *  Visit a variable summary.
     *
     *@param  node  the summary that we are visiting
     *@param  data  the data that was passed in
     *@return       the result
     */
    public Object visit(VariableSummary node, Object data);


    /**
     *  Visit a type declaration summary.
     *
     *@param  node  the summary that we are visiting
     *@param  data  the data that was passed in
     *@return       the result
     */
    public Object visit(TypeDeclSummary node, Object data);


    /**
     *  Visit a message send summary.
     *
     *@param  node  the summary that we are visiting
     *@param  data  the data that was passed in
     *@return       the result
     */
    public Object visit(MessageSendSummary node, Object data);


    /**
     *  Visit a field access summary.
     *
     *@param  node  the summary that we are visiting
     *@param  data  the data that was passed in
     *@return       the result
     */
    public Object visit(FieldAccessSummary node, Object data);
}
