package org.acm.seguin.ide.jedit;

import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.io.VFSManager;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.textarea.Selection;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.BorderLayout;
import java.util.List;
import java.util.ArrayList;

/**
 * A GUI Component to display Duplicate code.
 *
 * @author   Jiger Patel
 * @created  05 Apr 2003
 */

public class CodingStandardsViewer extends JPanel {
    final View view;

    /**
     * Constructor for the CPDDuplicateCodeViewer object
     *
     */
    public CodingStandardsViewer(View aView) {
        this.view = aView;
        setLayout(new BorderLayout());
        JButton checkBuff = new JButton("Check Current Buffer");
        checkBuff.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                org.acm.seguin.ide.jedit.JavaStyleActions.check(view.getBuffer(), view);
            }
        });
        JButton checkAllBuff = new JButton("Check All Open Buffers");
        checkAllBuff.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                org.acm.seguin.ide.jedit.JavaStyleActions.checkAllOpenBuffers(view);
            }
        });
        JButton checkCurDir = new JButton("Check Current Directory");
        checkCurDir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                org.acm.seguin.ide.jedit.JavaStyleActions.checkDirectory(view);
            }
        });
        JButton checkDirRecurse = new JButton("Check Directory Recursively");
        checkDirRecurse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                org.acm.seguin.ide.jedit.JavaStyleActions.checkDirectoryRecursively(view);
            }
        });
        JPanel top = new JPanel();
        top.add(checkBuff);
        top.add(checkAllBuff);
        top.add(checkCurDir);
        top.add(checkDirRecurse);
        add(top, BorderLayout.NORTH);
        
    }

}

