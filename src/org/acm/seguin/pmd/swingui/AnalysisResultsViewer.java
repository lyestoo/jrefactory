package org.acm.seguin.pmd.swingui;

import org.acm.seguin.pmd.PMDException;
import org.acm.seguin.pmd.RuleSet;
import org.acm.seguin.pmd.swingui.event.DirectoryTableEvent;
import org.acm.seguin.pmd.swingui.event.DirectoryTableEventListener;
import org.acm.seguin.pmd.swingui.event.ListenerList;
import org.acm.seguin.pmd.swingui.event.RulesInMemoryEvent;
import org.acm.seguin.pmd.swingui.event.RulesInMemoryEventListener;

import java.io.File;

/**
 *
 * @author Donald A. Leckie
 * @since August 27, 2002
 * @version $Revision: 1.1 $, $Date: 2003/07/29 20:51:59 $
 */
class AnalysisResultsViewer extends ResultsViewer {

    private RuleSet m_ruleSet;

    /**
     ********************************************************************************
     */
    protected AnalysisResultsViewer() {
        super();

        //
        // Add listeners
        //
        ListenerList.addListener((DirectoryTableEventListener) new DirectoryTableEventHandler());
        ListenerList.addListener((RulesInMemoryEventListener) new RulesInMemoryEventHandler());
    }

    /**
     ***********************************************************************************
     ***********************************************************************************
     ***********************************************************************************
     */
    private class DirectoryTableEventHandler implements DirectoryTableEventListener {

        /**
         ***************************************************************************
         *
         * @param event
         */
        public void requestSelectedFile(DirectoryTableEvent event) {
        }

        /**
         ***************************************************************************
         *
         * @param event
         */
        public void fileSelectionChanged(DirectoryTableEvent event) {
            try {
                File[] file = {event.getSelectedFile()};
                int priority = Preferences.getPreferences().getLowestPriorityForAnalysis();
                RulesInMemoryEvent.notifyRequestIncludedRules(this, priority);
                AnalysisResultsViewer.this.analyze(file, m_ruleSet);
            } catch (PMDException pmdException) {
                MessageDialog.show(PMDViewer.getViewer(), pmdException.getMessage(), pmdException.getReason());
            }
        }

        /**
         ***************************************************************************
         *
         * @param event
         */
        public void fileSelected(DirectoryTableEvent event) {
        }
    }

    /**
     ***************************************************************************
     ***************************************************************************
     ***************************************************************************
     */
    private class RulesInMemoryEventHandler implements RulesInMemoryEventListener {

        /**
         ***********************************************************************
         *
         * @param event
         */
        public void requestAllRules(RulesInMemoryEvent event) {
        }

        /**
         ***********************************************************************
         *
         * @param event
         */
        public void requestIncludedRules(RulesInMemoryEvent event) {
        }

        /**
         ***********************************************************************
         *
         * @param event
         */
        public void returnedRules(RulesInMemoryEvent event) {
            m_ruleSet = event.getRules();
        }
    }
}
