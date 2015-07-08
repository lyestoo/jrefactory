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

import java.io.*;
import java.util.Iterator;
import org.acm.seguin.tools.stub.StubFile;
import org.acm.seguin.tools.stub.StubGenerator;
import org.acm.seguin.summary.load.LoadStatus;
import org.acm.seguin.summary.load.TextLoadStatus;
import org.acm.seguin.util.FileSettings;
import org.acm.seguin.util.MissingSettingsException;
import org.acm.seguin.awt.ExceptionPrinter;

/**
 *  Loads the file summaries for the framework files
 *
 *@author     Chris Seguin
 *@created    August 23, 1999
 */
public class FrameworkFileSummaryLoader extends FrameworkLoader {
    private File directory;
    private boolean loaded;
    private LoadStatus status;


    /**
     *  Constructor for the FrameworkFileSummaryLoader object
     *
     *@param  init  Description of Parameter
     */
    public FrameworkFileSummaryLoader(LoadStatus init) {
        status = init;
        try {
            FileSettings umlBundle = FileSettings.getRefactorySettings("uml");
            directory = new File(umlBundle.getString("stub.dir") + File.separator + ".Refactory");
        } catch (MissingSettingsException mse) {
            directory = FileSettings.getRefactorySettingsRoot();
        }
        loaded = false;
    }


    /**
     *  Main processing method for the FrameworkFileSummaryLoader object
     */
    public void run() {
        if (loaded) {
            return;
        }

        StubGenerator.waitForLoaded();

        PackageSummary.getPackageSummary("java.lang");

        StubFile.waitForCreation();

        loaded = true;
        String[] filenames = directory.list();
        if (filenames == null) {
            return;
        }

        for (int ndx = 0; ndx < filenames.length; ndx++) {
            if (filenames[ndx].endsWith(".stub")) {
                loadFile(filenames[ndx]);
            }
        }
    }


    /**
     *  Gets the InputReader attribute of the FrameworkFileSummaryLoader object
     *
     *@param  filename         Description of Parameter
     *@return                  The InputReader value
     *@exception  IOException  Description of Exception
     */
    private Reader getInputReader(String filename) throws IOException {
        return new BufferedReader(new FileReader(new File(directory, filename)));
    }


    /**
     *  Gets the TypeName attribute of the FrameworkFileSummaryLoader object
     *
     *@param  summary  Description of Parameter
     *@return          The TypeName value
     */
    private String getTypeName(FileSummary summary) {
        if (summary == null) {
            return "No summary";
        }

        Iterator iter = summary.getTypes();
        if (iter == null) {
            return "No types";
        }

        TypeSummary first = (TypeSummary) iter.next();
        String name = first.getName();
        if (name == null) {
            return "No name";
        }

        return name;
    }


    /**
     *  Loads a stub file
     *
     *@param  filename  The name of the file to load
     */
    private void loadFile(String filename) {
        try {
            status.setRoot(filename);
            Reader input = getInputReader(filename);
            String buffer = loadBuffer(input);

            while (buffer.length() > 0) {
                FileSummary summary = FileSummary.getFileSummary(buffer);
                status.setCurrentFile(getTypeName(summary));
                buffer = loadBuffer(input);

                Thread.currentThread().yield();
            }
        } catch (IOException ioe) {
            ExceptionPrinter.print(ioe, false);
        }
    }


    /**
     *  Description of the Method
     *
     *@param  input            Description of Parameter
     *@return                  Description of the Returned Value
     *@exception  IOException  Description of Exception
     */
    private String loadBuffer(Reader input) throws IOException {
        StringBuffer buffer = new StringBuffer();
        int next = input.read();
        while ((next >= 0) && (next != '|') && input.ready()) {
            buffer.append((char) next);
            next = input.read();
        }

        return buffer.toString().trim();
    }


    /**
     *  The main program for the FrameworkFileSummaryLoader class
     *
     *@param  args  The command line arguments
     */
    public static void main(String[] args) {
        (new FrameworkFileSummaryLoader(new TextLoadStatus())).run();
    }
}
