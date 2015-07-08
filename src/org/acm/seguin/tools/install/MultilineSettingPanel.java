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
package org.acm.seguin.tools.install;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import org.acm.seguin.util.MissingSettingsException;


/**
 *  Stores the header and footer
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public abstract class MultilineSettingPanel extends SettingPanel
{
    private JTextArea textArea;


    /**
     *  Constructor for the MultilineSettingPanel object
     */
    public MultilineSettingPanel()
    {
        super();
        setLayout(new GridBagLayout());
    }


    /**
     *  Gets the DefaultValue attribute of the SettingPanel object
     *
     *@return    The DefaultValue value
     */
    public String getDefaultValue()
    {
        int count = 1;
        boolean possible = true;
        StringBuffer result = new StringBuffer();
        while (possible) {
            try {
                result.append(SettingPanel.bundle.getString(getKey() + "." + count));
                result.append("\n");
                count++;
            }
            catch (MissingSettingsException mse) {
                possible = false;
            }
        }
        return result.toString();
    }


    /**
     *  Gets the Value attribute of the SettingPanel object
     *
     *@return    The Value value
     */
    public String getValue()
    {
        return textArea.getText();
    }


    /**
     *  Adds a feature to the Control attribute of the MultilineSettingPanel
     *  object
     */
    public void addControl()
    {
        textArea = new JTextArea();
        textArea.setText(getDefaultValue());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.anchor = constraints.WEST;
        constraints.fill = constraints.BOTH;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.ipadx = 0;
        constraints.ipady = 0;
        textArea.setBorder(new EtchedBorder());
        add(textArea, constraints);
    }


    /**
     *  Generate the settings file for this particular setting
     *
     *@param  output  the output stream
     */
    public void generateSetting(PrintWriter output)
    {
        printDescription(output);
        StringTokenizer tok = new StringTokenizer(textArea.getText(), "\r\n");
        int count = 1;
        while (tok.hasMoreTokens()) {
            output.println(getKey() + "." + count + "=" + addEscapes(tok.nextToken()));
            count++;
        }
        output.println("");
    }


    /**
     *  Description of the Method
     */
    public void reload()
    {
        textArea.setText(getDefaultValue());
    }


    /**
     *  Gets the initial value if it is not defined
     *
     *@return    The InitialValue value
     */
    protected String getInitialValue()
    {
        return "";
    }


    /**
     *  Adds the escapes to the lines
     *
     *@param  line  the line to add the escapes to
     *@return       a transformed line
     */
    private String addEscapes(String line)
    {
        StringBuffer l_sbBuffer = new StringBuffer();
        for (int ndx = 0; ndx < line.length(); ndx++) {
            if (line.charAt(ndx) == '\\') {
                l_sbBuffer.append("\\\\");
            }
            else {
                l_sbBuffer.append(line.charAt(ndx));
            }
        }

        return l_sbBuffer.toString();
    }
}
/*******\
\*******/
