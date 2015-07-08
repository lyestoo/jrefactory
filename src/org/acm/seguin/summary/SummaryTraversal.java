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
package org.acm.seguin.summary;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;
import org.acm.seguin.io.DirectoryTreeTraversal;
import org.acm.seguin.summary.load.LoadStatus;
import org.acm.seguin.summary.load.SwingLoadStatus;
import org.acm.seguin.util.FileSettings;
import org.acm.seguin.util.MissingSettingsException;

/**
 *  Summarizes a directory structure
 *
 *@author     Chris Seguin
 *@author     Mike Atkinson
 *@created    June 6, 1999
 */
public class SummaryTraversal extends DirectoryTreeTraversal {
    private String root;
    private String blockDirectories;
    private LoadStatus status;
    private static FrameworkLoader framework = null;     
    private boolean useFramework;

    /**
     *  Traverses a directory tree structure and generates a summary of the
     *  classes.
     *
     *@param  init  the initial directory
     */
    public SummaryTraversal(String init) {
        this(init, new SwingLoadStatus());
    }

     /**
     *  Traverses a directory tree structure and generates a summary of the
     *  classes.
     *
     *@param  init          the initial directory
     *@param  initStatus    Description of Parameter     
     */
    
    public SummaryTraversal(String init, LoadStatus initStatus) {
        this(init, initStatus, true);
    }

    /**
     *  Traverses a directory tree structure and generates a summary of the
     *  classes.
     *
     *@param  init          the initial directory
     *@param  initStatus    Description of Parameter
     *@param  useFramework  use the framework (i.e. load JDK stubs)
     */
    public SummaryTraversal(String init, LoadStatus initStatus, boolean useFramework) {
        super(init);       
        root = init;
        status = initStatus;
        this.useFramework = useFramework;
        if (useFramework) {
            if (framework == null) {
                framework = new FrameworkFileSummaryLoader(status);
            }
        }

        try {
            FileSettings umlBundle = FileSettings.getRefactorySettings("uml");
            umlBundle.setContinuallyReload(true);
            blockDirectories = umlBundle.getString("skip.dir");
            if (blockDirectories == null) {
                blockDirectories = "";
            } else {
                blockDirectories = blockDirectories.trim();
                if (blockDirectories == null) {
                    blockDirectories = "";
                }
            }
        } catch (MissingSettingsException mse) {
            blockDirectories = "";
        }
    }
   
    
    /**
     *  Method that starts the traversal to generate the summaries.
     */
    public void run() {
        //System.out.println("SummaryTraversal.run() thread="+Thread.currentThread());
        if (useFramework) {
            framework.run();
        }

        File temp = new File(root);
        String dir = null;
        try {
            dir = temp.getCanonicalPath();
        } catch (IOException ioe) {
            dir = temp.getPath();
        }

        status.setRoot(dir);
        FileSummary.removeDeletedSummaries();
        super.run();
        status.done();
    }


    /**
     *  Determines if this file should be handled by this traversal
     *
     *@param  currentFile  the current file
     *@return              true if the file should be handled
     */
    protected boolean isTarget(File currentFile) {
        String name = currentFile.getName();
        int dot = name.indexOf(".");
        int java = name.indexOf(".java");

        return (dot == java) && name.endsWith(".java");
    }


    /**
     *  Are we allowed to traverse this directory?
     *
     *@param  currentDirectory  the directory that we are about to enter
     *@return                   true if we are allowed to enter it
     */
    protected boolean isAllowed(File currentDirectory) {
        if ((blockDirectories == null) || (blockDirectories.length() == 0)) {
            return true;
        }

        StringTokenizer tok = new StringTokenizer(blockDirectories, File.pathSeparator);
        while (tok.hasMoreTokens()) {
            String next = tok.nextToken();
            if (currentDirectory.getName().indexOf(next) >= 0) {
                return false;
            }
        }

        return true;
    }


    /**
     *  Visits the current file
     *
     *@param  currentFile  the current file
     */
    protected void visit(File currentFile) {
        try {
            status.setCurrentFile(currentFile.getPath());
            FileSummary.getFileSummary(currentFile);

            Thread.currentThread().yield();
        } catch (Throwable oops) {
            try {
               System.out.println("\nError loading:  " + currentFile.getCanonicalPath());
            } catch (Exception e) {
               System.out.println("\nError loading:  " + currentFile.getName());
            }
            oops.printStackTrace(System.out);
        }
    }


    /**
     *  Sets the framework loader
     *
     *@param  value  The new FrameworkLoader value
     */
    public static void setFrameworkLoader(FrameworkLoader value) {
        framework = value;
    }


    /**
     *  Main program
     *
     *@param  args  the command line arguments
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            (new SummaryTraversal(System.getProperty("user.dir"))).run();
        } else {
            (new SummaryTraversal(args[0])).run();
        }

        debug();
        System.exit(0);
    }


    /**
     *  Print everything for debugging purposes
     */
    public static void debug() {
        //  Now print everything
        PrintVisitor printer = new PrintVisitor();
        Iterator iter = PackageSummary.getAllPackages();
        while (iter.hasNext()) {
            PackageSummary next = (PackageSummary) iter.next();
            next.accept(printer, "");
        }
    }
}
