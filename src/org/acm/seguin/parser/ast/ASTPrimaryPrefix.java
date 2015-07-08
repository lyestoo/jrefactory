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
 *  The prefix expression.  This is the name of the method or
 *  the variable, but at this level it has no knowledge of whether
 *  it is a variable or a method invocation.
 *
 *@author     Chris Seguin
 *@created    October 13, 1999
 */
public class ASTPrimaryPrefix extends SimpleNode {
	//  Instance Variables
	private String name = "";
	private int count = 0;


	/**
	 *  Constructor for the ASTPrimaryPrefix object
	 *
	 *@param  id  Description of Parameter
	 */
	public ASTPrimaryPrefix(int id) {
		super(id);
	}


	/**
	 *  Constructor for the ASTPrimaryPrefix object
	 *
	 *@param  p   Description of Parameter
	 *@param  id  Description of Parameter
	 */
	public ASTPrimaryPrefix(JavaParser p, int id) {
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

	public int getCount() { return count; }
	public void setCount(int value) { count = value; }
    private boolean usesThisModifier;
    private boolean usesSuperModifier;

    public void setUsesThisModifier() {
        usesThisModifier = true;
    }

    public boolean usesThisModifier() {
        return this.usesThisModifier;
    }

    public void setUsesSuperModifier() {
        usesSuperModifier = true;
    }

    public boolean usesSuperModifier() {
        return this.usesSuperModifier;
    }
}
