/*
 *Author:  Chris Seguin
 *
 *This software has been developed under the copyleft
 *rules of the GNU General Public License.  Please
 *consult the GNU General Public License for more
 *details about use and distribution of this software.
 */
package org.acm.seguin.pretty;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.io.OutputStreamWriter;
import org.acm.seguin.awt.Question;
import org.acm.seguin.io.InplaceOutputStream;
import org.acm.seguin.parser.ast.SimpleNode;
import org.acm.seguin.parser.ast.ASTCompilationUnit;
import org.acm.seguin.util.FileSettings;
import org.acm.seguin.parser.factory.FileParserFactory;
import org.acm.seguin.parser.factory.ParserFactory;

import java.util.*;

/**
 *  Holds a refactoring. Default version just pretty prints the file.
 *
 * @author   Chris Seguin
 * @author   <a href="JRefactory@ladyshot.demon.co.uk">Mike Atkinson</a>
 * @created  July 1, 1999
 * @version  $Id: PrettyPrintFile.java,v 1.6 2003/09/18 23:07:29 mikeatkinson Exp $
 */
public class PrettyPrintFile {
//  Instance Variables
    private ParserFactory factory;
    private boolean ask;

    /**  Refactors java code. */
    public PrettyPrintFile() {
        ask = true;
    }

    /**
     *  Sets whether we should ask the user
     *
     * @paramway  The new Ask value
     * @paramway  the way to set the variable
     */
    public void setAsk(boolean way) {
        ask = way;
    }

    /**
     *  Set the parser factory
     *
     * @paramfactory  The new ParserFactory value
     * @paramfactory  Description of Parameter
     */
    public void setParserFactory(ParserFactory factory) {
        this.factory = factory;
    }

    /**
     *  Returns true if this refactoring is applicable
     *
     * @paraminputFile  Description of Parameter
     * @return          true if this refactoring is applicable
     * @paraminputFile  the input file
     */
    public boolean isApplicable(File inputFile) {
        if (!inputFile.canWrite()) {
            return false;
        }

        boolean result = true;

        if (ask) {
            result = Question.isYes("Pretty Printer",
                    "Do you want to pretty print the file\n" + inputFile.getPath() + "?");
        }

//  Create a factory if necessary
        if (result) {
            setParserFactory(new FileParserFactory(inputFile));
        }

        return result;
    }

    /**
     *  Return the factory that gets the abstract syntax trees
     *
     * @return  the parser factory
     */
    public ParserFactory getParserFactory() {
        return factory;
    }

    /**
     *  Apply the refactoring
     *
     * @paraminputFile  Description of Parameter
     * @paraminputFile  the input file
     */
    public void apply(File inputFile) {
        SimpleNode root = factory.getAbstractSyntaxTree(true);

        if (root!=null) {
           apply(inputFile, root);
           postApply(inputFile, (ASTCompilationUnit) root);
        }
    }

    /**
     *  Apply the refactoring
     *
     * @paraminputFile  Description of Parameter
     * @paramroot       Description of Parameter
     * @paraminputFile  the input file
     * @paramroot       Description of Parameter
     */
    public void apply(File inputFile, SimpleNode root) {
        if (root != null) {
            FileSettings pretty = FileSettings.getRefactoryPrettySettings();

            pretty.setReloadNow(true);

//  Create the visitor
            PrettyPrintVisitor printer = new PrettyPrintVisitor();

//  Create the appropriate print data
            PrintData data = getPrintData(inputFile);

            if (root instanceof ASTCompilationUnit) {
                printer.visit((ASTCompilationUnit) root, data);
            }
            else {
                printer.visit(root, data);
            }

//  Flush the output stream
            data.close();
        }
    }

    /**
     *  Create the output stream
     *
     * @paramfile  Description of Parameter
     * @return     the output stream
     * @paramfile  the name of the file
     */
    protected Writer getWriter(File file) {
//  Local Variables
        Writer out = null;

        try {
            out = new OutputStreamWriter(new InplaceOutputStream(file));
        }
        catch (IOException ioe) {
            out = new OutputStreamWriter(System.out);
        }

//  Return the output stream
        return out;
    }

    /**
     *  Return the appropriate print data
     *
     * @paraminput  Description of Parameter
     * @return      the print data
     * @paraminput  Description of Parameter
     */
    protected PrintData getPrintData(File input) {
        JavadocTags.get().reload();

//  Create the new stream
        return new PrintData(getWriter(input));
    }

    /**
     *  Apply the refactoring
     *
     * @paraminputFile  Description of Parameter
     * @paramroot       Description of Parameter
     * @paraminputFile  the input file
     * @paramroot       Description of Parameter
     */
    protected void postApply(File inputFile, ASTCompilationUnit root) {
    }
}

