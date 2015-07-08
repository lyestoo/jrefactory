/*
 * JSGeneralOptionPane.java - JavaStyle general options panel
 * Copyright (C) 2000,2001 Dirk Moebius
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


import javax.swing.JCheckBox;
import javax.swing.JTextField;


/**
 * @author    Mike Atkinson (<a href="mailto:javastyle@ladyshot.demon.co.uk">
 *      Mike@ladyshot.demon.co.uk</a> )
 * @author    Dirk Moebius (<a href="mailto:dmoebius@gmx.net">
 *      dmoebius@gmx.net </a> )
 * @created   04 September 2003
 * @version   $Version: $
 * @since     1.0
 */
public class JSGeneralOptionPane extends JSHelpOptionPane {

   //private JCheckBox formatOnSave;
   private SelectedPanel formatOnSave_sp;
   private SelectedPanel checkOnSave_sp;
   private SelectedPanel jdk_sp;


   /**
    * Constructor for the JSGeneralOptionPane object
    *
    * @param project  Description of the Parameter
    */
   public JSGeneralOptionPane(String project) {
      super("javastyle.general", "pretty", project);
   }


   /** Description of the Method  */
   public void _init() {
      // "Reformat when buffer is saved"
      formatOnSave_sp = addComponent("formatOnSave", "formatOnSave", new JCheckBox());
      checkOnSave_sp = addComponent("checkOnSave", "checkOnSave", new JCheckBox());
      //-- general options
      addSeparator("general-opt.label");
      jdk_sp = addComponent("jdk", "jdk", new JTextField(5));

      addHelpArea();
   }


   /**
    * Called when the options dialog's `OK' button is pressed. This should
    * save any properties saved in this option pane.
    */
   public void _save() {
      formatOnSave_sp.save();
      checkOnSave_sp.save();
      jdk_sp.save();
   }

}

