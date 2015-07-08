/* ====================================================================
 * The JRefactory License, Version 1.0
 *
 * Copyright (c) 2001 JRefactory.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        JRefactory (http://www.sourceforge.org/projects/jrefactory)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "JRefactory" must not be used to endorse or promote
 *    products derived from this software without prior written
 *    permission. For written permission, please contact seguin@acm.org.
 *
 * 5. Products derived from this software may not be called "JRefactory",
 *    nor may "JRefactory" appear in their name, without prior written
 *    permission of Chris Seguin.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE CHRIS SEGUIN OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of JRefactory.  For more information on
 * JRefactory, please see
 * <http://www.sourceforge.org/projects/jrefactory>.
 */
package org.acm.seguin.tools.install;

/**
 *  Stores a tag line
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public class TagLinePanel {
    private String tagName;
    private String description;
    private boolean classType;
    private boolean methodType;
    private boolean fieldType;
    private boolean enumType;


    /**
     *  Constructor for the TagLinePanel object
     */
    public TagLinePanel() {
        tagName = "";
        description = "";
        classType = false;
        methodType = false;
        fieldType = false;
        enumType = false;
    }


    /**
     *  Sets the TagName attribute of the TagLinePanel object
     *
     *@param  value  The new TagName value
     */
    public void setTagName(String value) {
        if (value == null) {
            tagName = "";
        } else {
            tagName = value;
        }
    }


    /**
     *  Sets the Description attribute of the TagLinePanel object
     *
     *@param  value  The new Description value
     */
    public void setDescription(String value) {
        if (value == null) {
            description = "";
        } else {
            description = value;
        }
    }


    /**
     *  Sets the ClassType attribute of the TagLinePanel object
     *
     *@param  way  The new ClassType value
     */
    public void setClassType(boolean way) {
        classType = way;
    }


    /**
     *  Sets the MethodType attribute of the TagLinePanel object
     *
     *@param  way  The new MethodType value
     */
    public void setMethodType(boolean way) {
        methodType = way;
    }


    /**
     *  Sets the FieldType attribute of the TagLinePanel object
     *
     *@param  way  The new FieldType value
     */
    public void setFieldType(boolean way) {
        fieldType = way;
    }


    /**
     *  Sets the EnumType attribute of the TagLinePanel object
     *
     *@param  way  The new EnumType value
     */
    public void setEnumType(boolean way) {
        enumType = way;
    }


    /**
     *  Gets the TagName attribute of the TagLinePanel object
     *
     *@return    The TagName value
     */
    public String getTagName() {
        return tagName;
    }


    /**
     *  Gets the Description attribute of the TagLinePanel object
     *
     *@return    The Description value
     */
    public String getDescription() {
        return description;
    }


    /**
     *  Gets the ClassType attribute of the TagLinePanel object
     *
     *@return    The ClassType value
     */
    public boolean isClassType() {
        return classType;
    }


    /**
     *  Gets the MethodType attribute of the TagLinePanel object
     *
     *@return    The MethodType value
     */
    public boolean isMethodType() {
        return methodType;
    }


    /**
     *  Gets the FieldType attribute of the TagLinePanel object
     *
     *@return    The FieldType value
     */
    public boolean isFieldType() {
        return fieldType;
    }


    /**
     *  Gets the EnumType attribute of the TagLinePanel object
     *
     *@return    The EnumType value
     */
    public boolean isEnumType() {
        return enumType;
    }


    /**
     *  Updates the label with the values
     *
     *@return    Description of the Return Value
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer(tagName);
        buffer.append(" [");
        buffer.append(description);
        buffer.append("] ");

        boolean needsSem = false;
        if (classType) {
            buffer.append("class");
            needsSem = true;
        }

        if (methodType) {
            if (needsSem) {
                buffer.append("; ");
            }

            buffer.append("method");
            needsSem = true;
        }

        if (fieldType) {
            if (needsSem) {
                buffer.append("; ");
            }

            buffer.append("field");
            needsSem = true;
        }

        if (enumType) {
            if (needsSem) {
                buffer.append("; ");
            }

            buffer.append("enum");
            needsSem = true;
        }

        return buffer.toString();
    }
}

//  This is the end of the file

