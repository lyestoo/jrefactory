/*
 * JSTagsOptionPane.java - options pane for JavaDoc tags
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


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import org.acm.seguin.ide.common.options.PropertiesFile;

/**
 *@author     Mike Atkinson (<a href="mailto:javastyle@ladyshot.demon.co.uk">
 *      Mike@ladyshot.demon.co.uk</a> )
 *@author     Dirk Moebius (<a href="mailto:dmoebius@gmx.net">dmoebius@gmx.net
 *      </a>)
 *@created    02 September 2003
 *@version    $Version: $
 *@since      1.0
 */
public class JSTagsOptionPane extends JSHelpOptionPane {

    private JTextField classTags;
    private JTextField enumTags;
    private JTextField methodTags;
    private JTextField fieldTags;
    private JTable table;
    private TagsTableModel model;

    private SelectedPanel spaceBeforeTag_sp;
    private SelectedPanel classTags_sp;
    private SelectedPanel enumTags_sp;
    private SelectedPanel methodTags_sp;
    private SelectedPanel fieldTags_sp;
    private SelectedPanel paramDescr_sp;
    private SelectedPanel returnDescr_sp;
    private SelectedPanel exceptionDescr_sp;



    /**
     *  Constructor for the JSTagsOptionPane object
     *
     *@param  project  Description of the Parameter
     */
    public JSTagsOptionPane(String project) {
        super("javastyle.tags", "pretty", project);
    }


    /**
     *  Description of the Method
     */
    public void _init() {
        FocusHandler fh = new FocusHandler();

        // "Additional space before @tags"
        spaceBeforeTag_sp = addComponent("space.before.javadoc", "tags.spaceBefore", new JCheckBox());

        // "Order or tags for..."
        addComponent(Box.createVerticalStrut(10));
        addComponent(new JLabel(getIdeJavaStyleOption("tags.orderFor")));

        // "Classes:"
        String class_tags = props.getString("class.tags");
        classTags = new JTextField();
        classTags.addFocusListener(fh);
        classTags_sp = addComponent("class.tags", "tags.classTags", classTags);

        // "Enums:"
        String enum_tags = props.getString("enum.tags");
        enumTags = new JTextField();
        enumTags.addFocusListener(fh);
        enumTags_sp = addComponent("enum.tags", "tags.enumTags", enumTags);

        // "Tag order for methods:"
        String method_tags = props.getString("method.tags");
        methodTags = new JTextField();
        methodTags.addFocusListener(fh);
        methodTags_sp = addComponent("method.tags", "tags.methodTags", methodTags);

        // "Tag order for fields:"
        String field_tags = props.getString("field.tags");
        fieldTags = new JTextField();
        fieldTags.addFocusListener(fh);
        fieldTags_sp = addComponent("field.tags", "tags.fieldTags", fieldTags);

        // "Default values for method tags:"
        addComponent(Box.createVerticalStrut(10));
        addComponent(new JLabel(getIdeJavaStyleOption("tags.methodTags.label")));

        paramDescr_sp = addMethodTagField("param");
        returnDescr_sp = addMethodTagField("return");
        exceptionDescr_sp = addMethodTagField("exception");

        // "Default values for other tags:"
        addComponent(Box.createVerticalStrut(10));
        addComponent(new JLabel(getIdeJavaStyleOption("tags.table.label")));

        // Tags table
        String all_tags = class_tags + "," + enum_tags + "," + method_tags + "," + field_tags;
        model = new TagsTableModel(props, all_tags, null);
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        addHelpFor(table, "tags.table");
        Dimension d = table.getPreferredSize();
        d.height = Math.min(d.height, 150);
        d.width = Math.min(d.width, 300);
        JScrollPane scrTable = new MyScrollPane(table);
        scrTable.setPreferredSize(d);
        GridBagConstraints cons = new GridBagConstraints();
        cons.gridy = y++;
        cons.gridheight = 1;
        cons.gridwidth = cons.REMAINDER;
        cons.fill = GridBagConstraints.BOTH;
        cons.weightx = 1.0f;
        cons.weighty = 1.0f;
        gridBag.setConstraints(scrTable, cons);
        addComponent(null, null, scrTable);

        addHelpArea();

        SwingUtilities.invokeLater(
            new Runnable() {
                public void run() {
                    table.sizeColumnsToFit(0);
                }
            });
    }

	private class MyScrollPane extends JScrollPane {
		public MyScrollPane(java.awt.Component c) {
			super(c);
		}
		private Dimension minSize = new Dimension(200, 100);
		private Dimension prefSize = new Dimension(300, 100);
		public Dimension getMinimumSize() {
			return minSize;
		}
		public Dimension getPreferredSize() {
			return prefSize;
		}
	}

    /**
     *  Description of the Method
     */
    public void _save() {
        spaceBeforeTag_sp.save();
        classTags_sp.save();
        enumTags_sp.save();
        methodTags_sp.save();
        fieldTags_sp.save();
        paramDescr_sp.save();
        returnDescr_sp.save();
        exceptionDescr_sp.save();

        SwingUtilities.invokeLater(
            new Runnable() {
                public void run() {
                    ((TagsTableModel) table.getModel()).save();
                }
            });
        //model.save();
    }


    /**
     *  Adds a feature to the MethodTagField attribute of the JSTagsOptionPane
     *  object
     *
     *@param  tagName  The feature to be added to the MethodTagField attribute
     *@return          Description of the Return Value
     */
    private SelectedPanel addMethodTagField(String tagName) {
        return addComponent(tagName + ".descr", "tags.stub." + tagName, new JTextField());
    }



    /**
     *  Description of the Class
     *
     *@author     Mike
     *@created    02 September 2003
     */
    private class FocusHandler extends FocusAdapter {

        /**
         *  Description of the Method
         *
         *@param  evt  Description of the Parameter
         */
        public void focusLost(FocusEvent evt) {
            String tagList = classTags.getText() + ","
                    + enumTags.getText() + ","
                    + methodTags.getText() + ","
                    + fieldTags.getText();
            model = new TagsTableModel(props, tagList, model);
            table.setModel(model);
            table.sizeColumnsToFit(0);
        }

    }



    /**
     *  Description of the Class
     *
     *@author     Mike
     *@created    02 September 2003
     */
    private static class TagsTableModel extends AbstractTableModel {

        /**
         *  Description of the Class
         *
         *@author     Mike
         *@created    02 September 2003
         */
        private static class Entry {
            String tag = null;
            String descr = "";
            boolean required = true;


            /**
             *  Constructor for the Entry object
             *
             *@param  t  Description of the Parameter
             *@param  d  Description of the Parameter
             *@param  r  Description of the Parameter
             */
            public Entry(String t, String d, boolean r) {
                tag = t;
                descr = d;
                required = r;
            }
        }


        Vector entries = new Vector();
        PropertiesFile props;


        /**
         *  Constructor for the TagsTableModel object
         *
         *@param  props     Description of the Parameter
         *@param  tagList   Description of the Parameter
         *@param  oldmodel  Description of the Parameter
         */
        public TagsTableModel(PropertiesFile props, String tagList, TagsTableModel oldmodel) {
            super();
            this.props = props;
            addTableModelListener(
                new TableModelListener() {
                    public void tableChanged(TableModelEvent e) {
                    }
                });
            StringTokenizer tok = new StringTokenizer(tagList, ", \t\r\n");
            while (tok.hasMoreTokens()) {
				// get next tag:
                String tag = tok.nextToken();

				// the tags "param", "return" and "exception" are not maintained by this table:
                if (tag.equals("param") || tag.equals("return") || tag.equals("exception")) {
                    continue;
                }

				// if already in table, quit:
                if (tag == null || tag.length() == 0 || find(tag) >= 0) {
                    continue;
                }

                // try to retrieve values from old (previous) model:
                String descr = null;
                boolean required = false;
                if (oldmodel != null) {
                    int i = oldmodel.find(tag);
                    if (i >= 0) {
                        descr = oldmodel.getValueAt(i, 1).toString();
                        required = ((Boolean) oldmodel.getValueAt(i, 2)).booleanValue();
                    }
                }
                // try to retrieve values from pretty.settings:
                if (descr == null) {
                    descr = props.getString(tag + ".descr");
                    required |= props.getBoolean("tagRequired." + tag, true);
                }
                // try to retrieve values from jEdit:
                if (descr == null) {
                    descr = props.getString("tagDescr." + tag);
                }
                if (descr == null) {
                    descr = "";
                }

                // add values to table:
                entries.addElement(new Entry(tag, descr, required));
            }
        }


        /**
         *  Gets the rowCount attribute of the TagsTableModel object
         *
         *@return    The rowCount value
         */
        public int getRowCount() {
            return entries.size();
        }


        /**
         *  Gets the columnCount attribute of the TagsTableModel object
         *
         *@return    The columnCount value
         */
        public int getColumnCount() {
            return 3;
        }


        /**
         *  Gets the valueAt attribute of the TagsTableModel object
         *
         *@param  row  Description of the Parameter
         *@param  col  Description of the Parameter
         *@return      The valueAt value
         */
        public Object getValueAt(int row, int col) {
            Entry e = (Entry) entries.elementAt(row);
            if (col == 0) {
                return e.tag;
            } else if (col == 1) {
                return e.descr;
            } else {
                return new Boolean(e.required);
            }
        }


        /**
         *  Sets the valueAt attribute of the TagsTableModel object
         *
         *@param  value  The new valueAt value
         *@param  row    The new valueAt value
         *@param  col    The new valueAt value
         */
        public void setValueAt(Object value, int row, int col) {
            Entry e = (Entry) entries.elementAt(row);
            if (col == 1) {
                e.descr = value.toString().trim();
            } else if (col == 2) {
                e.required = ((Boolean) value).booleanValue();
            }
        }


        /**
         *  Description of the Method
         */
        public void save() {
            for (Enumeration enum = entries.elements(); enum.hasMoreElements(); ) {
                Entry e = (Entry) enum.nextElement();
                if (e.required || e.descr.length() > 0) {
                    props.setString("tagDescr." + e.tag, e.descr);
                } else {
                    props.deleteKey("tagDescr." + e.tag);
                }
            }
        }


        /**
         *  Gets the cellEditable attribute of the TagsTableModel object
         *
         *@param  row  Description of the Parameter
         *@param  col  Description of the Parameter
         *@return      The cellEditable value
         */
        public boolean isCellEditable(int row, int col) {
            return col > 0;
        }


        /**
         *  Gets the columnName attribute of the TagsTableModel object
         *
         *@param  col  Description of the Parameter
         *@return      The columnName value
         */
        public String getColumnName(int col) {
            return getIdeJavaStyleOption("tags.table.col" + col);
        }


        /**
         *  Gets the columnClass attribute of the TagsTableModel object
         *
         *@param  col  Description of the Parameter
         *@return      The columnClass value
         */
        public Class getColumnClass(int col) {
            return (col == 2) ? Boolean.class : String.class;
        }


        /**
         *  Description of the Method
         *
         *@param  tag  Description of the Parameter
         *@return      Description of the Return Value
         */
        public int find(final String tag) {
            for (int i = 0; i < entries.size(); ++i) {
                Entry e = (Entry) entries.elementAt(i);
                if (tag.equals(e.tag)) {
                    return i;
                }
            }
            return -1; // not found
        }

    }

}

