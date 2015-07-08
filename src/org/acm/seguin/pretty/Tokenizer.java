/*
 *Author:  Mike Atkinson
 *
 *This software has been developed under the copyleft
 *rules of the GNU General Public License.  Please
 *consult the GNU General Public License for more
 *details about use and distribution of this software.
 */
package org.acm.seguin.pretty;


import org.acm.seguin.parser.Token;


/**
 *  Parses a XDoclet javadoc comment tag
 *
 * @author   Mike Atkinson
 * @created  June 22, 2003
 * @since    JRefactory 2.7.02
 */
public class Tokenizer {
    /**Description of the Field */
    protected StringBuffer buffer;
    /**Description of the Field */
    protected int index;
    /**Description of the Field */
    protected int last;
    /**Description of the Field */
    protected String value;

    /**  Represents spaces */
    public final static int SPACE = 0;

    /**  Represents newline */
    public final static int NEWLINE = 1;

    /**  Represents a word */
    public final static int WORD = 2;

    /**
     *  Constructor for the JavadocTokenizer object
     *
     * @paraminit  Description of Parameter
     * @paraminit  Description of Parameter
     */
    public Tokenizer(String init) {
        value = init;
        index = 0;
        buffer = new StringBuffer();
        last = value.length();
    }

    /**
     *  Description of the Method
     *
     * @return  Description of the Returned Value
     */
    public boolean hasNext() {
        return index < last;
    }

    /**
     *  Description of the Method
     *
     * @return  Description of the Returned Value
     */
    public Token next() {
        Token result = new Token();

        if (index == last) {
            result.kind = SPACE;
            result.image = " ";
            return result;
        }

        buffer.setLength(0);
        if (((index == 0) && (value.charAt(index) == '*')) ||
                ((index == 0) && (value.charAt(index) == '/')) ||
                (value.charAt(index) == '\r') ||
                (value.charAt(index) == '\n')) {
            if (value.charAt(index) == '/') {
                index++;
            }

            loadNewline();
            result.kind = NEWLINE;
            result.image = buffer.toString();

            //System.out.println("Found a newline:  [" + result.image + "]");
        } else if (Character.isWhitespace(value.charAt(index))) {
            loadSpace();
            result.kind = SPACE;
            result.image = buffer.toString();
            //System.out.println("Found a space:  [" + result.image + "]");
        } else {
            loadWord();
            result.kind = WORD;
            result.image = checkEnd(buffer.toString());
            //System.out.println("Found a word:  [" + result.image + "]");
        }

        return result;
    }

    /**  Loads a particular word. */
    protected void loadWord() {
        int start = index;

        while ((index < last) && !Character.isWhitespace(value.charAt(index))
                && ((value.charAt(index) != '<') || (index == start))) {
            buffer.append(value.charAt(index));
            index++;

            if (value.charAt(index - 1) == '>')
                return;
        }
    }

    /**
     *  Checks that the end of the word doesn't end with end of comment
     *
     * @paramvalue  Description of Parameter
     * @return      the revised value
     * @paramvalue  the value to check
     */
    private String checkEnd(String value) {
        if (value.endsWith("*/")) {
            return value.substring(0, value.length() - 2);
        }
        return value;
    }

    /**
     *  Moves from the end of one line past the * on the next line and possibly
     *  to the end of the comment.
     */
    private void loadNewline() {
        while ((index < last) && Character.isWhitespace(value.charAt(index))) {
            buffer.append(value.charAt(index));
            index++;
        }

        while ((index < last) && (value.charAt(index) == '*')) {
            buffer.append(value.charAt(index));
            index++;
        }

        if ((index < last) && (value.charAt(index - 1) == '*' && value.charAt(index) == '/')) {
            buffer.append(value.charAt(index));
            index++;
        }
    }

    /**  Description of the Method */
    private void loadSpace() {
        while ((index < last) && Character.isWhitespace(value.charAt(index))
                && (value.charAt(index) != '\n') && (value.charAt(index) != '\r')) {
            buffer.append(value.charAt(index));
            index++;
        }
    }

    /**
     *  Description of the Method
     *
     * @paramvalue  Description of Parameter
     * @return      Description of the Return Value
     * @paramvalue  Description of the Parameter
     */
    public static boolean hasContent(String value) {
        if (value == null) {
            return false;
        }

        int valueLength = value.length();

        if (valueLength == 0) {
            return false;
        }

        int start = 0;

        if (value.charAt(0) == '/') {
            start++;
        }


        int last = valueLength - 1;

        for (int ndx = start; ndx < last; ndx++) {
            char ch = value.charAt(ndx);

            if (!(Character.isWhitespace(ch) || (ch == '*'))) {
                return true;
            }
        }

        char ch = value.charAt(last);

        return !(Character.isWhitespace(ch) || (ch == '*') || (ch == '/'));
    }
}

