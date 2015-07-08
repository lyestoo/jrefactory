/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.pretty;




/**
 *  Store a portion of a javadoc item
 *
 *@author     Chris Seguin
 *@created    April 15, 1999
 */
public class NamedJavaDocComponent extends JavaDocComponent
{
    //  Instance Variable
    private String id;


    /**
     *  Create an instance of this java doc object
     */
    public NamedJavaDocComponent()
    {
        super();
        id = "";
    }


    /**
     *  Set the id
     *
     *@param  newID  the new id
     */
    public void setID(String newID)
    {
        if (newID != null) {
            id = newID;
            setLongestLength(getType().length() + id.length() + 2);
        }
    }


    /**
     *  Return the id
     *
     *@return    the id
     */
    public String getID()
    {
        return id;
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
        printData.indent();
        if (!printData.isStarsAlignedWithSlash()) {
            printData.space();
        }
        printData.appendComment("*", PrintData.JAVADOC_COMMENT);

        if (printData.isSpaceBeforeAt()) {
            printData.appendComment(" ", PrintData.JAVADOC_COMMENT);
        }

        //  Print the type
        printData.appendComment(getType(), PrintData.JAVADOC_COMMENT);
        for (int i = 0; i < printData.getJavadocIndent(); ++i) {
            printData.appendComment(" ", PrintData.JAVADOC_COMMENT);
        }

        //  Print the ID
        printData.appendComment(getID(), PrintData.JAVADOC_COMMENT);

        if (!printData.isJavadocLinedUp() && getID().length() > 0) {
            // Add a space after "param foo", "throws exc", etc.
            printData.appendComment(" ", PrintData.JAVADOC_COMMENT);
        }
        else if (printData.isKeepErroneousJavadocTags() && getType().endsWith("error")) {
            printData.appendComment(" ", PrintData.JAVADOC_COMMENT);
        }

        if (printData.isJavadocDescriptionLinedup()) {
            LineUpIndent(printData);
        }
        else {
            NoLineUpIndent(printData);
        }

        printData.newline();
    }


    /**
     *  The indent lines should line up
     *
     *@param  printData  the print data
     */
    private void LineUpIndent(PrintData printData)
    {
        if (printData.isJavadocLinedUp()) {
            //  Pad extra spaces after the ID
            int extra = getLongestLength() - (getType().length() + getID().length()) - 1;
            for (int ndx = 0; ndx < extra; ndx++) {
                printData.appendComment(" ", PrintData.JAVADOC_COMMENT);
            }

            // word wrap the description with the proper indent
            int indent = getLongestLength();

            if (printData.isSpaceBeforeAt()) {
                // we need to take into account the leading space before "@" if present
                indent++;
            }

            wordwrapDescription(printData, indent);
        }
        else {
            //  Print the description, no line up
            printDescription(printData);
        }
    }


    /**
     *  This is the old style javadoc line up
     *
     *@param  printData  the print data
     */
    private void NoLineUpIndent(PrintData printData)
    {
        if (printData.isJavadocLinedUp()) {
            //  Pad extra spaces after the ID
            int extra = getLongestLength() - (getType().length() + getID().length());
            for (int ndx = 0; ndx < extra; ndx++) {
                printData.appendComment(" ", PrintData.JAVADOC_COMMENT);
            }
        }

        //  Print the description
        printDescription(printData);
    }
}
/*******\
\*******/
