/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.pretty;

import org.acm.seguin.parser.Token;



/**
 *  Store a portion of a javadoc item
 *
 *@author     Mike Atkinson
 *@created    June 22, 2003
 *@since      JRefactory 2.7.02
 */
public class XDocletJavaDocComponent extends JavaDocComponent
{


    /**
     *  Create an instance of this java doc object
     */
    public XDocletJavaDocComponent()
    {
        super();
    }

    /**
     *  Set the type
     *
     *@param  newType  the new type
     */
    public void setType(String newType)
    {
        super.setType(newType);
        setLongestLength(8);
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

        //  Break into lines
        StringBuffer sb = new StringBuffer();
        boolean startOnNewLine = false;
        int state = 0;
        XDocletTokenizer tok = new XDocletTokenizer(getDescription());
        while (tok.hasNext()) {
            Token next = tok.next();

            if ((next.image == null) || (next.image.length() == 0)) {
                //  Do nothing
            } else if (next.image.equals("=")) {
                //System.out.println("kind="+next.kind+", state="+state+", next.image="+next.image);
                startOnNewLine = true;
                //    System.out.println("  appending \" = \"");
                sb.append(" ");
                sb.append("=");
                sb.append(" ");
                state = 2;
            } else if (next.kind==XDocletTokenizer.SPACE) {
            } else if (next.kind==XDocletTokenizer.NEWLINE) {
                //System.out.println("kind="+next.kind+", state="+state+", next.image="+next.image);
                if (state != 0 && startOnNewLine) {
                    //System.out.println("  appending \""+next.image+"\"");
                    sb.append(next.image);
                    state = 0;
                }
            } else if (next.image.trim().equals("*")) { 
                //System.out.println("kind="+next.kind+", state="+state+", next.image="+next.image);
            } else {
                //System.out.println("kind="+next.kind+", state="+state+", next.image="+next.image);
                //System.out.println("  appending \""+next.image+"\"");
                sb.append(next.image);
                state++;
                if (state==3) {
                    //System.out.println("  appending \\n");
                    sb.append("\n");
                    state=0;
                }
            }
        }

        int oldIndent = printData.getJavadocIndent();
        if (startOnNewLine) {
            printData.indent();
            if (!printData.isStarsAlignedWithSlash()) {
                printData.space();
            }
            printData.appendComment("*", PrintData.JAVADOC_COMMENT);
            for (int i = 0; i < oldIndent+6; ++i) {
                printData.space();
            }
        }
        //System.out.println("printing: \""+sb.toString()+"\"");
        setDescription(sb.toString());
        printData.setJavadocIndent(oldIndent+6); // increment indent
        leaveDescription(printData);
        printData.newline();
        printData.setJavadocIndent(oldIndent);  // restore old indent

        // blank line after Xdoclet comment
        printData.indent();
        if (!printData.isStarsAlignedWithSlash()) {
            printData.space();
        }
        printData.appendComment("*", PrintData.JAVADOC_COMMENT);
    }

}

