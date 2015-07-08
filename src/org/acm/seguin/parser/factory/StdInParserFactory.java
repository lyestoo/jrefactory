package org.acm.seguin.parser.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 *  Generates new parsers for standard input 
 *
 *@author    Chris Seguin 
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
	protected InputStream getInputStream() {
		return System.in;
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
