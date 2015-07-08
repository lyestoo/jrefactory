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
package org.acm.seguin.pretty;

import org.acm.seguin.util.FileSettings;
import org.acm.seguin.util.MissingSettingsException;

/**
 *  Stores all the javadoc tags, rather than getting them directly from the file
 *  settings. This will allow the descriptions to be unit tested.
 *
 *@author     Chris Seguin
 *@created    November 29, 2001
 *@since      2.6.32
 */
public class JavadocTags {
    private boolean debug;
    private String exceptionDescr;
    private String exceptionTag;
    private String paramDescr;
    private String returnDescr;
    private String constructorDescr;

    private static JavadocTags singleton = null;


    /**
     *  Constructor for the JavadocTags object
     *
     *@since    2.6.32
     */
    private JavadocTags() {
        debug = false;
        paramDescr = "";
        returnDescr = "";
        exceptionDescr = "";
    }


    /**
     *  Turns debugging on
     *
     *@param  way  The new debug value
     *@since       2.6.32
     */
    public void setDebug(boolean way) {
        debug = way;
    }


    /**
     *  Sets the exceptionDescr attribute of the JavadocTags object
     *
     *@param  value  The new exceptionDescr value
     *@since         2.6.34
     */
    public void setExceptionDescr(String value) {
    	if (value == null) exceptionDescr = ""; else
        exceptionDescr = value;
    }


    /**
     *  Sets the exceptionTag attribute of the JavadocTags object
     *
     *@param  value  The new exceptionTag value
     *@since         2.6.32
     */
    public void setExceptionTag(String value) {
        if ((value == null) || (value.length() == 0) || value.equals("@")) {
            exceptionTag = "exception";
        }
        else if (value.charAt(0) == '@') {
            exceptionTag = value.substring(1);
        }
        else {
            exceptionTag = value;
        }
    }


    /**
     *  Sets the paramDescr attribute of the JavadocTags object
     *
     *@param  value  The new paramDescr value
     *@since         2.6.34
     */
    public void setParamDescr(String value) {
    	if (value == null) paramDescr = ""; else
        paramDescr = value;
    }


    /**
     *  Sets the returnDescr attribute of the JavadocTags object
     *
     *@param  value  The new returnDescr value
     *@since         2.6.34
     */
    public void setReturnDescr(String value) {
    	if (value == null) returnDescr = ""; else
        returnDescr = value;
    }


    /**
     *  Gets the exceptionDescr attribute of the JavadocTags object
     *
     *@return    The exceptionDescr value
     *@since     2.6.34
     */
    public String getExceptionDescr() {
        return exceptionDescr;
    }


    /**
     *  Gets the exceptionTag attribute of the JavadocTags object
     *
     *@return    The exceptionTag value
     *@since     2.6.32
     */
    public String getExceptionTag() {
        return "@" + exceptionTag;
    }


    /**
     *  Gets the paramDescr attribute of the JavadocTags object
     *
     *@return    The paramDescr value
     *@since     2.6.34
     */
    public String getParamDescr() {
        return paramDescr;
    }


    /**
     *  Gets the returnDescr attribute of the JavadocTags object
     *
     *@return    The returnDescr value
     *@since     2.6.34
     */
    public String getReturnDescr() {
        return returnDescr;
    }


    /**
     *  Gets the exceptionTag attribute of the JavadocTags object
     *
     *@param  name  Description of the Parameter
     *@return       The exceptionTag value
     *@since        2.6.32
     */
    public boolean isExceptionTag(String name) {
        String temp = name;
        if (name.charAt(0) == '@') {
            temp = name.substring(1);
        }

        return (exceptionTag.equals(temp) || temp.equals("exception") || temp.equals("throws"));
    }


    /**
     *  Gets the debug attribute of the JavadocTags object
     *
     *@return    The debug value
     *@since     2.6.32
     */
    private boolean isDebug() {
        return debug;
    }


    /**
     *  Reloads from the pretty.settings file
     *
     *@since    2.6.32
     */
    public void reload() {
        FileSettings bundle = FileSettings.getRefactoryPrettySettings();

        try {
            setExceptionTag(bundle.getString("exception.tag.name"));
        }
        catch (MissingSettingsException mse) {
            setExceptionTag("exception");
        }

        try {
            setExceptionDescr(bundle.getString("exception.descr"));
        }
        catch (MissingSettingsException mse) {
            setExceptionDescr("Description of the exception");
        }

        try {
            setParamDescr(bundle.getString("param.descr"));
        }
        catch (MissingSettingsException mse) {
            setParamDescr("Description of the parameter");
        }

        try {
            setReturnDescr(bundle.getString("return.descr"));
        }
        catch (MissingSettingsException mse) {
            setReturnDescr("Description of the return value");
        }

        try {
        	setConstructorDescr(bundle.getString("constructor.descr"));
        }
        catch (MissingSettingsException mse) {
        	setConstructorDescr("Constructor for the {0} class");
        }
    }


    /**
     *  Description of the Method
     *
     *@return    Default description of the Return Value
     *@since     2.6.32
     */
    public static JavadocTags get() {
        if (singleton == null) {
            singleton = new JavadocTags();
            singleton.reload();
        }

        return singleton;
    }
    public String getConstructorDescr() { return constructorDescr; }
    public void setConstructorDescr(String value) {
    	if (value == null) constructorDescr = ""; else
    	constructorDescr = value; }
}
//  EOF
