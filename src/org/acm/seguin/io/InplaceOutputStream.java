/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.io;

import java.io.*;
import org.acm.seguin.util.FileSettings;

/**
 *  To the user of this object, it appears that the file is written in place.
 *
 *@author    Chris Seguin
 *@date      May 12, 1999
 */
public class InplaceOutputStream extends OutputStream {
	//  Instance Variables
	private File finalDestination;
	private File temporary;
	private FileOutputStream out;


	/**
	 *  Creates an InplaceOutputStream
	 *
	 *@param  dest             the output file location
	 *@exception  IOException  throws an IOException
	 */
	public InplaceOutputStream(File dest) throws IOException {
		finalDestination = dest;

		String parent = dest.getPath();
		if ((parent != null) && attempt(parent + File.separator + "inplace"))
		{
			//  Things have worked out!
		}
		else if (attempt("." + File.separator + "inplace"))
		{
			//  Things have worked out!
		}
		//else if (attempt(System.getProperty("user.home") + File.separator + ".Refactory" + File.separator + "inplace"))
		else if (attempt(new File(FileSettings.getRefactorySettingsRoot(), "inplace").toString()))
		{
		}
		else
		{
			throw new IOException("Unable to create the output file!");
		}
	}


	/**
	 *  Closes the file
	 *
	 *@exception  IOException  throws an IOException
	 */
	public void close() throws IOException {
		if (out == null) {
			return;
		}

		//  Close the file
		out.close();

		//  Copy it inplace
		if (temporary.exists() && (temporary.length() > 0)) {
			(new FileCopy(temporary, finalDestination, false)).run();
		}

		//  Delete the temporary file
		temporary.delete();

		//  Note that we are done
		out = null;
	}


	/**
	 *  Flush the file
	 *
	 *@exception  IOException  throws an IOException
	 */
	public void flush() throws IOException {
		if (out == null) {
			return;
		}

		out.flush();
	}


	/**
	 *  Write a byte to the file
	 *
	 *@param  b                the byte to be written
	 *@exception  IOException  throws an IOException
	 */
	public void write(int b) throws IOException {
		if (out == null) {
			return;
		}

		out.write(b);
	}


	/**
	 *  Write a byte array to the file
	 *
	 *@param  b                the byte array to be written
	 *@exception  IOException  throws an IOException
	 */
	public void write(byte b[]) throws IOException {
		if (out == null) {
			return;
		}

		out.write(b);
	}


	/**
	 *  Write a byte array to the file
	 *
	 *@param  b                the byte array to be written
	 *@param  off              the offset into the array
	 *@param  len              the number of bytes to write
	 *@exception  IOException  throws an IOException
	 */
	public void write(byte b[], int off, int len) throws IOException {
		if (out == null) {
			return;
		}

		out.write(b, off, len);
	}


	/**
	 *  Make sure to clean up after itself
	 */
	protected void finalize() {
		if (out == null) {
			return;
		}

		try {
			close();
		}
		catch (IOException ioe) {
		}
	}


	/**
	 *  Description of the Method
	 *
	 *@param  prefix  Description of Parameter
	 *@param  suffix  Description of Parameter
	 *@return         Description of the Returned Value
	 */
	private File createTempFile(String prefix, String suffix) {
		for (int ndx = 0; ndx < 1024; ndx++) {
			double number = Math.random() * 1024 * 1024;
			long rounded = Math.round(number);

			File possible = new File(prefix + rounded + suffix);
			if (!possible.exists()) {
				return possible;
			}
		}

		return null;
	}

/**  Attempts to determine if the file can be used for output
@param filepath the file to open
@return true if it worked
*/
	private boolean attempt(String filepath)
	{
		try {
			temporary = createTempFile(filepath, ".java");
			temporary.delete();
		 if (temporary.exists())
		 	return false;

		out = new FileOutputStream(temporary);
		}
		catch (IOException ioe) {
			return false;
		}
		return true;
	}
}
