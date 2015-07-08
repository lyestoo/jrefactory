/*
 *  ====================================================================
 *  The JRefactory License, Version 1.0
 *
 *  Copyright (c) 2001 JRefactory.  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  JRefactory (http://www.sourceforge.org/projects/jrefactory)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "JRefactory" must not be used to endorse or promote
 *  products derived from this software without prior written
 *  permission. For written permission, please contact seguin@acm.org.
 *
 *  5. Products derived from this software may not be called "JRefactory",
 *  nor may "JRefactory" appear in their name, without prior written
 *  permission of Chris Seguin.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE CHRIS SEGUIN OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of JRefactory.  For more information on
 *  JRefactory, please see
 *  <http://www.sourceforge.org/projects/jrefactory>.
 */
package org.acm.seguin.awt;


import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.JOptionPane;


/**
 *  Prints exceptions
 *
 *@author     Chris Seguin
 *@created    October 18, 2001
 *@since      2.6.33
 */
public abstract class ExceptionPrinter
{
    private int count;
    private static ExceptionPrinter singleton = null;


    /**
     *  Constructor for the ExceptionPrinter object
     *
     *@since    2.6.33
     */
    public ExceptionPrinter()
    {
        count = 0;
    }


    /**
     *  Prints exceptions
     *
     *@param  exc          the exception to be printed
     *@param  interactive  Description of the Parameter
     *@since               2.6.33
     */
    public abstract void printException(Throwable exc, boolean interactive);


    /**
     *  Gets the exceptionsPrinted attribute of the ExceptionPrinter class
     *
     *@return    The exceptionsPrinted value
     *@since     2.6.33
     */
    public static int getExceptionsPrinted()
    {
        if (singleton == null) {
            singleton = new GUIExceptionPrinter();
        }

        return singleton.count;
    }


    /**
     *  Prints exceptions
     *
     *@param  exc          the exception to be printed
     *@param  interactive  Is this interactive
     *@since               2.6.33
     */
    public static void print(Throwable exc, boolean interactive)
    {
        if (singleton == null) {
            singleton = new GUIExceptionPrinter();
        }

        singleton.printException(exc, interactive);
        singleton.count++;
    }


    /**
     *  Description of the Method
     *
     *@param  printer  Description of the Parameter
     *@since           2.6.33
     */
    public static void register(ExceptionPrinter printer)
    {
        singleton = printer;
    }
}
//  EOF
