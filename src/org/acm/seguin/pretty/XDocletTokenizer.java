/*
 *Author:  Chris Seguin
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
public class XDocletTokenizer extends Tokenizer {

    /**
     *  Constructor for the JavadocTokenizer object
     *
     * @paraminit  Description of Parameter
     */
    public XDocletTokenizer(String init) {
        super(init);
    }

    /**  Loads a particular word. */
    protected void loadWord() {
        int start = index;
        boolean withinQuotes = false;

        while (true) {
            if (index == last)
                return;

            char c = value.charAt(index);

            if (!withinQuotes && c == '=') {
                if (index == start) {
                    buffer.append(c);
                    index++;
                }
                return;
            }
            if (!withinQuotes && Character.isWhitespace(c))
                return;
            if (c == '\"')
                withinQuotes = !withinQuotes;

            buffer.append(c);
            index++;
        }
    }

}

