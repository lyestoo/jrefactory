/*
 * Author:  Chris Seguin
 *
 * This software has been developed under the copyleft
 * rules of the GNU General Public License.  Please
 * consult the GNU General Public License for more
 * details about use and distribution of this software.
 */
package org.acm.seguin.refactor;

import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import org.acm.seguin.parser.ast.SimpleNode;
import org.acm.seguin.pretty.PrettyPrintFile;
import org.acm.seguin.refactor.undo.UndoAction;
import org.acm.seguin.version.VersionControl;
import org.acm.seguin.version.VersionControlFactory;
import org.acm.seguin.awt.ExceptionPrinter;
import org.acm.seguin.parser.factory.FileParserFactory;
import org.acm.seguin.parser.factory.ParserFactory;

/**
 *  Base class for a program that reads in an abstract syntax tree, transforms
 *  the code, and rewrites the file to disk.
 *
 *@author    Chris Seguin
 */
public interface ComplexTransform  {

        public void setUndoAction(UndoAction init);

	/**
	 *  Adds a syntax tree transformation
	 *
	 *@param  value  Description of Parameter
	 */
	public void add(TransformAST value);


	/**
	 *  Clears all the transforms
	 */
	public void clear();


	/**
	 *  Is it worth applying the transforms
	 *
	 *@return    true if there is any
	 */
	public boolean hasAnyChanges();


	/**
	 *  Given a file, applies a set of transformations to it
	 *
	 *@param  inputFile   Description of Parameter
	 *@param  outputFile  Description of Parameter
	 */
	public void apply(File inputFile, File outputFile);


	/**
	 *  Creates a new file
	 *
	 *@param  file  Description of Parameter
	 */
	public void createFile(File file);


	/**
	 *  Removes an old file
	 *
	 *@param  file  Description of Parameter
	 */
	public void removeFile(File file);
}
