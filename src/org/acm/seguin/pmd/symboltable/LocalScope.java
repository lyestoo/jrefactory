package org.acm.seguin.pmd.symboltable;

import org.acm.seguin.pmd.util.Applier;

public class LocalScope extends AbstractScope {

    public void addDeclaration(VariableNameDeclaration nameDecl) {
        if (nameDecl.isExceptionBlockParameter()) {
            // this declaration needs to go somewhere... should this be delegated to the next
            // highest LocalScope?
            return;
        }
        super.addDeclaration(nameDecl);
    }

    protected NameDeclaration findVariableHere(NameOccurrence occurrence) {
        //System.err.println("LocalScope.findVariableHere("+occurrence+")");
        if (occurrence.isThisOrSuper()) {
            return null;
        }
        ImageFinderFunction finder = new ImageFinderFunction(occurrence.getImage());
        Applier.apply(finder, variableNames.keySet().iterator());
        //System.err.println("  "+this);
        return finder.getDecl();
    }

    public String toString() {
        return "LocalScope:" + super.glomNames();
    }
}
