/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.pretty;


import java.io.*;
import java.util.Enumeration;
import org.acm.seguin.parser.JavaParserVisitor;
import org.acm.seguin.parser.Node;
import org.acm.seguin.parser.Token;
import org.acm.seguin.parser.ast.ASTAdditiveExpression;
import org.acm.seguin.parser.ast.ASTAllocationExpression;
import org.acm.seguin.parser.ast.ASTAndExpression;
import org.acm.seguin.parser.ast.ASTArgumentList;
import org.acm.seguin.parser.ast.ASTArguments;
import org.acm.seguin.parser.ast.ASTArrayDimsAndInits;
import org.acm.seguin.parser.ast.ASTArrayInitializer;
import org.acm.seguin.parser.ast.ASTAssertionStatement;
import org.acm.seguin.parser.ast.ASTAssignmentOperator;
import org.acm.seguin.parser.ast.ASTAttribute;
import org.acm.seguin.parser.ast.ASTBlock;
import org.acm.seguin.parser.ast.ASTBlockStatement;
import org.acm.seguin.parser.ast.ASTBooleanLiteral;
import org.acm.seguin.parser.ast.ASTBreakStatement;
import org.acm.seguin.parser.ast.ASTCastExpression;
import org.acm.seguin.parser.ast.ASTCastLookahead;
import org.acm.seguin.parser.ast.ASTClassBody;
import org.acm.seguin.parser.ast.ASTClassBodyDeclaration;
import org.acm.seguin.parser.ast.ASTClassDeclaration;
import org.acm.seguin.parser.ast.ASTCompilationUnit;
import org.acm.seguin.parser.ast.ASTConditionalAndExpression;
import org.acm.seguin.parser.ast.ASTConditionalExpression;
import org.acm.seguin.parser.ast.ASTConditionalOrExpression;
import org.acm.seguin.parser.ast.ASTConstructorDeclaration;
import org.acm.seguin.parser.ast.ASTContinueStatement;
import org.acm.seguin.parser.ast.ASTDoStatement;
import org.acm.seguin.parser.ast.ASTEmptyStatement;
import org.acm.seguin.parser.ast.ASTEnumDeclaration;
import org.acm.seguin.parser.ast.ASTEnumElement;
import org.acm.seguin.parser.ast.ASTEqualityExpression;
import org.acm.seguin.parser.ast.ASTExclusiveOrExpression;
import org.acm.seguin.parser.ast.ASTExplicitConstructorInvocation;
import org.acm.seguin.parser.ast.ASTExpression;
import org.acm.seguin.parser.ast.ASTFieldDeclaration;
import org.acm.seguin.parser.ast.ASTForInit;
import org.acm.seguin.parser.ast.ASTForStatement;
import org.acm.seguin.parser.ast.ASTForUpdate;
import org.acm.seguin.parser.ast.ASTFormalParameter;
import org.acm.seguin.parser.ast.ASTFormalParameters;
import org.acm.seguin.parser.ast.ASTGenericNameList;
import org.acm.seguin.parser.ast.ASTIdentifier;
import org.acm.seguin.parser.ast.ASTIfStatement;
import org.acm.seguin.parser.ast.ASTImportDeclaration;
import org.acm.seguin.parser.ast.ASTInclusiveOrExpression;
import org.acm.seguin.parser.ast.ASTInitializer;
import org.acm.seguin.parser.ast.ASTInstanceOfExpression;
import org.acm.seguin.parser.ast.ASTInterfaceBody;
import org.acm.seguin.parser.ast.ASTInterfaceDeclaration;
import org.acm.seguin.parser.ast.ASTInterfaceMemberDeclaration;
import org.acm.seguin.parser.ast.ASTLabeledStatement;
import org.acm.seguin.parser.ast.ASTLiteral;
import org.acm.seguin.parser.ast.ASTLocalVariableDeclaration;
import org.acm.seguin.parser.ast.ASTMethodDeclaration;
import org.acm.seguin.parser.ast.ASTMethodDeclarationLookahead;
import org.acm.seguin.parser.ast.ASTMethodDeclarator;
import org.acm.seguin.parser.ast.ASTMultiplicativeExpression;
import org.acm.seguin.parser.ast.ASTName;
import org.acm.seguin.parser.ast.ASTNameList;
import org.acm.seguin.parser.ast.ASTNestedClassDeclaration;
import org.acm.seguin.parser.ast.ASTNestedInterfaceDeclaration;
import org.acm.seguin.parser.ast.ASTNullLiteral;
import org.acm.seguin.parser.ast.ASTPackageDeclaration;
import org.acm.seguin.parser.ast.ASTPostfixExpression;
import org.acm.seguin.parser.ast.ASTPreDecrementExpression;
import org.acm.seguin.parser.ast.ASTPreIncrementExpression;
import org.acm.seguin.parser.ast.ASTPrimaryExpression;
import org.acm.seguin.parser.ast.ASTPrimaryPrefix;
import org.acm.seguin.parser.ast.ASTPrimarySuffix;
import org.acm.seguin.parser.ast.ASTPrimitiveType;
import org.acm.seguin.parser.ast.ASTReferenceType;
import org.acm.seguin.parser.ast.ASTReferenceTypeList;
import org.acm.seguin.parser.ast.ASTReferenceTypeList;
import org.acm.seguin.parser.ast.ASTReferenceVariance;
import org.acm.seguin.parser.ast.ASTRelationalExpression;
import org.acm.seguin.parser.ast.ASTResultType;
import org.acm.seguin.parser.ast.ASTReturnStatement;
import org.acm.seguin.parser.ast.ASTShiftExpression;
import org.acm.seguin.parser.ast.ASTStatement;
import org.acm.seguin.parser.ast.ASTStatementExpression;
import org.acm.seguin.parser.ast.ASTStatementExpressionList;
import org.acm.seguin.parser.ast.ASTSwitchLabel;
import org.acm.seguin.parser.ast.ASTSwitchStatement;
import org.acm.seguin.parser.ast.ASTSynchronizedStatement;
import org.acm.seguin.parser.ast.ASTThrowStatement;
import org.acm.seguin.parser.ast.ASTTryStatement;
import org.acm.seguin.parser.ast.ASTType;
import org.acm.seguin.parser.ast.ASTTypeArguments;
import org.acm.seguin.parser.ast.ASTTypeDeclaration;
import org.acm.seguin.parser.ast.ASTTypeParameter;

import org.acm.seguin.parser.ast.ASTTypeParameterList;
import org.acm.seguin.parser.ast.ASTTypeParameters;
import org.acm.seguin.parser.ast.ASTUnaryExpression;
import org.acm.seguin.parser.ast.ASTUnaryExpressionNotPlusMinus;
import org.acm.seguin.parser.ast.ASTUnmodifiedClassDeclaration;
import org.acm.seguin.parser.ast.ASTUnmodifiedInterfaceDeclaration;
import org.acm.seguin.parser.ast.ASTVariableDeclarator;
import org.acm.seguin.parser.ast.ASTVariableDeclaratorId;
import org.acm.seguin.parser.ast.ASTVariableInitializer;
import org.acm.seguin.parser.ast.ASTVariance;
import org.acm.seguin.parser.ast.ASTWhileStatement;
import org.acm.seguin.parser.ast.SimpleNode;
import org.acm.seguin.util.FileSettings;
import org.acm.seguin.util.MissingSettingsException;

/**
 *  This object simply reflects all the processing back to the individual nodes.
 *
 *@author     Chris Seguin
 *@author     Mike Atkinson
 *@created    March 4, 1999
 */
public class PrettyPrintVisitor implements JavaParserVisitor {
    //  Instance Variables
    SpecialTokenVisitor specialTokenVisitor;
    private boolean enclosingIfStatement = false; // when true we are within an IfStatement
    private FieldSizeLookAhead fsla;
    private LocalVariableLookAhead lvla;


    /**
     *  Constructor for the PrettyPrintVisitor object
     */
    public PrettyPrintVisitor()
    {
        specialTokenVisitor = new SpecialTokenVisitor();

        fsla = new FieldSizeLookAhead(PrintData.DFS_ALIGN_EQUALS);
        lvla = new LocalVariableLookAhead();
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     *@since        JRefactory 2.7.00
     */
    public Object visit(SimpleNode node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     *@since        JRefactory 2.7.00
     */
    public Object visit(ASTVariance node, Object data)
    {
        PrintData printData = (PrintData) data;//  Get the data

        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("[."), printData));
        jjtAcceptSpecial(node, printData, "[.");
        printData.appendText("[");
        printData.appendText(node.getName());
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("]."), printData));
        jjtAcceptSpecial(node, printData, "].");
        printData.appendText("]");

        return data;//  Return the data
    }



    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     *@since        JRefactory 2.7.00
     */
    public Object visit(ASTTypeParameterList node, Object data)
    {
        PrintData printData = (PrintData) data;//  Get the data

        //  Traverse the children
        int countChildren = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < countChildren; ndx++) {
            if (ndx > 0) {
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("comma." + (ndx - 1)), printData));
                jjtAcceptSpecial(node, printData, ("comma." + (ndx - 1)) );
                printData.appendText(", ");
            }
            Node child = node.jjtGetChild(ndx);
            child.jjtAccept(this, data);
        }

        return data;//  Return the data
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     *@since        JRefactory 2.7.00
     */
    public Object visit(ASTTypeParameter node, Object data)
    {
        PrintData printData = (PrintData) data;//  Get the data

        //  Print the name of the node
        printData.appendText(node.getName());
        int countChildren = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < countChildren; ndx++) {
            if (ndx == 1) {
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("extends."), printData));
                jjtAcceptSpecial(node, printData, "extends.");
                printData.appendText(" extends ");
            }
            else if (ndx > 1) {
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("and."), printData));
                jjtAcceptSpecial(node, printData, "and.");
                printData.appendText(" & ");
            }
            Node child = node.jjtGetChild(ndx);
            child.jjtAccept(this, data);
        }

        return data;//  Return the data
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     *@since        JRefactory 2.7.00
     */
    public Object visit(ASTTypeParameters node, Object data)
    {
        PrintData printData = (PrintData) data;//  Get the data

        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("<."), printData));
        jjtAcceptSpecial(node, printData, "<.");
        printData.appendText("<");

        node.childrenAccept(this, data);//  Traverse the children

        printData.appendText(">");
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial(">."), printData));
        jjtAcceptSpecial(node, printData, ">.");

        return data;//  Return the data
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     *@since        JRefactory 2.7.00
     */
    public Object visit(ASTTypeArguments node, Object data)
    {
        PrintData printData = (PrintData) data;//  Get the data

        //  Print the name of the node
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("<."), printData));
        jjtAcceptSpecial(node, printData, "<.");
        printData.appendText("<");

        int countChildren = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < countChildren; ndx++) {
            if (ndx > 0) {
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("comma." + (ndx - 1)), printData));
                jjtAcceptSpecial(node, printData, ("comma." + (ndx - 1)));
                printData.appendText(", ");
            }
            node.jjtGetChild(ndx).jjtAccept(this, data);
        }

        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial(">."), printData));
        jjtAcceptSpecial(node, printData, ">.");
        printData.appendText(">");

        return data;//  Return the data
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     *@since        JRefactory 2.7.00
     */
    public Object visit(ASTReferenceTypeList node, Object data)
    {
        PrintData printData = (PrintData) data;//  Get the data

        int countChildren = node.jjtGetNumChildren();
        if (countChildren > 0) {
            for (int ndx = 0; ndx < countChildren; ndx++) {
                if (ndx > 0) {
                    //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("comma." + (ndx - 1)), printData));
                    jjtAcceptSpecial(node, printData, ("comma." + (ndx - 1)));
                    printData.appendText(", ");
                }
                node.jjtGetChild(ndx).jjtAccept(this, data);
            }
        }

        return data;//  Return the data
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     *@since        JRefactory 2.7.00
     */
    public Object visit(ASTReferenceType node, Object data)
    {
        PrintData printData = (PrintData) data;//  Get the data

        //  Traverse the children
        int childrenCount = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < childrenCount; ndx++) {
            node.jjtGetChild(ndx).jjtAccept(this, data);
        }

        return data;//  Return the data
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     *@since        JRefactory 2.7.00
     */
    public Object visit(ASTReferenceVariance node, Object data)
    {
        PrintData printData = (PrintData) data;//  Get the data

        if (node.isBiVariance()) {
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("star"), printData));
            jjtAcceptSpecial(node, printData, "star");
            printData.appendText("*");
        }

        //  Traverse the children
        int childrenCount = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < childrenCount; ndx++) {
            if (node.isCoVariance()) {
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("plus"), printData));
                jjtAcceptSpecial(node, printData, "plus");
                printData.appendText("+");
            }
            else if (node.isContraVariance()) {
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("minus"), printData));
                jjtAcceptSpecial(node, printData, "minus");
                printData.appendText("-");
            }
            node.jjtGetChild(ndx).jjtAccept(this, data);
        }

        return data;//  Return the data
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     *@since        JRefactory 2.7.00
     */
    public Object visit(ASTGenericNameList node, Object data)
    {
        PrintData printData = (PrintData) data;//  Get the data

        //  Traverse the children
        int countChildren = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < countChildren; ndx++) {
            Node child = node.jjtGetChild(ndx);
            if (ndx > 0 && child instanceof ASTName) {
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("comma." + (ndx - 1)), printData));
                jjtAcceptSpecial(node, printData, ("comma." + (ndx - 1)));
                printData.appendText(", ");
            }
            child.jjtAccept(this, data);
        }

        return data;//  Return the data
    }


   private static final String[] ENUM_MODIFIERS = new String[] { "static", "transient", "volatile", "final", "public", "protected", "private" };
    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     *@since        JRefactory 2.7.00
     */
    public Object visit(ASTEnumDeclaration node, Object data)
    {
        PrintData printData = (PrintData) data;//  Get the data

        //  Print any tokens
        printData.beginEnum();
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("static"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("transient"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("volatile"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("final"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("public"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("protected"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("private"), printData));
        jjtAcceptSpecials(node, printData, ENUM_MODIFIERS);
        if (isJavadocRequired(node, printData)) {
            node.finish();
            node.printJavaDocComponents(printData);
        }
        printData.indent();
        String modifiers = node.getModifiersString(printData.getModifierOrder());
        printData.appendKeyword(modifiers);

        printData.appendText("enum ");

        //assert(countChildren>1);
        Node next = node.jjtGetChild(0);
        next.jjtAccept(this, data);

        printData.appendText(" { ");

        //  Traverse the children
        boolean printingElements = true;
        int countChildren = node.jjtGetNumChildren();
        for (int ndx = 1; ndx < countChildren; ndx++) {
            Node child = node.jjtGetChild(ndx);
            if (!(child instanceof ASTEnumElement)) {
                if (printingElements) {
                    //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("semicolon"), printData));
                    jjtAcceptSpecial(node, printData, "semicolon");
                    printData.appendText(";");
                    printingElements = false;
                    printData.incrIndent();
                    printData.newline();
                }
            }
            if (ndx > 1 && printingElements) {
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("comma." + (ndx - 1)), printData));
                jjtAcceptSpecial(node, printData, ("comma." + (ndx - 1)));
                printData.appendText(", ");
            }
            child.jjtAccept(this, data);
        }
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("end"), printData));
        jjtAcceptSpecial(node, printData, "end");
        if (!printingElements) {
            printData.decrIndent();
            printData.indent();
            printData.appendText("}");
        }
        else {
            printData.appendText(" }");
        }
        //  Finish the entry
        printData.newline();
        printData.endEnum();

        return data;//  Return the data
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     *@since        JRefactory 2.7.00
     */
    public Object visit(ASTIdentifier node, Object data)
    {
        PrintData printData = (PrintData) data;//  Get the data

        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("id"), printData));
        jjtAcceptSpecial(node, printData, "id");
        printData.appendText(node.getName());

        return data;//  Return the data
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     *@since        JRefactory 2.7.00
     */
    public Object visit(ASTEnumElement node, Object data)
    {
        PrintData printData = (PrintData) data;//  Get the data

        int countChildren = node.jjtGetNumChildren();
        printData.appendText(node.getName());
        for (int ndx = 1; ndx < countChildren; ndx++) {
            Node child = node.jjtGetChild(ndx);
            child.jjtAccept(this, data);
        }

        return data;//  Return the data
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     *@since        JRefactory 2.7.00
     */
    public Object visit(ASTAttribute node, Object data)
    {
        PrintData printData = (PrintData) data;//  Get the data
        printData.appendText("@");
        int countChildren = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < countChildren; ndx++) {
            Node child = node.jjtGetChild(ndx);
            child.jjtAccept(this, data);
        }
        printData.space();

        return data;//  Return the data
    }



    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTCompilationUnit node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Sort it
        node.sort(printData.getTopOrder(node));

        //  Check if we have a header
        loadHeader(node, printData);

        //  Accept the children
        node.childrenAccept(this, data);

        //  Visit EOF special token
        if (!loadFooter(printData)) {
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("EOF"), printData));
            jjtAcceptSpecial(node, printData, "EOF");
        }

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
    public Object visit(ASTPackageDeclaration node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Print any tokens
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("package"), printData));
        jjtAcceptSpecial(node, printData, "package");
        printData.appendKeyword("package");
        printData.space();

        //  Traverse the children
        node.childrenAccept(this, data);

        //  Print any final tokens
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("semicolon"), printData));
        jjtAcceptSpecial(node, printData, "semicolon");
        printData.appendText(";");

        for (int ndx = 0; ndx <= printData.getLinesAfterPackage(); ndx++) {
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
    public Object visit(ASTImportDeclaration node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Print any tokens
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("import"), printData, printData.isMaintainNewlinesAroundImports()));
        jjtAcceptSpecial(node, printData, "import", printData.isMaintainNewlinesAroundImports());
        printData.appendKeyword("import");

        // handle static imports for JDK 1.5
        if (node.isStaticImport()) {
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("static"), printData));
            jjtAcceptSpecial(node, printData, "static");
            printData.appendKeyword(" static");
        }

        printData.space();

        //  Traverse the children
        node.childrenAccept(this, data);

        //  Print any final tokens
        if (node.isImportingPackage()) {
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("period"), printData));
            jjtAcceptSpecial(node, printData, "period");
            printData.appendText(".");
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("star"), printData));
            jjtAcceptSpecial(node, printData, "star");
            printData.appendText("*");
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("semicolon"), printData));
            jjtAcceptSpecial(node, printData, "semicolon");
            printData.appendText(";");
            printData.newline();
        } else {
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("semicolon"), printData));
            jjtAcceptSpecial(node, printData, "semicolon");
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
    public Object visit(ASTTypeDeclaration node, Object data)
    {
        if (node.hasAnyChildren()) {
            node.childrenAccept(this, data);
        }
        else {
            //  Get the data
            PrintData printData = (PrintData) data;
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("semicolon"), printData));
            jjtAcceptSpecial(node, printData, "semicolon");
            printData.appendText(";");
            printData.newline();
        }
        return data;
    }

   private static final String[] CLASS_DECLARATION_MODIFIERS = new String[] { "final", "public", "abstract", "strictfp" };

    /**
     *  Visit a class declaration
     *
     *@param  node  the class declaration
     *@param  data  the print data
     *@return       the result of visiting this node
     */
    public Object visit(ASTClassDeclaration node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Add blank lines before the class
        for (int ndx = 0; ndx < printData.getLinesBeforeClass(); ndx++) {
            printData.newline();
        }

        //  Print any tokens
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("final"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("public"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("abstract"), printData));
        jjtAcceptSpecials(node, printData, CLASS_DECLARATION_MODIFIERS);

        //  Get the child
        SimpleNode child = (SimpleNode) node.jjtGetChild(0);
        node.jjtAccept(specialTokenVisitor, new SpecialTokenData(child.getSpecial("class"), printData));

        //  Make sure that the javadoc stuff is there
        if (node.isRequired()) {
            node.finish();
            node.printJavaDocComponents(printData);
        }

        //  Indent and insert the modifiers
        printData.indent();
        printData.appendKeyword(node.getModifiersString(printData.getModifierOrder()));

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
    public Object visit(ASTUnmodifiedClassDeclaration node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        // print the Attribute(s) before the "class" token.
        int child = 0;
        if (node.jjtGetChild(0) instanceof ASTAttribute) {
            node.jjtGetChild(0).jjtAccept(this, data);
            child++; // we've now handled any attributes present
        }

        //  Print any tokens
        printData.appendKeyword("class");
        printData.space();
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("id"), printData));
        jjtAcceptSpecial(node, printData, "id");
        printData.appendText(node.getName());
        printData.pushCurrentClassName(node.getName());

        //  Traverse the children
        int lastIndex = node.jjtGetNumChildren();
        for (int ndx = child; ndx < lastIndex; ndx++) {
            Node next = node.jjtGetChild(ndx);
            if (next instanceof ASTTypeParameterList) {
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("extends"), printData));
                jjtAcceptSpecial(node, printData, "extends");
                printData.space();
                next.jjtAccept(this, data);
            }
            else if (next instanceof ASTName) {
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("extends"), printData));
                jjtAcceptSpecial(node, printData, "extends");
                //  Add blank line before extends
                if (printData.isLineBeforeExtends()) {
                    int indentation = printData.getExtendsIndentation();
                    printData.incrIndent(indentation);
                    printData.indent();
                    printData.incrIndent(-indentation);
                }
                else {
                    printData.space();
                }
                printData.appendKeyword("extends");
                printData.space();
                next.jjtAccept(this, data);
            }
            else if (next instanceof ASTNameList) {
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("implements"), printData));
                jjtAcceptSpecial(node, printData, "implements");
                //  Add blank line before implements
                if (printData.isLineBeforeImplements()) {
                    int indentation = printData.getImplementsIndentation();
                    printData.incrIndent(indentation);
                    printData.indent();
                    printData.incrIndent(-indentation);
                }
                else {
                    printData.space();
                }
                printData.appendKeyword("implements");
                printData.space();
                next.jjtAccept(this, data);
            }
            else {
                next.jjtAccept(this, data);
            }
        }

        //  Finish the class
        printData.popCurrentClassName();

        //  Return the data
        return data;
    }


    /**
     *  Visits a class body node
     *
     *@param  node  The node we are visiting
     *@param  data  the print data
     *@return       the print data
     */
    public Object visit(ASTClassBody node, Object data)
    {
        return visit(node, data, true);
    }


    /**
     *  Visits a class body node
     *
     *@param  node     The node we are visiting
     *@param  data     The print data
     *@param  newline  Whether there should be a new line
     *@return          the print data
     */
    public Object visit(ASTClassBody node, Object data, boolean newline)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Sort it
        node.sort(printData.getOrder());

        FieldSizeLookAhead fsla = new FieldSizeLookAhead(printData.getFieldSpaceCode());
        FieldSize size = fsla.run(node);
        size.update(printData.getDynamicFieldSpaces());
        printData.pushFieldSize(size);

        //  Print any tokens
        printData.classBrace();
        printData.beginBlock();
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("begin"), printData, false));
        jjtAcceptSpecial(node, printData, "begin", false);

        //  Traverse the children
        node.childrenAccept(this, data);

        //  Print any tokens
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("end"), printData));
        jjtAcceptSpecial(node, printData, "end");

        printData.classBrace();
        printData.endBlock(newline, true);

        printData.popFieldSize();

        //  Return the data
        return data;
    }


   private static final String[] NESTED_CLASS_MODIFIERS = new String[] { "static", "abstract", "strictfp", "final", "public", "protected", "private" };
    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTNestedClassDeclaration node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Print any tokens
        printData.beginClass();
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("final"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("public"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("protected"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("private"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("abstract"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("static"), printData));
        jjtAcceptSpecials(node, printData, NESTED_CLASS_MODIFIERS);

        //  Get the child
        SimpleNode child = (SimpleNode) node.jjtGetChild(0);
        node.jjtAccept(specialTokenVisitor, new SpecialTokenData(child.getSpecial("class"), printData));

        //  Make sure the java doc is there
        if (isJavadocRequired(node, printData)) {
            node.finish();
            node.printJavaDocComponents(printData);
        }

        //  Indent and include the modifiers
        printData.indent();
        printData.appendKeyword(node.getModifiersString(printData.getModifierOrder()));

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
    public Object visit(ASTClassBodyDeclaration node, Object data)
    {
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
    public Object visit(ASTMethodDeclarationLookahead node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }


   private static final String[] INTERFACE_MODIFIERS = new String[] { "strictfp", "abstract", "public" };
    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTInterfaceDeclaration node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Add blank lines before the interface
        for (int ndx = 0; ndx < printData.getLinesBeforeClass(); ndx++) {
            printData.newline();
        }

        //  Print any special tokens
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("public"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("abstract"), printData));
        jjtAcceptSpecials(node, printData, INTERFACE_MODIFIERS);

        //  Get the child
        SimpleNode child = (SimpleNode) node.jjtGetChild(0);
        node.jjtAccept(specialTokenVisitor, new SpecialTokenData(child.getSpecial("interface"), printData));

        //  Include the java doc comments
        if (node.isRequired()) {
            node.finish();
            node.printJavaDocComponents(printData);
        }

        //  Indent and add the modifiers
        printData.indent();
        printData.appendKeyword(node.getModifiersString(printData.getModifierOrder()));

        //  Traverse the children
        node.childrenAccept(this, data);

        //  Return the data
        return data;
    }


   private static final String[] NESTED_INTERFACE_MODIFIERS = new String[] { "static", "strictfp", "abstract", "final", "public", "protected", "private" };
    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTNestedInterfaceDeclaration node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Print any tokens
        printData.beginInterface();
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("static"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("abstract"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("final"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("public"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("protected"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("private"), printData));
        jjtAcceptSpecials(node, printData, NESTED_INTERFACE_MODIFIERS);

        //  Get the child
        SimpleNode child = (SimpleNode) node.jjtGetChild(0);
        node.jjtAccept(specialTokenVisitor, new SpecialTokenData(child.getSpecial("interface"), printData));

        //  Force the Javadoc to be included
        if (isJavadocRequired(node, printData)) {
            node.finish();
            node.printJavaDocComponents(printData);
        }

        //  Indent and include the modifiers
        printData.indent();
        printData.appendKeyword(node.getModifiersString(printData.getModifierOrder()));

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
    public Object visit(ASTUnmodifiedInterfaceDeclaration node, Object data)
    {
        //  Local Variables
        boolean first = true;

        //  Get the data
        PrintData printData = (PrintData) data;

        // print the Attribute(s) before the "interface" token.
        int child = 0;
        if (node.jjtGetChild(0) instanceof ASTAttribute) {
            node.jjtGetChild(0).jjtAccept(this, data);
            child++; // we've now handled any attributes present
        }

        //  Print any tokens
        printData.appendKeyword("interface");
        printData.space();
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("id"), printData));
        jjtAcceptSpecial(node, printData, "id");
        printData.appendText(node.getName());
        printData.pushCurrentClassName(node.getName());

        //  Traverse the children
        Node next = node.jjtGetChild(child++);
        if (next instanceof ASTNameList) {
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("extends"), printData));
            jjtAcceptSpecial(node, printData, "extends");
            //  Add blank line before extends
            if (printData.isLineBeforeExtends()) {
                int indentation = printData.getExtendsIndentation();
                printData.incrIndent(indentation);
                printData.indent();
                printData.incrIndent(-indentation);
            }
            else {
                printData.space();
            }
            printData.appendKeyword("extends");
            printData.space();
            next.jjtAccept(this, data);

            //  Get the next node
            next = node.jjtGetChild(child++);
        }

        //  Handle the interface body
        next.jjtAccept(this, data);

        //  Finish
        printData.popCurrentClassName();

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
    public Object visit(ASTInterfaceBody node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        FieldSizeLookAhead fsla = new FieldSizeLookAhead(printData.getFieldSpaceCode());
        FieldSize size = fsla.run(node);
        size.update(printData.getDynamicFieldSpaces());
        printData.pushFieldSize(size);

        //  Begin the block
        printData.classBrace();
        printData.beginBlock();
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("begin"), printData, false));
        jjtAcceptSpecial(node, printData, "begin", false);

        //  Travers the children
        node.childrenAccept(this, data);

        //  End the block
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("end"), printData));
        jjtAcceptSpecial(node, printData, "end");

        printData.classBrace();
        printData.endBlock();

        printData.popFieldSize();

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
    public Object visit(ASTInterfaceMemberDeclaration node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }


   private static final String[] FIELD_MODIFIERS = new String[] { "static", "transient", "volatile", "final", "public", "protected", "private" };
    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTFieldDeclaration node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        ;
        int child = 0;
        if (node.jjtGetChild(0) instanceof ASTAttribute) {
            child++;
        }
        ASTType type = (ASTType) node.jjtGetChild(child);

        //  Print any tokens
        printData.beginField();
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("static"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("transient"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("volatile"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("final"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("public"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("protected"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("private"), printData));
        jjtAcceptSpecials(node, printData, FIELD_MODIFIERS);
        node.jjtAccept(specialTokenVisitor, new SpecialTokenData(getInitialToken(type), printData));
        if (isJavadocRequired(node, printData)) {
            node.finish();
            node.printJavaDocComponents(printData);
        }
        printData.indent();
        String modifiers = node.getModifiersString(printData.getModifierOrder());
        printData.appendKeyword(modifiers);
        if (printData.isDynamicFieldSpacing(node.isJavadocPrinted())) {
            FieldSize size = printData.topFieldSize();
            int maxModifier = size.getModifierLength();
            for (int ndx = modifiers.length(); ndx < maxModifier; ndx++) {
                printData.space();
            }
        }

        //  Handle the first two/three children (which are required)
        if (child == 1) {
            // handle the Attibute(s)
            Node next = node.jjtGetChild(0);
            next.jjtAccept(this, data);
        }
        Node next = node.jjtGetChild(child);
        next.jjtAccept(this, data);
        if (printData.isDynamicFieldSpacing(node.isJavadocPrinted())) {
            int current = fsla.computeTypeLength(node);

            FieldSize size = printData.topFieldSize();
            int maxType = size.getTypeLength();
            for (int ndx = current; ndx < maxType; ndx++) {
                printData.space();
            }
        }

        if (printData.getFieldSpaceCode() == PrintData.DFS_ALIGN_EQUALS) {
            printData.setTempEqualsLength(fsla.computeEqualsLength(node));
        }

        standardFieldIndent(printData);
        next = node.jjtGetChild(child + 1);
        printData.setStoreJavadocPrinted(node.isJavadocPrinted());
        next.jjtAccept(this, data);
        printData.setStoreJavadocPrinted(false);

        //  Traverse the rest of the children
        int lastIndex = node.jjtGetNumChildren();
        for (int ndx = child + 2; ndx < lastIndex; ndx++) {
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("comma." + (ndx - (child + 2))), printData));
            jjtAcceptSpecial(node, printData, "comma." + (ndx - (child + 2)));
            printData.appendText(", ");
            next = node.jjtGetChild(ndx);
            next.jjtAccept(this, data);
        }

        //  Finish the entry
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("semicolon"), printData));
        jjtAcceptSpecial(node, printData, "semicolon");
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
    public Object visit(ASTVariableDeclarator node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Handle the first child (which is required)
        Node next = node.jjtGetChild(0);
        next.jjtAccept(this, data);

        if ((printData.isDynamicFieldSpacing(printData.isStoreJavadocPrinted())) &&
                (node.jjtGetNumChildren() > 1)) {
            int current = ((ASTVariableDeclaratorId) next).getName().length();

            FieldSize size = printData.topFieldSize();
            int maxName = size.getNameLength();
            for (int ndx = current; ndx < maxName; ndx++) {
                printData.space();
            }
        }
        if ((printData.getFieldSpaceCode() == PrintData.DFS_ALIGN_EQUALS) &&
                (node.jjtGetNumChildren() > 1) &&
                !printData.getSkipNameSpacing()) {
            int current = printData.getTempEqualsLength();

            FieldSize size = printData.topFieldSize();
            int maxName = size.getEqualsLength();
            for (int ndx = current; ndx < maxName; ndx++) {
                printData.space();
            }
        }
        printData.setStoreJavadocPrinted(false);

        //  Traverse the rest of the children
        if (node.jjtGetNumChildren() > 1) {
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("equals"), printData));
            jjtAcceptSpecial(node, printData, "equals");
            if (printData.isSpaceAroundOperators()) {
                printData.space();
            }
            printData.appendText("=");
            if (printData.isSpaceAroundOperators()) {
                printData.space();
            }
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
    public Object visit(ASTVariableDeclaratorId node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Handle the first child (which is required)
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("id"), printData));
        jjtAcceptSpecial(node, printData, "id");
        printData.appendText(node.getName());
        int last = node.getArrayCount();
        for (int ndx = 0; ndx < last; ndx++) {
            if (node.jjtGetChild(ndx) instanceof ASTVariance) {
                node.jjtGetChild(ndx).jjtAccept(this, data);
            }
            else {
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("[." + ndx), printData));
                jjtAcceptSpecial(node, printData, "[." + ndx);
                printData.appendText("[");
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("]." + ndx), printData));
                jjtAcceptSpecial(node, printData, "]." + ndx);
                printData.appendText("]");
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
    public Object visit(ASTVariableInitializer node, Object data)
    {
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
    public Object visit(ASTArrayInitializer node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        int incrBy = 3;
        //  Handle the first child (which is required)
        if (!printData.isArrayInitializerIndented()) {
            incrBy = (printData.getIndent() <= 1) ? 1 : 0;
            if (incrBy == 1) {
                printData.decrIndent();
            }
            else if (printData.getSurpriseReturn() == PrintData.SINGLE_INDENT) {
                printData.decrIndent();
            }
            else if (printData.getSurpriseReturn() == PrintData.DOUBLE_INDENT) {
                printData.decrIndent();
                printData.decrIndent();
            }
            else if (printData.getSurpriseReturn() == printData.PARAM_INDENT) {
                printData.decrIndent();
            }
        }
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("begin"), printData));
        jjtAcceptSpecial(node, printData, "begin");
        printData.appendText("{");
        if (printData.isIndentInInitailzer()) {
            printData.incrIndent();
        }

        int last = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < last; ndx++) {
            if (ndx > 0) {
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("comma." + (ndx - 1)), printData));
                jjtAcceptSpecial(node, printData, "comma." + (ndx-1));
                printData.appendText(", ");
            }
            Node child = node.jjtGetChild(ndx);
            child.jjtAccept(this, data);
        }
        if (node.isFinalComma()) {
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("comma." + (last - 1)), printData));
            jjtAcceptSpecial(node, printData, "comma." + (last-1));
            printData.appendText(",");
        }

        if (printData.isIndentInInitailzer()) {
            printData.decrIndent();
        }

        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("end"), printData));
        jjtAcceptSpecial(node, printData, "end");
        printData.appendText("}");
        if (!printData.isArrayInitializerIndented()) {
            if (incrBy == 1) {
                printData.incrIndent();
            }
            else if (printData.getSurpriseReturn() == PrintData.SINGLE_INDENT) {
                printData.incrIndent();
            }
            else if (printData.getSurpriseReturn() == PrintData.DOUBLE_INDENT) {
                printData.incrIndent();
                printData.incrIndent();
            }
            else if (printData.getSurpriseReturn() == printData.PARAM_INDENT) {
                printData.incrIndent();
            }
        }

        //  Return the data
        return data;
    }


   private static final String[] METHOD_MODIFIERS = new String[] { "synchronized", "static", "native", "volatile", "final", "public", "protected", "private" };
    /**
     *  Pretty prints the method declaration
     *
     *@param  node  the node in the parse tree
     *@param  data  the print data
     *@return       the print data
     */
    public Object visit(ASTMethodDeclaration node, Object data) {
        PrintData printData = (PrintData) data;  //  Get the data

        //  Print any tokens
        printData.beginMethod();
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("public"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("protected"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("private"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("static"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("abstract"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("final"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("native"), printData));
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("synchronized"), printData));
        jjtAcceptSpecials(node, printData, METHOD_MODIFIERS);
        int child = 0;
        if (node.jjtGetChild(0) instanceof ASTAttribute) {
            child++;
        }
        if (node.jjtGetChild(child) instanceof ASTTypeParameters) {
            child++;
        }
        node.jjtAccept(specialTokenVisitor, new SpecialTokenData(getInitialToken((ASTResultType) node.jjtGetChild(child)), printData));
        if (isJavadocRequired(node, printData)) {
            node.finish(printData.getCurrentClassName());
            node.printJavaDocComponents(printData);
        }
        printData.indent();
        printData.appendKeyword(node.getModifiersString(printData.getModifierOrder()));

        //  Handle the Attribute/TypeParameter declaration (if present)
        for (int i = 0; i < child; i++) {
            //node.jjtGetChild(i).jjtAccept(this, data);
            Node next = node.jjtGetChild(i);
            next.jjtAccept(this, data);
            if (next instanceof ASTTypeParameters) {
                printData.space();
            }
        }
        // Handle return param
        Node next = node.jjtGetChild(child++);
        next.jjtAccept(this, data);
        printData.space();
        // Handle the method name
        next = node.jjtGetChild(child++);
        next.jjtAccept(this, data);

        //  Traverse the rest of the children
        int lastIndex = node.jjtGetNumChildren();
        boolean foundBlock = false;
        for (int ndx = child; ndx < lastIndex; ndx++) {
            next = node.jjtGetChild(ndx);
            if (next instanceof ASTNameList) {
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("throws"), printData));
                jjtAcceptSpecial(node, printData, "throws");
                printData.space();
                if (printData.isThrowsOnNewline()) {
                    printData.incrIndent();
                    printData.indent();
                    printData.decrIndent();
                }
                printData.appendKeyword("throws");
                printData.space();
                next.jjtAccept(this, data);
            }
            else if (next instanceof ASTBlock) {
                foundBlock = true;
                if ((next.jjtGetNumChildren() == 0) &&
                        printData.isEmptyBlockOnSingleLine() &&
                        !isCommentsPresent((ASTBlock) next)) {
                    printData.appendText(" { }");
                    printData.newline();
                }
                else {
                    printData.methodBrace();
                    next.jjtAccept(this, data);
                }
            }
        }

        //  Finish if it is abstract
        if (!foundBlock) {
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("semicolon"), printData));
            jjtAcceptSpecial(node, printData, "semicolon");
            printData.appendText(";");
            printData.newline();
        }

        //  Note the end of the method
        printData.endMethod();

        return data;  //  Return the data
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTMethodDeclarator node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Handle the first child (which is required)
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("id"), printData));
        jjtAcceptSpecial(node, printData, "id");
        printData.appendText(node.getName());
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
    public Object visit(ASTFormalParameters node, Object data) {
        PrintData printData = (PrintData) data;  //  Get the data

        //  Print any tokens
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("begin"), printData));
        jjtAcceptSpecial(node, printData, "begin");
        if (printData.isSpaceAfterMethod() && node.hasAnyChildren()) {
            printData.space();
        }
        printData.beginExpression(node.hasAnyChildren());
        printData.enterMethodDecl();

        //  Traverse the children
        Node next;
        int lastIndex = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < lastIndex; ndx++) {
            if (ndx > 0) {
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("comma." + (ndx - 1)), printData));
                jjtAcceptSpecial(node, printData, "comma." + (ndx - 1));
                printData.appendText(", ");
            }
            next = node.jjtGetChild(ndx);
            next.jjtAccept(this, data);
        }

        //  Finish it
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("end"), printData));
        jjtAcceptSpecial(node, printData, "end");
        printData.endExpression(node.hasAnyChildren());

        printData.exitMethodDecl();

        return data;  //  Return the data
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTFormalParameter node, Object data) {
        PrintData printData = (PrintData) data; //  Get the data

        //  Print any tokens
        if (node.isUsingFinal()) {
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("final"), printData));
            jjtAcceptSpecial(node, printData, "final");
            printData.appendKeyword("final");
            printData.space();
        }

        printData.setParamIndent();

        //  Traverse the children
        Node next = node.jjtGetChild(0);
        next.jjtAccept(this, data);
        printData.space();
        next = node.jjtGetChild(1);
        next.jjtAccept(this, data);

        return data;  //  Return the data
    }


   private static final String[] CONSTRUCTOR_MODIFIERS = new String[] { "public", "protected", "private", "id" };
    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTConstructorDeclaration node, Object data) {
        PrintData printData = (PrintData) data; //  Get the data

        //  Print any tokens
        printData.beginMethod();
        jjtAcceptSpecials(node, printData, CONSTRUCTOR_MODIFIERS);
        if (node.isRequired()) {
            node.finish();
            node.printJavaDocComponents(printData);
        }
        printData.indent();
        printData.appendKeyword(node.getModifiersString(printData.getModifierOrder()));
        int child = 0;
        if (node.jjtGetChild(0) instanceof ASTAttribute) {
            Node next = node.jjtGetChild(0);
            next.jjtAccept(this, data);
            child++;
        }

        if (node.jjtGetChild(child) instanceof ASTTypeParameters) {
            Node next = node.jjtGetChild(child);
            next.jjtAccept(this, data);
            child++;
        }

        printData.appendText(node.getName());

        //  Handle the return type (which is required)
        Node next = node.jjtGetChild(child);
        next.jjtAccept(this, data);

        //  Get the last index
        int lastIndex = node.jjtGetNumChildren();
        child++;

        //  Handle the name list if it is present
        if (lastIndex > child) {
            next = node.jjtGetChild(child);
            if (next instanceof ASTNameList) {
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("throws"), printData));
                jjtAcceptSpecial(node, printData, "throws");
                printData.space();
                if (printData.isThrowsOnNewline()) {
                    printData.incrIndent();
                    printData.indent();
                    printData.decrIndent();
                }
                printData.appendKeyword("throws");
                printData.space();
                next.jjtAccept(this, data);
                child++;
            }
        }

        //  Print the starting block
        if ((lastIndex == child) && printData.isEmptyBlockOnSingleLine()) {
            printData.appendText(" { }");
            printData.newline();
        }
        else {
            LocalVariableLookAhead lvla = new LocalVariableLookAhead();
            FieldSize size = lvla.run(node);
            size.update(printData.getDynamicFieldSpaces());
            printData.pushFieldSize(size);

            printData.methodBrace();
            printData.beginBlock();
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("begin"), printData, false));
            jjtAcceptSpecial(node, printData, "begin", false);

            if (printData.isLineBeforeMultistatementMethodBody() &&
                    (lastIndex - child > 1) &&
                    (printData.getMethodBlockStyle() == PrintData.BLOCK_STYLE_C)) {
                printData.newline();
            }

            //  Traverse the rest of the children
            boolean foundBlock = false;
            for (int ndx = child; ndx < lastIndex; ndx++) {
                next = node.jjtGetChild(ndx);
                next.jjtAccept(this, data);
            }

            //  Print the end block
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("end"), printData));
            jjtAcceptSpecial(node, printData, "end");

            printData.methodBrace();
            printData.endBlock();
        }
        printData.endMethod();

        return data;  //  Return the data
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTExplicitConstructorInvocation node, Object data) {
        PrintData printData = (PrintData) data;  //  Get the data

        //  Print the name of the node
        int startWith = 0;
        printData.indent();
        if (node.jjtGetChild(0) instanceof ASTPrimaryExpression) {
            node.jjtGetChild(0).jjtAccept(this, data);
            startWith++;
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("."), printData));
            jjtAcceptSpecial(node, printData, ".");
            printData.appendText(".");
        }
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("explicit"), printData));
        jjtAcceptSpecial(node, printData, "explicit");
        printData.appendText(node.getName());

        //  Traverse the children
        int last = node.jjtGetNumChildren();
        for (int ndx = startWith; ndx < last; ndx++) {
            node.jjtGetChild(ndx).jjtAccept(this, data);
        }

        //  End of the line
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("semicolon"), printData));
        jjtAcceptSpecial(node, printData, "semicolon");
        printData.appendText(";");
        printData.newline();

        return data;   //  Return the data
    }


    /**
     *  Formats the initializer
     *
     *@param  node  The initializer node
     *@param  data  The print data
     *@return       Nothing interesting
     */
    public Object visit(ASTInitializer node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Print the name of the node
        printData.indent();
        if (node.isUsingStatic()) {
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("static"), printData));
            jjtAcceptSpecial(node, printData, "static");
            printData.appendKeyword("static");
        }

        //  Traverse the children
        ASTBlock block = (ASTBlock) node.jjtGetChild(0);
        blockProcess(block, printData, true, node.isUsingStatic());

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
    public Object visit(ASTType node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Traverse the children
        //node.childrenAccept(this, data);
        if (node.jjtGetChild(0) != null) {
            node.jjtGetChild(0).jjtAccept(this, data);
        }

        //  Add the array
        int count = node.getArrayCount();
        for (int ndx = 0; ndx < count; ndx++) {
            if (node.jjtGetChild(ndx + 1) instanceof ASTVariance) {
                node.jjtGetChild(ndx + 1).jjtAccept(this, data);
            }
            else {
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("[." + ndx), printData));
                jjtAcceptSpecial(node, printData, "[." + ndx);
                printData.appendText("[");
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("]." + ndx), printData));
                jjtAcceptSpecial(node, printData, "]." + ndx);
                printData.appendText("]");
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
    public Object visit(ASTPrimitiveType node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Print the name of the node
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("primitive"), printData));
        jjtAcceptSpecial(node, printData, "primitive");
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
    public Object visit(ASTResultType node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Traverse the children
        if (node.hasAnyChildren()) {
            node.childrenAccept(this, data);
        }
        else {
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("primitive"), printData));
            jjtAcceptSpecial(node, printData, "primitive");
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
    public Object visit(ASTName node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Print the name of the node
        int size = node.getNameSize();
        for (int ndx = 0; ndx < size; ndx++) {
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("id" + ndx), printData));
            jjtAcceptSpecial(node, printData, "id" + ndx);
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("period" + ndx), printData));
            jjtAcceptSpecial(node, printData, "period" + ndx);
        }
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
    public Object visit(ASTNameList node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Traverse the children
        int countChildren = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < countChildren; ndx++) {
            if (ndx > 0) {
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("comma." + (ndx - 1)), printData));
                jjtAcceptSpecial(node, printData, "comma." + (ndx - 1));
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
    public Object visit(ASTExpression node, Object data)
    {
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
    public Object visit(ASTAssignmentOperator node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Print the name of the node
        if (printData.isSpaceAroundOperators()) {
            printData.space();
        }
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("operator"), printData));
        jjtAcceptSpecial(node, printData, "operator");
        printData.appendText(node.getName());
        if (printData.isSpaceAroundOperators()) {
            printData.space();
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
    public Object visit(ASTConditionalExpression node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Traverse the children
        int childCount = node.jjtGetNumChildren();
        if (childCount == 1) {
            node.jjtGetChild(0).jjtAccept(this, data);
        }
        else {
            node.jjtGetChild(0).jjtAccept(this, data);
            if (printData.isSpaceAroundOperators()) {
                printData.space();
            }
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("?"), printData));
            jjtAcceptSpecial(node, printData, "?");
            printData.appendText("?");
            if (printData.isSpaceAroundOperators()) {
                printData.space();
            }
            node.jjtGetChild(1).jjtAccept(this, data);
            if (printData.isSpaceAroundOperators()) {
                printData.space();
            }
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial(":"), printData));
            jjtAcceptSpecial(node, printData, ":");
            printData.appendText(":");
            if (printData.isSpaceAroundOperators()) {
                printData.space();
            }
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
    public Object visit(ASTConditionalOrExpression node, Object data)
    {
        return binaryExpression(node, "||", data);
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTConditionalAndExpression node, Object data)
    {
        return binaryExpression(node, "&&", data);
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTInclusiveOrExpression node, Object data)
    {
        return binaryExpression(node, "|", data);
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTExclusiveOrExpression node, Object data)
    {
        return binaryExpression(node, "^", data);
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTAndExpression node, Object data)
    {
        return binaryExpression(node, "&", data);
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTEqualityExpression node, Object data)
    {
        return binaryExpression(node, node.getNames(), data);
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTInstanceOfExpression node, Object data)
    {
        return binaryExpression(node, "instanceof", data);
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTRelationalExpression node, Object data)
    {
        return binaryExpression(node, node.getNames(), data);
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTShiftExpression node, Object data)
    {
        return binaryExpression(node, node.getNames(), data);
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTAdditiveExpression node, Object data)
    {
        return binaryExpression(node, node.getNames(), data);
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTMultiplicativeExpression node, Object data)
    {
        return binaryExpression(node, node.getNames(), data);
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTUnaryExpression node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Traverse the children
        Node child = node.jjtGetChild(0);
        if (child instanceof ASTUnaryExpression) {
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("operator"), printData));
            jjtAcceptSpecial(node, printData, "operator");
            printData.appendText(node.getName());
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
    public Object visit(ASTPreIncrementExpression node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Include the preincrement
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("operator"), printData));
        jjtAcceptSpecial(node, printData, "operator");
        printData.appendText("++");

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
    public Object visit(ASTPreDecrementExpression node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Include the preincrement
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("operator"), printData));
        jjtAcceptSpecial(node, printData, "operator");
        printData.appendText("--");

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
    public Object visit(ASTUnaryExpressionNotPlusMinus node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Traverse the children
        Node child = node.jjtGetChild(0);
        if (child instanceof ASTUnaryExpression) {
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("operator"), printData));
            jjtAcceptSpecial(node, printData, "operator");
            printData.appendText(node.getName());
            if (node.getName().equals("!") && printData.isBangSpace()) {
                printData.space();
            }
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
    public Object visit(ASTCastLookahead node, Object data)
    {
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
    public Object visit(ASTPostfixExpression node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Traverse the children
        node.childrenAccept(this, data);

        //  Include the increment
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("operator"), printData));
        jjtAcceptSpecial(node, printData, "operator");
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
    public Object visit(ASTCastExpression node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Cast portion
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("begin"), printData));
        jjtAcceptSpecial(node, printData, "begin");
        printData.beginExpression(printData.isCastSpace());
        if (printData.isSpaceInsideCast()) {
            printData.space();
        }
        node.jjtGetChild(0).jjtAccept(this, data);
        if (printData.isSpaceInsideCast()) {
            printData.space();
        }
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("end"), printData));
        jjtAcceptSpecial(node, printData, "end");
        printData.endExpression(printData.isCastSpace());
        if (printData.isSpaceAfterCast()) {
            printData.space();
        }

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
    public Object visit(ASTPrimaryExpression node, Object data)
    {
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
    public Object visit(ASTPrimaryPrefix node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        for (int ndx = 0; ndx < node.getCount(); ndx++) {
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("this." + ndx), printData));
            jjtAcceptSpecial(node, printData, "this." + ndx);
        }

        //  Traverse the children
        if (node.jjtGetNumChildren() == 0) {
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("this"), printData));
            jjtAcceptSpecial(node, printData, "this");
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("id"), printData));
            jjtAcceptSpecial(node, printData, "id");
            printData.appendText(node.getName());
        }
        else {
            Node child = node.jjtGetChild(0);
            if ((child instanceof ASTLiteral) ||
                    (child instanceof ASTName) ||
                    (child instanceof ASTAllocationExpression)) {
                child.jjtAccept(this, data);
            }
            else if (child instanceof ASTExpression) {
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("begin"), printData));
                jjtAcceptSpecial(node, printData, "begin");
                if (printData.isSpaceAfterMethod() && node.hasAnyChildren() && printData.isSpaceAroundOperators()) {
                    printData.space();
                }
                printData.beginExpression(node.hasAnyChildren());
                child.jjtAccept(this, data);
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("end"), printData));
                jjtAcceptSpecial(node, printData, "end");
                printData.endExpression(node.hasAnyChildren());
            }
            else if (child instanceof ASTResultType) {
                child.jjtAccept(this, data);
                printData.appendText(".class");
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
    public Object visit(ASTPrimarySuffix node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Traverse the children
        if (node.jjtGetNumChildren() == 0) {
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("dot"), printData));
            jjtAcceptSpecial(node, printData, "dot");
            printData.appendText(".");
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("id"), printData));
            jjtAcceptSpecial(node, printData, "id");
            printData.appendText(node.getName());
        }
        else {
            Node child = node.jjtGetChild(0);
            if (child instanceof ASTArguments) {
                child.jjtAccept(this, data);
            }
            else if (child instanceof ASTExpression) {
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("["), printData));
                jjtAcceptSpecial(node, printData, "[");
                printData.appendText("[");
                child.jjtAccept(this, data);
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("]"), printData));
                jjtAcceptSpecial(node, printData, "]");
                printData.appendText("]");
            }
            else if (child instanceof ASTAllocationExpression) {
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("dot"), printData));
                jjtAcceptSpecial(node, printData, "dot");
                printData.appendText(".");
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("id"), printData));
                jjtAcceptSpecial(node, printData, "id");
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
    public Object visit(ASTLiteral node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Traverse the children
        if (node.hasAnyChildren()) {
            node.childrenAccept(this, data);
        }
        else {
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("id"), printData));
            jjtAcceptSpecial(node, printData, "id");
            printData.appendConstant(node.getName());
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
    public Object visit(ASTBooleanLiteral node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Print the data
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("id"), printData));
        jjtAcceptSpecial(node, printData, "id");
        printData.appendConstant(node.getName());

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
    public Object visit(ASTNullLiteral node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Print the data
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("id"), printData));
        jjtAcceptSpecial(node, printData, "id");
        printData.appendConstant("null");

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
    public Object visit(ASTArguments node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Start the parens
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("begin"), printData));
        jjtAcceptSpecial(node, printData, "begin");
        if (printData.isSpaceAfterMethod() && node.hasAnyChildren()) {
            printData.space();
        }
        printData.beginExpression(node.hasAnyChildren());

        //  Traverse the children
        node.childrenAccept(this, data);

        //  Finish the parens
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("end"), printData));
        jjtAcceptSpecial(node, printData, "end");
        printData.endExpression(node.hasAnyChildren());

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
    public Object visit(ASTArgumentList node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Traverse the children
        int count = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < count; ndx++) {
            if (ndx > 0) {
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("comma." + (ndx - 1)), printData));
                jjtAcceptSpecial(node, printData, "comma." + (ndx - 1));
                printData.appendText(", ");
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
    public Object visit(ASTAllocationExpression node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Traverse the children
        if (node.jjtGetChild(node.jjtGetNumChildren() - 1) instanceof ASTClassBody) {
            //  Indenting
            int lastType = printData.getState();
            printData.setState(PrintData.EMPTY);
            printData.incrIndent();

            //  Print the name of the node
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("id"), printData));
            jjtAcceptSpecial(node, printData, "id");
            printData.indent();
            printData.appendKeyword("new");
            printData.space();

            //  Traverse the children
            SimpleNode nameNode = (SimpleNode) node.jjtGetChild(0);
            nameNode.jjtAccept(this, data);
            int x = 0;
            if (node.jjtGetChild(1) instanceof ASTTypeArguments) {
                x++;
                ASTTypeArguments typeArguments = (ASTTypeArguments) node.jjtGetChild(1);
                typeArguments.jjtAccept(this, data);
            }

            SimpleNode arguments = (SimpleNode) node.jjtGetChild(1 + x);
            arguments.jjtAccept(this, data);

            ASTClassBody classBody = (ASTClassBody) node.jjtGetChild(2 + x);
            visit(classBody, data, false);

            //  Cleanup
            printData.decrIndent();
            //printData.indent();
            printData.setState(lastType);
        }
        else {
            //  Print the name of the node
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("id"), printData));
            jjtAcceptSpecial(node, printData, "id");
            printData.appendKeyword("new");
            printData.space();

            //  Traverse the children
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
    public Object visit(ASTArrayDimsAndInits node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Traverse the children
        boolean foundInitializer = false;
        int last = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < last; ndx++) {
            if (node.jjtGetChild(ndx) instanceof ASTExpression) {
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("[." + ndx), printData));
                jjtAcceptSpecial(node, printData, "[." + ndx);
                printData.appendText("[");
                node.jjtGetChild(ndx).jjtAccept(this, data);
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("]." + ndx), printData));
                jjtAcceptSpecial(node, printData, "]." + ndx);
                printData.appendText("]");
            }
            else if (node.jjtGetChild(ndx) instanceof ASTVariance) {
                node.jjtGetChild(ndx).jjtAccept(this, data);
            }
            else if (node.jjtGetChild(ndx) instanceof ASTArrayInitializer) {
                foundInitializer = true;
            }
        }
        int looping = node.getArrayCount();
        if (foundInitializer) {
            looping++;
        }

        for (int ndx = last; ndx < looping; ndx++) {
            if (node.jjtGetChild(ndx) instanceof ASTVariance) {
                node.jjtGetChild(ndx).jjtAccept(this, data);
            }
            else {
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("[." + ndx), printData));
                jjtAcceptSpecial(node, printData, "[." + ndx);
                printData.appendText("[");
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("]." + ndx), printData));
                jjtAcceptSpecial(node, printData, "]." + ndx);
                printData.appendText("]");
            }
        }
        if (foundInitializer) {
            node.jjtGetChild(last - 1).jjtAccept(this, data);
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
    public Object visit(ASTStatement node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Traverse the children
        printData.indent();
        node.childrenAccept(this, data);
        if (node.jjtGetChild(0) instanceof ASTStatementExpression) {
            //  Finish off the statement expression
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("semicolon"), printData));
            jjtAcceptSpecial(node, printData, "semicolon");
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
    public Object visit(ASTLabeledStatement node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Print the data
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("id"), printData));
        jjtAcceptSpecial(node, printData, "id");
        printData.appendText(node.getName());
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("colon"), printData));
        jjtAcceptSpecial(node, printData, "colon");
        printData.appendText(": ");

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
    public Object visit(ASTBlock node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        blockProcess(node, printData, true);

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
    public Object visit(ASTBlockStatement node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Include the stuff before the class/interface declaration
        if (node.hasAnyChildren()) {
            SimpleNode child = (SimpleNode) node.jjtGetChild(0);

            if (child instanceof ASTUnmodifiedClassDeclaration) {
                printData.beginClass();
                if (node.isFinal()) {
                    //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("final"), printData));
                    jjtAcceptSpecial(node, printData, "final");
                }
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(child.getSpecial("class"), printData));
                jjtAcceptSpecial(node, printData, "class");
                printData.indent();
                if (node.isFinal()) {
                    printData.appendKeyword("final");
                    printData.space();
                }
            }
            else if (child instanceof ASTUnmodifiedInterfaceDeclaration) {
                printData.beginClass();
                if (node.isFinal()) {
                    //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("final"), printData));
                    jjtAcceptSpecial(node, printData, "final");
                }
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(child.getSpecial("interface"), printData));
                jjtAcceptSpecial(node, printData, "interface");
                printData.indent();
                if (node.isFinal()) {
                    printData.appendKeyword("final");
                    printData.space();
                }
            }
            else if (printData.isInsertSpaceLocalVariables() &&
                    (child instanceof ASTLocalVariableDeclaration) &&
                    !isLastLocalVariable(node)) {
                printData.newline();
            }
        }

        //  Traverse the children
        node.childrenAccept(this, data);

        if (node.hasAnyChildren()) {
            SimpleNode child = (SimpleNode) node.jjtGetChild(0);

            if (child instanceof ASTUnmodifiedClassDeclaration) {
                printData.endClass();
            }
            else if (child instanceof ASTUnmodifiedInterfaceDeclaration) {
                printData.endClass();
            }
            else if (printData.isInsertSpaceLocalVariables() &&
                    (child instanceof ASTLocalVariableDeclaration) &&
                    !isNextLocalVariable(node)) {
                printData.newline();
                printData.newline();
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
    public Object visit(ASTLocalVariableDeclaration node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Traverse the children
        int last = node.jjtGetNumChildren();
        Node typeNode = node.jjtGetChild(0);
        for (int ndx = 1; ndx < last; ndx++) {
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("comma." + (ndx - 1)), printData));
            jjtAcceptSpecial(node, printData, "comma." + (ndx - 1));
            //  Indent this type of statement
            if (printData.isVariablesAlignWithBlock()) {
                printData.decrIndent();
                printData.indent();
            }
            else {
                printData.indent();
            }

            //  Print the final token
            int current = 0;
            if (node.isUsingFinal()) {
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("final"), printData));
                jjtAcceptSpecial(node, printData, "final");
                printData.appendKeyword("final");
                printData.space();

                current = 6;
            }

            if (printData.isDynamicFieldSpacing(false)) {
                FieldSize size = printData.topFieldSize();
                int maxModifier = size.getModifierLength();
                for (int modifierIndex = current; modifierIndex < maxModifier; modifierIndex++) {
                    printData.space();
                }
            }

            //  Print the type again
            typeNode.jjtAccept(this, data);
            printData.space();
            if (printData.isVariablesAlignWithBlock()) {
                printData.incrIndent();
            }

            if (printData.isDynamicFieldSpacing(false)) {
                current = lvla.computeTypeLength(node);

                FieldSize size = printData.topFieldSize();
                int maxType = size.getTypeLength();
                for (int typeIndex = current; typeIndex < maxType; typeIndex++) {
                    printData.space();
                }
            }

            if (printData.getFieldSpaceCode() == PrintData.DFS_ALIGN_EQUALS) {
                printData.setTempEqualsLength(lvla.computeEqualsLength(node));
            }

            //  Visit the child
            node.jjtGetChild(ndx).jjtAccept(this, data);

            //  End the variable declaration
            printData.appendText(";");
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
    public Object visit(ASTEmptyStatement node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Print the name of the node
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("semicolon"), printData));
        jjtAcceptSpecial(node, printData, "semicolon");
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
    public Object visit(ASTStatementExpression node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Traverse the children
        if (node.jjtGetChild(0) instanceof ASTPrimaryExpression) {
            int last = node.jjtGetNumChildren();
            node.jjtGetChild(0).jjtAccept(this, data);
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("id"), printData));
            jjtAcceptSpecial(node, printData, "id");
            printData.appendText(node.getName());
            for (int ndx = 1; ndx < last; ndx++) {
                node.jjtGetChild(ndx).jjtAccept(this, data);
            }
        }
        else {
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
    public Object visit(ASTSwitchStatement node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Switch
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("switch"), printData));
        jjtAcceptSpecial(node, printData, "switch");
        printData.appendKeyword("switch");
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("beginExpr"), printData));
        jjtAcceptSpecial(node, printData, "beginExpr");
        if (printData.isSpaceAfterKeyword()) {
            printData.space();
        }
        printData.beginExpression(true);
        node.jjtGetChild(0).jjtAccept(this, data);
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("endExpr"), printData));
        jjtAcceptSpecial(node, printData, "endExpr");
        printData.endExpression(true);

        //  Start the block
        printData.beginBlock();
        printData.decrIndent();
        printData.incrCaseIndent();
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("beginBlock"), printData, false));
        jjtAcceptSpecial(node, printData, "beginBlock", false);

        //  Traverse the children
        int last = node.jjtGetNumChildren();
        for (int ndx = 1; ndx < last; ndx++) {
            Node next = node.jjtGetChild(ndx);
            if (next instanceof ASTBlockStatement) {
                boolean indent = shouldIndentSwitchBody(next);
                if (indent) {
                    printData.incrIndent();
                    next.jjtAccept(this, data);
                    printData.decrIndent();
                }
                else {
                    Node child = next.jjtGetChild(0);
                    Node grandchild = child.jjtGetChild(0);
                    printData.indent();
                    blockProcess((ASTBlock) grandchild,
                            printData,
                            true,
                            false);
                }
            }
            else {
                next.jjtAccept(this, data);
            }
        }

        //  End the block
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("endBlock"), printData));
        jjtAcceptSpecial(node, printData, "endBlock");
        printData.decrCaseIndent();
        printData.incrIndent();
        printData.endBlock();

        //  Return the data
        return data;
    }


    /**
     *  Reformats the case XXX: portion of a switch statement
     *
     *@param  node  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    public Object visit(ASTSwitchLabel node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Determine if the node has children
        if (node.hasAnyChildren()) {
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("id"), printData));
            jjtAcceptSpecial(node, printData, "id");
            printData.indent();
            printData.appendKeyword("case");
            printData.space();
            node.childrenAccept(this, data);
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("colon"), printData));
            jjtAcceptSpecial(node, printData, "colon");
            printData.appendText(":");
            printData.newline();
        }
        else {
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("id"), printData));
            jjtAcceptSpecial(node, printData, "id");
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("colon"), printData));
            jjtAcceptSpecial(node, printData, "colon");
            printData.indent();
            printData.appendKeyword("default");
            printData.appendText(":");
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
    public Object visit(ASTIfStatement node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Determine if the node has children
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("if"), printData));
        jjtAcceptSpecial(node, printData, "if");
        printData.appendKeyword("if");
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("beginExpr"), printData));
        jjtAcceptSpecial(node, printData, "beginExpr");
        if (printData.isSpaceAfterKeyword()) {
            printData.space();
        }
        printData.beginExpression(true);
        node.jjtGetChild(0).jjtAccept(this, data);
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("endExpr"), printData));
        jjtAcceptSpecial(node, printData, "endExpr");
        printData.endExpression(true);

        //  Determine if the then contains a block
        boolean hasElse = (node.jjtGetNumChildren() == 3);
        if (node.jjtGetNumChildren() >= 2) {
            boolean oldEnclosingIfStatement = enclosingIfStatement;
            enclosingIfStatement = true;
            forceBlock(node.jjtGetChild(1), printData,
                    !hasElse && printData.isElseOnNewLine(),
                    printData.isRemoveExcessBlocks() &&
                    !isIfStatement(node.jjtGetChild(1)));
            enclosingIfStatement = oldEnclosingIfStatement;
        }

        //  Determine if the else part
        if (hasElse) {
            boolean shouldIndent = isShouldIndentBeforeElse(printData, node);
            if (shouldIndent) {
                printData.indent();
            }
            else {
                printData.space();
            }
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("else"), printData, shouldIndent));
            jjtAcceptSpecial(node, printData, "else", shouldIndent);
            printData.appendKeyword("else");
            //  Determine if the next item is a statement
            ASTStatement child = (ASTStatement) node.jjtGetChild(2);
            Node next = child.jjtGetChild(0);
            if (next instanceof ASTIfStatement) {
                printData.space();
                // this rather complex code removes unwanted new lines
                // from before "if" statements so that when they are moved
                // the comments are in the correct place.
                Token special = ((ASTIfStatement) next).getSpecial("if");
                Token prev = null;
                Token first = null;
                while (special != null) {
                    if (special.kind >= 4 && special.kind <= 6) {
                        special.kind = 1;// make it a space!
                        special.image = " ";
                    }
                    else if (first == null) {
                        first = special;
                        prev = first;
                    }
                    else {
                        prev.specialToken = special;
                        prev = special;
                    }
                    special = special.specialToken;
                }
                if (first != null) {
                    prev.specialToken = null;
                    special = ((ASTIfStatement) next).getSpecial("if");
                    special.kind = first.kind;
                    special.image = first.image;
                    special.specialToken = first.specialToken;
                }
                else {
                    ((ASTIfStatement) next).removeSpecial("if");
                }
                next.jjtAccept(this, data);
            }
            else {
                forceBlock(child, printData, true, printData.isRemoveExcessBlocks());
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
    public Object visit(ASTWhileStatement node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Determine if the node has children
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("while"), printData));
        jjtAcceptSpecial(node, printData, "while");
        printData.appendKeyword("while");
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("beginExpr"), printData));
        jjtAcceptSpecial(node, printData, "beginExpr");
        if (printData.isSpaceAfterKeyword()) {
            printData.space();
        }
        printData.beginExpression(true);
        node.jjtGetChild(0).jjtAccept(this, data);
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("endExpr"), printData));
        jjtAcceptSpecial(node, printData, "endExpr");
        printData.endExpression(true);

        //  Process the block
        Node next = node.jjtGetChild(1);// get the Statement
        forceBlock(next,
                printData,
                true,
                printData.isRemoveExcessBlocks() && isIfStatementWithElse(next));

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
    public Object visit(ASTDoStatement node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Begin the do block
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("do"), printData));
        jjtAcceptSpecial(node, printData, "do");
        printData.appendKeyword("do");

        //  Process the block
        forceBlock(node.jjtGetChild(0), printData, false, false);

        //  Process the while block
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("while"), printData, false));
        jjtAcceptSpecial(node, printData, "while", false);
        if (printData.isSpaceAfterKeyword()) {
            printData.space();
        }
        printData.appendKeyword("while");
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("beginExpr"), printData, false));
        jjtAcceptSpecial(node, printData, "beginExpr", false);
        printData.space();
        printData.beginExpression(true);
        node.jjtGetChild(1).jjtAccept(this, data);
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("endExpr"), printData));
        jjtAcceptSpecial(node, printData, "endExpr");
        printData.endExpression(true);
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("semicolon"), printData));
        jjtAcceptSpecial(node, printData, "semicolon");
        printData.appendText(";");

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
    public Object visit(ASTForStatement node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Start the for loop
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("for"), printData));
        jjtAcceptSpecial(node, printData, "for");
        printData.appendKeyword("for");
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("beginExpr"), printData));
        jjtAcceptSpecial(node, printData, "beginExpr");
        if (printData.isSpaceAfterKeyword()) {
            printData.space();
        }
        printData.beginExpression(node.hasAnyChildren());

        //  Traverse the children
        Node next = node.jjtGetChild(0);
        int index = 1;

        if (next instanceof ASTLocalVariableDeclaration) {
            // new for loop structure for JDK 1.5
            printData.setSkipNameSpacing(true);
            forInit((ASTLocalVariableDeclaration) next, data);
            printData.setSkipNameSpacing(false);
            next = node.jjtGetChild(index);
            index++;
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("loopover"), printData));
            jjtAcceptSpecial(node, printData, "loopover");
            printData.appendText(" : ");
            if (next instanceof ASTExpression) {
                next.jjtAccept(this, data);
                next = node.jjtGetChild(index);
                index++;
            }
        }
        else {
            if (next instanceof ASTForInit) {
                printData.setSkipNameSpacing(true);
                next.jjtAccept(this, data);
                printData.setSkipNameSpacing(false);
                next = node.jjtGetChild(index);
                index++;
            }
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("init"), printData));
            jjtAcceptSpecial(node, printData, "init");
            printData.appendText("; ");
            if (next instanceof ASTExpression) {
                next.jjtAccept(this, data);
                next = node.jjtGetChild(index);
                index++;
            }
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("test"), printData));
            jjtAcceptSpecial(node, printData, "test");
            printData.appendText("; ");
            if (next instanceof ASTForUpdate) {
                next.jjtAccept(this, data);
                next = node.jjtGetChild(index);
                index++;
            }
        }
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("endExpr"), printData));
        jjtAcceptSpecial(node, printData, "endExpr");
        printData.endExpression(node.hasAnyChildren());
        forceBlock(next,
                printData,
                true,
                printData.isRemoveExcessBlocks() && (!enclosingIfStatement || isIfStatementWithElse(next)));

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
    public Object visit(ASTForInit node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Traverse the children
        Node next = node.jjtGetChild(0);
        if (next instanceof ASTLocalVariableDeclaration) {
            forInit((ASTLocalVariableDeclaration) next, data);
        }
        else {
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
    public Object visit(ASTStatementExpressionList node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Traverse the children
        int last = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < last; ndx++) {
            if (ndx > 0) {
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("comma." + (ndx - 1)), printData));
                jjtAcceptSpecial(node, printData, "comma." + (ndx - 1));
                printData.appendText(", ");
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
    public Object visit(ASTForUpdate node, Object data)
    {
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
    public Object visit(ASTBreakStatement node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Print the break statement
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("break"), printData));
        jjtAcceptSpecial(node, printData, "break");
        printData.appendKeyword("break");
        String name = node.getName();
        if (!((name == null) || (name.length() == 0))) {
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("id"), printData));
            jjtAcceptSpecial(node, printData, "id");
            printData.appendText(" " + node.getName());
        }
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("semicolon"), printData));
        jjtAcceptSpecial(node, printData, "semicolon");
        printData.appendText(";");

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
    public Object visit(ASTContinueStatement node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Print the continue statement
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("continue"), printData));
        jjtAcceptSpecial(node, printData, "continue");
        printData.appendKeyword("continue");
        String name = node.getName();
        if (!((name == null) || (name.length() == 0))) {
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("id"), printData));
            jjtAcceptSpecial(node, printData, "id");
            printData.appendText(" " + node.getName());
        }
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("semicolon"), printData));
        jjtAcceptSpecial(node, printData, "semicolon");
        printData.appendText(";");

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
    public Object visit(ASTReturnStatement node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Traverse the children
        if (node.hasAnyChildren()) {
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("return"), printData));
            jjtAcceptSpecial(node, printData, "return");
            printData.appendKeyword("return");
            printData.space();
            node.childrenAccept(this, data);
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("semicolon"), printData));
            jjtAcceptSpecial(node, printData, "semicolon");
            printData.appendText(";");
        }
        else {
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("return"), printData));
            jjtAcceptSpecial(node, printData, "return");
            printData.appendKeyword("return");
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("semicolon"), printData));
            jjtAcceptSpecial(node, printData, "semicolon");
            printData.appendText(";");
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
    public Object visit(ASTThrowStatement node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Traverse the children
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("throw"), printData));
        jjtAcceptSpecial(node, printData, "throw");
        printData.appendKeyword("throw");
        printData.space();
        node.childrenAccept(this, data);
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("semicolon"), printData));
        jjtAcceptSpecial(node, printData, "semicolon");
        printData.appendText(";");

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
    public Object visit(ASTSynchronizedStatement node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Traverse the children
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("synchronized"), printData));
        jjtAcceptSpecial(node, printData, "synchronized");
        printData.appendKeyword("synchronized");
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("beginExpr"), printData));
        jjtAcceptSpecial(node, printData, "beginExpr");
        if (printData.isSpaceAfterKeyword()) {
            printData.space();
        }
        printData.beginExpression(true);
        node.jjtGetChild(0).jjtAccept(this, data);
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("endExpr"), printData));
        jjtAcceptSpecial(node, printData, "endExpr");
        printData.endExpression(true);
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
    public Object visit(ASTTryStatement node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Traverse the children
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("try"), printData));
        jjtAcceptSpecial(node, printData, "try");
        printData.appendKeyword("try");
        blockProcess((ASTBlock) node.jjtGetChild(0), printData, printData.isCatchOnNewLine());

        //  Now work with the pairs
        int last = node.jjtGetNumChildren();
        boolean paired = false;
        int catchCount = 0;

        for (int ndx = 1; ndx < last; ndx++) {
            Node next = node.jjtGetChild(ndx);
            if (next instanceof ASTFormalParameter) {
                if (printData.isCatchOnNewLine()) {
                    printData.indent();
                }
                else {
                    printData.space();
                }
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("catch" + catchCount), printData, printData.isCatchOnNewLine()));
                jjtAcceptSpecial(node, printData, "catch" + catchCount, printData.isCatchOnNewLine());
                printData.appendKeyword("catch");
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("beginExpr" + catchCount), printData));
                jjtAcceptSpecial(node, printData, "beginExpr" + catchCount);
                if (printData.isSpaceAfterKeyword()) {
                    printData.space();
                }
                printData.beginExpression(true);
                next.jjtAccept(this, data);
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("endExpr" + catchCount), printData));
                jjtAcceptSpecial(node, printData, "endExpr" + catchCount);
                printData.endExpression(true);
                paired = true;
                catchCount++;
            }
            else {
                if (!paired) {
                    //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("finally"), printData, printData.isCatchOnNewLine()));
                    jjtAcceptSpecial(node, printData, "finally", printData.isCatchOnNewLine());
                    if (printData.isCatchOnNewLine()) {
                        printData.indent();
                    }
                    else {
                        printData.space();
                    }
                    printData.appendKeyword("finally");
                }
                blockProcess((ASTBlock) next, printData, printData.isCatchOnNewLine());
                paired = false;
            }
        }

        if (!printData.isCatchOnNewLine()) {
            printData.newline();
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
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Traverse the children
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("assert"), printData));
        jjtAcceptSpecial(node, printData, "assert");
        printData.appendKeyword("assert");
        printData.space();
        node.jjtGetChild(0).jjtAccept(this, data);

        if (node.jjtGetNumChildren() > 1) {
            printData.space();
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("colon"), printData));
            jjtAcceptSpecial(node, printData, "colon");
            printData.appendText(":");
            printData.space();
            node.jjtGetChild(1).jjtAccept(this, data);
        }

        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("semicolon"), printData));
        jjtAcceptSpecial(node, printData, "semicolon");
        printData.appendText(";");

        //  Return the data
        return data;
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of Parameter
     *@param  name  Description of Parameter
     *@param  data  Description of Parameter
     *@return       Description of the Returned Value
     */
    protected Object binaryExpression(SimpleNode node, String name, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Traverse the children
        int childCount = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < childCount; ndx++) {
            if (ndx > 0) {
                if (printData.isSpaceAroundOperators() || name.equals("instanceof")) {
                    printData.space();
                }
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("operator." + (ndx - 1)), printData));
                jjtAcceptSpecial(node, printData, "operator." + (ndx - 1));
                printData.appendText(name);
                if (printData.isSpaceAroundOperators() || name.equals("instanceof")) {
                    printData.space();
                }
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
    protected Object binaryExpression(SimpleNode node, Enumeration names, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Traverse the children
        int childCount = node.jjtGetNumChildren();
        for (int ndx = 0; ndx < childCount; ndx++) {
            if (ndx > 0) {
                if (printData.isSpaceAroundOperators()) {
                    printData.space();
                }
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("operator." + (ndx - 1)), printData));
                jjtAcceptSpecial(node, printData, "operator." + (ndx - 1));
                printData.appendText(names.nextElement().toString());
                if (printData.isSpaceAroundOperators()) {
                    printData.space();
                }
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
     */
    protected void forInit(ASTLocalVariableDeclaration node, Object data)
    {
        //  Get the data
        PrintData printData = (PrintData) data;

        //  Print the final token
        if (node.isUsingFinal()) {
            //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("final"), printData));
            jjtAcceptSpecial(node, printData, "final");
            printData.appendKeyword("final");
            printData.space();
        }

        //  Traverse the children
        int last = node.jjtGetNumChildren();
        Node typeNode = node.jjtGetChild(0);
        typeNode.jjtAccept(this, data);
        printData.space();

        for (int ndx = 1; ndx < last; ndx++) {
            if (ndx > 1) {
                //  Add a comma between entries
                //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("comma." + (ndx - 1)), printData));
                jjtAcceptSpecial(node, printData, "comma." + (ndx - 1));
                printData.appendText(", ");
            }

            //  Visit the child
            node.jjtGetChild(ndx).jjtAccept(this, data);
        }
    }


    /**
     *  Forces a block around this node, if it is not already a block
     *
     *@param  node            the node
     *@param  printData       the print data
     *@param  newline         do we have a newline at the end of the block
     *@param  canRemoveBlock  Description of the Parameter
     */
    protected void forceBlock(Node node, PrintData printData,
            boolean newline, boolean canRemoveBlock)
    {
        if ((node.jjtGetNumChildren() > 0) &&
                (node.jjtGetChild(0) instanceof ASTBlock)) {
            //  We know we have a block
            ASTBlock child = (ASTBlock) node.jjtGetChild(0);

            LocalVariableLookAhead lvla = new LocalVariableLookAhead();
            FieldSize size = lvla.run(child);
            size.update(printData.getDynamicFieldSpaces());
            printData.pushFieldSize(size);

            //  Start the block
            if (isThisBlockRequired(canRemoveBlock, node)) {
                printData.beginBlock();
            }
            else {
                printData.incrIndent();
            }
            //child.jjtAccept(specialTokenVisitor, new SpecialTokenData(child.getSpecial("begin"), printData, false));
            jjtAcceptSpecial(child, printData, "begin", false);

            //  Accept the children
            child.childrenAccept(this, printData);

            //  End the block
            //child.jjtAccept(specialTokenVisitor, new SpecialTokenData(child.getSpecial("end"), printData));
            jjtAcceptSpecial(child, printData, "end");
            if (isThisBlockRequired(canRemoveBlock, node)) {
                printData.endBlock(newline, true);
            }
            else {
                printData.decrIndent();
            }

            printData.popFieldSize();
        }
        else {
            if (printData.isForcingBlock()) {
                printData.beginBlock();
            }
            else {
                printData.incrIndent();
            }

            printData.indent();
            ((SimpleNode) node).childrenAccept(this, printData);
            if (node.jjtGetChild(0) instanceof ASTStatementExpression) {
                //  Finish off the statement expression
                printData.appendText(";");
                printData.newline();
            }

            if (printData.isForcingBlock()) {
                printData.endBlock(newline, true);
            }
            else {
                printData.decrIndent();
            }
        }
    }


    /**
     *  Check the initial token, and removes it from the object.
     *
     *@param  top  the result type
     *@return      the initial token
     */
    private Token getInitialToken(ASTResultType top)
    {
        //  Check to see if we need to go farther down
        if (top.hasAnyChildren()) {
            ASTType type = (ASTType) top.jjtGetChild(0);
            if (type.jjtGetChild(0) instanceof ASTVariance) {
                //  No farther to go - return the token
                Token tok = top.getSpecial("primitive");
                top.removeSpecial("primitive");
                return tok;
            }
            else if (type.jjtGetChild(0) instanceof ASTPrimitiveType) {
                ASTPrimitiveType primitiveType = (ASTPrimitiveType) type.jjtGetChild(0);
                Token tok = primitiveType.getSpecial("primitive");
                primitiveType.removeSpecial("primitive");
                return tok;
            }
            else if (type.jjtGetChild(0) instanceof ASTReferenceType) {
                ASTReferenceType referenceType = (ASTReferenceType) type.jjtGetChild(0);
                if (referenceType.jjtGetChild(0) instanceof ASTName) {
                    ASTName name = (ASTName) referenceType.jjtGetChild(0);
                    Token tok = name.getSpecial("id0");
                    name.removeSpecial("id0");
                    return tok;
                }
                else {
                    ASTPrimitiveType primitiveType = (ASTPrimitiveType) referenceType.jjtGetChild(0);
                    Token tok = primitiveType.getSpecial("primitive");
                    primitiveType.removeSpecial("primitive");
                    return tok;
                }
            }
            else {
                ASTName name = (ASTName) type.jjtGetChild(0);
                Token tok = name.getSpecial("id0");
                name.removeSpecial("id0");
                return tok;
            }
        }
        else {
            //  No farther to go - return the token
            Token tok = top.getSpecial("primitive");
            top.removeSpecial("primitive");
            return tok;
        }
    }


    /**
     *  Check the initial token, and removes it from the object.
     *
     *@param  top  the type
     *@return      the initial token
     */
    private Token getInitialToken(ASTType top)
    {
        if (top.jjtGetChild(0) instanceof ASTVariance) {
            //  No farther to go - return the token
            Token tok = top.getSpecial("primitive");
            top.removeSpecial("primitive");
            return tok;
        }
        else if (top.jjtGetChild(0) instanceof ASTPrimitiveType) {
            ASTPrimitiveType primitiveType = (ASTPrimitiveType) top.jjtGetChild(0);
            Token tok = primitiveType.getSpecial("primitive");
            primitiveType.removeSpecial("primitive");
            return tok;
        }
        else if (top.jjtGetChild(0) instanceof ASTReferenceType) {
            ASTReferenceType reference = (ASTReferenceType) top.jjtGetChild(0);
            if (reference.jjtGetChild(0) instanceof ASTPrimitiveType) {
                ASTPrimitiveType primitiveType = (ASTPrimitiveType) reference.jjtGetChild(0);
                Token tok = primitiveType.getSpecial("primitive");
                primitiveType.removeSpecial("primitive");
                return tok;
            }
            else {
                ASTName name = (ASTName) reference.jjtGetChild(0);
                Token tok = name.getSpecial("id0");
                name.removeSpecial("id0");
                return tok;
            }
        }
        else {
            ASTName name = (ASTName) top.jjtGetChild(0);
            Token tok = name.getSpecial("id0");
            name.removeSpecial("id0");
            return tok;
        }
    }


    /**
     *  Gets the commentsPresent attribute of the PrettyPrintVisitor object
     *
     *@param  node  Description of the Parameter
     *@return       The commentsPresent value
     */
    private boolean isCommentsPresent(ASTBlock node)
    {
        Token tok = node.getSpecial("end");
        if (tok == null) {
            return false;
        }

        //  Find the first token
        Token current = tok;
        Token previous = tok.specialToken;
        int type;
        while (previous != null) {
            type = current.kind;
            if ((type < 5) || (type > 7)) {
                return true;
            }
            current = previous;
            previous = current.specialToken;
        }

        //  Return the first
        type = current.kind;
        return ((type < 5) || (type > 7));
    }


    /**
     *  Gets the ifStatement attribute of the PrettyPrintVisitor object
     *
     *@param  node  Description of the Parameter
     *@return       The ifStatement value
     */
    private boolean isIfStatement(Node node)
    {
        if (node.jjtGetChild(0) instanceof ASTBlock) {
            ASTBlock block = (ASTBlock) node.jjtGetChild(0);
            ASTBlockStatement blockStatement = (ASTBlockStatement) block.jjtGetChild(0);
            if (blockStatement.jjtGetChild(0) instanceof ASTStatement) {
                ASTStatement statement = (ASTStatement) blockStatement.jjtGetChild(0);
                return (statement.jjtGetChild(0) instanceof ASTIfStatement);
            }
        }

        return false;
    }


    /**
     *  Gets the ifStatement attribute of the PrettyPrintVisitor object
     *
     *@param  node  Description of the Parameter
     *@return       The ifStatement value
     *@since        JRefactory 2.7.00
     */
    private boolean isIfStatementWithElse(Node node)
    {
        if (node.jjtGetChild(0) instanceof ASTBlock) {
            ASTBlock block = (ASTBlock) node.jjtGetChild(0);
            ASTBlockStatement blockStatement = (ASTBlockStatement) block.jjtGetChild(0);
            if (blockStatement.jjtGetChild(0) instanceof ASTStatement) {
                ASTStatement statement = (ASTStatement) blockStatement.jjtGetChild(0);
                if (statement.jjtGetChild(0) instanceof ASTIfStatement) {
                    ASTIfStatement ifstatement = (ASTIfStatement) statement.jjtGetChild(0);
                    return (ifstatement.jjtGetNumChildren() == 3);
                }
            }
        }

        return true;
    }


    /**
     *  Determine if the node is a field or a method in an anonymous class
     *
     *@param  node  The node in question
     *@return       true if the node is in an anonymous class
     */
    private boolean isInAnonymousClass(Node node)
    {
        return (node.jjtGetParent().jjtGetParent().jjtGetParent()
                instanceof ASTAllocationExpression);
    }


    /**
     *  Determine if the node is a field or a method in an anonymous class
     *
     *@param  node  The node in question
     *@return       true if the node is in an anonymous class
     */
    private boolean isInInnerClass(Node node)
    {
        Node greatGreatGrandparent = node.jjtGetParent().jjtGetParent().jjtGetParent().jjtGetParent();

        boolean topLevelClass = (greatGreatGrandparent instanceof ASTClassDeclaration);
        boolean topLevelInterface = (greatGreatGrandparent instanceof ASTInterfaceDeclaration);

        return !topLevelClass && !topLevelInterface;
    }


    /**
     *  Determine if the class is an inner class
     *
     *@param  node  Description of Parameter
     *@return       The InnerClass value
     */
    private boolean isInnerClass(Node node)
    {
        return (node instanceof ASTNestedClassDeclaration) ||
                (node instanceof ASTNestedInterfaceDeclaration);
    }


    /**
     *  Gets the JavadocRequired attribute of the PrettyPrintVisitor object
     *
     *@param  node       Description of Parameter
     *@param  printData  Description of Parameter
     *@return            The JavadocRequired value
     */
    private boolean isJavadocRequired(SimpleNode node, PrintData printData)
    {
        if (printData.isNestedClassDocumented()) {
            return node.isRequired() && !isInAnonymousClass(node);
        }
        else {
            return node.isRequired() && !isInAnonymousClass(node)
                    && !isInInnerClass(node) && !isInnerClass(node);
        }
    }


    /**
     *  Gets the lastLocalVariable attribute of the PrettyPrintVisitor object
     *
     *@param  node  Description of the Parameter
     *@return       The lastLocalVariable value
     */
    private boolean isLastLocalVariable(ASTBlockStatement node)
    {
        Node parent = node.jjtGetParent();
        int last = parent.jjtGetNumChildren();
        return isNeighborLV(parent, node, 1, last, -1);
    }


    /**
     *  Gets the neighborLV attribute of the PrettyPrintVisitor object
     *
     *@param  parent     Description of the Parameter
     *@param  node       Description of the Parameter
     *@param  first      Description of the Parameter
     *@param  last       Description of the Parameter
     *@param  direction  Description of the Parameter
     *@return            The neighborLV value
     */
    private boolean isNeighborLV(Node parent, Node node, int first, int last, int direction)
    {
        for (int ndx = first; ndx < last; ndx++) {
            if (parent.jjtGetChild(ndx) == node) {
                Node next = parent.jjtGetChild(ndx + direction);
                if (next instanceof ASTBlockStatement) {
                    return (next.jjtGetChild(0) instanceof ASTLocalVariableDeclaration);
                }
                return true;
            }
        }
        return true;
    }


    /**
     *  Gets the nextLocalVariable attribute of the PrettyPrintVisitor object
     *
     *@param  node  Description of the Parameter
     *@return       The nextLocalVariable value
     */
    private boolean isNextLocalVariable(ASTBlockStatement node)
    {
        Node parent = node.jjtGetParent();
        int last = parent.jjtGetNumChildren() - 1;
        return isNeighborLV(parent, node, 0, last, 1);
    }


    /**
     *  The rules to determine if we should indent before the block are a little
     *  more complex if we are allowed not to force a block statement around the
     *  body of the then part.
     *
     *@param  printData  Description of the Parameter
     *@param  node       Description of the Parameter
     *@return            The shouldIndentBeforeElse value
     */
    private boolean isShouldIndentBeforeElse(PrintData printData, ASTIfStatement node)
    {
        if (printData.isElseOnNewLine()) {
            return true;
        }

        if (printData.isForcingBlock() || (node.jjtGetChild(1) instanceof ASTBlock)) {
            return false;
        }

        return true;
    }


    /**
     *  Gets the thisBlockRequired attribute of the PrettyPrintVisitor object
     *
     *@param  canRemoveBlock  Description of the Parameter
     *@param  node            Description of the Parameter
     *@return                 The thisBlockRequired value
     */
    private boolean isThisBlockRequired(boolean canRemoveBlock, Node node)
    {
        if (!canRemoveBlock) {
            return true;
        }

        return (node.jjtGetChild(0).jjtGetNumChildren() != 1);
    }


    /**
     *  Processes a block object
     *
     *@param  node       Description of Parameter
     *@param  printData  Description of Parameter
     *@param  newline    Description of Parameter
     */
    private void blockProcess(ASTBlock node, PrintData printData, boolean newline)
    {
        blockProcess(node, printData, newline, true);
    }


    /**
     *  Processes a block object
     *
     *@param  node       Description of Parameter
     *@param  printData  Description of Parameter
     *@param  newline    Description of Parameter
     *@param  space      Description of Parameter
     */
    private void blockProcess(ASTBlock node, PrintData printData, boolean newline, boolean space)
    {
        boolean onSingleLine = (node.jjtGetNumChildren() > 0) ||  // REVISIT: should be == 0
                !printData.isEmptyBlockOnSingleLine() ||
                (node.getSpecial("end") != null) ||
                (node.getSpecial("begin") != null);

        LocalVariableLookAhead lvla = new LocalVariableLookAhead();
        FieldSize size = lvla.run(node);
        size.update(printData.getDynamicFieldSpaces());
        printData.pushFieldSize(size);

        //  Start the block
        printData.beginBlock(space, onSingleLine);
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("begin"), printData, false));
        jjtAcceptSpecial(node, printData, "begin", false);

        // If this is a multistatement method body, then check whether to print a beginning line
        if (printData.isLineBeforeMultistatementMethodBody() &&
                        (node.jjtGetParent() instanceof ASTMethodDeclaration) &&
                        (node.jjtGetNumChildren() > 1) &&
                        (printData.getMethodBlockStyle() == PrintData.BLOCK_STYLE_C)) {
                printData.newline();
        }

        //  Traverse the children
        node.childrenAccept(this, printData);

        //  Finish the block
        //node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial("end"), printData));
        jjtAcceptSpecial(node, printData, "end");

        if (node.jjtGetParent() instanceof ASTMethodDeclaration) {
            printData.methodBrace();
        }
        printData.endBlock(newline, onSingleLine);

        printData.popFieldSize();
    }


    /**
     *  Loads the footer
     *
     *@param  printData  where we are printing to
     *@return            Description of the Return Value
     */
    private boolean loadFooter(PrintData printData)
    {
        boolean foundSomething = false;
        try {
            FileSettings settings = FileSettings.getRefactoryPrettySettings();
            String temp = settings.getString("footer.1");

            foundSomething = true;

            int ndx = 1;
            while (true) {
                String nextLine = settings.getString("footer." + ndx);
                printData.appendComment(nextLine, PrintData.C_STYLE_COMMENT);
                printData.newline();
                ndx++;
            }
        }
        catch (MissingSettingsException mse) {
        }

        return foundSomething;
    }


    /**
     *  Loads the header
     *
     *@param  node       the compilation unit
     *@param  printData  where we are printing to
     */
    private void loadHeader(ASTCompilationUnit node, PrintData printData)
    {
        if (node == null) {
            return;
        }

        try {
            FileSettings settings = FileSettings.getRefactoryPrettySettings();
            String temp = settings.getString("header.1");

            /*
             *  That would throw an exception if there wasn't a header.
             *  So now we are free to delete things out of the child node
             */
            SimpleNode child = (SimpleNode) node.jjtGetChild(0);
            if (child == null) {
                return;
            }
            else if (child instanceof ASTPackageDeclaration) {
                child.removeSpecial("package");
            }
            else if (child instanceof ASTImportDeclaration) {
                child.removeSpecial("import");
            }
            else {
                //  The first thing is a type declaration - you're on your own
                return;
            }

            int ndx = 1;
            while (true) {
                String nextLine = settings.getString("header." + ndx);
                printData.appendComment(nextLine, PrintData.C_STYLE_COMMENT);
                printData.newline();
                ndx++;
            }
        }
        catch (MissingSettingsException mse) {
        }
    }


    /**
     *  Determines if we should indent the statement that is contained in the
     *  switch statement.
     *
     *@param  next  the next node
     *@return       true if we should use the indent
     */
    private boolean shouldIndentSwitchBody(Node next)
    {
        Node child = next.jjtGetChild(0);
        if (child instanceof ASTStatement) {
            Node grandchild = child.jjtGetChild(0);
            if (grandchild instanceof ASTBlock) {
                return false;
            }
        }
        return true;
    }


    /**
     *  The standard field indent
     *
     *@param  printData  Description of Parameter
     */
    private void standardFieldIndent(PrintData printData)
    {
        printData.space();

        if (printData.isFieldNameIndented()) {
            int currentLength = printData.getLineLength();
            int desiredLength = printData.getFieldNameIndent();
            for (int ndx = currentLength; ndx < desiredLength; ndx++) {
                printData.space();
            }
        }
    }
    
    private void jjtAcceptSpecials(SimpleNode node, PrintData printData, String[] specials) {
        for (int i=0; i<specials.length; i++) {
           node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial(specials[i]), printData));
        }
    }
    private void jjtAcceptSpecial(SimpleNode node, PrintData printData, String special) {
        node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial(special), printData));
    }
    private void jjtAcceptSpecial(SimpleNode node, PrintData printData, String special, boolean newline) {
        node.jjtAccept(specialTokenVisitor, new SpecialTokenData(node.getSpecial(special), printData, newline));
    }
}

