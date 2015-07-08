/*
    :tabSize=4:indentSize=4:noTabs=false:
    :folding=explicit:collapseFolds=1:
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

import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.jedit.GUIUtilities;

import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.io.VFSManager;
import org.gjt.sp.util.Log;


/**
 *  A collection of actions accessible through jEdit's Action mechanism, and other utility methods that may be
 *  interesting for interacting with the plugin.
 *
 * @author     <a href="mailto:JRefactoryPlugin@ladyshot.demon.co.uk">Mike Atkinson</a>
 * @created    23 July 2003
 * @version    $Id: JavaStyleActions.java,v 1.3 2003/09/25 13:30:36 mikeatkinson Exp $
 * @since      0.0.1
 */
public final class JavaStyleActions {

   /**
    * @param  view      the view; may be null, if there is no current view
    * @param  buffer    the buffer containing the java source code
    * @param  silently  if true, no error dialogs are shown
    */
   public static void beautify(View view, Buffer buffer, boolean silently) {
      JavaStylePlugin.initJSPlugin();
      JavaStylePlugin.jsPlugin.instanceBeautify(view, buffer, silently);
      /*
          try {
          // does the current buffer contains Java source code?
          String bufferFilename = buffer.getName();
          if (!bufferFilename.toLowerCase().endsWith(".java") &&
          !buffer.getMode().getName().equals("java")) {
          if (!silently) {
          GUIUtilities.error(view, "javastyle.error.noJavaBuffer", null);
          }
          return;
          }
          // is the current buffer editable?
          if (buffer.isReadOnly()) {
          Log.log(Log.DEBUG, JavaStylePlugin.class, "the buffer is read-only: " + buffer);
          if (!silently) {
          GUIUtilities.error(view, "javastyle.error.isNotEditable", null);
          }
          return;
          }
          // run the format routine synchronously on the AWT thread:
          VFSManager.runInAWTThread(new JEditPrettyPrinter(JavaStylePlugin.jsPlugin, view, buffer));
          } catch (Throwable e) {
          e.printStackTrace();
          }
        */
   }


   /**
    *  Description of the Method
    *
    * @param  view  Description of Parameter
    */
   public static void optionDialog(View view) {
      org.acm.seguin.ide.common.IDEPlugin.setPlugin(new JavaStylePlugin());
      new org.acm.seguin.ide.common.options.JSOptionDialog(view);
   }


   /**
    *  check current buffer
    *
    * @param  buffer  Description of Parameter
    * @param  view    Description of Parameter
    */
   public static void check(Buffer buffer, View view) {
      JavaStylePlugin.initJSPlugin();
      JavaStylePlugin.jsPlugin.instanceCheck(view, buffer, false);
   }


   /**
    *  check current directory (of the File System Browser)
    *
    * @param  view  Description of Parameter
    */
   public static void checkDirectory(View view) {
      JavaStylePlugin.initJSPlugin();
      JavaStylePlugin.jsPlugin.instanceCheckDirectory(view, false);
   }


   /**
    *  check all open buffers
    *
    * @param  view  Description of Parameter
    */
   public static void checkAllOpenBuffers(View view) {
      JavaStylePlugin.initJSPlugin();
      JavaStylePlugin.jsPlugin.instanceCheckAllOpenBuffers(view);
   }


   /**
    *  check directory recursively
    *
    * @param  view  Description of Parameter
    */
   public static void checkDirectoryRecursively(View view) {
      JavaStylePlugin.initJSPlugin();
      JavaStylePlugin.jsPlugin.instanceCheckDirectory(view, true);
   }


   /**  clear error list */
   public static void clearErrorList() {
      JavaStylePlugin.initJSPlugin();
      JavaStylePlugin.jsPlugin.instanceClearErrorList();
   }


   /**
    * @param  view  Description of the Parameter
    */
   public static void openUML(View view) {
      JRefactory viewer = JRefactory.getViewer(view);
   }


   /**
    * @param  view  Description of the Parameter
    */
   public static void openAllProjectFiles(View view) {
      JRefactory viewer = JRefactory.getViewer(view);
   }


   /**
    * @param  view  Description of the Parameter
    */
   public static void removeAllProjectFiles(View view) {
      JRefactory viewer = JRefactory.getViewer(view);
   }

}

