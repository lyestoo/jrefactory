package org.acm.seguin.ide.common.options;

import org.acm.seguin.pmd.RuleSetNotFoundException;
import org.acm.seguin.pmd.Rule;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;



/**
 *    Description of the Class
 *
 * @author      Mike Atkinson
 */
public class PMDOptionPane extends JSHelpOptionPane {
   JTextField txtMinTileSize;
   JTextField txtCustomRules;
   private SelectedPanel pnlCustomRules_sp;
   private SelectedPanel directoryPopupBox_sp;
   private SelectedPanel txtMinTileSize_sp;

   private SelectedRules rules;
   private JTextArea exampleTextArea = new JTextArea(10, 80);
   private JCheckBox directoryPopupBox;

   /**
    *    Constructor for the PMDOptionPane object
    *
    * @param    project  Description of Parameter
    */
   public PMDOptionPane(String project) {
      super("javastyle.pmd", "pmd", project);
      try {
         rules = new SelectedRules( ((project==null) ? "default" : project), this);
      } catch (RuleSetNotFoundException rsne) {
         rsne.printStackTrace();
      }
   }

   /**    Description of the Method */
   public void _init() {
      removeAll();

      addComponent(new JLabel(getIdeProperty("javastyle.pmd.more.info")));

      JPanel rulesPanel = new JPanel(new BorderLayout());
      rulesPanel.setBorder(BorderFactory.createTitledBorder(getIdeProperty("javastyle.pmd.rules")));

      JList list = new CheckboxList(rules.getAllBoxes());
      list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
      rulesPanel.add(new JScrollPane(list), BorderLayout.NORTH);

      //Custom Rule Panel Defination.
      txtCustomRules = new JTextField(props.getString("pmd.customRulesPath", ""), 30);

      directoryPopupBox = new JCheckBox("Ask for directory?", props.getBoolean("pmd.ui.directorypopup", false));

      JLabel lblMinTileSize = new JLabel("Minimum Tile Size :");
      txtMinTileSize = new JTextField(props.getString("pmd.cpd.defMinTileSize", "100"), 5);

      addComponent(null, null, rulesPanel);

      
      JPanel textPanel = new JPanel();
      exampleTextArea.setEditable(false);
      exampleTextArea.setLineWrap(true);
      exampleTextArea.setWrapStyleWord(true);
      textPanel.setBorder(BorderFactory.createTitledBorder(getIdeProperty("javastyle.pmd.example")));
      textPanel.add(new JScrollPane(exampleTextArea));
      addComponent(null,null,textPanel);
      
      addComponent(new JLabel(""));
      addComponent(new JLabel(getIdeJavaStyleOption("pmd.customRulesPath")));
      pnlCustomRules_sp = addComponent("pmd.path", null, txtCustomRules);
      directoryPopupBox_sp = addComponent("pmd.ask.for.directory", "ui.directorypopup", new JCheckBox());
      addComponent(new JLabel(getIdeJavaStyleOption("cpd.defMinTileSize")));
      txtMinTileSize_sp = addComponent("pmd.min.tile.size", null, txtMinTileSize);

      addHelpArea();
   }

   /**    Description of the Method */
   public void _save() {
      rules.save();
      pnlCustomRules_sp.save();
      directoryPopupBox_sp.save();
      txtMinTileSize_sp.save();
   }

   /**
    *    Description of the Class
    *
    * @author      Chris Seguin
    */
   public class CheckboxList extends JList {

      /**
       *    Constructor for the CheckboxList object
       *
       * @param    args  Description of Parameter
       * @paramargs      Description of Parameter
       * @paramargs      Description of Parameter
       * @paramargs      Description of Parameter
       */
      public CheckboxList(Object[] args) {
         super(args);
         setCellRenderer(new CheckboxListCellRenderer());
         addMouseListener(new MyMouseAdapter());
      }

      /**
       *    Description of the Class
       *
       * @author      Chris Seguin
       */
      public class CheckboxListCellRenderer implements ListCellRenderer {
         /**
          *    Gets the ListCellRendererComponent attribute of the
          *    CheckboxListCellRenderer object
          *
          * @param    list          Description of Parameter
          * @param    value         Description of Parameter
          * @param    index         Description of Parameter
          * @param    isSelected    Description of Parameter
          * @param    cellHasFocus  Description of Parameter
          * @return                 The ListCellRendererComponent
          *      value
          */
         public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JCheckBox box = (JCheckBox) value;

            box.setEnabled(isEnabled());
            box.setFont(getFont());
            box.setFocusPainted(false);
            box.setBorderPainted(true);
            box.setBorder(isSelected ? UIManager.getBorder("List.focusCellHighlightBorder") : new EmptyBorder(1, 1, 1, 1));
            return box;
         }
      }

      /**
       *    Description of the Class
       *
       * @author      Chris Seguin
       */
      private class MyMouseAdapter extends MouseAdapter {
         String previousExample = "";
         private java.awt.event.MouseMotionAdapter mmma = new java.awt.event.MouseMotionAdapter() {
               public void mouseMoved(MouseEvent e) {
                  handleMouseEvent(e);
               }
            };
         /**
          *    Description of the Method
          *
          * @param    e  Description of Parameter
          */
         public void Exited(MouseEvent e) {
            removeMouseMotionListener(mmma);
         }
         /**
          *    Description of the Method
          *
          * @param    e  Description of Parameter
          */
         public void mouseEntered(MouseEvent e) {
            handleMouseEvent(e);
            addMouseMotionListener(mmma);
         }

         /**
          *    Description of the Method
          *
          * @param    e  Description of Parameter
          */
         public void mousePressed(MouseEvent e) {
            int index = locationToIndex(e.getPoint());

            if (index != -1) {
               JCheckBox box = (JCheckBox) getModel().getElementAt(index);

               box.setSelected(!box.isSelected());
               repaint();
            }
         }
         
         private void handleMouseEvent(MouseEvent e) {
            int index = locationToIndex(e.getPoint());
            if (index != -1) {
               JCheckBox box = (JCheckBox) getModel().getElementAt(index);
               Rule rule = rules.getRule(box);
               String example = rule.getExample().trim();
               if (example.startsWith("\r")) {
                  example = example.substring(1);
               }
               if (example.startsWith("\n")) {
                  example = example.substring(1);
               }
               if (previousExample==null || !previousExample.equals(example)) {
                  previousExample = example;
                  exampleTextArea.setText(example);
                  exampleTextArea.setCaretPosition(0);
                  String description = rule.getDescription().trim();
                  char prev = ' ';
                  StringBuffer sb = new StringBuffer();
                  for (int i=0; i<description.length(); i++) {
                     char c=description.charAt(i);
                     if (c=='\n') {
                        c = ' ';
                     }
                     if (c=='\r') {
                        c = ' ';
                     }
                     if (!(c==' ' && c==prev)) {
                        prev = c;
                        sb.append(c);
                     }
                  }
                  setHelpText(sb.toString());
               }
            }
         }
      }

   }
}

