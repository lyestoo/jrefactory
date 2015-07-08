package org.acm.seguin.pmd.swingui;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;

/**
 *
 * @author Donald A. Leckie
 * @since September 8, 2002
 * @version $Revision: 1.1 $, $Date: 2003/07/29 20:51:59 $
 */
class RuleAllEditingPanel extends JPanel {
    private RuleSetEditingPanel m_ruleSetPanel;
    private RuleEditingPanel m_rulePanel;
    private RulePropertyEditingPanel m_rulePropertyPanel;
    private boolean m_isEditing;

    /**
     *******************************************************************************
     *
     * @return
     */
    protected RuleAllEditingPanel() {
        super(new BorderLayout());

        EmptyBorder emptyBorder = new EmptyBorder(15, 15, 15, 15);

        setBorder(emptyBorder);

        JScrollPane ruleScrollPane;

        m_ruleSetPanel = new RuleSetEditingPanel();
        m_rulePanel = new RuleEditingPanel();
        ruleScrollPane = ComponentFactory.createScrollPane(m_rulePanel);
        m_rulePropertyPanel = new RulePropertyEditingPanel();

        add(m_ruleSetPanel, BorderLayout.NORTH);
        add(ruleScrollPane, BorderLayout.CENTER);
        add(m_rulePropertyPanel, BorderLayout.SOUTH);
    }

    /**
     *******************************************************************************
     *
     * @param isEditing
     */
    protected void setIsEditing(boolean isEditing) {
        m_isEditing = isEditing;
        m_ruleSetPanel.setIsEditing(isEditing);
        m_rulePanel.setIsEditing(isEditing);
        m_rulePropertyPanel.setIsEditing(isEditing);
    }
}