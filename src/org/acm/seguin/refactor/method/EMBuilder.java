/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.refactor.method;

import java.util.Iterator;
import java.util.LinkedList;
import org.acm.seguin.parser.Node;
import org.acm.seguin.parser.ast.ASTArgumentList;
import org.acm.seguin.parser.ast.ASTArguments;
import org.acm.seguin.parser.ast.ASTAssignmentOperator;
import org.acm.seguin.parser.ast.ASTBlockStatement;
import org.acm.seguin.parser.ast.ASTExpression;
import org.acm.seguin.parser.ast.ASTLocalVariableDeclaration;
import org.acm.seguin.parser.ast.ASTName;
import org.acm.seguin.parser.ast.ASTPrimaryExpression;
import org.acm.seguin.parser.ast.ASTPrimaryPrefix;
import org.acm.seguin.parser.ast.ASTPrimarySuffix;
import org.acm.seguin.parser.ast.ASTPrimitiveType;
import org.acm.seguin.parser.ast.ASTStatement;
import org.acm.seguin.parser.ast.ASTStatementExpression;
import org.acm.seguin.parser.ast.ASTType;
import org.acm.seguin.parser.ast.ASTVariableDeclarator;
import org.acm.seguin.parser.ast.ASTVariableDeclaratorId;
import org.acm.seguin.parser.ast.ASTVariableInitializer;
import org.acm.seguin.parser.ast.SimpleNode;
import org.acm.seguin.parser.build.BuildExpression;
import org.acm.seguin.summary.VariableSummary;
import org.acm.seguin.summary.TypeDeclSummary;

import org.acm.seguin.parser.ast.ASTReferenceType;
import org.acm.seguin.parser.ast.ASTClassOrInterfaceType;
import org.acm.seguin.parser.JavaParserTreeConstants;


/**
 *  Builds the invocation of the method that is extracted for insertion into
 *  the place where the method was extracted
 *
 *@author    Chris Seguin
 */
class EMBuilder
{
	private String methodName;
	private boolean isStatement;
	private LinkedList parameters;
	private VariableSummary returnSummary;
	private boolean localVariableNeeded = false;


	/**
	 *  Constructor for the EMBuilder object
	 */
	public EMBuilder()
	{
		returnSummary = null;
	}


	/**
	 *  Sets the MethodName attribute of the EMBuilder object
	 *
	 *@param  value  The new MethodName value
	 */
	public void setMethodName(String value)
	{
		methodName = value;
	}


	/**
	 *  Sets the Statement attribute of the EMBuilder object
	 *
	 *@param  value  The new Statement value
	 */
	public void setStatement(boolean value)
	{
		isStatement = value;
	}


	/**
	 *  Sets the Parameters attribute of the EMBuilder object
	 *
	 *@param  list  The new Parameters value
	 */
	public void setParameters(LinkedList list)
	{
		parameters = list;
	}


	/**
	 *  Sets the ReturnName attribute of the EMBuilder object
	 *
	 *@param  name  The new ReturnName value
	 */
	public void setReturnSummary(VariableSummary value)
	{
		returnSummary = value;
	}


	/**
	 *  Sets the LocalVariableNeeded attribute of the EMBuilder object
	 *
	 *@param  value  The new LocalVariableNeeded value
	 */
	public void setLocalVariableNeeded(boolean value)
	{
		localVariableNeeded = value;
	}


	/**
	 *  Builds the statement or assignment or local variable declaration
	 *
	 *@return    The resulting value
	 */
	public Node build()
	{
		ASTBlockStatement blockStatement = new ASTBlockStatement(JavaParserTreeConstants.JJTBLOCKSTATEMENT);

		if (localVariableNeeded) {
			buildWithLocal(blockStatement);
			return blockStatement;
		}

		ASTStatement statement = new ASTStatement(0);
		blockStatement.jjtAddChild(statement, 0);

		ASTStatementExpression stateExpress = new ASTStatementExpression(JavaParserTreeConstants.JJTSTATEMENTEXPRESSION);
		statement.jjtAddChild(stateExpress, 0);

		ASTPrimaryExpression primaryExpression = null;
		if (isStatement && (returnSummary != null))
		{
			buildAssignment(stateExpress);
		}
		else
		{
			primaryExpression = buildMethodInvocation(stateExpress, 0);
		}

		if (isStatement)
		{
			return blockStatement;
		}
		else
		{
			return primaryExpression;
		}
	}


	/**
	 *  Builds the assignment
	 *
	 *@param  stateExpress  Description of Parameter
	 */
	private void buildAssignment(ASTStatementExpression stateExpress)
	{
		//  First add what we are returning
		ASTPrimaryExpression primaryExpression = new ASTPrimaryExpression(JavaParserTreeConstants.JJTPRIMARYEXPRESSION);
		stateExpress.jjtAddChild(primaryExpression, 0);

		ASTPrimaryPrefix prefix = new ASTPrimaryPrefix(JavaParserTreeConstants.JJTPRIMARYPREFIX);
		primaryExpression.jjtAddChild(prefix, 0);

		ASTName name = new ASTName();
		name.addNamePart(returnSummary.getName());
		primaryExpression.jjtAddChild(name, 0);

		//  Now add the assignment operator
		ASTAssignmentOperator assign = new ASTAssignmentOperator(JavaParserTreeConstants.JJTASSIGNMENTOPERATOR);
		assign.setName("=");
		stateExpress.jjtAddChild(assign, 1);

		//  Finally add the rest
		buildMethodInvocation(stateExpress, 2);
	}


	/**
	 *  Builds the method invocation
	 *
	 *@param  stateExpress  Description of Parameter
	 *@param  index         Description of Parameter
	 *@return               Description of the Returned Value
	 */
	private ASTPrimaryExpression buildMethodInvocation(SimpleNode stateExpress, int index)
	{
		ASTPrimaryExpression primaryExpression = new ASTPrimaryExpression(JavaParserTreeConstants.JJTPRIMARYEXPRESSION);
		stateExpress.jjtAddChild(primaryExpression, index);

		ASTPrimaryPrefix prefix = new ASTPrimaryPrefix(JavaParserTreeConstants.JJTPRIMARYPREFIX);
		primaryExpression.jjtAddChild(prefix, 0);

		ASTName name = new ASTName();
		name.addNamePart(methodName);
		primaryExpression.jjtAddChild(name, 0);

		ASTPrimarySuffix suffix = new ASTPrimarySuffix(JavaParserTreeConstants.JJTPRIMARYSUFFIX);
		primaryExpression.jjtAddChild(suffix, 1);

		ASTArguments args = new ASTArguments(JavaParserTreeConstants.JJTARGUMENTS);
		suffix.jjtAddChild(args, 0);

		ASTArgumentList argList = new ASTArgumentList(JavaParserTreeConstants.JJTARGUMENTLIST);
		args.jjtAddChild(argList, 0);

		int count = 0;
		BuildExpression be = new BuildExpression();
		Iterator iter = parameters.iterator();
		if (iter != null)
		{
			while (iter.hasNext())
			{
				VariableSummary next = (VariableSummary) iter.next();
				ASTExpression expr = be.buildName(next.getName());
				argList.jjtAddChild(expr, count);
				count++;
			}
		}

		return primaryExpression;
	}


	/**
	 *  Builds a local variable declaration
	 *
	 *@param  blockStatement  the block statement we are inserting into
	 */
	private void buildWithLocal(ASTBlockStatement blockStatement)
	{
		ASTLocalVariableDeclaration statement = new ASTLocalVariableDeclaration(JavaParserTreeConstants.JJTLOCALVARIABLEDECLARATION);
		blockStatement.jjtAddChild(statement, 0);

		ASTType type = new ASTType(JavaParserTreeConstants.JJTTYPE);
		statement.jjtAddChild(type, 0);

		TypeDeclSummary typeDecl = returnSummary.getTypeDecl();
                if (typeDecl.getArrayCount()==0 && typeDecl.isPrimitive())
		{
			ASTPrimitiveType primitiveType = new ASTPrimitiveType(JavaParserTreeConstants.JJTPRIMITIVETYPE);
			primitiveType.setName(typeDecl.getType());
			type.jjtAddChild(primitiveType, 0);
		}
		else
		{
                        ASTReferenceType reference = new ASTReferenceType(JavaParserTreeConstants.JJTREFERENCETYPE);
                        type.jjtAddChild(reference, 0);
                        if (typeDecl.isPrimitive()) {
                            ASTPrimitiveType primitiveType = new ASTPrimitiveType(JavaParserTreeConstants.JJTPRIMITIVETYPE);
                            primitiveType.setName(typeDecl.getType());
                            reference.jjtAddChild(primitiveType, 0);
                        } else {
                            ASTClassOrInterfaceType name = new ASTClassOrInterfaceType(JavaParserTreeConstants.JJTCLASSORINTERFACETYPE);
                            name.fromString(typeDecl.getLongName());
                            reference.jjtAddChild(name, 0);
                        }
                        reference.setArrayCount(typeDecl.getArrayCount());
		}

		ASTVariableDeclarator varDecl = new ASTVariableDeclarator(JavaParserTreeConstants.JJTVARIABLEDECLARATORID);
		statement.jjtAddChild(varDecl, 1);

		ASTVariableDeclaratorId varDeclID = new ASTVariableDeclaratorId(JavaParserTreeConstants.JJTVARIABLEDECLARATORID);
		varDeclID.setName(returnSummary.getName());
		varDecl.jjtAddChild(varDeclID, 0);

		ASTVariableInitializer initializer = new ASTVariableInitializer(JavaParserTreeConstants.JJTVARIABLEINITIALIZER);
		varDecl.jjtAddChild(initializer, 1);

		buildMethodInvocation(initializer, 0);
	}
}
