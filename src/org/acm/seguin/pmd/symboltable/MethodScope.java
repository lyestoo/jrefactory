package org.acm.seguin.pmd.symboltable;

import org.acm.seguin.pmd.util.Applier;

public class MethodScope extends AbstractScope {

    protected NameDeclaration findVariableHere(NameOccurrence occurrence) {
        //System.err.println("MethodScope.findVariableHere("+occurrence+")");
        if (occurrence.isThisOrSuper()) {
            return null;
        }
        ImageFinderFunction finder = new ImageFinderFunction(occurrence.getImage());
        Applier.apply(finder, variableNames.keySet().iterator());
        //System.err.println("  "+this);
        return finder.getDecl();
    }

    public String toString() {
        return "MethodScope:" + super.glomNames();
    }
}
