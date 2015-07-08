/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.pretty.ai;

import java.text.MessageFormat;
import org.acm.seguin.parser.Node;
import org.acm.seguin.parser.ast.ASTFormalParameter;
import org.acm.seguin.parser.ast.ASTFormalParameters;
import org.acm.seguin.parser.ast.ASTMethodDeclaration;
import org.acm.seguin.parser.ast.ASTMethodDeclarator;
import org.acm.seguin.parser.ast.ASTName;
import org.acm.seguin.parser.ast.ASTNameList;
import org.acm.seguin.parser.ast.ASTResultType;
import org.acm.seguin.parser.ast.ASTType;
import org.acm.seguin.parser.ast.ASTVariableDeclaratorId;
import org.acm.seguin.pretty.JavaDocableImpl;
import org.acm.seguin.util.FileSettings;
import org.acm.seguin.util.MissingSettingsException;
import org.acm.seguin.pretty.DescriptionPadder;
import org.acm.seguin.pretty.JavadocTags;

import org.acm.seguin.parser.ast.ASTReferenceType;
import org.acm.seguin.parser.ast.ASTVariance;
import org.acm.seguin.parser.ast.ASTTypeParameters;
import org.acm.seguin.parser.ast.ASTAttribute;

/**
 *  Basis for the artificial intelligence that analyzes the method and
 *  determines the appropriate javadoc descriptions
 *
 *@author    Chris Seguin
 *@author    Mike Atkinson
 */
public class MethodAnalyzer
{
	private ASTMethodDeclaration node;
	private JavaDocableImpl jdi;
	private ParseVariableName pvn;
	private JavadocTags jt;


	/**
	 *  Constructor for the MethodAnalyzer object
	 *
	 *@param  node  Description of Parameter
	 *@param  jdi   Description of Parameter
	 */
	public MethodAnalyzer(ASTMethodDeclaration node, JavaDocableImpl jdi)
	{
		this.node = node;
		this.jdi = jdi;
		pvn = new ParseVariableName();
		jt = JavadocTags.get();
	}


	/**
	 *  Makes sure all the java doc components are present. For methods and
	 *  constructors we need to do more work - checking parameters, return types,
	 *  and exceptions.
	 *
	 *@param  className  Description of Parameter
	 */
	public void finish(String className)
	{
		//  Get the resource bundle
		FileSettings bundle = FileSettings.getRefactoryPrettySettings();

		//  Require a description of this method
		requireDescription(bundle, className);

		String methodTags = "return,param,exception,throws";
		try {
			methodTags = bundle.getString("method.tags");
		}
		catch (MissingSettingsException mse) {
		}

		//  Check that if there is a return type
		if (methodTags.indexOf("return") >= 0) {
			finishReturn(bundle);
		}

		//  Check for parameters
		if (methodTags.indexOf("param") >= 0) {
			finishParameters(bundle);
		}
		sortParameters(); // sort parameters into order the occur in the method

		//  Check for exceptions
		if ((methodTags.indexOf("exception") >= 0) ||
		    (methodTags.indexOf("throws") >= 0) ||
		    (methodTags.indexOf(jt.getExceptionTag()) >= 0)) {
			finishExceptions(bundle);
		}
	}


	/**
	 *  Determine if this is a setter method
	 *
	 *@return    true if it is a setter
	 */
	private boolean isSetter()
	{
		String name = getName();
		return ((name.length() > 3) && name.startsWith("set") &&
				Character.isUpperCase(name.charAt(3)));
	}


	/**
	 *  Determine if this is a getter method
	 *
	 *@return    true if it is a getter
	 */
	private boolean isGetter()
	{
		String name = getName();
		return ((name.length() > 3) && (name.startsWith("get") && Character.isUpperCase(name.charAt(3))) ||
				((name.length() > 2) && name.startsWith("is") && Character.isUpperCase(name.charAt(2))));
	}


	/**
	 *  Determine if this is a getter method
	 *
	 *@return    true if it is a getter
	 */
	private boolean isAdder()
	{
		String name = getName();
		return (name.length() > 3) && (name.startsWith("add") && Character.isUpperCase(name.charAt(3)));
	}


	/**
	 *  Determine if this is a run method
	 *
	 *@return    true if it is a run method
	 */
	private boolean isRunMethod()
	{
		String name = getName();
		return name.equals("run");
	}


	/**
	 *  Gets the MainMethod attribute of the MethodAnalyzer object
	 *
	 *@return    The MainMethod value
	 */
	private boolean isMainMethod()
	{
		String name = getName();
		if (!(name.equals("main") && node.isStatic()))
		{
			return false;
		}

                // If it has JDK 1.5 type parameters it cannot be a main() method.
                if (node.jjtGetChild(0) instanceof ASTTypeParameters) {
			return false;
                }
                // If it has JDK 1.5 attributes it cannot be a main() method.
                if (node.jjtGetChild(0) instanceof ASTAttribute) {
			return false;
                }
                
		//  Check for the void return type
		ASTResultType result = (ASTResultType) node.jjtGetChild(0);
		if (result.hasAnyChildren())
		{
			return false;
		}

		//  Check the parameters
		ASTMethodDeclarator decl = (ASTMethodDeclarator) node.jjtGetChild(1);
		ASTFormalParameters params = (ASTFormalParameters) decl.jjtGetChild(0);
		int childCount = params.jjtGetNumChildren();
		if (childCount != 1)
		{
			return false;
		}

		ASTFormalParameter nextParam = (ASTFormalParameter) params.jjtGetChild(0);
		ASTType type = (ASTType) nextParam.jjtGetChild(0);
		Node child = type.jjtGetChild(0);
		if (child instanceof ASTReferenceType) {
                        ASTReferenceType reference = (ASTReferenceType)child;
                        childCount = reference.jjtGetNumChildren();
                        if (childCount != 2) {
                            return false;
                        }
                        if (reference.jjtGetChild(0) instanceof ASTName) {
                                ASTName nameNode = (ASTName) reference.jjtGetChild(0);
                                if (nameNode.getName().equals("String") || nameNode.getName().equals("java.lang.String")) {
                                        if (reference.jjtGetChild(1) instanceof ASTVariance) {
                                                return true;
                                        }
                                }
                        }
		}

		return false;
	}


	/**
	 *  Determine if this is a JUnit setUp method
	 *
	 *@return    true if it is a JUnit setUp method
	 */
	private boolean isJUnitSetupMethod()
	{
		String name = getName();
		return name.equals("setUp");
	}


	/**
	 *  Determine if this is a JUnit test method
	 *
	 *@return    true if it is a JUnit test method
	 */
	private boolean isJUnitTestMethod()
	{
		String name = getName();
		return name.startsWith("test");
	}


	/**
	 *  Determine if this is a JUnit tearDown method
	 *
	 *@return    true if it is a JUnit tearDown method
	 */
	private boolean isJUnitTeardownMethod()
	{
		String name = getName();
		return name.equals("tearDown");
	}


	/**
	 *  Determine if this is a JUnit suite method
	 *
	 *@return    true if it is a JUnit suite method
	 */
	private boolean isJUnitSuiteMethod()
	{
		String name = getName();
		return name.equals("suite");
	}


	/**
	 *  Returns the name of the method
	 *
	 *@return    the name
	 */
	private String getName()
	{
                int child = 0;
                if (node.jjtGetChild(0) instanceof ASTAttribute) {
                    child = 1;
                }
                if (node.jjtGetChild(child) instanceof ASTTypeParameters) {
                    ASTMethodDeclarator decl = (ASTMethodDeclarator) node.jjtGetChild(child+2);
                    return decl.getName();
                } else {
                    ASTMethodDeclarator decl = (ASTMethodDeclarator) node.jjtGetChild(child+1);
                    return decl.getName();
                }
	}


	/**
	 *  Guesses the name ofthe setter or getter's attribute
	 *
	 *@return    the attribute name
	 */
	private String getAttributeName()
	{
		String name = getName();

		if (!isGetter() && !isSetter() && !isAdder())
		{
			return "";
		}
		else if (name.startsWith("is"))
		{
			return name.substring(2);
		}
		else
		{
			return name.substring(3);
		}
	}


	/**
	 *  Gets the ParameterDescription attribute of the MethodAnalyzer object
	 *
	 *@param  bundle  Description of Parameter
	 *@param  param   Description of Parameter
	 *@return         The ParameterDescription value
	 */
	private String getParameterDescription(FileSettings bundle, String param)
	{
		String pattern = "";

		if (isSetter())
		{
			pattern = bundle.getString("setter.param.descr");
		}
		else if (isAdder())
		{
			pattern = bundle.getString("adder.param.descr");
		}
		else if (isMainMethod())
		{
			pattern = bundle.getString("main.param.descr");
		}
		else
		{
			pattern = jt.getParamDescr();
		}

		return createDescription(pattern, getAttributeName(), param);
	}


	/**
	 *  Gets the ReturnDescription attribute of the MethodAnalyzer object
	 *
	 *@param  bundle  Description of Parameter
	 *@return         The ReturnDescription value
	 */
	private String getReturnDescription(FileSettings bundle)
	{
		String pattern = "";

		if (isJUnitSuiteMethod())
		{
			pattern = bundle.getString("junit.suite.return.descr");
		}
		else if (isGetter())
		{
			pattern = bundle.getString("getter.return.descr");
		}
		else
		{
			pattern = jt.getReturnDescr();
		}

		return createDescription(pattern, getAttributeName(), "");
	}


	/**
	 *  Description of the Method
	 *
	 *@param  bundle  Description of Parameter
	 */
	private void finishReturn(FileSettings bundle)
	{
            int child = 0;
            if (node.jjtGetChild(0) instanceof ASTAttribute) {
                child++;
            }
            if (node.jjtGetChild(child) instanceof ASTTypeParameters) {
                child++;
            }
            ASTResultType result = (ASTResultType) node.jjtGetChild(child);
		if (result.hasAnyChildren())
		{
			if (!jdi.contains("@return"))
			{
				jdi.require("@return", getReturnDescription(bundle));
			}
		}
	}


	/**
	 *  Description of the Method
	 *
	 *@param  bundle  Description of Parameter
	 */
	private void finishParameters(FileSettings bundle)
	{
                int child = 0;
                if (node.jjtGetChild(0) instanceof ASTAttribute) {
                    child++;
                }
                if (node.jjtGetChild(child++) instanceof ASTTypeParameters) {
                    child++;
                }
                ASTMethodDeclarator decl = (ASTMethodDeclarator) node.jjtGetChild(child);
                ASTFormalParameters params = (ASTFormalParameters) decl.jjtGetChild(0);
                
		int childCount = params.jjtGetNumChildren();
		for (int ndx = 0; ndx < childCount; ndx++)
		{
			ASTFormalParameter nextParam = (ASTFormalParameter) params.jjtGetChild(ndx);
			ASTVariableDeclaratorId id = (ASTVariableDeclaratorId) nextParam.jjtGetChild(1);
			if (!jdi.contains("@param", id.getName()))
			{
				jdi.require("@param", id.getName(), getParameterDescription(bundle, id.getName()));
			}
		}
	}


	/**
	 *  Sort the "@param" elements of the method.
	 *
         *@since JRefactory 2.7.00
	 */
	private void sortParameters()
	{
                int child = 0;
                if (node.jjtGetChild(0) instanceof ASTAttribute) {
                    child = 1;
                }
                if (node.jjtGetChild(child++) instanceof ASTTypeParameters) {
                    child++;
                }
                ASTMethodDeclarator decl = (ASTMethodDeclarator) node.jjtGetChild(child);
                ASTFormalParameters params = (ASTFormalParameters) decl.jjtGetChild(0);
                
		int childCount = params.jjtGetNumChildren();
                String[] methodParams = new String[childCount];
		for (int ndx = 0; ndx < childCount; ndx++)
		{
			ASTFormalParameter nextParam = (ASTFormalParameter) params.jjtGetChild(ndx);
			ASTVariableDeclaratorId id = (ASTVariableDeclaratorId) nextParam.jjtGetChild(1);
                        methodParams[ndx] = id.getName();
		}
                jdi.sort("@param", methodParams);
	}


	/**
	 *  Description of the Method
	 *
	 *@param  bundle  Description of Parameter
	 */
	private void finishExceptions(FileSettings bundle)
	{
		if ((node.jjtGetNumChildren() > 2) && (node.jjtGetChild(2) instanceof ASTNameList))
		{
			String exceptionTagName = jt.getExceptionTag();

			ASTNameList exceptions = (ASTNameList) node.jjtGetChild(2);
			int childCount = exceptions.jjtGetNumChildren();
			for (int ndx = 0; ndx < childCount; ndx++)
			{
				ASTName name = (ASTName) exceptions.jjtGetChild(ndx);
				if (!jdi.contains("@exception", name.getName()) &&
				    !jdi.contains("@throws", name.getName()) &&
				    !jdi.contains(exceptionTagName, name.getName()))
				{
					jdi.require(exceptionTagName, name.getName(), jt.getExceptionDescr());
				}
			}
		}
	}


	/**
	 *  Create the description string
	 *
	 *@param  pattern    Description of Parameter
	 *@param  attribute  Description of Parameter
	 *@param  className  Description of Parameter
	 *@return            the expanded string
	 */
	private String createDescription(String pattern, String attribute, String className)
	{
		//  Description of the constructor
		Object[] nameArray = new Object[5];
		nameArray[0] = attribute;
		nameArray[1] = className;

		if (node.isStatic())
		{
			nameArray[2] = "class";
		}
		else
		{
			nameArray[2] = "object";
		}

		nameArray[3] = lowerCaseFirstLetter(attribute);
		nameArray[4] = pvn.parse(attribute);

		String msg = MessageFormat.format(pattern, nameArray);
		return msg;
	}


	/**
	 *  Require the description
	 *
	 *@param  bundle     Description of Parameter
	 *@param  className  Description of Parameter
	 */
	private void requireDescription(FileSettings bundle, String className)
	{
		String pattern = "";

		if (isJUnitSetupMethod())
		{
			pattern = bundle.getString("junit.setUp.descr");
		}
		else if (isJUnitTestMethod())
		{
			pattern = bundle.getString("junit.test.descr");
		}
		else if (isJUnitTeardownMethod())
		{
			pattern = bundle.getString("junit.tearDown.descr");
		}
		else if (isJUnitSuiteMethod())
		{
			pattern = bundle.getString("junit.suite.descr");
		}
		else if (isGetter())
		{
			pattern = bundle.getString("getter.descr");
		}
		else if (isSetter())
		{
			pattern = bundle.getString("setter.descr");
		}
		else if (isRunMethod())
		{
			pattern = bundle.getString("run.descr");
		}
		else if (isMainMethod())
		{
			pattern = bundle.getString("main.descr");
		}
		else if (isAdder())
		{
			pattern = bundle.getString("adder.descr");
		}
		else
		{
			pattern = bundle.getString("method.descr");
		}

		String message = createDescription(pattern, getAttributeName(), className);
		message = DescriptionPadder.padBuffer(message, bundle);
		jdi.require("", message);
	}


	/**
	 *  Description of the Method
	 *
	 *@param  value  Description of Parameter
	 *@return        Description of the Returned Value
	 */
	private String lowerCaseFirstLetter(String value)
	{
		if ((value == null) || (value.length() == 0))
		{
			return "";
		}
		if (value.length() == 1)
		{
			return value.toLowerCase();
		}
		return Character.toLowerCase(value.charAt(0)) + value.substring(1);
	}
}
