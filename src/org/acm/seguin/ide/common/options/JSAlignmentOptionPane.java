/*
 *  JSAlignmentOptionPane.java - JavaStyle alignment options panel
 *  Copyright (C) 2001 Dirk Moebius
 *
 *  jEdit buffer options:
 *  :tabSize=4:indentSize=4:noTabs=false:maxLineLen=0:
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.acm.seguin.ide.common.options;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import org.acm.seguin.ide.common.IDEPlugin;
import org.acm.seguin.ide.common.IDEInterface;


/**
 *@author     Mike Atkinson (<a href="mailto:javastyle@ladyshot.demon.co.uk">
 *      Mike@ladyshot.demon.co.uk </a> )
 *@author     Dirk Moebius (<a href="mailto:dmoebius@gmx.net">dmoebius@gmx.net
 *      </a> )
 *@created    03 September 2003
 *@version    $Version: $
 *@since      1.0
 */
public class JSAlignmentOptionPane extends JSHelpOptionPane {
    private JCheckBox alignPrefixed;
    private JTextField dynamicSpacing;
    private JTextField fieldNameIndent;
    private JMouseComboBox lineUpFields;

	private SelectedPanel alignParameters_sp;
    private SelectedPanel lineUpFields_sp;
    private SelectedPanel alignWithBlock_sp;
    private SelectedPanel alignPrefixed_sp;
    private SelectedPanel dynamicSpacing_sp;
    private SelectedPanel fieldNameIndent_sp;
    private SelectedPanel lineUpTags_sp;

    private final static String[] lineUpFieldsOptions = {
            getIdeJavaStyleOption("lineUpFields.not"),
            getIdeJavaStyleOption("lineUpFields.tabular"),
            getIdeJavaStyleOption("lineUpFields.atEquals"),
            getIdeJavaStyleOption("lineUpFields.atFixedColumn")
            };


    /**
     *  Constructor for the JSAlignmentOptionPane object
     *
     *@param  project  Description of the Parameter
     */
    public JSAlignmentOptionPane(String project) {
        super("javastyle.alignment", "pretty", project);
    }


    /**
     *  Description of the Method
     */
    public void _init() {
        ActionHandler ah = new ActionHandler();

        String variable_spacing = props.getString("variable.spacing", "single");


        // "Align parameters if they start on a new line
        alignParameters_sp = addComponent("params.lineup", "params.lineup", new JCheckBox());

        // "Line up field definitions"
        lineUpFields = new JMouseComboBox(lineUpFieldsOptions);
        lineUpFields.addActionListener(ah);
        lineUpFields_sp = addComponent("variable.spacing", "lineUpFields", lineUpFields);

        // "Align with the block opener '{'"
        alignWithBlock_sp = addComponent("variable.align.with.block", "lineUpFields.alignWithBlock", new JCheckBox());

        // "Align even if prefixed by JavaDoc"
        alignPrefixed = new JCheckBox();
        alignPrefixed_sp = addComponent("variable.spacing", "lineUpFields.alignPrefixed", alignPrefixed);
        alignPrefixed.setSelected(variable_spacing.equals("dynamic"));

        // "Spacing between columns: nnn"
        dynamicSpacing = new JTextField();
        dynamicSpacing_sp = addComponent("dynamic.variable.spacing", "1", "lineUpFields.dynamicSpacing", dynamicSpacing);

        // "Indent field names to column: nnn"
        fieldNameIndent = new JTextField();
        fieldNameIndent_sp = addComponent("field.name.indent", "lineUpFields.fieldNameIndent", fieldNameIndent);
        int fni = props.getInteger("field.name.indent", -1);
        fieldNameIndent.setText(Integer.toString(fni));

        // "Line up names and descriptions in JavaDoc tags"
        lineUpTags_sp = addComponent("javadoc.id.lineup", "lineUpTags", new JCheckBox());

        if (fni >= 0) {
            lineUpFields.setSelectedIndex(3);
        } else if (variable_spacing.equals("dynamic") || variable_spacing.equals("javadoc.dynamic")) {
            lineUpFields.setSelectedIndex(1);
        } else if (variable_spacing.equals("align.equals")) {
            lineUpFields.setSelectedIndex(2);
        } else {
            lineUpFields.setSelectedIndex(0);
        }

        addHelpArea();
    }


    /**
     *  Called when the options dialog's `OK' button is pressed. This should
     *  save any properties saved in this option pane.
     */
    public void _save() {
        int sel = lineUpFields.getSelectedIndex();
        int fni = -1;
        int dvs = 1;
        String variable_spacing = "single";

        if (sel == 1) {
            if (alignPrefixed.isSelected()) {
                variable_spacing = "dynamic";
            } else {
                variable_spacing = "javadoc.dynamic";
            }
            try {
                dvs = Integer.parseInt(dynamicSpacing.getText());
            } catch (NumberFormatException nfex) {
                IDEPlugin.log(IDEInterface.ERROR, this, "invalid number for dynamic.variable.spacing: " + dynamicSpacing.getText());
            }
        } else if (sel == 2) {
            variable_spacing = "align.equals";
        } else if (sel == 3) {
            try {
                fni = Integer.parseInt(fieldNameIndent.getText());
            } catch (NumberFormatException nfex) {
                IDEPlugin.log(IDEInterface.ERROR, this, "invalid number for field.name.indent: " + fieldNameIndent.getText());
            }
        }

		alignParameters_sp.save();
        alignWithBlock_sp.save();
        alignPrefixed_sp.save(variable_spacing);
        dynamicSpacing_sp.save(Integer.toString(dvs));
        fieldNameIndent_sp.save(Integer.toString(fni));
        lineUpTags_sp.save();
    }


    /**
     *  Description of the Class
     *
     *@author     Mike Atkinson (Mike)
     *@created    03 September 2003
     *@version    $Version: $
     *@since      1.0
     */
    private class ActionHandler implements ActionListener {
        /**
         *  Description of the Method
         *
         *@param  evt  Description of Parameter
         */
        public void actionPerformed(ActionEvent evt) {
            if (evt.getSource() == lineUpFields) {
                int sel = lineUpFields.getSelectedIndex();

                alignPrefixed.setEnabled(sel == 1);
                dynamicSpacing.setEnabled(sel == 1);
                fieldNameIndent.setEnabled(sel == 3);
            }
        }
    }

}

