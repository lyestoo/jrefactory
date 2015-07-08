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

import org.acm.seguin.parser.JavaParserVisitor;
import org.acm.seguin.parser.Node;
import org.acm.seguin.parser.Token;
import org.acm.seguin.parser.ast.SimpleNode;
import org.acm.seguin.parser.ast.ASTInterfaceBody;
import org.acm.seguin.parser.ast.ASTTryStatement;
import org.acm.seguin.parser.ast.ASTSynchronizedStatement;
import org.acm.seguin.parser.ast.ASTThrowStatement;
import org.acm.seguin.parser.ast.ASTReturnStatement;
import org.acm.seguin.parser.ast.ASTForUpdate;
import org.acm.seguin.parser.ast.ASTStatementExpressionList;
import org.acm.seguin.parser.ast.ASTForInit;
import org.acm.seguin.parser.ast.ASTForStatement;
import org.acm.seguin.parser.ast.ASTDoStatement;
import org.acm.seguin.parser.ast.ASTWhileStatement;
import org.acm.seguin.parser.ast.ASTIfStatement;
import org.acm.seguin.parser.ast.ASTSwitchLabel;
import org.acm.seguin.parser.ast.ASTSwitchStatement;
import org.acm.seguin.parser.ast.ASTEmptyStatement;
import org.acm.seguin.parser.ast.ASTBlockStatement;
import org.acm.seguin.parser.ast.ASTBlock;
import org.acm.seguin.parser.ast.ASTStatement;
import org.acm.seguin.parser.ast.ASTAllocationExpression;
import org.acm.seguin.parser.ast.ASTArgumentList;
import org.acm.seguin.parser.ast.ASTArguments;
import org.acm.seguin.parser.ast.ASTNullLiteral;
import org.acm.seguin.parser.ast.ASTPrimaryExpression;
import org.acm.seguin.parser.ast.ASTCastExpression;
import org.acm.seguin.parser.ast.ASTCastLookahead;
import org.acm.seguin.parser.ast.ASTPreDecrementExpression;
import org.acm.seguin.parser.ast.ASTPreIncrementExpression;
import org.acm.seguin.parser.ast.ASTInstanceOfExpression;
import org.acm.seguin.parser.ast.ASTAndExpression;
import org.acm.seguin.parser.ast.ASTExclusiveOrExpression;
import org.acm.seguin.parser.ast.ASTInclusiveOrExpression;
import org.acm.seguin.parser.ast.ASTConditionalAndExpression;
import org.acm.seguin.parser.ast.ASTConditionalOrExpression;
import org.acm.seguin.parser.ast.ASTConditionalExpression;
import org.acm.seguin.parser.ast.ASTExpression;
import org.acm.seguin.parser.ast.ASTNameList;
import org.acm.seguin.parser.ast.ASTResultType;
import org.acm.seguin.parser.ast.ASTFormalParameters;
import org.acm.seguin.parser.ast.ASTVariableInitializer;
import org.acm.seguin.parser.ast.ASTVariableDeclarator;
import org.acm.seguin.parser.ast.ASTInterfaceMemberDeclaration;
import org.acm.seguin.parser.ast.ASTMethodDeclarationLookahead;
import org.acm.seguin.parser.ast.ASTClassBodyDeclaration;
import org.acm.seguin.parser.ast.ASTClassBody;
import org.acm.seguin.parser.ast.ASTTypeDeclaration;
import org.acm.seguin.parser.ast.ASTCompilationUnit;
import org.acm.seguin.parser.ast.ASTPackageDeclaration;
import org.acm.seguin.parser.ast.ASTArrayInitializer;
import org.acm.seguin.parser.ast.ASTContinueStatement;
import org.acm.seguin.parser.ast.ASTBreakStatement;
import org.acm.seguin.parser.ast.ASTStatementExpression;
import org.acm.seguin.parser.ast.ASTLocalVariableDeclaration;
import org.acm.seguin.parser.ast.ASTBooleanLiteral;
import org.acm.seguin.parser.ast.ASTPrimarySuffix;
import org.acm.seguin.parser.ast.ASTPrimaryPrefix;
import org.acm.seguin.parser.ast.ASTPostfixExpression;
import org.acm.seguin.parser.ast.ASTUnaryExpressionNotPlusMinus;
import org.acm.seguin.parser.ast.ASTUnaryExpression;
import org.acm.seguin.parser.ast.ASTMultiplicativeExpression;
import org.acm.seguin.parser.ast.ASTAdditiveExpression;
import org.acm.seguin.parser.ast.ASTShiftExpression;
import org.acm.seguin.parser.ast.ASTRelationalExpression;
import org.acm.seguin.parser.ast.ASTEqualityExpression;
import org.acm.seguin.parser.ast.ASTAssignmentOperator;
import org.acm.seguin.parser.ast.ASTPrimitiveType;
import org.acm.seguin.parser.ast.ASTType;
import org.acm.seguin.parser.ast.ASTInitializer;
import org.acm.seguin.parser.ast.ASTExplicitConstructorInvocation;
import org.acm.seguin.parser.ast.ASTFormalParameter;
import org.acm.seguin.parser.ast.ASTFieldDeclaration;
import org.acm.seguin.parser.ast.ASTNestedInterfaceDeclaration;
import org.acm.seguin.parser.ast.ASTMethodDeclaration;
import org.acm.seguin.parser.ast.ASTInterfaceDeclaration;
import org.acm.seguin.parser.ast.ASTNestedClassDeclaration;
import org.acm.seguin.parser.ast.ASTVariableDeclaratorId;
import org.acm.seguin.parser.ast.ASTImportDeclaration;
import org.acm.seguin.parser.ast.ASTConstructorDeclaration;
import org.acm.seguin.parser.ast.ASTUnmodifiedInterfaceDeclaration;
import org.acm.seguin.parser.ast.ASTUnmodifiedClassDeclaration;
import org.acm.seguin.parser.ast.ASTLiteral;
import org.acm.seguin.parser.ast.ASTClassDeclaration;
import org.acm.seguin.parser.ast.ASTMethodDeclarator;
import org.acm.seguin.parser.ast.ASTLabeledStatement;
import org.acm.seguin.parser.ast.ASTArrayDimsAndInits;
import org.acm.seguin.parser.ast.ASTName;
import org.acm.seguin.parser.JavaParserConstants;
import org.acm.seguin.util.FileSettings;
import org.acm.seguin.util.MissingSettingsException;
import org.acm.seguin.parser.ast.ASTAssertionStatement;

import org.acm.seguin.parser.ast.ASTTypeParameterList;
import org.acm.seguin.parser.ast.ASTTypeParameter;
import org.acm.seguin.parser.ast.ASTTypeArguments;
import org.acm.seguin.parser.ast.ASTReferenceTypeList;
import org.acm.seguin.parser.ast.ASTReferenceType;
import org.acm.seguin.parser.ast.ASTReferenceVariance;
import org.acm.seguin.parser.ast.ASTTypeParameters;
import org.acm.seguin.parser.ast.ASTGenericNameList;
import org.acm.seguin.parser.ast.ASTVariance;
import org.acm.seguin.parser.ast.ASTEnumDeclaration;
import org.acm.seguin.parser.ast.ASTEnumElement;
import org.acm.seguin.parser.ast.ASTIdentifier;
import org.acm.seguin.parser.ast.ASTAttribute;

import java.io.*;
import java.util.Enumeration;

/**
 *  This object counts the lines and labels them with specific data
 *
 *@author     Chris Seguin
 *@author     Mike Atkinson
 *@created    March 4, 1999
 */
public class LineCountVisitor implements JavaParserVisitor {
    //  Instance Variables
    private int lineCount;


    /**
     *  Constructor for the LineCountVisitor object
     */
    public LineCountVisitor() {
        lineCount = 1;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(SimpleNode node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTTypeParameterList node, Object data) {
        int countChildren = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < countChildren; ndx++) {
            if (ndx > 0) {
                countLines(node.getSpecial("comma." + (ndx-1)));
            }
            Node child = node.jjtGetChild(ndx);
            child.jjtAccept(this, data);
        }
        return data;
    }
    public Object visit(ASTTypeParameter node, Object data) {
        //System.out.println("FIXME: visit(ASTTypeParameter)");
        return data;
    }
    public Object visit(ASTTypeArguments node, Object data) {
        countLines(node.getSpecial("<."));
        int countChildren = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < countChildren; ndx++) {
            if (ndx > 0) {
                countLines(node.getSpecial("comma." + (ndx-1)));
            }
            Node child = node.jjtGetChild(ndx);
            child.jjtAccept(this, data);
        }
        countLines(node.getSpecial(">."));
        return data;
    }
    public Object visit(ASTReferenceTypeList node, Object data) {
        countLines(node.getSpecial("<."));
        int countChildren = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < countChildren; ndx++) {
            if (ndx > 0) {
                countLines(node.getSpecial("comma." + (ndx-1)));
            }
            Node child = node.jjtGetChild(ndx);
            child.jjtAccept(this, data);
        }
        countLines(node.getSpecial(">."));
        return data;
    }
    public Object visit(ASTReferenceType node, Object data) {
        node.childrenAccept(this, data);   //  Traverse the children
        return data;
    }
    public Object visit(ASTReferenceVariance node, Object data) {
        countLines(node.getSpecial("star"));
        countLines(node.getSpecial("plus"));
        countLines(node.getSpecial("minus"));
        node.childrenAccept(this, data);   //  Traverse the children
        return data;
    }
    public Object visit(ASTTypeParameters node, Object data) {
        countLines(node.getSpecial("<"));
        node.childrenAccept(this, data);   //  Traverse the children
        countLines(node.getSpecial(">"));
        return data;
    }
    public Object visit(ASTGenericNameList node, Object data) {
        //  Traverse the children
        int commaCount = 0;
        int countChildren = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < countChildren; ndx++) {
            Node child = node.jjtGetChild(ndx);
            if (ndx > 0 && child instanceof ASTName) {
                countLines(node.getSpecial("comma." + commaCount));
                commaCount++;
            }
            child.jjtAccept(this, data);
        }
        return data;//  Return the data
    }
    public Object visit(ASTVariance node, Object data) {
        countLines(node.getSpecial("[."));
        countLines(node.getSpecial("id"));
        countLines(node.getSpecial("]."));
        return data;
    }
    public Object visit(ASTEnumDeclaration node, Object data) {
        countLines(node.getSpecial("static"));
        countLines(node.getSpecial("final"));
        countLines(node.getSpecial("public"));
        countLines(node.getSpecial("protected"));
        countLines(node.getSpecial("private"));

        countLines(node.getSpecial("enum"));
        Node child = node.jjtGetChild(0);
        child.jjtAccept(this, data);
        
        int n=1;
        if (node.jjtGetChild(n) instanceof ASTGenericNameList) {
            countLines(node.getSpecial("implements"));
            child = node.jjtGetChild(n);
            child.jjtAccept(this, data);
            n++;
        }
        
        countLines(node.getSpecial("begin"));
        int countChildren = node.jjtGetNumChildren();
        for (int ndx = n; ndx < countChildren; ndx++) {
            if (node.jjtGetChild(ndx) instanceof ASTEnumElement) {
                if (ndx > n) {
                    countLines(node.getSpecial("comma." + (ndx - n)));
                }
            }
            child = node.jjtGetChild(ndx);
            child.jjtAccept(this, data);
        }
        countLines(node.getSpecial("end"));
        
        return data;
    }
    public Object visit(ASTEnumElement node, Object data) {
        countLines(node.getSpecial("id"));
        node.childrenAccept(this, data);   //  Traverse the children
        return data;
    }
    public Object visit(ASTIdentifier node, Object data) {
        countLines(node.getSpecial("id"));
        return data;
    }
    public Object visit(ASTAttribute node, Object data) {
        countLines(node.getSpecial("id"));
        node.childrenAccept(this, data);   //  Traverse the children
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTCompilationUnit node, Object data) {
        //  Accept the children
        node.childrenAccept(this, data);

        //  Visit EOF special token
        countLines(node.getSpecial("EOF"));

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTPackageDeclaration node, Object data) {
        //  Print any tokens
        countLines(node.getSpecial("package"));

        //  Traverse the children
        node.childrenAccept(this, data);

        //  Print any final tokens
        countLines(node.getSpecial("semicolon"));

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTImportDeclaration node, Object data) {

        //  Print any tokens
        countLines(node.getSpecial("import"));
        countLines(node.getSpecial("static"));   // JDK1.5 static imports

        //  Traverse the children
        node.childrenAccept(this, data);

        //  Print any final tokens
        if (node.isImportingPackage()) {
            countLines(node.getSpecial("period"));
            countLines(node.getSpecial("star"));
            countLines(node.getSpecial("semicolon"));
        } else {
            countLines(node.getSpecial("semicolon"));
        }

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTTypeDeclaration node, Object data) {
        if (node.hasAnyChildren()) {
            node.childrenAccept(this, data);
        } else {
            countLines(node.getSpecial("semicolon"));
        }
        return data;
    }


    /**
     *  Visit a class declaration
     *
     *@param  node  the class declaration
     *@param  data  the print data
     *@return       the result of visiting this node
     */
    public Object visit(ASTClassDeclaration node, Object data) {

        //  Print any tokens
        countLines(node.getSpecial("final"));
        countLines(node.getSpecial("public"));
        countLines(node.getSpecial("strictfp"));
        countLines(node.getSpecial("abstract"));

        //  Get the child
        SimpleNode child = (SimpleNode) node.jjtGetChild(0);
        countLines(child.getSpecial("class"));

        //  Traverse the children
        node.childrenAccept(this, data);

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTUnmodifiedClassDeclaration node, Object data) {

        //  Print any tokens
        countLines(node.getSpecial("id"));

        //  Traverse the children
        int lastIndex = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < lastIndex; ndx++) {
            Node next = node.jjtGetChild(ndx);
            if (next instanceof ASTName) {
                countLines(node.getSpecial("extends"));
                next.jjtAccept(this, data);
            } else if (next instanceof ASTGenericNameList) {
                countLines(node.getSpecial("implements"));
                next.jjtAccept(this, data);
            } else {
                next.jjtAccept(this, data);
            }
        }

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTClassBody node, Object data) {
        //  Print any tokens
        countLines(node.getSpecial("begin"));

        //  Traverse the children
        node.childrenAccept(this, data);

        //  Print any tokens
        countLines(node.getSpecial("end"));

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTNestedClassDeclaration node, Object data) {

        //  Print any tokens
        countLines(node.getSpecial("final"));
        countLines(node.getSpecial("public"));
        countLines(node.getSpecial("protected"));
        countLines(node.getSpecial("private"));
        countLines(node.getSpecial("abstract"));
        countLines(node.getSpecial("static"));
        countLines(node.getSpecial("strictfp"));

        //  Get the child
        SimpleNode child = (SimpleNode) node.jjtGetChild(0);
        countLines(child.getSpecial("class"));

        //  Traverse the children
        node.childrenAccept(this, data);

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTClassBodyDeclaration node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTMethodDeclarationLookahead node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTInterfaceDeclaration node, Object data) {

        //  Print any special tokens
        countLines(node.getSpecial("public"));
        countLines(node.getSpecial("abstract"));
        countLines(node.getSpecial("strictfp"));

        //  Get the child
        SimpleNode child = (SimpleNode) node.jjtGetChild(0);
        countLines(child.getSpecial("interface"));

        //  Traverse the children
        node.childrenAccept(this, data);

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTNestedInterfaceDeclaration node, Object data) {

        //  Print any tokens
        countLines(node.getSpecial("static"));
        countLines(node.getSpecial("abstract"));
        countLines(node.getSpecial("strictfp"));
        countLines(node.getSpecial("final"));
        countLines(node.getSpecial("public"));
        countLines(node.getSpecial("protected"));
        countLines(node.getSpecial("private"));

        //  Get the child
        SimpleNode child = (SimpleNode) node.jjtGetChild(0);
        countLines(child.getSpecial("interface"));

        //  Traverse the children
        node.childrenAccept(this, data);

        //  Finish this interface
        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTUnmodifiedInterfaceDeclaration node, Object data) {
        //  Local Variables
        boolean first = true;

        //  Print any tokens
        countLines(node.getSpecial("id"));

        //  Traverse the children
        int nextIndex = 0;
        Node next = node.jjtGetChild(nextIndex);
        if (next instanceof ASTGenericNameList) {
            countLines(node.getSpecial("extends"));
            next.jjtAccept(this, data);

            //  Get the next node
            nextIndex++;
            next = node.jjtGetChild(nextIndex);
        }

        //  Handle the interface body
        next.jjtAccept(this, data);

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTInterfaceBody node, Object data) {
        countLines(node.getSpecial("begin"));   //  Begin the block
        node.childrenAccept(this, data);        //  Travers the children
        countLines(node.getSpecial("end"));     //  End the block
        return data;                            //  Return the data
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTInterfaceMemberDeclaration node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTFieldDeclaration node, Object data) {
        //  Print any tokens
        countLines(node.getSpecial("static"));
        countLines(node.getSpecial("transient"));
        countLines(node.getSpecial("volatile"));
        countLines(node.getSpecial("strictfp"));
        countLines(node.getSpecial("final"));
        countLines(node.getSpecial("public"));
        countLines(node.getSpecial("protected"));
        countLines(node.getSpecial("private"));

        //  Handle the first two children (which are required)
        Node next = node.jjtGetChild(0);
        next.jjtAccept(this, data);
        next = node.jjtGetChild(1);
        next.jjtAccept(this, data);

        //  Traverse the rest of the children
        int lastIndex = node.jjtGetNumChildren();
        for (int ndx = 2; ndx < lastIndex; ndx++) {
            countLines(node.getSpecial("comma." + (ndx - 2)));
            next = node.jjtGetChild(ndx);
            next.jjtAccept(this, data);
        }

        //  Finish the entry
        countLines(node.getSpecial("semicolon"));

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTVariableDeclarator node, Object data) {

        //  Handle the first child (which is required)
        Node next = node.jjtGetChild(0);
        next.jjtAccept(this, data);

        //  Traverse the rest of the children
        if (node.jjtGetNumChildren() > 1) {
            countLines(node.getSpecial("equals"));
            next = node.jjtGetChild(1);
            next.jjtAccept(this, data);
        }

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTVariableDeclaratorId node, Object data) {

        //  Handle the first child (which is required)
        countLines(node.getSpecial("id"));
        int last = node.getArrayCount();
        for (int ndx = 0; ndx < last; ndx++) {
           node.jjtGetChild(ndx).jjtAccept(this, data);
        }

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTVariableInitializer node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTArrayInitializer node, Object data) {

        //  Handle the first child (which is required)
        countLines(node.getSpecial("begin"));
        int last = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < last; ndx++) {
            if (ndx > 0) {
                countLines(node.getSpecial("comma." + (ndx - 1)));
            }
            Node child = node.jjtGetChild(ndx);
            child.jjtAccept(this, data);
        }
        if (node.isFinalComma()) {
            countLines(node.getSpecial("comma." + (last - 1)));
        }
        countLines(node.getSpecial("end"));

        //  Return the data
        return data;
    }


    /**
     *  Pretty prints the method declaration
     *
     *@param  node  the node in the parse tree
     *@param  data  the print data
     *@return       the print data
     */
    public Object visit(ASTMethodDeclaration node, Object data) {
        //  Print any tokens
        countLines(node.getSpecial("public"));
        countLines(node.getSpecial("protected"));
        countLines(node.getSpecial("private"));
        countLines(node.getSpecial("static"));
        countLines(node.getSpecial("abstract"));
        countLines(node.getSpecial("final"));
        countLines(node.getSpecial("native"));
        countLines(node.getSpecial("strictfp"));
        countLines(node.getSpecial("synchronized"));
        countLines(getInitialToken((ASTResultType) node.jjtGetChild(0)));

        //  Handle the first two/three children (which are required)
        int child = 0;
        Node next = node.jjtGetChild(child++);
        next.jjtAccept(this, data);
        if (next instanceof ASTTypeParameters) {
            next = node.jjtGetChild(child++);
            next.jjtAccept(this, data);
        }
            
        next = node.jjtGetChild(child++);
        next.jjtAccept(this, data);

        //  Traverse the rest of the children
        int lastIndex = node.jjtGetNumChildren();
        boolean foundBlock = false;
        for (int ndx = child; ndx < lastIndex; ndx++) {
            next = node.jjtGetChild(ndx);
            if (next instanceof ASTNameList) {
                countLines(node.getSpecial("throws"));
                next.jjtAccept(this, data);
            } else if (next instanceof ASTBlock) {
                foundBlock = true;
                next.jjtAccept(this, data);
            }
        }

        //  Finish if it is abstract
        if (!foundBlock) {
            countLines(node.getSpecial("semicolon"));
        }

        //  Note the end of the method
        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTMethodDeclarator node, Object data) {

        //  Handle the first child (which is required)
        countLines(node.getSpecial("id"));
        node.childrenAccept(this, data);
        /*
        int last = node.getArrayCount();
        for (int ndx = 0; ndx < last; ndx++) {
            if (node.jjtGetChild(ndx+1) instanceof ASTVariance) {
                node.jjtGetChild(ndx+1).jjtAccept(this, data);
            } else {
                countLines(node.getSpecial("[." + ndx));
                countLines(node.getSpecial("]." + ndx));
            }
        }
        */

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTFormalParameters node, Object data) {

        //  Print any tokens
        countLines(node.getSpecial("begin"));

        //  Traverse the children
        Node next;
        int lastIndex = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < lastIndex; ndx++) {
            if (ndx > 0) {
                countLines(node.getSpecial("comma." + (ndx - 1)));
            }
            next = node.jjtGetChild(ndx);
            next.jjtAccept(this, data);
        }

        //  Finish it
        countLines(node.getSpecial("end"));

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTFormalParameter node, Object data) {

        //  Print any tokens
        if (node.isUsingFinal()) {
            countLines(node.getSpecial("final"));
        }

        //  Traverse the children
        Node next = node.jjtGetChild(0);
        next.jjtAccept(this, data);
        next = node.jjtGetChild(1);
        next.jjtAccept(this, data);

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTConstructorDeclaration node, Object data) {

        //  Print any tokens
        countLines(node.getSpecial("public"));
        countLines(node.getSpecial("protected"));
        countLines(node.getSpecial("private"));
        countLines(node.getSpecial("id"));

        //  Handle the first child (which is required)
        int child = 0;
        if (node.jjtGetChild(child) instanceof ASTAttribute) {
            Node next = node.jjtGetChild(child);
            next.jjtAccept(this, data);
            child++;
        }
        if (node.jjtGetChild(child) instanceof ASTTypeParameters) {
            Node next = node.jjtGetChild(child);
            next.jjtAccept(this, data);
            child++;
        }
        Node next = node.jjtGetChild(child);
        next.jjtAccept(this, data);

        //  Get the last index
        int lastIndex = node.jjtGetNumChildren();
        int startAt = child+1;

        //  Handle the name list if it is present
        if (lastIndex > 1) {
            next = node.jjtGetChild(1);
            if (next instanceof ASTNameList) {
                countLines(node.getSpecial("throws"));
                next.jjtAccept(this, data);
                startAt++;
            }
        }

        //  Print the starting block
        countLines(node.getSpecial("begin"));

        //  Traverse the rest of the children
        boolean foundBlock = false;
        for (int ndx = startAt; ndx < lastIndex; ndx++) {
            next = node.jjtGetChild(ndx);
            next.jjtAccept(this, data);
        }

        //  Print the end block
        countLines(node.getSpecial("end"));

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTExplicitConstructorInvocation node, Object data) {

        //  Print the name of the node
        int startWith = 0;
        if (node.jjtGetChild(0) instanceof ASTPrimaryExpression) {
            node.jjtGetChild(0).jjtAccept(this, data);
            startWith++;
            countLines(node.getSpecial("."));
        }
        countLines(node.getSpecial("explicit"));

        //  Traverse the children
        int last = node.jjtGetNumChildren();
        for (int ndx = startWith; ndx < last; ndx++) {
            node.jjtGetChild(ndx).jjtAccept(this, data);
        }

        //  End of the line
        countLines(node.getSpecial("semicolon"));

        //  Return the data
        return data;
    }


    /**
     *  Formats the initializer
     *
     *@param  node  The initializer node
     *@param  data  The print data
     *@return       Nothing interesting
     */
    public Object visit(ASTInitializer node, Object data) {

        //  Print the name of the node
        if (node.isUsingStatic()) {
            countLines(node.getSpecial("static"));
        }

        node.childrenAccept(this, data);   //  Traverse the children
        return data;                       //  Return the data
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTType node, Object data) {
        node.childrenAccept(this, data); //  Traverse the children
        return data;                     //  Return the data
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTPrimitiveType node, Object data) {
        countLines(node.getSpecial("primitive"));  //  Print the name of the node
        return data;                               //  Return the data
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTResultType node, Object data) {

        //  Traverse the children
        if (node.hasAnyChildren()) {
            node.childrenAccept(this, data);
        } else {
            countLines(node.getSpecial("primitive"));
        }

        return data;                  //  Return the data
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTName node, Object data) {

        //  Print the name of the node
        int size = node.getNameSize();
        for (int ndx = 0; ndx < size; ndx++) {
            if (ndx>0) {
                countLines(node.getSpecial("period" + ndx));
            }
            countLines(node.getSpecial("id" + ndx));
        }

        return data;        //  Return the data
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTNameList node, Object data) {

        //  Traverse the children
        int countChildren = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < countChildren; ndx++) {
            if (ndx > 0) {
                countLines(node.getSpecial("comma." + (ndx - 1)));
            }
            Node child = node.jjtGetChild(ndx);
            child.jjtAccept(this, data);
        }

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTExpression node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTAssignmentOperator node, Object data) {

        //  Print the name of the node
        countLines(node.getSpecial("operator"));

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTConditionalExpression node, Object data) {

        //  Traverse the children
        int childCount = node.jjtGetNumChildren();
        if (childCount == 1) {
            node.jjtGetChild(0).jjtAccept(this, data);
        } else {
            node.jjtGetChild(0).jjtAccept(this, data);
            countLines(node.getSpecial("?"));
            node.jjtGetChild(1).jjtAccept(this, data);
            countLines(node.getSpecial(":"));
            node.jjtGetChild(2).jjtAccept(this, data);
        }

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTConditionalOrExpression node, Object data) {
        return binaryExpression(node, "||", data);
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTConditionalAndExpression node, Object data) {
        return binaryExpression(node, "&&", data);
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTInclusiveOrExpression node, Object data) {
        return binaryExpression(node, "|", data);
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTExclusiveOrExpression node, Object data) {
        return binaryExpression(node, "^", data);
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTAndExpression node, Object data) {
        return binaryExpression(node, "&", data);
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTEqualityExpression node, Object data) {
        return binaryExpression2(node, node.getNames(), data);
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTInstanceOfExpression node, Object data) {
        return binaryExpression(node, "instanceof", data);
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTRelationalExpression node, Object data) {
        return binaryExpression2(node, node.getNames(), data);
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTShiftExpression node, Object data) {
        return binaryExpression2(node, node.getNames(), data);
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTAdditiveExpression node, Object data) {
        return binaryExpression2(node, node.getNames(), data);
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTMultiplicativeExpression node, Object data) {
        return binaryExpression2(node, node.getNames(), data);
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTUnaryExpression node, Object data) {

        //  Traverse the children
        Node child = node.jjtGetChild(0);
        if (child instanceof ASTUnaryExpression) {
            countLines(node.getSpecial("operator"));
        }
        child.jjtAccept(this, data);

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTPreIncrementExpression node, Object data) {

        //  Include the preincrement
        countLines(node.getSpecial("operator"));

        //  Traverse the children
        node.childrenAccept(this, data);

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTPreDecrementExpression node, Object data) {

        //  Include the preincrement
        countLines(node.getSpecial("operator"));

        //  Traverse the children
        node.childrenAccept(this, data);

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTUnaryExpressionNotPlusMinus node, Object data) {

        //  Traverse the children
        Node child = node.jjtGetChild(0);
        if (child instanceof ASTUnaryExpression) {
            countLines(node.getSpecial("operator"));
        }
        child.jjtAccept(this, data);

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTCastLookahead node, Object data) {

        //  Traverse the children
        node.childrenAccept(this, data);

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTPostfixExpression node, Object data) {

        //  Traverse the children
        node.childrenAccept(this, data);

        //  Include the increment
        countLines(node.getSpecial("operator"));

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTCastExpression node, Object data) {

        //  Cast portion
        countLines(node.getSpecial("begin"));
        node.jjtGetChild(0).jjtAccept(this, data);
        countLines(node.getSpecial("end"));

        //  Expression
        node.jjtGetChild(1).jjtAccept(this, data);

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTPrimaryExpression node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTPrimaryPrefix node, Object data) {

        //  Traverse the children
        if (node.jjtGetNumChildren() == 0) {
            int count = node.getCount();
            for (int i=0; i<count; i++) {
                countLines(node.getSpecial("this."+i));
            }
            countLines(node.getSpecial("this"));
            countLines(node.getSpecial("id"));
        } else {
            Node child = node.jjtGetChild(0);
            if ((child instanceof ASTLiteral) ||
                    (child instanceof ASTName) ||
                    (child instanceof ASTAllocationExpression)) {
                child.jjtAccept(this, data);
            } else if (child instanceof ASTExpression) {
                countLines(node.getSpecial("begin"));
                child.jjtAccept(this, data);
                countLines(node.getSpecial("end"));
            } else if (child instanceof ASTResultType) {
                child.jjtAccept(this, data);
            } else {
                System.out.println("UNEXPECTED child for ASTPrimaryPrefix ="+child);
                child.jjtAccept(this, data);
            }
        }

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTPrimarySuffix node, Object data) {

        //  Traverse the children
        if (node.jjtGetNumChildren() == 0) {
            countLines(node.getSpecial("dot"));
            countLines(node.getSpecial("id"));
        } else {
            Node child = node.jjtGetChild(0);
            if (child instanceof ASTArguments) {
                child.jjtAccept(this, data);
            } else if (child instanceof ASTExpression) {
                countLines(node.getSpecial("["));
                child.jjtAccept(this, data);
                countLines(node.getSpecial("]"));
            } else if (child instanceof ASTAllocationExpression) {
                countLines(node.getSpecial("dot"));
                child.jjtAccept(this, data);
            } else if (child instanceof ASTReferenceTypeList) {
                countLines(node.getSpecial("dot"));
                countLines(node.getSpecial("id"));
                child.jjtAccept(this, data);
            }
        }

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTLiteral node, Object data) {

        //  Traverse the children
        if (node.hasAnyChildren()) {
            node.childrenAccept(this, data);
        } else {
            countLines(node.getSpecial("id"));
        }

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTBooleanLiteral node, Object data) {
        countLines(node.getSpecial("id"));  //  Print the data
        return data;                        //  Return the data
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTNullLiteral node, Object data) {
        countLines(node.getSpecial("id"));  //  Print the data
        return data;                        //  Return the data
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTArguments node, Object data) {
        countLines(node.getSpecial("begin"));      //  Start the parens
        node.childrenAccept(this, data);           //  Traverse the children
        countLines(node.getSpecial("end"));        //  Finish the parens
        return data;                               //  Return the data
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTArgumentList node, Object data) {

        //  Traverse the children
        int count = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < count; ndx++) {
            if (ndx > 0) {
                countLines(node.getSpecial("comma." + (ndx - 1)));
            }
            node.jjtGetChild(ndx).jjtAccept(this, data);
        }

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTAllocationExpression node, Object data) {
        //  Traverse the children
        countLines(node.getSpecial("id"));    //  Print the name of the node
        node.childrenAccept(this, data);      //  Traverse the children
        return data;                          //  Return the data
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTArrayDimsAndInits node, Object data) {

        //  Traverse the children
        boolean foundInitializer = false;
        int last = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < last; ndx++) {
            if (node.jjtGetChild(ndx) instanceof ASTExpression) {
                countLines(node.getSpecial("[." + ndx));
                node.jjtGetChild(ndx).jjtAccept(this, data);
                countLines(node.getSpecial("]." + ndx));
            } else if (node.jjtGetChild(ndx) instanceof ASTVariance) {
                node.jjtGetChild(ndx).jjtAccept(this, data);
            } else if (node.jjtGetChild(ndx) instanceof ASTArrayInitializer) {
                node.jjtGetChild(ndx).jjtAccept(this, data);
            }
        }

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTStatement node, Object data) {

        //  Traverse the children
        node.childrenAccept(this, data);
        if (node.jjtGetChild(0) instanceof ASTStatementExpression) {
            //  Finish off the statement expression
            countLines(node.getSpecial("semicolon"));
        }

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTLabeledStatement node, Object data) {

        //  Print the data
        countLines(node.getSpecial("id"));
        countLines(node.getSpecial("colon"));

        //  Traverse the children
        node.childrenAccept(this, data);

        //  Return the data
        return data;
    }


    /**
     *  Visits a block in the parse tree
     *
     *@param  node  the block node
     *@param  data  the information that is used to traverse the tree
     *@return       data is returned
     */
    public Object visit(ASTBlock node, Object data) {
        //  Start the block
        countLines(node.getSpecial("begin"));

        //  Traverse the children
        node.childrenAccept(this, data);

        //  Finish the block
        countLines(node.getSpecial("end"));

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTBlockStatement node, Object data) {

        //  Include the stuff before the class/interface declaration
        if (node.hasAnyChildren()) {
            SimpleNode child = (SimpleNode) node.jjtGetChild(0);

            if (node.isFinal()) {
                countLines(node.getSpecial("final"));
            }
            if (child instanceof ASTUnmodifiedClassDeclaration) {
                countLines(child.getSpecial("class"));
            } else if (child instanceof ASTUnmodifiedInterfaceDeclaration) {
                countLines(child.getSpecial("interface"));
            }
        }
        countLines(node.getSpecial("semicolon"));

        //  Traverse the children
        node.childrenAccept(this, data);

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTLocalVariableDeclaration node, Object data) {
        //  Traverse the children
        int last = node.jjtGetNumChildren();

        //  Print the final token
        if (node.isUsingFinal()) {
            countLines(node.getSpecial("final"));
        }

        for (int ndx = 0; ndx < last; ndx++) {
            if (ndx>1) {
                countLines(node.getSpecial("comma." + (ndx - 2)));
            }
            //  Visit the child
            node.jjtGetChild(ndx).jjtAccept(this, data);
        }

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTEmptyStatement node, Object data) {

        //  Print the name of the node
        countLines(node.getSpecial("semicolon"));

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTStatementExpression node, Object data) {

        //  Traverse the children
        if (node.jjtGetChild(0) instanceof ASTPrimaryExpression) {
            int last = node.jjtGetNumChildren();
            node.jjtGetChild(0).jjtAccept(this, data);
            countLines(node.getSpecial("id"));
            for (int ndx = 1; ndx < last; ndx++) {
                node.jjtGetChild(ndx).jjtAccept(this, data);
            }
        } else {
            node.childrenAccept(this, data);
        }

        //  Return the data
        return data;
    }


    /**
     *  Counts the number of lines associated with a switch statement
     *
     *@param  node  the switch node in the parse tree
     *@param  data  the data used to visit this parse tree
     *@return       the data
     */
    public Object visit(ASTSwitchStatement node, Object data) {

        //  Switch
        countLines(node.getSpecial("switch"));
        countLines(node.getSpecial("beginExpr"));
        node.jjtGetChild(0).jjtAccept(this, data);
        countLines(node.getSpecial("endExpr"));

        //  Start the block
        countLines(node.getSpecial("beginBlock"));

        //  Traverse the children
        int last = node.jjtGetNumChildren();
        for (int ndx = 1; ndx < last; ndx++) {
            Node next = node.jjtGetChild(ndx);
            if (next instanceof ASTBlockStatement) {
                next.jjtAccept(this, data);
            } else {
                next.jjtAccept(this, data);
            }
        }

        //  End the block
        countLines(node.getSpecial("endBlock"));

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTSwitchLabel node, Object data) {

        //  Determine if the node has children
        if (node.hasAnyChildren()) {
            countLines(node.getSpecial("id"));
            node.childrenAccept(this, data);
            countLines(node.getSpecial("colon"));
        } else {
            countLines(node.getSpecial("id"));
            countLines(node.getSpecial("colon"));
        }

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTIfStatement node, Object data) {

        //  Determine if the node has children
        countLines(node.getSpecial("if"));
        countLines(node.getSpecial("beginExpr"));
        node.jjtGetChild(0).jjtAccept(this, data);
        countLines(node.getSpecial("endExpr"));

        //  Determine if the then contains a block
        boolean hasElse = (node.jjtGetNumChildren() == 3);
        if (node.jjtGetNumChildren() >= 2) {
            forceBlock(node.jjtGetChild(1), data);
        }

        //  Determine if the else part
        if (hasElse) {
            countLines(node.getSpecial("else"));
            //  Determine if the next item is a statement
            ASTStatement child = (ASTStatement) node.jjtGetChild(2);
            Node next = child.jjtGetChild(0);
            if (next instanceof ASTIfStatement) {
                next.jjtAccept(this, data);
            } else {
                forceBlock(child, data);
            }
        }

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTWhileStatement node, Object data) {

        //  Determine if the node has children
        countLines(node.getSpecial("while"));
        countLines(node.getSpecial("beginExpr"));
        node.jjtGetChild(0).jjtAccept(this, data);
        countLines(node.getSpecial("endExpr"));

        //  Process the block
        forceBlock(node.jjtGetChild(1), data);

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTDoStatement node, Object data) {

        //  Begin the do block
        countLines(node.getSpecial("do"));

        //  Process the block
        forceBlock(node.jjtGetChild(0), data);

        //  Process the while block
        countLines(node.getSpecial("while"));
        countLines(node.getSpecial("beginExpr"));
        node.jjtGetChild(1).jjtAccept(this, data);
        countLines(node.getSpecial("endExpr"));
        countLines(node.getSpecial("semicolon"));

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTForStatement node, Object data) {

        //  Start the for loop
        countLines(node.getSpecial("for"));
        countLines(node.getSpecial("beginExpr"));

        //  Traverse the children
        Node next = node.jjtGetChild(0);
        int index = 1;
        if (next instanceof ASTLocalVariableDeclaration) {
            next.jjtAccept(this, data);
            countLines(node.getSpecial("loopover"));
            next = node.jjtGetChild(index);
            index++;
            next.jjtAccept(this, data);
        } else {
            if (next instanceof ASTForInit) {
                next.jjtAccept(this, data);
                next = node.jjtGetChild(index);
                index++;
            }
            countLines(node.getSpecial("init"));
            if (next instanceof ASTExpression) {
                next.jjtAccept(this, data);
                next = node.jjtGetChild(index);
                index++;
            }
            countLines(node.getSpecial("test"));
            if (next instanceof ASTForUpdate) {
                next.jjtAccept(this, data);
                next = node.jjtGetChild(index);
                index++;
            }
        }
        countLines(node.getSpecial("endExpr"));
        forceBlock(next, data);

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTForInit node, Object data) {

        //  Traverse the children
        Node next = node.jjtGetChild(0);
        if (next instanceof ASTLocalVariableDeclaration) {
            forInit((ASTLocalVariableDeclaration) next, data);
        } else {
            node.childrenAccept(this, data);
        }

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTStatementExpressionList node, Object data) {

        //  Traverse the children
        int last = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < last; ndx++) {
            if (ndx > 0) {
                countLines(node.getSpecial("comma." + (ndx - 1)));
            }
            node.jjtGetChild(ndx).jjtAccept(this, data);
        }

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTForUpdate node, Object data) {
        //  Traverse the children
        node.childrenAccept(this, data);

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTBreakStatement node, Object data) {

        //  Print the break statement
        countLines(node.getSpecial("break"));
        String name = node.getName();
        if (!((name == null) || (name.length() == 0))) {
            countLines(node.getSpecial("id"));
        }
        countLines(node.getSpecial("semicolon"));

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTContinueStatement node, Object data) {

        //  Print the continue statement
        countLines(node.getSpecial("continue"));
        String name = node.getName();
        if (!((name == null) || (name.length() == 0))) {
            countLines(node.getSpecial("id"));
        }
        countLines(node.getSpecial("semicolon"));

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTReturnStatement node, Object data) {

        //  Traverse the children
        if (node.hasAnyChildren()) {
            countLines(node.getSpecial("return"));
            node.childrenAccept(this, data);
            countLines(node.getSpecial("semicolon"));
        } else {
            countLines(node.getSpecial("return"));
            countLines(node.getSpecial("semicolon"));
        }

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTThrowStatement node, Object data) {
        //  Traverse the children
        countLines(node.getSpecial("throw"));
        node.childrenAccept(this, data);
        countLines(node.getSpecial("semicolon"));

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTSynchronizedStatement node, Object data) {

        //  Traverse the children
        countLines(node.getSpecial("synchronized"));
        countLines(node.getSpecial("beginExpr"));
        node.jjtGetChild(0).jjtAccept(this, data);
        countLines(node.getSpecial("endExpr"));
        node.jjtGetChild(1).jjtAccept(this, data);

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTTryStatement node, Object data) {
        //  Traverse the children
        countLines(node.getSpecial("try"));
        node.jjtGetChild(0).jjtAccept(this, data);

        //  Now work with the pairs
        int last = node.jjtGetNumChildren();
        boolean paired = false;
        int catchCount = 0;
        for (int ndx = 1; ndx < last; ndx++) {
            Node next = node.jjtGetChild(ndx);
            if (next instanceof ASTFormalParameter) {
                countLines(node.getSpecial("catch" + catchCount));
                countLines(node.getSpecial("beginExpr" + catchCount));
                next.jjtAccept(this, data);
                countLines(node.getSpecial("endExpr" + catchCount));
                paired = true;
                catchCount++;
            } else {
                if (!paired) {
                    countLines(node.getSpecial("finally"));
                }
                next.jjtAccept(this, data);
                paired = false;
            }
        }

        //  Return the data
        return data;
    }


    /**
     *  Visit the assertion node
     *
     *@param  node  the node
     *@param  data  the data needed to perform the visit
     *@return       the result of visiting the node
     */
    public Object visit(ASTAssertionStatement node, Object data)
    {
        //  Traverse the children
        countLines(node.getSpecial("assert"));
        node.jjtGetChild(0).jjtAccept(this, data);
        if (node.jjtGetNumChildren() > 1) {
	        countLines(node.getSpecial("colon"));
	        node.jjtGetChild(1).jjtAccept(this, data);
        }
        countLines(node.getSpecial("semicolon"));

        //  Return the data
        return data;
    }

    /**
     *  Returns the current line count
     *
     *@return    the line count
     */
    protected int getLineCount() {
        return lineCount;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  name  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    protected Object binaryExpression(SimpleNode node, String name, Object data) {

        //  Traverse the children
        int childCount = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < childCount; ndx++) {
            if (ndx > 0) {
                countLines(node.getSpecial("operator." + (ndx - 1)));
            }
            node.jjtGetChild(ndx).jjtAccept(this, data);
        }

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node   Description of Parameter
     *@param  names  Description of Parameter
     *@param  data   Description of Parameter
     *@return        Description of the Returned Value
     */
    protected Object binaryExpression2(SimpleNode node, Enumeration names, Object data) {

        //  Traverse the children
        int childCount = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < childCount; ndx++) {
            if (ndx > 0) {
                countLines(node.getSpecial("operator." + (ndx - 1)));
            }
            node.jjtGetChild(ndx).jjtAccept(this, data);
        }

        //  Return the data
        return data;
    }


    /**
     *  This code is based on the pretty printer, and was used to force the
     *  source code to have a block for an if statement.
     *
     *@param  node  The node that should be contained inside a block
     *@param  data  the print data
     */
    protected void forceBlock(Node node, Object data) {
        if ((node.jjtGetNumChildren() > 0) &&
                (node.jjtGetChild(0) instanceof ASTBlock)) {
            //  We know we have a block
            ASTBlock child = (ASTBlock) node.jjtGetChild(0);

            child.jjtAccept(this, data);
        } else {
            ((SimpleNode) node).childrenAccept(this, data);
        }
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     */
    protected void forInit(ASTLocalVariableDeclaration node, Object data) {

        //  Traverse the children
        int last = node.jjtGetNumChildren();
        Node typeNode = node.jjtGetChild(0);
        typeNode.jjtAccept(this, data);

        for (int ndx = 1; ndx < last; ndx++) {
            if (ndx > 1) {
                //  Add a comma between entries
                countLines(node.getSpecial("comma." + (ndx - 1)));
            }

            //  Print the final token
            if (node.isUsingFinal()) {
                countLines(node.getSpecial("final"));
            }

            //  Visit the child
            node.jjtGetChild(ndx).jjtAccept(this, data);
        }
    }


    /**
     *  Check the initial token, and removes it from the object.
     *
     *@param  top  the result type
     *@return      the initial token
     */
    protected Token getInitialToken(ASTResultType top) {
        //  Check to see if we need to go farther down
        if (top.hasAnyChildren()) {
            ASTType type = (ASTType) top.jjtGetChild(0);
            if (type.jjtGetChild(0) instanceof ASTPrimitiveType) {
                ASTPrimitiveType primitiveType = (ASTPrimitiveType) type.jjtGetChild(0);
                Token tok = primitiveType.getSpecial("primitive");
                primitiveType.removeSpecial("primitive");
                return tok;
            }
            else if (type.jjtGetChild(0) instanceof ASTReferenceType) {
                    ASTReferenceType reference = (ASTReferenceType) type.jjtGetChild(0);
                    if (reference.jjtGetChild(0) instanceof ASTPrimitiveType) {
                            ASTPrimitiveType primitiveType = (ASTPrimitiveType) reference.jjtGetChild(0);
                            Token tok = primitiveType.getSpecial("primitive");
                            primitiveType.removeSpecial("primitive");
                            return tok;
                    } else {
                            ASTName name = (ASTName) reference.jjtGetChild(0);
                            Token tok = name.getSpecial("id0");
                            name.removeSpecial("id0");
                            return tok;
                    }
            }
            else {
                ASTName name = (ASTName) type.jjtGetChild(0);
                Token tok = name.getSpecial("id0");
                name.removeSpecial("id0");
                return tok;
            }
        } else {
            //  No farther to go - return the token
            Token tok = top.getSpecial("primitive");
            top.removeSpecial("primitive");
            return tok;
        }
    }

    /**
     *  Check the initial token, and removes it from the object.
     *
     *@param  top  the result type
     *@return      the initial token
     */
    protected Token getInitialToken(ASTTypeParameters top) {
        Token tok = top.getSpecial("<");
        top.removeSpecial("<");
        return tok;
    }



    /**
     *  Counts the number of lines in the current token
     *
     *@param  token  the token
     */
    protected void countLines(Token token) {
        if (token == null) {
            return;
        }

        Token first = beginning(token);
        Token current = first;
        while (current != null) {
            switch (current.kind) {
                case 4:
                case 5:
                case 6:
                case 7:
                    lineCount++;
                    break;
                case JavaParserConstants.SINGLE_LINE_COMMENT:
                case JavaParserConstants.FORMAL_COMMENT:
                case JavaParserConstants.MULTI_LINE_COMMENT:
                case JavaParserConstants.CATEGORY_COMMENT:
                    countNewlines(current.image);
                    break;
                default:
                    //System.out.println("Unknown code:  " + current.kind);
                    break;
            }
            current = current.next;
        }
    }


    /**
     *  Got to the beginning
     *
     *@param  tok  Description of Parameter
     *@return      Description of the Returned Value
     */
    private Token beginning(Token tok) {
        if (tok == null) {
            return null;
        }

        //  Find the first token
        Token current = tok;
        Token previous = tok.specialToken;
        while (previous != null) {
            current = previous;
            previous = current.specialToken;
        }

        //  Return the first
        return current;
    }


    /**
     *  Counts the lines in the token's string
     *
     *@param  value  the string to count
     */
    private void countNewlines(String value) {
        for (int ndx = 0; ndx < value.length(); ndx++) {
            if (value.charAt(ndx) == '\n') {
                lineCount++;
            }
        }
    }
}
