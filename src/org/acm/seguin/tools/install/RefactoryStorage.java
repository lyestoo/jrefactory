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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.Iterator;
import org.acm.seguin.util.FileSettings;
import org.acm.seguin.util.MissingSettingsException;
import org.acm.seguin.awt.ExceptionPrinter;

/**
 *  Description of the Class
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public class RefactoryStorage {
    private HashMap map;


    /**
     *  Constructor for the RefactoryStorage object
     */
    public RefactoryStorage() {
        map = new HashMap();
        load();
    }


    /**
     *  Gets the Value attribute of the RefactoryStorage object
     *
     *@param  key  Description of Parameter
     *@return      The Value value
     */
    public int getValue(String key) {
        Object obj = map.get(normalize(key));
        if (obj == null) {
            return 1000;
        }

        return ((Integer) obj).intValue();
    }


    /**
     *  Adds a feature to the Key attribute of the RefactoryStorage object
     *
     *@param  key    The feature to be added to the Key attribute
     *@param  value  The feature to be added to the Key attribute
     */
    public void addKey(String key, int value) {
        map.put(normalize(key), new Integer(value));
    }


    /**
     *  Description of the Method
     */
    public void store() {
        try {
            //String dir = FileSettings.getSettingsRoot() + File.separator + ".Refactory";
            //String filename = dir + File.separator + "refactory.settings";

            PrintWriter output = new PrintWriter(new FileWriter(new File(FileSettings.getRefactorySettingsRoot(),"refactory.settings")));
            Iterator iter = map.keySet().iterator();
            while (iter.hasNext()) {
                String next = (String) iter.next();
                output.println(next + "=" + map.get(next));
            }
            output.close();
        } catch (IOException ioe) {
            ExceptionPrinter.print(ioe, false);
        }
    }


    /**
     *  Description of the Method
     *
     *@param  input  Description of Parameter
     *@return        Description of the Returned Value
     */
    private String normalize(String input) {
        StringBuffer buffer = new StringBuffer();

        for (int ndx = 0; ndx < input.length(); ndx++) {
            char ch = input.charAt(ndx);
            if (Character.isJavaIdentifierPart(ch) || (ch == '.')) {
                buffer.append(ch);
            } else {
                buffer.append('_');
            }
        }

        return buffer.toString();
    }


    /**
     *  Description of the Method
     */
    private void load() {
        try {
            FileSettings settings = FileSettings.getRefactorySettings("refactory");
            Enumeration enum = settings.getKeys();
            while (enum.hasMoreElements()) {
                String next = (String) enum.nextElement();
                map.put(next, new Integer(settings.getInteger(next)));
            }
        } catch (MissingSettingsException mse) {
            //  Reasonable
        }
    }
}
