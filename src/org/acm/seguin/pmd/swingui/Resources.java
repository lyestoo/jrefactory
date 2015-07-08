package org.acm.seguin.pmd.swingui;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 *
 * @author Donald A. Leckie
 * @since September 9, 2002
 * @version $Revision: 1.1 $, $Date: 2003/07/29 20:51:59 $
 */
public class Resources {
    private static ResourceBundle RESOURCES = ResourceBundle.getBundle("org.acm.seguin.pmd.swingui.pmdViewer");

    /**
     *********************************************************************************
     *
     * @param name
     *
     * @return
     */
    public static final String getString(String name) {
        return RESOURCES.getString(name);
    }

    /**
     *********************************************************************************
     *
     * @param name
     * @param parameters
     *
     * @return
     */
    public static final String getString(String name, String[] parameters) {
        String template = RESOURCES.getString(name);
        String message = MessageFormat.format(template, parameters);

        return message;
    }
}