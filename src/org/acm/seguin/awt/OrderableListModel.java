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
package org.acm.seguin.awt;

import javax.swing.AbstractListModel;
import javax.swing.JList;

/**
 *  Contains a list of items for a list box that can be reordered
 *
 *@author     Chris Seguin
 *@created    October 18, 2001
 */
class OrderableListModel extends AbstractListModel {
    private Object[] data;
    private JList list;


    /**
     *  Constructor for the OrderableListModel object
     */
    public OrderableListModel() { }


    /**
     *  Sets the Data attribute of the OrderableListModel object
     *
     *@param  value  The new Data value
     */
    public void setData(Object[] value) {
        data = value;
    }


    /**
     *  Sets the List attribute of the OrderableListModel object
     *
     *@param  value  The new List value
     */
    public void setList(JList value) {
        list = value;
    }


    /**
     *  Gets the Data attribute of the OrderableListModel object
     *
     *@return    The Data value
     */
    public Object[] getData() {
        return data;
    }


    /**
     *  Gets the Element At from the data array
     *
     *@param  index  the index into the array
     *@return        The ElementAt value
     */
    public Object getElementAt(int index) {
        return data[index];
    }


    /**
     *  Gets the Size attribute of the OrderableListModel object
     *
     *@return    The Size value
     */
    public int getSize() {
        return data.length;
    }


    /**
     *  Swaps two items in the list box
     *
     *@param  first   the first one
     *@param  second  the second one
     */
    public void swap(int first, int second) {
        Object temp = data[first];
        data[first] = data[second];
        data[second] = temp;
        fireContentsChanged(this,
                Math.min(first, second),
                Math.max(first, second));
    }
}
//  EOF
