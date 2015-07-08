package org.acm.seguin.pmd.swingui.event;

import org.acm.seguin.pmd.PMDException;

import java.util.EventListener;

/**
 *
 * @author Donald A. Leckie
 * @since December 13, 2002
 * @version $Revision: 1.1 $, $Date: 2003/07/29 20:51:59 $
 */
public interface PMDDirectoryRequestEventListener extends EventListener {

    /**
     *******************************************************************************
     *
     * @param event
     */
    void requestRuleSetPath(PMDDirectoryRequestEvent event);

    /**
     *******************************************************************************
     *
     * @param event
     */
    void requestAllRuleSets(PMDDirectoryRequestEvent event) throws PMDException;

    /**
     *******************************************************************************
     *
     * @param event
     */
    void requestDefaultRuleSets(PMDDirectoryRequestEvent event);

    /**
     *******************************************************************************
     *
     * @param event
     */
    void requestIncludedRules(PMDDirectoryRequestEvent event) throws PMDException;
}
