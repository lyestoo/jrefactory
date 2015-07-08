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
package org.acm.seguin.ide.jdeveloper;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import oracle.ide.ContextMenu;
import oracle.ide.Ide;
import oracle.ide.IdeAction;
import oracle.ide.addin.Addin;
import oracle.ide.addin.Context;
import oracle.ide.addin.ContextMenuListener;
import oracle.ide.addin.Controller;
import oracle.ide.addin.View;
import oracle.ide.editor.Editor;
import oracle.ide.editor.EditorFrame;
import oracle.javatools.editor.BasicEditorPane;
import oracle.jdeveloper.ceditor.find.FindableEditor;
import org.acm.seguin.pretty.PrettyPrintFromIDE;
import org.acm.seguin.tools.install.PrettyPrinterConfigGUI;
import org.acm.seguin.tools.RefactoryInstaller;

/**
 *  The PrettyPrinter Extension for Oracle jDeveloper 9i.
 *
 * @author    Guenther Bodlak
 * @created   30. Mai 2002
 */
public class JDeveloperPrettyPrinter
     extends PrettyPrintFromIDE
     implements Addin, Controller
{
    private final static String PRETTY_PRINTER_MENU_ICON = "/org/acm/seguin/ide/jdeveloper/PrettyPrinter.gif";
    private final static int PRETTY_PRINTER_COMMAND_ID = Ide.newSysCmd("MyCommand.MY_MENU_COMMAND");
    private final static int PRETTY_PRINTER_GUI_ID = Ide.newSysCmd("MyCommand.MY_MENU_COMMAND");
    private final static float PRETTY_PRINTER_ADDIN_VERSION = 1.0f;

    private BasicEditorPane _oCurrentEditorPane = null;

    /**
     * Constructor for the JDeveloperPrettyPrinter object
     */
    public JDeveloperPrettyPrinter()
    {
        super();
    }

    /**
     *  Loads the icon.
     *
     * @param gifName  Description of the Parameter
     * @return         the Icon
     */
    public final static Icon doLoadIcon(String gifName)
    {

        Icon anIcon = null;
        java.net.URL imgURL = JDeveloperPrettyPrinter.class.getResource(gifName);
        if (imgURL != null)
        {
            anIcon = new ImageIcon(imgURL);
        }
        else
        {
            System.out.println("Error with Icon " + gifName);
        }
        return anIcon;
    }


    /**
     *  Description of the Method
     *
     * @param context    Description of the Parameter
     * @param extension  Description of the Parameter
     * @return           Description of the Return Value
     */
    public static boolean NodeIsFileType(Context context, String extension)
    {
        oracle.ide.model.Element element = (context == null) ? null : context.getElement();
        if (element != null)
        {
            if (element instanceof oracle.ide.model.Locatable)
            {
                java.net.URL url = ((oracle.ide.model.Locatable)element).getURL();
                if (url.getProtocol().equals("file"))
                {
                    String fileName = url.getFile();
                    return (extension == null) ? true : fileName.endsWith(extension);
                }
            }
        }
        return false;
    }


    /**
     *  This method is called to see if a Menu should be Enabled or Disabled The
     *  code here can be arbitrarily complex to determine the availability.
     *
     * @param context  - the Current Context to examine to determine true/false
     *      result
     * @return         <code>true</code> if the Action should be enabled, <code>false</code>
     *      otherwise
     */
    public static boolean isMenuAvailable(Context context)
    {
        return NodeIsFileType(context, ".java");
    }


    /**
     *  The AddinManager calls this method to request the version number of this
     *  addin <code>(1.0f)</code>. <br>
     *  This is different then the <code>ideVersion</code> which represents the
     *  version of the JDeveloper IDE for which this Addin was written and
     *  tested against.
     *
     * @return   the version number of this Addin <code>(e.g. 1.0f)</code>. <br>
     */
    public float version()
    {
        return PRETTY_PRINTER_ADDIN_VERSION;
    }


    /**
     *  The AddinManager calls this method to request the IDE version number for
     *  which this Addin was written and tested against. <br>
     *  Returning the wrong version number will tell the IDE to reject this
     *  Addin and not load it. <br>
     *  Note: This is different then the <code>version</code> method which
     *  represents the version number for this particular Addin. <br>
     *
     * @return   the IDE version number <code>oracle.ide.Ide.IDE_VERSION</code>.
     */
    public float ideVersion()
    {
        return oracle.ide.Ide.IDE_VERSION;
    }


    /**
     *  The AddinManager calls this method to see if this Addin is ready to shut
     *  down before the IDE terminates. <br>
     *
     * @return   allways true
     */
    public boolean canShutdown()
    {
        return true;
    }


    /**
     *  The AddinManager calls this method to allow an Addin to release any
     *  resources held before the IDE shuts down. This can include things such
     *  as saving the state of the Addin, Closing connections, etc. *
     */
    public void shutdown() { }


    /**
     *  The AddinManager calls this method at startup time to allow
     *  initialization.
     */
    public void initialize()
    {
        // Home-Dir for .Refactory dir is lib/ext
        String sJRefactoryHome =
            Ide.getHomeDirectory() + File.separator + "lib" + File.separator + "ext";
        System.getProperties().setProperty("jrefactory.home", sJRefactoryHome);

        // Is the User allowed to configure the settings?
        // If there is a file ".NoConfig" we don't show the "PrettyPrinter Options" menu
        File oFile = new File(sJRefactoryHome + File.separator + ".Refactory" + 
            File.separator + ".NoConfig");
        if (! oFile.exists())
        {
            Controller ctrlr = this;
            IdeAction mainMenuAction =
                doCreateIdeAction(ctrlr, "PrettyPrinter Options", null, PRETTY_PRINTER_MENU_ICON);
            JMenu mainMenu = oracle.ide.MainWindow.Tools;
            mainMenu.add(mainMenuAction);
        }

        // Create the Context Menu Item and add it to the Editor Menu
        JMenuItem rclickMi1 = doCreateMenuItem(this, PRETTY_PRINTER_COMMAND_ID, "Reformat Code",
            new Integer('R'), PRETTY_PRINTER_MENU_ICON);
        ctxMenuListener ctxMenuLstnr1 = new ctxMenuListener(rclickMi1);
        Ide.getEditorManager().getContextMenu().addContextMenuListener(ctxMenuLstnr1);
    }


    /**
     *  Gets the supervising controller if any associated with this Controller.
     *  This method allows multiple custom/specific controllers to allow common
     *  processing for update()/checkCommands() to be done by a common
     *  Controller (supervisor).
     *
     * @return   null
     */
    public Controller supervisor()
    {
        return null;
    }


    /**
     *  We call the PrettyPrinter ...
     *
     * @param action   action the IdeAction whose command (ID) is to be executed.
     * @param context  the current context under which this action has been invoked.
     * @return         <code>true</code> if method carried out the specified
     *      command, otherwise return <code>false</code>.
     */
    public boolean handleEvent(IdeAction action, Context context)
    {
        try
        {
            int cmdId = action.getCommandId();
            if (cmdId == PRETTY_PRINTER_COMMAND_ID)
            {
                setCurrentEditorPane(context);
                if (getCurrentEditorPane().isEditable())
                {
                    // call the PrettyPrinter
                    prettyPrintCurrentWindow();
                    return true;
                }
                else
                {
                    Ide.getLogWindow().log("PrettyPrinter: selected File is write protected!");
                }
            }
            else if (cmdId == PRETTY_PRINTER_GUI_ID)
            {
                //  Make sure everything is installed properly
                (new RefactoryInstaller(false)).run();

                // Start the config gui ...
                (new PrettyPrinterConfigGUI(false)).run();

                return true;
            }
        }
        catch (Throwable e)
        {
            StringWriter oStringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(oStringWriter));
            Ide.getLogWindow().log(oStringWriter.toString());
        }
        return false;
    }


    /**
     *  This method updates the enabled status of the specified action within
     *  the specified context.
     *
     * @param action   action whose command is to be executed.
     * @param context  the current context
     * @return         true if the controller handles the specified command.
     */
    public boolean update(IdeAction action, Context context)
    {
        int cmdId = action.getCommandId();
        if (cmdId == PRETTY_PRINTER_COMMAND_ID ||
            cmdId == PRETTY_PRINTER_GUI_ID)
        {
            if (isMenuAvailable(context))
            {
                action.setEnabled(true);
                return true;
            }
        }
        return false;
    }


    /**
     *  checkCommands() should be called on the controller associated with the
     *  active view whenever the Context changes.
     *
     * @param context           the current context. Null values are acceptable.
     * @param activeController  the controller associated with the active view.
     *      Null values are acceptable.
     */
    public void checkCommands(Context context, Controller activeController)
    {
        return;
    }


    /**
     *  Sets the stringInIDE attribute of the PrettyPrinterAddin object
     *
     * @param value  The new stringInIDE value
     */
    protected void setStringInIDE(String value)
    {
        // replacing the text is much faster than setText()!
        _oCurrentEditorPane.selectAll();
        _oCurrentEditorPane.replaceSelection(value);
    }


    /**
     *  Gets the stringFromIDE attribute of the PrettyPrinterAddin object
     *
     * @return   The stringFromIDE value
     */
    protected String getStringFromIDE()
    {
        return _oCurrentEditorPane.getText();
    }


    /**
     *  Gets the lineNumber attribute of the PrettyPrinterAddin object
     *
     * @return   The lineNumber value
     */
    protected int getLineNumber()
    {
        int iLine =
            _oCurrentEditorPane.getLineFromOffset(_oCurrentEditorPane.getCaretPosition()) + 1;
        return iLine;
    }


    /**
     *  Sets the lineNumber attribute of the PrettyPrinterAddin object
     *
     * @param iLine  The new lineNumber value
     */
    protected void setLineNumber(int iLine)
    {
        if (iLine - 1 >= 0)
        {
            _oCurrentEditorPane.setCaretPosition(_oCurrentEditorPane.getLineStartOffset(iLine - 1));
        }
    }


    /**
     *  Creates the menu item.
     *
     * @param ctrlr      Description of the Parameter
     * @param cmdID      Description of the Parameter
     * @param menuLabel  Description of the Parameter
     * @param mnemonic   Description of the Parameter
     * @param iconName   Description of the Parameter
     * @return           Description of the Return Value
     */
    private static JMenuItem doCreateMenuItem(Controller ctrlr,
        int cmdID, String menuLabel, Integer mnemonic, String iconName)
    {
        Icon mi = new ImageIcon(ctrlr.getClass().getResource(iconName));
        // This can be retrieved via IdeAction.find( ADD_COMMENT_CMD_ID )
        IdeAction actionTM =
            IdeAction.create(cmdID, null, menuLabel, mnemonic, null, mi, null, true);
        actionTM.setController(ctrlr);
        // To Set the Accelerator, uncomment line below
        //actionTM.putValue(IdeAction.ACCELERATOR, accelerator);
        // To Set the Tool Tip, uncomment line below
        //actionTM.putValue( javax.swing.Action.SHORT_DESCRIPTION, menuLabel );
        JMenuItem menuItem = Ide.getMenubar().createMenuItem(actionTM);
        return menuItem;
    }


    /**
     *  Description of the Method
     *
     * @param ctrlr      Description of the Parameter
     * @param menuLabel  Description of the Parameter
     * @param mnemonic   Description of the Parameter
     * @param iconName   Description of the Parameter
     * @return           Description of the Return Value
     */
    private static IdeAction doCreateIdeAction(Controller ctrlr, String menuLabel, Integer mnemonic, String iconName)
    {
        // Icon associated with this IdeAction
        Icon anIcon = doLoadIcon(iconName);
        // Accelerator (if any) associated with this IdeAction
        javax.swing.KeyStroke accelerator = null;
        // Name of class which extends oracle.ide.addin.AbstractCommand
        String cmdClass = null;

        // This can be retrieved via IdeAction.find( PRETTY_PRINTER_GUI_ID )
        IdeAction myCmdAction = IdeAction.get(PRETTY_PRINTER_GUI_ID, cmdClass, menuLabel,
            mnemonic, accelerator, anIcon, null, true);

        //To set the Action Category (Tools | IDE Preferences | Accelerators ), uncomment next line
        //myCmdAction.putValue(IdeAction.CATEGORY, categoryName );

        //To set an Accelerator , uncomment next line
        //myCmdAction.putValue(IdeAction.ACCELERATOR, accelerator);

        //To set the Tool Tip (used to ToolBars, etc), uncomment next line
        //myCmdAction.putValue( javax.swing.Action.SHORT_DESCRIPTION, menuLabel );

        myCmdAction.setController(ctrlr);
        return myCmdAction;
    }


    /**
     *  Gets the currentEditorPane attribute of the PrettyPrinterAddin object
     *
     * @return   The currentEditorPane value
     */
    private BasicEditorPane getCurrentEditorPane()
    {
        return _oCurrentEditorPane;
    }


    /**
     *  Sets the currentEditorPane attribute of the PrettyPrinterAddin object
     *
     * @param context  The new currentEditorPane value
     */
    private void setCurrentEditorPane(Context context)
    {
        BasicEditorPane editorPane = null;
        if (context != null)
        {
            View view = context.getView();
            FindableEditor findableEditor = null;
            if (view instanceof EditorFrame)
            {
                // If the current view is on an editor frame, then check
                // if the active editor supports the Find package.
                EditorFrame editorFrame = (EditorFrame)view;
                Editor editor = editorFrame.getCurrentEditor();
                if (editor instanceof FindableEditor)
                {
                    findableEditor = (FindableEditor)editor;
                }
            }
            else if (view instanceof FindableEditor)
            {
                // If the current view is an editor itself, then check
                // if it supports the Find package.
                findableEditor = (FindableEditor)view;
            }
            // If the current view supports the find interface, get the
            // editor pane that has focus.
            if (findableEditor != null)
            {
                editorPane = findableEditor.getFocusedEditorPane();
            }
        }
        _oCurrentEditorPane = editorPane;
    }


    /**
     *  Description of the Class
     *
     * @author    Günther Bodlak
     * @created   30. Mai 2002
     */
    private final static class ctxMenuListener
         implements ContextMenuListener
    {
        private JMenuItem menuItem;


        /**
         *  Constructor for the ctxMenuListener object
         *
         * @param ctxMenuItem  Description of the Parameter
         */
        ctxMenuListener(JMenuItem ctxMenuItem)
        {
            this.menuItem = ctxMenuItem;
        }


        /**
         *  Allow a ContextMenuListener to initialize itself before Context Menu
         *  is displayed.
         *
         * @param popup  the current contextMenu (container) which is about to
         *      be displayed
         */
        public void poppingUp(ContextMenu popup)
        {
            Context context = (popup == null) ? null : popup.getContext();
            if (context == null)
            {
                return;
            }

            // Insert the MenuItem in this Menu!
            // We could add the menu un-conditionally and then disable it,
            // But this is not always a good practice for context menus!
            if (isMenuAvailable(context))
            {
                // If it should be enabled, then display it!
                // Otherwise, dont bother showing menus that will be disabled!
                popup.add(this.menuItem);
            }
            return;
        }


        /**
         *  Allow a ContextMenuListener to do any cleanup nessecitated by the
         *  the <code>poppingUp</code> method.
         *
         * @param popup  the current contextMenu item + <code>this</code> to
         *      operate on
         */
        public void poppingDown(ContextMenu popup) { }


        /**
         *  If this Context Menu Item is the "Default" Action when user double
         *  clicks on an item. It should be realized that on any given Context
         *  Menu, there are many different available commands and only ONE can
         *  be the default cammand. Thus most implementation of this method
         *  should simply return <code>false</code> as they are not the default
         *  double click action command.
         *
         * @param context  Description of the Parameter
         * @return         Description of the Return Value
         */
        public boolean handleDefaultAction(Context context)
        {
            return false;
        }
    }
}

