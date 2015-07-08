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
package org.acm.seguin.tools.international;

import java.io.File;
import org.acm.seguin.parser.factory.FileParserFactory;
import org.acm.seguin.parser.ast.SimpleNode;
import org.acm.seguin.io.DirectoryTreeTraversal;

/**
 *  Lists the strings in a set of files
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
class StringListTraversal extends DirectoryTreeTraversal {
    /**
     *  Constructor for the StringListTraversal object
     *
     *@param  init  Description of Parameter
     */
    public StringListTraversal(String init) {
        super(init);
    }


    /**
     *  Gets the Target attribute of the StringListTraversal object
     *
     *@param  currentFile  Description of Parameter
     *@return              The Target value
     */
    protected boolean isTarget(File currentFile) {
        String name = currentFile.getName();
        String lower = name.toLowerCase();
        return lower.endsWith(".java");
    }


    /**
     *  Description of the Method
     *
     *@param  currentFile  Description of Parameter
     */
    protected void visit(File currentFile) {
        System.out.println("File:  " + currentFile.getPath());
        try {
            FileParserFactory fpf = new FileParserFactory(currentFile);
            SimpleNode root = fpf.getAbstractSyntaxTree(false);
            if (root != null) {
                root.jjtAccept(new StringListVisitor(), null);
            }
        } catch (Throwable thrown) {
            thrown.printStackTrace();
        }
        System.out.println(" ");
    }


    /**
     *  The main program for the StringListTraversal class
     *
     *@param  args  The command line arguments
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            (new StringListTraversal(System.getProperty("user.dir"))).run();
        } else {
            (new StringListTraversal(args[0])).run();
        }
    }
}
