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
package org.acm.seguin.ide.elixir;

/*<Imports>*/
import com.elixirtech.fx.FrameManager;
import com.elixirtech.extension.IExtension;
import com.elixirtech.msg.Message;
import com.elixirtech.msg.MsgType;
import com.elixirtech.msg.ApplicationBus;
import org.acm.seguin.JRefactoryVersion;
/*</Imports>*/

/**
 *  Pretty Printer extension mechanism for Elixir 2.4
 *
 *@author     Chris Seguin
 *@created    October 18, 2001
 */
public class PrettyPrinterExtension implements IExtension {

    /**
     *  Gets the CardName attribute of the PrettyPrinterExtension object
     *
     *@return    The CardName value
     */
    public String getCardName() {
        return "Pretty Printer";
    }


    /**
     *  Gets the Name attribute of the PrettyPrinterExtension object
     *
     *@return    The Name value
     */
    public String getName() {
        return "Pretty Printer";
    }


    /**
     *  Gets the ReleaseNo attribute of the PrettyPrinterExtension object
     *
     *@return    The ReleaseNo value
     */
    public int getReleaseNo() {
        return (new JRefactoryVersion()).getBuild();
    }


    /**
     *  Gets the Version attribute of the PrettyPrinterExtension object
     *
     *@return    The Version value
     */
    public String getVersion() {
        return (new JRefactoryVersion()).toString();
    }


    /**
     *  Removes the extension mechanism
     *
     *@return    Always returns true
     */
    public boolean destroy() {
        return true;
    }


    /**
     *  Initializes the extension
     *
     *@param  args  the arguments
     *@return       true if installed
     */
    public boolean init(String[] args) {
        if (FrameManager.current() == null) {
            System.out.println("Not installing " + getName() + " " + getVersion());
            return false;
        }
        System.out.println("Installing " + getName() + " " + getVersion());

        //  Load the objects
        new ElixirPrettyPrinter();
        new ElixirTextPrinter();

        //  Add the menu items
        FrameManager.current().addMenuItem("Script|JRefactory|Refresh=((method \"reload\" \"com.elixirtech.ide.edit.BasicViewManager\") (curr-vm))");
        FrameManager.current().addMenuItem("Script|JRefactory|Pretty Printer=((method \"prettyPrint\" \"org.acm.seguin.ide.elixir.ElixirPrettyPrinter\"))");
        FrameManager.current().addMenuItem("Script|JRefactory|Config=((method \"prettyPrintConfig\" \"org.acm.seguin.ide.elixir.ElixirPrettyPrinter\"))");
        FrameManager.current().addMenuItem("Script|JRefactory|Print=((method \"printCurrent\" \"org.acm.seguin.ide.elixir.ElixirTextPrinter\"))");

        return true;
    }
}
//  EOF
