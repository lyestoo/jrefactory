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
package org.acm.seguin.summary;

import java.io.File;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *  Creates a summary of a package
 *
 *@author     Chris Seguin
 *@created    May 5, 1999
 *@since      2.6.31
 */
public class PackageSummary extends Summary {
    private LinkedList fileList;
    //  Instance Variables
    private String name;

    //  Class Variables
    private static HashMap packageMap;


    /**
     *  Constructor for the package summary
     *
     *@since               2.6.31
     *@param  packageName  the name of the package
     */
    protected PackageSummary(String packageName) {
        //  Initialize package summary - packages have no parents
        super(null);

        name = packageName.intern();
        fileList = null;
    }


    /**
     *  Get a package summary object
     *
     *@since     2.6.31
     *@return    all package summaries
     */
    public static Iterator getAllPackages() {
        if (packageMap == null) {
            init();
        }

        return packageMap.values().iterator();
    }


    /**
     *  Get the directory associated with this package
     *
     *@since     2.6.31
     *@return    a file or null if none
     */
    public File getDirectory() {
        Iterator iter = getFileSummaries();
        if (iter == null) {
            return null;
        }

        while (iter.hasNext()) {
            FileSummary next = (FileSummary) iter.next();
            File result = next.getFile();
            if (result != null) {
                result = result.getParentFile();
                if (result != null) {
                    return result;
                }
            }
        }

        return null;
    }


    /**
     *  Return an iterator of the files
     *
     *@since     2.6.31
     *@return    the iterator
     */
    public Iterator getFileSummaries() {
        if (fileList == null) {
            return null;
        }

        return fileList.iterator();
    }


    /**
     *  Get a file summary by file name
     *
     *@since        2.6.31
     *@param  name  the name of the file summary
     *@return       the file summary if it is found and null otherwise
     */
    public FileSummary getFileSummary(String name) {
        //  Check for null pointers
        if (name == null) {
            return null;
        }

        //  Local Variables
        if (fileList != null) {
            Iterator iter = fileList.iterator();

            //  Check for it
            while (iter.hasNext()) {
                FileSummary next = (FileSummary) iter.next();
                if (name.equals(next.getName())) {
                    return next;
                }
            }
        }

        //  Hmm...  not found
        return null;
    }


    /**
     *  Get the name of the package
     *
     *@since     2.6.31
     *@return    the package name
     */
    public String getName() {
        return name;
    }


    /**
     *  Get a package summary object
     *
     *@since        2.6.31
     *@param  name  the name of the package that we are creating
     *@return       The PackageSummary value
     */
    public static PackageSummary getPackageSummary(String name) {
        if (packageMap == null) {
            init();
        }

        PackageSummary result = (PackageSummary) packageMap.get(name);
        if (result == null) {
            result = new PackageSummary(name);
            packageMap.put(name, result);
        }

        return result;
    }


    /**
     *  Determines if it is the top level package
     *
     *@since     2.6.31
     *@return    true if it is the top level
     */
    public boolean isTopLevel() {
        return ((name == null) || (name.length() == 0));
    }


    /**
     *  Provide method to visit a node
     *
     *@since           2.6.31
     *@param  visitor  the visitor
     *@param  data     the data for the visit
     *@return          some new data
     */
    public Object accept(SummaryVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }


    /**
     *  Add a file summary
     *
     *@since               2.6.31
     *@param  fileSummary  the file summary that we are adding
     */
    protected void addFileSummary(FileSummary fileSummary) {
        if (fileSummary != null) {
            if (fileList == null) {
                initFileList();
            }

            fileList.add(fileSummary);
        }
    }


    /**
     *  Delete a file summary
     *
     *@since               2.6.31
     *@param  fileSummary  the file summary object that we are removing
     */
    public void deleteFileSummary(FileSummary fileSummary) {
        if (fileSummary != null) {
            if (fileList == null) {
                initFileList();
            }

            fileList.remove(fileSummary);
        }
    }


    /**
     *  Initialization method
     *
     *@since    2.6.31
     */
    private static void init() {
        if (packageMap == null) {
            packageMap = new HashMap();
        }
    }


    /**
     *  Initialize the file list
     *
     *@since    2.6.31
     */
    private void initFileList() {
        fileList = new LinkedList();
    }


    /**
     *  Loads all the packages from the object input stream
     *
     *@since                   2.6.31
     *@param  in               Description of Parameter
     *@exception  IOException  Description of Exception
     */
    public static void loadAll(ObjectInputStream in) throws IOException {
        try {
            packageMap = (HashMap) in.readObject();

            if ((packageMap == null) || (packageMap.values() == null)) {
                return;
            }

            Iterator iter = packageMap.values().iterator();
            while (iter.hasNext()) {
                System.out.print("*");
                PackageSummary nextPackage = (PackageSummary) iter.next();
                Iterator iter2 = nextPackage.getFileSummaries();
                while ((iter2 != null) && iter2.hasNext()) {
                    System.out.print(".");
                    FileSummary nextFile = (FileSummary) iter2.next();
                    FileSummary.register(nextFile);
                }
            }

            System.out.println(" ");
        }
        catch (ClassNotFoundException cnfe) {
            packageMap = null;
            cnfe.printStackTrace(System.out);
        }
    }


    /**
     *  Removes all the packages to allow them to be reloaded
     *
     *@since    2.6.31
     */
    public static void removeAll() {
        packageMap = null;
        init();
        FileSummary.removeAll();
    }


    /**
     *  Saves all the packages to an object output stream
     *
     *@since                   2.6.31
     *@param  out              Description of Parameter
     *@exception  IOException  Description of Exception
     */
    public static void saveAll(ObjectOutputStream out) throws IOException {
        out.writeObject(packageMap);
    }


    /**
     *  Converts this object to a string
     *
     *@since     2.6.31
     *@return    the string
     */
    public String toString() {
        if (!isTopLevel()) {
            return name;
        }
        else {
            return "<Top Level Package>";
        }
    }
}
//  EOF
