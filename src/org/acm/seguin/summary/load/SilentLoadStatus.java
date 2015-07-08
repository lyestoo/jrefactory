/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.summary.load;

/**
 *  Reports nothing to the user during loading
 *
 *@author    Chris Seguin
 */
public class SilentLoadStatus implements LoadStatus {
	/**
	 *  Sets the Root attribute of the LoadStatus object
	 *
	 *@param  name  The new Root value
	 */
	public void setRoot(String name) {
	}


	/**
	 *  Sets the CurrentFile attribute of the LoadStatus object
	 *
	 *@param  name  The new CurrentFile value
	 */
	public void setCurrentFile(String name) {
	}


	/**
	 *  Completed the loading
	 */
	public void done() {
	}
}
