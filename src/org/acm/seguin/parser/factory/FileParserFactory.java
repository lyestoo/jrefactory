/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.parser.factory;

import java.io.File;
import java.io.Reader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;

/**
 *  Generates new parsers for a java file
 *
 *@author     Chris Seguin
 *@author     <a href="JRefactory@ladyshot.demon.co.uk">Mike Atkinson</a>
 *@version    $Id: FileParserFactory.java,v 1.2 2003/07/29 20:51:55 mikeatkinson Exp $ 
 *@created    June 6, 1999
 */
public class FileParserFactory extends ParserFactory {
	//  Instance Variables
	private File input;


	/**
	 *  Constructor for a file ParserFactory
	 *
	 *@param  file  the file that we want to create a parser for
	 */
	public FileParserFactory(File file) {
		input = file;
	}


	/**
	 *  Return the input stream
	 *
	 *@return    the input stream
	 */
	protected Reader getReader() {
		try {
			return new FileReader(input);
		}
		catch (FileNotFoundException fnfe) {
			System.err.println("Unable to find the file specified by " + getKey());
			return null;
		}
		catch (IOException ioe) {
			System.err.println("Unable to create the file " + getKey());
			return null;
		}
	}


	/**
	 *  A method to return some key identifying the file that is being parsed
	 *
	 *@return    the identifier
	 */
	protected String getKey() {
		return input.getAbsolutePath();
	}
}

