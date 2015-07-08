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
 *  Blocks are made up of BlockStatements. This contains some sort of statement
 *  or variable declaration.
 *
 *@author     Chris Seguin
 *@created    October 13, 1999
 */
public class ASTBlockStatement extends SimpleNode {
    private boolean isFinal;


    /**
     *  Constructor for the ASTBlockStatement object
     *
     *@param  id  Description of Parameter
     */
    public ASTBlockStatement(int id) {
        super(id);

        isFinal = false;
    }


    /**
     *  Constructor for the ASTBlockStatement object
     *
     *@param  p   Description of Parameter
     *@param  id  Description of Parameter
     */
    public ASTBlockStatement(JavaParser p, int id) {
        super(p, id);

        isFinal = false;
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


    /**
     *  Gets the final attribute of the ASTBlockStatement object
     *
     *@return    The final value
     */
    public boolean isFinal() {
        return isFinal;
    }


    /**
     *  Sets the final attribute of the ASTBlockStatement object
     *
     *@param  value  The new final value
     */
    public void setFinal(boolean value) {
        isFinal = value;
    }
}
