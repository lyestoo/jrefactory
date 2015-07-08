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
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.acm.seguin.io.AllFileFilter;
import org.acm.seguin.io.DirectoryTreeTraversal;
import org.acm.seguin.io.ExtensionFileFilter;
import org.acm.seguin.summary.load.LoadStatus;
import org.acm.seguin.summary.load.SwingLoadStatus;

/**
 *  Traverses a directory structure and performs all refactorings on the files.
 *
 *@author     Chris Seguin
 *@author     Mike Atkinson
 *@created    May 12, 1999
 */
class StubGenTraversal extends DirectoryTreeTraversal implements Runnable {
    //  Instance Variables
    private StubFile sf;
    private SwingLoadStatus status;


    /**
     *  Creates a stub generator
     *
     *@param  directory  the initial directory or file
     *@param  key        the key associated with this stub
     *@param  file       Description of Parameter
     */
    public StubGenTraversal(String directory, String key, File file) {
        super(directory);

        sf = new StubFile(key, file);
        status = new SwingLoadStatus();
        status.setLength(directory, 1000);
        status.setRoot(directory);
        status.setVisible(true);
    }


    /**
     *  Main processing method for the StubGenTraversal object
     */
    public void run() {
        run();
        sf.done();
        status.done();
    }


    /**
     *  Determines if this file should be handled by this traversal
     *
     *@param  currentFile  the current file
     *@return              true if the file should be handled
     */
    protected boolean isTarget(File currentFile) {
        return (currentFile.getName().endsWith(".java"));
    }


    /**
     *  Visits the current file
     *
     *@param  currentFile  the current file
     */
    protected void visit(File currentFile) {
        System.out.println("Generating a stub for:  " + currentFile.getPath());
        status.setCurrentFile(currentFile.getPath());
        sf.apply(currentFile);
    }
}
