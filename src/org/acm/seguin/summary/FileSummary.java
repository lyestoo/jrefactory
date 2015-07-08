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
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import org.acm.seguin.parser.ast.ASTTypeDeclaration;
import org.acm.seguin.parser.ast.SimpleNode;
import org.acm.seguin.parser.factory.BufferParserFactory;
import org.acm.seguin.parser.factory.FileParserFactory;
import org.acm.seguin.parser.factory.InputStreamParserFactory;
import org.acm.seguin.parser.factory.ParserFactory;

/**
 *  Stores a summary of a java file
 *
 *@author     Chris Seguin
 *@author     <a href="JRefactory@ladyshot.demon.co.uk">Mike Atkinson</a>
 *@version    $Id: FileSummary.java,v 1.4 2003/07/29 20:51:57 mikeatkinson Exp $ 
 *@created    June 6, 1999
 *@since      2.6.31
 */
public class FileSummary extends Summary {
    private boolean delete;

    //  Class Variables
    private static HashMap fileMap;
    private ArrayList importList;
    private boolean isMoving;
    private Date lastModified;
    //  Instance Variables
    private File theFile;
    private LinkedList typeList;


    /**
     *  Creates a file map
     *
     *@since                 2.6.31
     *@param  parentSummary  the parent summary
     *@param  initFile       the file
     */
    protected FileSummary(Summary parentSummary, File initFile) {
        //  Initialize parent class
        super(parentSummary);

        //  Set instance Variables
        theFile = initFile;
        importList = null;
        typeList = null;
        isMoving = false;
        delete = false;
        lastModified = new Date();
    }


    /**
     *  Mark whether this file should be deleted
     *
     *@since       2.6.31
     *@param  way  the way that this parameter is changing
     */
    public void setDeleted(boolean way) {
        delete = way;
    }


    /**
     *  Change whether this file is moving or not
     *
     *@since       2.6.31
     *@param  way  the way that this parameter is changing
     */
    public void setMoving(boolean way) {
        isMoving = way;
    }


    /**
     *  Return the file
     *
     *@since     2.6.31
     *@return    the file object
     */
    public File getFile() {
        return theFile;
    }


    /**
     *  Get the file summary for a particular file
     *
     *@since        2.6.31
     *@param  file  the file we are looking up
     *@return       the file summary
     */
    public static FileSummary getFileSummary(File file) {
        if (fileMap == null) {
            init();
        }

        FileSummary result = (FileSummary) fileMap.get(getKey(file));
        if (result == null) {
            SummaryLoaderState state = loadNewFileSummary(file);
            result = linkFileSummary(state, file);
        }
        else {
            Date currentModificationTime = new Date(file.lastModified());
            if (currentModificationTime.after(result.lastModified)) {
                resetFileSummary(file, result);
                result.lastModified = new Date(file.lastModified());

                //  Create a new file summary object
                ParserFactory factory = new FileParserFactory(file);
                SimpleNode root = factory.getAbstractSyntaxTree(false);
                if (root == null) {
                    return null;
                }
                reloadFileSummary(file, result, root);
            }
        }

        return result;
    }


    /**
     *  Get the file summary for a particular file
     *
     *@since          2.6.31
     *@param  buffer  the buffer that is used to load the summary
     *@return         the file summary
     */
    public static FileSummary getFileSummary(String buffer) {
        //  Create a new file summary object
        ParserFactory factory = new BufferParserFactory(buffer);
        SimpleNode root = factory.getAbstractSyntaxTree(false);
        if ((root == null) || (!hasType(root))) {
            return null;
        }

        //  Start the summary
        SummaryLoaderState state = new SummaryLoaderState();
        state.setFile(null);
        root.jjtAccept(new SummaryLoadVisitor(), state);

        //  Associate them together
        FileSummary result = (FileSummary) state.getCurrentSummary();
        ((PackageSummary) result.getParent()).addFileSummary(result);

        return result;
    }


    /**
     *  Return the list of imports
     *
     *@since     2.6.31
     *@return    an iterator containing the imports
     */
    public Iterator getImports() {
        if (importList == null) {
            return null;
        }

        return importList.iterator();
    }


    /**
     *  Get the key that is used to index the files
     *
     *@since        2.6.31
     *@param  file  the file we are using to find the key
     *@return       the key
     */
    protected static String getKey(File file) {
        try {
            return file.getCanonicalPath();
        }
        catch (IOException ioe) {
            return "Unknown";
        }
    }


    /**
     *  Return the name of the file
     *
     *@since     2.6.31
     *@return    a string containing the name
     */
    public String getName() {
        if (theFile == null) {
            return "";
        }

        return theFile.getName();
    }


    /**
     *  Counts the types stored in the file
     *
     *@since     2.6.31
     *@return    the number of types in this file
     */
    public int getTypeCount() {
        if (typeList == null) {
            return 0;
        }

        return typeList.size();
    }


    /**
     *  Get the list of types stored in this file
     *
     *@since     2.6.31
     *@return    an iterator over the types
     */
    public Iterator getTypes() {
        if (typeList == null) {
            return null;
        }

        return typeList.iterator();
    }


    /**
     *  Has this file been deleted
     *
     *@since     2.6.31
     *@return    true if the file is deleted
     */
    public boolean isDeleted() {
        return delete;
    }


    /**
     *  Is this file moving to a new package
     *
     *@since     2.6.31
     *@return    true if the file is moving
     */
    public boolean isMoving() {
        return isMoving;
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
     *  Add an import summary
     *
     *@since                 2.6.31
     *@param  importSummary  the summary of what was imported
     */
    protected void add(ImportSummary importSummary) {
        if (importSummary != null) {
            //  Initialize the import list
            if (importList == null) {
                initImportList();
            }

            //  Add it in
            importList.add(importSummary);
        }
    }


    /**
     *  Add an type summary
     *
     *@since               2.6.31
     *@param  typeSummary  the summary of the type
     */
    protected void add(TypeSummary typeSummary) {
        if (typeSummary != null) {
            //  Initialize the type list
            if (typeList == null) {
                initTypeList();
            }

            //  Add it to the list
            typeList.add(typeSummary);
        }
    }


    /**
     *  Scans through the tree looking for a type declaration
     *
     *@since        2.6.31
     *@param  root  the root of the abstract syntax tree
     *@return       true if there is a type node present
     */
    private static boolean hasType(SimpleNode root) {
        int last = root.jjtGetNumChildren();
        for (int ndx = 0; ndx < last; ndx++) {
            if (root.jjtGetChild(ndx) instanceof ASTTypeDeclaration) {
                return true;
            }
        }
        return false;
    }


    /**
     *  Initialization method
     *
     *@since    2.6.31
     */
    private static void init() {
        if (fileMap == null) {
            fileMap = new HashMap();
        }
    }


    /**
     *  Initialize the import list
     *
     *@since    2.6.31
     */
    private void initImportList() {
        importList = new ArrayList();
    }


    /**
     *  Initialize the type list
     *
     *@since    2.6.31
     */
    private void initTypeList() {
        typeList = new LinkedList();
    }


    /**
     *  Description of the Method
     *
     *@since         2.6.31
     *@param  state  Description of Parameter
     *@param  file   Description of Parameter
     *@return        Description of the Returned Value
     */
    private static FileSummary linkFileSummary(SummaryLoaderState state, File file) {
        FileSummary result;

        //  Associate them together
        result = (FileSummary) state.getCurrentSummary();
        ((PackageSummary) result.getParent()).addFileSummary(result);

        //  Store it away
        fileMap.put(getKey(file), result);

        return result;
    }


    /**
     *  Description of the Method
     *
     *@since        2.6.31
     *@param  file  Description of Parameter
     *@return       Description of the Returned Value
     */
    private static SummaryLoaderState loadNewFileSummary(File file) {
        //  Create a new file summary object
        ParserFactory factory = new FileParserFactory(file);
        SimpleNode root = factory.getAbstractSyntaxTree(false);
        if (root == null) {
            return null;
        }

        //  Start the summary
        SummaryLoaderState state = new SummaryLoaderState();
        state.setFile(file);
        root.jjtAccept(new SummaryLoadVisitor(), state);
        return state;
    }


    /**
     *  Registers a single new file. This method is used by the rapid metadata
     *  reloader
     *
     *@since           2.6.31
     *@param  summary  Description of Parameter
     */
    static void register(FileSummary summary) {
        if (fileMap == null) {
            init();
        }

        File file = summary.getFile();
        if (file == null) {
            return;
        }

        fileMap.put(getKey(file), summary);
    }


    /**
     *  Description of the Method
     *
     *@since          2.6.31
     *@param  file    Description of Parameter
     *@param  result  Description of Parameter
     *@param  root    Description of Parameter
     */
    private static void reloadFileSummary(File file, FileSummary result, SimpleNode root) {
        SummaryLoaderState state = new SummaryLoaderState();
        state.setFile(file);
        state.startSummary(result);
        state.setCode(SummaryLoaderState.LOAD_FILE);
        root.jjtAccept(new SummaryLoadVisitor(), state);
    }


    /**
     *  This method allows JBuilder to load a file summary from the buffer
     *
     *@since         2.6.31
     *@param  file   the file
     *@param  input  the input stream
     *@return        the file summary loaded
     */
    public static FileSummary reloadFromBuffer(File file, Reader input) {
        if (fileMap == null) {
            init();
        }

        if (file == null) {
            System.out.println("No file!");
            return null;
        }

        String key = getKey(file);
        if (key == null) {
            System.out.println("No key:  " + file.toString());
            return null;
        }

        FileSummary result = (FileSummary) fileMap.get(key);

        if (result == null) {
            System.out.println("No initial file summary");
            SummaryLoaderState state = loadNewFileSummary(file);
            result = linkFileSummary(state, file);

            //  If you still can't get something that makes sense abort
            if (result == null) {
                System.out.println("Unable to load the file summary from the file");
                return null;
            }
        }

        resetFileSummary(file, result);
        result.lastModified = new Date(file.lastModified());

        //  Create a new file summary object
        ParserFactory factory;
        if (input == null) {
            factory = new FileParserFactory(file);
        }
        else {
            factory = new InputStreamParserFactory(input, key);
        }
        SimpleNode root = factory.getAbstractSyntaxTree(false);
        if (root == null) {
            System.out.println("Unable to get a parse tree for this file:  Using existing file summary");
            return result;
        }
        reloadFileSummary(file, result, root);

        return result;
    }


    /**
     *  Removes all the files from the system
     *
     *@since    2.6.31
     */
    public static void removeAll() {
        fileMap = null;
        init();
    }


    /**
     *  Remove any file summaries that have been deleted
     *
     *@since    2.6.31
     */
    public static void removeDeletedSummaries() {
        if (fileMap == null) {
            init();
            return;
        }

        LinkedList temp = new LinkedList();
        Iterator keys = fileMap.values().iterator();
        while (keys.hasNext()) {
            FileSummary next = (FileSummary) keys.next();
            File file = next.getFile();
            if ((file != null) && !file.exists()) {
                temp.add(file);
            }
        }

        Iterator iter = temp.iterator();
        while (iter.hasNext()) {
            File next = (File) iter.next();
            removeFileSummary(next);
        }
    }


    /**
     *  Remove the file summary for a particular file
     *
     *@since        2.6.31
     *@param  file  the file we are looking up
     */
    public static void removeFileSummary(File file) {
        if (fileMap == null) {
            init();
        }

        String key = getKey(file);
        FileSummary fileSummary = (FileSummary) fileMap.get(key);
        if (fileSummary != null) {
            //  There is something to remove
            fileMap.remove(key);

            //  Get the parent
            PackageSummary parent = (PackageSummary) fileSummary.getParent();
            parent.deleteFileSummary(fileSummary);
        }
    }


    /**
     *  Description of the Method
     *
     *@since          2.6.31
     *@param  file    Description of Parameter
     *@param  result  Description of the Parameter
     */
    private static void resetFileSummary(File file, FileSummary result) {
        if (result == null) {
            return;
        }

        result.theFile = file;
        result.importList = null;
        result.typeList = null;
        result.isMoving = false;
        result.delete = false;
    }


    /**
     *  Description of the Method
     *
     *@since     2.6.31
     *@return    Description of the Returned Value
     */
    public String toString() {
        if (theFile == null) {
            return "FileSummary<Framework File>";
        }
        return "FileSummary<" + theFile.getName() + ">";
    }
}
//  EOF
