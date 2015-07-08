/*
 *  Author:  Mike Atkinson
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.ide.standalone;

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
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

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

import org.acm.seguin.JRefactoryVersion;
import org.acm.seguin.summary.*;
import org.acm.seguin.tools.RefactoryInstaller;
import org.acm.seguin.uml.loader.ReloaderSingleton;
import org.acm.seguin.util.FileSettings;


/**
 *  Draws a UML diagram for all the classes in a package
 *
 * @author    Mike Atkinson
 */
public class JRefactory extends JPanel implements IDEInterface {

   private JTabbedPane mainstage;
   private CPDDuplicateCodeViewer cpdViewer;
   private CodingStandardsViewer csViewer;
   private ASTViewerPane astv;
   private Frame aView;
   private static PropertiesFile properties = null;
   private Properties ideProperties = new Properties();
   private JTextPane pane;
   private String fileForPane = "";

   /**  Description of the Field */
   public static String JAVASTYLE_DIR = "";
   /**  Description of the Field */
   public static File PRETTY_SETTINGS_FILE = null;
   
   private final static File userDir = new File(System.getProperty("user.dir"));
   //private JPanel panel = null;
   private static Map propertiesMap = new HashMap();

   private static JFrame frame = null;

   /**
    *  Create a new <code>JRefactory</code>.
    *
    * @param  view  Description of Parameter
    */
   public JRefactory(Frame view) {
      super(new BorderLayout());
      aView = view;
      System.out.println("new JRefactory()");


      // plug into JRefactory some classes that adapt it to jEdit.
      org.acm.seguin.ide.common.ExitOnCloseAdapter.setExitOnWindowClose(true);

      // check whether JavaStyle is invoked for the first time
      boolean firstTime = !PRETTY_SETTINGS_FILE.exists();


      //  Make sure everything is installed properly
      (new RefactoryInstaller(true)).run();
      properties = getProperties("pretty", null);
      try {
         ideProperties.load(getClass().getResourceAsStream("/ui/JavaStyle.props"));
      } catch (java.io.IOException e) {
         e.printStackTrace();
      }

      // if JavaStyle is invoked for the first time, we need to
      // correct some default values:
      if (firstTime) {
         setDefaultValues();
      }


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

      // print some version info
      String jversion = new JRefactoryVersion().toString();
      String fversion = properties.getString("version");

      log(1, this, "JRefactory version: " + jversion);
      log(1, this, "pretty settings file version: " + fversion);
   }

   /**  Sets the DefaultValues attribute of the JavaStylePlugin object */
   private void setDefaultValues() {
      // these default settings need to be corrected:
      setProperty("end.line", "NL"); // jEdit requires this
      setProperty("space.before.javadoc", "true"); // default (false) looks odd
   }

   /**
    *  Sets the Property attribute of the JavaStylePlugin class
    *
    * @param  key    The new Property value
    * @param  value  The new Property value
    */
   public static void setProperty(String key, String value) {
      properties.setString(key, value);
   }


   /**
    *  Description of the Method
    *
    * @param  view      Description of Parameter
    * @param  fileName  The new Buffer value
    */
   public void setBuffer(Frame view, Object fileName) {
      System.out.println("setBuffer(view, " + fileName + ")");
   }

   /**
    *  Description of the Method
    *
    * @param  view   Description of Parameter
    * @param  start  The new Selection value
    * @param  end    The new Selection value
    */
   public void setSelection(Frame view, Object buffer, int start, int end) {
      System.out.println("setSelection(view, " + start + "," + end + ")");

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
      System.out.println("getIDEProperty(" + prop + ")");
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
      System.out.println("getIDEProperty(" + prop + "," + deflt + ")");
      return ideProperties.getProperty(prop, deflt);
   }

   /**
    *  Gets the IDEProjects attribute of the IDEInterface object
    *
    * @param  parent  Description of Parameter
    * @return         The IDEProjects value
    */
   public String[] getIDEProjects(Frame parent) {
      System.out.println("getIDEProjects(" + parent + ")");
      return new String[]{"default"};
      //return new String[0];
   }

   /**
    *  Gets the Properties attribute of the IDEInterface object
    *
    * @param  type     Description of Parameter
    * @param  project  Description of Parameter
    * @return          The Properties value
    */
   public PropertiesFile getProperties(String type, String project) {
      System.out.println("getProperties(" + type + "," + project + ")");

      //System.out.println("getProperties(" + type+","+project + ")");
      String key = ("default".equals(project)) ? type + "::null" : type + "::" + project;
      PropertiesFile projectProperties = (PropertiesFile)propertiesMap.get(key);

      //System.out.println("  key="+key+" ->projectProperties="+projectProperties);
      if (projectProperties == null) {
         //System.out.println("  getting Properties(FileSettings.getSettings("+project+", \"Refactory\", "+type+")");
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
      System.out.println("getLineStartOffset(" + buffer + "," + begin + ")");

      String text = pane.getText();
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
      System.out.println("getLineEndOffset(" + buffer + "," + end + ")");

      String text = pane.getText();
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
    * @param  view    Description of Parameter
    * @param  buffer  Description of Parameter
    * @return         The Text value
    */
   public String getText(Frame view, Object buffer) {
      System.out.println("getText(view)");
      return pane.getText();
   }

   /**
    *  Description of the Method
    *
    * @param  buffer  Description of Parameter
    * @return         The LineEndOffset value
    */
   public int getLineCount(Object buffer) {
      System.out.println("getLineCount(" + buffer + ")");

      String text = pane.getText();
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
    * @param  buffer  Description of Parameter
    * @return         The File path for this buffer
    */
   public String getFilePathForBuffer(Object buffer) {
      return fileForPane;
   }

   /**
    *  Description of the Method
    *
    * @param  view    Description of Parameter
    * @param  buffer  Description of Parameter
    */
   public void goToBuffer(Frame view, Object buffer) {
      System.out.println("goToBuffer(view" + ", " + buffer + ")");
   }

   /**
    *  Description of the Method
    *
    * @param  parent  Description of Parameter
    */
   public void showWaitCursor(Frame parent) {
      System.out.println("showWaitCursor(" + parent + ")");
   }

   /**
    *  Description of the Method
    *
    * @param  parent  Description of Parameter
    */
   public void hideWaitCursor(Frame parent) {
      System.out.println("hideWaitCursor(" + parent + ")");
   }

   /**
    *  Description of the Method
    *
    * @param  urgency  Description of Parameter
    * @param  source   Description of Parameter
    * @param  message  Description of Parameter
    */
   public void log(int urgency, Object source, Object message) {
      System.out.println("log(" + urgency + "," + source + "," + message + ")");
   }

   /**
    *  Description of the Method
    *
    * @param  view             Description of Parameter
    * @exception  IOException  Description of Exception
    */
   public void cpdBuffer(Frame view, Object buffer) {
      System.out.println("cpdBuffer(view,buffer)");
   }

   /**
    *  Description of the Method
    *
    * @param  view             Description of Parameter
    * @exception  IOException  Description of Exception
    */
   public void cpdAllOpenBuffers(Frame view) {
      System.out.println("cpdAllOpenBuffers(view)");
   }

   /**
    *  Description of the Method
    *
    * @param  view             Description of Parameter
    * @param  recursive        Description of Parameter
    * @exception  IOException  Description of Exception
    */
   public void cpdDir(Frame view, boolean recursive) throws IOException {
      System.out.println("cpdDir(view)");

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
      System.out.println("openFile(view, " + fileName + ")");

      File file = new File(fileName);

      if (file.exists()) {
         try {
            pane.read(new java.io.FileReader(file), file);
            fileForPane = file.getCanonicalPath();
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
      System.out.println("moveCaretPosition(view, " + start + ")");

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
      System.out.println("runInAWTThread(" + runnable + ")");
   }

   /**
    *  Description of the Method
    *
    * @param  view  Description of Parameter
    */
   public void checkBuffer(Frame view, Object buffer) {
      System.out.println("check(view)");
      csViewer.check(view, buffer);
   }

   /**
    *  Description of the Method
    *
    * @param  view  Description of Parameter
    */
   public void checkAllOpenBuffers(Frame view) {
      System.out.println("checkAllOpenBuffers(view)");
      checkBuffer(view, null);
   }

   /**
    *  Description of the Method
    *
    * @param  view       Description of Parameter
    * @param  recursive  Description of Parameter
    */
   public void checkDirectory(Frame view, boolean recursive) {
      System.out.println("checkDirectory(view" + recursive + ")");

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

   /**  Description of the Method */
   public void saveProperties() {
      for (java.util.Iterator i = propertiesMap.keySet().iterator(); i.hasNext(); ) {
         PropertiesFile projectProperties = (PropertiesFile)propertiesMap.get(i.next());

         projectProperties.save();
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

      Properties props = System.getProperties();

      props.list(System.out);

      JAVASTYLE_DIR = new File(props.getProperty("user.home") + File.separator + ".JRefactory" + File.separator + "javastyle").getAbsolutePath();
      PRETTY_SETTINGS_FILE = new File(JAVASTYLE_DIR + File.separator + ".Refactory", "pretty.settings");
      FileSettings.setSettingsRoot(JAVASTYLE_DIR);

      org.acm.seguin.parser.JavaParser.setTargetJDK("jdk1.4.2");
      for (int ndx = 0; ndx < args.length; ndx++) {
         if (args[ndx].equals("-config")) {
            String dir = args[ndx + 1];

            ndx++;
            FileSettings.setSettingsRoot(dir);
         }
      }

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

      options.addActionListener(
               new ActionListener() {
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
    * @version    $Id: JRefactory.java,v 1.4 2003/10/08 06:52:45 mikeatkinson Exp $
    * @since      0.1.0
    */
   private final class ReloadChooserPanel extends JPanel {
      /**  Constructor for the ReloadChooserPanel object */
      public ReloadChooserPanel() {
         //System.out.println("new ReloadChooserPanel()");
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

