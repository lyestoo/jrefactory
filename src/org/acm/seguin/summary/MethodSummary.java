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

import org.acm.seguin.pretty.ModifierHolder;
import java.util.LinkedList;
import java.util.Iterator;
import org.acm.seguin.summary.query.GetTypeSummary;

/**
 *  Stores information about a method
 *
 *@author     Chris Seguin
 *@created    June 8, 1999
 */
public class MethodSummary extends Summary {
    //  Instance Variables
    private TypeDeclSummary returnType;
    private String name;
    private LinkedList parameterList = null;
    private LinkedList exceptionList = null;
    private LinkedList dependencyList = null;
    private ModifierHolder modifiers;
    private int statementCount = 0;
    private int blockDepth = 0;
    private int maxBlockDepth = 0;
    private int declarationLine = 0;


    /**
     *  Construct a method from a method declaration node
     *
     *@param  parent  Description of Parameter
     */
    public MethodSummary(Summary parent) {
        //  Initialize the parent class
        super(parent);

        returnType = null;
    }


    /**
     *  Set the name
     *
     *@param  newName  the name of the method
     */
    public void setName(String newName) {
        name = newName.intern();
    }


    /**
     *  Set the return
     *
     *@param  type  the return type. For a constructor, this value is null.
     */
    public void setReturnType(TypeDeclSummary type) {
        returnType = type;
    }


    /**
     *  Sets the DeclarationLine attribute of the MethodSummary object
     *
     *@param  value  The new DeclarationLine value
     */
    public void setDeclarationLine(int value) {
        declarationLine = value;
    }


    /**
     *  Return the name
     *
     *@return    the name of the method
     */
    public String getName() {
        return name;
    }


    /**
     *  Get the return
     *
     *@return    the return type. For a constructor, this value is null.
     */
    public TypeDeclSummary getReturnType() {
        return returnType;
    }


    /**
     *  Get a list of execeptions
     *
     *@return    an iterator full of exceptions
     */
    public Iterator getExceptions() {
        if (exceptionList == null) {
            return null;
        }

        return exceptionList.iterator();
    }


    /**
     *  Get a list of parameters
     *
     *@return    an iterator over the parameters
     */
    public Iterator getParameters() {
        if (parameterList == null) {
            return null;
        }

        return parameterList.iterator();
    }


    /**
     *  Return the number of parameters
     *
     *@return    the count of parameters to this method
     */
    public int getParameterCount() {
        if (parameterList == null) {
            return 0;
        }

        return parameterList.size();
    }


    /**
     *  Return a list of dependencies. This list will contain variable summaries
     *  and type decl summaries.
     *
     *@return    a list of dependencies.
     */
    public Iterator getDependencies() {
        if (dependencyList == null) {
            return null;
        }

        return dependencyList.iterator();
    }


    /**
     *  Returns the modifier holder
     *
     *@return    the holder
     */
    public ModifierHolder getModifiers() {
        return modifiers;
    }


    /**
     *  Get the statement count
     *
     *@return    the number of statements in the method
     */
    public int getStatementCount() {
        return statementCount;
    }


    /**
     *  Gets the NearMiss attribute of the MethodSummary object
     *
     *@param  other  Description of Parameter
     *@return        The NearMiss value
     */
    public boolean isNearMiss(MethodSummary other) {
        return checkSignature(other) && !(checkReturn(other) &&
                checkProtection(other));
    }


    /**
     *  Determines if this is really a method or an initializer
     *
     *@return    true if it is an initializer
     */
    public boolean isInitializer() {
        return name.equals("***Initializer***");
    }


    /**
     *  Returns the maximum levels of blocks in a certain code
     *
     *@return    The MaxBlockDepth value
     */
    public int getMaxBlockDepth() {
        return maxBlockDepth;
    }


    /**
     *  Gets the DeclarationLine attribute of the MethodSummary object
     *
     *@return    The DeclarationLine value
     */
    public int getDeclarationLine() {
        return declarationLine;
    }


    /**
     *  Determine if this is a constructor
     *
     *@return    The Constructor value
     */
    public boolean isConstructor() {
        return (returnType == null) && !isInitializer();
    }


    /**
     *  Convert this to a string
     *
     *@return    a string representation of this object
     */
    public String toString() {
        //  Start with method name
        StringBuffer buffer = new StringBuffer(getName());
        buffer.append("(");

        //  Add parameters
        Iterator iter = getParameters();
        if (iter != null) {
            while (iter.hasNext()) {
                Object next = iter.next();
                buffer.append(next.toString());
                if (iter.hasNext()) {
                    buffer.append(", ");
                }
            }
        }

        //  Add return type
        if (returnType == null) {
            buffer.append(")");
        } else {
            buffer.append(") : ");
            buffer.append(returnType.toString());
        }

        //  Return the result
        return buffer.toString();
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
     *  Increments the statement count
     */
    public void incrStatementCount() {
        statementCount++;
    }


    /**
     *  Description of the Method
     *
     *@param  other  Description of Parameter
     *@return        Description of the Returned Value
     */
    public boolean equals(Object other) {
        if (other instanceof MethodSummary) {
            MethodSummary temp = (MethodSummary) other;

            return checkSignature(temp) && checkReturn(temp) &&
                    checkProtection(temp);
        }
        return false;
    }


    /**
     *  Notes to the method summary that there is a block in the code
     */
    public void beginBlock() {
        blockDepth++;
        if (blockDepth > maxBlockDepth) {
            maxBlockDepth = blockDepth;
        }
    }


    /**
     *  Notes to the method summary that the block in the code is complete
     */
    public void endBlock() {
        blockDepth--;
    }


    /**
     *  Determines if their signatures are the same
     *
     *@param  other  the oether method
     *@return        true if they have the same signatures
     */
    public boolean checkSignature(MethodSummary other) {
        if (!name.equals(other.getName())) {
            return false;
        }

        Iterator params1 = getParameters();
        Iterator params2 = other.getParameters();

        if (params1 == null) {
            return (params2 == null);
        }

        if (params2 == null) {
            return false;
        }

        while (params1.hasNext() && params2.hasNext()) {
            ParameterSummary next1 = (ParameterSummary) params1.next();
            ParameterSummary next2 = (ParameterSummary) params2.next();

            if (!next1.getTypeDecl().isSame(next2.getTypeDecl())) {
                return false;
            }
        }

        return (params1.hasNext() == params2.hasNext());
    }


    /**
     *  Sets the modifier holder
     *
     *@param  mod  the holder
     */
    protected void setModifiers(ModifierHolder mod) {
        modifiers = mod;
    }


    /**
     *  Add the formal parameters
     *
     *@param  except  Description of Parameter
     */
    protected void add(TypeDeclSummary except) {
        if (except != null) {
            if (exceptionList == null) {
                initExceptionList();
            }

            exceptionList.add(except);
        }
    }


    /**
     *  Add the formal parameters
     *
     *@param  param  the next formal parameter
     */
    protected void add(ParameterSummary param) {
        if (param != null) {
            if (parameterList == null) {
                initParameterList();
            }

            parameterList.add(param);
        }
    }


    /**
     *  Adds a variable dependency
     *
     *@param  dependsOn  a variable that this method is depending on - local
     *      variable or something in an anonymous class.
     */
    protected void addDependency(Summary dependsOn) {
        if (dependsOn != null) {
            if (dependencyList == null) {
                initDependencyList();
            }

            if (!dependencyList.contains(dependsOn)) {
                dependencyList.add(dependsOn);
            }
        }
    }


    /**
     *  Initialize the parameter list
     */
    private void initParameterList() {
        parameterList = new LinkedList();
    }


    /**
     *  Initialize the exception list
     */
    private void initExceptionList() {
        exceptionList = new LinkedList();
    }


    /**
     *  Initialize the dependency list
     */
    private void initDependencyList() {
        dependencyList = new LinkedList();
    }


    /**
     *  Determines if they have the same return value
     *
     *@param  other  Description of Parameter
     *@return        Description of the Returned Value
     */
    private boolean checkReturn(MethodSummary other) {
        return getReturnType().isSame(other.getReturnType());
    }


    /**
     *  Determines if they have the same protections
     *
     *@param  other  Description of Parameter
     *@return        Description of the Returned Value
     */
    private boolean checkProtection(MethodSummary other) {
        ModifierHolder mh1 = getModifiers();
        ModifierHolder mh2 = other.getModifiers();

        return (mh1.isPublic() == mh2.isPublic()) &&
                (mh1.isProtected() == mh2.isProtected()) &&
                (mh1.isPackage() == mh2.isPackage()) &&
                (mh1.isPrivate() == mh2.isPrivate());
    }
}
