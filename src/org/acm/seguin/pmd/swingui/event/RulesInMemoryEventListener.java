package org.acm.seguin.pmd.swingui.event;

import java.util.EventListener;

public interface RulesInMemoryEventListener extends EventListener {

    /**
     ****************************************************************************
     *
     * @param event
     */
    void requestAllRules(RulesInMemoryEvent event);

    /**
     ****************************************************************************
     *
     * @param event
     */
    void requestIncludedRules(RulesInMemoryEvent event);

    /**
     ****************************************************************************
     *
     * @param event
     */
    void returnedRules(RulesInMemoryEvent event);
}