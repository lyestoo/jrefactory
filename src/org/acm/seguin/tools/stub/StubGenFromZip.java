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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.acm.seguin.summary.load.SwingLoadStatus;

/**
 *  Generates a stub set from a file
 *
 *@author     Chris Seguin
 *@author     <a href="JRefactory@ladyshot.demon.co.uk">Mike Atkinson</a>
 *@version    $Id: StubGenFromZip.java,v 1.4 2003/07/29 20:51:57 mikeatkinson Exp $ 
 *@created    September 12, 2001
 */
class StubGenFromZip implements Runnable {
    private String filename;
    private StubFile sf;
    private SwingLoadStatus status;


    /**
     *  Constructor for the StubGenFromZip object
     *
     *@param  name     The name of the zip file
     *@param  stubKey  Description of Parameter
     *@param  file     Description of Parameter
     */
    public StubGenFromZip(String name, String stubKey, File file) {
        filename = name;
        sf = new StubFile(stubKey, file);
        status = new SwingLoadStatus();
        status.setRoot(name);
        status.setVisible(true);
    }


    /**
     *  Main processing method for the StubGenFromZip object
     */
    public void run() {
        try {
            ZipFile zipfile = new ZipFile(filename);
            status.setLength(filename, 	zipfile.size());

            Enumeration entryEnum = zipfile.entries();
            while (entryEnum.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entryEnum.nextElement();
                if (applies(entry)) {
                    InputStream input = zipfile.getInputStream(entry);
                    generateStub(new InputStreamReader(input), entry.getName());
                    input.close();
                }
            }

            sf.done();
            status.done();
        } catch (Throwable thrown) {
            thrown.printStackTrace(System.out);
        }
    }


    /**
     *  Does this algorithm apply to this entry
     *
     *@param  entry  the entry
     *@return        true if we should generate a stub from it
     */
    private boolean applies(ZipEntry entry) {
        return !entry.isDirectory() && entry.getName().endsWith(".java");
    }


    /**
     *  Generates a stub
     *
     *@param  input     the input stream
     *@param  filename  the filename
     */
    private void generateStub(Reader input, String filename) {
        System.out.println("Generating a stub for:  " + filename);
        status.setCurrentFile(filename);
        sf.apply(input, filename);
    }
}
