/*
 *  JSCommentOptionPane.java - JavaStyle options panel for C-style comments
 *  Copyright (C) 2000,2001 Dirk Moebius
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


/**
 *@author     Mike Atkinson (<a href="mailto:javastyle@ladyshot.demon.co.uk">
 *      Mike@ladyshot.demon.co.uk</a> )
 *@author     Dirk Moebius (<a href="mailto:dmoebius@gmx.net">dmoebius@gmx.net
 *      </a>)
 *@created    16 July 2003
 *@version    $Version: $
 *@since      1.0
 */
public class JSCommentOptionPane extends JSHelpOptionPane {

    private JCheckBox formatCStyle;
    private JCheckBox fillCStyleWithStars;
    private JCheckBox alignCStyle;
    private JCheckBox ownLine;
    private JCheckBox csOwnLine;
    private JCheckBox ownLineIndent;
    private JCheckBox appendedIndent;
    private JTextField absoluteIndent;
    private JTextField incrementalIndent;
    private JTextField cStyleIndent;

    private SelectedPanel formatCStyle_sp;
    private SelectedPanel fillCStyleWithStars_sp;
    private SelectedPanel alignCStyle_sp;
    private SelectedPanel cStyleIndent_sp;
    private SelectedPanel ownLine_sp;
    private SelectedPanel csOwnLine_sp;
    private SelectedPanel appendedIndent_sp;
    private SelectedPanel ownLineIndent_sp;
    private SelectedPanel absoluteIndent_sp;
    private SelectedPanel incrementalIndent_sp;



    /**
     *  Constructor for the JSCommentOptionPane object
     *
     *@param  project  Description of the Parameter
     */
    public JSCommentOptionPane(String project) {
        super("javastyle.comments", "pretty", project);
    }


    /**
     *  Description of the Method
     */
    public void _init() {
        ActionHandler ah = new ActionHandler();

        String cStyleFormat = props.getString("c.style.format");

        // "Format C-style comments"
        formatCStyle = new JCheckBox();
        formatCStyle_sp = addComponent("c.style.format", "csFormat", formatCStyle);
        formatCStyle.setSelected(!cStyleFormat.equals("leave"));
        formatCStyle.addActionListener(ah);

        // "Fill C-style comments with stars on the left"
        fillCStyleWithStars = new JCheckBox();
        fillCStyleWithStars_sp = addComponent(null, "csFillWithStars", fillCStyleWithStars);
        fillCStyleWithStars.setSelected(cStyleFormat.equals("maintain.space.star") || cStyleFormat.equals("align.star"));
        fillCStyleWithStars.addActionListener(ah);

        // "Align text to the left in C-style comments"
        alignCStyle = new JCheckBox();
        alignCStyle_sp = addComponent(null, "csAlign", alignCStyle);
        alignCStyle.setSelected(cStyleFormat.equals("align.blank") || cStyleFormat.equals("align.star"));
        alignCStyle.addActionListener(ah);

        // "Number of spaces to indent: xxx"
        cStyleIndent = new JTextField();
        cStyleIndent_sp = addComponent("c.style.indent", "csIndentAmount", cStyleIndent);

        // "Put end-line comments on an own line"
        ownLine = new JCheckBox();
        ownLine.addActionListener(ah);
        ownLine_sp = addComponent("singleline.comment.ownline", "slOwnLine", ownLine);

        csOwnLine = new JCheckBox();
        csOwnLine.addActionListener(ah);
        csOwnLine_sp = addComponent("cstyle.comment.ownline", "treatSLasCS", csOwnLine);

        // "Indent end-line comments to a certain column"
        appendedIndent = new JCheckBox();
        String indentstyle_shared = props.getString("singleline.comment.indentstyle.shared");
        appendedIndent.setSelected(indentstyle_shared.toUpperCase().startsWith("A"));
        appendedIndent.addActionListener(ah);
        appendedIndent_sp = addComponent("singleline.comment.indentstyle.shared", "slAppendedIndent", appendedIndent);

        // "Indent single line comments to a certain column"
        ownLineIndent = new JCheckBox();
        ownLineIndent_sp = addComponent("singleline.comment.indentstyle.ownline", "slOwnLineIndent", ownLineIndent);
        String indentstyle_ownline = props.getString("singleline.comment.indentstyle.ownline");
        ownLineIndent.setSelected(indentstyle_ownline.toUpperCase().startsWith("A"));
        ownLineIndent.addActionListener(ah);

        // "Indent to column: xxx"
        absoluteIndent = new JTextField();
        absoluteIndent_sp = addComponent("singleline.comment.absoluteindent", "slAbsoluteIndent", absoluteIndent);

        // "Space between code and comment: xxx"
        incrementalIndent = new JTextField();
        incrementalIndent_sp = addComponent("singleline.comment.incrementalindent", "slIncrementalIndent", incrementalIndent);

        addHelpArea();
        ah.update();
    }


    /**
     *  Called when the options dialog's `OK' button is pressed. This should
     *  save any properties saved in this option pane.
     */
    public void _save() {
        String cStyleFormat = "leave";
        if (formatCStyle.isSelected()) {
            if (fillCStyleWithStars.isSelected()) {
                if (alignCStyle.isSelected()) {
                    cStyleFormat = "align.star";
                } else {
                    cStyleFormat = "maintain.space.star";
                }
            } else {
                cStyleFormat = "align.blank";
            }
        }

        formatCStyle_sp.save(cStyleFormat);
        cStyleIndent_sp.saveInt(0,0);
        ownLine_sp.save();
        csOwnLine_sp.save();
        //appendedIndent_sp.save();
		if (appendedIndent.isSelected()) {
			appendedIndent_sp.save("absolute");
		} else {
			appendedIndent_sp.save("incremental");
		}
		if (ownLineIndent.isSelected()) {
			ownLineIndent_sp.save("absolute");
		} else {
			ownLineIndent_sp.save("code");
		}
        absoluteIndent_sp.saveInt(0,0);
        incrementalIndent_sp.saveInt(0,0);
    }



    /**
     *  Description of the Class
     *
     *@author     Mike
     *@created    16 July 2003
     */
    private class ActionHandler implements ActionListener {

        /**
         *  Description of the Method
         *
         *@param  evt  Description of the Parameter
         */
        public void actionPerformed(ActionEvent evt) {
            update();

            if (evt.getSource() == fillCStyleWithStars) {
                if (!fillCStyleWithStars.isSelected() && !alignCStyle.isSelected()) {
                    alignCStyle.setSelected(true);
                }
            } else if (evt.getSource() == alignCStyle) {
                if (!fillCStyleWithStars.isSelected() && !alignCStyle.isSelected()) {
                    fillCStyleWithStars.setSelected(true);
                }
            }

            cStyleIndent.setEnabled(formatCStyle.isSelected() && alignCStyle.isSelected());
        }


        /**
         *  Description of the Method
         */
        public void update() {
            fillCStyleWithStars.setEnabled(formatCStyle.isSelected());
            alignCStyle.setEnabled(formatCStyle.isSelected());
            cStyleIndent.setEnabled(formatCStyle.isSelected() && alignCStyle.isSelected());

            appendedIndent.setEnabled(!ownLine.isSelected());
            absoluteIndent.setEnabled(ownLineIndent.isSelected() || appendedIndent.isSelected());
            incrementalIndent.setEnabled(!ownLine.isSelected() && !appendedIndent.isSelected());
        }

    }
}

