package org.acm.seguin.ide.jedit;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import errorlist.DefaultErrorSource;

import org.acm.seguin.awt.ExceptionPrinter;
import org.acm.seguin.ide.common.options.PropertiesFile;
import org.acm.seguin.parser.ParseException;
import org.acm.seguin.parser.Token;
import org.acm.seguin.parser.ast.ASTCompilationUnit;
import org.acm.seguin.parser.factory.BufferParserFactory;
import org.acm.seguin.pretty.PrettyPrintFile;
import org.acm.seguin.project.Project;
import org.gjt.sp.jedit.*;
import org.gjt.sp.jedit.io.VFSManager;
import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.gjt.sp.util.Log;


// note: JEditPrettyPrinter is _not_ derived from PrettyPrintFromIDE
// (like it should be), because then we cannot handle the parse
// exceptions ourselves.
/**
 *  Description of the Class
 *
 * @author     Mike Atkinson (Mike)
 * @created    14 July 2003
 * @since      1.0
 */
public class JEditPrettyPrinter extends PrettyPrintFile implements Runnable {
   private JavaStyleExceptionPrinter exceptionPrinter = new JavaStyleExceptionPrinter();
   private Buffer buffer;
   private Writer writer;
   private JavaStylePlugin jsPlugin;

   private View view;
   private static Throwable exception = null;


   /**
    *  Constructor for the JEditPrettyPrinter object
    *
    * @param  jsPlugin  Description of Parameter
    * @param  view      Description of Parameter
    * @param  buffer    Description of Parameter
    */
   public JEditPrettyPrinter(JavaStylePlugin jsPlugin, View view, Buffer buffer) {
      super();
      super.setAsk(false);
      //Log.log(Log.DEBUG, this, "JEditPrettyPrinter()");
      this.jsPlugin = jsPlugin;
      this.view = view;
      this.buffer = buffer;
      this.writer = new StringWriter();
      JavaStylePlugin.getErrorSource().clear();
      ExceptionPrinter.register(exceptionPrinter);
   }


   /**  Main processing method for the JEditPrettyPrinter object */
   public void run() {
      Log.log(Log.DEBUG, this, "formatting the buffer...");

      int caretPos = 0;
      JEditTextArea textarea = null;

      try {
         if (view != null) {
            view.showWaitCursor();
            textarea = view.getTextArea();
            caretPos = textarea.getCaretPosition();
         }

         setProjectData(view, buffer);
         setSettings();

         // set settings from jEdit
         // get text string
         String before = buffer.getText(0, buffer.getLength());
         setInputString(before);

         // remember and remove all markers:
         Vector markers = (Vector)buffer.getMarkers().clone();

         apply(null);

         // JRefactory reformat (may cause exception to be set).
         String contents = getOutputBuffer();

         // get the new contents back
         if (exception != null) {
            exception.printStackTrace();
            // the JRefactory reformatting caused an exception, so handle it.
            if (exception instanceof ParseException) {
               Token currentToken = ((ParseException)exception).currentToken;

               //if (error != null) {
               JavaStylePlugin.getErrorSource().clear();
               JavaStylePlugin.getErrorSource().addError(DefaultErrorSource.ERROR,
                     buffer.getPath(),
                     currentToken.next.beginLine - 1,
                     currentToken.next.beginColumn - 1,
                     currentToken.next.endColumn,
                     exception.getMessage());
            }
            else {
               exception.printStackTrace();
               // no idea what caused this exception
               Log.log(Log.ERROR, this, exception);
            }
            exception = null;

         }
         else if (contents == null || contents.length() == 0)
            // we did not get an exception but JRefactory still did not produce valid contents
            GUIUtilities.error(view, "javastyle.error.other", null);

         else if (!contents.equals(before))
            // The JRefactory reformatting caused a change in the text so perform the edit.
            try {
               buffer.beginCompoundEdit();
               if (markers.size() > 0)
                  buffer.removeAllMarkers();

               buffer.remove(0, buffer.getLength());
               buffer.insert(0, contents);

               // restore markers:
               Enumeration enum = markers.elements();

               while (enum.hasMoreElements()) {
                  Marker marker = (Marker)enum.nextElement();

                  buffer.addMarker(marker.getShortcut(), marker.getPosition());
               }
               // restore caret position
               if (textarea != null) {
                  int bufLen = textarea.getBufferLength();

                  textarea.setCaretPosition(caretPos > bufLen ? bufLen : caretPos);
                  textarea.scrollToCaret(true);
               }
            } catch (Exception ex) {
               ex.printStackTrace();
               Log.log(Log.ERROR, this, ex);
            } finally {
               buffer.endCompoundEdit();
            }

      } catch (Throwable ex) {
         ex.printStackTrace();
         Log.log(Log.ERROR, this, ex);
         GUIUtilities.error(view, "javastyle.error.parse", new Object[]{ex.toString()});
      } finally {
         if (view != null)
            view.hideWaitCursor();

      }
   }


   /**
    *  Sets the InputString attribute of the JEditPrettyPrinter object
    *
    * @param  input  The new InputString value
    */
   protected void setInputString(String input) {
      if (input == null)
         return;
      setParserFactory(new BufferParserFactory(input));
   }


   /**
    *  Gets the OutputBuffer attribute of the JEditPrettyPrinter object
    *
    * @return    The OutputBuffer value
    */
   protected String getOutputBuffer() {
      return writer.toString();
   }


   /**
    *  Gets the OutputStream attribute of the JEditPrettyPrinter object
    *
    * @param  file  Description of Parameter
    * @return       The Writer value
    */
   protected Writer getWriter(File file) {
      return writer;
   }


   /**
    *  After we have applied the pretty printing
    *
    * @param  inputFile  Description of Parameter
    * @param  root       Description of Parameter
    */
   protected void postApply(File inputFile, ASTCompilationUnit root) {
      PropertiesFile props = jsPlugin.getProperties("pretty", jsPlugin.getProjectName(view, buffer));
      if (props.getBoolean("checkOnSave", false))
         //VFSManager.runInAWTThread(
         //   new Runnable() {
         //      public void run() {
         jsPlugin.instanceCheck(view, buffer, true);
      //      }
      //   });

   }


   /**  Sets the Settings attribute of the JEditPrettyPrinter object */
   private void setSettings() {
      // determine new settings:
      boolean noTabs = buffer.getMode().getBooleanProperty("noTabs");
      Object indentSizeProp = buffer.getMode().getProperty("indentSize");
      String indentSize = "4";

      if (indentSizeProp != null)
         indentSize = indentSizeProp.toString();

      // set new settings:
      JavaStylePlugin.setProperty("indent.char", noTabs ? "space" : "tab");
      JavaStylePlugin.setProperty("indent", noTabs ? indentSize : "1");
      JavaStylePlugin.setProperty("end.line", "NL");

      // save settings:
      jsPlugin.saveProperties();
   }


   /**
    *  Sets the projectData attribute of the JEditPrettyPrinter object
    *
    * @param  view    The new ProjectData value
    * @param  buffer  The new ProjectData value
    */
   private static void setProjectData(View view, Buffer buffer) {
      try {
         String path = buffer.getPath();
         Class clazz = Class.forName("projectviewer.ProjectViewer");
         //projectviewer.ProjectViewer viewer = projectviewer.ProjectViewer.getViewer(view);
         projectviewer.ProjectManager manager = projectviewer.ProjectManager.getInstance();
         //List projs = new ArrayList();

         for (Iterator i = manager.getProjects(); i.hasNext(); ) {
            projectviewer.vpt.VPTProject proj = (projectviewer.vpt.VPTProject)i.next();

            if (proj.isProjectFile(path)) {
               Project project = Project.getProject(proj.getName());

               if (project == null)
                  project = org.acm.seguin.project.Project.createProject(proj.getName());

               org.acm.seguin.project.Project.setCurrentProject(project);
               break;
            }
         }
      } catch (ClassNotFoundException e) {
      }
   }


   /**
    *  Prints to stdout and includes a dialog box
    *
    * @author     Chris Seguin
    * @created    December 6, 2001
    * @since      2.6.33
    */
   private class JavaStyleExceptionPrinter extends ExceptionPrinter {
      /**
       *  Prints exceptions
       *
       * @param  exc          Description of Parameter
       * @param  interactive  Description of Parameter
       * @since               2.6.33
       */
      public void printException(Throwable exc, boolean interactive) {
         exception = exc;
      }
   }

}

