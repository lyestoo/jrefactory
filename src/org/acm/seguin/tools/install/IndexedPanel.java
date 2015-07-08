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
import javax.swing.JLabel;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *  Allows the user to select true or false
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public abstract class IndexedPanel extends SettingPanel {
    private JSlider slider;
    private JLabel valueLabel;


    /**
     *  Constructor for the IndexedPanel object
     */
    public IndexedPanel() {
        super();
    }


    /**
     *  Sets the Minimum attribute of the IndexedPanel object
     *
     *@param  value  The new Minimum value
     */
    public void setMinimum(int value) {
        slider.setMinimum(value);
    }


    /**
     *  Sets the Maximum attribute of the IndexedPanel object
     *
     *@param  value  The new Maximum value
     */
    public void setMaximum(int value) {
        slider.setMaximum(value);
    }


    /**
     *  Gets the Value attribute of the TogglePanel object
     *
     *@return    The Value value
     */
    public String getValue() {
        return "" + slider.getValue();
    }


    /**
     *  Adds a feature to the Control attribute of the TogglePanel object
     */
    public void addControl() {
        addControl(-1, 80);
    }


    /**
     *  Adds a feature to the Control attribute of the TogglePanel object
     *
     *@param  low   The feature to be added to the Control attribute
     *@param  high  The feature to be added to the Control attribute
     */
    public void addControl(int low, int high) {
        incrItems();

	int value;
	try {
	    value = Integer.parseInt(getDefaultValue());
	}
	catch (NumberFormatException nfe) {
	    value = Integer.parseInt(getInitialValue());
	}

        if (value < low) {
            low = value;
        }
        if (value > high) {
            high = value;
        }
        slider = new JSlider(low, high, value);
        add(slider);
        valueLabel = new JLabel("Value:  " + value);
        incrItems();
        add(valueLabel);
        slider.addChangeListener(
            new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    updateState();
                }
            });
    }


    /**
     *  Description of the Method
     */
    protected void updateState() {
        valueLabel.setText("Value:  " + slider.getValue());
    }


    /**
     *  Reloads the value from the file
     */
    public void reload() {
        int value = Integer.parseInt(getDefaultValue());
        slider.setValue(value);
        updateState();
    }
}

//  This is the end of the file

