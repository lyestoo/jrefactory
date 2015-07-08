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
import org.acm.seguin.tools.RefactoryInstaller;

/**
 *  Generates a stub set from a file or a directory
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public class StubGenerator implements Runnable {
    private String filename;
    private String key;
    private File file;


    /**
     *  Constructor for the StubGenerator object
     *
     *@param  name     The name of the zip file
     *@param  stubKey  The key associated with this stub
     */
    public StubGenerator(String name, String stubKey) {
        filename = name;
        key = stubKey;
        file = null;
    }


    /**
     *  Constructor for the StubGenerator object
     *
     *@param  name    The name of the zip file
     *@param  output  Description of Parameter
     */
    public StubGenerator(String name, File output) {
        filename = name;
        key = null;
        file = output;
    }


    /**
     *  Main processing method for the StubGenerator object
     */
    public void run() {
        synchronized (StubGenerator.class) {
            File sourceFile = new File(filename);
            if (sourceFile.isDirectory()) {
                StubGenTraversal sgt = new StubGenTraversal(filename, key, file);
                new Thread(sgt).start();
                //sgt.run();
            } else {
                StubGenFromZip sgt = new StubGenFromZip(filename, key, file);
                new Thread(sgt).start();
                //sgt.run();
            }
        }
    }


    /**
     *  Waits until it is appropriate to allow the stub files to be loaded
     */
    public static synchronized void waitForLoaded() { }


    /**
     *  The main program
     *
     *@param  args  the command line arguments
     */
    public static void main(String[] args) {
        //  Make sure everything is installed properly
        (new RefactoryInstaller(false)).run();

        if (args.length != 2) {
            System.out.println("Syntax:  java org.acm.seguin.tools.stub.StubGenerator <name> <file>   ");
            System.out.println("   OR    java org.acm.seguin.tools.stub.StubGenerator <name> <dir>   ");
            System.out.println("   where <name> is the name of the stub file to generate");
            System.out.println("   where <file> is the jar or zip file");
            System.out.println("   where <dir> is the directory for one or more source files source file");
            return;
        }
        generateStubs(args[0], args[1]);

        //  Exit
        System.exit(0);
    }


    /**
     *  Generate a stub for the current file or directory
     *
     *@param  filename  the name of the directory
     *@param  stubname  the name of the stub
     */
    public static void generateStubs(String stubname, String filename) {
        (new StubGenerator(filename, stubname)).run();
    }
}
