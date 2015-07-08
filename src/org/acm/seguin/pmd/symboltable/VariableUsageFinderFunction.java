package org.acm.seguin.pmd.symboltable;

import org.acm.seguin.pmd.util.UnaryFunction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VariableUsageFinderFunction implements UnaryFunction {
    private Map results = new HashMap();
    private Map decls;
    private boolean lookingForUsed;

    public VariableUsageFinderFunction(Map decls, boolean lookingForUsed) {
        //System.err.println("VariableUsageFinderFunction("+decls+", "+lookingForUsed+")");
        this.decls = decls;
        this.lookingForUsed = lookingForUsed;
    }

    public void applyTo(Object o) {
        NameDeclaration decl = (NameDeclaration) o;
        //System.err.print("VariableUsageFinderFunction.applyTo("+decl+")");
        List usages = (List) decls.get(decl);
        if (!usages.isEmpty()) {
            //System.err.println("  usages is not empty");
            if (lookingForUsed) {
                //System.err.println(" add "+usages+"to usages for "+decl);
                results.put(decl, usages);
            }
        } else {
            if (!lookingForUsed) {
                //System.err.println(" add "+usages+"to usages for "+decl);
                results.put(decl, usages);
            }
        }
    }

    public Map getUsed() {
        return results;
    }
}
