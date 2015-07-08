package org.acm.seguin.pmd.cpd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Match implements Comparable {

    private int tokenCount;
    private int lineCount;
    private List marks = new ArrayList();
    private String code;

    public Match(int tokenCount) {
        this.tokenCount = tokenCount;
    }
 	 
    public Match(int tokenCount, Mark first, Mark second) {
        marks.add(first);
        marks.add(second);
        this.tokenCount = tokenCount;
    }

    public void add(Mark mark) {
        marks.add(mark);
    }

    public int getMarkCount() {
        return this.marks.size();
    }
    public void setLineCount(int lineCount) {
        this.lineCount = lineCount;
    }

    public int getLineCount() {
        return this.lineCount;
    }

    public int getTokenCount() {
        return this.tokenCount;
    }

    public String getSourceCodeSlice() {
        return this.code;
    }

    public void setSourceCodeSlice(String code) {
        this.code = code;
    }

    public Iterator iterator() {
        return marks.iterator();
    }

    public int compareTo(Object o) {
        Match other = (Match)o;
        return other.getTokenCount() - this.getTokenCount();
    }

    public String toString() {
        return "Match:\r\ntokenCount = " + tokenCount + "\r\nmark1 = " + marks.get(0) + "\r\nmark2 =" + marks.get(1);
    }
}
