/*
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more detaProjectTreeSelectionListenerils.
    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
  */
package org.acm.seguin.ide.jedit;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTabbedPane;

import edu.umd.cs.findbugs.DetectorFactoryCollection;
import org.acm.seguin.ide.command.CommandLineSourceBrowser;
import org.acm.seguin.ide.common.ASTViewerPane;
import org.acm.seguin.ide.common.CPDDuplicateCodeViewer;
import org.acm.seguin.ide.common.CodingStandardsViewer;
import org.acm.seguin.ide.common.PackageSelectorPanel;
import org.acm.seguin.ide.common.SourceBrowser;
import org.acm.seguin.ide.jedit.action.Action;
import org.acm.seguin.ide.jedit.action.HideAction;
import org.acm.seguin.ide.jedit.action.ReloadAction;
import org.acm.seguin.ide.jedit.action.ReloadAllAction;
import org.acm.seguin.ide.jedit.action.ShowAction;

import org.acm.seguin.ide.jedit.event.JRefactoryEvent;
import org.acm.seguin.ide.jedit.event.JRefactoryListener;
import org.acm.seguin.io.AllFileFilter;

// JRefactory imports

import org.acm.seguin.tools.RefactoryInstaller;
import org.acm.seguin.uml.loader.ReloaderSingleton;
import org.gjt.sp.jedit.EBComponent;
import org.gjt.sp.jedit.EBMessage;
import org.gjt.sp.jedit.EditBus;
import org.gjt.sp.jedit.EditPlugin;

import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.util.Log;


/**
 *  Main GUI for JRefactory
 *
 *@author     <a href="mailto:JRefactoryPlugin@ladyshot.demon.co.uk">Mike Atkinson</a>
 *@created    23 July 2003
 *@version    $Id: JRefactory.java,v 1.4 2003/09/25 13:30:36 mikeatkinson Exp $
 *@since      0.0.1
 */
public final class JRefactory extends JPanel implements EBComponent {

   private View view;
   private JTabbedPane mainstage;
   private CPDDuplicateCodeViewer cpdViewer;
   private CodingStandardsViewer csViewer;

   /**  Description of the Field */
   public final static String CREATE_NEW_PROJECT = jEdit.getProperty("jrefactory.create_project");

   private final static HashMap viewers = new HashMap();
   private final static HashMap listeners = new HashMap();
   private final static ArrayList viewerList = new ArrayList();
   private final static ArrayList actions = new ArrayList();



   /**
    *  Create a new <code>JRefactory</code>.
    *
    *@param  aView  Description of Parameter `
    */
   public JRefactory(View aView) {
      super(new BorderLayout());
      //System.out.println("new JRefactory()");
      view = aView;

      if (viewers.get(aView) == null) {
         viewers.put(aView, this);
      }
      viewerList.add(this);

      EditBus.addToBus(this);

      // plug into JRefactory some classes that adapt it to jEdit.
      org.acm.seguin.ide.common.ExitOnCloseAdapter.setExitOnWindowClose(false);
      org.acm.seguin.refactor.undo.UndoStack.get().setUndoAction(org.acm.seguin.ide.jedit.action.JEditUndoAction.class);
      org.acm.seguin.refactor.Refactoring.setComplexTransform(org.acm.seguin.ide.jedit.JEditComplexTransform.class);

      //  Make sure everything is installed properly
      (new RefactoryInstaller(true)).run();
      SourceBrowser.set(new CommandLineSourceBrowser());

      cpdViewer = new CPDDuplicateCodeViewer(aView);
      org.acm.seguin.ide.command.PackageSelectorPanel panel = PackageSelectorPanel.getMainPanel(null);
      JPanel jRefactoryPanel = (panel == null) ? new ReloadChooserPanel() : panel.getPanel();
      ASTViewerPane astv = new ASTViewerPane(aView);

      JRootPane findBugs = null;
      try {
         EditPlugin thisPlugin = jEdit.getPlugin("JavaStylePlugin");
         if (thisPlugin == null) {
            thisPlugin = jEdit.getPlugin("org.acm.seguin.ide.jedit.JavaStylePlugin");
         }
         File[] pluginList = new File[0];
         if (thisPlugin != null) {
            pluginList = new File[]{
                  new File(thisPlugin.getPluginJAR().getFile().getParentFile(), "coreplugin.jar")
                  };
         }
         DetectorFactoryCollection.setPluginList(pluginList);
         findBugs = org.acm.seguin.findbugs.FindBugsFrame.createFindBugsPanel(aView);
      } catch (Throwable e) {
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
      astv.initDividers();

   }


   /**
    *  Gets the CPDDuplicateCodeViewer attribute of the JRefactory object
    *
    *@return    The CPDDuplicateCodeViewer value
    */
   public CPDDuplicateCodeViewer getCPDDuplicateCodeViewer() {
      return cpdViewer;
   }


   /**
    *  Gets the CodingStandardsViewer attribute of the JRefactory object
    *
    *@return    The CodingStandardsViewer value
    */
   public CodingStandardsViewer getCodingStandardsViewer() {
      return csViewer;
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
      chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

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
    *  Description of the Method
    *
    *@param  msg  Description of the Parameter
    */
   public void handleMessage(EBMessage msg) {
      //if (msg instanceof ViewUpdate) {
      //    ViewUpdate vu = (ViewUpdate) msg;
      //    //System.out.println("JRefactory.handleMessage("+vu+")");
      //} else if (msg instanceof EditorExitRequested) {
      //    // Editor is exiting, save info about current project
      //    EditorExitRequested eer = (EditorExitRequested) msg;
      //    //System.out.println("JRefactory.handleMessage("+eer+")");
      //} else if (msg instanceof BufferUpdate) {
      //    BufferUpdate bu = (BufferUpdate) msg;
      //    //System.out.println("JRefactory.handleMessage("+bu+")");
      //}
   }


   /**
    *  Returns the viewer associated with the given view, or null if none exists.
    *
    *@param  view  Description of the Parameter
    *@return       The viewer value
    */
   public static JRefactory getViewer(View view) {
      return (JRefactory)viewers.get(view);
   }


   /**
    *  Adds an action to be shown on the toolbar.
    *
    *@param  action  Description of the Parameter
    */
   public static void registerAction(Action action) {
      actions.add(action);
      actionsChanged();
   }


   /**
    *  Removes an action from the toolbar.
    *
    *@param  action  Description of the Parameter
    */
   public static void unregisterAction(Action action) {
      actions.remove(action);
      actionsChanged();
   }


   /**
    *  Add a listener for the instance of project viewer of the given view. If the given view is null, the listener will
    *  be called from all instances. <p>
    *
    *  Additionally, for listeners that are registered for all views, a JRefactoryEvent is fired when a different view
    *  is selected.</p>
    *
    *@param  lstnr  The listener to add.
    *@param  view   The view that the lstnr is attached to, or <code>null</code> if the listener wants to be called from
    *      all views.
    */
   public static void addJRefactoryListener(JRefactoryListener lstnr, View view) {
      ArrayList lst = (ArrayList)listeners.get(view);
      if (lst == null) {
         lst = new ArrayList();
         listeners.put(view, lst);
      }
      lst.add(lstnr);
   }


   /**
    *  Remove the listener from the list of listeners for the given view. As with the {@link
    *  #addJRefactoryListener(JRefactoryListener, View) add} method, <code>view</code> can be <code>null</code>.
    *
    *@param  lstnr  Description of the Parameter
    *@param  view   Description of the Parameter
    */
   public static void removeJRefactoryListener(JRefactoryListener lstnr, View view) {
      ArrayList lst = (ArrayList)listeners.get(view);
      if (lst != null) {
         lst.remove(lstnr);
      }
   }


   /**
    *  Fires an event for the loading of a project. Notify all the listeners registered for this instance's view and
    *  listeners registered for all views. <p>
    *
    *  If the view provided is null, only the listeners registered for the null View will receive the event.</p>
    *
    *@param  src  Description of the Parameter
    *@param  p    The activated project.
    *@param  v    The view where the change occured, or null.
    */
   public static void fireProjectLoaded(Object src, UMLProject p, View v) {
      JRefactoryEvent evt;
      if (src instanceof JRefactory) {
         evt = new JRefactoryEvent((JRefactory)src, p);
      } else {
         JRefactory viewer = (JRefactory)viewers.get(v);
         evt = new JRefactoryEvent(src, p);
      }

      ArrayList lst;
      if (v != null) {
         lst = (ArrayList)listeners.get(v);
         if (lst != null) {
            for (Iterator i = lst.iterator(); i.hasNext(); ) {
               ((JRefactoryListener)i.next()).projectLoaded(evt);
            }
         }
      }

      lst = (ArrayList)listeners.get(null);
      if (lst != null) {
         for (Iterator i = lst.iterator(); i.hasNext(); ) {
            ((JRefactoryListener)i.next()).projectLoaded(evt);
         }
      }
   }


   /**
    *  Fires a "project added" event. All listeners, regardless of the view, are notified of this event.
    *
    *@param  src  Description of the Parameter
    *@param  p    Description of the Parameter
    */
   public static void fireProjectAdded(Object src, UMLProject p) {
      HashSet notify = new HashSet();
      for (Iterator i = listeners.values().iterator(); i.hasNext(); ) {
         notify.addAll((ArrayList)i.next());
      }

      JRefactoryEvent evt = new JRefactoryEvent(src, p);
      for (Iterator i = notify.iterator(); i.hasNext(); ) {
         ((JRefactoryListener)i.next()).projectAdded(evt);
      }
   }


   /**
    *  Fires a "project removed" event. All listeners, regardless of the view, are notified of this event.
    *
    *@param  src  Description of the Parameter
    *@param  p    Description of the Parameter
    */
   public static void fireProjectRemoved(Object src, UMLProject p) {
      HashSet notify = new HashSet();
      for (Iterator i = listeners.values().iterator(); i.hasNext(); ) {
         notify.addAll((ArrayList)i.next());
      }

      JRefactoryEvent evt = new JRefactoryEvent(src, p);
      for (Iterator i = notify.iterator(); i.hasNext(); ) {
         ((JRefactoryListener)i.next()).projectRemoved(evt);
      }
   }


   /**  Reloads the action list for the toolbar. */
   private static void actionsChanged() {
      for (Iterator it = viewers.values().iterator(); it.hasNext(); ) {
         JRefactory v = (JRefactory)it.next();
      }
   }


   /**
    *  Description of the Class
    *
    *@author     <a href="mailto:JRefactoryPlugin@ladyshot.demon.co.uk">Mike Atkinson</a>
    *@created    23 July 2003
    *@version    $Id: JRefactory.java,v 1.4 2003/09/25 13:30:36 mikeatkinson Exp $
    *@since      0.1.0
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

   static {
      actions.add(new ShowAction());
      actions.add(new HideAction());
      actions.add(new ReloadAction());
      actions.add(new ReloadAllAction());
   }

}

