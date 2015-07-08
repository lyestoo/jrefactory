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
package org.acm.seguin.uml;

import java.util.TreeMap;
import java.util.Iterator;
import javax.swing.AbstractListModel;
import org.acm.seguin.summary.PackageSummary;

/**
 *  Holds a list of packages for the list box
 *
 *@author     Chris Seguin
 *@created    August 11, 1999
 */
public class PackageSummaryListModel extends AbstractListModel {
    //  Instance Variables
    private TreeMap orderedMap;
    private PackageSummary[] array;
    private boolean ready;


    /**
     *  Constructor for the PackageSummaryListModel object
     */
    public PackageSummaryListModel() {
        orderedMap = new TreeMap();
        ready = false;
        array = null;
    }


    /**
     *  Return the requested item
     *
     *@param  index  the index of the item required
     *@return        The object
     */
    public Object getElementAt(int index) {
        if (!ready) {
            prepare();
        }

        return array[index];
    }


    /**
     *  Return the number of items
     *
     *@return    The size
     */
    public int getSize() {
        return orderedMap.size();
    }


    /**
     *  Adds a package summary
     *
     *@param  summary  the new summary
     */
    public void add(PackageSummary summary) {
        orderedMap.put(summary.getName(), summary);
        ready = false;
    }


    /**
     *  Prepare to handle the incoming requests
     */
    private void prepare() {
        //  Check that we have enough array
        if ((array == null) || (array.length < orderedMap.size())) {
            array = new PackageSummary[orderedMap.size() + 5];
        }

        //  Load the array
        Iterator iter = orderedMap.values().iterator();
        int ndx = 0;
        while (iter.hasNext()) {
            array[ndx] = (PackageSummary) iter.next();
            ndx++;
        }

        //  Finish
        ready = true;
    }
}
