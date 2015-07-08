package org.acm.seguin.pmd.rules.design;

import org.acm.seguin.parser.ast.SimpleNode;
import org.acm.seguin.pmd.stat.DataPoint;
import org.acm.seguin.pmd.stat.StatisticalRule;

/**
 * This is a common super class for things which
 * have excessive length.
 *
 * i.e. LongMethod and LongClass rules.
 *
 * To implement an ExcessiveLength rule, you pass
 * in the Class of node you want to check, and this
 * does the rest for you.
 */
public class ExcessiveLengthRule extends StatisticalRule {
    private Class nodeClass;

    public ExcessiveLengthRule(Class nodeClass) {
        this.nodeClass = nodeClass;
    }

    public Object visit(SimpleNode node, Object data) {
        if (nodeClass.isInstance(node)) {
            DataPoint point = new DataPoint();
            point.setLineNumber(node.getBeginLine());
            point.setScore(1.0 * (node.getEndLine() - node.getBeginLine()));
            point.setRule(this);
            point.setMessage(getMessage());
            addDataPoint(point);
        }

        return node.childrenAccept(this, data);
    }
}


