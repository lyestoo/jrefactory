/*
 * PropertiesFile.java
 * Copyright (c) 2000,2001 Dirk Moebius (dmoebius@gmx.net)
 *
 * jEdit buffer options:
 * :tabSize=4:indentSize=4:noTabs=false:maxLineLen=0:
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.acm.seguin.ide.common.options;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import org.acm.seguin.util.FileSettings;
import org.acm.seguin.util.SettingNotFoundException;
import org.acm.seguin.ide.common.IDEPlugin;
import org.acm.seguin.ide.common.IDEInterface;


/**
 *  An extension to <code>org.acm.seguin.util.FileSettings</code> with methods
 *  for storing and saving new properties.
 *
 *@author     Mike Atkinson (<a href="mailto:javastyle@ladyshot.demon.co.uk">
 *      Mike@ladyshot.demon.co.uk</a> )
 *@author     Dirk Moebius (<a href="mailto:dmoebius@gmx.net">dmoebius@gmx.net
 *      </a>)
 *@created    03 September 2003
 *@version    $Version: $
 *@since      1.0
 */
public class PropertiesFile {

    /**
     *  sentinel for deleted keys
     */
    private final static Object DELETED = new Object();

    private FileSettings props = null;
    private Hashtable newValues = new Hashtable();


    /**
     *  initializes the properties with the settings of a <code>FileSettings</code>
     *  instance.
     *
     *@param  props  Description of the Parameter
     */
    public PropertiesFile(FileSettings props) {
        this.props = props;
    }


    /**
     *  Gets the localProperty attribute of the PropertiesFile object
     *
     *@param  key  Description of the Parameter
     *@return      The localProperty value
     */
    public boolean isLocalProperty(String key) {
        boolean lp = props.isLocalProperty(key);
        return lp;
    }


    /**
     *@param  key  the property key
     *@return      the value of the property as String, or null if the property
     *      cannot be found.
     */
    public String getString(String key) {
        Object obj = newValues.get(key);
        if (obj == DELETED) {
            return null;
        } else if (obj != null) {
            return obj.toString();
        } else {
            try {
                return props.getString(key);
            } catch (SettingNotFoundException snfex) {
                return null;
            }
        }
    }


    /**
     *@param  key           the property key
     *@param  defaultValue  a default value
     *@return               the value of the property as String, or the default
     *      value if the property cannot be found.
     */
    public String getString(String key, String defaultValue) {
        String val = getString(key);
        return val != null ? val : defaultValue;
    }


    /**
     *@param  key           the property key
     *@param  defaultValue  a default value
     *@return               the value of the property as int, or the
     *      defaultValue if either the property cannot be found, or the stored
     *      property is not a valid integer.
     */
    public int getInteger(String key, int defaultValue) {
        String value = getString(key);
        if (value == null) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            IDEPlugin.log(IDEInterface.ERROR, this, "invalid number for " + key + ": " + value);
            return defaultValue;
        }
    }


    /**
     *@param  key  the property key
     *@return      true, if the value of the property equals "true", false
     *      otherwise
     */
    public boolean getBoolean(String key) {
        String value = getString(key);
        if (value == null) {
            return true;
        }
        return new Boolean(value).booleanValue();
    }


    /**
     *@param  key           the property key
     *@param  defaultValue  Description of the Parameter
     *@return               true, if the value of the property equals "true",
     *      false otherwise
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        String value = getString(key);
        if (value == null) {
            //throw new SettingNotFoundException("Refactory", "pretty", key);
            return defaultValue;
        }
        return new Boolean(value).booleanValue();
    }


    /**
     *  Sets the string attribute of the PropertiesFile object
     *
     *@param  key    The new string value
     *@param  value  The new string value
     */
    public void setString(String key, String value) {
        newValues.put(key, value);
    }


    /**
     *  Description of the Method
     *
     *@param  key  Description of the Parameter
     */
    public void deleteKey(String key) {
        // Mark the key as deleted in the newValues table. The key is not
        // simply removed from newValues, because it could still be in
        // props (and we have no means to remove from props).
        newValues.put(key, DELETED);
    }


    /**
     *  Reloads the original property file, overrides the old values with the
     *  stored new ones, and saves the property file, if there are any changes.
     */
    public synchronized void save() {
        if (newValues.isEmpty()) {
            // no new values: nothing to do!
            return;
        }

        // determine if any property was changed
        boolean changed = false;
        // insert new values into writeValues, or delete the keys that are
        // marked as DELETED in newValues
        for (Enumeration e = newValues.keys(); e.hasMoreElements(); ) {
            String key = (String) e.nextElement();
            Object newval = newValues.get(key);
            if (newval == null || newval == DELETED) {
                props.removeKey(key);
                changed = true;
            } else {
                // compare new value with old value from pretty.settings:
                String oldval = null;
                try {
                    oldval = props.getString(key);
                } catch (SettingNotFoundException snfex) {
                }
                if (oldval == null || !newval.toString().equals(oldval)) {
                    props.setString(key, newval.toString());
                    changed = true;
                }
            }
        }

        if (changed) {
            // only bother to save if propery changed or deleted.
            props.save();
        }
    }


    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public String toString() {
        return "PropertiesFile[" + props + "]";
    }


    /**
     *  Reloads the original property file, overrides the old values with the
     *  stored new ones, and saves the property file, if there are any changes.
     *
     *@param  file             Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public synchronized void save(File file) throws IOException {
        if (newValues.isEmpty()) {
            return;
        }
        // no new values: nothing to do!

        // create a hashtable for the values to be written
        Hashtable writeValues = new Hashtable();
        // reload property file
        props.setReloadNow(true);
        // copy values of original property file into writeValues
        for (Enumeration e = props.getKeys(); e.hasMoreElements(); ) {
            String key = (String) e.nextElement();
            String val = props.getString(key);
            writeValues.put(key, val);
        }

        // determine if any property was changed
        boolean changed = false;
        // insert new values into writeValues, or delete the keys that are
        // marked as DELETED in newValues
        for (Enumeration e = newValues.keys(); e.hasMoreElements(); ) {
            String key = (String) e.nextElement();
            Object newval = newValues.get(key);
            if (newval == null || newval == DELETED) {
                writeValues.remove(key);
                changed = true;
            } else {
                // compare new value with old value from pretty.settings:
                String oldval = null;
                try {
                    oldval = props.getString(key);
                } catch (SettingNotFoundException snfex) {
                }
                if (oldval == null || !newval.toString().equals(oldval)) {
                    writeValues.put(key, newval.toString());
                    changed = true;
                }
            }
        }

        // if no property was changed or deleted, we can stop here
        if (!changed) {
            return;
        }

        // open file for write:
        BufferedWriter awriter = new BufferedWriter(
                new OutputStreamWriter(
                new FileOutputStream(file), "8859_1"));

        // save property file with all new and old entries from writeValues
        for (Enumeration e = writeValues.keys(); e.hasMoreElements(); ) {
            String key = (String) e.nextElement();
            String val = writeValues.get(key).toString();
            key = saveConvert(key, true);
            val = saveConvert(val, false);
            awriter.write(key + "=" + val);
            awriter.newLine();
        }

        // close file:
        awriter.flush();
        awriter.close();
    }


    /*
	 * Converts unicodes to encoded &#92;uxxxx
	 * and writes out any of the characters in specialSaveChars
	 * with a preceding slash
	 */
    /**
     *  Description of the Method
     *
     *@param  theString    Description of the Parameter
     *@param  escapeSpace  Description of the Parameter
     *@return              Description of the Return Value
     */
    private static String saveConvert(String theString, boolean escapeSpace) {
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len * 2);

        for (int x = 0; x < len; x++) {
            char aChar = theString.charAt(x);
            switch (aChar) {
                case ' ':
                    if (x == 0 || escapeSpace) {
                        outBuffer.append('\\');
                    }
                    outBuffer.append(' ');
                    break;
                case '\\':
                    outBuffer.append('\\');
                    outBuffer.append('\\');
                    break;
                case '\t':
                    outBuffer.append('\\');
                    outBuffer.append('t');
                    break;
                case '\n':
                    outBuffer.append('\\');
                    outBuffer.append('n');
                    break;
                case '\r':
                    outBuffer.append('\\');
                    outBuffer.append('r');
                    break;
                case '\f':
                    outBuffer.append('\\');
                    outBuffer.append('f');
                    break;
                default:
                    if ((aChar < 0x0020) || (aChar > 0x007e)) {
                        outBuffer.append('\\');
                        outBuffer.append('u');
                        outBuffer.append(toHex((aChar >> 12) & 0xF));
                        outBuffer.append(toHex((aChar >> 8) & 0xF));
                        outBuffer.append(toHex((aChar >> 4) & 0xF));
                        outBuffer.append(toHex(aChar & 0xF));
                    } else {
                        if (specialSaveChars.indexOf(aChar) != -1) {
                            outBuffer.append('\\');
                        }
                        outBuffer.append(aChar);
                    }
            }
        }

        return outBuffer.toString();
    }


    /**
     *  Convert a nibble to a hex character
     *
     *@param  nibble  the nibble to convert.
     *@return         Description of the Return Value
     */
    private static char toHex(int nibble) {
        return hexDigit[(nibble & 0xF)];
    }


    /**
     *  A table of hex digits
     */
    private final static char[] hexDigit = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
            };

    private final static String specialSaveChars = "=: \t\r\n\f#!";

}

