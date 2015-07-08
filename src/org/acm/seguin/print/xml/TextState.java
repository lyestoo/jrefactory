/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.print.xml;

import java.awt.Font;

/**
 *  State pattern that is used to print the XML file
 *
 *@author    Chris Seguin
 */
public class TextState extends State {
	private static State state = null;


	/**
	 *  Gets the Font attribute of the State object
	 *
	 *@return    The Font value
	 */
	public Font getFont() {
		if (font == null) {
			font = new Font("Monospaced", Font.PLAIN, getFontSize());
		}
		return font;
	}


	/**
	 *  The actual worker method that processes the line. This is what is defined
	 *  by the various states
	 *
	 *@param  line   the line
	 *@param  index  the index of the character
	 *@param  buf    the buffer
	 *@return        the state at the end of the line
	 */
	protected State processLine(String line, int index,
			StringBuffer buf) {
		State nextState = null;
		int length = line.length();

		while (nextState == null) {
			if (index == length) {
				print(buf);
				return this;
			}

			if ((length != index + 1) &&
					(line.charAt(index + 1) == '<')) {
				nextState = TagState.getState();
			}

			buf.append(line.charAt(index));
			index++;
		}

		print(buf);
		initState(nextState);
		buf.setLength(0);
		return nextState.processLine(line, index, buf);
	}


	/**
	 *  Gets the State attribute of the TextState class
	 *
	 *@return    The State value
	 */
	public static State getState() {
		if (state == null) {
			state = new TextState();
		}
		return state;
	}
}
