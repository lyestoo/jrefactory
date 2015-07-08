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
package org.acm.seguin.version;

import javax.swing.JOptionPane;

/**
 *  User directed version control
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public class UserDirectedVersionControl implements VersionControl {
    /**
     *  Determines if a file is under version control
     *
     *@param  fullFilename  The full path of the file
     *@return               Returns true if the files is under version control
     */
    public boolean contains(String fullFilename) {
        return (JOptionPane.YES_OPTION ==
                JOptionPane.showConfirmDialog(null,
                "Does your source control system contain\n" + fullFilename +
                "?",
                "Contains",
                JOptionPane.YES_NO_OPTION));
    }


    /**
     *  Adds a file to version control
     *
     *@param  fullFilename  the file to add
     */
    public void add(String fullFilename) {
        JOptionPane.showMessageDialog(null,
                "Please add\n" + fullFilename +
                "\nfrom your version control system",
                "Add",
                JOptionPane.QUESTION_MESSAGE);
    }


    /**
     *  Checks in a file
     *
     *@param  fullFilename  the file to check in
     */
    public void checkIn(String fullFilename) {
        JOptionPane.showMessageDialog(null,
                "Please check in\n" + fullFilename +
                "\nto your version control system",
                "Check in",
                JOptionPane.QUESTION_MESSAGE);
    }


    /**
     *  Check out a file
     *
     *@param  fullFilename  the file to check out
     */
    public void checkOut(String fullFilename) {
        JOptionPane.showMessageDialog(null,
                "Please check out\n" + fullFilename +
                "\nfrom your version control system",
                "Check out",
                JOptionPane.QUESTION_MESSAGE);
    }
}
