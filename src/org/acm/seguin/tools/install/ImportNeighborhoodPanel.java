package org.acm.seguin.tools.install;
/**
 *  Specifies the neighborhood
 *
 *@author     Chris Seguin
 *@created    September 12, 2001
 */
public class ImportNeighborhoodPanel extends OptionPanel {
    /**
     *  Constructor for the ImportNeighborhoodPanel object
     */
    public ImportNeighborhoodPanel() {
        super();
        addDescription("If you want classes that are written by you to move to the end");
        addDescription("of the list of imports change this value.");
        addOption("0", "Keep all the imports in alphabetical order");
        addOption("1", "If the package and the import start with the same value - com or org - put them at the end");
        addOption("2", "When package and import share 2 directory levels, the imports are listed last");
        addOption("3", "When package and import share 3 directory levels, the imports are listed last");
        addControl();
    }


    /**
     *  Gets the key attribute of the ImportNeighborhoodPanel object
     *
     *@return    The key value
     */
    public String getKey() {
        return "import.sort.neighbourhood";
    }


    /**
     *  Gets the initialValue attribute of the ClassMinimumPanel object
     *
     *@return    The initialValue value
     */
    protected String getInitialValue() {
        return "0";
    }
}
