package org.acm.seguin.pmd.renderers;

import org.acm.seguin.pmd.PMD;
import org.acm.seguin.pmd.Report;
import org.acm.seguin.pmd.RuleViolation;

import java.util.Iterator;

public class TextRenderer implements Renderer {
    public String render(Report report) {
        if (report.isEmpty()) {
            return "No problems found!";
        }
        StringBuffer buf = new StringBuffer();
        for (Iterator i = report.iterator(); i.hasNext();) {
            RuleViolation rv = (RuleViolation) i.next();
            buf.append(PMD.EOL + rv.getFilename());
            buf.append("\t" + Integer.toString(rv.getLine()));
            buf.append("\t" + rv.getDescription());
        }
        for (Iterator i = report.errors(); i.hasNext();) {
            Report.ProcessingError error = (Report.ProcessingError) i.next();
            buf.append(PMD.EOL + error.getFile());
            buf.append("\t-");
            buf.append("\t" + error.getMsg());
        }
        return buf.toString();
    }
}
