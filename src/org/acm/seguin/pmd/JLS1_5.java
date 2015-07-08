package org.acm.seguin.pmd;

import org.acm.seguin.parser.JavaParser;

import java.io.InputStream;
import java.io.Reader;

public class JLS1_5 implements JLSVersion {

    public JavaParser createParser(InputStream in) {
        throw new RuntimeException("Not yet implemented");
    }

    public JavaParser createParser(Reader in) {
        throw new RuntimeException("Not yet implemented");
    }
}
