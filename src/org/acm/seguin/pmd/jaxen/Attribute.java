/*
 * Created on 15/03/2003
 */
package org.acm.seguin.pmd.jaxen;

import org.acm.seguin.parser.Node;

/**
 * @author daniels
 *
 */
public class Attribute {
    
    private Node parent;
    private String name;
    private String value;

	public Attribute(Node parent, String name, String value) {
	    this.parent = parent;
	    this.name = name;
	    this.value = value;
	}

    /**
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * @return String
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the name.
     * @param name The name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the value.
     * @param value The value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return Node
     */
    public Node getParent() {
        return parent;
    }

    /**
     * Sets the parent.
     * @param parent The parent to set
     */
    public void setParent(Node parent) {
        this.parent = parent;
    }

}
