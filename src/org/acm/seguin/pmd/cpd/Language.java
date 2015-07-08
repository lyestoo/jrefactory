package org.acm.seguin.pmd.cpd;

import java.io.FilenameFilter;

public interface Language {
    public Tokenizer getTokenizer();
    public FilenameFilter getFileFilter();
}
