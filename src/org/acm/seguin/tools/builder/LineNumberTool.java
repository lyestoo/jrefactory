/*
 *  ====================================================================
 *  The JRefactory License, Version 1.0
 *
 *  Copyright (c) 2001 JRefactory.  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  JRefactory (http://www.sourceforge.org/projects/jrefactory)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "JRefactory" must not be used to endorse or promote
 *  products derived from this software without prior written
 *  permission. For written permission, please contact seguin@acm.org.
 *
 *  5. Products derived from this software may not be called "JRefactory",
 *  nor may "JRefactory" appear in their name, without prior written
 *  permission of Chris Seguin.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE CHRIS SEGUIN OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of JRefactory.  For more information on
 *  JRefactory, please see
 *  <http://www.sourceforge.org/projects/jrefactory>.
 */
package org.acm.seguin.tools.builder;


import java.io.*;
import java.util.ArrayList;
import org.acm.seguin.parser.ParseException;
import org.acm.seguin.parser.ast.SimpleNode;
import org.acm.seguin.parser.factory.FileParserFactory;
import org.acm.seguin.parser.factory.ParserFactory;
import org.acm.seguin.parser.factory.StdInParserFactory;
import org.acm.seguin.pretty.PrettyPrintVisitor;
import org.acm.seguin.pretty.PrintData;
import org.acm.seguin.pretty.line.LineNumberingData;
import org.acm.seguin.util.FileSettings;
import org.acm.seguin.tools.RefactoryInstaller;


/**
 *  Tool that uses the pretty printer to number lines
 *
 *@author     Chris Seguin
 *@created    May 11, 1999
 */
public class LineNumberTool
{
    private String dest;
    //  Instance Variables
    private ArrayList inputList;
    private Writer out;


    /**
     *  Constructor for the line numbering
     */
    public LineNumberTool()
    {
        inputList = new ArrayList();
        dest = null;
        out = null;
    }


    /**
     *  Read command line inputs
     *
     *@param  args  Description of Parameter
     */
    protected void init(String[] args)
    {
        int last = args.length;
        for (int ndx = 0; ndx < last; ndx++) {
            if (args[ndx].equals("-help")) {
                System.out.println("Pretty Printer Version 1.0.  Has the following inputs");
                System.out.println("         java LineNumberTool [-out filename] (inputfile)*");
                System.out.println("OR");
                System.out.println("         java LineNumberTool [-out filename] < inputfile");
                System.out.println("where");
                System.out.println("         -out filename     Output to the file or directory");
            }
            else if (args[ndx].equals("-out")) {
                ndx++;
                dest = args[ndx];
            }
            else {
                //  Add another input to the list
                inputList.add(args[ndx]);
            }
        }
    }


    /**
     *  Run the pretty printer
     */
    protected void run()
    {
        //  Local Variables
        int last = inputList.size();

        //  Create the visitor
        PrettyPrintVisitor printer = new PrettyPrintVisitor();

        //  Create the appropriate print data
        PrintData data = null;

        for (int index = 0; (index < last) || (index == 0); index++) {
            data = getPrintData(index, data);

            //  Create the parser and visit the parse tree
            printer.visit(getRoot(index), data);

            //  Flush the output stream
            data.flush();
        }
        data.close();
    }


    /**
     *  Create the output stream
     *
     *@param  index     the index of the output stream
     *@param  filename  the name of the file
     *@return           the output stream
     */
    private Writer getOutputStream(int index, String filename)
    {
        //  Local Variables
        Writer out = null;

        //  Check the destination
        if (dest == null) {
            out = new OutputStreamWriter(System.out);
        }
        else {
            try {
                out = new FileWriter(dest);
            }
            catch (IOException ioe) {
                //  Hmmm...  Can't create the output stream, then fall back to stdout
                out = new OutputStreamWriter(System.out);
            }
        }

        //  Return the output stream
        return out;
    }


    /**
     *  Return the appropriate print data
     *
     *@param  oldPrintData  the old print data
     *@param  index         Description of Parameter
     *@return               the print data
     */
    private PrintData getPrintData(int index, PrintData oldPrintData)
    {
        if (oldPrintData == null) {
            out = getOutputStream(index, null);
            return new LineNumberingData(out);
        }
        else {
            oldPrintData.flush();
            try {
                out.write(12);
            }
            catch (IOException ioe) {
            }
            return oldPrintData;
        }
    }


    /**
     *  Create the parser
     *
     *@param  index  the index
     *@return        the java parser
     */
    private SimpleNode getRoot(int index)
    {
        File in;
        ParserFactory factory;

        if (inputList.size() > index) {
            in = new File((String) inputList.get(index));
            factory = new FileParserFactory(in);
        }
        else {
            factory = new StdInParserFactory();
        }

        //  Create the parse tree
        return factory.getAbstractSyntaxTree(true);
    }


    /**
     *  Main program
     *
     *@param  args  the command line arguments
     */
    public static void main(String args[])
    {
        for (int ndx = 0; ndx < args.length; ndx++) {
            if (args[ndx].equals("-config")) {
                String dir = args[ndx + 1];
                ndx++;
                FileSettings.setSettingsRoot(dir);
            }
        }

        //  Make sure everything is installed properly
        (new RefactoryInstaller(false)).run();

        try {
            LineNumberTool pp = new LineNumberTool();
            pp.init(args);
            pp.run();
        }
        catch (Throwable t) {
            if (t == null) {
                System.out.println("We have caught a null throwable");
            }
            else {
                System.out.println("t is a " + t.getClass().getName());
                System.out.println("t has a message " + t.getMessage());
                t.printStackTrace(System.out);
            }
        }
    }
}
//  EOF
