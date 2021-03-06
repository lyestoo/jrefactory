/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.pretty;


import java.util.StringTokenizer;


/**
 *  Store a portion of a javadoc item
 *
 *@author     Chris Seguin
 *@created    April 15, 1999
 */
public class JavaDocComponent
{
    private int longestLength = 0;
    private String description;
    private boolean printed;
    private boolean required;
    //  Instance Variable
    private String type;


    /**
     *  Create an instance of this java doc object
     */
    public JavaDocComponent()
    {
        type = "";
        description = "";
        printed = false;
        required = false;
    }


    /**
     *  Set the description
     *
     *@param  newDescription  the new description
     */
    public void setDescription(String newDescription)
    {
        if (newDescription != null) {
            description = newDescription;
        }
    }


    /**
     *  Set the longestLength
     *
     *@param  newLongestLength  the new longestLength
     */
    public void setLongestLength(int newLongestLength)
    {
        longestLength = newLongestLength;
    }


    /**
     *  Note that this node is required
     *
     *@param  req  true if it is required
     */
    public void setRequired(boolean req)
    {
        required = req;
    }


    /**
     *  Set the type
     *
     *@param  newType  the new type
     */
    public void setType(String newType)
    {
        if (newType != null) {
            type = newType;
            setLongestLength(type.length() + 2);
        }
    }


    /**
     *  Return the description
     *
     *@return    the description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     *  Return the longestLength
     *
     *@return    the longestLength
     */
    public int getLongestLength()
    {
        return longestLength;
    }


    /**
     *  Return the type
     *
     *@return    the type
     */
    public String getType()
    {
        return type;
    }


    /**
     *  Return whether this node has been printed
     *
     *@return    true if it was printed
     */
    public boolean isPrinted()
    {
        return printed;
    }


    /**
     *  Return whether this node is required
     *
     *@return    true if it is required
     */
    public boolean isRequired()
    {
        return required;
    }


    /**
     *  Print this tag
     *
     *@param  printData  printData
     */
    public void print(PrintData printData)
    {
        //  We are now printing it
        setPrinted(true);

        //  Start the line
        if (!printData.isCurrentSingle()) {
            printData.indent();
            if (!printData.isStarsAlignedWithSlash()) {
                printData.space();
            }
            printData.appendComment("*", PrintData.JAVADOC_COMMENT);
        }

        if (printData.isSpaceBeforeAt() && !isDescription()) {
            printData.appendComment(" ", PrintData.JAVADOC_COMMENT);
        }

        //  Print the type
        if (!isDescription()) {
            printData.appendComment(getType(), PrintData.JAVADOC_COMMENT);
        }

        //  Pad extra spaces after the ID
        if (!isDescription() && printData.isJavadocLinedUp()) {
            int extra = getLongestLength() - getType().length();
            for (int ndx = 0; ndx < extra; ndx++) {
                printData.appendComment(" ", PrintData.JAVADOC_COMMENT);
            }
        }

        // Pad any extra spaces after the stars and before the text
        if (printData.isReformatComments() || !isDescription()) {
            for (int i = 0; i < printData.getJavadocIndent(); ++i) {
                printData.appendComment(" ", PrintData.JAVADOC_COMMENT);
            }
        }

        //  Print the description
        printDescription(printData);

        if (!printData.isCurrentSingle()) {
            printData.newline();
        }
    }


    /**
     *  Note that this node has been printed
     *
     *@param  prn  Description of Parameter
     */
    protected void setPrinted(boolean prn)
    {
        printed = prn;
    }


    /**
     *  Print the drescription
     *
     *@param  printData  the print data
     */
    protected void leaveDescription(PrintData printData)
    {
        StringBuffer sb = new StringBuffer(printData.getJavadocIndent());
        for (int i = 0; i < printData.getJavadocIndent(); ++i) {
            sb.append(" ");
        }
        String indent = sb.toString();

        StringTokenizer tok = new StringTokenizer(getDescription(), "\n\r");
        boolean first = true;
        while (tok.hasMoreTokens()) {
            String nextToken = tok.nextToken();
            if (!first) {
                printData.indent();
                if (!printData.isStarsAlignedWithSlash()) {
                    printData.space();
                }
                printData.appendComment("*", PrintData.JAVADOC_COMMENT);
                printData.appendComment(indent, PrintData.JAVADOC_COMMENT);
            }
            printData.appendComment(nextToken, PrintData.JAVADOC_COMMENT);
            first = false;
        }
    }


    /**
     *  Print the drescription
     *
     *@param  printData  the print data
     */
    protected void printDescription(PrintData printData)
    {
        int indent = 0;

        if (getType().length() == 0) {
            indent = printData.getJavadocIndent();
        }
        else {
            indent = printData.getTaggedJavadocDescription();
        }

        wordwrapDescription(printData, indent);
    }


    /**
     *  Print the drescription
     *
     *@param  printData  the print data
     *@param  indent     the number of spaces to indent
     */
    protected void wordwrapDescription(PrintData printData, int indent)
    {
        JavadocDescriptionPrinter jdp = new JavadocDescriptionPrinter(printData,
                getDescription(),
                indent);

        jdp.run();
    }


    /**
     *  returns true if this is a description
     *
     *@return    true if it is a description
     */
    boolean isDescription()
    {
        return (getType().length() == 0);
    }
}
/*******\
\*******/
