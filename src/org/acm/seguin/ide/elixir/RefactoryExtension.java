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
import com.elixirtech.fx.ViewManager;
import com.elixirtech.fx.IViewSite;
import com.elixirtech.fx.SettingManager;
import com.elixirtech.extension.IExtension;
import com.elixirtech.msg.Message;
import com.elixirtech.msg.MsgType;
import com.elixirtech.msg.ApplicationBus;
import com.elixirtech.ide.project.Folder;
import org.acm.seguin.tools.RefactoryInstaller;
import org.acm.seguin.ide.common.SingleDirClassDiagramReloader;
import org.acm.seguin.uml.loader.ReloaderSingleton;
import org.acm.seguin.ide.common.SourceBrowser;
/*</Imports>*/

/**
 *  Refactory extension loads the refactory tool into memory
 *
 *@author     Chris Seguin
 *@created    April 4, 2000
 */
public class RefactoryExtension extends PrettyPrinterExtension
         implements ApplicationBus.ICard {
    /**
     *  Stores the base directory for the source code
     */
    static String base;
    private UMLDocManager manager;


    /**
     *  Gets the CardName attribute of the Refactory
     *
     *@return    The CardName value
     */
    public String getCardName() {
        return "Refactory";
    }


    /**
     *  Gets the Name attribute of the Refactory extension
     *
     *@return    The Name value
     */
    public String getName() {
        return "Refactory";
    }


    /**
     *  Removes the extension mechanism
     *
     *@return    Always returns true
     */
    public boolean destroy() {
        ApplicationBus.removeCard(this);
        return super.destroy();
    }


    /**
     *  Initializes the extension
     *
     *@param  args  the arguments
     *@return       true if installed
     */
    public boolean init(String[] args) {
        FrameManager fm = FrameManager.current();
        if (fm == null) {
            return false;
        }

        (new RefactoryInstaller(true)).run();

        manager = new UMLDocManager();
        fm.addDocManager(manager);

        Folder.addOpenFileFilter(".uml", "Class Diagrams (*.uml)");

        ApplicationBus.addCard(this);

        boolean result = super.init(args);

        ZoomDiagram.tenPercent();
        new UndoMenuItem();
        new ElixirClassDiagramLoader();
        try {
            new ElixirExtractMethod();
        }
        catch (Exception exc) {
        }

        SourceBrowser.set(new ElixirSourceBrowser());

        FrameManager.current().addMenuItem("Script|JRefactory|Extract Method=((method \"extractMethod\" \"org.acm.seguin.ide.elixir.ElixirExtractMethod\"))");
        FrameManager.current().addMenuItem("Script|JRefactory|Reload Diagrams=((method \"reload\" \"org.acm.seguin.ide.elixir.ElixirClassDiagramLoader\"))");
        FrameManager.current().addMenuItem("Script|JRefactory|Undo Refactoring=((method \"undo\" \"org.acm.seguin.ide.elixir.UndoMenuItem\"))");
        FrameManager.current().addMenuItem("Script|JRefactory|Zoom|10%=((method \"tenPercent\" \"org.acm.seguin.ide.elixir.ZoomDiagram\"))");
        FrameManager.current().addMenuItem("Script|JRefactory|Zoom|25%=((method \"twentyfivePercent\" \"org.acm.seguin.ide.elixir.ZoomDiagram\"))");
        FrameManager.current().addMenuItem("Script|JRefactory|Zoom|50%=((method \"fiftyPercent\" \"org.acm.seguin.ide.elixir.ZoomDiagram\"))");
        FrameManager.current().addMenuItem("Script|JRefactory|Zoom|100%=((method \"fullSize\" \"org.acm.seguin.ide.elixir.ZoomDiagram\"))");
        FrameManager.current().addMenuItem("Script|JRefactory|About JRefactory=((method \"run\" \"org.acm.seguin.awt.AboutBox\"))");

        return result;
    }


    /**
     *  Listener for GUI change events
     *
     *@param  msg  the message
     */
    public void update(Message msg) {
        SingleDirClassDiagramReloader reloader = manager.getReloader();
        MsgType type = msg.getType();
        if (type == MsgType.PROJECT_OPENED) {
            RefactoryExtension.base = SettingManager.getSetting("WorkRoot");
            reloader.setRootDirectory(RefactoryExtension.base);
            Thread anonymous =
                new Thread() {
                    /**
                     *  Main processing method for the RefactoryExtension object
                     */
                    public void run() {
                        ReloaderSingleton.reload();
                    }
                };
            anonymous.start();
        }
        else if (type == MsgType.PROJECT_CLOSED) {
            reloader.clear();
        }
        else if (type == MsgType.DOCUMENT_OPENED) {
            if (msg.getData() instanceof UMLViewManager) {
                UMLViewManager view = (UMLViewManager) msg.getData();
                reloader.add(view.getDiagram());
            }
        }
        else if (type == MsgType.DOCUMENT_CLOSED) {
            if (msg.getData() instanceof UMLViewManager) {
                UMLViewManager view = (UMLViewManager) msg.getData();
                reloader.remove(view.getDiagram());
            }
        }
    }


    /**
     *  Opportunity to veto a message
     *
     *@param  msg  the message
     */
    public void veto(Message msg) {
        // no veto
    }
}
//  EOF
