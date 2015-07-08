/*
 * JSHelpOptionPane.java - options panel with some helper functions
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


import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;
import org.acm.seguin.ide.common.options.PropertiesFile;
import org.acm.seguin.ide.common.IDEPlugin;

/**
 *@author     Mike Atkinson (<a href="mailto:javastyle@ladyshot.demon.co.uk">
 *      Mike@ladyshot.demon.co.uk</a> )
 *@author     Dirk Moebius (<a href="mailto:dmoebius@gmx.net">dmoebius@gmx.net
 *      </a>)
 *@created    03 September 2003
 *@version    $Version: $
 *@since      1.0
 */
public abstract class JSHelpOptionPane extends JPanel {

    public static final String[] NAMES = new String[] {
	    "javastyle.general",
	    "javastyle.indenting",
	    "javastyle.spacing",
	    "javastyle.alignment",
	    "javastyle.sorting",
	    "javastyle.javadoc",
	    "javastyle.stubs",
	    "javastyle.stubs_junit",
	    "javastyle.tags",
	    "javastyle.comments",
	    "javastyle.pmd"
	};

    private final static Font helpFont = new Font("DialogInput", Font.PLAIN, 11);
    private final static Color helpBackground = UIManager.getColor("Label.background");
    private final static Color helpForeground = UIManager.getColor("Label.foreground");

    private JTextArea helpArea = null;
    private Hashtable helpTexts = new Hashtable();
    private MouseHandler mh = new MouseHandler();
    private String project;
    /**
     *  Description of the Field
     */
    protected PropertiesFile props;


    /**
     *  Gets the IDE Property attribute of the JSHelpOptionPane class
     *
     *@param  prop  Description of the Parameter
     *@return       The IDE Property value
     */
    public static String getIdeProperty(String prop) {
        return IDEPlugin.getProperty(prop);
    }


    /**
     *  Gets the IDE Property attribute of the JSHelpOptionPane class
     *
     *@param  prop  Description of the Parameter
     *@return       The IDE Property value
     */
    public static String getIdeJavaStyleOption(String prop) {
        return IDEPlugin.getProperty("options.javastyle." + prop);
    }


    /**
     *  Constructor for the JSHelpOptionPane object
     *
     *@param  key      Description of the Parameter
     *@param  project  Description of the Parameter
     */
    public JSHelpOptionPane(String key, String type, String project) {
		name = key;
        this.project = (project==null) ? "default" : project;
		setLayout(gridBag = new GridBagLayout());
        props = IDEPlugin.getProperties(type, this.project);
    }


    /**
     *  Adds a feature to the HelpFor attribute of the JSHelpOptionPane object
     *
     *@param  comp          The feature to be added to the HelpFor attribute
     *@param  compProperty  The feature to be added to the HelpFor attribute
     */
    protected void addHelpFor(Component comp, String compProperty) {
		try {
			comp.addMouseListener(mh);
			helpTexts.put(comp, getIdeJavaStyleOption(compProperty + ".tooltip"));
		} catch (Exception e) {
			System.out.println("can't find \""+compProperty + ".tooltip\"");
		}
    }


    /**
     *  Adds a feature to the HelpArea attribute of the JSHelpOptionPane object
     */
    protected void addHelpArea() {
        // "----Help----"
        addSeparator("helpArea");
        // help area
        helpArea = new JTextArea();
        helpArea.setEditable(false);
        helpArea.setLineWrap(true);
        helpArea.setWrapStyleWord(true);
        helpArea.setFont(helpFont);
        helpArea.setBackground(helpBackground);
        helpArea.setForeground(helpForeground);
        helpArea.setText("");
        helpArea.setRows(5);
        JScrollPane scrHelpArea = new JScrollPane(helpArea);
        GridBagConstraints cons = new GridBagConstraints();
        cons.gridy = y++;
        cons.gridheight = 1;
        cons.gridwidth = cons.REMAINDER;
        cons.fill = GridBagConstraints.BOTH;
        cons.weightx = 1.0f;
        cons.weighty = 1.0f;
        gridBag.setConstraints(scrHelpArea, cons);
        add(scrHelpArea);
    }


    /**
     *  Adds a feature to the Component attribute of the JSHelpOptionPane object
     *
     *@param  option     The feature to be added to the Component attribute
     *@param  label      The feature to be added to the Component attribute
     *@param  component  The feature to be added to the Component attribute
     *@return            Description of the Return Value
     */
    protected SelectedPanel addComponent(String option, String label, javax.swing.JComponent component) {
        return new SelectedPanel(this, props, project, option, null, label, component);
    }


    /**
     *  Adds a feature to the Component attribute of the JSHelpOptionPane object
     *
     *@param  option         The feature to be added to the Component attribute
     *@param  defaultOption  The feature to be added to the Component attribute
     *@param  label          The feature to be added to the Component attribute
     *@param  component      The feature to be added to the Component attribute
     *@return                Description of the Return Value
     */
    protected SelectedPanel addComponent(String option, String defaultOption, String label, javax.swing.JComponent component) {
        return new SelectedPanel(this, props, project, option, defaultOption, label, component);
    }


	public void setHelpText(String text) {
		helpArea.setText(text);
		helpArea.setCaretPosition(0);
	}

    /**
     *  Description of the Class
     *
     *@author     Mike
     *@created    03 September 2003
     */
    private class MouseHandler extends MouseAdapter {
        /**
         *  Description of the Method
         *
         *@param  evt  Description of the Parameter
         */
        public void mouseEntered(MouseEvent evt) {
            setHelpText((String) helpTexts.get(evt.getSource()));
        }
    }

	//{{{ getName() method
	/**
	 * Returns the internal name of this option pane. The option pane's label
	 * is set to the value of the property named
	 * <code>options.<i>name</i>.label</code>.
	 */
	public String getName()
	{
		return name;
	} //}}}

	//{{{ getComponent() method
	/**
	 * Returns the component that should be displayed for this option pane.
	 * Because this class extends Component, it simply returns "this".
	 */
	public Component getComponent()
	{
		return this;
	} //}}}

	//{{{ init() method
	/**
	 * Do not override this method, override {@link #_init()} instead.
	 */
	// final in 4.2
	public void init()
	{
		if(!initialized)
		{
			initialized = true;
			_init();
		}
	} //}}}

	//{{{ save() method
	/**
	 * Do not override this method, override {@link #_save()} instead.
	 */
	// final in 4.2
	public void save()
	{
		if(initialized)
			_save();
	} //}}}

	//{{{ addComponent() method
	/**
	 * Adds a labeled component to the option pane. Components are
	 * added in a vertical fashion, one per row. The label is
	 * displayed to the left of the component.
	 * @param label The label
	 * @param comp The component
	 */
	public void addComponent(String label, Component comp)
	{
		JLabel l = new JLabel(label);
		l.setBorder(new EmptyBorder(0,0,0,12));
		addComponent(l,comp,GridBagConstraints.BOTH);
	} //}}}

	//{{{ addComponent() method
	/**
	 * Adds a labeled component to the option pane. Components are
	 * added in a vertical fashion, one per row. The label is
	 * displayed to the left of the component.
	 * @param label The label
	 * @param comp The component
	 * @param fill Fill parameter to GridBagConstraints for the right
	 * component
	 */
	public void addComponent(String label, Component comp, int fill)
	{
		JLabel l = new JLabel(label);
		l.setBorder(new EmptyBorder(0,0,0,12));
		addComponent(l,comp,fill);
	} //}}}

	//{{{ addComponent() method
	/**
	 * Adds a labeled component to the option pane. Components are
	 * added in a vertical fashion, one per row. The label is
	 * displayed to the left of the component.
	 * @param comp1 The label
	 * @param comp2 The component
	 *
	 * @since JRefactory 2.8.02
	 */
	public void addComponent(Component comp1, Component comp2)
	{
		addComponent(comp1,comp2,GridBagConstraints.BOTH);
	} //}}}

	//{{{ addComponent() method
	/**
	 * Adds a labeled component to the option pane. Components are
	 * added in a vertical fashion, one per row. The label is
	 * displayed to the left of the component.
	 * @param comp1 The label
	 * @param comp2 The component
	 * @param fill Fill parameter to GridBagConstraints for the right
	 * component
	 *
	 * @since JRefactory 2.8.02
	 */
	public void addComponent(Component comp1, Component comp2, int fill)
	{
		GridBagConstraints cons = new GridBagConstraints();
		cons.gridy = y++;
		cons.gridheight = 1;
		cons.gridwidth = 1;
		cons.weightx = 0.0f;
		cons.insets = new Insets(1,0,1,0);
		cons.fill = GridBagConstraints.BOTH;

		gridBag.setConstraints(comp1,cons);
		add(comp1);

		cons.fill = fill;
		cons.gridx = 1;
		cons.weightx = 1.0f;
		gridBag.setConstraints(comp2,cons);
		add(comp2);
	} //}}}

	//{{{ addComponent() method
	/**
	 * Adds a component to the option pane. Components are
	 * added in a vertical fashion, one per row.
	 * @param comp The component
	 */
	public void addComponent(Component comp)
	{
		GridBagConstraints cons = new GridBagConstraints();
		cons.gridy = y++;
		cons.gridheight = 1;
		cons.gridwidth = cons.REMAINDER;
		cons.fill = GridBagConstraints.NONE;
		cons.anchor = GridBagConstraints.WEST;
		cons.weightx = 1.0f;
		cons.insets = new Insets(1,0,1,0);

		gridBag.setConstraints(comp,cons);
		add(comp);
	} //}}}

	//{{{ addComponent() method
	/**
	 * Adds a component to the option pane. Components are
	 * added in a vertical fashion, one per row.
	 * @param comp The component
	 * @param fill Fill parameter to GridBagConstraints
	 * @since JRefactory 2.8.02
	 */
	public void addComponent(Component comp, int fill)
	{
		GridBagConstraints cons = new GridBagConstraints();
		cons.gridy = y++;
		cons.gridheight = 1;
		cons.gridwidth = cons.REMAINDER;
		cons.fill = fill;
		cons.anchor = GridBagConstraints.WEST;
		cons.weightx = 1.0f;
		cons.insets = new Insets(1,0,1,0);

		gridBag.setConstraints(comp,cons);
		add(comp);
	} //}}}

	//{{{ addSeparator() method
	/**
	 * Adds a separator component.
	 * @param label The separator label property
	 * @since JRefactory 2.8.02
	 */
	public void addSeparator()
	{
		addComponent(Box.createVerticalStrut(6));

		JSeparator sep = new JSeparator(JSeparator.HORIZONTAL);

		GridBagConstraints cons = new GridBagConstraints();
		cons.gridy = y++;
		cons.gridheight = 1;
		cons.gridwidth = cons.REMAINDER;
		cons.fill = GridBagConstraints.BOTH;
		cons.anchor = GridBagConstraints.WEST;
		cons.weightx = 1.0f;
		//cons.insets = new Insets(1,0,1,0);

		gridBag.setConstraints(sep,cons);
		add(sep);

		addComponent(Box.createVerticalStrut(6));
	} //}}}

	//{{{ addSeparator() method
	/**
	 * Adds a separator component.
	 * @param label The separator label property
	 * @since JRefactory 2.8.02
	 */
	public void addSeparator(String label) {
		if(y != 0) {
			addComponent(Box.createVerticalStrut(6));
		}

		Box box = new Box(BoxLayout.X_AXIS);
		Box box2 = new Box(BoxLayout.Y_AXIS);
		box2.add(Box.createGlue());
		box2.add(new JSeparator(JSeparator.HORIZONTAL));
		box2.add(Box.createGlue());
		box.add(box2);
		JLabel l = new JLabel(getIdeJavaStyleOption(label));
		l.setMaximumSize(l.getPreferredSize());
		box.add(l);
		Box box3 = new Box(BoxLayout.Y_AXIS);
		box3.add(Box.createGlue());
		box3.add(new JSeparator(JSeparator.HORIZONTAL));
		box3.add(Box.createGlue());
		box.add(box3);

		GridBagConstraints cons = new GridBagConstraints();
		cons.gridy = y++;
		cons.gridheight = 1;
		cons.gridwidth = cons.REMAINDER;
		cons.fill = GridBagConstraints.BOTH;
		cons.anchor = GridBagConstraints.WEST;
		cons.weightx = 1.0f;
		cons.insets = new Insets(1,0,1,0);

		gridBag.setConstraints(box,cons);
		add(box);
	} //}}}

	//{{{ Protected members
	/**
	 * Has the option pane been initialized?
	 */
	protected boolean initialized;

	/**
	 * The layout manager.
	 */
	protected GridBagLayout gridBag;

	/**
	 * The number of components already added to the layout manager.
	 */
	protected int y;

	/**
	 * This method should create and arrange the components of the option pane
	 * and initialize the option data displayed to the user. This method
	 * is called when the option pane is first displayed, and is not
	 * called again for the lifetime of the object.
	 */
	public void _init() {}

	/**
	 * Called when the options dialog's "ok" button is clicked.
	 * This should save any properties being edited in this option
	 * pane.
	 */
	public void _save() {}
	//}}}

	//{{{ Private members
	private String name;
	//}}}
}

