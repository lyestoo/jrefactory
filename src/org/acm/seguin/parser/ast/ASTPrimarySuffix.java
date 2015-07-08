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
 *  The primary suffix of the expression
 *
 *@author     Chris Seguin
 *@created    October 13, 1999
 */
public class ASTPrimarySuffix extends SimpleNode {
	//  Instance Variables
	private String name = "";


	/**
	 *  Constructor for the ASTPrimarySuffix object
	 *
	 *@param  id  Description of Parameter
	 */
	public ASTPrimarySuffix(int id) {
		super(id);
	}


	/**
	 *  Constructor for the ASTPrimarySuffix object
	 *
	 *@param  p   Description of Parameter
	 *@param  id  Description of Parameter
	 */
	public ASTPrimarySuffix(JavaParser p, int id) {
		super(p, id);
	}


	/**
	 *  Set the object's name
	 *
	 *@param  newName  the new name
	 */
	public void setName(String newName) {
		name = newName.intern();
	}


	/**
	 *  Set the object's name (used for PMD testing)
	 *
	 *@param  newName  the new name
	 */
	public void setImage(String newName) {
		name = newName.intern();
	}


	/**
	 *  Get the object's name
	 *
	 *@return    the name
	 */
	public String getName() {
		return name;
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
    
    private boolean isArguments;

    public void setIsArguments() {
        this.isArguments = true;
    }

    public boolean isArguments() {
        return this.isArguments;
    }

}
