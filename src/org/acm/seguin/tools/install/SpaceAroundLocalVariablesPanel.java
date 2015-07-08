package org.acm.seguin.tools.install;
/**
 *  Whether to include spaces around a local variable
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public class SpaceAroundLocalVariablesPanel extends TogglePanel {
    public SpaceAroundLocalVariablesPanel() {
        super();
        addDescription("Do we force a blank line before and after local variable declarations?");
        addControl();
    }


    /**
     *  Gets the key attribute of the SpaceAroundLocalVariablesPanel object
     *
     *@return    The key value
     */
    public String getKey() {
        return "insert.space.around.local.variables";
    }


    /**
     *  Gets the initialValue attribute of the SpaceAroundLocalVariablesPanel
     *  object
     *
     *@return    The initialValue value
     */
    protected String getInitialValue() {
        return "false";
    }
}
