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
package org.acm.seguin.uml.refactor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *  This adapter is resposible for keeping the signature in the dialog box
 *  relatively up to date.
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
class SignatureUpdateAdapter implements ListDataListener, ActionListener,
        FocusListener, ListSelectionListener, DocumentListener {
    private ExtractMethodDialog emd;


    /**
     *  Constructor for the SignatureUpdateAdapter object
     *
     *@param  init  the dialog box it is responsible for
     */
    public SignatureUpdateAdapter(ExtractMethodDialog init) {
        emd = init;
    }


    /**
     *  Description of the Method
     *
     *@param  e  Description of Parameter
     */
    public void intervalAdded(ListDataEvent e) {
        emd.update();
    }


    /**
     *  Description of the Method
     *
     *@param  e  Description of Parameter
     */
    public void intervalRemoved(ListDataEvent e) {
        emd.update();
    }


    /**
     *  Description of the Method
     *
     *@param  e  Description of Parameter
     */
    public void contentsChanged(ListDataEvent e) {
        emd.update();
    }


    /**
     *  Description of the Method
     *
     *@param  e  Description of Parameter
     */
    public void actionPerformed(ActionEvent e) {
        emd.update();
    }


    /**
     *  Description of the Method
     *
     *@param  e  Description of Parameter
     */
    public void focusGained(FocusEvent e) { }


    /**
     *  Description of the Method
     *
     *@param  e  Description of Parameter
     */
    public void focusLost(FocusEvent e) {
        emd.update();
    }


    /**
     *  Someone selected something in the list box
     *
     *@param  e  Description of Parameter
     */
    public void valueChanged(ListSelectionEvent e) {
        emd.update();
    }


    /**
     *  Document listener event
     *
     *@param  evt  Description of Parameter
     */
    public void insertUpdate(DocumentEvent evt) {
        emd.update();
    }


    /**
     *  Document listener event
     *
     *@param  e  Description of Parameter
     */
    public void removeUpdate(DocumentEvent e) {
        emd.update();
    }


    /**
     *  Document listener event
     *
     *@param  e  Description of Parameter
     */
    public void changedUpdate(DocumentEvent e) {
        emd.update();
    }
}
