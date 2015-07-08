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
package org.acm.seguin.uml.refactor;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Iterator;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import org.acm.seguin.refactor.Refactoring;
import org.acm.seguin.refactor.RefactoringFactory;
import org.acm.seguin.refactor.method.RenameParameterRefactoring;
import org.acm.seguin.summary.MethodSummary;
import org.acm.seguin.summary.ParameterSummary;
import org.acm.seguin.uml.UMLPackage;


/**
 *  Dialog box that gets input for renaming the parameter
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
class RenameParameterDialog extends ClassNameDialog
{
    private MethodSummary method;
    private ParameterSummary param;
    private JComboBox parameterSelection;


    /**
     *  Constructor for the RenameParameterDialog object
     *
     *@param  init       Description of Parameter
     *@param  initParam  Description of Parameter
     */
    public RenameParameterDialog(UMLPackage init, ParameterSummary initParam)
    {
        super(init, 0);
        param = initParam;
        method = (MethodSummary) param.getParent();
        if (method == null) {
            System.out.println("No method specified");
        }

        setTitle(getWindowTitle());

        pack();

        setDefaultName(initParam);

        org.acm.seguin.awt.CenterDialog.center(this, init);
    }


    /**
     *  Constructor for the RenameParameterDialog object
     *
     *@param  init        Description of Parameter
     *@param  initMethod  Description of Parameter
     */
    public RenameParameterDialog(UMLPackage init, MethodSummary initMethod)
    {
        super(init, 1);

        param = null;
        method = initMethod;
        if (method == null) {
            System.out.println("No method specified");
        }

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel newNameLabel = new JLabel("Parameter:  ");
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        GridBagLayout gridbag = (GridBagLayout) getContentPane().getLayout();
        gridbag.setConstraints(newNameLabel, gbc);
        getContentPane().add(newNameLabel);

        parameterSelection = new JComboBox();
        Iterator iter = method.getParameters();
        while (iter.hasNext()) {
            parameterSelection.addItem(iter.next());
        }
        parameterSelection.setEditable(false);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gridbag.setConstraints(parameterSelection, gbc);
        getContentPane().add(parameterSelection);

        setTitle(getWindowTitle());

        pack();

        org.acm.seguin.awt.CenterDialog.center(this, init);
    }


    /**
     *  Gets the LabelText attribute of the RenameParameterDialog object
     *
     *@return    The LabelText value
     */
    public String getLabelText()
    {
        return "New parameter name:";
    }


    /**
     *  Gets the WindowTitle attribute of the RenameParameterDialog object
     *
     *@return    The WindowTitle value
     */
    public String getWindowTitle()
    {
        if (param == null) {
            return "Renaming a parameter";
        }
        return "Renaming the parameter " + param.getName() + " in " + method.getName();
    }


    /**
     *  Description of the Method
     *
     *@return    Description of the Returned Value
     */
    protected Refactoring createRefactoring()
    {
        RenameParameterRefactoring rpr = RefactoringFactory.get().renameParameter();
        rpr.setMethodSummary(method);
        if (param == null) {
            Object selection = parameterSelection.getSelectedItem();
            rpr.setParameterSummary((ParameterSummary) selection);
        }
        else {
            rpr.setParameterSummary(param);
        }
        rpr.setNewName(getClassName());
        return rpr;
    }


    /**
     *  Sets the suggested name of this parameter
     *
     *@param  initVariable  The new defaultName value
     */
    private void setDefaultName(ParameterSummary initVariable)
    {
        try {
            HungarianNamer namer = new HungarianNamer();
            setClassName(namer.getDefaultName(initVariable, "a_"));
        }
        catch (Exception exc) {
            exc.printStackTrace();
            setClassName(initVariable.getName());
        }
    }
}
