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
package org.acm.seguin.util;

import org.acm.seguin.tools.RefactoryInstaller;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Enumeration;
import org.acm.seguin.awt.ExceptionPrinter;
import org.acm.seguin.project.Project;

/**
 *  Settings loaded from a file
 *
 *@author     Chris Seguin
 *@author     Mike Atkinson
 *@created    October 3, 1999
 */
public class FileSettings implements Settings {
    private String project;
    private String app;
    private String type;
    private File file;
    private long lastModified;
    private Properties props;
    private boolean continuallyReload;
    private boolean reloadNow;
    private FileSettings parent;

    private static Hashtable map = null;
    private static File settingsRoot = null;
    private static File refactorySettingsRoot = null;


    /**
     *  Constructor for the FileSettings object
     *
     *@param  express                       The file to use for loading
     *@exception  MissingSettingsException  The file is not found
     */
    public FileSettings(File express) throws MissingSettingsException {
        file = express;
        app = express.getParent();
        type = express.getName();

        if (!file.exists()) {
            throw new NoSettingsFileException(app, type);
        }

        load();

        continuallyReload = false;
        reloadNow = false;

        parent = null;
    }


    /**
     *  Constructor for the FileSettings object
     *
     *@param  project                       Description of the Parameter
     *@param  app                           The application name
     *@param  type                          The application type
     *@exception  MissingSettingsException  The file is not found
     */
    protected FileSettings(String project, String app, String type) throws MissingSettingsException {
        //System.out.println("FileSettings(" + project + "," + app + "," + type + ")");
        File directory = getSettingsRoot();
        //System.out.println("(1) directory=" + directory);

        directory = new File(directory, "." + app);
        //System.out.println("(2) directory=" + directory);
        if (!directory.exists()) {
            directory.mkdirs();
            System.out.println("directory="+directory);
            throw new NoSettingsFileException(app, type);
        }

        if (project != null) {
            directory = new File(directory, "projects");
            //System.out.println("(3) directory=" + directory);
            if (!directory.exists()) {
                directory.mkdirs();
                //throw new NoSettingsFileException(app, type);
            }
            directory = new File(directory, project);
            //System.out.println("(4) directory=" + directory);
            if (!directory.exists()) {
                directory.mkdirs();
                //throw new NoSettingsFileException(app, type);
            }
        }

        file = new File(directory, type + ".settings");
        if (!file.exists()) {
            System.out.println("creating file=" + file);
            try {
                java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter(file));
                pw.println("# project " + type + " pretty.settings");
                pw.close();
            } catch (java.io.IOException e) {
            }
            //throw new NoSettingsFileException(app, type);
        }

        load();

        this.app = app;
        this.type = type;
        this.project = project;

        continuallyReload = false;
        reloadNow = false;

        parent = (project == null) ? null : getSettings(null, app, type);
    }


    /**
     *  Constructor for the FileSettings object
     */
    /*protected FileSettings(String app, String type) throws MissingSettingsException {
        File directory = new File(getSettingsRoot(), "." + app);
        if (!directory.exists()) {
            directory.mkdirs();
            throw new NoSettingsFileException(app, type);
        }
        file = new File(directory, type + ".settings");
        if (!file.exists()) {
            throw new NoSettingsFileException(app, type);
        }
        load();
        this.app = app;
        this.type = type;
        this.project = null;
        continuallyReload = false;
        reloadNow = false;
        parent = null;
    }
    */
    /**
     *  Sets the ContinuallyReload attribute of the FileSettings object
     *
     *@param  way  The new ContinuallyReload value
     */
    public void setContinuallyReload(boolean way) {
        continuallyReload = way;
    }


    /**
     *  Sets the ReloadNow attribute of the FileSettings object
     *
     *@param  way  The new ReloadNow value
     */
    public void setReloadNow(boolean way) {
        reloadNow = way;

        if (reloadNow) {
            load();
        }
    }


    /**
     *  Gets the keys associated with this properties
     *
     *@return    the iterator
     */
    public Enumeration getKeys() {
        reloadIfNecessary();
        return props.keys();
    }


    /**
     *  Description of the Method
     *
     *@param  key  Description of the Parameter
     */
    public void removeKey(String key) {
        props.remove(key);
    }


    /**
     *  Gets a string
     *
     *@param  key  The code to look up
     *@return      The associated string
     */
    public boolean isLocalProperty(String key) {
        reloadIfNecessary();

        if (key == null || props == null) {
            return false;
        }
        String result = props.getProperty(key);
        return result != null;
    }


    /**
     *  Gets a string
     *
     *@param  key  The code to look up
     *@return      The associated string
     */
    public String getProperty(String key) {
        reloadIfNecessary();

        String result = props.getProperty(key);
        if ((result == null) && (parent != null)) {
            result = parent.getString(key);
        }
        return result;
    }


    /**
     *  Gets a string
     *
     *@param  key  The code to look up
     *@return      The associated string
     */
    public String getString(String key) {
        String result = getProperty(key);
        if (result == null) {
            throw new SettingNotFoundException(app, type, key);
        }

        return result;
    }


    /**
     *  Gets a string
     *
     *@param  key  The code to look up
     *@param  def  Use this if the code is not found
     *@return      The associated string
     */
    public String getProperty(String key, String def) {
        String result = getProperty(key);
        if (result == null) {
            result = def;
        }
        return result;
    }


    /**
     *  Sets a string
     *
     *@param  key    The code to look up
     *@param  value  New value for the setting code.
     */
    public void setString(String key, String value) {
        reloadIfNecessary();
        props.setProperty(key, value);
    }


    /**
     *  Gets a integer
     *
     *@param  key  The code to look up
     *@return      The associated integer
     */
    public int getInteger(String key) {
        try {
            return Integer.parseInt(getString(key));
        } catch (NumberFormatException mfe) {
            throw new SettingNotFoundException(app, type, key);
        }
    }


    /**
     *  Gets a double
     *
     *@param  key  The code to look up
     *@return      The associated double
     */
    public double getDouble(String key) {
        try {
            Double value = new Double(getString(key));
            return value.doubleValue();
        } catch (NumberFormatException mfe) {
            throw new SettingNotFoundException(app, type, key);
        }
    }


    /**
     *  Gets a boolean
     *
     *@param  key  The code to look up
     *@return      The associated boolean
     */
    public boolean getBoolean(String key) {
        try {
            Boolean value = new Boolean(getString(key));
            return value.booleanValue();
        } catch (NumberFormatException mfe) {
            throw new SettingNotFoundException(app, type, key);
        }
    }


    /**
     *  Sets the Parent attribute of the FileSettings object
     *
     *@param  value  The new Parent value
     */
    protected void setParent(FileSettings value) {
        parent = value;
    }


    /**
     *  Get the escaped character
     *
     *@param  ch  the character
     *@return     The character it should be replaced with
     */
    private char getSpecial(char ch) {
        switch (ch) {
            case 'b':
                return (char) 8;
            case 'r':
                return (char) 13;
            case 'n':
                return (char) 10;
            case 'f':
                return (char) 12;
            case 't':
                return (char) 9;
            default:
                return ch;
        }
    }


    /**
     *  Returns true if the file is up to date. This method is used to determine
     *  if it is necessary to reload the file.
     *
     *@return    true if it is up to date.
     */
    private boolean isUpToDate() {
        if (continuallyReload || reloadNow) {
            return (lastModified == file.lastModified());
        }
        return true;
        //  Assume that it is up to date
    }


    /**
     *  Loads all the settings from the file
     */
    private synchronized void load() {
        //System.out.println("Loading from:  " + file.getPath() + "  " + file.length());

        props = new Properties();

        // FIXME? use Properties.load()
        try {
            BufferedReader input = new BufferedReader(new FileReader(file));
            String line = input.readLine();
            while (line != null) {
                if ((line.length() == 0) || (line.charAt(0) == '#')) {
                    //  Comment - skip the line
                } else {
                    int equalsAt = line.indexOf('=');
                    if (equalsAt > 0) {
                        String key = line.substring(0, equalsAt);
                        String value = unescapeChars(line.substring(equalsAt + 1));
                        props.put(key, value);
                    }
                }
                line = input.readLine();
            }

            input.close();
            //System.out.println("  - loaded OK");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            ExceptionPrinter.print(ioe, false);
        }

        setReloadNow(false);
        lastModified = file.lastModified();
    }


    /**
     *  Stores all the settings from the file
     */
    public synchronized void save() {
        System.out.println("Saving to:  " + file.getPath());
        props.list(new java.io.PrintWriter(System.out));

        try {
            props.store(new java.io.FileOutputStream(file), app + " property file");
            //System.out.println("  - saved OK");
        } catch (IOException ioe) {
            ExceptionPrinter.print(ioe, false);
        }

        setReloadNow(false);
        lastModified = file.lastModified();
    }


    /**
     *  A transformation on the characters in the string
     *
     *@param  value  the string we are updating
     *@return        the updated string
     */
    private String unescapeChars(String value) {
        StringBuffer buffer = new StringBuffer();
        int last = value.length();

        for (int ndx = 0; ndx < last; ndx++) {
            char ch = value.charAt(ndx);
            if (ch == '\\') {
                char nextChar = value.charAt(ndx + 1);
                char result = ' ';
                if (nextChar == 'u') {
                    result = unicode(value, ndx);
                    ndx += 5;
                } else if (Character.isDigit(nextChar)) {
                    result = octal(value, ndx);
                    ndx += 3;
                } else if (ndx == last - 1) {
                    //  Continuation...
                } else {
                    result = getSpecial(nextChar);
                    ndx++;
                }

                buffer.append(result);
            } else {
                buffer.append(ch);
            }
        }

        return buffer.toString();
    }


    /**
     *  Determine the unicode character
     *
     *@param  value  Description of Parameter
     *@param  ndx    Description of Parameter
     *@return        Description of the Returned Value
     */
    private char unicode(String value, int ndx) {
        String hex = value.substring(ndx + 2, ndx + 6);
        int result = Integer.parseInt(hex, 16);
        return (char) result;
    }


    /**
     *  Determine the octal character
     *
     *@param  value  Description of Parameter
     *@param  ndx    Description of Parameter
     *@return        Description of the Returned Value
     */
    private char octal(String value, int ndx) {
        String oct = value.substring(ndx + 1, ndx + 4);
        int result = Integer.parseInt(oct, 8);
        return (char) result;
    }


    /**
     *  Sets the root directory for settings files
     *
     *@param  dir  The new SettingsRoot value
     */
    public static void setSettingsRoot(String dir) {
        settingsRoot = new File(dir);
        refactorySettingsRoot = null;
    }


    /**
     *  Sets the root directory for settings files
     *
     *@param  dir  The new SettingsRoot value
     */
    public static void setSettingsRoot(File dir) {
        settingsRoot = dir;
        refactorySettingsRoot = null;
    }


    /**
     *  Factory method to create FileSettings objects
     *
     *@param  project  The name of the project
     *@param  app      The name of the application
     *@param  name     The name of the specific settings
     *@return          A settings object
     */
    public static FileSettings getSettings(String project, String app, String name) {
        //System.out.println("getSettings(" + project + "," + app + "," + name + ")");
        initIfNecessary();

        String key = (project == null) ? "default" : project;
        key = key + "::" + app + "::" + name;
        FileSettings result = null;
        result = (FileSettings) map.get(key);
        //System.out.println("  map.get(" + key + ") -> " + result);
        if (result == null) {
            result = new FileSettings(project, app, name);
            map.put(key, result);
            //System.out.println("  map.put(" + key + "," + result + ")");
        }
        return result;
    }


    /**
     *  Factory method to create FileSettings objects
     *
     *@param  app   The name of the application
     *@param  name  The name of the specific settings
     *@return       A settings object
     */
    public static FileSettings getSettings(String app, String name) {
        return getSettings(Project.getCurrentProjectName(), app, name);
    }


    /**
     *  Factory method to create FileSettings objects. Equivalent to
     *  getSettings("Refactory",name)
     *
     *@param  name  The name of the specific settings
     *@return       A settings object
     *@since        2.7.04
     */
    public static FileSettings getRefactorySettings(String name) {
        return getSettings("Refactory", name);
    }


    /**
     *  Factory method to create FileSettings objects. Equivalent to
     *  getSettings("Refactory","pretty")
     *
     *@return    A settings object
     *@since     2.7.04
     */
    public static FileSettings getRefactoryPrettySettings() {
        return getSettings("Refactory", "pretty");
    }


    /**
     *  Gets the SettingsRoot for a Refactory application
     *
     *@return    The SettingsRoot value
     *@since     2.7.04
     */
    public static File getRefactorySettingsRoot() {
        if (refactorySettingsRoot == null) {
            refactorySettingsRoot = new File(settingsRoot, ".Refactory");
        }

        return refactorySettingsRoot;
    }


    /**
     *  Gets the SettingsRoot attribute of the FileSettings class
     *
     *@return    The SettingsRoot value
     */
    public static File getSettingsRoot() {
        if (settingsRoot == null) {
            initRootDir();
        }

        return settingsRoot;
    }


    /**
     *  Main program to test the FileSettings object
     *
     *@param  args  the command line arguments
     */
    public static void main(String[] args) {
        //  Make sure everything is installed properly
        (new RefactoryInstaller(false)).run();

        String key = "author";
        if (args.length > 0) {
            key = args[0];
        }

        String type = "pretty";
        if (args.length > 1) {
            type = args[1];
        }

        String app = "Refactory";
        if (args.length > 2) {
            app = args[2];
        }

        String project = "JRefactory";
        if (args.length > 3) {
            project = args[3];
        }

        System.out.println("Found:  " + (new FileSettings(project, app, type)).getString(key));
    }


    /**
     *  Initializes static variables
     */
    private static synchronized void initIfNecessary() {
        if (map == null) {
            map = new Hashtable();
            initRootDir();
        }
    }


    /**
     *  Initializes the root directory
     */
    private static void initRootDir() {
        if (settingsRoot != null) {
            return;
        }

        String javaHome = System.getProperty("jrefactory.home");
        if (javaHome != null) {
            //System.out.println("Home:  " + javaHome);
            settingsRoot = new File(javaHome);
            return;
        }

        javaHome = System.getProperty("user.home");
        if (javaHome != null) {
            //System.out.println("Home:  " + javaHome);
            settingsRoot = new File(javaHome);
            return;
        }

        settingsRoot = new File("~/");
        if (settingsRoot.exists()) {
            //System.out.println("Home:  ~/");
            return;
        }

        settingsRoot = new File("C:\\winnt\\profiles");
        if (settingsRoot.exists()) {
            File attempt = new File(settingsRoot, System.getProperty("user.name"));
            if (attempt.exists()) {
                //System.out.println("Home:  C:\\winnt\\profiles\\currentuser");
                settingsRoot = attempt;
                return;
            }
        }

        settingsRoot = new File("c:\\windows");
        //System.out.println("Home:  C:\\windows");
    }


    /**
     *  Return the file we are monitoring
     *
     *@return    The file value
     */
    public File getFile() {
        return file;
    }


    /**
     *  Description of the Method
     */
    private void reloadIfNecessary() {
        if (!isUpToDate()) {
            load();
        }
        reloadNow = false;
    }


    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public String toString() {
        return project + "::" + app + "::" + type + ", parent=" + parent;
    }
}

