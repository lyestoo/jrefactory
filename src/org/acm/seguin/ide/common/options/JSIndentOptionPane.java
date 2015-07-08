/*
 * JSIndentOptionPane.java - JavaStyle indenting options panel
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
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;


/**
 *@author     Mike Atkinson (<a href="mailto:javastyle@ladyshot.demon.co.uk">
 *      Mike@ladyshot.demon.co.uk</a> )
 *@author     Dirk Moebius (<a href="mailto:dmoebius@gmx.net">dmoebius@gmx.net
 *      </a>)
 *@created    03 September 2003
 *@version    $Version: $
 *@since      1.0
 */
public class JSIndentOptionPane extends JSHelpOptionPane {

    private final static String[] bracketModes = {"C", "PASCAL", "EMACS"};

    private JRadioButton forceBlocks;
    private JRadioButton removeBlocks;
    private JTextField caseIndent;

    private SelectedPanel elseStartLine_sp;
    private SelectedPanel catchStartLine_sp;
    private SelectedPanel throwsStartLine_sp;
    private SelectedPanel forceBlocks_sp;
    private SelectedPanel removeBlocks_sp;
    private SelectedPanel emptyBlocks_sp;
    private SelectedPanel indentCont_sp;
    private SelectedPanel cbrackets_sp;
    private SelectedPanel mbrackets_sp;
    private SelectedPanel brackets_sp;
    private SelectedPanel caseIndent_sp;


    /**
     *  Constructor for the JSIndentOptionPane object
     *
     *@param  project  Description of the Parameter
     */
    public JSIndentOptionPane(String project) {
        super("javastyle.indenting", "pretty", project);
    }


    /**
     *  Description of the Method
     */
    public void _init() {
        ActionHandler ah = new ActionHandler();

        // "'else' starts on a new line"
        elseStartLine_sp = addComponent("else.start.line", "elseStartLine", new JCheckBox());

        // "'catch' starts on a new line"
        catchStartLine_sp = addComponent("catch.start.line", "catchStartLine", new JCheckBox());

        // "'throws' starts on a new line"
        throwsStartLine_sp = addComponent("throws.newline", "throwsStartLine", new JCheckBox());

        // "Indent continued lines"
        String[] indentContModes = {"single", "double", "param"};
        //indentCont = new JMouseComboBox(indentContModes);
        indentCont_sp = addComponent("surprise.return", "indentContinuedLines", new JMouseComboBox(indentContModes));

        // "Brackets { } :"
        addComponent(new JLabel(getIdeJavaStyleOption("brackets")));

        // "Class brackets are on lines by themselves"
        cbrackets_sp = addComponent("class.block.style", "brackets.classes", new JMouseComboBox(bracketModes));

        /*
		// // REVISIT: enum blocks currently use the class brackets style.
		// // "Enum brackets are on lines by themselves"
		//ebrackets = new JMouseComboBox(bracketModes);
		//ebrackets.setEditable(false);
		//ebrackets.setSelectedItem(props.getString("enum.block.style"));
		//addHelpFor(ebrackets, "brackets.enums");
		//addComponent(getIdeJavaStyleOption("brackets.enums"), ebrackets);
		*/
        // "Method brackets are on lines by themselves"
        mbrackets_sp = addComponent("method.block.style", "brackets.methods", new JMouseComboBox(bracketModes));

        // "Other brackets (if,while,for etc.) are on lines by themselves"
        brackets_sp = addComponent("block.style", "brackets.other", new JMouseComboBox(bracketModes));

        // "Create brackets around single-line blocks"
        forceBlocks = new JRadioButton();
        forceBlocks_sp = addComponent("force.block", "forceBlocks", forceBlocks);

        // "Remove brackets around single-line blocks"
        removeBlocks = new JRadioButton();
        removeBlocks.addActionListener(ah);
        removeBlocks_sp = addComponent("remove.excess.blocks", "removeBlocks", removeBlocks);
		
        ButtonGroup group = new ButtonGroup();
        group.add(forceBlocks);
        group.add(removeBlocks);
		
        // "Put empty methods and constructors on a single line"
        emptyBlocks_sp = addComponent("empty.block.single.line", "emptyBlocks", new JCheckBox());

        // "Indent case statements: nnn"
        caseIndent = new JTextField();
        caseIndent_sp = addComponent("case.indent", "caseIndent", caseIndent);

        addHelpArea();
    }


    /**
     *  Called when the options dialog's `OK' button is pressed. This should
     *  save any properties saved in this option pane.
     */
    public void _save() {
        elseStartLine_sp.save();
        catchStartLine_sp.save();
        throwsStartLine_sp.save();
        forceBlocks_sp.save();
        removeBlocks_sp.save();
        emptyBlocks_sp.save();
        indentCont_sp.save();
        cbrackets_sp.save();
        mbrackets_sp.save();
        brackets_sp.save();
        caseIndent_sp.saveInt(4,0);

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
            if (evt.getSource() == forceBlocks) {
                if (forceBlocks.isSelected()) {
                    removeBlocks.setSelected(false);
                }
            } else if (evt.getSource() == removeBlocks) {
                if (removeBlocks.isSelected()) {
                    forceBlocks.setSelected(false);
                }
            }
        }
    }

}

