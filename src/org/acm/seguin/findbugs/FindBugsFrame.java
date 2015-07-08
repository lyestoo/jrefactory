/*
    FindBugs - Find bugs in Java programs
    Copyright (C) 2003, University of Maryland
    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.
    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.
    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
  */
/*
    FindBugsFrame.java
    Created on March 30, 2003, 12:05 PM
  */
package org.acm.seguin.findbugs;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.BufferedReader;
import java.io.File;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import javax.swing.text.*;
import javax.swing.tree.*;
import edu.umd.cs.daveho.ba.SourceFinder;
import edu.umd.cs.findbugs.*;
import edu.umd.cs.findbugs.gui.*;
import org.acm.seguin.ide.common.IDEInterface;



import org.acm.seguin.ide.common.IDEPlugin;

/**
 *  The main GUI frame for FindBugs.
 *
 *@author    David Hovemeyer
 */
public class FindBugsFrame extends edu.umd.cs.findbugs.gui.FindBugsFrame {
   private static Frame view = null;

   private static ConsoleLogger consoleLogger = null;


   /**
    *  Gets the Logger attribute of the FindBugsFrame object
    *
    *@return    The Logger value
    */
   public edu.umd.cs.findbugs.gui.ConsoleLogger getLogger() {
      return consoleLogger;
   }


   /**
    *  Write a message to the console window.
    *
    *@param  message  Description of Parameter
    */
   public void writeToConsole(String message) {
      System.out.println(message);
      super.writeToConsole(message);
   }


   /**
    *  Show an error dialog.
    *
    *@param  message  Description of Parameter
    */
   public void error(String message) {
      JOptionPane.showMessageDialog(view, message, "Error", JOptionPane.ERROR_MESSAGE);
   }


   /**
    *  Gets the AWTFrame attribute of the FindBugsFrame object
    *
    *@return    The AWTFrame value
    */
   protected Frame getAWTFrame() {
      return view;
   }



   /**
    *  Update the source view window.
    *
    *@param  project          the project (containing the source directories to search)
    *@param  analysisRun      the analysis run (containing the mapping of classes to source files)
    *@param  srcLine          the source line annotation (specifying source file to load and which lines to highlight)
    *@param  selected         Description of Parameter
    *@return                  true if the source was shown successfully, false otherwise
    *@exception  IOException  Description of Exception
    */
   protected boolean viewSource(Project project, AnalysisRun analysisRun, final SourceLineAnnotation srcLine, BugInstance selected) throws IOException {
      IDEPlugin.log(IDEInterface.DEBUG, this, "srcLine=" + srcLine);
      IDEPlugin.log(IDEInterface.DEBUG, this, "selected=" + selected);
      SourceFinder sourceFinder = new SourceFinder();
      sourceFinder.setSourceBaseList(project.getSourceDirList());
      String sourceFileName = null;
      String packageName = (srcLine == null) ? "" : srcLine.getPackageName();
      IDEPlugin.log(IDEInterface.DEBUG, this, "packageName=" + packageName);
      if (srcLine != null) {
         sourceFileName = srcLine.getSourceFile();
      } else if (selected != null) {
         ClassAnnotation primaryClass = selected.getPrimaryClass();
         sourceFileName = primaryClass.getClassName();
         IDEPlugin.log(IDEInterface.DEBUG, this, "sourceFileName=" + sourceFileName);
         sourceFileName = sourceFileName.replace('.', File.separatorChar);
         int x = sourceFileName.indexOf("$");
         IDEPlugin.log(IDEInterface.DEBUG, this, "sourceFileName=" + sourceFileName + ", x=" + x);
         if (x > 0) {
            sourceFileName = sourceFileName.substring(0, x);
         }
         x = sourceFileName.lastIndexOf("java") - 1;
         IDEPlugin.log(IDEInterface.DEBUG, this, "sourceFileName=" + sourceFileName + ", x=" + x);
         if (x > 0) {
            sourceFileName = sourceFileName.substring(0, x);
         }
         IDEPlugin.log(IDEInterface.DEBUG, this, "sourceFileName=" + sourceFileName);
         sourceFileName = sourceFileName + ".java";
         IDEPlugin.log(IDEInterface.DEBUG, this, "sourceFileName=" + sourceFileName);
      } else {
         return super.viewSource(project, analysisRun, srcLine, selected);
      }

      IDEPlugin.log(IDEInterface.DEBUG, this, "sourceFileName=" + sourceFileName);
      if (sourceFileName == null || sourceFileName.equals("<Unknown>")) {
         getLogger().logMessage(ConsoleLogger.WARNING, "No source file for class " + srcLine.getClassName());
         return super.viewSource(project, analysisRun, srcLine, selected);
      }

      File sourceFile = sourceFinder.findSource(packageName, sourceFileName);
      IDEPlugin.log(IDEInterface.DEBUG, this, "sourceFile=" + sourceFile);
      // Try to open the source file and display its contents
      // in the source text area.
      if (sourceFile != null) {
         //Buffer buffer = jEdit.openFile(view, sourceFile.getAbsolutePath());
         Object buffer = IDEPlugin.openFile(view, sourceFile.getAbsolutePath());
         IDEPlugin.goToBuffer(view, buffer);

         if (!srcLine.isUnknown()) {
            int start = 0;
            int end = 0;

            int lineNo = srcLine.getStartLine() - 1;
            int endLine = srcLine.getEndLine();
            IDEPlugin.log(IDEInterface.DEBUG, this, "startLine=" + lineNo + ", endLine=" + endLine);
            if (lineNo >= 0 && lineNo < IDEPlugin.getLineCount(buffer)) {
               start += IDEPlugin.getLineStartOffset(buffer, lineNo);
            }
            if (endLine >= 0 && endLine < IDEPlugin.getLineCount(buffer)) {
               if (end == 0) {
                  end = IDEPlugin.getLineEndOffset(buffer, endLine) - 1;
               } else {
                  end += IDEPlugin.getLineStartOffset(buffer, endLine);
               }
            }

            IDEPlugin.log(IDEInterface.DEBUG, this, "start=" + start + ", end=" + end);
            IDEPlugin.setSelection(view, buffer, start, end);

            IDEPlugin.moveCaretPosition(view, buffer, end);
         }
      }

      return super.viewSource(project, analysisRun, srcLine, selected);
   }


   /**
    *  Description of the Method
    *
    *@param  aView  Description of Parameter
    *@return        Description of the Returned Value
    */
   public static JRootPane createFindBugsPanel(Frame aView) {
      view = aView;
      // Load plugins!

      System.setProperty("findbugs.debug", "true");
      System.setProperty("findbugs.home", "true");
      FindBugsFrame frame = new FindBugsFrame();
      //frame.setSize(800, 600);
      //frame.show();
      //frame.hide();
      consoleLogger = new ConsoleLogger(frame);
      return frame.getRootPane();
   }


   /**
    *  Description of the Class
    *
    *@author    Mike Atkinson
    */
   public static class ConsoleLogger extends edu.umd.cs.findbugs.gui.ConsoleLogger {

      private edu.umd.cs.findbugs.gui.FindBugsFrame frame;


      /**
       *  Creates a new instance of ConsoleLogger
       *
       *@param  frame  Description of Parameter
       */
      public ConsoleLogger(edu.umd.cs.findbugs.gui.FindBugsFrame frame) {
         super(frame);
         this.frame = frame;
      }


      /**
       *  Description of the Method
       *
       *@param  severity  Description of Parameter
       *@param  message   Description of Parameter
       */
      public void logMessage(int severity, String message) {
         switch (severity) {
          case INFO:
             IDEPlugin.log(IDEInterface.DEBUG, frame, message);
             break;
          case WARNING:
             IDEPlugin.log(IDEInterface.WARNING, frame, message);
             break;
          case ERROR:
             IDEPlugin.log(IDEInterface.ERROR, frame, message);
             break;
          default:
             IDEPlugin.log(IDEInterface.ERROR, frame, message);
             break;
         }
      }

   }
}

