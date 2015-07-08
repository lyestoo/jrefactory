/*
 * JSSpacingOptionPane.java - JavaStyle spacing options panel
 * Copyright (C) 2001 Dirk Moebius
 *
 * jEdit buffer options:
 * :tabSize=4:indentSize=4:noTabs=false:maxLineLen=0:
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.acm.seguin.ide.common.options;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JTextField;


/**
 *@author     Mike Atkinson (<a href="mailto:javastyle@ladyshot.demon.co.uk">
 *      Mike@ladyshot.demon.co.uk</a> )
 *@author     Dirk Moebius (<a href="mailto:dmoebius@gmx.net">dmoebius@gmx.net
 *      </a>)
 *@created    03 September 2003
 *@version    $Version: $
 *@since      1.0
 */
public class JSSpacingOptionPane extends JSHelpOptionPane {
    private JCheckBox spaceParens;
    private JCheckBox spaceNotInCasts;
    private JTextField linesAfterPackage;
    private JTextField linesBeforeClass;
    private JTextField linesBetween;

    private SelectedPanel spaceCasts_sp;
    private SelectedPanel spaceKeywords_sp;
    private SelectedPanel spaceParens_sp;
    private SelectedPanel spaceNotInCasts_sp;
    private SelectedPanel spaceLocals_sp;
    private SelectedPanel linesAfterPackage_sp;
    private SelectedPanel linesBeforeClass_sp;
    private SelectedPanel linesBetween_sp;


    /**
     *  Constructor for the JSSpacingOptionPane object
     *
     *@param  project  Description of the Parameter
     */
    public JSSpacingOptionPane(String project) {
        super("javastyle.spacing", "pretty", project);
    }


    /**
     *  Description of the Method
     */
    public void _init() {
        ActionHandler ah = new ActionHandler();

        // "Lines after package statement: nnn"
        String lines_after_package = props.getString("lines.after.package");
        linesAfterPackage = new JTextField();
        linesAfterPackage_sp = addComponent("lines.after.package", "linesAfterPackage", linesAfterPackage);
        int valLinesAfterPackage = 1;
        try {
            valLinesAfterPackage = Integer.parseInt(lines_after_package);
        } catch (NumberFormatException ex) {}
        linesAfterPackage.setText(String.valueOf(valLinesAfterPackage - 1));

        // "Lines before class statement: nnn"
        linesBeforeClass = new JTextField();
        linesBeforeClass_sp = addComponent("lines.before.class", "linesBeforeClass", linesBeforeClass);

        // "Min. lines between methods & classes: nnn"
        linesBetween = new JTextField();
        linesBetween_sp = addComponent("lines.between", "linesBetween", linesBetween);

        // "Additional space after casts"
        spaceCasts_sp = addComponent("cast.space", "spaceCasts", new JCheckBox());

        // "Additional space after keywords (if, while, etc.)"
        spaceKeywords_sp = addComponent("keyword.space", "spaceKeywords", new JCheckBox());

        // "Additional space in parenthesized expressions"
        spaceParens = new JCheckBox();
        spaceParens.addActionListener(ah);
        spaceParens_sp = addComponent("expr.space", "spaceParens", spaceParens);

        // "...but not in casts"
        spaceNotInCasts = new JCheckBox();
        spaceNotInCasts_sp = addComponent("cast.force.nospace", "spaceNotInCasts", spaceNotInCasts);
        spaceNotInCasts.setEnabled(spaceParens.isSelected());

        // "Additional blank lines before and after local variable declarations"
        spaceLocals_sp = addComponent("insert.space.around.local.variables", "spaceLocals", new JCheckBox());

        addHelpArea();
    }


    /**
     *  Called when the options dialog's `OK' button is pressed. This should
     *  save any properties saved in this option pane.
     */
    public void _save() {
        spaceCasts_sp.save();
        spaceKeywords_sp.save();
        spaceParens_sp.save();
        spaceNotInCasts_sp.save();
        spaceLocals_sp.save();
        linesAfterPackage_sp.saveInt(1,1);
        linesBeforeClass_sp.saveInt(0,1);
        linesBetween_sp.saveInt(2,0);

    }


    /**
     *  Description of the Class
     *
     *@author     Mike
     *@created    03 September 2003
     */
    private class ActionHandler implements ActionListener {
        /**
         *  Description of the Method
         *
         *@param  evt  Description of the Parameter
         */
        public void actionPerformed(ActionEvent evt) {
            if (evt.getSource() == spaceParens) {
                spaceNotInCasts.setEnabled(spaceParens.isSelected());
            }
        }
    }

}

