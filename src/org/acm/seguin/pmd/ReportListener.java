package org.acm.seguin.pmd;

import org.acm.seguin.pmd.stat.Metric;

public interface ReportListener {
    void ruleViolationAdded(RuleViolation ruleViolation);

    void metricAdded(Metric metric);
}
