/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.pretty.ai;

/**
 *  Parses a variable name into different words
 *
 *@author    Chris Seguin
 */
public class ParseVariableName {
	private final static int INITIAL = 0;
	private final static int NORMAL = 1;
	private final static int ACRONYM = 2;


	/**
	 *  This breaks words around capitalization except when there are
	 *  capitalization occurs two or more times in a row.
	 *
	 *@param  value  the string to break
	 *@return        the broken string
	 */
	public String parse(String value)
	{
		StringBuffer buffer = new StringBuffer();
		int state = INITIAL;

		int last = value.length();
		for (int ndx = 0; ndx < last; ndx++) {
			char ch = value.charAt(ndx);
			if (Character.isUpperCase(ch)) {
				if (state == NORMAL) {
					buffer.append(" ");
				}

				if (ndx + 1 == last) {
					//  Don't adjust state
				}
				else if (Character.isUpperCase(value.charAt(ndx + 1))) {
					state = ACRONYM;
				}
				else {
					if (state == ACRONYM)
						buffer.append(" ");
					state = NORMAL;
				}

				if (state == ACRONYM) {
					buffer.append(ch);
				}
				else {
					buffer.append(Character.toLowerCase(ch));
				}
			}
			else {
				buffer.append(ch);
				state = NORMAL;
			}
		}

		return buffer.toString();
	}
}
