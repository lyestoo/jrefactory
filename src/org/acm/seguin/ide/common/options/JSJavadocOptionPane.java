/*
 *JSJavadocOptionPane.java - JavaStyle javadoc options panel
 *Copyright (C) 2000,2001 Dirk Moebius
 *
 *jEdit buffer options:
 *:tabSize=4:indentSize=4:noTabs=false:maxLineLen=0:
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.acm.seguin.ide.common.options;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;


/**
 *    Description of the Class
 *
 * @author       Mike Atkinson (<a
 *      href="mailto:javastyle@ladyshot.demon.co.uk">
 *      Mike@ladyshot.demon.co.uk</a> )
 * @author       Dirk Moebius (<a href="mailto:dmoebius@gmx.net">
 *      dmoebius@gmx.net </a> )
 * @created      03 September 2003
 * @version      $Version: $
 * @since        1.0
 */
public class JSJavadocOptionPane extends JSHelpOptionPane {
   private JTextField jdocIndent;
   private JMouseComboBox jdocOnClasses;
   private JMouseComboBox jdocOnEnums;
   private JMouseComboBox jdocOnFields;
   private JMouseComboBox jdocOnMethods;
   private JCheckBox jdocReformat;
   private JTextField jdocWordwrap;
   private JTextField jdocWordwrapIgnore;

   private SelectedPanel jdocIndent_sp;
   private SelectedPanel jdocInnerClasses_sp;
   private SelectedPanel jdocKeepAll_sp;
   private SelectedPanel jdocOnClasses_sp;
   private SelectedPanel jdocOnEnums_sp;
   private SelectedPanel jdocOnFields_sp;
   private SelectedPanel jdocOnMethods_sp;
   private SelectedPanel jdocReformat_sp;
   private SelectedPanel jdocSingleLine_sp;
   private SelectedPanel jdocWordwrap_sp;
   private SelectedPanel jdocWordwrapIgnore_sp;

   private final static String[] prots =
         {
         getIdeJavaStyleOption("prot.none"),
         getIdeJavaStyleOption("prot.public"),
         getIdeJavaStyleOption("prot.protected"),
         getIdeJavaStyleOption("prot.package"),
         getIdeJavaStyleOption("prot.all")
         };

   /**
    *    Constructor for the JSJavadocOptionPane object
    *
    * @param    project  Description of the Parameter
    */
   public JSJavadocOptionPane(String project) {
      super("javastyle.javadoc", "pretty", project);
   }

   /**    Description of the Method  */
   public void _init() {
      ActionHandler ah = new ActionHandler();

      //PropertiesFile props = JavaStylePlugin.getProperties();

      // "Keep all JavaDoc comments"
      jdocKeepAll_sp = addComponent("keep.all.javadoc", "jdocKeepAll", new JCheckBox());

      // "Create JavaDoc comments for inner classes"
      jdocInnerClasses_sp = addComponent("document.nested.classes", "jdocInnerClasses", new JCheckBox());

      // "Allow single line JavaDoc comments"
      jdocSingleLine_sp = addComponent("allow.singleline.javadoc", "jdocSingleLine", new JCheckBox());

      // "Create JavaDoc, if missing, on..."
      addComponent(new JLabel(getIdeJavaStyleOption("jdocCreate")));

      // "...Fields:"
      jdocOnFields = new JMouseComboBox(prots);
      jdocOnFields_sp = addComponent("field.minimum", "jdocOnFields", jdocOnFields);
      jdocOnFields.setSelectedIndex(protnameToIndex(props.getString("field.minimum")));

      // "...Methods:"
      jdocOnMethods = new JMouseComboBox(prots);
      jdocOnMethods_sp = addComponent("method.minimum", "jdocOnMethods", jdocOnMethods);
      jdocOnMethods.setSelectedIndex(protnameToIndex(props.getString("method.minimum")));

      // "...Classes:"
      jdocOnClasses = new JMouseComboBox(prots);
      jdocOnClasses_sp = addComponent("class.minimum", "jdocOnClasses", jdocOnClasses);
      jdocOnClasses.setSelectedIndex(protnameToIndex(props.getString("class.minimum")));

      // "...Enums:"
      jdocOnEnums = new JMouseComboBox(prots);
      jdocOnEnums_sp = addComponent("enum.minimum", "jdocOnEnums", jdocOnEnums);
      jdocOnEnums.setSelectedIndex(protnameToIndex(props.getString("enum.minimum")));

      // "Wordwrap JavaDoc comments"
      boolean reformat_comments = props.getBoolean("reformat.comments");

      jdocReformat = new JCheckBox();
      jdocReformat.addActionListener(ah);
      jdocReformat_sp = addComponent("reformat.comments", "jdocReformat", jdocReformat);
      jdocReformat.setSelected(reformat_comments);

      // "At column: nnn"
      jdocWordwrap = new JTextField();
      jdocWordwrap_sp = addComponent("javadoc.wordwrap.max", "jdocWordwrap", jdocWordwrap);
      jdocWordwrap.setEnabled(reformat_comments);

      // "Only lines longer than: nnn"
      jdocWordwrapIgnore = new JTextField();
      jdocWordwrapIgnore_sp = addComponent("javadoc.wordwrap.min", "jdocWordwrapIgnore", jdocWordwrapIgnore);
      jdocWordwrapIgnore.setEnabled(reformat_comments);

      // "Spaces between JavaDoc star and text: nnn"
      jdocIndent_sp = addComponent("javadoc.indent", "jdocIndent", new JTextField());

      addHelpArea();
   }

   /**
    *    Called when the options dialog's `OK' button is pressed.
    *    This should save any properties saved in this option pane.
    */
   public void _save() {
      jdocIndent_sp.saveInt(1, 0);
      jdocInnerClasses_sp.save();
      jdocKeepAll_sp.save();
      jdocOnClasses_sp.save(indexToProtname(jdocOnClasses.getSelectedIndex()));
      jdocOnEnums_sp.save(indexToProtname(jdocOnEnums.getSelectedIndex()));
      jdocOnFields_sp.save(indexToProtname(jdocOnFields.getSelectedIndex()));
      jdocOnMethods_sp.save(indexToProtname(jdocOnMethods.getSelectedIndex()));
      jdocReformat_sp.save();
      jdocSingleLine_sp.save();
      jdocWordwrap_sp.saveInt(78, 0);
      jdocWordwrapIgnore_sp.saveInt(40, 0);
   }

   /**
    *    Description of the Method
    *
    * @param    index  Description of Parameter
    * @return          Description of the Returned Value
    */
   private static String indexToProtname(int index) {
      switch (index) {
      case 1:
         return "public";
      case 2:
         return "protected";
      case 3:
         return "package";
      case 4:
         return "all";
      default:
         return "none";
      }
   }

   /**
    *    Description of the Method
    *
    * @param    propname  Description of Parameter
    * @return             Description of the Returned Value
    */
   private static int protnameToIndex(String propname) {
      if (propname == null) {
         return 0;
      }
      else if (propname.equalsIgnoreCase("public")) {
         return 1;
      }
      else if (propname.equalsIgnoreCase("protected")) {
         return 2;
      }
      else if (propname.equalsIgnoreCase("package") || propname.equalsIgnoreCase("default")) {
         return 3;
      }
      else if (propname.equalsIgnoreCase("private") || propname.equalsIgnoreCase("all")) {
         return 4;
      }
      else {
         return 0;
      }
   }

   /**
    *    Description of the Class
    *
    * @author       Mike Atkinson (Mike)
    * @created      14 July 2003
    */
   private class ActionHandler implements ActionListener {
      /**
       *    Description of the Method
       *
       * @param    evt  Description of Parameter
       */
      public void actionPerformed(ActionEvent evt) {
         if (evt.getSource() == jdocReformat) {
            boolean sel = jdocReformat.isSelected();

            jdocWordwrap.setEnabled(sel);
            jdocWordwrapIgnore.setEnabled(sel);
         }
      }
   }

}

