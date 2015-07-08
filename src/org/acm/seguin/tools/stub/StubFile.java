/* ====================================================================
 * The JRefactory License, Version 1.0
 *
 * Copyright (c) 2001 JRefactory.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        JRefactory (http://www.sourceforge.org/projects/jrefactory)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "JRefactory" must not be used to endorse or promote
 *    products derived from this software without prior written
 *    permission. For written permission, please contact seguin@acm.org.
 *
 * 5. Products derived from this software may not be called "JRefactory",
 *    nor may "JRefactory" appear in their name, without prior written
 *    permission of Chris Seguin.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE CHRIS SEGUIN OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of JRefactory.  For more information on
 * JRefactory, please see
 * <http://www.sourceforge.org/projects/jrefactory>.
 */
package org.acm.seguin.tools.stub;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.io.OutputStreamWriter;
import org.acm.seguin.parser.ast.SimpleNode;
import org.acm.seguin.parser.factory.FileParserFactory;
import org.acm.seguin.parser.factory.InputStreamParserFactory;
import org.acm.seguin.parser.factory.ParserFactory;
import org.acm.seguin.pretty.PrintData;
import org.acm.seguin.util.FileSettings;

/**
 *  Holds a refactoring. Default version just pretty prints the file.
 *
 *@author     Chris Seguin
 *@author     <a href="JRefactory@ladyshot.demon.co.uk">Mike Atkinson</a>
 *@version    $Id: StubFile.java,v 1.5 2003/09/01 00:25:32 mikeatkinson Exp $ 
 *@created    May 12, 1999
 */
public class StubFile {
    //  Instance Variables
    private ParserFactory factory;
    private Writer out;
    private String name;
    private File outputFile;

    private static boolean creating = false;


    /**
     *  Refactors java code.
     *
     *@param  init  Description of Parameter
     *@param  file  Description of Parameter
     */
    public StubFile(String init, File file) {
        factory = null;
        out = null;
        name = init;
        outputFile = file;

        StubFile.creating = true;
    }


    /**
     *  Set the parser factory
     *
     *@param  factory  Description of Parameter
     */
    public void setParserFactory(ParserFactory factory) {
        this.factory = factory;
    }


    /**
     *  Return the factory that gets the abstract syntax trees
     *
     *@return    the parser factory
     */
    public ParserFactory getParserFactory() {
        return factory;
    }


    /**
     *  Create the stub for this file
     *
     *@param  inputFile  the input file
     */
    public void apply(File inputFile) {
        setParserFactory(new FileParserFactory(inputFile));
        apply();
    }


    /**
     *  Create the stub for this file
     *
     *@param  inputStream  the input stream
     *@param  filename     the name of the file contained by the input stream
     */
    public void apply(Reader inputStream, String filename) {
        setParserFactory(new InputStreamParserFactory(inputStream, filename));
        apply();
    }


    /**
     *  Close the file and note that we have completed this operation
     */
    public void done() {
        if (out != null) {
            try {
                out.close();
            } catch (IOException ioe) {
            }
        }

        StubFile.creating = false;
        StubFile.resume();
    }


    /**
     *  Create the output stream
     *
     *@param  file  the name of the file
     *@return       the output stream
     */
    protected Writer getOutputStream(File file) {
        if (out != null) {
            return out;
        }

        if (name != null) {
            File directory = FileSettings.getRefactorySettingsRoot();
            if (!directory.exists()) {
                directory.mkdirs();
            }

            try {
                File outFile = new File(directory, name + ".stub");
                out = new FileWriter(outFile.getPath(), true);
            } catch (IOException ioe) {
                out = new OutputStreamWriter(System.out);
            }
        } else {
            try {
                out = new FileWriter(outputFile.getPath(), true);
            } catch (IOException ioe) {
                out = new OutputStreamWriter(System.out);
            }
        }

        return out; //  Return the output stream
    }


    /**
     *  Return the appropriate print data
     *
     *@param  input  Description of Parameter
     *@return        the print data
     */
    private PrintData getPrintData(File input) {
        //  Create the new stream
        return new PrintData(getOutputStream(input));
    }


    /**
     *  Create the stub for this file
     */
    private void apply() {
        //  Create the visitor
        StubPrintVisitor printer = new StubPrintVisitor();

        //  Create the appropriate print data
        PrintData data = getPrintData(null);
        SimpleNode root = factory.getAbstractSyntaxTree(false);

        if (root != null) {
            printer.visit(root, data);
        }

        //  Flush the output stream
        data.flush();
        try {
            out.write("\n\n|\n");
        } catch (IOException ioe) {
            ioe.printStackTrace(System.out);
        }
    }


    /**
     *  Wait while this is being created
     */
    public static synchronized void waitForCreation() {
        if (creating) {
            try {
                StubFile.class.wait();
            } catch (InterruptedException ie) {
            }
        }
    }


    /**
     *  Resume all processing
     */
    private static synchronized void resume() {
        StubFile.class.notifyAll();
    }
}
