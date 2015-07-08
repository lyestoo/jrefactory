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

import java.util.Iterator;
import org.acm.seguin.summary.FileSummary;
import org.acm.seguin.summary.ImportSummary;
import org.acm.seguin.summary.PackageSummary;
import org.acm.seguin.summary.Summary;
import org.acm.seguin.summary.TypeSummary;

/**
 *  Determines if a particular type is imported
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public class ImportsType {
    /**
     *  Checks to see if the type is imported
     *
     *@param  summary  Description of Parameter
     *@param  type     Description of Parameter
     *@return          true if it is imported
     */
    public static boolean query(Summary summary, TypeSummary type) {
        //  Check the special cases first
        PackageSummary packageSummary = getPackageSummary(type);
        if (packageSummary.getName().equals("java.lang")) {
            return true;
        }

        PackageSummary destPackage = getPackageSummary(summary);
        if (packageSummary == destPackage) {
            return true;
        }

        //  Now we need to search the list of imports
        FileSummary fileSummary = getFileSummary(summary);
        Iterator iter = fileSummary.getImports();

        if (iter != null) {
            while (iter.hasNext()) {
                ImportSummary next = (ImportSummary) iter.next();
                if (packageSummary == next.getPackage()) {
                    if (next.getType() == null) {
                        return true;
                    } else if (next.getType().equals(type.getName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     *  Gets the FileSummary attribute of the ImportsType class
     *
     *@param  summary  Description of Parameter
     *@return          The FileSummary value
     */
    private static FileSummary getFileSummary(Summary summary) {
        Summary current = summary;
        while ((current != null) && !(current instanceof FileSummary)) {
            current = current.getParent();
        }

        return (FileSummary) current;
    }


    /**
     *  Gets the PackageSummary attribute of the ImportsType class
     *
     *@param  summary  Description of Parameter
     *@return          The PackageSummary value
     */
    private static PackageSummary getPackageSummary(Summary summary) {
        Summary current = summary;
        while ((current != null) && !(current instanceof PackageSummary)) {
            current = current.getParent();
        }

        return (PackageSummary) current;
    }
}
