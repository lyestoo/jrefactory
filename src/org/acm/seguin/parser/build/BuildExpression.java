package org.acm.seguin.parser.build;

import org.acm.seguin.parser.ast.*;

/**
 *  This object builds an expression.  The first method builds
 *  an expression based on the name of the argument.
 *
 *@author    Chris Seguin
 */
public class BuildExpression {
	/**
	 *  Builds an expression based on a name
	 *
	 *@param  name  the name of the field or parameter
	 *@return       the expression
	 */
	public ASTExpression buildName(String name) {
		ASTExpression expression = new ASTExpression(0);

		ASTConditionalExpression condExpression = new ASTConditionalExpression(0);
		expression.jjtAddFirstChild(condExpression);

		ASTConditionalOrExpression condOrExpression = new ASTConditionalOrExpression(0);
		condExpression.jjtAddFirstChild(condOrExpression);

		ASTConditionalAndExpression condAndExpression = new ASTConditionalAndExpression(0);
		condOrExpression.jjtAddFirstChild(condAndExpression);

		ASTInclusiveOrExpression inclOrExpression = new ASTInclusiveOrExpression(0);
		condAndExpression.jjtAddFirstChild(inclOrExpression);

		ASTExclusiveOrExpression exclOrExpression = new ASTExclusiveOrExpression(0);
		inclOrExpression.jjtAddFirstChild(exclOrExpression);

		ASTAndExpression andExpression = new ASTAndExpression(0);
		exclOrExpression.jjtAddFirstChild(andExpression);

		ASTEqualityExpression equalExpression = new ASTEqualityExpression(0);
		andExpression.jjtAddFirstChild(equalExpression);

		ASTInstanceOfExpression instanceOfExpression = new ASTInstanceOfExpression(0);
		equalExpression.jjtAddFirstChild(instanceOfExpression);

		ASTRelationalExpression relationalExpression = new ASTRelationalExpression(0);
		instanceOfExpression.jjtAddFirstChild(relationalExpression);

		ASTShiftExpression shiftExpression = new ASTShiftExpression(0);
		relationalExpression.jjtAddFirstChild(shiftExpression);

		ASTAdditiveExpression addExpression = new ASTAdditiveExpression(0);
		shiftExpression.jjtAddFirstChild(addExpression);

		ASTMultiplicativeExpression multExpression = new ASTMultiplicativeExpression(0);
		addExpression.jjtAddFirstChild(multExpression);

		ASTUnaryExpression unaryExpression = new ASTUnaryExpression(0);
		multExpression.jjtAddFirstChild(unaryExpression);

		ASTUnaryExpressionNotPlusMinus uenpm = new ASTUnaryExpressionNotPlusMinus(0);
		unaryExpression.jjtAddFirstChild(uenpm);

		ASTPostfixExpression postfixExpression = new ASTPostfixExpression(0);
		uenpm.jjtAddFirstChild(postfixExpression);

		ASTPrimaryExpression primaryExpression = new ASTPrimaryExpression(0);
		postfixExpression.jjtAddFirstChild(primaryExpression);

		ASTPrimaryPrefix primaryPrefix = new ASTPrimaryPrefix(0);
		primaryExpression.jjtAddFirstChild(primaryPrefix);

		ASTName nameNode = new ASTName();
		nameNode.addNamePart(name);
		primaryExpression.jjtAddFirstChild(nameNode);

		return expression;
	}
}
