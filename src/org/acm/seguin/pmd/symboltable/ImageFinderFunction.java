package org.acm.seguin.pmd.symboltable;

import org.acm.seguin.pmd.util.UnaryFunction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ImageFinderFunction implements UnaryFunction {

    private Set images = new HashSet();
    private NameDeclaration decl;

    public ImageFinderFunction(String img) {
        images.add(img);
    }

    public ImageFinderFunction(List imageList) {
        images.addAll(imageList);
    }

    public void applyTo(Object o) {
        NameDeclaration nameDeclaration = (NameDeclaration) o;
        if (images.contains(nameDeclaration.getImage())) {
            decl = nameDeclaration;
        }
    }

    public NameDeclaration getDecl() {
        return this.decl;
    }
}
