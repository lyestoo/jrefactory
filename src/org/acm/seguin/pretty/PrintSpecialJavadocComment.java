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
 *  Consume a javadoc comment
 *
 *@author     Chris Seguin
 *@author     Mike Atkinson
 *@created    April 10, 1999
 */
public class PrintSpecialJavadocComment extends PrintSpecial
{
    /**
     *  Determines if this print special can handle the current object
     *
     *@param  spec  Description of Parameter
     *@return       true if this one should process the input
     */
    public boolean isAcceptable(SpecialTokenData spec)
    {
        return (spec.getTokenType() == JavaParserConstants.FORMAL_COMMENT);
    }


    /**
     *  Processes the special token
     *
     *@param  node  the type of node this special is being processed for
     *@param  spec  the special token data
     *@return       Description of the Returned Value
     */
    public boolean process(Node node, SpecialTokenData spec)
    {
        JavaDocable docable = null;

        if (node instanceof JavaDocable) {
            docable = (JavaDocable) node;
        }
        else if (spec.getPrintData().isAllJavadocKept()) {
            docable = new JavaDocableImpl();
        }
        else {
            return false;
        }

        //  Current components
        JavaDocComponent jdc = new JavaDocComponent();
        StringBuffer description = new StringBuffer();

        //  Break into lines
        JavadocTokenizer tok = new JavadocTokenizer(spec.getTokenImage());
        while (tok.hasNext()) {
            Token next = tok.next();

            if ((next.image == null) || (next.image.length() == 0)) {
                //  Do nothing
            }
            else if (isJavadocTag(next)) {
                storeJDCinNode(docable, jdc, description);
                jdc = createJavaDocComponent(next.image, tok, description);
            }
            else {
                description.append(next.image);
            }
        }

        //  Last JDC
        if (jdc != null) {
            storeJDCinNode(docable, jdc, description);
        }

        //  Finish the JavaDoc by adding @param, @return, @throws from method
        // but only if this is the last JavaDoc before the method.
        if (spec.isLastJavadocComment()) {
            docable.finish();
        }
        docable.printJavaDocComponents(spec.getPrintData());
        return true;
    }


    /**
     *  Checks to see if the token is a javadoc tag that should be separated out
     *  from the rest of the code.
     *
     *@param  next  the token that we are checking
     *@return       true if this tag should be beautified
     */
    private boolean isJavadocTag(Token next)
    {
        if (next.kind != JavadocTokenizer.WORD) {
            return false;
        }

        String image = next.image;
        if (image.charAt(0) != '@') {
            return false;
        }

        int imageLength = image.length();
        if (image.charAt(imageLength - 1) == '@') {
            return false;
        }

        for (int ndx = 1; ndx < imageLength; ndx++) {
                char c = image.charAt(ndx);
        	if (!(Character.isLetterOrDigit(c) || c=='.' || c=='-') )
        		return false;
        }

        return true;
    }


    /**
     *  Create new JavaDocComponent
     *
     *@param  current  the current item
     *@param  parts    the tokenizer
     *@return          the new JavaDocComponent
     */
    private JavaDocComponent createJavaDocComponent(String current, JavadocTokenizer parts, StringBuffer descr)
    {
        JavaDocComponent jdc;

        //  Create the new jdc
        if (current.equals("@param") || current.equals("@exception") || current.equals("@throws")) {
            jdc = new NamedJavaDocComponent();
            jdc.setType(current);

            while (parts.hasNext()) {
                Token next = parts.next();
                if (next.kind == JavadocTokenizer.WORD) {
                    ((NamedJavaDocComponent) jdc).setID(next.image);
                    return jdc;
                }
            }

            return null;
        }
        else if (current.indexOf('.')>=0) {
            jdc = new XDocletJavaDocComponent();
            jdc.setType(current);
        }
        else {
            jdc = new NamedJavaDocComponent();
            jdc.setType(current);
        }

        //  Return the result
        return jdc;
    }


    /**
     *  Store JavaDocComponent in the node
     *
     *@param  node   the next node
     *@param  jdc    the component
     *@param  descr  the description
     */
    private void storeJDCinNode(JavaDocable node, JavaDocComponent jdc, StringBuffer descr)
    {
        if (jdc == null) {
            return;
        }

        jdc.setDescription(descr.toString().trim());
        node.addJavaDocComponent(jdc);
        descr.setLength(0);
    }
}
