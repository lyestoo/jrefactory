package org.acm.seguin.pmd.renderers;

import org.acm.seguin.pmd.PMD;
import org.acm.seguin.pmd.Report;
import org.acm.seguin.pmd.RuleViolation;
import org.acm.seguin.pmd.util.StringUtil;

import java.util.Iterator;

public class HTMLRenderer implements Renderer {
    public String render(Report report) {
        StringBuffer buf = new StringBuffer("<html><head><title>PMD</title></head><body>" + PMD.EOL + "<table align=\"center\" cellspacing=\"0\" cellpadding=\"3\"><tr>" + PMD.EOL + "<th>#</th><th>File</th><th>Line</th><th>Problem</th></tr>" + PMD.EOL);
        boolean colorize = true;
        int violationCount = 1;
        for (Iterator i = report.iterator(); i.hasNext();) {
            RuleViolation rv = (RuleViolation) i.next();
            buf.append("<tr");
            if (colorize) {
                buf.append(" bgcolor=\"lightgrey\"");
                colorize = false;
            } else {
                colorize = true;
            }
            buf.append("> " + PMD.EOL);
            buf.append("<td align=\"center\">" + violationCount + "</td>" + PMD.EOL);
            buf.append("<td width=\"*%\">" + rv.getFilename() + "</td>" + PMD.EOL);
            buf.append("<td align=\"center\" width=\"5%\">" + Integer.toString(rv.getLine()) + "</td>" + PMD.EOL);

            String d = rv.getDescription();
            d = StringUtil.replaceString(d, '&', "&amp;");
            d = StringUtil.replaceString(d, '<', "&lt;");
            d = StringUtil.replaceString(d, '>', "&gt;");
            buf.append("<td width=\"*\">" + d + "</td>" + PMD.EOL);

            buf.append("</tr>" + PMD.EOL);

            violationCount++;
        }
        buf.append("</table></body></html>");
        return buf.toString();
    }
}
