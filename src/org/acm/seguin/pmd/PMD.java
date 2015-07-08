package org.acm.seguin.pmd;

import org.acm.seguin.parser.ast.ASTCompilationUnit;
import org.acm.seguin.parser.JavaParser;
import org.acm.seguin.parser.ParseException;
import org.acm.seguin.pmd.cpd.FileFinder;
import org.acm.seguin.pmd.cpd.JavaLanguage;
import org.acm.seguin.pmd.renderers.CSVRenderer;
import org.acm.seguin.pmd.renderers.EmacsRenderer;
import org.acm.seguin.pmd.renderers.HTMLRenderer;
import org.acm.seguin.pmd.renderers.IDEAJRenderer;
import org.acm.seguin.pmd.renderers.Renderer;
import org.acm.seguin.pmd.renderers.TextRenderer;
import org.acm.seguin.pmd.renderers.XMLRenderer;
import org.acm.seguin.pmd.symboltable.SymbolFacade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class PMD {
    public static final String EOL = System.getProperty("line.separator", "\n");

    private JLSVersion jlsVersion;

    public PMD() {
        jlsVersion = new JLS1_4();
    }

    public PMD(JLSVersion jlsVersion) {
        this.jlsVersion = jlsVersion;
    }

    /**
     * @param reader - a Reader to the Java code to analyse
     * @param ruleSet - the set of rules to process against the file
     * @param ctx - the context in which PMD is operating.  This contains the Renderer and whatnot
     */
    public void processFile(Reader reader, RuleSet ruleSet, RuleContext ctx) throws PMDException {
        try {
            JavaParser parser = jlsVersion.createParser(reader);
            ASTCompilationUnit c = parser.CompilationUnit();
            Thread.yield();
            SymbolFacade stb = new SymbolFacade();
            stb.initializeWith(c);
            List acus = new ArrayList();
            acus.add(c);
            ruleSet.apply(acus, ctx);
            reader.close();
        } catch (ParseException pe) {
            throw new PMDException("Error while parsing " + ctx.getSourceCodeFilename(), pe);
        } catch (Exception e) {
            throw new PMDException("Error while processing " + ctx.getSourceCodeFilename(), e);
        }
    }

    /**
     * @param fileContents - an InputStream to the Java code to analyse
     * @param ruleSet - the set of rules to process against the file
     * @param ctx - the context in which PMD is operating.  This contains the Report and whatnot
     */
    public void processFile(InputStream fileContents, RuleSet ruleSet, RuleContext ctx) throws PMDException {
        processFile(new InputStreamReader(fileContents), ruleSet, ctx);
    }

    public static void main(String[] args) {
        CommandLineOptions opts = new CommandLineOptions(args);

        List files;
        if (opts.containsCommaSeparatedFileList()) {
            files = collectFromCommaDelimitedString(opts.getInputFileName());
        } else {
            files = collectFilesFromOneName(opts.getInputFileName());
        }

        PMD pmd = new PMD();

        RuleContext ctx = new RuleContext();
        ctx.setReport(new Report());

        try {
            RuleSetFactory ruleSetFactory = new RuleSetFactory();
            RuleSet rules = ruleSetFactory.createRuleSet(opts.getRulesets());
            for (Iterator i = files.iterator(); i.hasNext();) {
                File file = (File) i.next();
                ctx.setSourceCodeFilename(glomName(opts.shortNamesEnabled(), opts.getInputFileName(), file));
                try {
                    pmd.processFile(new FileInputStream(file), rules, ctx);
                } catch (PMDException pmde) {
                    if (opts.debugEnabled()) {
                        pmde.getReason().printStackTrace();
                    }
                    ctx.getReport().addError(new Report.ProcessingError(pmde.getMessage(), glomName(opts.shortNamesEnabled(), opts.getInputFileName(), file)));
                }
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println(opts.usage());
            fnfe.printStackTrace();
        } catch (RuleSetNotFoundException rsnfe) {
            System.out.println(opts.usage());
            rsnfe.printStackTrace();
        }

        try {
            Renderer r = opts.createRenderer();
            System.out.println(r.render(ctx.getReport()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(opts.usage());
            if (opts.debugEnabled()) {
                e.printStackTrace();
            }
        }
    }

    private static String glomName(boolean shortNames, String inputFileName, File file) {
        if (shortNames && inputFileName.indexOf(',') == -1 && (new File(inputFileName)).isDirectory()) {
            String name = file.getAbsolutePath().substring(inputFileName.length());
            if (name.startsWith(System.getProperty("file.separator"))) {
                name = name.substring(1);
            }
            return name;
        } else {
            return file.getAbsolutePath();
        }
    }

    private static List collectFilesFromOneName(String inputFileName) {
        return collect(inputFileName);
    }

    private static List collectFromCommaDelimitedString(String fileList) {
        List files = new ArrayList();
        for (StringTokenizer st = new StringTokenizer(fileList, ","); st.hasMoreTokens();) {
            files.addAll(collect(st.nextToken()));
        }
        return files;
    }

    private static List collect(String filename) {
        File inputFile = new File(filename);
        if (!inputFile.exists()) {
            throw new RuntimeException("File " + inputFile.getName() + " doesn't exist");
        }
        List files;
        if (!inputFile.isDirectory()) {
            files = new ArrayList();
            files.add(inputFile);
        } else {
            FileFinder finder = new FileFinder();
            files = finder.findFilesFrom(inputFile.getAbsolutePath(), new JavaLanguage.JavaFileOrDirectoryFilter(), true);
        }
        return files;
    }

}
