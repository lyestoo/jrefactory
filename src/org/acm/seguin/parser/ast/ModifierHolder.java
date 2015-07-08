/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.parser.ast;

import java.io.Serializable;

/**
 *  Holds a description of the modifiers for a field or a class
 *
 *@author    <a href="mailto:JRefactory@ladyshot.demon.co.uk">Mike Atkinson</a>
 *@version   $Id: ModifierHolder.java,v 1.3 2003/08/29 22:55:51 mikeatkinson Exp $
 *@since     2.8.00
 */
public interface ModifierHolder {

	/**
	 *  Description of the Field
	 */
	//public static final int ABSTRACT = 0x0001;
	/**
	 *  Description of the Field
	 */
	//public static final int EXPLICIT = 0x0002;
	/**
	 *  Description of the Field
	 */
	//public static final int FINAL = 0x0004;
	/**
	 *  Description of the Field
	 */
	//public static final int INTERFACE = 0x0008;
	/**
	 *  Description of the Field
	 */
	//public static final int NATIVE = 0x0010;
	/**
	 *  Description of the Field
	 */
	//public static final int PRIVATE = 0x0020;
	/**
	 *  Description of the Field
	 */
	//public static final int PROTECTED = 0x0040;
	/**
	 *  Description of the Field
	 */
	//public static final int PUBLIC = 0x0080;
	/**
	 *  Description of the Field
	 */
	//public static final int STATIC = 0x0100;
	/**
	 *  Description of the Field
	 */
	//public static final int SYNCHRONIZED = 0x0400;
	/**
	 *  Description of the Field
	 */
	//public static final int TRANSIENT = 0x0800;
	/**
	 *  Description of the Field
	 */
	//public static final int VOLATILE = 0x1000;
	/**
	 *  Description of the Field
	 */
	//public static final int STRICTFP = 0x2000;

    /**
     * The <code>int</code> value representing the <code>public</code> 
     * modifier.
     */    
    public static final int PUBLIC           = 0x00000001;

    /**
     * The <code>int</code> value representing the <code>private</code> 
     * modifier.
     */    
    public static final int PRIVATE          = 0x00000002;

    /**
     * The <code>int</code> value representing the <code>protected</code> 
     * modifier.
     */    
    public static final int PROTECTED        = 0x00000004;

    /**
     * The <code>int</code> value representing the <code>static</code> 
     * modifier.
     */    
    public static final int STATIC           = 0x00000008;

    /**
     * The <code>int</code> value representing the <code>final</code> 
     * modifier.
     */    
    public static final int FINAL            = 0x00000010;

    /**
     * The <code>int</code> value representing the <code>synchronized</code> 
     * modifier.
     */    
    public static final int SYNCHRONIZED     = 0x00000020;

    /**
     * The <code>int</code> value representing the <code>volatile</code> 
     * modifier.
     */    
    public static final int VOLATILE         = 0x00000040;

    /**
     * The <code>int</code> value representing the <code>transient</code> 
     * modifier.
     */    
    public static final int TRANSIENT        = 0x00000080;

    /**
     * The <code>int</code> value representing the <code>native</code> 
     * modifier.
     */    
    public static final int NATIVE           = 0x00000100;

    /**
     * The <code>int</code> value representing the <code>interface</code> 
     * modifier.
     */    
    public static final int INTERFACE        = 0x00000200;

    /**
     * The <code>int</code> value representing the <code>abstract</code> 
     * modifier.
     */    
    public static final int ABSTRACT         = 0x00000400;

    /**
     * The <code>int</code> value representing the <code>strictfp</code> 
     * modifier.
     */    
    public static final int STRICTFP           = 0x00000800;


	///**
	// *  Description of the Field
	// */
	//public static final int STRICT = 0x1000;

	/**
	 *  Description of the Field
	 */
	public static final int EXPLICIT = 0x2000;



	/**
	 *  Description of the Field
	 */
	public static final String[] names =
			{
			"abstract",
			"explicit",
			"final",
			"interface",
			"native",
			"private",
			"protected",
			"public",
			"static",
			"strict",
			"strictfp",
			"synchronized",
			"transient",
			"volatile"
			};

                        
    /**
     *  Gets the modifier bits
     *
     *@return    the modifier bits
     */
    public int getModifiers();
    
    /**
     *  Sets the modifier bits
     *
     *@param modifiers   the modifier bits
     */
     public void setModifiers(int modifiers);

     
	/**
	 *  Sets the private bit in the modifiers
	 *
	 *@param  value  true if we are setting the private modifier
	 */
	public void setPrivate(boolean value);

	/**
	 *  Sets the private bit (to true) in the modifiers
	 */
	public void setPrivate();


	/**
	 *  Sets the protected bit in the modifiers
	 *
	 *@param  value  true if we are setting the protected modifier
	 */
	public void setProtected(boolean value);

	/**
	 *  Sets the protected bit (to true) in the modifiers
	 */
	public void setProtected();


	/**
	 *  Sets the public bit in the modifiers
	 *
	 *@param  value  true if we are setting the public modifier
	 */
	public void setPublic(boolean value);

	/**
	 *  Sets the public bit (to true) in the modifiers
	 */
	public void setPublic();


	/**
	 *  Sets the abstract bit in the modifiers
	 *
	 *@param  value  true if we are setting the modifier
	 */
	public void setAbstract(boolean value);

	/**
	 *  Sets the abstract bit (to true) in the modifiers
	 */
	public void setAbstract();


	/**
	 *  Sets the Synchronized bit of the in the modifiers
	 *
	 *@param  value  The new Synchronized value
	 */
	public void setSynchronized(boolean value);

	/**
	 *  Sets the Synchronized bit (to true) in the modifiers
	 */
	public void setSynchronized();


	/**
	 *  Sets the Static bit of the in the modifiers
	 *
	 *@param  value  The new Static value
	 */
	public void setStatic(boolean value);

	/**
	 *  Sets the Static bit (to true) of the in the modifiers
	 */
	public void setStatic();

	/**
	 *  Sets the Final bit (to true) of the in the modifiers
	 */
	public void setFinal();

	/**
	 *  Sets the StrictFP bit (to true) of the in the modifiers
	 */
	public void setStrict();


	/**
	 *  Determine if the object is abstract
	 *
	 *@return    true if this stores an ABSTRACT flag
	 */
	public boolean isAbstract();


	/**
	 *  Determine if the object is explicit
	 *
	 *@return    true if this stores an EXPLICIT flag
	 */
	public boolean isExplicit();


	/**
	 *  Determine if the object is final
	 *
	 *@return    true if this stores an FINAL flag
	 */
	public boolean isFinal();


	/**
	 *  Determine if the object is interface
	 *
	 *@return    true if this stores an INTERFACE flag
	 */
	public boolean isInterface();


	/**
	 *  Determine if the object is native
	 *
	 *@return    true if this stores an NATIVE flag
	 */
	public boolean isNative();


	/**
	 *  Determine if the object is private
	 *
	 *@return    true if this stores an PRIVATE flag
	 */
	public boolean isPrivate();


	/**
	 *  Determine if the object is protected
	 *
	 *@return    true if this stores an PROTECTED flag
	 */
	public boolean isProtected();

	/**
	 *  Determine if the object is public
	 *
	 *@return    true if this stores an PUBLIC flag
	 */
	public boolean isPublic();


	/**
	 *  Determine if the object is static
	 *
	 *@return    true if this stores an static flag
	 */
	public boolean isStatic();


	///**
	// *  Determine if the object is strict
	// *
	// *@return    true if this stores an STRICT flag
	// */
	//public boolean isStrict();


	/**
	 *  Determine if the object is strictFP
	 *
	 *@return    true if this stores an STRICTFP flag
	 */
	public boolean isStrictFP();


	/**
	 *  Determine if the object is synchronized
	 *
	 *@return    true if this stores an SYNCHRONIZED flag
	 */
	public boolean isSynchronized();


	/**
	 *  Determine if the object is transient
	 *
	 *@return    true if this stores an TRANSIENT flag
	 */
	public boolean isTransient();


	/**
	 *  Determine if the object is volatile
	 *
	 *@return    true if this stores an VOLATILE flag
	 */
	public boolean isVolatile();


	/**
	 *  Determines if this has package scope
	 *
	 *@return    true if this has package scope
	 */
	public boolean isPackage();


	/**
	 *  Add a modifier
	 *
	 *@param  mod  the new modifier
	 */
	public void addModifier(String mod);

	public void copyModifiers(ModifierHolder source);
}
