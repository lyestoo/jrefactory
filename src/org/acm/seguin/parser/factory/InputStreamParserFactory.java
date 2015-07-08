package org.acm.seguin.parser.factory;

import java.io.Reader;

/**
 *  Generates new parsers for a java file 
 *
 *@author     Chris Seguin 
 *@author     <a href="JRefactory@ladyshot.demon.co.uk">Mike Atkinson</a>
 *@version    $Id: InputStreamParserFactory.java,v 1.2 2003/07/29 20:51:55 mikeatkinson Exp $ 
 *@created    June 6, 1999 
 */
public class InputStreamParserFactory extends ParserFactory {
	//  Instance Variables
	private Reader reader;
	private String key;


	/**
	 *  Constructor for a file ParserFactory 
	 *
	 *@param  inputStream  Description of Parameter 
	 *@param  initKey      Description of Parameter 
	 */
	public InputStreamParserFactory(Reader reader, String initKey) {
		this.reader = reader;
		key = initKey;
	}


	/**
	 *  Return the input stream 
	 *
	 *@return    the input stream 
	 */
	protected Reader getReader() {
		return reader;
	}


	/**
	 *  A method to return some key identifying the file that is being parsed 
	 *
	 *@return    the identifier 
	 */
	protected String getKey() {
		return key;
	}
}

