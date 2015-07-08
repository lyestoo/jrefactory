/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.uml.refactor;

import java.awt.event.ActionEvent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import org.acm.seguin.refactor.Refactoring;
import org.acm.seguin.refactor.RefactoringException;
import org.acm.seguin.refactor.RefactoringFactory;
import org.acm.seguin.refactor.method.PushUpMethodRefactoring;
import org.acm.seguin.summary.MethodSummary;
import org.acm.seguin.summary.TypeSummary;
import org.acm.seguin.uml.PopupMenuListener;
import org.acm.seguin.uml.UMLPackage;
import org.acm.seguin.uml.loader.ReloaderSingleton;

/**
 *  Pushes a field into the parent class
 *
 *@author    Chris Seguin
 */
public class PushUpMethodListener extends NoInputRefactoringListener
{
	private MethodSummary methodSummary;


	/**
	 *  Constructor for the PushUpMethodListener object
	 *
	 *@param  initPackage  the UML package that is being operated on
	 *@param  initMenu     The popup menu
	 *@param  initItem     The current item
	 *@param  initMethod   The method
	 */
	public PushUpMethodListener(UMLPackage initPackage,
			MethodSummary initMethod,
			JPopupMenu initMenu, JMenuItem initItem)
	{
		super(initPackage, initMenu, initItem);
		methodSummary = initMethod;
	}


	/**
	 *  Creates a refactoring to be performed
	 *
	 *@return    the refactoring
	 */
	protected Refactoring createRefactoring()
	{
		PushUpMethodRefactoring puff = RefactoringFactory.get().pushUpMethod();
		puff.setMethod(methodSummary);
		return puff;
	}
}
