/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.parser.factory;

import java.io.Reader;
import java.io.StringReader;

/**
 *  Generates new parsers for a java file
 *
 *@author     Chris Seguin
 *@author     <a href="JRefactory@ladyshot.demon.co.uk">Mike Atkinson</a>
 *@version    $Id: BufferParserFactory.java,v 1.2 2003/07/29 20:51:55 mikeatkinson Exp $ 
 *@created    June 6, 1999
 */
public class BufferParserFactory extends ParserFactory {
	//  Instance Variables
	private String inputBuffer;


	/**
	 *  Constructor for the buffer parser factory
	 *
	 *@param  buffer  the initial buffer
	 */
	public BufferParserFactory(String buffer) {
		inputBuffer = buffer;
	}


	/**
	 *  Return the input stream
	 *
	 *@return    the input stream
	 */
	protected Reader getReader() {
            return new StringReader(inputBuffer);
	}


	/**
	 *  A method to return some key identifying the file that is being parsed
	 *
	 *@return    the identifier
	 */
	protected String getKey() {
		return "the current file";
	}
}

