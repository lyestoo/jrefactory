/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.pretty.sort;

import java.util.StringTokenizer;
import org.acm.seguin.util.Comparator;
import org.acm.seguin.util.Settings;
import org.acm.seguin.util.MissingSettingsException;

/**
 *  Description of the Class
 *
 *@author     Chris Seguin
 *@created    November 12, 1999
 */
public class MultipleOrdering implements Comparator {
	private Ordering[] ordering;


	/**
	 *  Constructor for the MultipleOrdering object
	 *
	 *@param  settings  Description of Parameter
	 */
	public MultipleOrdering(Settings settings) {
		int count = 0;

		try {
			while (true) {
				settings.getString("sort." + (count + 1));
				count++;
			}
		}
		catch (MissingSettingsException mse) {
			//  Found the end of the loop
		}

		ordering = new Ordering[count];

		load(settings);
	}


	public MultipleOrdering(String[] order) {
		ordering = new Ordering[order.length];

		for (int ndx = 0; ndx < order.length; ndx++) {
			ordering[ndx] = parse(order[ndx]);
		}
	}

	/**
	 *  Description of the Method
	 *
	 *@param  obj1  Description of Parameter
	 *@param  obj2  Description of Parameter
	 *@return       Description of the Returned Value
	 */
	public int compare(Object obj1, Object obj2) {
		for (int ndx = 0; ndx < ordering.length; ndx++) {
			int comp = ordering[ndx].compare(obj1, obj2);
			if (comp != 0) {
				return comp;
			}
		}

		return 0;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  settings  Description of Parameter
	 */
	private void load(Settings settings) {
		for (int ndx = 0; ndx < ordering.length; ndx++) {
			String order = settings.getString("sort." + (ndx + 1));
			ordering[ndx] = parse(order);
		}
	}


	/**
	 *  Description of the Method
	 *
	 *@param  order  Description of Parameter
	 *@return        Description of the Returned Value
	 */
	private Ordering parse(String order) {
		StringTokenizer tok = new StringTokenizer(order, "()");

		String name = tok.nextToken();
		String args = tok.nextToken();

		if (name.equals("Type")) {
			return new TypeOrder(args);
		}

		if (name.equals("Class")) {
			return new StaticOrder(args);
		}

		if (name.equals("Protection")) {
			return new ProtectionOrder(args);
		}

		if (name.equals("Method")) {
			return new SetterGetterOrder(args);
		}

		return null;
	}
}
