/*
 *====================================================================
 *The JRefactory License, Version 1.0
 *
 *Copyright (c) 2001 JRefactory.  All rights reserved.
 *
 *Redistribution and use in source and binary forms, with or without
 *modification, are permitted provided that the following conditions
 *are met:
 *
 *1. Redistributions of source code must retain the above copyright
 *notice, this list of conditions and the following disclaimer.
 *
 *2. Redistributions in binary form must reproduce the above copyright
 *notice, this list of conditions and the following disclaimer in
 *the documentation and/or other materials provided with the
 *distribution.
 *
 *3. The end-user documentation included with the redistribution,
 *if any, must include the following acknowledgment:
 *"This product includes software developed by the
 *JRefactory (http://www.sourceforge.org/projects/jrefactory)."
 *Alternately, this acknowledgment may appear in the software itself,
 *if and wherever such third-party acknowledgments normally appear.
 *
 *4. The names "JRefactory" must not be used to endorse or promote
 *products derived from this software without prior written
 *permission. For written permission, please contact seguin@acm.org.
 *
 *5. Products derived from this software may not be called "JRefactory",
 *nor may "JRefactory" appear in their name, without prior written
 *permission of Chris Seguin.
 *
 *THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *DISCLAIMED.  IN NO EVENT SHALL THE CHRIS SEGUIN OR
 *ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *SUCH DAMAGE.
 *====================================================================
 *
 *This software consists of voluntary contributions made by many
 *individuals on behalf of JRefactory.  For more information on
 *JRefactory, please see
 *<http://www.sourceforge.org/projects/jrefactory>.
 */
package org.acm.seguin.tools.install;

import java.io.*;
import java.util.*;

import org.acm.seguin.tools.RefactoryInstaller;

/**
 *  Parses the pretty.settings file to generate the blocks
 *
 * @author   Chris Seguin
 * @created  September 12, 2001
 */
public class PrettySettingsParser {
    private String filename;
    private LinkedList list;

    /**  Constructor for the PrettySettingsParser object */
    public PrettySettingsParser() {
        list = new LinkedList();
    }

    /**
     *  Sets the Filename attribute of the PrettySettingsParser object
     *
     * @paramvalue  The new Filename value
     */
    public void setFilename(String value) {
        filename = value;
    }

    /**
     *  Main processing method for the PrettySettingsParser object
     */
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new FileReader(filename));

            String line = input.readLine();

            while (line != null) {
                if (line.length() == 0)
                    list.clear();
                else if (line.charAt(0) == '#')
                    addToDescription(line);
                else
                    processKey(line);

                line = input.readLine();
            }

            input.close();
        }
        catch (IOException ioe) {
            ioe.printStackTrace(System.out);
        }
    }

    /**
     *  Gets the IndexedOption attribute of the PrettySettingsParser object
     *
     * @paramvalue  Description of Parameter
     * @return      The IndexedOption value
     */
    private boolean isIndexedOption(String value) {
        try {
            Integer.parseInt(value);
        }
        catch (Exception exc) {
            return false;
        }

        return true;
    }

    /**
     *  Description of the Method
     *
     * @paramline             Description of Parameter
     * @exceptionIOException  Description of Exception
     */
    private void processKey(String line) throws IOException {
        int equalsLocation = line.indexOf('=');
        String key = line.substring(0, equalsLocation);
        String value = line.substring(equalsLocation + 1).trim();

        if (key.equals("version"))
            return;

        StringTokenizer tok = new StringTokenizer(key, ".");
        StringBuffer buffer = new StringBuffer();

        while (tok.hasMoreTokens()) {
            String next = tok.nextToken();

            buffer.append(Character.toUpperCase(next.charAt(0)));
            buffer.append(next.substring(1));
        }
        buffer.append("Panel");

        String name = buffer.toString();
        String classExtension = "TextPanel";
        String currentValue = value.trim().toLowerCase();

        if (currentValue.equals("true") || currentValue.equals("false"))
            classExtension = "TogglePanel";
        else if (containsOption())
            classExtension = "OptionPanel";
        else if (isIndexedOption(currentValue))
            classExtension = "IndexedPanel";

        PrintWriter output = new PrintWriter(new FileWriter(name + ".java"));

        output.println("package org.acm.seguin.tools.install;");
        output.println("public class " + name + " extends " + classExtension + "{");
        output.println("\tpublic " + name + "() {");
        output.println("\t\tsuper();");
        printDescription(output);
        output.println("\t\taddControl();");
        output.println("\t}");
        output.println("\tpublic String getKey() { return \"" + key + "\"; }");
        output.println("\tprotected String getInitialValue() { return \"" + value + "\"; }");
        output.println("}");
        output.flush();
        output.close();
    }

    /**
     *  Prints the description
     *
     * @paramoutput  Description of Parameter
     */
    private void printDescription(PrintWriter output) {
        Iterator iter = list.iterator();

        while (iter.hasNext()) {
            String next = (String) iter.next();

            next = next.trim();
            if (next.charAt(0) == '*') {
                int indexMinus = next.indexOf('-');
                String key = next.substring(1, indexMinus).trim();
                String descr = next.substring(indexMinus + 1).trim();

                output.println("\t\taddOption(\"" + key + "\", \"" + descr + "\");");
            }
            else
                output.println("\t\taddDescription(\"" + next + "\");");

        }
    }

    /**
     *  Determines if this is an option
     *
     * @return  Description of the Returned Value
     */
    private boolean containsOption() {
        Iterator iter = list.iterator();

        while (iter.hasNext()) {
            String next = (String) iter.next();

            if (next.indexOf("*") >= 0)
                return true;
        }
        return false;
    }

    /**
     *  Adds a feature to the ToDescription attribute of the
     *  PrettySettingsParser object
     *
     * @paramline  The feature to be added to the ToDescription attribute
     */
    private void addToDescription(String line) {
        String message = line.substring(1);

        if (!((message == null) || (message.trim().length() == 0)))
            list.add(message);

    }

    /**
     *  The main program for the PrettySettingsParser class
     *
     * @paramargs  The command line arguments
     */
    public static void main(String[] args) {
        (new RefactoryInstaller(false)).run();

        PrettySettingsParser psp = new PrettySettingsParser();

        psp.setFilename(args[0]);
        psp.run();
    }
}

//  This is the end of the file

