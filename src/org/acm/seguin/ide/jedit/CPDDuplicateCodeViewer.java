package org.acm.seguin.ide.jedit;

import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import org.acm.seguin.ide.common.IDEInterface;

import org.acm.seguin.ide.common.IDEPlugin;

import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.io.VFSManager;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.textarea.Selection;

/**
 *  A GUI Component to display Duplicate code.
 *
 *@author     Jiger Patel
 *@created    05 Apr 2003
 */

public class CPDDuplicateCodeViewer extends JPanel {
   JTree tree;
   DefaultTreeModel treeModel = new DefaultTreeModel(new DefaultMutableTreeNode("CPD Results", true));
   final View view;


   /**
    *  Constructor for the CPDDuplicateCodeViewer object
    *
    *@param  aView  Description of Parameter
    */
   public CPDDuplicateCodeViewer(View aView) {
      this.view = aView;
      setLayout(new BorderLayout());
      JPanel top = new JPanel();
      JButton currentFile = new JButton("In Current File");
      currentFile.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               try {
                  //org.acm.seguin.ide.jedit.JavaStylePlugin.cpdCurrentFile(view);
                  IDEPlugin.cpdBuffer(view, null);
               } catch (java.io.IOException ex) {
                  IDEPlugin.log(IDEInterface.ERROR, "CPDDuplicateCodeViewer", ex.getMessage());
               }
            }
         });

      JButton currentDir = new JButton("In Current Directory");
      currentDir.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               try {
                  //org.acm.seguin.ide.jedit.JavaStylePlugin.cpdDir(view, false);
                  IDEPlugin.cpdDir(view, false);
               } catch (java.io.IOException ex) {
                  IDEPlugin.log(IDEInterface.ERROR, "CPDDuplicateCodeViewer", ex.getMessage());
               }
            }
         });

      JButton currentDirRecurse = new JButton("In Current Directory recursively");
      currentDirRecurse.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               try {
                  //org.acm.seguin.ide.jedit.JavaStylePlugin.cpdDir(view, true);
                  IDEPlugin.cpdDir(view, true);
               } catch (java.io.IOException ex) {
                  IDEPlugin.log(IDEInterface.ERROR, "CPDDuplicateCodeViewer", ex.getMessage());
               }
            }
         });

      top.add(currentFile);
      top.add(currentDir);
      top.add(currentDirRecurse);
      add(top, BorderLayout.NORTH);

      tree = new JTree(treeModel);
      tree.getSelectionModel().setSelectionMode
            (TreeSelectionModel.SINGLE_TREE_SELECTION);
      tree.addTreeSelectionListener(
         new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
               DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

               if (node != null) {
                  //System.out.println("Node is " + node +" class "+ node.getClass());
                  if (node.isLeaf() && node instanceof Duplicate) {
                     Duplicate duplicate = (Duplicate) node;

                     gotoDuplicate(duplicate);
                     //System.out.println("Got!! " + duplicate);
                  }
               }
            }
         });

      add(new JScrollPane(tree));

   }


   /**
    *  Gets the root attribute of the CPDDuplicateCodeViewer object
    *
    *@return    The root value
    */
   public DefaultMutableTreeNode getRoot() {
      return (DefaultMutableTreeNode) treeModel.getRoot();
   }


   /**  Description of the Method */
   public void refreshTree() {
      treeModel.reload();
   }


   /**
    *  Description of the Method
    *
    *@param  duplicate  Description of Parameter
    */
   public void gotoDuplicate(final Duplicate duplicate) {
      if (duplicate != null) {
         try {
            //final Buffer buffer = jEdit.openFile(view.getView(), duplicate.getFilename());
            final Object buffer = IDEPlugin.openFile(view, duplicate.getFilename());
   
            VFSManager.runInAWTThread(
               new Runnable() {
                  public void run() {
                     //view.getView().setBuffer(buffer);
                     IDEPlugin.setBuffer(view, buffer);
   
                     //int start = buffer.getLineStartOffset(duplicate.getBeginLine());
                     //int end = buffer.getLineEndOffset(duplicate.getEndLine() - 2);
                     int start = IDEPlugin.getLineStartOffset(buffer, duplicate.getBeginLine());
                     int end = IDEPlugin.getLineEndOffset(buffer, duplicate.getEndLine());
   
                     //Log.log(Log.DEBUG, this.getClass(), "Start Line "+ duplicate.getBeginLine() + " End Line "+ duplicate.getEndLine() + " Start " + start + " End "+ end);
                     //Since an AIOOB Exception is thrown if the end is the end of file. we do a -1 from end to fix it.
                     //view.getTextArea().setSelection(new Selection.Range(start, end - 1));
                     IDEPlugin.setSelection(view, buffer, start, end - 1);
                     view.getTextArea().moveCaretPosition(start);
                  }
               });
         } catch (java.io.IOException e) {
         }
      }
   }


   /**
    *  Adds a feature to the Duplicates attribute of the CPDDuplicateCodeViewer object
    *
    *@param  duplicates  The feature to be added to the Duplicates attribute
    */
   public void addDuplicates(Duplicates duplicates) {
      //System.out.println("Inside addDuplicates " + duplicates +" Root child count "+ treeModel.getChildCount(treeModel.getRoot()));
      getRoot().add(duplicates);
   }


   /**  Description of the Method */
   public void expandAll() {
      int row = 0;

      while (row < tree.getRowCount()) {
         tree.expandRow(row);
         row++;
      }
   }


   /**  Description of the Method */
   public void collapseAll() {
      int row = tree.getRowCount() - 1;

      while (row >= 0) {
         tree.collapseRow(row);
         row--;
      }
   }


   /**  Description of the Method */
   public void clearDuplicates() {
      getRoot().removeAllChildren();
   }


   /**
    *  Description of the Class
    *
    *@author     Mike Atkinson
    *@created    September 13, 2003
    */
   public class Duplicates extends DefaultMutableTreeNode {
      List vecduplicate = new ArrayList();
      String message, sourcecode;


      /**
       *  Constructor for the Duplicates object
       *
       *@param  message     Description of Parameter
       *@param  sourcecode  Description of Parameter
       *@parammessage       Description of Parameter
       *@paramsourcecode    Description of Parameter
       *@parammessage       Description of Parameter
       *@paramsourcecode    Description of Parameter
       *@parammessage       Description of Parameter
       *@paramsourcecode    Description of Parameter
       *@parammessage       Description of the Parameter
       *@paramsourcecode    Description of the Parameter
       */
      public Duplicates(String message, String sourcecode) {
         this.message = message;
         this.sourcecode = sourcecode;
      }


      /**
       *  Gets the sourceCode attribute of the Duplicates object
       *
       *@return    The sourceCode value
       */
      public String getSourceCode() {
         return sourcecode;
      }


      /**
       *  Adds a feature to the Duplicate attribute of the Duplicates object
       *
       *@param  duplicate  The feature to be added to the Duplicate attribute
       *@paramduplicate    The feature to be added to the Duplicate attribute
       *@paramduplicate    The feature to be added to the Duplicate attribute
       *@paramduplicate    The feature to be added to the Duplicate attribute
       *@paramduplicate    The feature to be added to the Duplicate attribute
       */
      public void addDuplicate(Duplicate duplicate) {
         add(duplicate);
      }


      /**
       *  Description of the Method
       *
       *@return    Description of the Return Value
       */
      public String toString() {
         return message;
      }
   }


   /**
    *  Description of the Class
    *
    *@author     Mike Atkinson
    *@created    September 13, 2003
    */
   public class Duplicate extends DefaultMutableTreeNode {
      private String filename;
      private int beginLine, endLine;


      /**
       *  Constructor for the Duplicate object
       *
       *@param  filename   Description of Parameter
       *@param  beginLine  Description of Parameter
       *@param  endLine    Description of Parameter
       *@paramfilename     Description of Parameter
       *@parambeginLine    Description of Parameter
       *@paramendLine      Description of Parameter
       *@paramfilename     Description of Parameter
       *@parambeginLine    Description of Parameter
       *@paramendLine      Description of Parameter
       *@paramfilename     Description of Parameter
       *@parambeginLine    Description of Parameter
       *@paramendLine      Description of Parameter
       *@paramfilename     Description of the Parameter
       *@parambeginLine    Description of the Parameter
       *@paramendLine      Description of the Parameter
       */
      public Duplicate(String filename, int beginLine, int endLine) {
         this.filename = filename;
         this.beginLine = beginLine;
         this.endLine = endLine;
      }


      /**
       *  Gets the filename attribute of the Duplicate object
       *
       *@return    The filename value
       */
      public String getFilename() {
         return filename;
      }


      /**
       *  Gets the beginLine attribute of the Duplicate object
       *
       *@return    The beginLine value
       */
      public int getBeginLine() {
         return beginLine;
      }


      /**
       *  Gets the endLine attribute of the Duplicate object
       *
       *@return    The endLine value
       */
      public int getEndLine() {
         return endLine;
      }


      /**
       *  Description of the Method
       *
       *@return    Description of the Return Value
       */
      public String toString() {
         return filename + ":" + getBeginLine() + "-" + getEndLine();
      }
   }

}

