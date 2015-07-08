package org.acm.seguin.parser.factory;

import java.io.Reader;
import java.util.EmptyStackException;
import org.acm.seguin.parser.ast.SimpleNode;
import org.acm.seguin.parser.JavaParser;
import org.acm.seguin.parser.ParseException;
import org.acm.seguin.awt.ExceptionPrinter;

/**
 *  Generates new parsers for a java file
 *
 *@author     Chris Seguin
 *@author     <a href="JRefactory@ladyshot.demon.co.uk">Mike Atkinson</a>
 *@version    $Id: ParserFactory.java,v 1.4 2003/09/18 23:07:29 mikeatkinson Exp $ 
 *@created    June 6, 1999
 */
public abstract class ParserFactory {
	//  Instance Variables
	private SimpleNode root = null;

	//  Class Variables
	private static JavaParser parser = null;


	/**
	 *  Return the AST
	 *
	 *@param  interactive         do we want to receive a response in the form of
	 *      a dialog box when a parse exception is encountered
	 *@return                     the simple node which represents the root
	 */
	public SimpleNode getAbstractSyntaxTree(boolean interactive) {
		//  Check to see if it is here yet
		if (root == null) {
			synchronized (ParserFactory.class) {
				//  Look it up
				JavaParser parser = getParser();
				if (parser == null) {
					return null;
				}

				//  Get the parse tree
				try {
					root = parser.CompilationUnit();
				} catch (ParseException pe) {
					System.out.println("ParserFactory Version 0.1:  Encountered errors during parse:  " + getKey());
					ExceptionPrinter.print(pe, interactive);

					return null;
				} catch (EmptyStackException ese) {
					System.out.println("ParserFactory Version 0.1:  Encountered errors during parse:  " + getKey());
					ExceptionPrinter.print(ese, false);
					root = null;
				}
			}
		}

		//  Return the tree
		return root;
	}


	/**
	 *  Return the input stream
	 *
	 *@return    the input stream
	 */
	protected abstract Reader getReader();


	/**
	 *  Create the parser
	 *
	 *@return    the java parser
	 */
	protected JavaParser getParser() {
		Reader in = getReader();
		if (in == null) {
			return null;
		}

		//  Create the parser
		if (parser == null) {
			ParserFactory.parser = new JavaParser(in);
		}
		else {
			ParserFactory.parser.ReInit(in);
		}

		return ParserFactory.parser;
	}


	/**
	 *  A method to return some key identifying the file that is being parsed
	 *
	 *@return    the identifier
	 */
	protected abstract String getKey();
}
