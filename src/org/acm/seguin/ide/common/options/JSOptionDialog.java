/*
 *  JSOptionDialog.java - options dialog for JavaStyle option panes
 *  Copyright (C) 2001 Dirk Moebius
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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JDialog;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;


import org.acm.seguin.ide.common.IDEPlugin;

/**
 *  An option dialog for JavaStyle options.
 *
 *@author     Mike Atkinson (<a href="mailto:javastyle@ladyshot.demon.co.uk">
 *      Mike@ladyshot.demon.co.uk</a> )
 *@author     Dirk Moebius (<a href="mailto:dmoebius@gmx.net">dmoebius@gmx.net
 *      </a>)
 *@created    04 September 2003
 *@version    $Version: $
 *@since      1.0
 */
public class JSOptionDialog extends JDialog implements ActionListener {
    private JButton apply;
    private JButton cancel;

    private JButton ok;
    private JSHelpOptionPane[][] optionPanes;
    private String[] projects = new String[]{"default"};


	//{{{ Private members
	
	private void _init() {
		((Container)getLayeredPane()).addContainerListener(
			new ContainerHandler());
		getContentPane().addContainerListener(new ContainerHandler());

		keyHandler = new KeyHandler();
		addKeyListener(keyHandler);
		addWindowListener(new WindowHandler());

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}

	//}}}
	
	// protected members
	protected KeyHandler keyHandler;

	// Recursively adds our key listener to sub-components
	class ContainerHandler extends ContainerAdapter
	{
		public void componentAdded(ContainerEvent evt)
		{
			componentAdded(evt.getChild());
		}

		public void componentRemoved(ContainerEvent evt)
		{
			componentRemoved(evt.getChild());
		}

		private void componentAdded(Component comp)
		{
			comp.addKeyListener(keyHandler);
			if(comp instanceof Container)
			{
				Container cont = (Container)comp;
				cont.addContainerListener(this);
				Component[] comps = cont.getComponents();
				for(int i = 0; i < comps.length; i++)
				{
					componentAdded(comps[i]);
				}
			}
		}

		private void componentRemoved(Component comp)
		{
			comp.removeKeyListener(keyHandler);
			if(comp instanceof Container)
			{
				Container cont = (Container)comp;
				cont.removeContainerListener(this);
				Component[] comps = cont.getComponents();
				for(int i = 0; i < comps.length; i++)
				{
					componentRemoved(comps[i]);
				}
			}
		}
	}

	class KeyHandler extends KeyAdapter
	{
		public void keyPressed(KeyEvent evt)
		{
			if(evt.isConsumed())
				return;

			if(evt.getKeyCode() == KeyEvent.VK_ENTER)
			{
				// crusty workaround
				Component comp = getFocusOwner();
				while(comp != null)
				{
					if(comp instanceof JComboBox)
					{
						JComboBox combo = (JComboBox)comp;
						if(combo.isEditable())
						{
							Object selected = combo.getEditor().getItem();
							if(selected != null)
								combo.setSelectedItem(selected);
						}
						break;
					}

					comp = comp.getParent();
				}

				ok();
				evt.consume();
			}
			else if(evt.getKeyCode() == KeyEvent.VK_ESCAPE)
			{
				cancel();
				evt.consume();
			}
		}
	}

	class WindowHandler extends WindowAdapter
	{
		public void windowClosing(WindowEvent evt)
		{
			cancel();
		}
	}

    /**
     *  Constructor for the JSOptionDialog object
     *
     *@param  view  Description of Parameter
     */
    public JSOptionDialog(Frame parent) {
        super(parent, IDEPlugin.getProperty("options.javastyle.label"), true);
		_init();
        projects = IDEPlugin.getProjects(parent);

        IDEPlugin.showWaitCursor(parent);

        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(new EmptyBorder(5, 8, 8, 8));
        content.setLayout(new BorderLayout());
        setContentPane(content);

        optionPanes = new JSHelpOptionPane[projects.length][];

        JTabbedPane mainstage = new JTabbedPane( ((projects.length <= 1) ? JTabbedPane.LEFT : JTabbedPane.TOP) );
        for (int proj = 0; proj < projects.length; proj++) {
            optionPanes[proj] = new JSHelpOptionPane[]{
                    new JSGeneralOptionPane(projects[proj]),
                    new JSIndentOptionPane(projects[proj]),
                    new JSSpacingOptionPane(projects[proj]),
                    new JSAlignmentOptionPane(projects[proj]),
                    new JSSortOptionPane(projects[proj]),
                    new JSJavadocOptionPane(projects[proj]),
                    new JSStubsOptionPane(projects[proj]),
                    new JSStubsJUnitOptionPane(projects[proj]),
                    new JSTagsOptionPane(projects[proj]),
                    new JSCommentOptionPane(projects[proj]),
					new PMDOptionPane(projects[proj]),
                    };

            JTabbedPane stage = new JTabbedPane(JTabbedPane.LEFT);
            for (int i = 0; i < optionPanes[proj].length; ++i) {
                optionPanes[proj][i].setBorder(new EmptyBorder(12, 12, 12, 12));
                optionPanes[proj][i].init();
                if (projects.length <= 1) {
                    mainstage.addTab(
                            IDEPlugin.getProperty("options." + optionPanes[proj][i].getName() + ".label"),
                            optionPanes[proj][i]
                            );
                } else {
                    stage.addTab(
                            IDEPlugin.getProperty("options." + optionPanes[proj][i].getName() + ".label"),
                            optionPanes[proj][i]
                            );
                }
            }
            if (projects.length > 1) {
                mainstage.addTab(projects[proj], stage);
            }
        }
        content.add(mainstage, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.setBorder(new EmptyBorder(12, 0, 0, 0));
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.add(Box.createGlue());

        ok = new JButton(IDEPlugin.getProperty("common.ok"));
        ok.addActionListener(this);
        buttons.add(ok);
        buttons.add(Box.createHorizontalStrut(6));
        getRootPane().setDefaultButton(ok);

        cancel = new JButton(IDEPlugin.getProperty("common.cancel"));
        cancel.addActionListener(this);
        buttons.add(cancel);
        buttons.add(Box.createHorizontalStrut(6));

        apply = new JButton(IDEPlugin.getProperty("common.apply"));
        apply.addActionListener(this);
        buttons.add(apply);

        buttons.add(Box.createGlue());

        content.add(buttons, BorderLayout.SOUTH);

        IDEPlugin.hideWaitCursor(parent);
        pack();
        setLocationRelativeTo(parent);
        show();
    }



    /**
     *  Description of the Method
     *
     *@param  evt  Description of Parameter
     */
    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();

        if (source == ok) {
            ok();
        } else if (source == cancel) {
            cancel();
        } else if (source == apply) {
            ok(false);
        }
    }


    /**
     *  Description of the Method
     */
    public void cancel() {
        dispose();
    }



    /**
     *  Description of the Method
     */
    public void ok() {
        ok(true);
    }


    /**
     *  Description of the Method
     *
     *@param  dispose  Description of Parameter
     */
    public void ok(final boolean dispose) {
        for (int proj = 0; proj < optionPanes.length; ++proj) {
            for (int i = 0; i < optionPanes[proj].length; ++i) {
                optionPanes[proj][i].save();
            }
        }

        IDEPlugin.saveProperties();

        // get rid of this dialog if necessary
        if (dispose) {
            dispose();
        }
    }

}

