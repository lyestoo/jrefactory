package org.acm.seguin.parser.factory;


import java.io.Reader;
import java.io.InputStreamReader;

/**
 *  Generates new parsers for standard input 
 *
 *@author     Chris Seguin 
 *@author     <a href="JRefactory@ladyshot.demon.co.uk">Mike Atkinson</a>
 *@version    $Id: StdInParserFactory.java,v 1.2 2003/07/29 20:51:55 mikeatkinson Exp $ 
 */
public class StdInParserFactory extends ParserFactory {
	/**
	 *  Constructor for a standard input ParserFactory 
	 */
	public StdInParserFactory() {
	}


	/**
	 *  Return the input stream 
	 *
	 *@return    the input stream 
	 */
	protected Reader getReader() {
		return new InputStreamReader(System.in);
	}


	/**
	 *  A method to return some key identifying the file that is being parsed 
	 *
	 *@return    the identifier 
	 */
	protected String getKey() {
		return "Standard Input";
	}
}
