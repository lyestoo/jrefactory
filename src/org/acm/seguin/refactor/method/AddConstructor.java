/*
 * Author:  Chris Seguin
 *
 * This software has been developed under the copyleft
 * rules of the GNU General Public License.  Please
 * consult the GNU General Public License for more
 * details about use and distribution of this software.
 */
package org.acm.seguin.refactor.method;

import java.util.Iterator;
import java.util.StringTokenizer;
import org.acm.seguin.parser.ast.ASTBlock;
import org.acm.seguin.parser.ast.ASTClassBodyDeclaration;
import org.acm.seguin.parser.ast.ASTClassDeclaration;
import org.acm.seguin.parser.ast.ASTFormalParameter;
import org.acm.seguin.parser.ast.ASTFormalParameters;
import org.acm.seguin.parser.ast.ASTConstructorDeclaration;
import org.acm.seguin.parser.ast.ASTExplicitConstructorInvocation;
import org.acm.seguin.parser.ast.ASTArguments;
import org.acm.seguin.parser.ast.ASTArgumentList;
import org.acm.seguin.parser.ast.ASTName;
import org.acm.seguin.parser.ast.ASTNameList;
import org.acm.seguin.parser.ast.ASTPrimitiveType;
import org.acm.seguin.parser.ast.ASTResultType;
import org.acm.seguin.parser.ast.ASTType;
import org.acm.seguin.parser.ast.ASTTypeDeclaration;
import org.acm.seguin.parser.ast.ASTVariableDeclaratorId;
import org.acm.seguin.parser.ast.SimpleNode;
import org.acm.seguin.parser.build.BuildExpression;
import org.acm.seguin.parser.ast.ModifierHolder;
import org.acm.seguin.refactor.TransformAST;
import org.acm.seguin.summary.MethodSummary;
import org.acm.seguin.summary.ParameterSummary;
import org.acm.seguin.summary.TypeDeclSummary;

import org.acm.seguin.parser.ast.ASTReferenceType;
import org.acm.seguin.parser.ast.ASTClassOrInterfaceType;
import org.acm.seguin.parser.JavaParserTreeConstants;


/**
 *  A series of transformations taht adds a new constructor to a class.
 *  The constructor invokes the super classes's constructor.  This
 *  object shares a lot in common with the AddNewMethod class, and
 *  combining these two objects can be considered for future refactorings.
 *
 *@author    Chris Seguin
 */
public class AddConstructor extends TransformAST {
	/**
	 *  The method we are updating
	 */
	protected MethodSummary methodSummary;
	private String typeName;


	/**
	 *  Constructor for the AddConstructor object
	 *
	 *@param  init  the method summary to add
	 */
	public AddConstructor(MethodSummary init, String name) {
		methodSummary = init;
		typeName = name;
	}


	/**
	 *  Update the syntax tree
	 *
	 *@param  root  the root of the syntax tree
	 */
	public void update(SimpleNode root) {
		//  Find the type declaration
		int last = root.jjtGetNumChildren();
		for (int ndx = 0; ndx < last; ndx++) {
			if (root.jjtGetChild(ndx) instanceof ASTTypeDeclaration) {
				drillIntoType((SimpleNode) root.jjtGetChild(ndx));
				return;
			}
		}
	}



	/**
	 *  Determines if the method is abstract
	 *
	 *@return    true if the method is abstract
	 */
	protected boolean isAbstract() {
		return methodSummary.isAbstract();
	}


	/**
	 *  Adds the return to the method declaration
	 *
	 *@param  methodDecl  The feature to be added to the Return attribute
	 *@param  index       The feature to be added to the Return attribute
	 */
	protected void addReturn(SimpleNode methodDecl, int index) {
		ASTResultType result = new ASTResultType(JavaParserTreeConstants.JJTRESULTTYPE);
		TypeDeclSummary returnType = methodSummary.getReturnType();
		if ((returnType != null) && !returnType.getType().equals("void")) {
			ASTType type = buildType(returnType);
			result.jjtAddChild(type, 0);
		}
		methodDecl.jjtAddChild(result, index);
	}


	/**
	 *  Creates the parameters
	 *
	 *@return    Description of the Returned Value
	 */
	protected ASTFormalParameters createParameters() {
		ASTFormalParameters params = new ASTFormalParameters(JavaParserTreeConstants.JJTFORMALPARAMETERS);
		Iterator iter = methodSummary.getParameters();
		if (iter != null) {
			int paramCount = 0;
			while (iter.hasNext()) {
				ParameterSummary paramSummary = (ParameterSummary) iter.next();
				ASTFormalParameter nextParam = new ASTFormalParameter(JavaParserTreeConstants.JJTFORMALPARAMETER);
				ASTType type = buildType(paramSummary.getTypeDecl());
				nextParam.jjtAddChild(type, 0);
				ASTVariableDeclaratorId paramDeclID = new ASTVariableDeclaratorId(JavaParserTreeConstants.JJTVARIABLEDECLARATORID);
				paramDeclID.setName(paramSummary.getName());
				nextParam.jjtAddChild(paramDeclID, 1);
				params.jjtAddChild(nextParam, paramCount);
				paramCount++;
			}
		}
		return params;
	}


	/**
	 *  Creates the exceptions
	 *
	 *@param  iter  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	protected ASTNameList createExceptions(Iterator iter) {
		ASTNameList list = new ASTNameList(JavaParserTreeConstants.JJTNAMELIST);
		int ndx = 0;
		while (iter.hasNext()) {
			TypeDeclSummary next = (TypeDeclSummary) iter.next();
			list.jjtAddChild(buildName(next), ndx);
			ndx++;
		}
		return list;
	}


	/**
	 *  Adds the exceptions to the node
	 *
	 *@param  methodDecl  The feature to be added to the Exceptions attribute
	 *@param  index       The feature to be added to the Exceptions attribute
	 *@return             Description of the Returned Value
	 */
	protected int addExceptions(SimpleNode methodDecl, int index) {
		Iterator iter = methodSummary.getExceptions();
		if (iter != null) {
			ASTNameList list = createExceptions(iter);
			methodDecl.jjtAddChild(list, index);
			index++;
		}
		return index;
	}


	/**
	 *  Adds the body of the method
	 *
	 *@param  methodDecl  The feature to be added to the Body attribute
	 *@param  index       The feature to be added to the Body attribute
	 */
	protected void addBody(SimpleNode methodDecl, int index) {
		ASTExplicitConstructorInvocation eci = new ASTExplicitConstructorInvocation(JavaParserTreeConstants.JJTEXPLICITCONSTRUCTORINVOCATION);
		eci.setName("super");

		ASTArguments args = new ASTArguments(JavaParserTreeConstants.JJTARGUMENTS);
		eci.jjtAddChild(args, 0);

		ASTArgumentList argList = new ASTArgumentList(JavaParserTreeConstants.JJTARGUMENTLIST);
		args.jjtAddChild(argList, 0);

		Iterator iter = methodSummary.getParameters();
		int item = 0;
		if (iter != null) {
			BuildExpression builder = new BuildExpression();
			while (iter.hasNext()) {
				ParameterSummary param = (ParameterSummary) iter.next();
				argList.jjtAddChild(builder.buildName(param.getName()), item);
				item++;
			}
		}

		methodDecl.jjtAddChild(eci, index);
	}


	/**
	 *  Drills down into the class definition to add the method
	 *
	 *@param  node  the type syntax tree node that is being modified
	 */
	private void drillIntoType(SimpleNode node) {
		ASTClassDeclaration classDecl = (ASTClassDeclaration) node.jjtGetFirstChild();
		if (isAbstract()) {
			classDecl.setAbstract(true);
		}
		SimpleNode unmodified = (SimpleNode) classDecl.jjtGetFirstChild();
		SimpleNode classBody = (SimpleNode) unmodified.jjtGetChild(unmodified.jjtGetNumChildren() - 1);
		ASTClassBodyDeclaration decl = new ASTClassBodyDeclaration(JavaParserTreeConstants.JJTCLASSBODYDECLARATION);
		decl.jjtAddChild(build(), 0);
		classBody.jjtAddChild(decl, classBody.jjtGetNumChildren());
	}


	/**
	 *  Builds the method to be adding
	 *
	 *@return    a syntax tree branch containing the new method
	 */
	private ASTConstructorDeclaration build() {
		ASTConstructorDeclaration methodDecl = new ASTConstructorDeclaration(0);

		//  Set the modifiers
		copyModifiers(methodSummary,methodDecl);

		//  Set the declaration
		methodDecl.setName(typeName);
		ASTFormalParameters params = createParameters();
		methodDecl.jjtAddChild(params, 0);

		int index = 1;

		index = addExceptions(methodDecl, index);

		addBody(methodDecl, index);

		return methodDecl;
	}


	/**
	 *  Builds the type from the type declaration summary
	 *
	 *@param  summary  the summary
	 *@return          the AST type node
	 */
	private ASTType buildType(TypeDeclSummary summary) {
		ASTType type = new ASTType(JavaParserTreeConstants.JJTTYPE);
		if (summary.getArrayCount()==0 && summary.isPrimitive()) {
			type.jjtAddChild(buildPrimitive(summary), 0);
		} else {
                        type.jjtAddChild(buildReferenceType(summary), 0);
		}
		return type;
	}


	/**
	 *  Builds the name of the type from the type decl summary
	 *
	 *@param  summary  the summary
	 *@return          the name node
	 */
	private ASTName buildName(TypeDeclSummary summary) {
		ASTName name = new ASTName();
		name.fromString(summary.getLongName());
		return name;
	}
        
        
	/**
	 *  Builds the name of the type from the type decl summary
	 *
	 *@param  summary  the summary
	 *@return          the name node
	 */
	private ASTClassOrInterfaceType buildClassName(TypeDeclSummary summary) {
		ASTClassOrInterfaceType name = new ASTClassOrInterfaceType(JavaParserTreeConstants.JJTCLASSORINTERFACETYPE);
		name.fromString(summary.getLongName());
		return name;
	}
        
        
	/**
	 *  Builds the name of the type from the type decl summary
	 *
	 *@param  summary  the summary
	 *@return          the name node
	 */
	private ASTPrimitiveType buildPrimitive(TypeDeclSummary summary) {
		ASTPrimitiveType primitive = new ASTPrimitiveType(JavaParserTreeConstants.JJTPRIMITIVETYPE);
		primitive.setName(summary.getLongName());
		return primitive;
	}
        
        
	/**
	 *  Builds the name of the type from the type decl summary
	 *
	 *@param  summary  the summary
	 *@return          the name node
	 */
	private ASTReferenceType buildReferenceType(TypeDeclSummary summary) {
                ASTReferenceType reference = new ASTReferenceType(JavaParserTreeConstants.JJTREFERENCETYPE);
		if (summary.isPrimitive()) {
			reference.jjtAddChild(buildPrimitive(summary), 0);
		} else {
                        reference.jjtAddChild(buildClassName(summary), 0);
		}
                reference.setArrayCount(summary.getArrayCount());
		return reference;
	}
}
