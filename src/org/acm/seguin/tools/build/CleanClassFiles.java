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
package org.acm.seguin.tools.build;

import java.io.*;
import java.util.LinkedList;
import org.acm.seguin.io.DirectoryTreeTraversal;

/**
 *  Description of the Class
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public class CleanClassFiles extends DirectoryTreeTraversal {
    private LinkedList directoryList;
    private PrintWriter output;


    /**
     *  Constructor for the CleanClassFiles object
     *
     *@param  rootDir  Description of Parameter
     *@param  out      Description of Parameter
     */
    public CleanClassFiles(String rootDir, PrintWriter out) {
        super(rootDir);
        directoryList = new LinkedList();
        output = out;
    }


    /**
     *  Determines if this file should be handled by this traversal
     *
     *@param  currentFile  the current file
     *@return              true if the file should be handled
     */
    protected boolean isTarget(File currentFile) {
        return currentFile.getName().endsWith(".java");
    }


    /**
     *  Visits the current file
     *
     *@param  currentFile  the current file
     */
    protected void visit(File currentFile) {
        String parentString = currentFile.getParent();
        if (!directoryList.contains(parentString)) {
            directoryList.add(parentString);
            output.println("del " + parentString +
                    File.separator + "*.class");
        }
    }


    /**
     *  The main program for the CleanClassFiles class
     *
     *@param  args  The command line arguments
     */
    public static void main(String[] args) {
        String directory = System.getProperty("user.dir");
        PrintWriter out = null;

        try {
            if (args.length > 0) {
                out = new PrintWriter(new FileWriter(args[0]));
            } else {
                out = new PrintWriter(new OutputStreamWriter(System.out));
            }
        } catch (IOException ioe) {
            System.out.println("Unable to create the output file:  " + args[0]);
            return;
        }

        if (args.length > 1) {
            directory = args[1];
        }

        (new CleanClassFiles(directory, out)).run();

        out.flush();
        out.close();
    }
}

