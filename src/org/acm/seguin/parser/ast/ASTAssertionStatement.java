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
 *  Allows assertions to be thrown
 *
 *@author     Chris Seguin
 */
public class ASTAssertionStatement extends SimpleNode {
  public ASTAssertionStatement(int id) {
    super(id);
  }

  public ASTAssertionStatement(JavaParser p, int id) {
    super(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(JavaParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
