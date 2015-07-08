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
package org.acm.seguin.ide.common;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *  Loads the class diagrams based on a single directory
 *
 *@author     Chris Seguin
 *@created    October 18, 2001
 */
public class MultipleDirClassDiagramReloader extends ClassDiagramReloader {
    private LinkedList list;
    private boolean necessary;


    /**
     *  Constructor for the MultipleDirClassDiagramReloader object
     */
    public MultipleDirClassDiagramReloader() {
        super();
        list = new LinkedList();
        necessary = false;
    }


    /**
     *  Sets the Necessary attribute of the MultipleDirClassDiagramReloader
     *  object
     *
     *@param  value  The new Necessary value
     */
    public void setNecessary(boolean value) {
        necessary = value;
    }


    /**
     *  Gets the Necessary attribute of the MultipleDirClassDiagramReloader
     *  object
     *
     *@return    The Necessary value
     */
    public boolean isNecessary() {
        return necessary;
    }


    /**
     *  Sets the directory to load the data from
     *
     *@param  value  the directory
     */
    public void addRootDirectory(String value) {
        if (!list.contains(value)) {
            list.add(value);
        }
    }


    /**
     *  Clears all directories in the list
     */
    public void clear() {
        list.clear();
    }


    /**
     *  Reload the summary information and update the diagrams
     */
    public void reload() {
        if (!necessary) {
            return;
        }

        //  Build a list of directories to load
        StringBuffer buffer = new StringBuffer();
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            String base = (String) iter.next();
            buffer.append(base);
            if (iter.hasNext()) {
                buffer.append(File.pathSeparator);
            }
        }

        //  Load those directories
        String baseDirs = buffer.toString();
        if (baseDirs.length() > 0) {
            (new SummaryLoaderThread(baseDirs)).start();
        }

        //  Load the diagrams
        reloadDiagrams();
    }
}
//  EOF
