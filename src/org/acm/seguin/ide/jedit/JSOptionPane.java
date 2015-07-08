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
package org.acm.seguin.ide.jedit;



import java.awt.Component;
import org.gjt.sp.jedit.AbstractOptionPane;
import org.acm.seguin.ide.common.options.*;

/**
 * @author    Mike Atkinson (<a href="mailto:javastyle@ladyshot.demon.co.uk">
 *      Mike@ladyshot.demon.co.uk</a> )
 * @created   04 September 2003
 * @version   $Version: $
 * @since     1.0
 */
public class JSOptionPane extends AbstractOptionPane {
    private static final String[] names = new String[] {
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
				"javastyle.pmd"};
	private JSHelpOptionPane options;
	public JSOptionPane(int type) {
		super(names[type]);
		switch (type) {
			case 0:
			   options = new JSGeneralOptionPane(null);
			   break;
			case 1:
			   options = new JSIndentOptionPane(null);
			   break;
			case 2:
			   options = new JSSpacingOptionPane(null);
			   break;
			case 3:
			   options = new JSAlignmentOptionPane(null);
			   break;
			case 4:
			   options = new JSSortOptionPane(null);
			   break;
			case 5:
			   options = new JSJavadocOptionPane(null);
			   break;
			case 6:
			   options = new JSStubsOptionPane(null);
			   break;
			case 7:
			   options = new JSStubsJUnitOptionPane(null);
			   break;
			case 8:
			   options = new JSTagsOptionPane(null);
			   break;
			case 9:
			   options = new JSCommentOptionPane(null);
			   break;
			case 10:
			   options = new PMDOptionPane(null);
			   break;
			default:
			   throw new RuntimeException("unknown option type");
		}
	}
	
	/**
	 * Returns the internal name of this option pane. The option pane's label
	 * is set to the value of the property named
	 * <code>options.<i>name</i>.label</code>.
	 */
	public String getName()
	{
		return options.getName();
	} //}}}

	//{{{ getComponent() method
	/**
	 * Returns the component that should be displayed for this option pane.
	 * Because this class extends Component, it simply returns "this".
	 */
	public Component getComponent()
	{
		return options.getComponent();
	} //}}}

	protected void _save() {
		options._save();
	}
	
	protected void _init() {
		options._init();
	}

}

