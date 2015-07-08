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
package org.acm.seguin.pretty;


import java.io.*;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Comparator;
import org.acm.seguin.parser.ast.ASTCompilationUnit;
import org.acm.seguin.pretty.sort.MultipleOrdering;
import org.acm.seguin.pretty.sort.SameOrdering;
import org.acm.seguin.pretty.sort.TopLevelOrdering;
//import org.acm.seguin.util.Comparator;
import org.acm.seguin.util.FileSettings;
import org.acm.seguin.util.MissingSettingsException;
import org.acm.seguin.util.Settings;


/**
 *  This object stores all the data necessary to print the the Java file
 *
 *@author     Chris Seguin
 *@created    March 6, 1999
 */
public class PrintData
{
    /**
     *  Description of the Field
     */
    public int finalLine = -1;
    private int last = EMPTY;

    private int INDENT = 4;
    private char indentChar = ' ';

    private int codeBlockStyle = BLOCK_STYLE_C;
    private int methodBlockStyle = BLOCK_STYLE_C;
    private int classBlockStyle = BLOCK_STYLE_C;

    // Set to true after method parameters are formatted, before the
    // opening brace
    private boolean isMethodBrace = false;

    // Method parameter indentation
    private boolean lineUpParams = true;
    private boolean inParams = false;
    private int lastParamIndent = 0;

    private boolean exprSpace = false;
    private int linesBetween = 2;

    private int javadocMinimum = 40;
    private int javadocMaximum = 80;

    private boolean spaceAfterCast = true;
    private boolean spaceAfterKeyword = true;
    private boolean spaceAfterMethod = false;
    private boolean spaceInsideCast = false;

    private int javadocStars = 2;
    private int originalLine = -1;
    private boolean storeJavadocPrinted = false;
    private boolean skipNameSpacing = false;
    private int cStyleFormatCode = CSC_ALIGN_STAR;
    private int cStyleIndent = 2;
    private boolean isClassBrace = false;
    private boolean emptyBlockOnSingleLine = false;
    private boolean castSpace = true;
    private boolean documentNestedClasses = true;

    private boolean elseOnNewLine = true;

    private boolean allowSingleLineJavadoc = false;

    private boolean currentIsSingle = false;

    private boolean variablesAlignWithBlock = false;
    private boolean localVariableSpaceInsert = false;
    private int linesAfterPackage = 1;
    private boolean maintainNewlinesAroundImports = true;
    private int linesBeforeClass = 0;
    private boolean indentInitializer = false;
    private boolean bangSpace = false;
    private boolean spaceAroundOps = true;

    private FileSettings bundle;

    private int caseIndent;
    private Vector classNameStack;
    private int dynamicFieldSpace;
    private int fieldNameIndent;
    private int fieldSpaceCode;
    private Vector fieldStack;
    private boolean forceBlock;
    private String[] importSortImportant;
    private int importSortNeighbourhood;
    //  Instance Variables
    private int indent;
    private boolean keepAllJavadoc;
    private boolean keepErroneousJavadocTags;
    private LineQueue lineQueue;

    private MultipleOrdering morder;
    private int newlineCount;
    private StringBuffer outputBuffer;
    private boolean reformatComments;
    private boolean removeExcessBlocks;
    private boolean sortTop;
    private int surpriseType;
    private int tempEqualsLength;

    private boolean lineBeforeExtends = false;
    private int extendsIndentation = 1;
    private boolean lineBeforeImplements = lineBeforeExtends;
    private int implementsIndentation = extendsIndentation;
    private boolean lineBeforeClassBody = false;
    private boolean lineBeforeMultistatementMethodBody = false;
    private boolean lineupJavadocIDs = true;
    private boolean lineupJavadocDescr = false;

    private int javadocIndent = -1;    // Means it should be loaded from "javadoc.indent"
    private boolean c_ownline = true; // Means that c style comments at the end of a 
                                       // program line should be placed on their own line.
    
    /**
     *  Use the C style blocks
     */
    public final static int BLOCK_STYLE_C = 0;
    /**
     *  Use the PASCAL style blocks
     */
    public final static int BLOCK_STYLE_PASCAL = 1;
    /**
     *  Use the EMACS style of blocks (like pascal but 2 spaces)
     */
    public final static int BLOCK_STYLE_EMACS = 2;

    //  Class Variables
    /**
     *  Description of the Field
     */
    public static int EMPTY = 0;
    /**
     *  Description of the Field
     */
    public static int METHOD = 1;
    /**
     *  Description of the Field
     */
    public static int FIELD = 2;
    /**
     *  Description of the Field
     */
    public static int INTERFACE = 3;
    /**
     *  Description of the Field
     */
    public static int CLASS = 3;
    /**
     *  This is used to mark enumerations (which are really classes behind the scenes)
     *@since       JRefactory 2.7.00
     */
    public static int ENUM = 3;

    /**
     *  The indent type for an unexpected end of line - single indent
     */
    public static int SINGLE_INDENT = 1;
    /**
     *  The indent type for an unexpected end of line - double indent
     */
    public static int DOUBLE_INDENT = 2;
    /**
     *  The indent type for an unexpected end of line - line up parameters
     *  indent
     */
    public static int PARAM_INDENT = 3;
    /**
     *  There should be no indent
     */
    public static int NO_INDENT = 4;
    /**
     *  A comment with javadoc
     */
    public final static int JAVADOC_COMMENT = 1;
    /**
     *  A c style comment
     */
    public final static int C_STYLE_COMMENT = 2;
    /**
     *  A category comment
     */
    public final static int CATEGORY_COMMENT = 3;
    /**
     *  A single line comment
     */
    public final static int SINGLE_LINE_COMMENT = 4;

    /**
     *  Never use dynamic field spacing
     */
    public final static int DFS_NEVER = 0;

    /**
     *  ALWAYS use dynamic field spacing
     */
    public final static int DFS_ALWAYS = 1;

    /**
     *  Use dynamic field spacing except with javadoc
     */
    public final static int DFS_NOT_WITH_JAVADOC = 2;

    /**
     *  Only align on the equals
     */
    public final static int DFS_ALIGN_EQUALS = 3;
    /**
     *  Leaves C Style comments untouched
     */
    public final static int CSC_LEAVE_UNTOUCHED = 1;
    /**
     *  Aligns the C style comments with a * to the right
     */
    public final static int CSC_ALIGN_STAR = 0;
    /**
     *  Aligns the C style comments with a * to the right
     */
    public final static int CSC_ALIGN_BLANK = 2;
    /**
     *  Maintains spacing in the C style comments, but insists upon a star at
     *  the right
     */
    public final static int CSC_MAINTAIN_STAR = 3;


    /**
     *  Create a print data object
     */
    public PrintData()
    {
        this(System.out);
    }


    /**
     *  Create a print data object
     *
     *@param  out  the output stream
     */
    public PrintData(OutputStream out)
    {
        indent = 0;
        lineQueue = lineQueueFactory(new PrintWriter(new OutputStreamWriter(out)));
        outputBuffer = new StringBuffer();
        newlineCount = 0;

        //  Load the properties
        bundle = FileSettings.getRefactoryPrettySettings();
        try {
            INDENT = bundle.getInteger("indent");
            String indentCharacter = bundle.getString("indent.char");
            if (indentCharacter.equalsIgnoreCase("space")) {
                indentChar = ' ';
            }
            else if (indentCharacter.equalsIgnoreCase("tab")) {
                indentChar = '\t';
            }
            else {
                indentChar = indentCharacter.charAt(0);
            }
        }
        catch (MissingSettingsException mre) {
            //  Default is sufficient
        }

        codeBlockStyle = translateBlockStyle("block.style");
        methodBlockStyle = translateBlockStyle("method.block.style");
        classBlockStyle = translateBlockStyle("class.block.style");

        try {
            exprSpace = (new Boolean(bundle.getString("expr.space"))).booleanValue();
        }
        catch (MissingSettingsException mre) {
            //  Default is sufficient
        }

        try {
            lineUpParams = (new Boolean(bundle.getString("params.lineup"))).booleanValue();
        }
        catch (MissingSettingsException mre) {
            //  Default is sufficient
        }

        try {
            linesBetween = Integer.parseInt(bundle.getString("lines.between"));
        }
        catch (MissingSettingsException mre) {
            //  Default is sufficient
        }
        catch (NumberFormatException nfe) {
            //  Default is sufficient
        }

        try {
            javadocMinimum = bundle.getInteger("javadoc.wordwrap.min");
            javadocMaximum = bundle.getInteger("javadoc.wordwrap.max");
        }
        catch (MissingSettingsException snfe) {
            //  Default is sufficient
        }

        try {
            spaceAfterCast = bundle.getBoolean("cast.space");
        }
        catch (MissingSettingsException snfe) {
            //  Default is sufficient
        }

        try {
            javadocStars = bundle.getInteger("javadoc.star");
        }
        catch (MissingSettingsException snfe) {
            //  Default is sufficient
        }

        try {
            spaceAfterKeyword = bundle.getBoolean("keyword.space");
        }
        catch (MissingSettingsException snfe) {
            // Default is sufficient
        }

        try {
            String value = bundle.getString("variable.spacing");
            if (value.equalsIgnoreCase("dynamic")) {
                fieldSpaceCode = DFS_ALWAYS;
            }
            else if (value.equalsIgnoreCase("javadoc.dynamic")) {
                fieldSpaceCode = DFS_NOT_WITH_JAVADOC;
            }
            else if (value.equalsIgnoreCase("align.equals")) {
                fieldSpaceCode = DFS_ALIGN_EQUALS;
            }
            else {
                fieldSpaceCode = DFS_NEVER;
            }
        }
        catch (MissingSettingsException snfe) {
            fieldSpaceCode = DFS_NEVER;
        }

        morder = new MultipleOrdering(bundle);
        classNameStack = new Vector();
        fieldStack = new Vector();

        String surpriseReturnString;
        try {
            surpriseReturnString = bundle.getString("surprise.return");
        }
        catch (MissingSettingsException mse) {
            System.out.println("Cannot find surprise.return");
            surpriseReturnString = "double";
        }

        if (surpriseReturnString.equalsIgnoreCase("single")) {
            surpriseType = SINGLE_INDENT;
        }
        else if (surpriseReturnString.equalsIgnoreCase("param")) {
            surpriseType = PARAM_INDENT;
        }
        else if (surpriseReturnString.equalsIgnoreCase("none")) {
            surpriseType = NO_INDENT;
        }
        else {
            surpriseType = DOUBLE_INDENT;
        }

        try {
            reformatComments = bundle.getBoolean("reformat.comments");
        }
        catch (MissingSettingsException mse) {
            reformatComments = true;
        }

        try {
            fieldNameIndent = bundle.getInteger("field.name.indent");
        }
        catch (MissingSettingsException mse) {
            fieldNameIndent = -1;
        }

        try {
            keepAllJavadoc = bundle.getBoolean("keep.all.javadoc");
        }
        catch (MissingSettingsException mse) {
            keepAllJavadoc = false;
        }

        try {
            forceBlock = bundle.getBoolean("force.block");
        }
        catch (MissingSettingsException mse) {
            forceBlock = true;
        }

        try {
            dynamicFieldSpace = bundle.getInteger("dynamic.variable.spacing");
        }
        catch (MissingSettingsException mse) {
            dynamicFieldSpace = 1;
        }

        try {
            String temp = bundle.getString("c.style.format");
            if (temp.equalsIgnoreCase("leave")) {
                cStyleFormatCode = CSC_LEAVE_UNTOUCHED;
            }
            else if (temp.equalsIgnoreCase("maintain.space.star")) {
                cStyleFormatCode = CSC_MAINTAIN_STAR;
            }
            else if (temp.equalsIgnoreCase("align.blank")) {
                cStyleFormatCode = CSC_ALIGN_BLANK;
            }
            else {
                cStyleFormatCode = CSC_ALIGN_STAR;
            }
        }
        catch (MissingSettingsException mse) {
            cStyleFormatCode = CSC_ALIGN_STAR;
        }

        try {
            cStyleIndent = bundle.getInteger("c.style.indent");
        }
        catch (MissingSettingsException mse) {
            cStyleIndent = 2;
        }

        try {
            emptyBlockOnSingleLine = bundle.getBoolean("empty.block.single.line");
        }
        catch (MissingSettingsException mse) {
            emptyBlockOnSingleLine = false;
        }

        try {
            castSpace = !bundle.getBoolean("cast.force.nospace");
        }
        catch (MissingSettingsException mse) {
            castSpace = true;
        }

        try {
            documentNestedClasses = bundle.getBoolean("document.nested.classes");
        }
        catch (MissingSettingsException mse) {
            documentNestedClasses = true;
        }

        try {
            allowSingleLineJavadoc = bundle.getBoolean("allow.singleline.javadoc");
        }
        catch (MissingSettingsException mse) {
            allowSingleLineJavadoc = false;
        }

        try {
            variablesAlignWithBlock = bundle.getBoolean("variable.align.with.block");
        }
        catch (MissingSettingsException mse) {
            variablesAlignWithBlock = false;
        }

        try {
            elseOnNewLine = bundle.getBoolean("else.start.line");
        }
        catch (MissingSettingsException mse) {
            elseOnNewLine = true;
        }

        try {
            caseIndent = bundle.getInteger("case.indent");
        }
        catch (MissingSettingsException mse) {
            caseIndent = INDENT;
        }

        try {
            sortTop = bundle.getBoolean("sort.top");
        }
        catch (MissingSettingsException mse) {
            sortTop = false;
        }

        try {
            String data = bundle.getString("import.sort.important");
            StringTokenizer st = new StringTokenizer(data, ",");
            Vector list = new Vector();
            while (st.hasMoreTokens()) {
                String value = st.nextToken();
                if (!value.endsWith(".")) {
                    value += ".";
                }
                list.add(value);
            }
            importSortImportant = (String[]) list.toArray(new String[list.size()]);
        }
        catch (MissingSettingsException mse) {
            importSortImportant = new String[]{};
        }

        try {
            importSortNeighbourhood = bundle.getInteger("import.sort.neighbourhood");
        }
        catch (MissingSettingsException mse) {
            importSortNeighbourhood = 0;
        }

        try {
            localVariableSpaceInsert = bundle.getBoolean("insert.space.around.local.variables");
        }
        catch (MissingSettingsException mse) {
            localVariableSpaceInsert = false;
        }

        try {
            removeExcessBlocks = bundle.getBoolean("remove.excess.blocks");
        }
        catch (MissingSettingsException mse) {
            removeExcessBlocks = false;
        }

        try {
            linesAfterPackage = bundle.getInteger("lines.after.package");
        }
        catch (MissingSettingsException mse) {
            linesAfterPackage = 0;
        }

        try {
            maintainNewlinesAroundImports = bundle.getBoolean("maintain.newlines.around.imports");
        }
        catch (MissingSettingsException mse) {
            maintainNewlinesAroundImports = true;
        }

        try {
            linesBeforeClass = bundle.getInteger("lines.before.class");
        }
        catch (MissingSettingsException mse) {
            linesBeforeClass = 0;
        }

        try {
            lineBeforeExtends = bundle.getBoolean("line.before.extends");
        }
        catch (MissingSettingsException mse) {
            lineBeforeExtends = false;
        }

        try {
            extendsIndentation = bundle.getInteger("extends.indent");
        }
        catch (MissingSettingsException mse) {
            extendsIndentation = 1;
        }

        try {
            lineBeforeImplements = bundle.getBoolean("line.before.implements");
        }
        catch (MissingSettingsException mse) {
            // implements line break defaults to extends line break
            lineBeforeImplements = lineBeforeExtends;
        }

        try {
            lineBeforeMultistatementMethodBody = bundle.getBoolean("line.before.multistatement.method.body");
        }
        catch (MissingSettingsException mse) {
            lineBeforeMultistatementMethodBody = false;
        }

        try {
            implementsIndentation = bundle.getInteger("implements.indent");
        }
        catch (MissingSettingsException mse) {
            // implements indentation defaults to extends indentation
            implementsIndentation = extendsIndentation;
        }

        try {
            indentInitializer = bundle.getBoolean("indent.in.initializer");
        }
        catch (MissingSettingsException mse) {
            indentInitializer = false;
        }

        try {
            arrayInitializerIndented = bundle.getBoolean("indent.braces.initializer");
        }
        catch (MissingSettingsException mse) {
            arrayInitializerIndented = true;
        }

        try {
            lineBeforeClassBody = bundle.getBoolean("line.before.class.body");
        }
        catch (MissingSettingsException mse) {
            lineBeforeClassBody = false;
        }

        try {
            bangSpace = bundle.getBoolean("bang.space");
        }
        catch (MissingSettingsException mse) {
            bangSpace = false;
        }

        try {
            keepErroneousJavadocTags = bundle.getBoolean("keep.erroneous.tags");
        }
        catch (MissingSettingsException mse) {
            keepErroneousJavadocTags = false;
        }

        try {
            spaceAfterMethod = bundle.getBoolean("method.space");
        }
        catch (MissingSettingsException mse) {
            // Default is sufficient
        }

        try {
            spaceInsideCast = bundle.getBoolean("cast.inside.space");
        }
        catch (MissingSettingsException mse) {
            // Default is sufficient
        }

        try {
            spaceAroundOps = bundle.getBoolean("space.around.ops");
        }
        catch (MissingSettingsException mse) {
            // Default is sufficient
        }

        try {
            String temp = bundle.getString("modifier.order");
            temp = temp.toLowerCase();
            if (temp.startsWith("alpha"))
            	modifierOrder = ALPHABETICAL_ORDER;
            else
            	modifierOrder = STANDARD_ORDER;
        }
        catch (MissingSettingsException mse) {
            // Default is sufficient
        }

        try {
            alignStarsWithSlash = bundle.getBoolean("align.stars.with.slash");
        }
        catch (MissingSettingsException mse) {
            alignStarsWithSlash = false;
        }

        try {
            lineupJavadocIDs = bundle.getBoolean("javadoc.id.lineup");
        }
        catch (MissingSettingsException mse) {
            lineupJavadocIDs = true;
        }
        
        try {
            lineupJavadocDescr = bundle.getBoolean("javadoc.descr.lineup");
        }
        catch (MissingSettingsException mse) {
            lineupJavadocDescr = false;
        }
        
        try {
            taggedJavadocDescription = bundle.getInteger("javadoc.tag.indent");
        }
        catch (MissingSettingsException mse) {
            taggedJavadocDescription = 6;
        }

        try {
            c_ownline = bundle.getBoolean("cstyle.comment.ownline");
            //System.out.println("bundle.getBoolean(\"cstyle.comment.ownline\") => c_ownline="+c_ownline);
        }
        catch (MissingSettingsException mse) {
            c_ownline = true;
            //System.out.println(" exception  => c_ownline="+c_ownline);
        }
        
        fieldStack = new Vector();
    }


    /**
     *  Sets the AbsoluteCommentSpacing attribute of the PrintData object
     *
     *@param  value  The new AbsoluteCommentSpacing value
     */
    public void setAbsoluteCommentSpacing(int value)
    {
        lineQueue.setAbsoluteCommentSpacing(value);
    }


    /**
     *  Sets the bangSpace attribute of the PrintData object
     *
     *@param  value  The new bangSpace value
     */
    public void setBangSpace(boolean value)
    {
        bangSpace = value;
    }


    /**
     *  Sets the CStyleFormatCode attribute of the PrintData object
     *
     *@param  value  The new CStyleFormatCode value
     */
    public void setCStyleFormatCode(int value)
    {
        cStyleFormatCode = value;
    }


    /**
     *  Sets the CStyleIndent attribute of the PrintData object
     *
     *@param  value  The new CStyleIndent value
     */
    public void setCStyleIndent(int value)
    {
        cStyleIndent = value;
    }


    /**
     *  Sets the caseIndent attribute of the PrintData object
     *
     *@param  value  The new caseIndent value
     */
    public void setCaseIndent(int value)
    {
        caseIndent = value;
    }


    /**
     *  Sets the CastSpace attribute of the PrintData object
     *
     *@param  value  The new CastSpace value
     */
    public void setCastSpace(boolean value)
    {
        castSpace = value;
    }


    /**
     *  Sets the ClassCStyleBlock attribute of the PrintData object
     *
     *@param  value  The new ClassCStyleBlock value
     */
    public void setClassBlockStyle(int value)
    {
        classBlockStyle = value;
    }


    /**
     *  Sets the codeBlockStyle attribute of the PrintData object
     *
     *@param  value  The new codeBlockStyle value
     */
    public void setCodeBlockStyle(int value)
    {
        codeBlockStyle = value;
    }


    /**
     *  Sets the currentIsSingle attribute of the PrintData object
     *
     *@param  value  The new currentIsSingle value
     */
    public void setCurrentIsSingle(boolean value)
    {
        currentIsSingle = value;
    }


    /**
     *  Sets the DocumentNestedClasses attribute of the PrintData object
     *
     *@param  value  The new DocumentNestedClasses value
     */
    public void setDocumentNestedClasses(boolean value)
    {
        documentNestedClasses = value;
    }


    /**
     *  Sets the DynamicFieldSpacing attribute of the PrintData object
     *
     *@param  value  The new DynamicFieldSpacing value
     */
    public void setDynamicFieldSpaces(int value)
    {
        dynamicFieldSpace = value;
    }


    /**
     *  Sets the DynamicFieldSpacing attribute of the PrintData object
     *
     *@param  value  The new DynamicFieldSpacing value
     */
    public void setDynamicFieldSpacing(int value)
    {
        fieldSpaceCode = value;
    }


    /**
     *  Sets the elseOnNewLine attribute of the PrintData object
     *
     *@param  value  The new elseOnNewLine value
     */
    public void setElseOnNewLine(boolean value)
    {
        elseOnNewLine = value;
    }


    /**
     *  Sets the EmptyBlockOnSingleLine attribute of the PrintData object
     *
     *@param  value  The new EmptyBlockOnSingleLine value
     */
    public void setEmptyBlockOnSingleLine(boolean value)
    {
        emptyBlockOnSingleLine = value;
    }


    /**
     *  Sets the ExpressionSpace attribute of the PrintData object
     *
     *@param  value  The new ExpressionSpace value
     */
    public void setExpressionSpace(boolean value)
    {
        exprSpace = value;
    }


    /**
     *  Sets the FinalLine attribute of the PrintData object
     *
     *@param  value  The new FinalLine value
     */
    public void setFinalLine(int value)
    {
        finalLine = value;
    }


    /**
     *  Sets the ForceBlock attribute of the PrintData object
     *
     *@param  value  The new ForceBlock value
     */
    public void setForceBlock(boolean value)
    {
        forceBlock = value;
    }


    /**
     *  Sets the importSortImportant attribute of the PrintData object
     *
     *@param  newImportSortImportant  The new importSortImportant value
     */
    public void setImportSortImportant(String[] newImportSortImportant)
    {
        importSortImportant = newImportSortImportant;
    }


    /**
     *  Sets the importSortNeighbourhood attribute of the PrintData object
     *
     *@param  newImportSortNeighbourhood  The new importSortNeighbourhood value
     */
    public void setImportSortNeighbourhood(int newImportSortNeighbourhood)
    {
        importSortNeighbourhood = newImportSortNeighbourhood;
    }


    /**
     *  Sets the IncrementalCommentSpacing attribute of the PrintData object
     *
     *@param  value  The new IncrementalCommentSpacing value
     */
    public void setIncrementalCommentSpacing(int value)
    {
        lineQueue.setIncrementalCommentSpacing(value);
    }


    /**
     *  Sets the indentInInitializer attribute of the PrintData object
     *
     *@param  value  The new indentInInitializer value
     */
    public void setIndentInInitializer(boolean value)
    {
        indentInitializer = value;
    }


    /**
     *  Sets the insertSpaceLocalVariables attribute of the PrintData object
     *
     *@param  value  The new insertSpaceLocalVariables value
     */
    public void setInsertSpaceLocalVariables(boolean value)
    {
        localVariableSpaceInsert = value;
    }


    /**
     *  Sets the keepErroneousJavadocTags attribute of the PrintData object
     *
     *@param  value  The new keepErroneousJavadocTags value
     */
    public void setKeepErroneousJavadocTags(boolean value)
    {
        keepErroneousJavadocTags = value;
    }


    /**
     *  Sets the linesAfterPackage attribute of the PrintData object
     *
     *@param  value  The new linesAfterPackage value
     */
    public void setLinesAfterPackage(int value)
    {
        linesAfterPackage = value;
    }


    /**
     *  Sets the linesBeforeClass attribute of the PrintData object
     *
     *@param  value  The new linesBeforeClass value
     */
    public void setLinesBeforeClass(int value)
    {
        linesBeforeClass = value;
    }


    /**
     *  Sets the maintainNewlinesAroundImports attribute of the PrintData object
     *
     *@param  value  The new maintainNewlinesAroundImports value
     */
    public void setMaintainNewlinesAroundImports(boolean value)
    {
        maintainNewlinesAroundImports = value;
    }


    /**
     *  Sets the methodBlockStyle attribute of the PrintData object
     *
     *@param  value  The new methodBlockStyle value
     */
    public void setMethodBlockStyle(int value)
    {
        methodBlockStyle = value;
    }


    /**
     *  Sets the MultipleOrdering attribute of the PrintData object
     *
     *@param  value  The new MultipleOrdering value
     */
    public void setMultipleOrdering(MultipleOrdering value)
    {
        if (value != null) {
            morder = value;
        }
    }


    /**
     *  Sets the OriginalLine attribute of the PrintData object
     *
     *@param  value  The new OriginalLine value
     */
    public void setOriginalLine(int value)
    {
        originalLine = value;
    }


    /**
     *  Set the output writer
     *
     *@param  newOutput  the new output writer
     */
    public void setOutput(PrintWriter newOutput)
    {
        if (newOutput != null) {
            lineQueue = lineQueueFactory(newOutput);
        }
    }


    /**
     *  Sets the Ownline attribute of the LineQueue object
     *
     *@param  value  The new Ownline value
     */
    public void setOwnline(boolean value)
    {
        lineQueue.setOwnline(value);
    }


    /**
     *  Sets the OwnlineCode attribute of the PrintData object
     *
     *@param  value  The new OwnlineCode value
     */
    public void setOwnlineCode(boolean value)
    {
        lineQueue.setOwnlineCode(value);
    }


    /**
     *  Records the position of the method's opening parenthesis for use in
     *  indenting parameters on subsequent lines.
     */
    public void setParamIndent()
    {
        if (inParams && lastParamIndent == 0) {
            lastParamIndent = getLineLength();
        }
    }


    /**
     *  Sets the ReformatComments attribute of the PrintData object
     *
     *@param  value  The new ReformatComments value
     */
    public void setReformatComments(boolean value)
    {
        reformatComments = value;
    }


    /**
     *  Sets the removeExcessBlocks attribute of the PrintData object
     *
     *@param  value  The new removeExcessBlocks value
     */
    public void setRemoveExcessBlocks(boolean value)
    {
        removeExcessBlocks = value;
    }


    /**
     *  Sets the SharedIncr attribute of the PrintData object
     *
     *@param  value  The new SharedIncr value
     */
    public void setSharedIncr(boolean value)
    {
        lineQueue.setSharedIncremental(value);
    }


    /**
     *  Sets the singleLineJavadoc attribute of the PrintData object
     *
     *@param  value  The new singleLineJavadoc value
     */
    public void setSingleLineJavadoc(boolean value)
    {
        allowSingleLineJavadoc = value;
    }


    /**
     *  Sets the sortTop attribute of the PrintData object
     *
     *@param  value  The new sortTop value
     */
    public void setSortTop(boolean value)
    {
        sortTop = value;
    }


    /**
     *  Sets the spaceAfterMethod attribute of the PrintData object
     *
     *@param  way  The new spaceAfterMethod value
     */
    public void setSpaceAfterMethod(boolean way)
    {
        spaceAfterMethod = way;
    }


    /**
     *  Sets the spaceAfterKeyword attribute of the PrintData object
     *
     *@param  way  The new spaceAfterKeyword value
     *@since       JRefactory 2.7.00
     */
    public void setSpaceAfterKeyword(boolean way)
    {
        spaceAfterKeyword = way;
    }


    /**
     *  Sets the spaceAroundOperators attribute of the PrintData object
     *
     *@param  way  The new spaceAroundOperators value
     */
    public void setSpaceAroundOperators(boolean way)
    {
        spaceAroundOps = way;
    }


    /**
     *  Sets the spaceInsideCast attribute of the PrintData object
     *
     *@param  way  The new spaceInsideCast value
     */
    public void setSpaceInsideCast(boolean way)
    {
        spaceInsideCast = way;
    }


    /**
     *  Set the state
     *
     *@param  newState  Description of Parameter
     */
    public void setState(int newState)
    {
        last = newState;
    }


    /**
     *  Sets the variablesAlignWithBlock attribute of the PrintData object
     *
     *@param  value  The new variablesAlignWithBlock value
     */
    public void setVariablesAlignWithBlock(boolean value)
    {
        variablesAlignWithBlock = value;
    }


    /**
     *  Gets the CStyleFormatCode attribute of the PrintData object
     *
     *@return    The CStyleFormatCode value
     */
    public int getCStyleFormatCode()
    {
        return cStyleFormatCode;
    }


    /**
     *  Gets the CStyleIndent attribute of the PrintData object
     *
     *@return    The CStyleIndent value
     */
    public int getCStyleIndent()
    {
        return cStyleIndent;
    }


    /**
     *  Gets the CStyleOnline attribute of the PrintData object
     *
     *@return    The CStyleOnline value
     */
    public boolean getCStyleOwnline()
    {
        //System.out.println("getCStyleOwnline() c_ownline="+c_ownline);
        return c_ownline;
    }
    

    /**
     *  Sets the CStyleOnline attribute of the PrintData object
     *
     *@param   ownline  The new CStyleOnline value
     */
    public void setCStyleOwnline(boolean ownline) {
        c_ownline = ownline;
            //System.out.println("setCStyleOwnline() => c_ownline="+c_ownline);
    }
    
    /**
     *  Gets the CurrentClassName attribute of the PrintData object
     *
     *@return    The CurrentClassName value
     */
    public String getCurrentClassName()
    {
        return (String) classNameStack.elementAt(classNameStack.size() - 1);
    }


    /**
     *  Gets the DynamicFieldSpacing attribute of the PrintData object
     *
     *@return    The DynamicFieldSpacing value
     */
    public int getDynamicFieldSpaces()
    {
        return dynamicFieldSpace;
    }


    /**
     *  Gets the FieldNameIndent attribute of the PrintData object
     *
     *@return    The FieldNameIndent value
     */
    public int getFieldNameIndent()
    {
        return fieldNameIndent;
    }


    /**
     *  Return the code for field and local variable spacing
     *
     *@return    the code
     */
    public int getFieldSpaceCode()
    {
        return fieldSpaceCode;
    }


    /**
     *  Gets the FinalLine attribute of the PrintData object
     *
     *@return    The FinalLine value
     */
    public int getFinalLine()
    {
        return finalLine;
    }


    /**
     *  Gets the importSortImportant attribute of the PrintData object
     *
     *@return    The importSortImportant value
     */
    public String[] getImportSortImportant()
    {
        return importSortImportant;
    }


    /**
     *  Gets the importSortNeighbourhood attribute of the PrintData object
     *
     *@return    The importSortNeighbourhood value
     */
    public int getImportSortNeighbourhood()
    {
        return importSortNeighbourhood;
    }


    /**
     *  Return the indent string
     *
     *@return    an appropriate length string
     */
    public String getIndentString()
    {
        StringBuffer buffer = new StringBuffer();
        for (int ndx = 0; ndx < indent; ndx++) {
            buffer.append(indentChar);
        }
        return buffer.toString();
    }


    /**
     *  Returns the number of spaces between the JavaDoc asterisks and the
     *  comment text.
     *
     *@return    the number of spaces between the JavaDoc asterisks and the
     *      comment
     */
    public int getJavadocIndent()
    {
        if (javadocIndent>=0) {
            return javadocIndent;
        }
        try {
            javadocIndent = bundle.getInteger("javadoc.indent");
            return javadocIndent;
        }
        catch (MissingSettingsException mre) {
            javadocIndent = 2;
            return javadocIndent;
        }
    }

    /**
     *  Sets the number of spaces between the JavaDoc asterisks and the
     *  comment text.
     *
     *@param indent    the number of spaces between the JavaDoc asterisks and the
     *      comment
     *@since JRefactory 2.7.02
     */
    public void setJavadocIndent(int indent) {
        javadocIndent = indent;
    }
        

    /**
     *  Gets the JavadocStarCount attribute of the PrintData object
     *
     *@return    The JavadocStarCount value
     */
    public int getJavadocStarCount()
    {
        return javadocStars;
    }


    /**
     *  Gets the JavadocWordWrapMaximum attribute of the PrintData object
     *
     *@return    The JavadocWordWrapMaximum value
     */
    public int getJavadocWordWrapMaximum()
    {
        return javadocMaximum;
    }

    /**
     *  Sets the JavadocWordWrapMaximum attribute of the PrintData object
     *
     *@param  wrap  The JavadocWordWrapMaximum value
     *@since       JRefactory 2.7.00
     */
    public void setJavadocWordWrapMaximum(int wrap)
    {
        javadocMaximum = wrap;
    }


    /**
     *  Gets the JavadocWordWrapMinimum attribute of the PrintData object
     *
     *@return    The JavadocWordWrapMinimum value
     */
    public int getJavadocWordWrapMinimum()
    {
        return javadocMinimum;
    }


    /**
     *  Sets the JavadocWordWrapMinimum attribute of the PrintData object
     *
     *@param  wrap  The JavadocWordWrapMinimum value
     *@since       JRefactory 2.7.00
     */
    public void setJavadocWordWrapMinimum(int wrap)
    {
        javadocMinimum = wrap;
    }


    /**
     *  Get the length of the line
     *
     *@return    the length of the buffer
     */
    public int getLineLength()
    {
        return outputBuffer.length();
    }


    /**
     *  Gets the linesAfterPackage attribute of the PrintData object
     *
     *@return    The linesAfterPackage value
     */
    public int getLinesAfterPackage()
    {
        return linesAfterPackage;
    }


    /**
     *  Gets the linesBeforeClass attribute of the PrintData object
     *
     *@return    The linesBeforeClass value
     */
    public int getLinesBeforeClass()
    {
        return linesBeforeClass;
    }


    /**
     *  Gets the Order attribute of the PrintData object
     *
     *@return    The Order value
     */
    public MultipleOrdering getOrder()
    {
        return morder;
    }


    /**
     *  Gets the OriginalLine attribute of the PrintData object
     *
     *@return    The OriginalLine value
     */
    public int getOriginalLine()
    {
        return originalLine;
    }


    /**
     *  Returns the indentation to use for parameters on new lines. If the
     *  <code>
     *  params.lineup</code> property is true, parameters on new lines are lined
     *  up with the method's open parenthesis.
     *
     *@return    The ParamIndent value
     */
    public int getParamIndent()
    {
        if (inParams) {
            return lastParamIndent;
        }
        return 0;
    }


    /**
     *  Gets the Settings attribute of the PrintData object
     *
     *@return    The Settings value
     */
    public Settings getSettings()
    {
        return bundle;
    }


    /**
     *  Return the state of the pretty printer
     *
     *@return    an integer representing the state
     */
    public int getState()
    {
        return last;
    }


    /**
     *  Gets the SurpriseReturn attribute of the PrintData object
     *
     *@return    The SurpriseReturn value
     */
    public int getSurpriseReturn()
    {
        return surpriseType;
    }


    /**
     *  Gets the TopOrder attribute of the PrintData object
     *
     *@param  node  Description of the Parameter
     *@return       The TopOrder value
     */
    public Comparator getTopOrder(ASTCompilationUnit node)
    {
        try {
            if (sortTop) {
                return new TopLevelOrdering(node, this);
            }
        }
        catch (MissingSettingsException mre) {
        }

        return new SameOrdering();
    }


    /**
     *  Gets the AllJavadocKept attribute of the PrintData object
     *
     *@return    The AllJavadocKept value
     */
    public boolean isAllJavadocKept()
    {
        return keepAllJavadoc;
    }


    /**
     *  Gets the allowSingleLineJavadoc attribute of the PrintData object
     *
     *@return    The allowSingleLineJavadoc value
     */
    public boolean isAllowSingleLineJavadoc()
    {
        return allowSingleLineJavadoc;
    }


    /**
     *  Gets the bangSpace attribute of the PrintData object
     *
     *@return    The bangSpace value
     */
    public boolean isBangSpace()
    {
        return bangSpace;
    }


    /**
     *  Is the output buffer empty?
     *
     *@return    true if the output buffer is empty
     */
    public boolean isBufferEmpty()
    {
        return (outputBuffer.toString().trim().length() == 0);
    }


    /**
     *  Gets the CastSpace attribute of the PrintData object
     *
     *@return    The CastSpace value
     */
    public boolean isCastSpace()
    {
        return castSpace;
    }


    /**
     *  Returns true if the catch statement is on a new line
     *
     *@return    true if catch should start a line
     */
    public boolean isCatchOnNewLine()
    {
        try {
            return bundle.getBoolean("catch.start.line");
        }
        catch (MissingSettingsException mse) {
            return true;
        }
    }


    /**
     *  Gets the currentSingle attribute of the PrintData object
     *
     *@return    The currentSingle value
     */
    public boolean isCurrentSingle()
    {
        return currentIsSingle;
    }


    /**
     *  Gets the DynamicFieldSpacing attribute of the PrintData object
     *
     *@param  javadocPrinted  Description of Parameter
     *@return                 The DynamicFieldSpacing value
     */
    public boolean isDynamicFieldSpacing(boolean javadocPrinted)
    {
        if (skipNameSpacing) {
            return false;
        }
        return (!javadocPrinted && (fieldSpaceCode == DFS_NOT_WITH_JAVADOC)) ||
                (fieldSpaceCode == DFS_ALWAYS);
    }


    /**
     *  Returns true if the else statement is on a new line
     *
     *@return    true if else should start a line
     */
    public boolean isElseOnNewLine()
    {
        return elseOnNewLine;
    }


    /**
     *  Gets the EmptyBlockOnSingleLine attribute of the PrintData object
     *
     *@return    The EmptyBlockOnSingleLine value
     */
    public boolean isEmptyBlockOnSingleLine()
    {
        return emptyBlockOnSingleLine;
    }


    /**
     *  Gets the FieldNameIndented attribute of the PrintData object
     *
     *@return    The FieldNameIndented value
     */
    public boolean isFieldNameIndented()
    {
        return (fieldNameIndent > 0);
    }


    /**
     *  Gets the ForcingBlock attribute of the PrintData object
     *
     *@return    The ForcingBlock value
     */
    public boolean isForcingBlock()
    {
        return forceBlock;
    }


    /**
     *  Gets the indentInInitailzer attribute of the PrintData object
     *
     *@return    The indentInInitailzer value
     */
    public boolean isIndentInInitailzer()
    {
        return indentInitializer;
    }


    /**
     *  Gets the insertSpaceLocalVariables attribute of the PrintData object
     *
     *@return    The insertSpaceLocalVariables value
     */
    public boolean isInsertSpaceLocalVariables()
    {
        return localVariableSpaceInsert;
    }


    /**
     *  Returns true if JavaDoc IDs (param, returns, etc.) should be lined up.
     *
     *@return    True if lining up comments, false otherwise
     */
    public boolean isJavadocLinedUp()
    {
        return lineupJavadocIDs;
    }


    /**
     *  Gets the keepErroneousJavadocTags attribute of the PrintData object
     *
     *@return    The keepErroneousJavadocTags value
     */
    public boolean isKeepErroneousJavadocTags()
    {
        return keepErroneousJavadocTags;
    }


    /**
     *  Is the output buffer empty?
     *
     *@return    true if the output buffer is empty
     */
    public boolean isLineIndented()
    {
        return (isBufferEmpty()) &&
                ((outputBuffer.toString().length() > 0) || (indent == 0));
    }


    /**
     *  Gets the maintainNewlinesAroundImports attribute of the PrintData object
     *
     *@return    The maintainNewlinesAroundImports value
     */
    public boolean isMaintainNewlinesAroundImports()
    {
        return maintainNewlinesAroundImports;
    }


    /**
     *  Gets the NestedClassDocumented attribute of the PrintData object
     *
     *@return    The NestedClassDocumented value
     */
    public boolean isNestedClassDocumented()
    {
        return documentNestedClasses;
    }


    /**
     *  Gets the ReformatComments attribute of the PrintData object
     *
     *@return    The ReformatComments value
     */
    public boolean isReformatComments()
    {
        return reformatComments;
    }


    /**
     *  Gets the removeExcessBlocks attribute of the PrintData object
     *
     *@return    The removeExcessBlocks value
     */
    public boolean isRemoveExcessBlocks()
    {
        return removeExcessBlocks;
    }


    /**
     *  Gets the SpaceAfterCast attribute of the PrintData object
     *
     *@return    The SpaceAfterCast value
     */
    public boolean isSpaceAfterCast()
    {
        return spaceAfterCast;
    }


    /**
     *  Determines whether there should be a space between a keyword such as
     *  'if' or 'while' and the opening brace that follows it.
     *
     *@return    The SpaceAfterKeyword value
     */
    public boolean isSpaceAfterKeyword()
    {
        return spaceAfterKeyword;
    }


    /**
     *  Determines whether there should be a space between a method call and the
     *  opening brace that follows it.
     *
     *@return    The SpaceAfterMethod value
     */
    public boolean isSpaceAfterMethod()
    {
        return spaceAfterMethod;
    }


    /**
     *  Gets the spaceAroundOperators attribute of the PrintData object
     *
     *@return    The spaceAroundOperators value
     */
    public boolean isSpaceAroundOperators()
    {
        return spaceAroundOps;
    }


    /**
     *  Determines if there should be a space between the '*' and the '@' in a
     *  javadoc comment.
     *
     *@return    true if there should be a space
     */
    public boolean isSpaceBeforeAt()
    {
        try {
            return bundle.getBoolean("space.before.javadoc");
        }
        catch (MissingSettingsException mre) {
            return false;
        }
    }


    /**
     *  Determines if there is a space inside the cast parens
     *
     *@return    The SpaceAfterKeyword value
     */
    public boolean isSpaceInsideCast()
    {
        return spaceInsideCast;
    }


    /**
     *  Gets the ThrowsOnNewline attribute of the PrintData object
     *
     *@return    The ThrowsOnNewline value
     */
    public boolean isThrowsOnNewline()
    {
        try {
            return bundle.getBoolean("throws.newline");
        }
        catch (MissingSettingsException mse) {
            return false;
        }
    }


    /**
     *  Gets the variablesAlignWithBlock attribute of the PrintData object
     *
     *@return    The variablesAlignWithBlock value
     */
    public boolean isVariablesAlignWithBlock()
    {
        return variablesAlignWithBlock;
    }


    /**
     *  Append a comment to the output
     *
     *@param  string  the input string
     *@param  type    Description of Parameter
     */
    public void appendComment(String string, int type)
    {
        if (type == CATEGORY_COMMENT) {
            lineQueue.appendCategoryComment(string, outputBuffer.toString());
            newlineCount++;
        }
        else if (type == SINGLE_LINE_COMMENT) {
            lineQueue.appendSingleLineComment(string, outputBuffer.toString());
            newlineCount--;
        }
        else {
            append(string);
        }
    }


    /**
     *  Append constant to the output
     *
     *@param  string  the input string
     */
    public void appendConstant(String string)
    {
        outputBuffer.append(string);
    }


    /**
     *  Append a keyword to the output
     *
     *@param  string  the input string
     */
    public void appendKeyword(String string)
    {
        append(string);
    }


    /**
     *  Append text to the output
     *
     *@param  string  the input string
     */
    public void appendText(String string)
    {
        append(string);
    }


    /**
     *  Backspace
     */
    public void backspace()
    {
        outputBuffer.setLength(outputBuffer.length() - 1);
    }


    /**
     *  Start a block
     */
    public void beginBlock()
    {
        beginBlock(true, true);
    }


    /**
     *  Start a block
     *
     *@param  space  Description of the Parameter
     */
    public void beginBlock(boolean space)
    {
        beginBlock(space, true);
    }


    /**
     *  Start a block
     *
     *@param  space         Description of Parameter
     *@param  newlineAfter  Description of the Parameter
     */
    public void beginBlock(boolean space, boolean newlineAfter)
    {
        int currentStyle = getCurrentBlockStyle();

        if (currentStyle == BLOCK_STYLE_C) {
            if (space) {
                space();
            }
            append("{");
        }
        else {
            if (currentStyle == BLOCK_STYLE_EMACS) {
                incrIndent();
            }
            indent();
            append("{");
        }

        isMethodBrace = false;
        isClassBrace = false;
        if (newlineAfter) {
            newline();
        }
        incrIndent();
    }


    /**
     *  Start a Class
     */
    public void beginClass()
    {
        if (last != EMPTY) {
            for (int ndx = 0; ndx < linesBetween; ndx++) {
                newline();
            }
        }
        last = EMPTY;
    }


    /**
     *  Start an expression
     *
     *@param  notEmpty  Description of Parameter
     */
    public void beginExpression(boolean notEmpty)
    {
        if (notEmpty == false || exprSpace == false) {
            append("(");
        }
        else {
            append("( ");
        }
    }


    /**
     *  Start a Enum
     *@since       JRefactory 2.7.00
     */
    public void beginEnum()
    {
        if ((last == EMPTY) || (last == ENUM)) {
        }
        else {
            for (int ndx = 0; ndx < linesBetween; ndx++) {
                newline();
            }
        }
    }


    /**
     *  Start a Field
     */
    public void beginField()
    {
        if ((last == EMPTY) || (last == FIELD)) {
        }
        else {
            for (int ndx = 0; ndx < linesBetween; ndx++) {
                newline();
            }
        }
    }


    /**
     *  Start a Interface
     */
    public void beginInterface()
    {
        if (last != EMPTY) {
            for (int ndx = 0; ndx < linesBetween; ndx++) {
                newline();
            }
        }
        last = EMPTY;
    }


    /**
     *  Start a Method
     */
    public void beginMethod()
    {
        if (last != EMPTY) {
            for (int ndx = 0; ndx < linesBetween; ndx++) {
                newline();
            }
        }
    }


    /**
     *  Indicates that a class's open brace is about to be formatted.
     */
    public void classBrace()
    {
        // This is reset to false in beginBlock after formatting
        isClassBrace = true;
    }


    /**
     *  Closes the output stream
     */
    public void close()
    {
        flush();
        lineQueue.getOutput().close();
    }


    /**
     *  Consume a newline
     *
     *@return    true when we were expecting this newline
     */
    public boolean consumeNewline()
    {
        lineQueue.flushFirstLine();

        if (!isBufferEmpty()) {
            newlineCount = 0;
        }

        if (newlineCount > 0) {
            newlineCount--;
            return true;
        }
        else {
            newline();
            lineQueue.flush();
            return false;
        }
    }


    /**
     *  Description of the Method
     */
    public void decrCaseIndent()
    {
        incrIndent(-caseIndent);
    }


    /**
     *  Decrement the indent by the default amount
     */
    public void decrIndent()
    {
        incrIndent(-INDENT);
    }


    /**
     *  End a block
     *
     *@param  newline        Description of Parameter
     *@param  newlineBefore  Description of the Parameter
     */
    public void endBlock(boolean newline, boolean newlineBefore)
    {
        decrIndent();
        if (newlineBefore || (outputBuffer.length() == 0)) {
            indent();
        }
        if (getCurrentBlockStyle() == BLOCK_STYLE_EMACS) {
            decrIndent();
        }
        append("}");
        if (newline) {
            newline();
        }

        isMethodBrace = false;
        isClassBrace = false;
    }


    /**
     *  End a block
     */
    public void endBlock()
    {
        endBlock(true, true);
    }


    /**
     *  End a Class
     */
    public void endClass()
    {
        last = CLASS;
    }


    /**
     *  End an expression
     *
     *@param  notEmpty  Description of Parameter
     */
    public void endExpression(boolean notEmpty)
    {
        if (notEmpty == false || exprSpace == false) {
            append(")");
        }
        else {
            append(" )");
        }
    }


    /**
     *  End a Enum
     *@since       JRefactory 2.7.00
     */
    public void endEnum()
    {
        last = ENUM;
    }

    /**
     *  End a Field
     */
    public void endField()
    {
        last = FIELD;
    }


    /**
     *  End a Interface
     */
    public void endInterface()
    {
        last = INTERFACE;
    }


    /**
     *  End a Method
     */
    public void endMethod()
    {
        last = METHOD;
    }


    /**
     *  Sets the state for being inside a method declaration. Used for lining up
     *  parameters with the method's open parenthesis.
     */
    public void enterMethodDecl()
    {
        inParams = true;
        lastParamIndent = 0;
    }


    /**
     *  Indicates that we've exited a method declaration.
     */
    public void exitMethodDecl()
    {
        inParams = false;
        lastParamIndent = 0;
    }


    /**
     *  Flushes the buffer
     */
    public void flush()
    {
        lineQueue.flush();
    }


    /**
     *  Description of the Method
     */
    public void incrCaseIndent()
    {
        incrIndent(caseIndent);
    }


    /**
     *  Increment the indent by the default amount
     */
    public void incrIndent()
    {
        incrIndent(INDENT);
    }


    /**
     *  Indent the output
     */
    public void indent()
    {
        if (!isBufferEmpty()) {
            newline();
        }

        outputBuffer.setLength(0);
        append(getIndentString());
    }


    /**
     *  Indents a method parameter. If the <code>params.lineup</code> property
     *  is true, parameters on new lines are lined up with the method's open
     *  parenthesis.
     */
    public void indentParam()
    {
        if ((surpriseType == PARAM_INDENT) && (lastParamIndent > 0)) {
            indent();
            for (int ndx = 0; ndx < lastParamIndent - indent; ndx++) {
                append(" ");
            }
        }
        else {
            incrIndent();
            indent();
            decrIndent();
        }
    }


    /**
     *  Indicates that a method's open brace is about to be formatted.
     */
    public void methodBrace()
    {
        // This is reset to false in beginBlock after formatting
        isMethodBrace = true;
    }


    /**
     *  Add a newline
     */
    public void newline()
    {
        String save = "";

        if (isBufferEmpty()) {
            outputBuffer.setLength(0);
            lineQueue.println("");
            newlineCount++;
        }
        else {
            save = outputBuffer.toString();
            lineQueue.println(outputBuffer.toString());
            outputBuffer.setLength(0);
            newlineCount = 1;
        }
    }


    /**
     *  Description of the Method
     */
    public void popCurrentClassName()
    {
        classNameStack.removeElementAt(classNameStack.size() - 1);
    }


    /**
     *  Description of the Method
     */
    public void popFieldSize()
    {
        fieldStack.removeElementAt(fieldStack.size() - 1);
    }


    /**
     *  Description of the Method
     *
     *@param  name  Description of Parameter
     */
    public void pushCurrentClassName(String name)
    {
        classNameStack.addElement(name);
    }


    /**
     *  Description of the Method
     *
     *@param  size  Description of Parameter
     */
    public void pushFieldSize(FieldSize size)
    {
        fieldStack.addElement(size);
    }


    /**
     *  Description of the Method
     */
    public void reset()
    {
        outputBuffer.setLength(0);
    }


    /**
     *  Description of the Method
     */
    public void saveCurrentLine()
    {
        setFinalLine(lineQueue.getCurrentLine());
    }


    /**
     *  Add a space
     */
    public void space()
    {
        append(" ");
    }


    /**
     *  Description of the Method
     *
     *@return    Description of the Returned Value
     */
    public FieldSize topFieldSize()
    {
        return (FieldSize) fieldStack.elementAt(fieldStack.size() - 1);
    }


    /**
     *  Increment the indent
     *
     *@param  incr  the amount to increment the indent
     */
    protected void incrIndent(int incr)
    {
        indent += incr;
        if (indent < 0) {
            indent = 0;
        }
    }


    /**
     *  Get the indent
     *
     *@return  the indent
     *@since       JRefactory 2.7.00
     */
    public int getIndent()
    {
        return indent;
    }


    /**
     *  Creates a line queue object
     *
     *@param  output  the output stream
     *@return         the queue
     */
    protected LineQueue lineQueueFactory(PrintWriter output)
    {
        return new LineQueue(output);
    }


    /**
     *  Sets the AllJavadocKept attribute of the PrintData object
     *
     *@param  value  The new AllJavadocKept value
     */
    void setAllJavadocKept(boolean value)
    {
        keepAllJavadoc = value;
    }


    /**
     *  Sets the SkipNameSpacing attribute of the PrintData object
     *
     *@param  value  The new SkipNameSpacing value
     */
    void setSkipNameSpacing(boolean value)
    {
        skipNameSpacing = value;
    }


    /**
     *  Sets the StoreJavadocPrinted attribute of the PrintData object
     *
     *@param  value  The new StoreJavadocPrinted value
     */
    void setStoreJavadocPrinted(boolean value)
    {
        storeJavadocPrinted = value;
    }


    /**
     *  Sets the TempEqualsLength attribute of the PrintData object
     *
     *@param  value  The new TempEqualsLength value
     */
    void setTempEqualsLength(int value)
    {
        tempEqualsLength = value;
    }


    /**
     *  Gets the SkipNameSpacing attribute of the PrintData object
     *
     *@return    The SkipNameSpacing value
     */
    boolean getSkipNameSpacing()
    {
        return skipNameSpacing;
    }


    /**
     *  Gets the TempEqualsLength attribute of the PrintData object
     *
     *@return    The TempEqualsLength value
     */
    int getTempEqualsLength()
    {
        return tempEqualsLength;
    }


    /**
     *  Gets the StoreJavadocPrinted attribute of the PrintData object
     *
     *@return    The StoreJavadocPrinted value
     */
    boolean isStoreJavadocPrinted()
    {
        return storeJavadocPrinted;
    }


    /**
     *  Determine what the current block style is
     *
     *@return    true if we are using the C style now
     */
    private int getCurrentBlockStyle()
    {
        if (isClassBrace) {
            return classBlockStyle;
        }

        if (isMethodBrace) {
            return methodBlockStyle;
        }

        return codeBlockStyle;
    }

    /**
     *  Returns the method block style
     */
    public int getMethodBlockStyle()
    {
    	return methodBlockStyle;
    }


    /**
     *  Append a string to the output
     *
     *@param  string  the input string
     */
    private void append(String string)
    {
        outputBuffer.append(string);
    }


    /**
     *  Translates the key in the Settings into the block style
     *
     *@param  key  Description of the Parameter
     *@return      Description of the Return Value
     */
    private int translateBlockStyle(String key)
    {
        try {
            String code = bundle.getString(key);
            if (code.equalsIgnoreCase("PASCAL")) {
                return BLOCK_STYLE_PASCAL;
            }
            if (code.equalsIgnoreCase("EMACS")) {
                return BLOCK_STYLE_EMACS;
            }
        }
        catch (MissingSettingsException mre) {
            //  Default is sufficient
        }
        return BLOCK_STYLE_C;
    }

	public void setLineBeforeClassBody(boolean value) {
		lineBeforeClassBody = value;
	}
	public boolean isLineBeforeClassBody() { return lineBeforeClassBody; }

	public void setLineBeforeExtends(boolean value) {
		lineBeforeExtends = value;
	}
	public boolean isLineBeforeExtends() { return lineBeforeExtends; }
	public void setExtendsIndentation(int value)
	{
		extendsIndentation = value;
	}
	public int getExtendsIndentation() { return extendsIndentation; }
	public void setLineBeforeImplements(boolean value)
	{
		lineBeforeImplements = value;
	}
	public boolean isLineBeforeImplements() { return lineBeforeImplements; }
	public void setLineBeforeMultistatementMethodBody(boolean value)
	{
		lineBeforeMultistatementMethodBody = value;
	}
	public boolean isLineBeforeMultistatementMethodBody() { return lineBeforeMultistatementMethodBody; }
	public void setImplementsIndentation(int value)
	{
		implementsIndentation = value;
	}
	public int getImplementsIndentation() { return implementsIndentation; }
	private boolean arrayInitializerIndented = true;
	public void setArrayInitializerIndented(boolean way) { arrayInitializerIndented = way; }
	public boolean isArrayInitializerIndented() { return arrayInitializerIndented; }
	public final static int ALPHABETICAL_ORDER = 1;
	public final static int STANDARD_ORDER = 2;
	private int modifierOrder = ALPHABETICAL_ORDER;
	public void setModifierOrder(int value) { modifierOrder = value; }
	public int getModifierOrder() { return modifierOrder; }
	private boolean alignStarsWithSlash = false;
	public void setAlignStarsWithSlash(boolean value) { alignStarsWithSlash = value; }
	public boolean isStarsAlignedWithSlash() { return alignStarsWithSlash; }
	public void setLineupJavadocIDs(boolean way) { lineupJavadocIDs = way; }
	public void setLineupJavadocDescr(boolean way) { lineupJavadocDescr = way; }
	public boolean isJavadocDescriptionLinedup() { return lineupJavadocDescr; }
	private int taggedJavadocDescription = 6;
	public int getTaggedJavadocDescription() { return taggedJavadocDescription; }
	public void setTaggedJavadocDescription(int value) { taggedJavadocDescription = value; }
        
        
        public StringBuffer getBuffer() {
            return outputBuffer;
        }
}

