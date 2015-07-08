/*
 * JSSortOptionPane.java - JavaStyle sort options panel
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


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractListModel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import java.util.StringTokenizer;
import org.acm.seguin.ide.common.options.PropertiesFile;
import org.acm.seguin.ide.common.IDEPlugin;
import org.acm.seguin.ide.common.IDEInterface;



/**
 *@author     Mike Atkinson (<a href="mailto:javastyle@ladyshot.demon.co.uk">
 *      Mike@ladyshot.demon.co.uk</a> )
 *@author     Dirk Moebius (<a href="mailto:dmoebius@gmx.net">dmoebius@gmx.net
 *      </a>)
 *@created    03 September 2003
 *@version    $Version: $
 *@since      1.0
 */
public class JSSortOptionPane extends JSHelpOptionPane {

    private SortTableModel tableModel;
    private JTable table;
    private JPanel configPanel;
    private Component configComponent;
    private JCheckBox sortImports;
    private JButton bMoveUp;
    private JButton bMoveDown;
    private JTextField importSortImportant;

    private SelectedPanel sortImports_sp;
    private SelectedPanel importSortImportant_sp;
    private SelectedPanel sortOrder_sp;
    private SelectedPanel sortButtons_sp;
    private SelectedPanel sortOption_sp;


    /**
     *  Constructor for the JSSortOptionPane object
     *
     *@param  project  Description of the Parameter
     */
    public JSSortOptionPane(String project) {
        super("javastyle.sorting", "pretty", project);
    }


    /**
     *  Description of the Method
     */
    public void _init() {
        ActionHandler ah = new ActionHandler();

        // "Sort imports and top-level classes"
        sortImports = new JCheckBox();
        sortImports.addActionListener(ah);
        sortImports_sp = addComponent("sort.top", "sorting.sortImports", sortImports);

        // "Sort this packages at top: xxx"
        addComponent(new JLabel(getIdeJavaStyleOption("sorting.importSortImportant.label")));
        importSortImportant = new JTextField();
        //importSortImportant_sp = addComponent("import.sort.important", "sorting.importSortImportant", importSortImportant);
        importSortImportant_sp = addComponent("import.sort.important", null, importSortImportant);

        // "Sort order:"
        //addComponent(new JLabel(getIdeJavaStyleOption("sorting.labelSortOrder")));
		addComponent(new JLabel(" "));

        // Sorting table
        tableModel = new SortTableModel();
        table = new JTable(tableModel);
        table.setTableHeader(null);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getSelectionModel().addListSelectionListener(ah);
        addHelpFor(table, "sorting.table");

        TableColumn column = table.getColumnModel().getColumn(0);
        int checkBoxWidth = new JCheckBox().getPreferredSize().width;
        column.setPreferredWidth(checkBoxWidth);
        column.setMinWidth(checkBoxWidth);
        column.setWidth(checkBoxWidth);
        column.setMaxWidth(checkBoxWidth);
        column.setResizable(false);

        JScrollPane scrTable = new JScrollPane(table);
        scrTable.setMinimumSize(new Dimension(200, 50));
        scrTable.setPreferredSize(new Dimension(300, 80));

        GridBagConstraints cons = new GridBagConstraints();
        cons.gridy = y++;
        cons.gridwidth = GridBagConstraints.REMAINDER;
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.weightx = 1.0f;
        gridBag.setConstraints(scrTable, cons);
        //add(scrTable);
        sortOrder_sp = addComponent(null, "sorting.labelSortOrder", scrTable);

        // "Move up" button
        bMoveUp = new JButton(getIdeJavaStyleOption("sorting.moveUp"));
        bMoveUp.addActionListener(ah);

        // "Move down" button
        bMoveDown = new JButton(getIdeJavaStyleOption("sorting.moveDown"));
        bMoveDown.addActionListener(ah);

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(bMoveUp);
        buttons.add(bMoveDown);

        cons = new GridBagConstraints();
        cons.gridy = y++;
        cons.gridwidth = GridBagConstraints.REMAINDER;
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.weightx = 1.0f;
        gridBag.setConstraints(buttons, cons);
        sortButtons_sp = addComponent(null, null, buttons);

        // "Configure Sort Option" panel
        configPanel = new ConfigComponent();
        configPanel.setBorder(new TitledBorder(getIdeJavaStyleOption("sorting.configPanel")));
        configComponent = new JPanel();
        configPanel.add(configComponent, BorderLayout.CENTER);

        cons = new GridBagConstraints();
        cons.gridy = y++;
        cons.gridwidth = GridBagConstraints.REMAINDER;
        cons.fill = GridBagConstraints.BOTH;
        cons.weightx = 1.0f;
        cons.weighty = 1.0f;
        gridBag.setConstraints(configPanel, cons);
        sortOption_sp = addComponent(null, null, configPanel);

        addHelpArea();

        SwingUtilities.invokeLater(
            new Runnable() {
                public void run() {
                    table.setRowSelectionInterval(0, 0);
                }
            });
    }

	
	private static class ConfigComponent extends JPanel {
		private Dimension minSize = new Dimension(200, 100);
		private Dimension prefSize = new Dimension(300, 150);
		public ConfigComponent() {
			super(new BorderLayout());
		}
		public Dimension getMinimumSize() {
			return minSize;
		}
		public Dimension getPreferredSize() {
			return prefSize;
		}
	}

    /**
     *  Called when the options dialog's `OK' button is pressed. This should
     *  save any properties saved in this option pane.
     */
    public void _save() {
        sortImports_sp.save();
        importSortImportant_sp.save();

	    PropertiesFile props = sortOrder_sp.getPropertiesFile();
		if (sortOrder_sp.localAvailable()) {
			int nr = 1;
			for (int i = 0; i < tableModel.getRowCount(); ++i) {
				SortOption sortOption = tableModel.getSortOption(i);
				if (sortOption.isSelected()) {
					props.setString("sort." + nr++, sortOption.getFullProperty());
				}
				sortOption.saveArgument(props);
			}
			props.deleteKey("sort." + nr);
		} else if (sortOrder_sp.localDelete()) {
			for (int i = 0; i < tableModel.getRowCount(); ++i) {
				props.deleteKey("sort." + i);
			}
        }
    }



    /**
     *  Handles all button and list selection actions in the <code>JSSortOptionPane</code>
     *  .
     *
     *@author     Mike
     *@created    03 September 2003
     */
    private class ActionHandler implements ActionListener, ListSelectionListener {

        /**
         *  Description of the Method
         *
         *@param  evt  Description of the Parameter
         */
        public void actionPerformed(ActionEvent evt) {
            if (evt.getSource() == bMoveUp) {
                int row = table.getSelectedRow();
                tableModel.swap(row - 1, row);
                table.setRowSelectionInterval(row - 1, row - 1);
            } else if (evt.getSource() == bMoveDown) {
                int row = table.getSelectedRow();
                tableModel.swap(row, row + 1);
                table.setRowSelectionInterval(row + 1, row + 1);
            } else if (evt.getSource() == sortImports) {
                importSortImportant.setEnabled(sortImports.isSelected());
            }
        }


        /**
         *  Description of the Method
         *
         *@param  evt  Description of the Parameter
         */
        public void valueChanged(ListSelectionEvent evt) {
            int row = table.getSelectedRow();
            bMoveUp.setEnabled(row > 0);
            bMoveDown.setEnabled(row >= 0 && row < table.getRowCount() - 1);
            configPanel.remove(configComponent);
            configComponent = tableModel.getSortOption(row).getConfigureComponent();
            configPanel.add(configComponent);
            configComponent.invalidate();
            configPanel.invalidate();
            configPanel.validate();
            repaint();
        }

    }



    /**
     *  A <code>SortOption</code> is one of the properties in <code>pretty.settings</code>
     *  with a key <code>sort.</code>&lt;n&gt;.
     *
     *@author     Mike
     *@created    03 September 2003
     */
    private interface SortOption {

        /**
         *  Gets the fullProperty attribute of the SortOption object
         *
         *@return    The fullProperty value
         */
        public String getFullProperty();


        /**
         *  Gets the description attribute of the SortOption object
         *
         *@return    The description value
         */
        public String getDescription();


        /**
         *  Gets the selected attribute of the SortOption object
         *
         *@return    The selected value
         */
        public boolean isSelected();


        /**
         *  Sets the selected attribute of the SortOption object
         *
         *@param  isSelected  The new selected value
         */
        public void setSelected(boolean isSelected);


        /**
         *  Gets the configureComponent attribute of the SortOption object
         *
         *@return    The configureComponent value
         */
        public Component getConfigureComponent();


        /**
         *  Description of the Method
         */
        public void saveArgument(PropertiesFile props);

    }



    /**
     *  The table model for the table of <code>SortOptions</code>.
     *
     *@author     Mike
     *@created    03 September 2003
     */
    private class SortTableModel extends AbstractTableModel {

        /**
         *  Constructor for the SortTableModel object
         */
        public SortTableModel() {
            super();

            int i = 0;
            while (props.getString("sort." + (i + 1)) != null) {
                ++i;
            }

            sortOption = new SortOption[i > 7 ? i : 7];
            i = 0;

            boolean foundType = false;
            boolean foundClass = false;
            boolean foundFinal = false;
            boolean foundProtection = false;
            boolean foundMethod = false;
            boolean foundAlphabetical = false;
            boolean foundFieldInitializers = false;
            String prop = null;

            while ((prop = props.getString("sort." + (i + 1))) != null) {
                StringTokenizer tok = new StringTokenizer(prop, "()");
                String name = tok.nextToken();
                String args = tok.hasMoreTokens() ? tok.nextToken() : "";
                if (name.equals("Type")) {
                    sortOption[i] = new TypeSortOption(args, true);
                    foundType = true;
                } else if (name.equals("Class")) {
                    sortOption[i] = new ClassSortOption(args, true);
                    foundClass = true;
                } else if (name.equals("Final")) {
                    sortOption[i] = new FinalSortOption(args, true);
                    foundFinal = true;
                } else if (name.equals("Protection")) {
                    sortOption[i] = new ProtectionSortOption(args, true);
                    foundProtection = true;
                } else if (name.equals("Method")) {
                    sortOption[i] = new MethodSortOption(args, true);
                    foundMethod = true;
                } else if (name.equals("Alphabetical")) {
                    sortOption[i] = new AlphabeticalSortOption(true);
                    foundAlphabetical = true;
                } else if (name.equals("FieldInitializers")) {
                    sortOption[i] = new FieldInitializersSortOption(true);
                    foundFieldInitializers = true;
                }
                ++i;
            }

            if (!foundType) {
                sortOption[i++] = new TypeSortOption(props.getString("sort.type","Field,Constructor,Method,NestedClass,NestedInterface,Initializer"), false);
            }
            if (!foundClass) {
                sortOption[i++] = new ClassSortOption(props.getString("sort.class","Instance"), false);
            }
            if (!foundFinal) {
                sortOption[i++] = new FinalSortOption(props.getString("sort.final","bottom"), false);
            }
            if (!foundProtection) {
                sortOption[i++] = new ProtectionSortOption(props.getString("sort.protection","public"), false);
            }
            if (!foundMethod) {
                sortOption[i++] = new MethodSortOption(props.getString("sort.method","Setter,Getter,Other"), false);
            }
            if (!foundAlphabetical) {
                sortOption[i++] = new AlphabeticalSortOption(false);
            }
            if (!foundFieldInitializers) {
                sortOption[i++] = new FieldInitializersSortOption(false);
            }
        }


        /**
         *  Gets the valueAt attribute of the SortTableModel object
         *
         *@param  row  Description of the Parameter
         *@param  col  Description of the Parameter
         *@return      The valueAt value
         */
        public Object getValueAt(int row, int col) {
            if (col == 0) {
                return new Boolean(sortOption[row].isSelected());
            } else {
                return "" + (row + 1) + ". " + sortOption[row].getDescription();
            }
        }


        /**
         *  Sets the valueAt attribute of the SortTableModel object
         *
         *@param  value  The new valueAt value
         *@param  row    The new valueAt value
         *@param  col    The new valueAt value
         */
        public void setValueAt(Object value, int row, int col) {
            if (col == 0 && value instanceof Boolean) {
                sortOption[row].setSelected(((Boolean) value).booleanValue());
            }
        }


        /**
         *  Gets the rowCount attribute of the SortTableModel object
         *
         *@return    The rowCount value
         */
        public int getRowCount() {
            return sortOption.length;
        }


        /**
         *  Gets the columnCount attribute of the SortTableModel object
         *
         *@return    The columnCount value
         */
        public int getColumnCount() {
            return 2;
        }


        /**
         *  Gets the cellEditable attribute of the SortTableModel object
         *
         *@param  row  Description of the Parameter
         *@param  col  Description of the Parameter
         *@return      The cellEditable value
         */
        public boolean isCellEditable(int row, int col) {
            return col == 0;
        }


        /**
         *  Gets the columnClass attribute of the SortTableModel object
         *
         *@param  col  Description of the Parameter
         *@return      The columnClass value
         */
        public Class getColumnClass(int col) {
            return col == 0 ? Boolean.class : String.class;
        }


        /**
         *  Gets the sortOption attribute of the SortTableModel object
         *
         *@param  row  Description of the Parameter
         *@return      The sortOption value
         */
        public SortOption getSortOption(int row) {
            return sortOption[row];
        }


        /**
         *  Description of the Method
         *
         *@param  entry1  Description of the Parameter
         *@param  entry2  Description of the Parameter
         */
        public void swap(int entry1, int entry2) {
            SortOption temp = sortOption[entry1];
            sortOption[entry1] = sortOption[entry2];
            sortOption[entry2] = temp;
            fireTableRowsUpdated(0, sortOption.length);
        }


        private SortOption[] sortOption;

    }



    /**
     *  An option consist of a set of option entries; each entry has a name and
     *  a description.
     *
     *@author     Mike
     *@created    03 September 2003
     */
    private static class OptionEntry {

        /**
         *  Constructor for the OptionEntry object
         *
         *@param  name     Description of the Parameter
         *@param  descKey  Description of the Parameter
         */
        public OptionEntry(String name, String descKey) {
            this.name = name;
            this.description = getIdeJavaStyleOption(descKey);
        }


        /**
         *  Description of the Field
         */
        public String name;
        /**
         *  Description of the Field
         */
        public String description;

    }



    /**
     *  Configuration component for <code>SortOption</code>s that have a certain
     *  sort order.
     *
     *@author     Mike
     *@created    03 September 2003
     */
    private abstract class OrderSortOption
             extends JPanel
             implements SortOption, ActionListener, ListSelectionListener {

        /**
         *  Constructor for the OrderSortOption object
         *
         *@param  argument    Description of the Parameter
         *@param  isSelected  Description of the Parameter
         *@param  entries     Description of the Parameter
         */
        public OrderSortOption(String argument, boolean isSelected, OptionEntry[] entries) {
            super(new BorderLayout());
            this.argument = argument;
            this.isSelected = isSelected;
            this.entries = entries;
            this.order = new int[entries.length];
            for (int i = 0; i < entries.length; ++i) {
                this.order[i] = i;
            }

            list = new JList();
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.addListSelectionListener(this);
            JScrollPane scrList = new JScrollPane(list);
            scrList.setPreferredSize(new Dimension(300, 150));
            bMoveUp2 = new JButton(getIdeJavaStyleOption("sorting.moveUp"));
            bMoveDown2 = new JButton(getIdeJavaStyleOption("sorting.moveDown"));
            bMoveUp2.addActionListener(this);
            bMoveDown2.addActionListener(this);
            JPanel buttons = new JPanel(new FlowLayout());
            buttons.add(bMoveUp2);
            buttons.add(bMoveDown2);
            this.add(new JLabel(getIdeJavaStyleOption("sorting.configPanel.order")), BorderLayout.NORTH);
            this.add(scrList, BorderLayout.CENTER);
            this.add(buttons, BorderLayout.SOUTH);
        }


        /**
         *  Gets the configureComponent attribute of the OrderSortOption object
         *
         *@return    The configureComponent value
         */
        public Component getConfigureComponent() {
            StringTokenizer tok = new StringTokenizer(argument, ", \t");
            int i = 0;

            while (tok.hasMoreTokens() && i < entries.length) {
                String next = tok.nextToken();
                int j = getEntryPos(next);
                if (j >= 0) {
                    order[i] = j;
                } else {
                    IDEPlugin.log(IDEInterface.ERROR, this, "entry " + next + " not found in entries array!");
                }
                ++i;
            }

            listModel = new OrderListModel();
            list.setModel(listModel);
            list.setSelectedIndex(0);
            return this;
        }


        /**
         *  Gets the selected attribute of the OrderSortOption object
         *
         *@return    The selected value
         */
        public boolean isSelected() {
            return isSelected;
        }


        /**
         *  Sets the selected attribute of the OrderSortOption object
         *
         *@param  isSelected  The new selected value
         */
        public void setSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }


        /**
         *  Description of the Method
         *
         *@param  evt  Description of the Parameter
         */
        public void actionPerformed(ActionEvent evt) {
            if (evt.getSource() == bMoveUp2) {
                int i = list.getSelectedIndex();
                listModel.swap(i, i - 1);
                updateArgument();
                list.setSelectedIndex(i - 1);
            } else if (evt.getSource() == bMoveDown2) {
                int i = list.getSelectedIndex();
                listModel.swap(i, i + 1);
                updateArgument();
                list.setSelectedIndex(i + 1);
            }
        }


        /**
         *  Description of the Method
         *
         *@param  evt  Description of the Parameter
         */
        public void valueChanged(ListSelectionEvent evt) {
            int i = list.getSelectedIndex();
            bMoveUp2.setEnabled(i > 0);
            bMoveDown2.setEnabled(i >= 0 && i < entries.length - 1);
        }


        /**
         *  Gets the entryPos attribute of the OrderSortOption object
         *
         *@param  name  Description of the Parameter
         *@return       The entryPos value
         */
        private int getEntryPos(String name) {
            for (int i = 0; i < entries.length; ++i) {
                if (name.equalsIgnoreCase(entries[i].name)) {
                    return i;
                }
            }
            return -1;
        }


        /**
         *  Description of the Method
         */
        private void updateArgument() {
            StringBuffer opt = new StringBuffer();
            for (int i = 0; i < entries.length; ++i) {
                if (i > 0) {
                    opt.append(',');
                }
                opt.append(entries[order[i]].name);
            }
            argument = opt.toString();
        }


        /**
         *  Description of the Class
         *
         *@author     Mike
         *@created    03 September 2003
         */
        private class OrderListModel extends AbstractListModel {
            /**
             *  Gets the elementAt attribute of the OrderListModel object
             *
             *@param  index  Description of the Parameter
             *@return        The elementAt value
             */
            public Object getElementAt(int index) {
                return "" + (index + 1) + ". " + entries[order[index]].description;
            }


            /**
             *  Gets the size attribute of the OrderListModel object
             *
             *@return    The size value
             */
            public int getSize() {
                return entries.length;
            }


            /**
             *  Description of the Method
             *
             *@param  entry1  Description of the Parameter
             *@param  entry2  Description of the Parameter
             */
            public void swap(int entry1, int entry2) {
                int temp = order[entry1];
                order[entry1] = order[entry2];
                order[entry2] = temp;
                fireContentsChanged(this, 0, entries.length);
            }
        }


        /**
         *  Description of the Field
         */
        protected String argument;
        private boolean isSelected;
        private OptionEntry[] entries;
        private int[] order;
        private OrderListModel listModel;
        private JList list;
        private JButton bMoveUp2;
        private JButton bMoveDown2;

    }



    /**
     *  Configuration component for <code>SortOption</code>s that have a certain
     *  value out of a set of possible values.
     *
     *@author     Mike
     *@created    03 September 2003
     */
    private abstract class ToggleSortOption
             extends Box
             implements SortOption, ActionListener {

        /**
         *  Constructor for the ToggleSortOption object
         *
         *@param  argument    Description of the Parameter
         *@param  isSelected  Description of the Parameter
         *@param  entries     Description of the Parameter
         */
        public ToggleSortOption(String argument, boolean isSelected, OptionEntry[] entries) {
            super(BoxLayout.Y_AXIS);
            this.argument = argument;
            this.isSelected = isSelected;
            this.entries = entries;

            ButtonGroup group = new ButtonGroup();
            optionButton = new JRadioButton[entries.length];
            for (int i = 0; i < entries.length; ++i) {
                optionButton[i] = new JRadioButton(entries[i].description);
                optionButton[i].addActionListener(this);
                group.add(optionButton[i]);
                this.add(optionButton[i]);
            }

            updateSelected();
            optionButton[selected].setSelected(true);
        }


        /**
         *  Gets the configureComponent attribute of the ToggleSortOption object
         *
         *@return    The configureComponent value
         */
        public Component getConfigureComponent() {
            updateSelected();
            optionButton[selected].setSelected(true);
            return this;
        }


        /**
         *  Gets the selected attribute of the ToggleSortOption object
         *
         *@return    The selected value
         */
        public boolean isSelected() {
            return isSelected;
        }


        /**
         *  Sets the selected attribute of the ToggleSortOption object
         *
         *@param  isSelected  The new selected value
         */
        public void setSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }


        /**
         *  Description of the Method
         *
         *@param  evt  Description of the Parameter
         */
        public void actionPerformed(ActionEvent evt) {
            for (int i = 0; i < optionButton.length; ++i) {
                if (optionButton[i].isSelected()) {
                    selected = i;
                    argument = entries[selected].name;
                    break;
                }
            }
        }


        /**
         *  Description of the Method
         */
        private void updateSelected() {
            StringTokenizer tok = new StringTokenizer(argument, ", \t");
            String first = tok.nextToken();
            for (int i = 0; i < entries.length; ++i) {
                if (first.equalsIgnoreCase(entries[i].name)) {
                    selected = i;
                    break;
                }
            }
        }


        /**
         *  Description of the Field
         */
        protected String argument;
        private boolean isSelected;
        private OptionEntry[] entries;
        private int selected;
        private JRadioButton[] optionButton;

    }



    /**
     *  Configuration component for <code>SortOption</code>s that do not have
     *  any additional configuration arguments, but can be selected and
     *  unselected.
     *
     *@author     Mike
     *@created    03 September 2003
     */
    private abstract class SimpleSortOption
             extends JPanel
             implements SortOption {

        /**
         *  Constructor for the SimpleSortOption object
         *
         *@param  isSelected  Description of the Parameter
         */
        public SimpleSortOption(boolean isSelected) {
            super();
            this.isSelected = isSelected;
        }


        /**
         *  Gets the configureComponent attribute of the SimpleSortOption object
         *
         *@return    The configureComponent value
         */
        public Component getConfigureComponent() {
            return this;
        }


        /**
         *  Gets the selected attribute of the SimpleSortOption object
         *
         *@return    The selected value
         */
        public boolean isSelected() {
            return isSelected;
        }


        /**
         *  Sets the selected attribute of the SimpleSortOption object
         *
         *@param  isSelected  The new selected value
         */
        public void setSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }


        /**
         *  Description of the Method
         */
        public void saveArgument(PropertiesFile props) { }


        private boolean isSelected;

    }



    /**
     *  A <code>SortOption</code> for option "Type(...)".
     *
     *@author     Mike
     *@created    03 September 2003
     */
    private class TypeSortOption extends OrderSortOption {

        /**
         *  Constructor for the TypeSortOption object
         *
         *@param  argument    Description of the Parameter
         *@param  isSelected  Description of the Parameter
         */
        public TypeSortOption(String argument, boolean isSelected) {
            super(argument, isSelected, new OptionEntry[]{
                    new OptionEntry("Enum", "sorting.type.enum"),
                    new OptionEntry("Field", "sorting.type.field"),
                    new OptionEntry("Constructor", "sorting.type.constructor"),
                    new OptionEntry("Method", "sorting.type.method"),
                    new OptionEntry("NestedClass", "sorting.type.nestedClass"),
                    new OptionEntry("NestedInterface", "sorting.type.nestedInterface"),
                    new OptionEntry("Initializer", "sorting.type.initializer"),
                    new OptionEntry("Main", "sorting.type.main")
                    });
        }


        /**
         *  Gets the fullProperty attribute of the TypeSortOption object
         *
         *@return    The fullProperty value
         */
        public String getFullProperty() {
            return "Type(" + argument + ")";
        }


        /**
         *  Gets the description attribute of the TypeSortOption object
         *
         *@return    The description value
         */
        public String getDescription() {
            return getIdeJavaStyleOption("sorting.description.type");
        }


        /**
         *  Description of the Method
         */
        public void saveArgument(PropertiesFile props) {
            props.setString("sort.type", argument);
        }

    }



    /**
     *  A <code>SortOption</code> for option "Method(...)".
     *
     *@author     Mike
     *@created    03 September 2003
     */
    private class MethodSortOption extends OrderSortOption {

        /**
         *  Constructor for the MethodSortOption object
         *
         *@param  argument    Description of the Parameter
         *@param  isSelected  Description of the Parameter
         */
        public MethodSortOption(String argument, boolean isSelected) {
            super(argument, isSelected, new OptionEntry[]{
                    new OptionEntry("Setter", "sorting.method.setter"),
                    new OptionEntry("Getter", "sorting.method.getter"),
                    new OptionEntry("Other", "sorting.method.other")
                    });
        }


        /**
         *  Gets the fullProperty attribute of the MethodSortOption object
         *
         *@return    The fullProperty value
         */
        public String getFullProperty() {
            return "Method(" + argument + ")";
        }


        /**
         *  Gets the description attribute of the MethodSortOption object
         *
         *@return    The description value
         */
        public String getDescription() {
            return getIdeJavaStyleOption("sorting.description.method");
        }


        /**
         *  Description of the Method
         */
        public void saveArgument(PropertiesFile props) {
            props.setString("sort.method", argument);
        }

    }



    /**
     *  A <code>SortOption</code> for option "Final(...)".
     *
     *@author     Mike
     *@created    03 September 2003
     */
    private class FinalSortOption extends ToggleSortOption {

        /**
         *  Constructor for the FinalSortOption object
         *
         *@param  argument    Description of the Parameter
         *@param  isSelected  Description of the Parameter
         */
        public FinalSortOption(String argument, boolean isSelected) {
            super(argument, isSelected, new OptionEntry[]{
                    new OptionEntry("top", "sorting.final.top"),
                    new OptionEntry("bottom", "sorting.final.bottom")
                    });
        }


        /**
         *  Gets the fullProperty attribute of the FinalSortOption object
         *
         *@return    The fullProperty value
         */
        public String getFullProperty() {
            return "Final(" + argument + ")";
        }


        /**
         *  Gets the description attribute of the FinalSortOption object
         *
         *@return    The description value
         */
        public String getDescription() {
            return getIdeJavaStyleOption("sorting.description.final");
        }


        /**
         *  Description of the Method
         */
        public void saveArgument(PropertiesFile props) {
            props.setString("sort.final", argument);
        }

    }



    /**
     *  A <code>SortOption</code> for option "Protection(...)".
     *
     *@author     Mike
     *@created    03 September 2003
     */
    private class ProtectionSortOption extends ToggleSortOption {

        /**
         *  Constructor for the ProtectionSortOption object
         *
         *@param  argument    Description of the Parameter
         *@param  isSelected  Description of the Parameter
         */
        public ProtectionSortOption(String argument, boolean isSelected) {
            super(argument, isSelected, new OptionEntry[]{
                    new OptionEntry("public", "sorting.protection.public"),
                    new OptionEntry("private", "sorting.protection.private")
                    });
        }


        /**
         *  Gets the fullProperty attribute of the ProtectionSortOption object
         *
         *@return    The fullProperty value
         */
        public String getFullProperty() {
            return "Protection(" + argument + ")";
        }


        /**
         *  Gets the description attribute of the ProtectionSortOption object
         *
         *@return    The description value
         */
        public String getDescription() {
            return getIdeJavaStyleOption("sorting.description.protection");
        }


        /**
         *  Description of the Method
         */
        public void saveArgument(PropertiesFile props) {
            props.setString("sort.protection", argument);
        }

    }



    /**
     *  A <code>SortOption</code> for option "Class(...)".
     *
     *@author     Mike
     *@created    03 September 2003
     */
    private class ClassSortOption extends ToggleSortOption {

        /**
         *  Constructor for the ClassSortOption object
         *
         *@param  argument    Description of the Parameter
         *@param  isSelected  Description of the Parameter
         */
        public ClassSortOption(String argument, boolean isSelected) {
            super(argument, isSelected, new OptionEntry[]{
                    new OptionEntry("Static", "sorting.class.static"),
                    new OptionEntry("Instance", "sorting.class.instance")
                    });
        }


        /**
         *  Gets the fullProperty attribute of the ClassSortOption object
         *
         *@return    The fullProperty value
         */
        public String getFullProperty() {
            return "Class(" + argument + ")";
        }


        /**
         *  Gets the description attribute of the ClassSortOption object
         *
         *@return    The description value
         */
        public String getDescription() {
            return getIdeJavaStyleOption("sorting.description.class");
        }


        /**
         *  Description of the Method
         */
        public void saveArgument(PropertiesFile props) {
            props.setString("sort.class", argument);
        }

    }



    /**
     *  A <code>SortOption</code> for option "Alphabetical()".
     *
     *@author     Mike
     *@created    03 September 2003
     */
    private class AlphabeticalSortOption extends SimpleSortOption {

        /**
         *  Constructor for the AlphabeticalSortOption object
         *
         *@param  isSelected  Description of the Parameter
         */
        public AlphabeticalSortOption(boolean isSelected) {
            super(isSelected);
        }


        /**
         *  Gets the fullProperty attribute of the AlphabeticalSortOption object
         *
         *@return    The fullProperty value
         */
        public String getFullProperty() {
            return "Alphabetical()";
        }


        /**
         *  Gets the description attribute of the AlphabeticalSortOption object
         *
         *@return    The description value
         */
        public String getDescription() {
            return getIdeJavaStyleOption("sorting.description.alphabetical");
        }

    }



    /**
     *  A <code>SortOption</code> for option "FieldInitializers()".
     *
     *@author     Mike
     *@created    03 September 2003
     */
    private class FieldInitializersSortOption extends SimpleSortOption {

        /**
         *  Constructor for the FieldInitializersSortOption object
         *
         *@param  isSelected  Description of the Parameter
         */
        public FieldInitializersSortOption(boolean isSelected) {
            super(isSelected);
        }


        /**
         *  Gets the fullProperty attribute of the FieldInitializersSortOption
         *  object
         *
         *@return    The fullProperty value
         */
        public String getFullProperty() {
            return "FieldInitializers()";
        }


        /**
         *  Gets the description attribute of the FieldInitializersSortOption
         *  object
         *
         *@return    The description value
         */
        public String getDescription() {
            return getIdeJavaStyleOption("sorting.description.fieldInitializers");
        }

    }

}

