/*
 *  Author:  Mike Atkinson
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.ide.jbuilder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


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
import org.acm.seguin.io.AllFileFilter;

import org.acm.seguin.pmd.cpd.CPD;
import org.acm.seguin.pmd.cpd.FileFinder;
import org.acm.seguin.pmd.cpd.JavaLanguage;

import org.acm.seguin.summary.*;
import org.acm.seguin.tools.RefactoryInstaller;
import org.acm.seguin.uml.loader.ReloaderSingleton;
import org.acm.seguin.util.FileSettings;


import com.borland.primetime.node.Node;
import com.borland.primetime.node.FileNode;
import com.borland.primetime.node.TextFileNode;
import com.borland.primetime.vfs.Buffer;
import com.borland.primetime.ide.Browser;


/**
 *  Draws a UML diagram for all the classes in a package
 *
 * @author    Mike Atkinson
 */
public class JRefactory extends JPanel implements IDEInterface {

   private JTabbedPane mainstage;
   private CPDDuplicateCodeViewer cpdViewer;
   private CodingStandardsViewer csViewer;
   protected ASTViewerPane astv;
   private Frame aView;
   private static Properties ideProperties = null;
   private JTextPane pane;
   private JBuilderBrowser sourceBrowser;


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
   public JRefactory(Frame view) {
      super(new BorderLayout());
      aView = view;
      log("new JRefactory()");
      Properties props = System.getProperties();
      props.list(System.out);

      initialiseIDEProperties();

      JAVASTYLE_DIR = new File(props.getProperty("user.home") + File.separator + ".JRefactory" + File.separator + "javastyle").getAbsolutePath();

      // plug into JRefactory some classes that adapt it to jEdit.
      org.acm.seguin.ide.common.ExitOnCloseAdapter.setExitOnWindowClose(false);

      //  Make sure everything is installed properly
      FileSettings.setSettingsRoot(JAVASTYLE_DIR);
      (new RefactoryInstaller(true)).run();
      SourceBrowser.set(new CommandLineSourceBrowser());

      cpdViewer = new CPDDuplicateCodeViewer(aView);
      org.acm.seguin.ide.command.PackageSelectorPanel panel = PackageSelectorPanel.getMainPanel(null);
      JPanel jRefactoryPanel = (panel == null) ? new ReloadChooserPanel() : panel.getPanel();
      astv = new ASTViewerPane(aView);

      JRootPane findBugs = null;
      try {
         File corePluginDir = new File(userDir, "coreplugin.jar");
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
      pane = new JTextPane();
      add(new MyScrollPane(pane), BorderLayout.EAST);
      pane.setSelectionColor(java.awt.Color.BLUE.brighter().brighter());
      pane.setSelectedTextColor(java.awt.Color.BLACK);
      pane.setHighlighter(new javax.swing.text.DefaultHighlighter());
   }

   public void initialiseIDEProperties() {
      if (ideProperties==null) {
         try {
            ideProperties.load(getClass().getResourceAsStream("/ui/JavaStyle.props"));
         } catch (java.io.IOException e) {
             e.printStackTrace();
         }
      }
   }


   /**
    *  Description of the Method
    *
    * @param  view      Description of Parameter
    * @param  fileName  The new Buffer value
    */
   public void setBuffer(Frame view, Object fileName) {
      log("setBuffer(view, " + fileName + ")");
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
    *  Description of the Method
    *
    * @param  view   Description of Parameter
    * @param  start  The new Selection value
    * @param  end    The new Selection value
    */
   public void setSelection(Frame view, Object buffer, int start, int end) {
      log("setSelection(view, " + start + "," + end + ")");
      javax.swing.text.Caret caret = pane.getCaret();
      caret.setDot(start);
      caret.moveDot(end);
      caret.setVisible(true);
      caret.setSelectionVisible(true);
   }


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
    *  Gets the IDEProperty attribute of the IDEInterface object
    *
    * @param  prop  Description of Parameter
    * @return       The IDEProperty value
    */
   public String getIDEProperty(String prop) {
      log("getIDEProperty(" + prop + ")");
      initialiseIDEProperties();
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
      log("getIDEProperty(" + prop + "," + deflt + ")");
      initialiseIDEProperties();
      return ideProperties.getProperty(prop, deflt);
   }


   /**
    *  Gets the IDEProjects attribute of the IDEInterface object
    *
    * @param  parent  Description of Parameter
    * @return         The IDEProjects value
    */
   public String[] getIDEProjects(Frame parent) {
      log("getIDEProjects(" + parent + ")");
      return new String[] { "default" };
   }


   /**
    *  Gets the Properties attribute of the IDEInterface object
    *
    * @param  type     Description of Parameter
    * @param  project  Description of Parameter
    * @return          The Properties value
    */
   public PropertiesFile getProperties(String type, String project) {
      log("getProperties(" + type + "," + project + ")");
      //log("getProperties(" + type+","+project + ")");
      String key = ("default".equals(project)) ? type + "::null" : type + "::" + project;
      PropertiesFile projectProperties = (PropertiesFile)propertiesMap.get(key);

      //log("  key="+key+" ->projectProperties="+projectProperties);
      if (projectProperties == null) {
         //log("  getting Properties(FileSettings.getSettings("+project+", \"Refactory\", "+type+")");
         projectProperties = new PropertiesFile(org.acm.seguin.util.FileSettings.getSettings(project, "Refactory", type));
         propertiesMap.put(key, projectProperties);
      }
      return projectProperties;
   }



   /**
    *  Description of the Method
    *
    * @param  buffer  Description of Parameter
    * @param  begin   Description of Parameter
    * @return         The BeginLine value
    */
   public int getLineStartOffset(Object buffer, int begin) {
      log("getLineStartOffset(" + buffer + "," + begin + ")");
      String text = getText(null,null);
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
      log("getLineEndOffset(" + buffer + "," + end + ")");
      String text = getText(null,null);
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
    * @param  view  Description of Parameter
    * @return       The Text value
    */
   public String getText(Frame view, Object buffer) {
      log("getText(view)");
        Node active = getActiveNode();
        if (active instanceof TextFileNode) {
            TextFileNode jtn = (TextFileNode) active;
            try {
                Buffer buff = jtn.getBuffer();
                byte[] contents = buff.getContent();
                return new String(contents);
            }
            catch (java.io.IOException ioex) {
                ioex.printStackTrace();
            }
        }

        return "";
   }


   /**
    *  Description of the Method
    *
    * @param  buffer  Description of Parameter
    * @return         The LineEndOffset value
    */
   public int getLineCount(Object buffer) {
      log("getLineCount(" + buffer + ")");
      String text = getText(null,null);
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
    *  Description of the Method
    *
    * @param  view    Description of Parameter
    * @param  buffer  Description of Parameter
    */
   public void goToBuffer(Frame view, Object buffer) {
      log("goToBuffer(view" + ", " + buffer + ")");
      sourceBrowser.showNode((FileNode)buffer);
   }


   /**
    *  Description of the Method
    *
    * @param  parent  Description of Parameter
    */
   public void showWaitCursor(Frame parent) {
      log("showWaitCursor(" + parent + ")");
   }

    private static java.io.PrintStream logger = null;
    private static java.io.PrintStream logger2 = null;
    public static void initLog() {
       if (logger ==null) {
          try {
             logger = new java.io.PrintStream(new java.io.FileOutputStream("C:\\temp\\JBuilder.log.txt"));
             logger2 = new java.io.PrintStream(new java.io.FileOutputStream("C:\\temp\\JBuilder.err.txt"));
             System.setOut(logger);
             System.setErr(logger2);
          } catch (java.io.FileNotFoundException e) {
             e.printStackTrace(System.err);
             logger = System.err;
          }
       }
    }

    public static void log(String message) {
       initLog();
       logger.println(message);
    }
    

   /**
    *  Description of the Method
    *
    * @param  parent  Description of Parameter
    */
   public void hideWaitCursor(Frame parent) {
      log("hideWaitCursor(" + parent + ")");
   }


   /**
    *  Description of the Method
    *
    * @param  urgency  Description of Parameter
    * @param  source   Description of Parameter
    * @param  message  Description of Parameter
    */
   public void log(int urgency, Object source, Object message) {
      log("log(" + urgency + "," + source + "," + message + ")");
   }


   /**
    *  Description of the Method
    *
    * @param  view             Description of Parameter
    * @exception  IOException  Description of Exception
    */
   public void cpdBuffer(Frame view, Object buffer) throws IOException {
      log("cpdBuffer(view)");
   }


   /**
    *  Description of the Method
    *
    * @param  view             Description of Parameter
    * @exception  IOException  Description of Exception
    */
   public void cpdAllOpenBuffers(Frame view) throws IOException {
      log("cpdAllOpenBuffers(view)");
   }


   /**
    *  Description of the Method
    *
    * @param  view             Description of Parameter
    * @param  recursive        Description of Parameter
    * @exception  IOException  Description of Exception
    */
   public void cpdDir(Frame view, boolean recursive) throws IOException {
      log("cpdDir(view)");
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
    * @param  view             Description of Parameter
    * @param  fileName         Description of Parameter
    * @return                  Description of the Returned Value
    * @exception  IOException  Description of Exception
    */
   public Object openFile(Frame view, String fileName) throws IOException {
      log("openFile(view, " + fileName + ")");
      File file = new File(fileName);
      if (file.exists()) {
         try {
            pane.read(new java.io.FileReader(file), file);
         } catch (IOException e) {
            JOptionPane.showMessageDialog(pane, e.getMessage(), "JRefactory", JOptionPane.INFORMATION_MESSAGE);
         }
      }
      return new File(fileName);
   }



   /**
    *  Description of the Method
    *
    * @param  view   Description of Parameter
    * @param  start  Description of Parameter
    */
   public void moveCaretPosition(Frame view, Object buffer, int start) {
      log("moveCaretPosition(view, " + start + ")");
      javax.swing.text.Caret caret = pane.getCaret();
      caret.moveDot(start);
      caret.setVisible(true);
   }


   /**
    *  Description of the Method
    *
    * @param  runnable  Description of Parameter
    */
   public void runInAWTThread(Runnable runnable) {
      log("runInAWTThread(" + runnable + ")");
   }


   /**
    *  Description of the Method
    *
    * @param  view  Description of Parameter
    */
   public void checkBuffer(Frame view, Object buffer) {
      log("check(view)");
      csViewer.check(view, null);
   }


   /**
    *  Description of the Method
    *
    * @param  view  Description of Parameter
    */
   public void checkAllOpenBuffers(Frame view) {
      log("checkAllOpenBuffers(view)");
      checkBuffer(view, null);
   }


   /**
    *  Description of the Method
    *
    * @param  view       Description of Parameter
    * @param  recursive  Description of Parameter
    */
   public void checkDirectory(Frame view, boolean recursive) {
      log("checkDirectory(view" + recursive + ")");
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
    *  Sets the projectData attribute of the JEditPrettyPrinter object
    *
    * @param  view  Description of Parameter
    * @return       The projectName value
    */
   public static String getProjectName(Frame view) {
      return "";
   }

   private static JFrame frame = null;

   /**
    *  The main program
    *
    * @param  args  the command line arguments
    */
   public static void main(String[] args) {
      try {
         System.setOut(new java.io.PrintStream(new java.io.FileOutputStream("out.txt")));
         System.setErr(new java.io.PrintStream(new java.io.FileOutputStream("err.txt")));
      } catch (java.io.FileNotFoundException e) {
         e.printStackTrace();
         System.exit(1);
      }
      org.acm.seguin.parser.JavaParser.setTargetJDK("jdk1.4.2");
      for (int ndx = 0; ndx < args.length; ndx++) {
         if (args[ndx].equals("-config")) {
            String dir = args[ndx + 1];
            ndx++;
            FileSettings.setSettingsRoot(dir);
         }
      }

      ExitOnCloseAdapter.setExitOnWindowClose(true);

      //  Make sure everything is installed properly
      (new RefactoryInstaller(true)).run();
      SourceBrowser.set(new CommandLineSourceBrowser());

      //if (args.length == 0) {
      //   elixir();
      //} else {
      //   selectionPanel(args[0]);
      //}
      frame = new JFrame();
      frame.setTitle("JRefactory");
      JRefactory refactory = new JRefactory(frame);
      IDEPlugin.setPlugin(refactory);
      frame.getContentPane().add(refactory);

      JMenuBar menuBar = new JMenuBar();
      frame.setJMenuBar(menuBar);
      JMenuItem options = new JMenuItem("Options");
      options.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               new org.acm.seguin.ide.common.options.JSOptionDialog(frame);
            }
         });

      menuBar.add(options);

      frame.addWindowListener(new ExitMenuSelection());
      frame.pack();
      refactory.astv.initDividers();
      frame.show();
      
   }



   /**
    *  Creates the selection panel
    *
    * @param  directory  Description of Parameter
    */
   public static void selectionPanel(String directory) {
      org.acm.seguin.ide.command.PackageSelectorPanel panel = PackageSelectorPanel.getMainPanel(directory);
      ReloaderSingleton.register(panel);
   }


   /**  Insertion point for elixir */
   public static void elixir() {
      if (PackageSelectorPanel.getMainPanel(null) != null) {
         return;
      }

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
         selectionPanel(chooser.getSelectedFile().getAbsolutePath());
      }
   }


   /**
    *  Description of the Class
    *
    * @author    Mike Atkinson
    */
   private class MyScrollPane extends JScrollPane {
      private Dimension size = new Dimension(600, 300);


      /**
       *  Constructor for the MyScrollPane object
       *
       * @param  component  Description of Parameter
       */
      public MyScrollPane(java.awt.Component component) {
         super(component);
      }


      /**
       *  Gets the MinimumSize attribute of the MyTextPane object
       *
       * @return    The MinimumSize value
       */
      public Dimension getMinimumSize() {
         return size;
      }


      /**
       *  Gets the PreferredSize attribute of the MyTextPane object
       *
       * @return    The PreferredSize value
       */
      public Dimension getPreferredSize() {
         return size;
      }
   }


   /**
    *  Description of the Class
    *
    * @author     <a href="mailto:JRefactoryPlugin@ladyshot.demon.co.uk">Mike Atkinson</a>
    * @created    23 July 2003
    * @version    $Id: JRefactory.java,v 1.2 2003/10/07 16:03:05 mikeatkinson Exp $
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

   /**
    *  Description of the Method
    *
    *@param  view    Description of Parameter
    *@param  buffer  Description of Parameter
    */
   public void saveProperties() {
      for (java.util.Iterator i = propertiesMap.keySet().iterator(); i.hasNext(); ) {
         PropertiesFile projectProperties = (PropertiesFile)propertiesMap.get(i.next());
         projectProperties.save();
      }
   }

    /**
     *  Gets the ActiveNode attribute of the PrintAction object
     *
     *@return    The ActiveNode value
     */
    private Node getActiveNode() {
        Browser browser = Browser.getActiveBrowser();
        return browser.getActiveNode();
    }

    
    public void setSourceBrowser(JBuilderBrowser sourceBrowser) {
       this.sourceBrowser = sourceBrowser;
    }

}

