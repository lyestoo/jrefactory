/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.parser;
/*
 * Generated By:JavaCC: Do not edit this line. TokenMgrError.java Version 0.7pre2
 */
/**
 *  Description of the Class 
 *
 *@author     Chris Seguin 
 *@created    October 14, 1999 
 */
public class TokenMgrError extends Error {

	int errorCode;
	/*
	 * Ordinals for various reasons why an Error of this type can be thrown.
	 */

	/**
	 *  Lexical error occured. 
	 */
	final static int LEXICAL_ERROR = 0;

	/**
	 *  An attempt wass made to create a second instance of a static token 
	 *  manager. 
	 */
	final static int STATIC_LEXER_ERROR = 1;

	/**
	 *  Tried to change to an invalid lexical state. 
	 */
	final static int INVALID_LEXICAL_STATE = 2;

	/**
	 *  Detected (and bailed out of) an infinite loop in the token manager. 
	 */
	final static int LOOP_DETECTED = 3;


	/*
	 * Constructors of various flavors follow.
	 */

	/**
	 *  Constructor for the TokenMgrError object 
	 */
	public TokenMgrError() {
	}


	/**
	 *  Constructor for the TokenMgrError object 
	 *
	 *@param  message  Description of Parameter 
	 *@param  reason   Description of Parameter 
	 */
	public TokenMgrError(String message, int reason) {
		super(message);
		errorCode = reason;
	}


	/**
	 *  Constructor for the TokenMgrError object 
	 *
	 *@param  EOFSeen      Description of Parameter 
	 *@param  lexState     Description of Parameter 
	 *@param  errorLine    Description of Parameter 
	 *@param  errorColumn  Description of Parameter 
	 *@param  errorAfter   Description of Parameter 
	 *@param  curChar      Description of Parameter 
	 *@param  reason       Description of Parameter 
	 */
	public TokenMgrError(boolean EOFSeen, int lexState, int errorLine, int errorColumn, String errorAfter, char curChar, int reason) {
		this(LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar), reason);
	}


	/**
	 *  You can also modify the body of this method to customize your error 
	 *  messages. For example, cases like LOOP_DETECTED and INVALID_LEXICAL_STATE 
	 *  are not of end-users concern, so you can return something like : 
	 *  "Internal Error : Please file a bug report .... " from this method for 
	 *  such cases in the release version of your parser. 
	 *
	 *@return    Description of the Returned Value 
	 */
	public String getMessage() {
		return super.getMessage();
	}


	/**
	 *  Replaces unprintable characters by their espaced (or unicode escaped) 
	 *  equivalents in the given string 
	 *
	 *@param  str  Description of Parameter 
	 *@return      Description of the Returned Value 
	 */
	protected final static String addEscapes(String str) {
		StringBuffer retval = new StringBuffer();
		char ch;
		for (int i = 0; i < str.length(); i++) {
			switch (str.charAt(i)) {
				case 0:
					continue;
				case '\b':
					retval.append("\\b");
					continue;
				case '\t':
					retval.append("\\t");
					continue;
				case '\n':
					retval.append("\\n");
					continue;
				case '\f':
					retval.append("\\f");
					continue;
				case '\r':
					retval.append("\\r");
					continue;
				case '\"':
					retval.append("\\\"");
					continue;
				case '\'':
					retval.append("\\\'");
					continue;
				case '\\':
					retval.append("\\\\");
					continue;
				default:
					if ((ch = str.charAt(i)) < 0x20 || ch > 0x7e) {
						String s = "0000" + Integer.toString(ch, 16);
						retval.append("\\u" + s.substring(s.length() - 4, s.length()));
					}
					else {
						retval.append(ch);
					}
					continue;
			}
		}
		return retval.toString();
	}


	/**
	 *  Returns a detailed message for the Error when it is thrown by the token 
	 *  manager to indicate a lexical error. Parameters : EOFSeen : indicates if 
	 *  EOF caused the lexicl error curLexState : lexical state in which this 
	 *  error occured errorLine : line number when the error occured errorColumn 
	 *  : column number when the error occured errorAfter : prefix that was seen 
	 *  before this error occured curchar : the offending character Note: You can 
	 *  customize the lexical error message by modifying this method. 
	 *
	 *@param  EOFSeen      Description of Parameter 
	 *@param  lexState     Description of Parameter 
	 *@param  errorLine    Description of Parameter 
	 *@param  errorColumn  Description of Parameter 
	 *@param  errorAfter   Description of Parameter 
	 *@param  curChar      Description of Parameter 
	 *@return              Description of the Returned Value 
	 */
	private final static String LexicalError(boolean EOFSeen, int lexState, int errorLine, int errorColumn, String errorAfter, char curChar) {
		return ("Lexical error at line " + 
				errorLine + ", column " + 
				errorColumn + ".  Encountered: " + 
				(EOFSeen ? "<EOF> " : ("\"" + addEscapes(String.valueOf(curChar)) + "\"") + " (" + (int) curChar + "), ") + 
				"after : \"" + addEscapes(errorAfter) + "\"");
	}
}
