/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.parser.ast;

import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;
import org.acm.seguin.parser.JavaParser;
import org.acm.seguin.parser.JavaParserVisitor;
import org.acm.seguin.parser.JavaParserTreeConstants;

/**
 *  Stores a name. The name can consist of a number of parts separated by
 *  periods.
 *
 *@author     Chris Seguin
 *@created    October 13, 1999
 */
public class ASTName extends SimpleNode implements Cloneable {
	//  Instance Variables
        protected String name = null;


	/**
	 *  Constructor for the ASTName object
	 *
	 *@param  id  Description of Parameter
	 */
	public ASTName()
	{
		super(JavaParserTreeConstants.JJTIDENTIFIER);
	}
   
   
	/**
	 *  Constructor for the ASTName object
	 *
	 *@param  id  Description of Parameter
	 */
	public ASTName(int id)
	{
		super(id);
	}


	/**
	 *  Constructor for the ASTName object
	 *
	 *@param  p   Description of Parameter
	 *@param  id  Description of Parameter
	 */
	public ASTName(JavaParser p, int id)
	{
		super(p, id);
	}


  
	/**
	 *  Add a component of the name
	 *
	 *@param  ndx    the index of the part requested
	 *@param  value  The new NamePart value
	 */
	public void setNamePart(int ndx, String value)
	{
		name=null;
                int count = 0;
                for (int i=0; i<children.length; i++) {
                    if (children[i] instanceof ASTIdentifier) {
                        if (count==ndx) {
                            ((ASTIdentifier)children[i]).setName(value);
                            return;
                        }
                        count++;
                    }
                }
	}


	/**
	 *  Add a component of the name
	 *
	 *@param  ndx  the index of the part requested
	 *@return      the portion of the name requested
	 */
	public String getNamePart(int ndx)
	{
		int count = 0;
                for (int i=0; i<children.length; i++) {
                    if (children[i] instanceof ASTIdentifier) {
                        if (count==ndx) {
                            return ((ASTIdentifier)children[i]).getName();
                        }
                        count++;
                    }
                }

		return null;
	}


	/**
	 *  Add a component of the name
	 *
	 *@param  ndx    the index of the part requested
	 *@param  value  Description of Parameter
	 */
	public void insertNamePart(int ndx, String value)
	{
                name=null;
		int count = 0;
                ASTIdentifier ident = new ASTIdentifier(JavaParserTreeConstants.JJTIDENTIFIER);
                ident.setName(value);
                for (int i=0; i<children.length; i++) {
                    if (children[i] instanceof ASTIdentifier) {
                        if (count==ndx) {
                            jjtInsertChild(ident, i);
                            return;
                        }
                        count++;
                    }
                }
	}


	/**
	 *  Set the object's name
	 *
	 *@param  newName  the new name
	 */
	public void addNamePart(String newName) {
      name=null;
      ASTIdentifier ident = new ASTIdentifier(JavaParserTreeConstants.JJTIDENTIFIER);
      ident.setName(newName);
      jjtAddChild(ident, ((children==null) ? 0 : children.length));
	}


	/**
	 *  Get the object's name
	 *
	 *@return    the name
	 */
	public String getName() {
      if (name==null) {
         if (children==null) {
            name = "";
            return name;
         }
         //  Local Variables
         StringBuffer buf = new StringBuffer();
         boolean first = true;
         
         //  Iterate through the parts
         for (int i=0; i<children.length; i++) {
            if (children[i] instanceof ASTIdentifier) {
               if (!first) {
                  buf.append(".");
               }
               buf.append(((ASTIdentifier)children[i]).getName());
               first = false;
            }
         }
         
         //  Return the buffer
         name = buf.toString();
      }
      return name;
	}


	/**
	 *  Get the length of the name
	 *
	 *@return    the number of parts in the name
	 */
	public int getNameSize() {
      int count = 0;
      for (int i=0; i<children.length; i++) {
         if (children[i] instanceof ASTIdentifier) {
            count++;
         }
      }
      return count;
	}


	/**
	 *  Convert this object from a string
	 *
	 *@param  input  Description of Parameter
	 */
	public void fromString(String input) {
		//  Clean the old one
      name = null;   //FIXME? = input
		children = null;

		//  Load it
		StringTokenizer tok = new StringTokenizer(input, ".");
		while (tok.hasMoreTokens()) {
			String next = tok.nextToken();
         //System.out.println("ASTName.fromString.addNamePart("+next+")");
			addNamePart(next);
		}
	}



	/**
	 *  Checks to see if the two names are equal
	 *
	 *@param  other  Description of Parameter
	 *@return        Description of the Returned Value
	 */
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if (other instanceof ASTName) {
			ASTName otherName = (ASTName) other;

			if (otherName.getNameSize() == getNameSize()) {
				return getName().equals(otherName.getName());
			}
		}
		return false;
	}
   
   public int hashCode() {
      if (name==null) {
         name = getName();
      }
      return name.hashCode();
   }


	/**
	 *  Determines if two names start with the same series of items
	 *
	 *@param  otherName  Description of Parameter
	 *@return            Description of the Returned Value
	 */
	public boolean startsWith(ASTName otherName)
	{
		//  To start with the other name, the other name must be less than or equal in parts
		if (otherName.getNameSize() > getNameSize()) {
			return false;
		}

		//  Look for the point where they are different
		int last = Math.min(otherName.getNameSize(), getNameSize());
		for (int ndx = 0; ndx < last; ndx++) {
			if (!getNamePart(ndx).equals(otherName.getNamePart(ndx))) {
				return false;
			}
		}

		//  They must be the same
		return true;
	}



	/**
	 *  Change starting part. Presumes that otherName is less than the length of
	 *  the current name.
	 *
	 *@param  oldBase  Description of Parameter
	 *@param  newBase  Description of Parameter
	 *@return          Description of the Returned Value
	 */
	public ASTName changeStartingPart(ASTName oldBase, ASTName newBase)
	{
		ASTName result = new ASTName();

		int last = newBase.getNameSize();
		for (int ndx = 0; ndx < last; ndx++) {
			result.addNamePart(newBase.getNamePart(ndx));
		}

		int end = getNameSize();
		int start = oldBase.getNameSize();
		for (int ndx = start; ndx < end; ndx++) {
			result.addNamePart(getNamePart(ndx));
		}

		return result;
	}



	/**
	 *  Accept the visitor.
	 *
	 *@param  visitor  Description of Parameter
	 *@param  data     Description of Parameter
	 *@return          Description of the Returned Value
	 */
	public Object jjtAccept(JavaParserVisitor visitor, Object data)
	{
		return visitor.visit(this, data);
	}
    
	/**
	 *  Convert this object from a string (used for PMD testing)
	 *
	 *@param  image  The name in form "org.test.AName" for example
	 */
    public void setImage(String image) {
        fromString(image);
    }
}
