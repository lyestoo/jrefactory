package org.acm.seguin.pmd.cpd;

import java.io.File;

public interface CPDListener {
    void addedFile(int fileCount, File file);
    void comparisonCountUpdate(long comparisons);
}
