package org.acm.seguin.pmd.rules.design;

import org.acm.seguin.parser.ast.ASTClassDeclaration;

/**
 * This rule detects when a class exceeds a certain
 * threshold.  i.e. if a class has more than 1000 lines
 * of code.
 */
public class LongClassRule extends ExcessiveLengthRule {
    public LongClassRule() {
        super(ASTClassDeclaration.class);
    }
}
