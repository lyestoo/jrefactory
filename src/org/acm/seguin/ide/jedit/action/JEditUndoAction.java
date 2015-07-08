/*
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.acm.seguin.ide.jedit.action;

import java.awt.event.ActionEvent;

import java.io.File;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import org.acm.seguin.refactor.undo.FileSet;
import org.acm.seguin.refactor.undo.UndoAction;
import org.acm.seguin.util.FileSettings;
import org.acm.seguin.util.MissingSettingsException;

import org.gjt.sp.jedit.jEdit;

/**
 *  Stores the undo operation. The undo operation consists of a description of the refactoring that was performed to
 *  create this UndoAction and a list of files that have changed. <P>
 *
 *  The files that have changed are indexed files, in that they have an index appended to the name of the file.
 *
 * @author     <a href="mailto:JRefactoryPlugin@ladyshot.demon.co.uk">Mike Atkinson</a>
 * @created    23 July 2003
 * @version    $Id: JEditUndoAction.java,v 1.2 2003/09/25 13:30:36 mikeatkinson Exp $
 * @since      0.1
 */
public class JEditUndoAction extends Action implements UndoAction, Serializable {
   /**
    *  A description of the refactoring
    *
    * @serial    true
    */
   private String description;

   /**
    *  A linked list
    *
    * @serial    true
    */
   private LinkedList list;


   /**  Constructor for the UndoAction object  */
   public JEditUndoAction() {
      //System.out.println("JEditUndoAction()");
      description = "unknown";
      list = new LinkedList();
   }


   /**
    *  Sets the Description attribute of the UndoAction object
    *
    * @param  description  the description of the action
    */
   public void setDescription(String description) {
      //System.out.println("JEditUndoAction.setDescription(" + description + ")");
      this.description = description;
   }


   /**
    *  Returns the text to be shown on the button and/or menu item.
    *
    * @return    The text value
    */
   public String getText() {
      return jEdit.getProperty("javastyle.action.undo");
   }


   /**
    *  Gets the Description attribute of the UndoAction object
    *
    * @return    The Description value
    */
   public String getDescription() {
      //System.out.println("JEditUndoAction.setDescription() => " + description);
      return description;
   }


   /**
    *  Description of the Method
    *
    * @param  ae  Description of the Parameter
    */
   public void actionPerformed(ActionEvent ae) {
      //System.out.println("JEditUndoAction.actionPerformed(" + ae + ")");
      undo();
   }


   /**
    *  Adds a file to this action
    *
    * @param  oldFile  the original file
    * @param  newFile  the new file
    */
   public void add(File oldFile, File newFile) {
      //System.out.println("JEditUndoAction.add(" + oldFile + "," + newFile + ")");
      File dest = null;
      if (oldFile == null) {
         dest = null;
      } else {
         //  Get the parent directory and the name
         File parent = oldFile.getParentFile();
         String name = oldFile.getName();

         //  Find the file's next index location
         dest = findNextStorageSlot(parent, name);

         //  Renames the file
         oldFile.renameTo(dest);
      }

      //System.out.println("  list.add(new FileSet(" + oldFile + "," + dest + "," + newFile + "))");
      list.add(new FileSet(oldFile, dest, newFile));
   }


   /**  Undo the current action  */
   public void undo() {
      //System.out.println("JEditUndoAction.undo()");
      Iterator iter = list.iterator();
      while (iter.hasNext()) {
         ((FileSet)iter.next()).undo();
      }
   }


   /**
    *  Gets the NextName attribute of the UndoAction object
    *
    * @param  name     Description of Parameter
    * @param  index    Description of Parameter
    * @param  pattern  Description of Parameter
    * @return          The NextName value
    */
   private String getNextName(String name, int index, String pattern) {
      StringBuffer buffer = new StringBuffer(name);
      char ch;

      for (int ndx = 0; ndx < pattern.length(); ndx++) {
         ch = pattern.charAt(ndx);
         if (ch == '#') {
            buffer.append(index);
         } else {
            buffer.append(ch);
         }
      }

      return buffer.toString();
   }


   /**
    *  Finds the next slot to store the original file
    *
    * @param  dir   the directory that the original file is contained in
    * @param  name  the name of the original file
    * @return       Description of the Returned Value
    */
   private File findNextStorageSlot(File dir, String name) {
      String pattern = ".#";

      try {
         FileSettings umlBundle = FileSettings.getRefactorySettings("uml");
         umlBundle.setContinuallyReload(true);
         pattern = umlBundle.getString("backup.ext");
      } catch (MissingSettingsException mse) {
         //  The default is fine
      }

      File dest = null;
      int index = 0;
      do {
         dest = new File(dir, getNextName(name, index, pattern));
         index++;
      } while (dest.exists());
      return dest;
   }
}

