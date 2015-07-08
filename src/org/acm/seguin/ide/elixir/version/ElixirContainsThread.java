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

import java.io.File;
import java.io.IOException;
import javax.swing.JMenuItem;
import com.elixirtech.tree.TNode;
import org.acm.seguin.util.FileSettings;
import org.acm.seguin.util.MissingSettingsException;
import org.acm.seguin.version.VersionControl;
import org.acm.seguin.version.VersionControlCache;

/**
 *  Interact with version control
 *
 *@author     Chris Seguin
 *@created    July 12, 1999
 */
public class ElixirContainsThread extends Thread {
    //  Instance Variables
    private VersionControl delegate;
    private ElixirVersionControl evc;
    private JMenuItem menu;
    private TNode parent;


    /**
     *  Constructor
     *
     *@param  initMenuItem  The menu item
     *@param  initParent    The initial parent
     *@param  initDelegate  The delegate
     *@param  initEVC       Description of Parameter
     */
    public ElixirContainsThread(JMenuItem initMenuItem, TNode initParent,
            VersionControl initDelegate, ElixirVersionControl initEVC) {
        menu = initMenuItem;
        parent = initParent;
        delegate = initDelegate;
        evc = initEVC;
    }


    /**
     *  Determines if the file is under souce control
     *
     *@param  name  the name of the file
     *@return       true if it is under source control
     */
    private boolean isUnderSourceControl(String name) {
        if (name == null) {
            return false;
        }

        try {
            FileSettings bundle = FileSettings.getRefactorySettings("vss");

            int index = 1;
            while (true) {
                String next = bundle.getString("extension." + index);
                System.out.println("\t\tComparing:  [" + name + "] to [" + next + "]");
                if (name.endsWith(next)) {
                    System.out.println("\t\tFound it");
                    return true;
                }
                index++;
            }
        }
        catch (MissingSettingsException mse) {
            //  Finished
        }

        return false;
    }


    /**
     *  Description of the Method
     */
    private void add() {
        menu.setText("Add");
        menu.setEnabled(false);
        menu.addActionListener(new AddListener(evc, parent.getFullName(), parent.getName()));
    }


    /**
     *  Description of the Method
     */
    private void checkIn() {
        VersionControlCache cache = VersionControlCache.getCache();
        String filename = parent.getFullName();
        menu.setText("Check In");
        menu.setEnabled(cache.lookup(filename) == VersionControlCache.CHECK_IN);
        menu.addActionListener(new CheckInListener(evc, filename, parent.getName()));
    }


    /**
     *  Sets the menu up to say that it is being checked out
     */
    private void checkOut() {
        boolean enabled = true;
        String filename = parent.getFullName();
        VersionControlCache cache = VersionControlCache.getCache();

        if (cache.isInCache(filename)) {
            enabled = (cache.lookup(filename) == VersionControlCache.CHECK_OUT);
        }
        else {
            cache.add(filename, VersionControlCache.CHECK_OUT);
        }

        menu.setText("Check Out");
        menu.setEnabled(enabled);
        menu.addActionListener(new CheckOutListener(evc, filename, parent.getName()));
    }


    /**
     *  Is this file contained in visual source safe?
     *
     *@param  filename  The full path of the file in question
     *@return           Returns true if it is in source safe
     */
    public int contains(String filename) {
        //  Start with the cache
        VersionControlCache cache = VersionControlCache.getCache();
        if (cache.isInCache(filename)) {
            return cache.lookup(filename);
        }

        boolean way = delegate.contains(filename);
        int result;

        if (way) {
            result = VersionControlCache.CHECK_IN;
        }
        else {
            result = VersionControlCache.ADD;
        }

        cache.add(filename, result);
        return result;
    }


    /**
     *  Actually do the work
     */
    public void run() {
        String name = parent.getName();

        if (!isUnderSourceControl(name)) {
            menu.setText("Not under source control");
            menu.setEnabled(false);
            return;
        }

        System.out.println("Full Name:  " + parent.getFullName());
        File file = new File(parent.getFullName());
        if (!file.canWrite()) {
            checkOut();
        }
        else if (contains(parent.getFullName()) == VersionControlCache.ADD) {
            add();
        }
        else {
            checkIn();
        }

        menu.repaint();
    }
}
//  EOF
