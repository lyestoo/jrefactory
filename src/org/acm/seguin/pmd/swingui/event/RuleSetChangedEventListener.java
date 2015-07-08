package org.acm.seguin.pmd.swingui.event;

import java.util.EventListener;

/**
 *
 * @author Donald A. Leckie
 * @since December 13, 2002
 * @version $Revision: 1.1 $, $Date: 2003/07/29 20:51:59 $
 */
public interface RuleSetChangedEventListener extends EventListener {

    /**
     *********************************************************************************
     *
     * @param ruleSet
     */
    void ruleSetChanged(RuleSetChangedEvent event);

    /**
     *********************************************************************************
     *
     */
    void ruleSetsChanged(RuleSetChangedEvent event);
}