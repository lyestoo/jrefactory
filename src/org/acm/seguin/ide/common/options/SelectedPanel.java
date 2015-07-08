/*
 *JSIndentOptionPane.java - JavaStyle indenting options panel
 *Copyright (C) 2001 Dirk Moebius
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

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import org.acm.seguin.ide.common.options.PropertiesFile;
import org.acm.seguin.ide.common.IDEPlugin;

/**
 * @author       Mike Atkinson (<a
 *      href="mailto:javastyle@ladyshot.demon.co.uk">
 *      Mike@ladyshot.demon.co.uk</a> )
 * @created      02 September 2003
 * @version      $Version: $
 * @since        1.5
 */
public class SelectedPanel extends JPanel {
   private JComponent component;
   private JLabel optionLabel = null;
   private PropertiesFile props;
   private String option;
   private JCheckBox cb = null;

   /**
    *    Constructor for the SelectedPanel object
    *
    * @param    optionPane     Description of the Parameter
    * @param    props          Description of the Parameter
    * @param    project        Description of the Parameter
    * @param    option         Description of the Parameter
    * @param    defaultOption  Description of the Parameter
    * @param    label          Description of the Parameter
    * @param    comp           Description of the Parameter
    */
   public SelectedPanel(JSHelpOptionPane optionPane, PropertiesFile props, String project, String option, String defaultOption, String label, JComponent comp) {
      this.props = props;
      this.option = option;

      String fullLabel = (label == null) ? null : ("options.javastyle." + label);
      boolean selected = false;

      component = comp;
      setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
      if (!"default".equals(project)) {
         selected = props.isLocalProperty(option);
         cb = new JCheckBox("    ");
         cb.setSelected(selected);
         add(cb);
         cb.addChangeListener(
            new ChangeListener() {
               public void stateChanged(ChangeEvent e) {
                  component.setEnabled(cb.isSelected());
                  if (optionLabel != null) {
                     optionLabel.setEnabled(cb.isSelected());
                  }
               }
            });
         optionPane.addHelpFor(cb, "project.local");
      }
      else {
         selected = true;
      }
      component.setEnabled(selected);

      String value = null;

      if (option != null) {
         if (defaultOption != null) {
            value = props.getString(option, defaultOption);
         }
         else {
            value = props.getString(option);
         }
         if (value == null) {
            System.out.println("WARNING: Option Not Found option=" + option);
         }
      }
      if (component instanceof AbstractButton) {
         String text = IDEPlugin.getProperty(fullLabel);

         if (text == null) {
            System.out.println("WARNING: IDEPlugin.getProperty() not found fullLabel=" + fullLabel);
         }
         ((AbstractButton) component).setText(text);
         if (option != null) {
            ((AbstractButton) component).setSelected(props.getBoolean(option));
         }
      }
      else if (component instanceof JMouseComboBox) {
         ((JMouseComboBox) component).setEditable(false);
         if (value != null) {
            ((JMouseComboBox) component).setSelectedItem(value);
         }
      }
      else if (component instanceof JTextField) {
         if (value != null) {
            ((JTextField) component).setText(value);
         }
         //((JTextField)component).setColumns(20);
      }
      if (label != null && component != null) {
         optionPane.addHelpFor(component, label);
      }
      if (component instanceof AbstractButton) {
         add(component);
         optionPane.addComponent(this);
      }
      else if (fullLabel != null) {
         optionLabel = new JLabel(IDEPlugin.getProperty(fullLabel));
         optionLabel.setEnabled(selected);
         add(optionLabel);
         optionPane.addComponent(this, component);
      }
      else {
         optionPane.addComponent(this, component);
      }
   }

   /**
    *    Gets the PropertiesFile attribute of the SelectedPanel
    *    object
    *
    * @return      The PropertiesFile value
    */
   public PropertiesFile getPropertiesFile() {
      return props;
   }

   /**    Description of the Method  */
   public void save() {
      if (localAvailable()) {
         if (component instanceof AbstractButton) {
            props.setString(option, ((AbstractButton) component).isSelected() ? "true" : "false");
         }
         else if (component instanceof JTextField) {
            props.setString(option, ((JTextField) component).getText());
         }
         else if (component instanceof JMouseComboBox) {
            props.setString(option, ((JMouseComboBox) component).getSelectedItem().toString());
         }
         else {
         }
      }
      else if (localDelete()) {
         props.deleteKey(option);
      }
   }

   /**
    *    Description of the Method
    *
    * @param    value  Description of the Parameter
    */
   public void save(String value) {
      if (localAvailable()) {
         props.setString(option, value);
      }
      else if (localDelete()) {
         props.deleteKey(option);
      }
   }

   /**
    *    Description of the Method
    *
    * @param    defaultValue  Description of Parameter
    * @param    minValue      Description of Parameter
    */
   public void saveInt(int defaultValue, int minValue) {
      int val = defaultValue;

      try {
         val = Integer.parseInt(((JTextField) component).getText());
      }
      catch (NumberFormatException nfex) {
      }
      if (val < minValue) {
         val = minValue;
      }
      save(Integer.toString(val));
   }

   /**
    *    Description of the Method
    *
    * @return      Description of the Returned Value
    */
   public boolean localAvailable() {
      return (cb == null || cb.isSelected());
   }

   /**
    *    Description of the Method
    *
    * @return      Description of the Returned Value
    */
   public boolean localDelete() {
      return (props.isLocalProperty(option) && !cb.isSelected());
   }
}


