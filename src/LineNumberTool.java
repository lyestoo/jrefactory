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
import org.acm.seguin.tools.install.RefactoryInstaller;


/**
 *  Tool that uses the pretty printer to number lines
 *
 *@author     Chris Seguin
 *@created    May 11, 1999
 */
public class LineNumberTool extends org.acm.seguin.tools.builder.LineNumberTool
{

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
