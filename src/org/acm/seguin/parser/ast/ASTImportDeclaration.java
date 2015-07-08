/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.parser.ast;

import org.acm.seguin.parser.JavaParserVisitor;
import org.acm.seguin.parser.JavaParser;

/**
 *  Stores an import declaration that appears at the beginning of
 *  a java file.
 *
 *@author     Chris Seguin
 *@author     Mike Atkinson
 *@created    October 13, 1999
 */
public class ASTImportDeclaration extends SimpleNode {
	//  Instance Variables
	private boolean importPackage;
	private boolean staticImport;   // JDK 1.5


	/**
	 *  Constructor for the ASTImportDeclaration object
	 *
	 *@param  id  Description of Parameter
	 */
	public ASTImportDeclaration(int id) {
		super(id);
	}


	/**
	 *  Constructor for the ASTImportDeclaration object
	 *
	 *@param  p   Description of Parameter
	 *@param  id  Description of Parameter
	 */
	public ASTImportDeclaration(JavaParser p, int id) {
		super(p, id);
	}


	/**
	 *  Set when including everything in a package
	 *
	 *@param  way  whether we are importing the package
	 */
	public void setImportPackage(boolean way) {
		importPackage = way;
	}


	/**
	 *  Return whether we are importing a package
	 *
	 *@return    true if we are importing a package
	 */
	public boolean isImportingPackage() {
		return importPackage;
	}

        
	/**
	 *  Set when including everything in a package.
         *
         * JDK 1.5 indicate that this is a static import.
	 *
	 *@param  isStatic  whether we are importing statics from the package
         *@since     JRefactory 2.7.00
	 */
        public void setStaticImport(boolean isStatic) {
            staticImport = isStatic;
        }
        

	/**
	 *  Return whether we are importing a the static from this package.
	 *
         * JDK 1.5
         *
	 *@return    true if we are importing statics from the package
         *@since     JRefactory 2.7.00
	 */
        public boolean isStaticImport() {
            return staticImport;
        }
        
        

	/**
	 *  Accept the visitor.
	 *
	 *@param  visitor  Description of Parameter
	 *@param  data     Description of Parameter
	 *@return          Description of the Returned Value
	 */
	public Object jjtAccept(JavaParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}


    public boolean isImportOnDemand() {
        return importPackage;
    }

    public ASTName getImportedNameNode() {
        return (ASTName) jjtGetFirstChild();
    }

}
