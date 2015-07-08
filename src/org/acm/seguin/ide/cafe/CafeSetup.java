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
package org.acm.seguin.ide.cafe;

import com.symantec.itools.vcafe.openapi.pluginapi.Plugin;
import com.symantec.itools.vcafe.openapi.VisualCafe;
import java.awt.MenuBar;
import java.awt.Menu;
import java.awt.MenuItem;
import java.io.InputStream;
import java.io.OutputStream;
import org.acm.seguin.tools.install.RefactoryInstaller;
import org.acm.seguin.util.FileSettings;
import org.acm.seguin.refactor.RefactoringException;

/**
 *  This class sets up the pretty printer and all the associated menu items.
 *
 *@author     Chris Seguin
 *@created    August 26, 2000
 */
public class CafeSetup implements Plugin {
    /**
     *  Gets the PluginInfo attribute of the CafePrettyPrinter object
     */
    public void getPluginInfo() {
        System.out.println("CafePrettyPrinter::getInfo()");
    }


    /**
     *  Gets the SubMenu attribute of the CafePrettyPrinter object
     *
     *@return    The SubMenu value
     */
    private Menu getSubMenu() {
        Menu jrefactoryMenu = new Menu("JRefactory");

        MenuItem prettyPrinterMenuItem = new MenuItem("P&retty Printer");
        prettyPrinterMenuItem.addActionListener(new CafePrettyPrinter());
        jrefactoryMenu.add(prettyPrinterMenuItem);

        try {
            MenuItem loadMenuItem = new MenuItem("Extract Method");
            loadMenuItem.addActionListener(new CafeExtractMethod());
            jrefactoryMenu.add(loadMenuItem);
        }
        catch (Throwable re) {
        }

        MenuItem extractMenuItem = new MenuItem("Load Metadata");
        extractMenuItem.addActionListener(new ReloadActionAdapter());
        jrefactoryMenu.add(extractMenuItem);

        /*
         *  MenuItem viewDiagramMenuItem = new MenuItem("View Class Diagram");
         *  viewDiagramMenuItem.setEnabled(false);
         *  jrefactoryMenu.add(viewDiagramMenuItem);
         *  MenuItem printMenuItem = new MenuItem("Print");
         *  printMenuItem.setEnabled(false);
         *  jrefactoryMenu.add(printMenuItem);
         *  Menu zoomMenu = new Menu("Zoom");
         *  jrefactoryMenu.add(zoomMenu);
         *  MenuItem tenMenuItem = new MenuItem("10%");
         *  tenMenuItem.setEnabled(false);
         *  zoomMenu.add(tenMenuItem);
         *  MenuItem twentyfiveMenuItem = new MenuItem("25%");
         *  twentyfiveMenuItem.setEnabled(false);
         *  zoomMenu.add(twentyfiveMenuItem);
         *  MenuItem fiftyMenuItem = new MenuItem("50%");
         *  fiftyMenuItem.setEnabled(false);
         *  zoomMenu.add(fiftyMenuItem);
         *  MenuItem fullMenuItem = new MenuItem("100%");
         *  fullMenuItem.setEnabled(false);
         *  zoomMenu.add(fullMenuItem);
         */
        return jrefactoryMenu;
    }


    /**
     *  Used to close out this object
     */
    public void destroy() { }


    /**
     *  Initializes Visual Cafe settings
     */
    public void init() {
        String root = System.getProperty("user.home");
        FileSettings.setSettingsRoot(root);

        //  Make sure everything is installed properly
        (new RefactoryInstaller(false)).run();

        VisualCafe vc = VisualCafe.getVisualCafe();

        // Add sample submenus to Visual Cafe MenuBar
        MenuBar mb = vc.getMenuBar();
        Menu subMenu = getSubMenu();
        mb.add(subMenu);
    }


    /**
     *  Restores the state
     *
     *@param  is  The input stream
     *@param  b   a boolean if anything has changed
     */
    public void restore(InputStream is, boolean b) { }


    /**
     *  Used to save this object
     *
     *@param  os  the output stream
     *@param  b   boolean if it needs to be saved
     */
    public void save(OutputStream os, boolean b) { }
}
//  EOF
