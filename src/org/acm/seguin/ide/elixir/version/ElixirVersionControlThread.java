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
import com.elixirtech.ide.version.IVersionControl;
import com.elixirtech.tree.TNode;
import com.elixirtech.fx.*;
import org.acm.seguin.version.VersionControl;

/**
 *  Version control in separate thread so the window doesn't appear to lock up.
 *
 *@author     Chris Seguin
 *@created    July 12, 1999
 */
public class ElixirVersionControlThread extends Thread {
    /**
     *  Description of the Field
     */
    public static int ADD = 3;

    /**
     *  Description of the Field
     */
    public static int CHECK_IN = 1;
    /**
     *  Description of the Field
     */
    public static int CHECK_OUT = 2;
    //  Instance Variables
    private VersionControl delegate = null;
    private String filename;
    private int operation;


    /**
     *  Constructor for ElixirVersionControl object
     *
     *@param  initDelegate  Description of Parameter
     *@param  initFile      Description of Parameter
     *@param  initOp        Description of Parameter
     */
    public ElixirVersionControlThread(VersionControl initDelegate, String initFile, int initOp) {
        delegate = initDelegate;
        filename = initFile;
        operation = initOp;
    }


    /**
     *  Program that actually runs
     */
    public void run() {
        if (operation == CHECK_IN) {
            delegate.checkIn(filename);
        }
        else if (operation == CHECK_OUT) {
            delegate.checkOut(filename);
        }
        else if (operation == ADD) {
            delegate.add(filename);
        }

        //  Update elixir
        updateElixirFile(filename);
        updateElixirProject();
    }


    /**
     *  Update the current file
     *
     *@param  filename  Description of Parameter
     */
    private void updateElixirFile(String filename) {
        //  Get the view manager for this file
        IViewSite vs = FrameManager.current().getViewSite();
        ViewManager vm = vs.getViewManager(filename);

        //  If we got one, it is open
        if (vm != null) {
            vm.reload();

            /*
             *  The following code would bring that file to the front.
             *  I don't want to do that yet.
             */
            //vs.setCurrentViewManager(vm);
        }
    }


    /**
     *  Update Elixir's project window
     */
    private void updateElixirProject() {
        //  Repaint the project manager
        com.elixirtech.ide.project.ProjectManager.current().repaint();
    }
}
//  EOF
