/*
 * Author:  Chris Seguin
 *
 * This software has been developed under the copyleft
 * rules of the GNU General Public License.  Please
 * consult the GNU General Public License for more
 * details about use and distribution of this software.
 */
package org.acm.seguin.refactor.undo;

import java.io.File;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Stack;
import org.acm.seguin.refactor.Refactoring;
import org.acm.seguin.uml.loader.ReloaderSingleton;
import org.acm.seguin.util.FileSettings;

/**
 *  The stack of refactorings that we can undo. This stack holds all the
 *  refactorings that have occurred in the system. <P>
 *
 *  This object is a singleton object because we only want one object
 *  responsible for storing the refactorings that can be undone.
 *
 *@author    Chris Seguin
 *@author     <a href="mailto:JRefactory@ladyshot.demon.co.uk">Mike Atkinson</a>
 *@version    $Id: UndoStack.java,v 1.5 2003/09/01 00:25:32 mikeatkinson Exp $
 */
public class UndoStack {
	/**
	 *  The stack that contains the actual elements
	 */
	private Stack stack;
        private Class undoer = DefaultUndoAction.class;

	private static UndoStack singleton;


	/**
	 *  Constructor for the UndoStack object
	 */
	public UndoStack() {
		if (!load()) {
			stack = new Stack();
		}
	}

        /**
         * This sets the class that holds undo actions.
         *
         *  If not called the org.acm.seguin.refactor.undo.DefaultUndoAction class
         * is used. This creates file backups with extensions of incrementing integers.
         *
         * @param undoer a class that implements org.acm.seguin.refactor.undo.UndoAction
         * @throws IllegalArgumentException if the class cannot be instantiated or does not implement UndoAction
         */
        public void setUndoAction(Class undoer) throws IllegalArgumentException {
            try {
                if (!(undoer.newInstance() instanceof UndoAction)) {
                    throw new IllegalArgumentException("the undo class must implement org.acm.seguin.refactor.undo.UndoAction");
                }
            } catch (IllegalAccessException ex) {
                IllegalArgumentException e = new IllegalArgumentException("your UndoAction class cannot be accessed");
                e.initCause(ex);
            } catch (InstantiationException ex) {
                IllegalArgumentException e = new IllegalArgumentException("your UndoAction class must have a zero argument constructor");
                e.initCause(ex);
            }
            this.undoer = undoer;
        }

	/**
	 *  Gets the StackEmpty attribute of the UndoStack object
	 *
	 *@return    The StackEmpty value
	 */
	public boolean isStackEmpty() {
		return stack.isEmpty();
	}


	/**
	 *  Adds a refactoring to the undo stack. You provide the refactoring, this
	 *  method provides the undo action.
	 *
	 *@param  ref  the refactoring about to be performed
	 *@return      an undo action
	 */
	public UndoAction add(Refactoring ref) {
		//UndoAction action = new UndoAction(ref.getDescription());
                try {
                    UndoAction action = (UndoAction)undoer.newInstance();
                    action.setDescription(ref.getDescription());
                    
                    stack.push(action);
  		    return action;
                } catch (IllegalAccessException ex) {
                    // this should not occur as we checked we could instantiate in setUndoAction().
                } catch (InstantiationException ex) {
                    // this should not occur as we checked we could instantiate in setUndoAction().
                }
                return null;
	}


	/**
	 *  Return the top option without removing it from the stack
	 *
	 *@return    the top object
	 */
	public UndoAction peek() {
		return (UndoAction) stack.peek();
	}


	/**
	 *  Lists the undo actions in the stack
	 *
	 *@return    an iterator of undo actions
	 */
	public Iterator list() {
		return stack.iterator();
	}


	/**
	 *  Performs an undo of the top action
	 */
	public void undo() {
		UndoAction action = (UndoAction) stack.pop();
		action.undo();
		ReloaderSingleton.reload();
	}


	/**
	 *  Description of the Method
	 */
	public void done() {
		save();
	}


	/**
	 *  Deletes the undo stack
	 */
	public void delete() {
		File file = getFile();
		file.delete();
		stack = new Stack();
	}


	/**
	 *  Gets the stack file
	 *
	 *@return    The File value
	 */
	private File getFile() {
		return new File(FileSettings.getRefactorySettingsRoot(), "undo.stk");
	}


	/**
	 *  Saves the undo stack to the disk
	 */
	private void save() {
		try {
			File file = getFile();
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file));
			output.writeObject(stack);
			output.flush();
			output.close();
		}
		catch (IOException ioe) {
			ioe.printStackTrace(System.out);
		}
	}


	/**
	 *  Loads the undo stack from the disk
	 *
	 *@return    Description of the Returned Value
	 */
	private boolean load() {
		try {
			File file = getFile();
			ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
			stack = (Stack) input.readObject();
			input.close();

			return true;
		}
		catch (FileNotFoundException fnfe) {
			//  Expected - this is normal the first time
		}
		catch (IOException ioe) {
			ioe.printStackTrace(System.out);
		}
		catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace(System.out);
		}

		return false;
	}


	/**
	 *  Gets the singleton undo operation
	 *
	 *@return    the undo stack for the system
	 */
	public static UndoStack get() {
		if (singleton == null) {
			singleton = new UndoStack();
		}

		return singleton;
	}
}
