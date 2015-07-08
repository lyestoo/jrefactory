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
package org.acm.seguin.summary.load;

import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.File;
import org.acm.seguin.summary.PackageSummary;
import org.acm.seguin.summary.SummaryTraversal;
import org.acm.seguin.util.FileSettings;

/**
 *  This code is responsible for speeding the loading and saving of the meta
 *  data about the different classes.
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public class RapidLoader {
    /**
     *  This will save the classes
     */
    public void save() {
        (new SaveThread()).start();
    }


    /**
     *  This will load the classes
     */
    public void load() {
        try {
            System.out.println("RapidLoader.load()");
            FileInputStream fileInput = new FileInputStream(new File(FileSettings.getRefactorySettingsRoot(), "data.sof"));
            BufferedInputStream bufferInput = new BufferedInputStream(fileInput);
            ObjectInputStream in = new ObjectInputStream(bufferInput);
            PackageSummary.loadAll(in);
            in.close();

            SummaryTraversal.setFrameworkLoader(new FlashLoader());
        } catch (IOException ioe) {
            ioe.printStackTrace(System.out);
        }
    }


    /**
     *  Separate thread to save the data to the serialized object file
     *
     *@author     Chris Seguin
     *@created    September 12, 2001
     */
    public class SaveThread extends Thread {
        /**
         *  Main processing method for the SaveThread object
         */
        public void run() {
            try {
                FileOutputStream fileOutput = new FileOutputStream(new File(FileSettings.getRefactorySettingsRoot(), "data.sof"));
                BufferedOutputStream bufferOutput = new BufferedOutputStream(fileOutput);
                ObjectOutputStream out = new ObjectOutputStream(bufferOutput);
                PackageSummary.saveAll(out);
                out.close();
            } catch (IOException ioe) {
                ioe.printStackTrace(System.out);
            }
        }
    }
}
