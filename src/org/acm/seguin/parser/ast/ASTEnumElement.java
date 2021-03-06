/*
 *  Author: Mike Atkinson
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.parser.ast;

import org.acm.seguin.parser.JavaParserVisitor;
import org.acm.seguin.parser.JavaParserTreeConstants;
import org.acm.seguin.parser.Node;
import org.acm.seguin.parser.NamedToken;
import org.acm.seguin.parser.JavaParser;
import org.acm.seguin.parser.Token;
import java.util.*;
/* Generated By:JJTree: Do not edit this line. ASTEnumElement.java */

public class ASTEnumElement extends ASTLiteral {
  public ASTEnumElement(int id) {
    super(id);
  }

  public ASTEnumElement(JavaParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(JavaParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
