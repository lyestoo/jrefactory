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
package org.acm.seguin.ide.elixir.version;
import org.acm.seguin.tools.install.RefactoryInstaller;

import java.io.File;
import java.io.IOException;
import javax.swing.JMenuItem;
import com.elixirtech.ide.version.IVersionControl;
import com.elixirtech.tree.TNode;
import com.elixirtech.fx.*;
import org.acm.seguin.version.SourceSafe;
import org.acm.seguin.version.VersionControl;
import org.acm.seguin.version.VersionControlCache;

/**
 *  Interact with version control
 *
 *@author     Chris Seguin
 *@created    June 29, 1999
 */
public class ElixirVersionControl implements IVersionControl {
    //  Instance Variables
    private VersionControl delegate = null;


    /**
     *  Creates a menu item
     *
     *@param  parent  Node that describes the file
     *@return         The menu item
     */
    public JMenuItem getMenu(TNode parent) {
        String name = parent.getName();

        JMenuItem jmi = new JMenuItem("Querying source control...");
        jmi.setEnabled(false);

        if (delegate == null) {
            init();
        }
        ElixirContainsThread ect = new ElixirContainsThread(jmi, parent, delegate, this);
        ect.start();

        return jmi;
    }


    /**
     *  Adds a file to visual source safe
     *
     *@param  filename  The full path to the file
     */
    public void add(String filename) {
        System.out.println("Add:  " + filename);
        VersionControlCache cache = VersionControlCache.getCache();
        cache.add(filename, VersionControlCache.ADD_PROGRESS);
        if (delegate == null) {
            init();
        }

        Thread evct = new ElixirVersionControlThread(delegate, filename, ElixirVersionControlThread.ADD);
        evct.start();
    }


    /**
     *  Adds an array of files
     *
     *@param  filenames  The array of files to add
     */
    public void add(String[] filenames) {
        System.out.println("Multiple Add");
        for (int ndx = 0; ndx < filenames.length; ndx++) {
            add(filenames[ndx]);
        }
    }


    /**
     *  Checks in a file to visual source safe
     *
     *@param  filename  The full pathname of the file
     */
    public void checkIn(String filename) {
        System.out.println("Check In:  " + filename);
        if (delegate == null) {
            init();
        }

        Thread evct = new ElixirVersionControlThread(delegate, filename, ElixirVersionControlThread.CHECK_IN);
        evct.start();
    }


    /**
     *  Checks in multiple files
     *
     *@param  filenames  Multiple files to check in
     */
    public void checkIn(String[] filenames) {
        System.out.println("Multiple Check In");
        for (int ndx = 0; ndx < filenames.length; ndx++) {
            checkIn(filenames[ndx]);
        }
    }


    /**
     *  Checks out a file from visual source safe
     *
     *@param  filename  The full path name of the file
     */
    public void checkOut(String filename) {
        System.out.println("Check Out:  " + filename);
        if (delegate == null) {
            init();
        }

        Thread evct = new ElixirVersionControlThread(delegate, filename, ElixirVersionControlThread.CHECK_OUT);
        evct.start();
    }


    /**
     *  Is this file contained in visual source safe?
     *
     *@param  filename  The full path of the file in question
     *@return           Returns true if it is in source safe
     */
    public boolean contains(String filename) {
        VersionControlCache cache = VersionControlCache.getCache();
        return cache.lookup(filename) != VersionControlCache.ADD;
    }


    /**
     *  Constructor for ElixirVersionControl object
     */
    private synchronized void init() {
        if (delegate == null) {
            //  Make sure everything is installed properly
            (new RefactoryInstaller(false)).run();

            delegate = new SourceSafe();
        }
    }
}
//  EOF
