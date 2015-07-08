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
package org.acm.seguin.tools.build;

import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 *  Creates a JRefactoryVersion object from a command line string specifying the
 *  version.
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public class CreateVersion {
    private String major;
    private String minor;
    private String build;

    private String output;


    /**
     *  Constructor for the CreateVersion object
     *
     *@param  input   the command line argument
     *@param  output  Description of the Parameter
     */
    public CreateVersion(String input, String output) {
        StringTokenizer tok = new StringTokenizer(input, ".");

        major = tok.nextToken();
        minor = tok.nextToken();
        build = tok.nextToken();

        this.output = output;
        System.out.println(output);
    }


    /**
     *  Main processing method for the CreateVersion object
     */
    public void run() {
        try {
            java.io.File op = new java.io.File(output);
            System.out.println("op="+op.getCanonicalFile());
            PrintWriter printer = new PrintWriter(new FileWriter(op));

            printer.println("package org.acm.seguin;");
            printer.println("");
            printer.println("/**");
            printer.println(" *  The current version of JRefactory");
            printer.println(" *");
            printer.println(" *@author    Chris Seguin");
            printer.println(" */");
            printer.println("public class JRefactoryVersion {");
            printer.println("	/**");
            printer.println("	 *  Gets the MajorVersion attribute of the JRefactoryVersion object");
            printer.println("	 *");
            printer.println("	 *@return    The MajorVersion value");
            printer.println("	 */");
            printer.println("	public int getMajorVersion() {");
            printer.println("		return " + major + ";");
            printer.println("	}");
            printer.println("");
            printer.println("");
            printer.println("	/**");
            printer.println("	 *  Gets the MinorVersion attribute of the JRefactoryVersion object");
            printer.println("	 *");
            printer.println("	 *@return    The MinorVersion value");
            printer.println("	 */");
            printer.println("	public int getMinorVersion() {");
            printer.println("		return " + minor + ";");
            printer.println("	}");
            printer.println("");
            printer.println("");
            printer.println("	/**");
            printer.println("	 *  Gets the Build attribute of the JRefactoryVersion object");
            printer.println("	 *");
            printer.println("	 *@return    The Build value");
            printer.println("	 */");
            printer.println("	public int getBuild() {");
            printer.println("		return " + build + ";");
            printer.println("	}");
            printer.println("");
            printer.println("");
            printer.println("	/**");
            printer.println("	 *  Converts the JRefactoryVersion to a string");
            printer.println("	 *");
            printer.println("	 *@return    a string representing the version");
            printer.println("	 */");
            printer.println("	public String toString() {");
            printer.println("		StringBuffer buffer = new StringBuffer();");
            printer.println("");
            printer.println("		buffer.append(getMajorVersion());");
            printer.println("		buffer.append('.');");
            printer.println("");
            printer.println("		buffer.append(getMinorVersion());");
            printer.println("		buffer.append('.');");
            printer.println("");
            printer.println("		buffer.append(getBuild());");
            printer.println("");
            printer.println("		return buffer.toString();");
            printer.println("	}");
            printer.println("");
            printer.println("   public static void main(String[] args) {");
            printer.println("       System.out.println(\"Version:  \" + (new JRefactoryVersion()).toString());");
            printer.println("   }");
            printer.println("}");

            printer.flush();
            printer.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    /**
     *  The main program for the CreateVersion class
     *
     *@param  args  The command line arguments
     */
    public static void main(String[] args) {
        CreateVersion cv = new CreateVersion(args[0], args[1]);
        cv.run();
    }
}
