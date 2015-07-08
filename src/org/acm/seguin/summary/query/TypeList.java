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
package org.acm.seguin.summary.query;

import org.acm.seguin.summary.PackageSummary;
import org.acm.seguin.summary.FileSummary;
import org.acm.seguin.summary.TypeSummary;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *  Determines if a package contains a certain type
 *
 *@author     Chris Seguin
 *@created    November 22, 1999
 */
public abstract class TypeList {
    /**
     *  Places the query
     *
     *@param  packageName  the name of the package
     *@return              the list of types
     */
    public LinkedList query(String packageName) {
        return query(PackageSummary.getPackageSummary(packageName));
    }


    /**
     *  Places the query
     *
     *@param  summary  the package summary
     *@return          the list of types
     */
    public LinkedList query(PackageSummary summary) {
        LinkedList list = new LinkedList();

        Iterator iter = summary.getFileSummaries();
        if (iter != null) {
            while (iter.hasNext()) {
                FileSummary fileSummary = (FileSummary) iter.next();

                if (isIncluded(fileSummary)) {
                    add(fileSummary, list);
                }
            }
        }

        return list;
    }


    /**
     *  Determines if the types in the file should be included or not
     *
     *@param  summary  the summary to check
     *@return          true if it should be included
     */
    protected abstract boolean isIncluded(FileSummary summary);


    /**
     *  Determines if the types in the file should be included or not
     *
     *@param  summary  the summary to check
     *@return          true if it should be included
     */
    protected abstract boolean isIncluded(TypeSummary summary);


    /**
     *  Adds the type in a certain file
     *
     *@param  summary  the file summary
     *@param  list     the linked list to add to
     */
    private void add(FileSummary summary, LinkedList list) {
        Iterator iter = summary.getTypes();
        if (iter != null) {
            while (iter.hasNext()) {
                TypeSummary typeSummary = (TypeSummary) iter.next();
                if (isIncluded(typeSummary)) {
                    list.add(typeSummary.getName());
                }
            }
        }
    }
}
