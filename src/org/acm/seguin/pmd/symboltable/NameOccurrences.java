package org.acm.seguin.pmd.symboltable;

import org.acm.seguin.parser.ast.ASTName;
import org.acm.seguin.parser.ast.ASTPrimaryExpression;
import org.acm.seguin.parser.ast.ASTPrimaryPrefix;
import org.acm.seguin.parser.ast.ASTPrimarySuffix;
import org.acm.seguin.parser.ast.SimpleNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class NameOccurrences {

    private List names = new ArrayList();

    public NameOccurrences(ASTPrimaryExpression node) {
        buildOccurrences(node);
    }

    public List getNames() {
        return names;
    }

    public Iterator iterator() {
        return names.iterator();
    }

    private void buildOccurrences(ASTPrimaryExpression node) {
        ASTPrimaryPrefix prefix = (ASTPrimaryPrefix) node.jjtGetFirstChild();
        if (prefix.usesSuperModifier()) {
           //System.err.println("usesSuperModifier");
            add(new NameOccurrence(prefix, "super"));
        } else if (prefix.usesThisModifier()) {
           //System.err.println("usesThisModifier");
            add(new NameOccurrence(prefix, "this"));
        }
        checkForNameChild(prefix);

        for (int i = 1; i < node.jjtGetNumChildren(); i++) {
            checkForNameChild((ASTPrimarySuffix) node.jjtGetChild(i));
        }
    }

    private void checkForNameChild(SimpleNode node) {
       //System.err.println("NameOccurrences.checkForNameChild("+node+"),  node.getImage()="+node.getImage());
        // TODO when is this null?
        // MRA if (node.getImage() != null) {
        if (!node.getImage().equals("")) {// MRA
            add(new NameOccurrence(node, node.getImage()));
        }
        //System.err.println("node.jjtGetNumChildren()="+node.jjtGetNumChildren());
        if (node.jjtGetNumChildren() > 0 && node.jjtGetFirstChild() instanceof ASTName) {
            ASTName grandchild = (ASTName) node.jjtGetFirstChild();
            //System.err.println("  node.jjtGetFirstChild().getImage()="+grandchild.getImage());
            //System.err.println("  node.jjtGetFirstChild().getName()="+grandchild.getName());
            //FIXME: MRA the following looks wrong, 
            for (StringTokenizer st = new StringTokenizer(grandchild.getImage(), "."); st.hasMoreTokens();) {
                NameOccurrence no = new NameOccurrence(grandchild, st.nextToken());
                //System.err.println("   - found "+no);
                add(no);
            }
        }
        if (node instanceof ASTPrimarySuffix && ((ASTPrimarySuffix) node).isArguments()) {
            ((NameOccurrence) names.get(names.size() - 1)).setIsMethodOrConstructorInvocation();
        }
    }

    private void add(NameOccurrence name) {
       //System.err.println("NameOccurrences.add("+name+")");
        names.add(name);
        if (names.size() > 1) {
            NameOccurrence qualifiedName = (NameOccurrence) names.get(names.size() - 2);
            qualifiedName.setNameWhichThisQualifies(name);
        }
    }


    public String toString() {
        String result = "";
        for (Iterator i = names.iterator(); i.hasNext();) {
            NameOccurrence occ = (NameOccurrence) i.next();
            result += occ.getImage();
        }
        return result;
    }
}
