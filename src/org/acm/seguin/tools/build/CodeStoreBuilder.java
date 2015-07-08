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
public class CodeStoreBuilder extends DirectoryTreeTraversal {
    private LinkedList directoryList;
    private PrintWriter output;
    private int rootLength;
    private boolean first;
    private String jarFile;
    private int directoryCount;

    private final static int MAX_DIRS = 3;


    /**
     *  Constructor for the CodeStoreBuilder object
     *
     *@param  jarFile  Description of Parameter
     *@param  rootDir  Description of Parameter
     *@param  out      Description of Parameter
     */
    public CodeStoreBuilder(String jarFile, String rootDir, PrintWriter out) {
        super(rootDir);
        directoryList = new LinkedList();
        output = out;
        first = true;
        rootLength = rootDir.length();
        this.jarFile = jarFile;
        directoryCount = 0;
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
        //System.out.println("Parent:  " + parentString);
        if (!directoryList.contains(parentString)) {
            directoryList.add(parentString);

            if (directoryCount % MAX_DIRS == 0) {
                if (first) {
                    output.print("jar cf " + jarFile);
                    first = false;
                } else {
                    output.print("jar uf " + jarFile);
                }
            }
            directoryCount++;

            if (parentString.length() == rootLength) {
                output.print(" *.java *.html");
            } else {
                output.print(" " + parentString.substring(rootLength + 1) +
                        File.separator + "*.java" +
                        " " + parentString.substring(rootLength + 1) +
                        File.separator + "*.html");
            }

            if (directoryCount % MAX_DIRS == 0) {
                output.println("");
            }
        }
    }


    /**
     *  The main program for the CodeStoreBuilder class
     *
     *@param  args  The command line arguments
     */
    public static void main(String[] args) {
        String jarFile = "code.jar";
        String directory = System.getProperty("user.dir");
        PrintWriter out = null;

        if (args.length > 0) {
            jarFile = args[0];
        }

        try {
            if (args.length > 1) {
                out = new PrintWriter(new FileWriter(args[1]));
            } else {
                out = new PrintWriter(new OutputStreamWriter(System.out));
            }
        } catch (IOException ioe) {
            System.out.println("Unable to create the output file:  " + args[0]);
            return;
        }

        if (args.length > 2) {
            directory = args[2];
        }

        (new CodeStoreBuilder(jarFile, directory, out)).run();

        out.println("");

        out.flush();
        out.close();
    }
}
