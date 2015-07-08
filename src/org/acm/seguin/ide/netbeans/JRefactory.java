/*
 *JavaStylePlugin.java - a Java pretty printer plugin for jEdit
 *Copyright (c) 1999 Andreas "Mad" Schaefer (andreas.schaefer@madplanet.ch)
 *Copyright (c) 2000,2001 Dirk Moebius (dmoebius@gmx.net)
 *
 *jEdit buffer options:
 *:tabSize=4:indentSize=4:noTabs=false:maxLineLen=0:
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.acm.seguin.ide.netbeans;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JEditorPane;

import edu.umd.cs.findbugs.DetectorFactoryCollection;
import org.acm.seguin.ide.command.CommandLineSourceBrowser;
import org.acm.seguin.ide.command.ExitMenuSelection;
import org.acm.seguin.ide.common.ASTViewerPane;
import org.acm.seguin.ide.common.CPDDuplicateCodeViewer;
import org.acm.seguin.ide.common.CodingStandardsViewer;
import org.acm.seguin.ide.common.ExitOnCloseAdapter;
import org.acm.seguin.ide.common.IDEInterface;
import org.acm.seguin.ide.common.IDEPlugin;
import org.acm.seguin.ide.common.PackageSelectorPanel;
import org.acm.seguin.ide.common.SourceBrowser;
import org.acm.seguin.ide.common.options.PropertiesFile;
import org.acm.seguin.util.FileSettings;
import org.acm.seguin.io.AllFileFilter;

import org.acm.seguin.pmd.cpd.CPD;
import org.acm.seguin.pmd.cpd.FileFinder;
import org.acm.seguin.pmd.cpd.JavaLanguage;

import org.acm.seguin.summary.*;
import org.acm.seguin.tools.RefactoryInstaller;
import org.acm.seguin.uml.loader.ReloaderSingleton;
import org.acm.seguin.util.FileSettings;

import org.openide.cookies.*;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.*;
import org.openide.util.Lookup;
import org.openide.cookies.EditorCookie;
import org.openide.windows.WindowManager;
import org.openide.windows.TopComponent;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import javax.swing.text.StyledDocument;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbBundle;
import org.openide.windows.*;

// As needed:
/*
import java.io.*;
import java.net.*;
import org.openide.ErrorManager;
import org.openide.actions.*;
import org.openide.awt.UndoRedo;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.Utilities;
import org.openide.util.actions.*;
import org.openide.util.datatransfer.PasteType;
import org.openide.util.io.*;
 */

/**
 *
 * @author       Mike Atkinson (<a
 *      href="mailto:javastyle@ladyshot.demon.co.uk">
 *      Mike@ladyshot.demon.co.uk</a> )
 * @created      30 September 2003
 * @version      $Version: $
 * @since        1.0
 */

public class JRefactory extends TopComponent /* or CloneableTopComponent */ implements IDEInterface {
    public JRefactory() {
        initComponents(null);
        setCloseOperation(CLOSE_LAST); // or CLOSE_EACH
        // Display name of this window (not needed if you use the DataObject constructor):
        setName(NbBundle.getMessage(JRefactory.class, "LBL_jrefactory"));
        // You may set the icon, but often it is better to set the icon for an associated mode instead:
        // setIcon(Utilities.loadImage("src/JRefactoryIcon.gif", true));
        // Use the Component Inspector to set tool-tip text. This will be saved
        // automatically. Other JComponent properties you may need to save yuorself.
        // At any time you can affect the node selection:
        // setActivatedNodes(new Node[] {...});
    }
    
    // REMEMBER: You should have a public default constructor!
    // This is for externalization. If you have a nondefault
    // constructor for normal creation of the component, leave
    // in a default constructor that will put the component into
    // a consistent but unconfigured state, and make sure readExternal
    // initializes it properly. Or, be creative with writeReplace().
    public JRefactory(Frame view) {
        initComponents(view);
        setCloseOperation(CLOSE_LAST); // or CLOSE_EACH
        // Display name of this window (not needed if you use the DataObject constructor):
        setName(NbBundle.getMessage(JRefactory.class, "LBL_jrefactory"));
        // You may set the icon, but often it is better to set the icon for an associated mode instead:
        // setIcon(Utilities.loadImage("src/JRefactoryIcon.gif", true));
        // Use the Component Inspector to set tool-tip text. This will be saved
        // automatically. Other JComponent properties you may need to save yuorself.
        // At any time you can affect the node selection:
        // setActivatedNodes(new Node[] {...});
    }
    
    /*
    public HelpCtx getHelpCtx() {
        return new HelpCtx(JRefactory.class);
    }
     */
    
    /*
    // If you are using CloneableTopComponent, probably you should override:
    protected CloneableTopComponent createClonedObject() {
        return new JRefactory();
    }
    protected boolean closeLast() {
        // You might want to prompt the user first and maybe return false:
        return true;
    }
     */
    
    // APPEARANCE
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    
    
    // Variables declaration - do not modify
    // End of variables declaration
    
    // MODES AND WORKSPACES
    
    /*
    // If you want it to open in a specific mode:
    public static final String JRefactory_MODE = "JRefactory";
    public void open(Workspace ws) {
        super.open(ws);
        if (ws == null) ws = WindowManager.getDefault().getCurrentWorkspace();
        Mode m = ws.findMode(JRefactory_MODE);
        if (m == null) {
            try {
                m = ws.createMode(JRefactory_MODE, // code name
                                   NbBundle.getMessage(JRefactory.class, "LBL_mode_name"), // display name
                                   new URL("nbresloc:/src/JRefactoryIcon.gif"));
            } catch (MalformedURLException mfue) {
                ErrorManager.getDefault().notify(mfue);
                return;
            }
            // If you want it in a specific position:
            // m.setBounds(...ws.getBounds()...);
        }
        m.dockInto(this);
    }
     */
    
    /*
    // If you are not specifying a mode you may wish to use:
    public Dimension getPreferredSize() {
        return ...WindowManager.getDefault().getCurrentWorkspace().getBounds()...;
    }
     */
    
    /*
    // If you want it to open on a specific workspace:
    public static final String JRefactory_WORKSPACE = NbBundle.getMessage(JRefactory.class, "LBL_workspace_name");
    public void open() {
        WindowManager wm = WindowManager.getDefault();
        Workspace ws = wm.findWorkspace(JRefactory_WORKSPACE);
        if (ws == null)
            ws = wm.createWorkspace(JRefactory_WORKSPACE);
        open(ws);
        ws.activate();
    }
     */
    
    // PERSISTENCE
    
    private static final long serialVersionUID = 1L;
    
    /*
    // If you wish to keep any state between IDE restarts, put it here:
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        setSomeState((SomeType)in.readObject());
    }
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeObject(getSomeState());
    }
     */
    
    /*
    // The above assumes that the SomeType is safely serializable, e.g. String or Date.
    // If it is some class of your own that might change incompatibly, use e.g.:
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        NbMarshalledObject read = (NbMarshalledObject)in.readObject();
        if (read != null) {
            try {
                setSomeState((SomeType)read.get());
            } catch (Exception e) {
                ErrorManager.getDefault().notify(e);
                // If the problem would make this component inconsistent, use:
                // throw new SafeException(e);
            }
        }
    }
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        Object toWrite;
        try {
            toWrite = new NbMarshalledObject(getSomeState());
        } catch (Exception e) {
            ErrorManager.getDefault().notify(e);
            toWrite = null;
            // Again you may prefer to use:
            // throw new SafeException(e);
        }
        out.writeObject(toWrite);
    }
     */
    
    /*
    // Use this to discard the component after restarts (make it nonpersistent):
    private Object readResolve() throws ObjectStreamException {
        return null;
        // If you wish to conditionally discard it, make readExternal set
        // or clear some flag acc. to the condition, then use:
        // return discardFlag ? null : this;
        // Singleton component using private static JRefactory theInstance:
        // if (theInstance == null) theInstance = this;
        // return theInstance;
    }
     */
    
    // ACTIONS
    
    /*
    // If you wish to have extra actions appear in the window's
    // popup menu, they can go here:
    public SystemAction[] getSystemActions() {
        SystemAction[] supe = super.getSystemActions();
        SystemAction[] mine = new SystemAction[supe.length + 1];
        System.arraycopy(supe, 0, mine, 0, supe.length);
        mine[supe.length] = SystemAction.get(SomeActionOfMine.class);
        return mine;
    }
     */
    
    /*
    // Associate implementations with copying, searching, etc.:
    protected void componentActivated() {
        ((CallbackSystemAction)SystemAction.get(FindAction.class)).setActionPerformer(new ActionPerformer() {
                public void performAction(SystemAction action) {
                    // search this component somehow
                }
            });
        // similar for CopyAction, CutAction, DeleteAction, GotoAction, ReplaceAction, etc.
        // for PasteAction, use:
        // ((PasteAction)SystemAction.get(PasteAction.class)).setPasteTypes(new PasteType[] {...});
    }
    protected void componentDeactivated() {
        // FindAction will be turned off by itself
        // ((PasteAction)SystemAction.get(PasteAction.class)).setPasteTypes(null);
    }
     */
    
    /*
    // If you want UndoAction and RedoAction to be enabled on this component:
    public UndoRedo getUndoRedo() {
        return new MyUndoRedo(this);
    }
     */
    
    // Printing, saving, compiling, etc.: use cookies on some appropriate node and
    // use this node as the node selection.

    
    
   /**    Description of the Field */
   public final static String OPTION_RULES_PREFIX = "options.pmd.rules.";
   /**    Description of the Field */
   public final static String OPTION_UI_DIRECTORY_POPUP = "pmd.ui.directorypopup";
   /**    Description of the Field */
   public final static String DEFAULT_TILE_MINSIZE_PROPERTY = "pmd.cpd.defMinTileSize";
   /**    Description of the Field */
   public final static String NAME = "javastyle";
   /**    Description of the Field */


   /**    Description of the Field */
   public static JRefactory jsPlugin = null;
   private static PropertiesFile properties = null;
   private Properties ideProperties = new Properties();
   private JTabbedPane mainstage;
   private CPDDuplicateCodeViewer cpdViewer;
   private CodingStandardsViewer csViewer;
   private ASTViewerPane astv;
   private Frame aView;
   private JRootPane findBugs = null;

   //private JTextPane pane;

   /**    Description of the Field */
   public static File PRETTY_SETTINGS_FILE;
   /**  Description of the Field */
   public static String JAVASTYLE_DIR = "";
   private final static File userDir = new File(System.getProperty("user.dir"));
   //private JPanel panel = null;
   private static Map propertiesMap = new HashMap();


   /**
    *  Create a new <code>JRefactory</code>.
    *
    * @param  view  Description of Parameter
    */
    private void initComponents(Frame view) {
      setLayout(new java.awt.BorderLayout());
      aView = view;
      //log("new JRefactory()");
      Properties props = System.getProperties();
      props.list(System.out);

      JAVASTYLE_DIR = new File(props.getProperty("user.home") + File.separator + ".netbeans" + File.separator + "javastyle").getAbsolutePath();
	  PRETTY_SETTINGS_FILE = new File(JAVASTYLE_DIR + File.separator + ".Refactory", "pretty.settings");

      // plug into JRefactory some classes that adapt it to jEdit.
      ExitOnCloseAdapter.setExitOnWindowClose(false);

      //  Make sure everything is installed properly
      FileSettings.setSettingsRoot(JAVASTYLE_DIR);
      (new RefactoryInstaller(true)).run();
      SourceBrowser.set(new CommandLineSourceBrowser());

      cpdViewer = new CPDDuplicateCodeViewer(aView);
      org.acm.seguin.ide.command.PackageSelectorPanel panel = PackageSelectorPanel.getMainPanel(null);
      JPanel jRefactoryPanel = (panel == null) ? new ReloadChooserPanel() : panel.getPanel();
      astv = new ASTViewerPane(aView);

      try {
         //File corePluginDir = new File(userDir, "coreplugin.jar");
         File corePluginDir = new File("./Modules/JRefactoryModule.jar");
         System.err.println("corePluginDir="+corePluginDir.getCanonicalPath());
         if (!corePluginDir.exists()) {
             corePluginDir = new File("./Modules/autoload/JRefactoryModule.jar");
         }
         if (!corePluginDir.exists()) {
             corePluginDir = new File("./Modules/eager/JRefactoryModule.jar");
         }
         File[] pluginList = (corePluginDir.exists()) ? new File[]{corePluginDir} : new File[0];
         DetectorFactoryCollection.setPluginList(pluginList);
         findBugs = org.acm.seguin.findbugs.FindBugsFrame.createFindBugsPanel(aView);
      } catch (Exception e) {
         e.printStackTrace();
      }

      csViewer = new CodingStandardsViewer(aView);
      mainstage = new JTabbedPane(JTabbedPane.TOP);
      mainstage.addTab("JRefactory", jRefactoryPanel);
      mainstage.addTab("Cut & paste detector", cpdViewer);
      mainstage.addTab("Coding standards", csViewer);
      if (findBugs != null) {
         mainstage.addTab("Find Bugs", findBugs);
      }
      mainstage.addTab("Abstract Syntax Tree", astv);
      add(mainstage, BorderLayout.CENTER);
      //pane = new JTextPane();
      //add(new MyScrollPane(pane), BorderLayout.EAST);
      //pane.setSelectionColor(java.awt.Color.BLUE.brighter().brighter());
      //pane.setSelectedTextColor(java.awt.Color.BLACK);
      //pane.setHighlighter(new javax.swing.text.DefaultHighlighter());
      try {
         ideProperties.load(getClass().getResourceAsStream("/ui/JavaStyle.props"));
      } catch (java.io.IOException e) {
          e.printStackTrace();
      }

   }


   public void setView(Frame view) {
      cpdViewer.setView(view);
      csViewer.setView(view);
      astv.setView(view);
	  findBugs = org.acm.seguin.findbugs.FindBugsFrame.createFindBugsPanel(view);
   }
   

   public Frame getFrame() {
       return aView;
   }
   public static String getSettingsDirectory() {
	   return System.getProperty("user.dir") + File.separator + ".JRefactory";
   }
   
   
   public CodingStandardsViewer getCodingStandardsViewer() {
       return csViewer;
   }
   
   /**
    *  Gets the IDEProperty attribute of the IDEInterface object
    *
    * @param  prop  Description of Parameter
    * @return       The IDEProperty value
    */
   public String getIDEProperty(String prop) {
      //log("getIDEProperty(" + prop + ")");
      return ideProperties.getProperty(prop);
   }

   /**
    *  Gets the IDEProperty attribute of the IDEInterface object
    *
    * @param  prop   Description of Parameter
    * @param  deflt  Description of Parameter
    * @return        The IDEProperty value
    */
   public String getIDEProperty(String prop, String deflt) {
      //log("getIDEProperty(" + prop + "," + deflt + ")");
      return ideProperties.getProperty(prop, deflt);
   }


   /**
    *  Gets the IDEProjects attribute of the IDEInterface object
    *
    * @param  parent  Description of Parameter
    * @return         The IDEProjects value
    */
   public String[] getIDEProjects(Frame parent) {
      //log("getIDEProjects(" + parent + ")");
      return new String[] { "default" };  // FIXME: only gets "default" project at present.
   }


   /**
    *  Description of the Method
    *
    * @param  view    Description of Parameter
    * @param  buffer  Description of Parameter
    * @return         The ProjectName value
    */
   public String getProjectName(Frame view, Object buffer) {
      return getProjectName(view);
   }

   /**
    *  Sets the projectData attribute of the JEditPrettyPrinter object
    *
    * @param  view  Description of Parameter
    * @return       The projectName value
    */
   public static String getProjectName(Frame view) {
      return "";
   }

   /**
    *  Description of the Method
    *
    * @param  buffer  Description of Parameter
    * @return         The File path for this buffer
    */
   public String getFilePathForBuffer(Object buffer) {
      return ""; // FIXME "" means the file path is not known.
   }


   /**
    *    Gets the CPDDuplicateCodeViewer attribute of the
    *    JRefactory object
    *
    * @param    view  Description of Parameter
    * @return         The CPDDuplicateCodeViewer value
    */
   //public CPDDuplicateCodeViewer getCPDDuplicateCodeViewer(Frame view) {
   //   view.getDockableWindowManager().showDockableWindow("javastyle");
   //   JRefactory jr = (JRefactory) view.getDockableWindowManager().getDockableWindow("javastyle");
   //   return jr.getCPDDuplicateCodeViewer();
   //}




   /**
    *  Description of the Method
    *
    * @param  view    Description of Parameter
    * @param  buffer  Description of Parameter
    */
   public void goToBuffer(Frame view, Object buffer) {
      log("NOT IMPLEMENTED: goToBuffer(view" + ", " + buffer + ")");
   }


   /**
    *  Description of the Method
    *
    * @param  parent  Description of Parameter
    */
   public void showWaitCursor(Frame parent) {
      log("NOT IMPLEMENTED: showWaitCursor(" + parent + ")");
   }


   /**
    *  Description of the Method
    *
    * @param  parent  Description of Parameter
    */
   public void hideWaitCursor(Frame parent) {
      log("NOT IMPLEMENTED: hideWaitCursor(" + parent + ")");
   }

   /**
    *  Description of the Method
    *
    * @param  view  Description of Parameter
    */
   public void checkBuffer(Frame view, Object buffer) {
      //log("check(view)");
      csViewer.check(view, buffer);
   }


   /**
    *  Description of the Method
    *
    * @param  view  Description of Parameter
    */
   public void checkAllOpenBuffers(Frame view) {
      //log("checkAllOpenBuffers(view)");
      checkBuffer(view, null);
   }


   /**
    *  Description of the Method
    *
    * @param  view       Description of Parameter
    * @param  recursive  Description of Parameter
    */
   public void checkDirectory(Frame view, boolean recursive) {
      //log("checkDirectory(view" + recursive + ")");
      JFileChooser chooser = new JFileChooser();

      //  Add other file filters - All
      AllFileFilter allFilter = new AllFileFilter();
      chooser.addChoosableFileFilter(allFilter);

      //  Set it so that files and directories can be selected
      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

      //  Set the directory to the current directory
      chooser.setCurrentDirectory(userDir);

      //  Get the user's selection
      int returnVal = chooser.showOpenDialog(null);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
         //selectionPanel(chooser.getSelectedFile().getAbsolutePath());
         process(findFiles(chooser.getSelectedFile().getAbsolutePath(), recursive), view);
      }
   }


   /**
    *  Description of the Method
    *
    * @param  dir      Description of Parameter
    * @param  recurse  Description of Parameter
    * @return          Description of the Returned Value
    */
   private List findFiles(String dir, boolean recurse) {
      FileFinder finder = new FileFinder();
      return finder.findFilesFrom(dir, new JavaLanguage.JavaFileOrDirectoryFilter(), recurse);
   }


   /**
    *  Description of the Method
    *
    * @param  files  Description of Parameter
    * @param  view   Description of Parameter
    */
   private void process(final List files, final Frame view) {
      new Thread(
         new Runnable() {
            public void run() {
               processFiles(files, view);
            }
         }).start();
   }


   /**
    *  Description of the Method
    *
    * @param  files  Description of Parameter
    * @param  view   Description of Parameter
    */
   private void processFiles(List files, Frame view) {
      List contexts = csViewer.checkFiles(files, view, null);
   }



   /**
    *    Sets the Property attribute of the JavaStylePlugin class
    *
    * @param    key    The new Property value
    * @param    value  The new Property value
    */
   public static void setProperty(String key, String value) {
      properties.setString(key, value);
   }

   /**
    *  Description of the Method
    *
    * @param  view  Description of Parameter
    * @return       The Text value
    */
   public String getText(Frame view, Object buffer) {
      //System.out.println("view="+view);
      //System.out.println("buffer="+buffer);
      if (buffer==null || (((JEditorPane)buffer).getText()==null) ) {
          System.out.println("buffer="+buffer);
          JEditorPane pane = getCurrentEditorPane(null);
          String text = pane.getText();
          System.out.println("text="+text);
          return text;
      } else {
          //log("getText(view)->"+ (((JEditorPane)buffer).getText()));
          return ((JEditorPane)buffer).getText();
      }
   }

    /**
     *  Gets the CurrentEditorPane attribute of the NetBeansExtractMethodDialog
     *  object
     *
     *@param  cookie  Description of Parameter
     *@return         The CurrentEditorPane value
     */
    private JEditorPane getCurrentEditorPane(EditorCookie cookie) {
        WindowManager wm = (WindowManager)Lookup.getDefault().lookup(WindowManager.class);
        TopComponent comp = wm.getRegistry().getActivated();
        Node[] nodes = comp.getRegistry().getActivatedNodes();
        System.out.println("nodes.length="+nodes.length);

        //(NOTE) This is a hack fix
        cookie = null;
        for (int i=0; i<nodes.length; i++) {
            cookie = (EditorCookie) nodes[i].getCookie(EditorCookie.class);
            if (cookie!=null) {
                JEditorPane[] panes = cookie.getOpenedPanes();
                System.out.println("panes.length="+panes.length);
                if (panes.length == 1) {
                    return panes[0];
                }
                break; 
            }
        }

        return null;
    }

    private javax.swing.text.Caret getCaret() {
        JEditorPane pane = getCurrentEditorPane(null);
        return (pane==null) ? null : pane.getCaret();
    }
    
    
    
   /**
    *  Description of the Method
    *
    * @param  buffer  Description of Parameter
    * @param  begin   Description of Parameter
    * @return         The BeginLine value
    */
   public int getLineStartOffset(Object buffer, int begin) {
      //log("getLineStartOffset(" + buffer + "," + begin + ")");
      String text = getText(null,buffer);
      int i = 0;
      int pos = begin;
      while (pos > 0 && i < text.length()) {
         if (text.charAt(i++) == '\n') {
            pos--;
         }
      }
      return i - begin;
   }


   /**
    *  Description of the Method
    *
    * @param  buffer  Description of Parameter
    * @param  end     Description of Parameter
    * @return         The LineEndOffset value
    */
   public int getLineEndOffset(Object buffer, int end) {
      //log("getLineEndOffset(" + buffer + "," + end + ")");
      String text = getText(null,buffer);
      int i = 0;
      int pos = end;
      while (pos > 0 && i < text.length()) {
         if (text.charAt(i++) == '\n') {
            pos--;
         }
      }
      return i - 1 - end;
   }


   /**
    *  Description of the Method
    *
    * @param  view             Description of Parameter
    * @param  fileName         Description of Parameter
    * @return                  Description of the Returned Value
    * @exception  IOException  Description of Exception
    */
   public Object openFile(Frame view, String fileName) throws IOException {
      //log("openFile(view, " + fileName + ")");
      File file = new File(fileName);
      if (file.exists()) {
         try {
             FileObject fo = FileUtil.fromFile(file)[0];
             DataObject d = DataObject.find(fo);
             EditorCookie ec = (EditorCookie)d.getCookie(EditorCookie.class);
             ec.open();
             StyledDocument doc = ec.openDocument();
         } catch (IOException e) {
            JOptionPane.showMessageDialog(view, e.getMessage(), "JRefactory", JOptionPane.INFORMATION_MESSAGE);
         }
      }
      return new File(fileName);
   }



   /**
    *  Description of the Method
    *
    * @param  view   Description of Parameter
    * @param  start  The new Selection value
    * @param  end    The new Selection value
    */
   public void setSelection(Frame view, Object buffer, int start, int end) {
      //log("setSelection(view, " + start + "," + end + ")");
      javax.swing.text.Caret caret = getCaret();
      caret.setDot(start);
      caret.moveDot(end);
      caret.setVisible(true);
      caret.setSelectionVisible(true);
   }

   /**
    *  Description of the Method
    *
    * @param  view      Description of Parameter
    * @param  fileName  The new Buffer value
    */
   public void setBuffer(Frame view, Object fileName) {
      log("NOT IMPLEMENTED: setBuffer(view, " + fileName + ")");
   }

   /**
    *  Description of the Method
    *
    * @param  runnable  Description of Parameter
    */
   public void runInAWTThread(Runnable runnable) {
      //log("runInAWTThread(" + runnable + ")");
	  runnable.run();  // FIXME: need to run in AWT Thread.
   }


   /**
    *  Description of the Method
    *
    * @param  view   Description of Parameter
    * @param  start  Description of Parameter
    */
   public void moveCaretPosition(Frame view, Object buffer, int start) {
      //log("moveCaretPosition(view, " + start + ")");
      javax.swing.text.Caret caret = getCaret();
      caret.moveDot(start);
      caret.setVisible(true);
   }


   /**
    *    Gets the Properties attribute of the JavaStylePlugin class
    *
    * @param    project  Description of Parameter
    * @return            The Properties value
    */
   public PropertiesFile getProperties(String type, String project) {
      //log("getProperties(" + type+","+project + ")");
      if ("default".equals(project)) {
         project=null;
      }

	  String key = type+"::"+project;
      PropertiesFile pf = (PropertiesFile) propertiesMap.get(key);

	  //log("  key="+key+" ->pf="+pf);
      if (pf == null) {
		 //log("  getting Properties(FileSettings.getSettings("+project+", \"Refactory\", "+type+")");
         pf = new PropertiesFile(org.acm.seguin.util.FileSettings.getSettings(project, "Refactory", type));
         propertiesMap.put(key, pf);
      }
      return pf;
   }

   /**
    *    Description of the Method
    *
    * @param    key  Description of Parameter
    */
   public static void deleteProperty(String key) {
      properties.deleteKey(key);
   }

   /**    write new settings */
   public void saveProperties() {
      for (java.util.Iterator i = propertiesMap.keySet().iterator(); i.hasNext(); ) {
         PropertiesFile projectProperties = (PropertiesFile)propertiesMap.get(i.next());
         projectProperties.save();
      }
   }

   /**
    *    Description of the Method
    *
    * @param    view             Description of Parameter
    * @exception    IOException  Description of Exception
    */
   public void cpdBuffer(Frame view, Object buffer) throws IOException {
      log("NOT IMPLEMENTED: cpdBuffer(view,buffer)");
   }

   /**
    *    Description of the Method
    *
    * @param    view             Description of Parameter
    * @exception    IOException  Description of Exception
    */
   public void cpdAllOpenBuffers(Frame view) throws IOException {
      log("NOT IMPLEMENTED: cpdAllOpenBuffers(view)");
   }

   /**
    *  Description of the Method
    *
    * @param  buffer  Description of Parameter
    * @return         The LineEndOffset value
    */
   public int getLineCount(Object buffer) {
      //log("getLineCount(" + buffer + ")");
      String text = getText(null, buffer);
      int lc = 1;
      int i = 0;
      while (i < text.length()) {
         if (text.charAt(i++) == '\n') {
            lc++;
         }
      }
      return lc;
   }


   /**
    *    Description of the Method
    *
    * @param    view             Description of Parameter
    * @param    recursive        Description of Parameter
    * @exception    IOException  Description of Exception
    */
   public void cpdDir(Frame view, boolean recursive) throws IOException {
      //log("cpdDir(view)");
      JFileChooser chooser = new JFileChooser(getIDEProperty("pmd.cpd.lastDirectory"));

      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

      JPanel pnlAccessory = new JPanel();

      pnlAccessory.add(new JLabel("Minimum Tile size :"));

      JTextField txttilesize = new JTextField("100");

      pnlAccessory.add(txttilesize);
      chooser.setAccessory(pnlAccessory);

      int returnVal = chooser.showOpenDialog(view);
      File selectedFile = null;

      if (returnVal == JFileChooser.APPROVE_OPTION) {
         selectedFile = chooser.getSelectedFile();
         if (!selectedFile.isDirectory()) {
            JOptionPane.showMessageDialog(view, "Selection not a directory.", "JRefactory", JOptionPane.ERROR_MESSAGE);
            return;
         }
      } else {
         return;
      }
      // In case the user presses cancel or escape.

      getIDEProperty("pmd.cpd.lastDirectory", selectedFile.getCanonicalPath());

      int tilesize = 100;

      try {
         tilesize = Integer.parseInt(txttilesize.getText());
      } catch (NumberFormatException e) {
         //use the default.
         tilesize = 100;
      }

      CPD cpd = new CPD(tilesize, new JavaLanguage());

      if (recursive) {
         cpd.addRecursively(selectedFile.getCanonicalPath());
      } else {
         cpd.addAllInDirectory(selectedFile.getCanonicalPath());
      }

      cpd.go();
      if (cpdViewer != null) {
         cpdViewer.processDuplicates(cpd, view);
      }
   }




   /**
    *  Description of the Method
    *
    * @param  urgency  Description of Parameter
    * @param  source   Description of Parameter
    * @param  message  Description of Parameter
    */
   public static void log(Object message) {
      System.err.println("JavaStyle: " + message);
   }

   /**
    *  Description of the Method
    *
    * @param  urgency  Description of Parameter
    * @param  source   Description of Parameter
    * @param  message  Description of Parameter
    */
   public void log(int urgency, Object source, Object message) {
      System.err.println("JavaStyle(" + urgency + "," + source + "," + message + ")");
   }

   /**
    *  Description of the Class
    *
    * @author    Mike Atkinson
    */
//   private static class MyScrollPane extends JScrollPane {
//      private Dimension size = new Dimension(600, 300);


      /**
       *  Constructor for the MyScrollPane object
       *
       * @param  component  Description of Parameter
       */
//      public MyScrollPane(java.awt.Component component) {
//         super(component);
//      }


      /**
       *  Gets the MinimumSize attribute of the MyTextPane object
       *
       * @return    The MinimumSize value
       */
//      public Dimension getMinimumSize() {
//         return size;
//      }


      /**
       *  Gets the PreferredSize attribute of the MyTextPane object
       *
       * @return    The PreferredSize value
       */
//      public Dimension getPreferredSize() {
//         return size;
//      }
//   }

   /**  Gets the userSelection attribute of the JRefactory object */
   public void getUserSelection() {
      JFileChooser chooser = new JFileChooser();

      //  Add other file filters - All
      AllFileFilter allFilter = new AllFileFilter();
      chooser.addChoosableFileFilter(allFilter);

      //  Set it so that files and directories can be selected
      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

      //  Set the directory to the current directory
      chooser.setCurrentDirectory(userDir);

      int returnVal = chooser.showOpenDialog(null);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
         org.acm.seguin.ide.command.PackageSelectorPanel panel = PackageSelectorPanel.getMainPanel(chooser.getSelectedFile().getAbsolutePath());
         ReloaderSingleton.register(panel);
         mainstage.setComponentAt(0, panel.getPanel());
      } else {
         mainstage.setComponentAt(0, new ReloadChooserPanel());
      }
   }


   /**
    *  Description of the Class
    *
    * @author     <a href="mailto:JRefactoryPlugin@ladyshot.demon.co.uk">Mike Atkinson</a>
    * @created    23 July 2003
    * @version    $Id: JRefactory.java,v 1.3 2003/10/07 15:59:25 mikeatkinson Exp $
    * @since      0.1.0
    */
   private final class ReloadChooserPanel extends JPanel {
      /**  Constructor for the ReloadChooserPanel object */
      public ReloadChooserPanel() {
         //log("new ReloadChooserPanel()");
         JButton load = new JButton("load JRefactory UML viewer");
         load.addActionListener(
            new ActionListener() {
               public void actionPerformed(ActionEvent event) {
                  JRefactory.this.getUserSelection();
               }
            });
         add(load);
      }
   }


}

