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
package org.acm.seguin.util;

import org.acm.seguin.tools.install.RefactoryInstaller;
import java.io.*;
import org.acm.seguin.io.DirectoryTreeTraversal;
import org.acm.seguin.io.FileCopy;

/**
 *  Traverses a directory structure and backups all java files found
 *
 *@author     Chris Seguin
 *@created    October 31, 1999
 *@date       May 12, 1999
 */
public class BackupTraversal extends DirectoryTreeTraversal {
    //  Instance Variables
    private String dest;


    /**
     *  Traverses a directory tree structure
     *
     *@param  init  the initial directory
     *@param  out   the output directory
     */
    public BackupTraversal(String init, String out) {
        super(init);
        if (out.charAt(out.length() - 1) != File.separatorChar) {
            dest = out + File.separator;
        } else {
            dest = out;
        }
    }


    /**
     *  Determines if this file should be handled by this traversal
     *
     *@param  currentFile  the current file
     *@return              true if the file should be handled
     */
    protected boolean isTarget(File currentFile) {
        String filename = currentFile.getName();
        String lowercase = filename.toLowerCase();
        if (!lowercase.endsWith(".java")) {
            return false;
        }

        String classname = lowercase.substring(0, lowercase.length() - 5) + ".class";
        File classFile = new File(currentFile.getParentFile(), classname);
        return classFile.exists();
    }


    /**
     *  Visits the current file
     *
     *@param  currentFile  the current file
     */
    protected void visit(File currentFile) {
        String destString = getDestination(currentFile);
        File destFile = new File(destString);
        (new FileCopy(currentFile, destFile)).run();
    }


    /**
     *  Program called when we arrive at a directory
     *
     *@param  current  the current directory
     */
    protected void arriveAtDir(File current) {
        String currentPath = current.getPath();
        String base = "";
        if (currentPath.startsWith("./") || currentPath.startsWith(".\\")) {
            base = currentPath.substring(2);
        } else {
            base = currentPath;
        }
        createDir(dest + "src/" + base);
        createDir(dest + "test/src/" + base);
    }


    /**
     *  Returns the destination file from the current file
     *
     *@param  current  the current file
     *@return          the destination file
     */
    private String getDestination(File current) {
        String prefix = "src/";
        if (current.getName().startsWith("Test")) {
            prefix = "test/src/";
        }

        String currentPath = current.getPath();
        if (currentPath.startsWith("./") || currentPath.startsWith(".\\")) {
            return dest + prefix + currentPath.substring(2);
        } else {
            return dest + prefix + currentPath;
        }
    }


    /**
     *  The main program
     *
     *@param  args  Description of Parameter
     */
    public static void main(String[] args) {
        //  Make sure everything is installed properly
        (new RefactoryInstaller(false)).run();

        if (args.length != 2) {
            System.out.println("Syntax:  java BackupTraversal source dest");
            return;
        }
        (new BackupTraversal(args[0], args[1])).run();
    }


    /**
     *  Creates a named directory if it does not exist
     *
     *@param  destDir  Description of the Parameter
     */
    private void createDir(String destDir) {
        File destDirFile = new File(destDir);
        if (destDirFile.exists()) {
            //  Nothing to do
        } else {
            destDirFile.mkdirs();
        }
    }


    /**
     *  Creates a named directory if it does not exist
     *
     *@param  destDir  Description of the Parameter
     */
    private void deleteDir(String destDir) {
        File destDirFile = new File(destDir);
        String[] children = destDirFile.list();
        if (children.length == 0) {
            destDirFile.delete();
        }
    }


    /**
     *  Program called when we arrive at a directory
     *
     *@param  current      Description of the Parameter
     */
    protected void leaveDir(File current) {
        String currentPath = current.getPath();
        String base = "";
        if (currentPath.startsWith("./") || currentPath.startsWith(".\\")) {
            base = currentPath.substring(2);
        } else {
            base = currentPath;
        }
        deleteDir(dest + "src/" + base);
        deleteDir(dest + "test/src/" + base);
    }
}
