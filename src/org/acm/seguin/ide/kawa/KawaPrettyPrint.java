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
package org.acm.seguin.ide.kawa;

import java.util.Properties;
import java.util.Enumeration;
import org.acm.seguin.pretty.*;
import com.allaire.kawa.plugin.*;
import org.acm.seguin.util.FileSettings;

/**
 *  This plugin is for Kawa 5 and will not work on previous versions though it
 *  will be fairly easy to port. I decided to write this plugin since I used to
 *  write my code in Kawa and then reopen it with the slow java editor jEdit
 *  just to add code comments. So one night I spend some minutes in creating
 *  this simple plugin and now I have pretty print feature in my Kawa - thanks
 *  to folks who created jrefactory (http://jrefactory.sf.net/).<P>
 *
 *  Instructions for installation:<BR>
 *
 *  <UL>
 *    <LI> Extract KawaPrettyPrint.jar in <EM>KAWA</EM> \plugin directory.
 *    <LI> From "Customize" menu select "Commands" and then "Plugin Command"
 *
 *    <LI> Press button "New" and write down "KawaPrettyPrint" for "Menu Name"
 *
 *    <LI> Set "Main Class" to "org.acm.seguin.ide.kawa.KawaPrettyPrint"
 *    <LI> If you wish KawaPrettyPrint to automaticaly beautify your source on
 *    file save set "Arguments" to "-install" (without quotes)
 *    <LI> Set classpath to the folder where KawaPrettyPrint is installed and to
 *    the file jrefactory.jar located in the same directory.
 *    <LI> If you set "Arguments" to -install you may open Options tab and
 *    select "Execute command when Kawa starts". If you didn't set -install
 *    option then you may wish to select "Add command to right button menu in
 *    editor"
 *    <LI> Enjoy!
 *  </UL>
 *
 *
 *@author     Valentin Valchev <v_valchev@prosyst.bg>
 *@created    16.11.2001
 *@since      2.6.32
 */
public class KawaPrettyPrint extends PrettyPrintFromIDE implements KawaEventListener {
    KawaEditor editor = null;
    static boolean installed = false;


    /**
     *  Constructor for the KawaPrettyPrint object
     *
     *@since           2.6.32
     *@param  install  Has this been installed?
     */
    public KawaPrettyPrint(boolean install) {
        if (install && (!installed)) {
            KawaFile.addListener(this);
            installed = true;
        }
        else {
            for (Enumeration e = KawaApp.enumerateEditors(); e.hasMoreElements(); ) {
                editor = (KawaEditor) e.nextElement();
                if (editor.hasFocus()) {
                    break;
                }
            }
            synchronized (editor) {
                try {
                    prettyPrintCurrentWindow();
                }
                catch (Throwable t) {
                }
            }
            editor = null;
        }
    }


    /**
     *  This method is called from parent. Unfortunately Kawa does not support
     *  getting current line number and that's why setting current line number
     *  is useless.
     *
     *@since         2.6.32
     *@param  value  The new line number
     */
    protected void setLineNumber(int value) {
        synchronized (editor) {
            editor.gotoLine(value, false);
        }
    }


    /**
     *  Again method called from within parent class. When called buffer
     *  contents should be replaced with text contained within <code>value</code>
     *  parameter
     *
     *@since         2.6.32
     *@param  value  the new contents of current buffer
     */
    protected void setStringInIDE(String value) {
        synchronized (editor) {
            editor.setSelect(0, -1);
            editor.paste(value);
        }
    }


    /**
     *  Returns the number of current line - Kawa does not support this - but I
     *  do!! Yes! This works now almost perfectly ;)
     *
     *@since     2.6.32
     *@return    current line number
     */
    protected int getLineNumber() {
        synchronized (editor) {
            String text = editor.getEditorText();
            try {
                int line = 1;
                for (int i = 0; i < editor.getLineIndex(-1); i++) {
                    if (text.charAt(i) == '\n') {
                        line++;
                    }
                }
                return (line);
            }
            catch (Throwable t) {
                return (int) (editor.getLineCount() / 2);
            }
        }
    }


    /**
     *  Called prom parrent class this method should return contents of buffer
     *
     *@since     2.6.32
     *@return    contents of file which is going to be saved.
     */
    protected String getStringFromIDE() {
        synchronized (editor) {
            return editor.getEditorText();
        }
    }


    /**
     *  The main program for the KawaPrettyPrint class
     *
     *@since        2.6.32
     *@param  args  The command line arguments
     */
    public static void main(String[] args) {

        boolean install = false;
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-install")) {
                    install = true;
                }
            }
        }

        // create and start plugin
        new KawaPrettyPrint(install);
    }


    /**
     *  This method is implementation of KawaEventListener, onEvent(). It is
     *  called by Kawa upon incoming events. KawaEventListener is installed on
     *  KawaFile and this method is called upon KawaFileEvent.
     *
     *@since         2.6.32
     *@param  event  KawaEvent
     */
    public void onEvent(KawaEvent event) {
        if (event.getID() == KawaFileEvent.FILE_SAVING) {
            // inform user. (must be done since first loading of plugin takes along time)
            KawaApp.out.showWindow(true);
            KawaApp.out.println("Please wait, formatting buffer");

            // get editor and lock it in synchronized section. This will prevent loss of data.
            editor = ((KawaFile) event.getSource()).getEditor(false);

            // don't try to beautify read only files
            if (editor.isReadOnly()) {
                editor = null;
                return;
            }
            synchronized (editor) {
                try {
                    prettyPrintCurrentWindow();
                }
                catch (Throwable t) {
                }
            }
            editor = null;
        }
    }
}
//  EOF
