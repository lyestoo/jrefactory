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
package org.acm.seguin.pretty.sort;

import org.acm.seguin.parser.Node;
import org.acm.seguin.parser.ast.ASTCompilationUnit;
import org.acm.seguin.parser.ast.ASTPackageDeclaration;
import org.acm.seguin.parser.ast.ASTImportDeclaration;
import org.acm.seguin.parser.ast.ASTTypeDeclaration;
import org.acm.seguin.parser.ast.ASTClassDeclaration;
import org.acm.seguin.parser.ast.ASTInterfaceDeclaration;
import org.acm.seguin.parser.ast.ASTName;
import org.acm.seguin.pretty.PrintData;

/**
 *  Sorts the items in a java file at the top level
 *
 *@author     Chris Seguin
 *@created    September 11, 2001
 */
public class TopLevelOrdering extends Ordering {
    private String[] importSortImportant;
    private String prefix;


    /**
     *  Constructor for the TopLevelOrdering object
     *
     *@param  node  Description of the Parameter
     *@param  data  Description of the Parameter
     */
    public TopLevelOrdering(ASTCompilationUnit node, PrintData data) {
        String packageName = "";
        if (node.jjtGetNumChildren() > 0) {
            Node firstNode = node.jjtGetFirstChild();
            if (firstNode instanceof ASTPackageDeclaration) {
                ASTName name = (ASTName) ((ASTPackageDeclaration)
                        firstNode).jjtGetFirstChild();
                packageName = name.getName() + '.';
            }
        }

        int current = -1;
        int periodCount = data.getImportSortNeighbourhood();
        if (periodCount > 0) {
            for (int ndx = 0; ndx < periodCount; ndx++) {
                current = packageName.indexOf('.', current + 1);
            }
            if (current == -1) {
                prefix = packageName;
            } else {
                prefix = packageName.substring(0, current);
            }
        } else {
            prefix = "";
        }
        importSortImportant = data.getImportSortImportant();
    }


    /**
     *  Description of the Method
     *
     *@param  one  Description of the Parameter
     *@param  two  Description of the Parameter
     *@return      Description of the Return Value
     */
    public int compare(Object one, Object two) {

        int oneIndex = getIndex(one);
        int twoIndex = getIndex(two);

        if (oneIndex > twoIndex) {
            return 1;
        } else if (oneIndex < twoIndex) {
            return -1;
        } else {
            return fineCompare(one, two);
        }
    }


    /**
     *  Return the index of the item in the order array
     *
     *@param  object  the object we are checking
     *@return         the objects index if it is found or 7 if it is not
     */
    protected int getIndex(Object object) {
        if (object instanceof ASTPackageDeclaration) {
            return 1;
        } else if (object instanceof ASTImportDeclaration) {
            return 2;
        } else if (object instanceof ASTTypeDeclaration) {
            ASTTypeDeclaration type = (ASTTypeDeclaration) object;
            Node child = type.jjtGetFirstChild();
            if (child instanceof ASTClassDeclaration) {
                ASTClassDeclaration classDecl = (ASTClassDeclaration) child;
                if (classDecl.isPublic()) {
                    return 3;
                } else {
                    return 4;
                }
            } else {
                ASTInterfaceDeclaration interfaceDecl = (ASTInterfaceDeclaration) child;
                if (interfaceDecl.isPublic()) {
                    return 3;
                } else {
                    return 4;
                }
            }
        }

        return 5;
    }


    /**
     *  Compare two items
     *
     *@param  import1  Description of the Parameter
     *@param  import2  Description of the Parameter
     *@return          1 if the first item is greater than the second, -1 if the
     *      first item is less than the second, and 0 otherwise.
     */
    private int compareImports(ASTImportDeclaration import1, ASTImportDeclaration import2) {
        String name1 = ((ASTName) import1.jjtGetFirstChild()).getName();
        String name2 = ((ASTName) import2.jjtGetFirstChild()).getName();

        int result = compareImportsByNeighbourhood(name1, name2);
        if (result == 0) {
            result = compareImportsByPackagePrecedence(name1, name2);
            if (result == 0) {
                result = name1.compareTo(name2);
            }
        }

        return result;
    }


    /**
     *  Description of the Method
     *
     *@param  name1  Description of the Parameter
     *@param  name2  Description of the Parameter
     *@return        Description of the Return Value
     */
    private int compareImportsByNeighbourhood(String name1, String name2) {
        return neighbourhoodOrder(name1) - neighbourhoodOrder(name2);
    }


    /**
     *  Description of the Method
     *
     *@param  name1  Description of the Parameter
     *@param  name2  Description of the Parameter
     *@return        Description of the Return Value
     */
    private int compareImportsByPackagePrecedence(String name1, String name2) {
        return packageOrder(name1) - packageOrder(name2);
    }


    /**
     *  Fine grain comparison based on knowing what the types are
     *
     *@param  obj1  the object
     *@param  obj2  the second object
     *@return       -1 if obj1 is less than obj2, 0 if they are the same, and +1
     *      if obj1 is greater than obj2
     */
    private int fineCompare(Object obj1, Object obj2) {
        if (obj1 instanceof ASTImportDeclaration) {
            return compareImports((ASTImportDeclaration) obj1, (ASTImportDeclaration) obj2);
        }

        return 0;
    }


    /**
     *  Description of the Method
     *
     *@param  name  Description of the Parameter
     *@return       Description of the Return Value
     */
    private int neighbourhoodOrder(String name) {
        if (name.startsWith(prefix)) {
            return 1;
        } else {
            return 0;
        }
    }


    /**
     *  Description of the Method
     *
     *@param  name  Description of the Parameter
     *@return       Description of the Return Value
     */
    private int packageOrder(String name) {
    	if (importSortImportant == null)
    		return 0;

    	int matchLength = 0;
    	int ord = importSortImportant.length;

    	for (int ndx = 0; ndx < importSortImportant.length; ndx++) {
    		if ((importSortImportant[ndx] != null) && name.startsWith(importSortImportant[ndx])) {
    			int currentLength = importSortImportant[ndx].length();
    			if (currentLength > matchLength) {
    				ord = ndx;
    				matchLength = currentLength;
    			}
    		}
    	}

        return ord;
    }
}
