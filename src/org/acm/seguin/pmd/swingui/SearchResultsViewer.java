package org.acm.seguin.pmd.swingui;

import org.acm.seguin.pmd.RuleSet;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Donald A. Leckie
 * @since August 27, 2002
 * @version $Revision: 1.1 $, $Date: 2003/07/29 20:51:59 $
 */
class SearchResultsViewer extends ResultsViewer {

    /**
     ********************************************************************************
     *
     */
    protected SearchResultsViewer() {
        super();

    }

    /**
     ********************************************************************************
     *
     * @param ruleSet
     * @param directory
     */
    protected void analyze(File directory, RuleSet ruleSet) {
        List fileList;
        FileFilter fileFilter;
        File[] sourceFiles;

        fileList = new ArrayList(20);
        fileFilter = new FilesFilter();
        buildFileList(directory, fileList, fileFilter);
        sourceFiles = new File[fileList.size()];
        sourceFiles = (File[]) fileList.toArray(sourceFiles);
        fileList.clear();
        analyze(sourceFiles, ruleSet);
    }

    /**
     *******************************************************************************
     *
     * @param directory
     * @param fileList
     * @param fileFilter
     */
    private void buildFileList(File directory, List fileList, FileFilter fileFilter) {
        File[] files = directory.listFiles(fileFilter);

        for (int n = 0; n < files.length; n++) {
            if (files[n].isDirectory()) {
                buildFileList(files[n], fileList, fileFilter);
            } else {
                fileList.add(files[n]);
            }

            files[n] = null;
        }
    }

    /**
     *******************************************************************************
     *******************************************************************************
     *******************************************************************************
     */
    private class FilesFilter implements FileFilter {

        private String fileExtension = ".java";

        public boolean accept(File file) {
            if (file.isDirectory() && (file.isHidden() == false)) {
                return true;
            }

            if (file.isFile() && (file.isHidden() == false)) {
                String fileName = file.getName().toLowerCase();

                return (fileName.endsWith(fileExtension));
            }

            return false;

        }
    }
}
