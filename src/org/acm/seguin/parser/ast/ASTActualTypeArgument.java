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
/* Generated By:JJTree: Do not edit this line. ASTActualTypeArgument.java */

public class ASTActualTypeArgument extends SimpleNode {
  private boolean hasWildcard;
  private boolean hasSuper;
  private boolean hasExtends;

  public ASTActualTypeArgument(int id) {
    super(id);
  }

  public ASTActualTypeArgument(JavaParser p, int id) {
    super(p, id);
  }

  public void setWildcard() {
      hasWildcard = true;
  }
  public void setSuper() {
      hasSuper = true;
  }
  public void setExtends() {
      hasExtends = true;
  }
  
  public boolean hasWildcard() {
      return hasWildcard;
  }
  public boolean hasSuper() {
      return hasSuper;
  }
  public boolean hasExtends() {
      return hasExtends;
  }
  
  /** Accept the visitor. **/
  public Object jjtAccept(JavaParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}