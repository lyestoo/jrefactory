/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.pretty;

import org.acm.seguin.parser.JavaParserConstants;
import org.acm.seguin.parser.Node;
import org.acm.seguin.parser.Token;

/**
 *  Consume a category comment
 *
 *@author     Chris Seguin
 *@created    July 23, 1999
 */
public class PrintSpecialCategoryComment extends PrintSpecial {
	/**
	 *  Determines if this print special can handle the current object
	 *
         *@FIXME this tries to handle Category comments, but convert non-Category comments
         *       to multiline comments. This convertion should really take place as part of
         *       the parser but I can't work out how to do it.
         *
	 *@param  spec  Description of Parameter
	 *@return       true if this one should process the input
	 */
	public boolean isAcceptable(SpecialTokenData spec) {
            if (spec.getTokenType() == JavaParserConstants.CATEGORY_COMMENT) {
		String image = spec.getTokenImage().trim();
                Token special = spec.getSpecialToken();
                if (image.endsWith(">*/") ) {
                   special.kind = JavaParserConstants.CATEGORY_COMMENT;
                   return true;
                } else {
                   special.kind = JavaParserConstants.MULTI_LINE_COMMENT;
                   return false;
                }
            }
            return false;
	}


	/**
	 *  Processes the special token
	 *
	 *@param  node  the type of node this special is being processed for
	 *@param  spec  the special token data
	 *@return       Description of the Returned Value
	 */
	public boolean process(Node node, SpecialTokenData spec) {
		//  Get the print data
		PrintData printData = spec.getPrintData();

		//  Make sure we are indented
		if (!printData.isLineIndented()) {
			printData.indent();
		}

		//  Print the comment
		String image = spec.getTokenImage().trim();
		printData.appendComment(image, PrintData.CATEGORY_COMMENT);

		return true;
	}
}
