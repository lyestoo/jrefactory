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
import org.acm.seguin.parser.ast.ASTImportDeclaration;
import org.acm.seguin.parser.ast.ASTName;

/**
 *  Stores the summary of an import
 *
 *@author     Chris Seguin
 *@created    June 6, 1999
 */
public class ImportSummary extends Summary {
    //  Instance Variables
    private PackageSummary packageSummary;
    private String type;


    /**
     *  Create an import summary
     *
     *@param  parentSummary  the parent summary
     *@param  importDecl     the import declaration
     */
    public ImportSummary(Summary parentSummary, ASTImportDeclaration importDecl) {
        //  Load parent class
        super(parentSummary);

        //  Local Variables
        ASTName name = (ASTName) importDecl.jjtGetFirstChild();

        if (importDecl.isImportingPackage()) {
            type = null;
            packageSummary = PackageSummary.getPackageSummary(name.getName());
        } else {
            int last = name.getNameSize() - 1;
            type = name.getNamePart(last).intern();
            String packageName = getPackageName(last, name);
            packageSummary = PackageSummary.getPackageSummary(packageName);
        }
    }


    /**
     *  Get the package
     *
     *@return    the package summary
     */
    public PackageSummary getPackage() {
        return packageSummary;
    }


    /**
     *  Get the type
     *
     *@return    the name of the type or null if this represents the entire
     *      package
     */
    public String getType() {
        return type;
    }


    /**
     *  Provide method to visit a node
     *
     *@param  visitor  the visitor
     *@param  data     the data for the visit
     *@return          some new data
     */
    public Object accept(SummaryVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }


    /**
     *  Extract the name of the package
     *
     *@param  last  the index of the last
     *@param  name  the name
     *@return       the package name
     */
    private String getPackageName(int last, ASTName name) {
        if (last > 0) {
            StringBuffer buffer = new StringBuffer(name.getNamePart(0));
            for (int ndx = 1; ndx < last; ndx++) {
                buffer.append(".");
                buffer.append(name.getNamePart(ndx));
            }
            return buffer.toString();
        }
        return "";
    }


    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public String toString() {
        if (type == null) {
            return "ImportSummary<" + packageSummary.getName() + ".*>";
        }
        return "ImportSummary<" + packageSummary.getName() + "." + type + ">";
    }


    /**
     *  Gets the name attribute of the ImportSummary object
     *
     *@return    The name value
     */
    public String getName() {
        return type;
    }
}
