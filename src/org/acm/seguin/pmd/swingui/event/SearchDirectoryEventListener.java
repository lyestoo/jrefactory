package org.acm.seguin.pmd.swingui.event;

import java.util.EventListener;

/**
 *
 * @author Donald A. Leckie
 * @since January 6, 2003
 * @version $Revision: 1.1 $, $Date: 2003/07/29 20:51:59 $
 */
public interface SearchDirectoryEventListener extends EventListener {

    /**
     *
     * @param event
     */
    void setSearchDirectory(SearchDirectoryEvent event);
}