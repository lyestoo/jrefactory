package org.acm.seguin.pmd.rules;

import org.acm.seguin.pmd.AbstractRule;
import org.acm.seguin.pmd.RuleContext;
import org.acm.seguin.parser.ast.ASTArgumentList;
import org.acm.seguin.parser.ast.ASTCompilationUnit;
import org.acm.seguin.parser.ast.ASTLiteral;
import org.acm.seguin.parser.Node;
import org.acm.seguin.parser.ast.SimpleNode;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AvoidDuplicateLiteralsRule extends AbstractRule {

    private Map literals = new HashMap();

    public Object visit(ASTCompilationUnit node, Object data) {
        //System.err.println("AvoidDuplicateLiteralsRule: "+node.dumpString("\r\n"));
        literals.clear();
        super.visit(node, data);
        int threshold = getIntProperty("threshold");
        int size = getIntProperty("size") + 2;  // remember a string has " at beginning and end! 
        int sizeThreshold = getIntProperty("sizeThreshold");
        //System.err.println("size="+size+", threshold="+threshold+", sizeThreshold="+sizeThreshold);
        for (Iterator i = literals.keySet().iterator(); i.hasNext();) {
            String key = (String) i.next();
            List occurrences = (List) literals.get(key);
            //System.err.println("key="+key+", key.length()="+key.length()+", occurrences="+occurrences.size());
            //System.err.println("(key.length()>size&&occurrences.size() >= threshold)="+(key.length()>size&&occurrences.size() >= threshold));
            //System.err.println("(key.length()<=size&&occurrences.size() >= sizeThreshold)="+(key.length()<=size&&occurrences.size() >= sizeThreshold));
            if ( (key.length()>size&&occurrences.size() >= threshold) || (key.length()<=size&&occurrences.size() >= sizeThreshold)) {
               //System.err.println("  adding error");
                Object[] args = new Object[]{key, new Integer(occurrences.size()), new Integer(((SimpleNode) occurrences.get(0)).getBeginLine())};
                String msg = MessageFormat.format(getMessage(), args);
                RuleContext ctx = (RuleContext) data;
                ctx.getReport().addRuleViolation(createRuleViolation(ctx, ((SimpleNode) occurrences.get(0)).getBeginLine(), msg));
            }
        }
        return data;
    }

    public Object visit(ASTLiteral node, Object data) {
        if (!hasAtLeastSixParents(node)) {
            return data;
        }

        if (!nthParentIsASTArgumentList(node, 21)) {
            //System.err.println("AvoidDuplicateLiteralsRule: is not part of ASTArgumentList");
            return data;
        }

            //System.err.println("node.getImage()="+node.getImage());
        // just catching strings for now
        if (node.getImage() == null || node.getImage().indexOf('\"') == -1) {
            return data;
        }

        if (literals.containsKey(node.getImage())) {
            List occurrences = (List) literals.get(node.getImage());
            occurrences.add(node);
        } else {
            List occurrences = new ArrayList();
            occurrences.add(node);
            literals.put(node.getImage(), occurrences);
        }

        return data;
    }

    private boolean nthParentIsASTArgumentList(Node node, int n) {
        Node currentNode = node;
        for (int i = 0; i < n; i++) {
            if (currentNode==null) {
                return false;
            }
            if (currentNode instanceof ASTArgumentList) {
                //System.err.println("nth parent is a ASTArgumentList i="+i);
                return true;
            }
            currentNode = currentNode.jjtGetParent();
        }
        return false;
    }
    private boolean hasAtLeastSixParents(Node node) {
        Node currentNode = node;
        for (int i = 0; i < 6; i++) {
            if (currentNode instanceof ASTCompilationUnit) {
                //System.err.println("less than 6 parents");
                return false;
            }
            currentNode = currentNode.jjtGetParent();
        }
        return true;
    }
}

