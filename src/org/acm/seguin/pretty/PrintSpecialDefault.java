/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.pretty;
import org.acm.seguin.parser.Node;
/*
 * Copyright 1999
 * 
 * Chris Seguin
 */

/**
 *  Consume unknonwn special tokens 
 *
 *@author     Chris Seguin 
 *@created    October 14, 1999 
 *@date       April 10, 1999 
 */
public class PrintSpecialDefault extends PrintSpecial {
	/**
	 *  Determines if this print special can handle the current object 
	 *
	 *@param  spec  Description of Parameter 
	 *@return       true if this one should process the input 
	 */
	public boolean isAcceptable(SpecialTokenData spec) {
		return true;
	}


	/**
	 *  Processes the special token 
	 *
	 *@param  node  the type of node this special is being processed for 
	 *@param  spec  the special token data 
	 *@return       Description of the Returned Value 
	 */
	public boolean process(Node node, SpecialTokenData spec) {
		System.err.println("Unknown special token of type:  " + spec.getTokenType());
		return false;
	}
}
