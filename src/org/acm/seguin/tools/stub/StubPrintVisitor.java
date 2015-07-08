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
package org.acm.seguin.tools.stub;
import org.acm.seguin.pretty.PrintData;
import org.acm.seguin.parser.JavaParserVisitor;
import org.acm.seguin.parser.Node;
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
import org.acm.seguin.parser.ast.ASTAssertionStatement;
import java.io.*;
import java.util.Enumeration;

import org.acm.seguin.parser.ast.ASTTypeParameterList;
import org.acm.seguin.parser.ast.ASTTypeParameter;
import org.acm.seguin.parser.ast.ASTTypeArguments;
import org.acm.seguin.parser.ast.ASTReferenceTypeList;
import org.acm.seguin.parser.ast.ASTReferenceType;
import org.acm.seguin.parser.ast.ASTClassOrInterfaceType;
import org.acm.seguin.parser.ast.ASTActualTypeArgument;
import org.acm.seguin.parser.ast.ASTTypeParameters;
import org.acm.seguin.parser.ast.ASTGenericNameList;
import org.acm.seguin.parser.ast.ASTEnumDeclaration;
import org.acm.seguin.parser.ast.ASTEnumElement;
import org.acm.seguin.parser.ast.ASTIdentifier;
import org.acm.seguin.parser.ast.ASTAttribute;

/**
 *  This object simply reflects all the processing back to the individual nodes.
 *
 *@author     Chris Seguin
 *@author     Mike Atkinson
 *@created    October 13, 1999
 *@date       March 4, 1999
 */
public class StubPrintVisitor implements JavaParserVisitor {
    /**
     *  Constructor for the StubPrintVisitor object
     */
    public StubPrintVisitor() { }


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
        PrintData printData = (PrintData) data; //  Get the data
        node.childrenAccept(this, data);        //  Accept the children
        printData.flush();                      //  Flush the buffer
        return data;                            //  Return the data
    }
    public Object visit(ASTTypeParameter node, Object data) {
        PrintData printData = (PrintData) data; //  Get the data
        node.childrenAccept(this, data);        //  Accept the children
        printData.flush();                      //  Flush the buffer
        return data;                            //  Return the data
    }
    public Object visit(ASTTypeArguments node, Object data) {
        PrintData printData = (PrintData) data; //  Get the data
        node.childrenAccept(this, data);        //  Accept the children
        printData.flush();                      //  Flush the buffer
        return data;                            //  Return the data
    }
    public Object visit(ASTReferenceTypeList node, Object data) {
        PrintData printData = (PrintData) data; //  Get the data
        node.childrenAccept(this, data);        //  Accept the children
        printData.flush();                      //  Flush the buffer
        return data;                            //  Return the data
    }
    public Object visit(ASTReferenceType node, Object data) {
        PrintData printData = (PrintData) data; //  Get the data
        node.childrenAccept(this, data);        //  Accept the children
        //  Add the array
        int count = node.getArrayCount();
        for (int ndx = 0; ndx < count; ndx++) {
            printData.appendText("[");
            printData.appendText("]");
        }
        printData.flush();                      //  Flush the buffer
        return data;                            //  Return the data
    }
    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTClassOrInterfaceType node, Object data) {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Print the name of the node
        printData.appendText(node.getName());

        //  Return the data
        return data;
    }

    public Object visit(ASTActualTypeArgument node, Object data) {
        PrintData printData = (PrintData) data; //  Get the data
        node.childrenAccept(this, data);        //  Accept the children
        printData.flush();                      //  Flush the buffer
        return data;                            //  Return the data
    }
    public Object visit(ASTTypeParameters node, Object data) {
        PrintData printData = (PrintData) data; //  Get the data
        node.childrenAccept(this, data);        //  Accept the children
        printData.flush();                      //  Flush the buffer
        return data;                            //  Return the data
    }
    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTGenericNameList node, Object data) {
        PrintData printData = (PrintData) data; //  Get the data
        //  Traverse the children
        int countChildren = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < countChildren; ndx++) {
            if (ndx > 0) {
                printData.appendText(", ");
            }
            Node child = node.jjtGetChild(ndx);
            child.jjtAccept(this, data);
        }
        return data;  //  Return the data
    }


    public Object visit(ASTEnumDeclaration node, Object data) {
        PrintData printData = (PrintData) data; //  Get the data
        node.childrenAccept(this, data);        //  Accept the children
        printData.flush();                      //  Flush the buffer
        return data;                            //  Return the data
    }
    public Object visit(ASTEnumElement node, Object data) {
        PrintData printData = (PrintData) data; //  Get the data
        node.childrenAccept(this, data);        //  Accept the children
        printData.flush();                      //  Flush the buffer
        return data;                            //  Return the data
    }
    public Object visit(ASTIdentifier node, Object data) {
        PrintData printData = (PrintData) data; //  Get the data
        node.childrenAccept(this, data);        //  Accept the children
        printData.flush();                      //  Flush the buffer
        return data;                            //  Return the data
    }

    public Object visit(ASTAttribute node, Object data) {
        PrintData printData = (PrintData) data; //  Get the data
        node.childrenAccept(this, data);        //  Accept the children
        printData.flush();                      //  Flush the buffer
        return data;                            //  Return the data
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTCompilationUnit node, Object data) {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Accept the children
        node.childrenAccept(this, data);

        //  Flush the buffer
        printData.flush();

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
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Print any tokens
        printData.appendKeyword("package");
        printData.space();

        //  Traverse the children
        node.childrenAccept(this, data);

        //  Print any final tokens
        printData.appendText(";");
        printData.newline();

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
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Print any tokens
        printData.appendKeyword("import ");

        //  Traverse the children
        node.childrenAccept(this, data);

        //  Print any final tokens
        if (node.isImportingPackage()) {
            printData.appendText(".");
            printData.appendText("*");
            printData.appendText(";");
            printData.newline();
        } else {
            printData.appendText(";");
            printData.newline();
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
            //  Get the data
            PrintData printData = (PrintData) data;
            printData.appendText(";");
            printData.newline();
        }
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTClassDeclaration node, Object data) {
        if (!(node.isPublic() || node.isProtected())) {
            return data;
        }

        //  Get the data
        PrintData printData = (PrintData) data;

        //  Get the child
        SimpleNode child = (SimpleNode) node.jjtGetFirstChild();

        //  Indent and insert the modifiers
        printData.indent();
        printData.appendKeyword(node.getModifiersString(PrintData.STANDARD_ORDER));

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
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Print any tokens
        printData.appendKeyword("class ");
        printData.appendText(node.getName());

        //  Traverse the children
        int lastIndex = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < lastIndex; ndx++) {
            Node next = node.jjtGetChild(ndx);
            if (next instanceof ASTClassOrInterfaceType) {
                printData.appendKeyword(" extends ");
                next.jjtAccept(this, data);
            } else if (next instanceof ASTNameList) {
                printData.appendKeyword(" implements ");
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
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Print any tokens
        printData.beginBlock(false, false);

        //  Traverse the children
        node.childrenAccept(this, data);

        //  Print any tokens
        printData.endBlock(); //false, false);

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
        if (!(node.isPublic() || node.isProtected())) {
            return data;
        }

        //  Get the data
        PrintData printData = (PrintData) data;

        //  Print any tokens
        printData.beginClass();

        //  Get the child
        SimpleNode child = (SimpleNode) node.jjtGetFirstChild();

        //  Indent and include the modifiers
        printData.indent();
        printData.appendKeyword(node.getModifiersString(PrintData.STANDARD_ORDER));

        //  Traverse the children
        node.childrenAccept(this, data);
        printData.endClass();

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
        if (!(node.isPublic() || node.isProtected())) {
            return data;
        }

        //  Get the data
        PrintData printData = (PrintData) data;

        //  Get the child
        SimpleNode child = (SimpleNode) node.jjtGetFirstChild();

        //  Indent and add the modifiers
        printData.indent();
        printData.appendKeyword(node.getModifiersString(PrintData.STANDARD_ORDER));

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
        if (!(node.isPublic() || node.isProtected())) {
            return data;
        }

        //  Get the data
        PrintData printData = (PrintData) data;

        //  Print any tokens
        printData.beginInterface();

        //  Get the child
        SimpleNode child = (SimpleNode) node.jjtGetFirstChild();

        //  Force the Javadoc to be included
        if (node.isRequired()) {
            node.finish();
            //node.printJavaDocComponents(printData);
        }

        //  Indent and include the modifiers
        printData.indent();
        printData.appendKeyword(node.getModifiersString(PrintData.STANDARD_ORDER));

        //  Traverse the children
        node.childrenAccept(this, data);

        //  Finish this interface
        printData.endInterface();

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

        //  Get the data
        PrintData printData = (PrintData) data;

        //  Print any tokens
        printData.appendKeyword("interface ");
        printData.appendText(node.getName());

        //  Traverse the children
        int nextIndex = 0;
        Node next = node.jjtGetChild(nextIndex);
        if (next instanceof ASTNameList) {
            printData.appendKeyword(" extends ");
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
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Begin the block
        printData.beginBlock(false, false);

        //  Travers the children
        node.childrenAccept(this, data);

        //  End the block
        printData.endBlock(); //false, false);

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
        if (!(node.isPublic() || node.isProtected())) {
            return data;
        }

        //  Get the data
        PrintData printData = (PrintData) data;

        //  Print any tokens
        printData.beginField();
        if (node.isRequired()) {
            node.finish();
            //node.printJavaDocComponents(printData);
        }
        printData.indent();
        printData.appendKeyword(node.getModifiersString(PrintData.STANDARD_ORDER));

        //  Handle the first two children (which are required)
        Node next = node.jjtGetFirstChild();
        next.jjtAccept(this, data);
        printData.space();
        next = node.jjtGetChild(1);
        next.jjtAccept(this, data);

        //  Traverse the rest of the children
        int lastIndex = node.jjtGetNumChildren();
        for (int ndx = 2; ndx < lastIndex; ndx++) {
            printData.appendText(", ");
            next = node.jjtGetChild(ndx);
            next.jjtAccept(this, data);
        }

        //  Finish the entry
        printData.appendText(";");
        printData.newline();
        printData.endField();

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
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Handle the first child (which is required)
        Node next = node.jjtGetFirstChild();
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
    public Object visit(ASTVariableDeclaratorId node, Object data) {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Handle the first child (which is required)
        printData.appendText(node.getName());
        int last = node.getArrayCount();
        for (int ndx = 0; ndx < last; ndx++) {
            printData.appendText("[");
            printData.appendText("]");
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
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Handle the first child (which is required)
        printData.appendText("{");
        int last = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < last; ndx++) {
            if (ndx > 0) {
                printData.appendText(", ");
            }
            Node child = node.jjtGetChild(ndx);
            child.jjtAccept(this, data);
        }
        if (node.isFinalComma()) {
            printData.appendText(",");
        }
        printData.appendText("}");

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
    public Object visit(ASTMethodDeclaration node, Object data) {
        if (!(node.isPublic() || node.isProtected())) {
            return data;
        }

        //  Get the data
        PrintData printData = (PrintData) data;

        //  Print any tokens
        //printData.beginMethod(); //no need for lines between methods
        printData.indent();
        printData.appendKeyword(node.getModifiersString(PrintData.STANDARD_ORDER));

        //  Handle the first two/three children (which are required)
        int child = 0;
        Node next = node.jjtGetChild(child++);
        next.jjtAccept(this, data);
        printData.space();
        if (next instanceof ASTTypeParameters) {
            next = node.jjtGetChild(child++);
            next.jjtAccept(this, data);
            printData.space();
        }
        next = node.jjtGetChild(child++);
        next.jjtAccept(this, data);

        //  Traverse the rest of the children
        int lastIndex = node.jjtGetNumChildren();
        boolean foundBlock = false;
        for (int ndx = child; ndx < lastIndex; ndx++) {
            next = node.jjtGetChild(ndx);
            if (next instanceof ASTNameList) {
                printData.appendKeyword(" throws ");
                next.jjtAccept(this, data);
            } else if (next instanceof ASTBlock) {
                foundBlock = true;
                printData.appendText("{ ");
                next.jjtAccept(this, data);
            }
        }

        //  Finish if it is abstract
        if (foundBlock) {
            printData.appendText("}");
        } else {
            printData.appendText(";");
            printData.newline();
        }

        //  Note the end of the method
        printData.endMethod();

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
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Handle the first child (which is required)
        printData.appendText(node.getName());
        node.childrenAccept(this, data);

        int last = node.getArrayCount();
        for (int ndx = 0; ndx < last; ndx++) {
            printData.appendText("[");
            printData.appendText("]");
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
    public Object visit(ASTFormalParameters node, Object data) {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Print any tokens
        printData.beginExpression(node.jjtGetNumChildren() > 0);

        //  Traverse the children
        Node next;
        int lastIndex = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < lastIndex; ndx++) {
            if (ndx > 0) {
                printData.appendText(", ");
            }
            next = node.jjtGetChild(ndx);
            next.jjtAccept(this, data);
        }

        //  Finish it
        printData.endExpression(node.jjtGetNumChildren() > 0);

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
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Print any tokens
        if (node.isUsingFinal()) {
            printData.appendKeyword("final ");
        }

        //  Traverse the children
        Node next = node.jjtGetFirstChild();
        next.jjtAccept(this, data);
        printData.space();
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
        if (!(node.isPublic() || node.isProtected())) {
            return data;
        }

        //  Get the data
        PrintData printData = (PrintData) data;

        //  Print any tokens
        printData.beginMethod();
        printData.indent();
        printData.appendKeyword(node.getModifiersString(PrintData.STANDARD_ORDER));
        printData.appendText(node.getName());

        //  Handle the first child (which is required)
        int child = 0;
        if (node.jjtGetChild(child) instanceof ASTAttribute) {
            child++;
        }
        if (node.jjtGetChild(child) instanceof ASTTypeParameters) {
            child++;
        }
        Node next = node.jjtGetChild(child);
        next.jjtAccept(this, data);

        //  Get the last index
        int lastIndex = node.jjtGetNumChildren();
        int startAt = child+1;

        //  Handle the name list if it is present
        if (lastIndex > child+1) {
            next = node.jjtGetChild(1);
            if (next instanceof ASTNameList) {
                printData.space();
                printData.appendKeyword("throws");
                printData.space();
                next.jjtAccept(this, data);
                startAt++;
            }
        }

        //  Print the starting block
        printData.beginBlock(false, false);

        //  Traverse the rest of the children
        boolean foundBlock = false;
        for (int ndx = startAt; ndx < lastIndex; ndx++) {
            next = node.jjtGetChild(ndx);
            next.jjtAccept(this, data);
        }

        //  Print the end block
        printData.endBlock(false,false);
        printData.endMethod();

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
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTInitializer node, Object data) {
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTType node, Object data) {
        //  Get the data
        PrintData printData = (PrintData) data;

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
    public Object visit(ASTPrimitiveType node, Object data) {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Print the name of the node
        printData.appendKeyword(node.getName());

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
    public Object visit(ASTResultType node, Object data) {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Traverse the children
        if (node.hasAnyChildren()) {
            node.childrenAccept(this, data);
        } else {
            printData.appendKeyword("void");
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
    public Object visit(ASTName node, Object data) {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Print the name of the node
        printData.appendText(node.getName());

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
    public Object visit(ASTNameList node, Object data) {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Traverse the children
        int countChildren = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < countChildren; ndx++) {
            if (ndx > 0) {
                printData.appendText(", ");
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
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTConditionalAndExpression node, Object data) {
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTInclusiveOrExpression node, Object data) {
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTExclusiveOrExpression node, Object data) {
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTAndExpression node, Object data) {
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTEqualityExpression node, Object data) {
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTInstanceOfExpression node, Object data) {
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTRelationalExpression node, Object data) {
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTShiftExpression node, Object data) {
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTAdditiveExpression node, Object data) {
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTMultiplicativeExpression node, Object data) {
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTUnaryExpression node, Object data) {
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
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTNullLiteral node, Object data) {
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTArguments node, Object data) {
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTArgumentList node, Object data) {
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
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTArrayDimsAndInits node, Object data) {
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
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTBlock node, Object data) {
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
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTSwitchStatement node, Object data) {
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
        return data;
    }
}
