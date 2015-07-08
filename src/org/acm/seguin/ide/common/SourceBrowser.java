package org.acm.seguin.ide.common;

import java.io.File;

/**
 *  Base class for source browsing.  This is the generic
 *  base class.
 *
 *@author    Chris Seguin
 */
public abstract class SourceBrowser {
	private static SourceBrowser singleton = null;


	/**
	 *  Determines if the system is in a state where
	 *  it can browse the source code
	 *
	 *@return    true if the source code browsing is enabled
	 */
	public abstract boolean canBrowseSource();


	/**
	 *  Actually browses to the file
	 *
	 *@param  filename  the file
	 *@param  line      the line in the file
	 */
	public abstract void gotoSource(File file, int line);


	/**
	 *  Sets the singleton source browser
	 *
	 *@param  value  the new singleton
	 */
	public static void set(SourceBrowser value) {
		singleton = value;
	}


	/**
	 *  Gets the singleton source browser
	 *
	 *@return    the current source browser
	 */
	public static SourceBrowser get() {
		if (singleton == null) {
			singleton = new NoSourceBrowser();
		}
		return singleton;
	}
}
