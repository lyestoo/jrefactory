/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.ide.jbuilder.refactor;

import com.borland.jbuilder.node.JavaFileNode;
import com.borland.primetime.node.Node;
import java.awt.event.ActionEvent;
import java.io.IOException;
import org.acm.seguin.ide.common.action.MoveClassAction;
import org.acm.seguin.ide.jbuilder.JBuilderAction;
import org.acm.seguin.summary.FileSummary;
import org.acm.seguin.summary.TypeSummary;
import org.acm.seguin.uml.refactor.AddMoveClassListener;

/**
 *  Moves the class
 *
 *@author    Chris Seguin
 */
public class JBuilderMoveClassAction extends MoveClassAction {
	/**
	 *  Constructor for the MoveClassAction object
	 *
	 *@param  init  Description of Parameter
	 */
	public JBuilderMoveClassAction(Node[] init)
	{
		super(new JBuilderSelectedFileSet(init));
	}
}
