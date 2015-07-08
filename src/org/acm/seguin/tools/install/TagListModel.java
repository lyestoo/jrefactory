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
package org.acm.seguin.tools.install;

import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.AbstractListModel;

/**
 *  List model that stores the tags
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public class TagListModel extends AbstractListModel {
    private ArrayList list;


    /**
     *  Constructor for the TagListModel object
     */
    public TagListModel() {
        list = new ArrayList();
    }


    /**
     *  Gets the Size attribute of the TagListModel object
     *
     *@return    The Size value
     */
    public int getSize() {
        return list.size();
    }


    /**
     *  Gets the ElementAt attribute of the TagListModel object
     *
     *@param  index  Description of Parameter
     *@return        The ElementAt value
     */
    public Object getElementAt(int index) {
        if (index < 0) {
            return null;
        }
        if (index >= list.size()) {
            return null;
        }

        return list.get(index);
    }


    /**
     *  Description of the Method
     *
     *@param  name  Description of Parameter
     *@return       Description of the Returned Value
     */
    public TagLinePanel find(String name) {
        int last = getSize();
        for (int ndx = 0; ndx < last; ndx++) {
            TagLinePanel tlp = (TagLinePanel) list.get(ndx);
            if (tlp.getTagName().equals(name)) {
                return tlp;
            }
        }
        return null;
    }


    /**
     *  Description of the Method
     *
     *@param  name  Description of Parameter
     */
    public void remove(String name) {
        TagLinePanel tlp = find(name);
        if (tlp != null) {
            list.remove(tlp);
        }
    }


    /**
     *  Adds a tag
     *
     *@param  tlp  Description of Parameter
     */
    public void add(TagLinePanel tlp) {
        list.add(tlp);
    }


    /**
     *  Gets the list of items
     *
     *@return    Description of the Returned Value
     */
    public Iterator iterator() {
        return list.iterator();
    }


    /**
     *  Swaps two entries
     *
     *@param  index1  Description of Parameter
     *@param  index2  Description of Parameter
     */
    public void swap(int index1, int index2) {
        Object temp = list.get(index1);
        list.set(index1, list.get(index2));
        list.set(index2, temp);
    }


    /**
     *  Empty out the list model
     */
    public void clearAll() {
        list.clear();
    }
}

//  This is the end of the file

