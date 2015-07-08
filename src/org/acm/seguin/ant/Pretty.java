/* ====================================================================
 * The JRefactory License, Version 1.0
 *
 * Copyright (c) 2001 JRefactory.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        JRefactory (http://www.sourceforge.org/projects/jrefactory)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "JRefactory" must not be used to endorse or promote
 *    products derived from this software without prior written
 *    permission. For written permission, please contact seguin@acm.org.
 *
 * 5. Products derived from this software may not be called "JRefactory",
 *    nor may "JRefactory" appear in their name, without prior written
 *    permission of Chris Seguin.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE CHRIS SEGUIN OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of JRefactory.  For more information on
 * JRefactory, please see
 * <http://www.sourceforge.org/projects/jrefactory>.
 */
package org.acm.seguin.ant;

import java.io.*;
import java.util.*;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;
//import org.apache.tools.ant.util.*;

import org.acm.seguin.awt.ExceptionPrinter;
import org.acm.seguin.awt.TextExceptionPrinter;
import org.acm.seguin.util.FileSettings;
import org.acm.seguin.parser.factory.FileParserFactory;
import org.acm.seguin.pretty.PrettyPrintFile;
import org.apache.tools.ant.taskdefs.FixCRLF;

/**
 *@author     Ara Abrahamian (ara_e@email.com)
 *@created    May 18, 2001
 *@version    $Revision: 1.6 $
 */
public final class Pretty extends Task {
    private CVSUtil cvsUtil = new CVSUtil();
    private Vector filesets = new Vector();
    private boolean cvs = false;
    private File compileDir;


    /**
     *  Sets the cvs attribute of the Pretty object
     *
     *@param  cvs  The new cvs value
     */
    public void setCvs(boolean cvs) {
        this.cvs = cvs;
    }


    /**
     *  Sets the settingsdir attribute of the Pretty object
     *
     *@param  new_settings_dir  The new settingsdir value
     */
    public void setSettingsdir(File new_settings_dir) {
        FileSettings.setSettingsRoot(new_settings_dir);
    }

    public File getCompileDir()
    {
        return compileDir;
    }

    public void setCompileDir( File compileDir )
    {
        this.compileDir = compileDir;
    }

   /**
     *  Adds a set of files (nested fileset attribute).
     *
     *@param  set  The feature to be added to the Fileset attribute
     */
    public void addFileset(FileSet set) {
        filesets.addElement(set);
    }


    /**
     *  Description of the Method
     *
     *@exception  BuildException  Description of the Exception
     */
    public void execute() throws BuildException {
        // make sure we don't have an illegal set of options
        validateAttributes();

        try {
            ExceptionPrinter.register(new TextExceptionPrinter());
            PrettyPrintFile ppf = new PrettyPrintFile();
				FixCRLF fixcrlf_task = (FixCRLF)project.createTask("fixcrlf");

            configureFixCrlfTask( fixcrlf_task );

            ppf.setAsk(false);

            for (int i = 0; i < filesets.size(); i++) {
                FileSet fs = (FileSet) filesets.elementAt(i);
                DirectoryScanner ds = fs.getDirectoryScanner(project);
                File from_dir = fs.getDir(project);
                String[] src_files = ds.getIncludedFiles();

                for (int j = 0; j < src_files.length; j++) {
                    File source_file = new File( from_dir, src_files[j] );

                    if ( shouldBeautify( source_file, from_dir ) ) {
                        System.out.println("formatting:" + source_file);
                        ppf.setParserFactory(new FileParserFactory(source_file));

                        // reformat
                        ppf.apply(source_file);

                        fixcrlf_task.setSrcdir(from_dir);
                        fixcrlf_task.setIncludesfile(source_file);
                        fixcrlf_task.execute();
                    }
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();

            throw new BuildException("Cannot javastyle files", location);
        }
    }

   private void configureFixCrlfTask( FixCRLF fixcrlf_task ) {
      fixcrlf_task.setOwningTarget( getOwningTarget() );
      FixCRLF.CrLf eol = new FixCRLF.CrLf();
      eol.setValue("cr");
      fixcrlf_task.setEol(eol);
   }

   private boolean shouldBeautify( File source_file, File from_dir )
   {
      //if read-only, then we can't beautify it anyway
      if( source_file.canWrite() == false )
         return false;

      //if cvs parameter is false
      if( cvs == false )
         return sourceModifiedAfterLastCompile( source_file, from_dir );

      //if not cvs-modified
      if( cvsUtil.isFileModified(source_file) == false )
         return false;

      return sourceModifiedAfterLastCompile( source_file, from_dir );
   }

   private boolean sourceModifiedAfterLastCompile( File source_file, File from_dir )
   {
      //if compileDir parameter not specified, then we can't check for
      //modified date of source file against the compiled one
      if( compileDir == null )
         return true;

      //calc absolute paths so that we're sure we're comparing correct unique files
      String source_file_path = source_file.getAbsoluteFile().toString();
      String compile_dir_path = compileDir.getAbsoluteFile().toString();
      String from_dir_path = from_dir.getAbsoluteFile().toString();

      String source_file_name_from_dir = source_file_path.substring( from_dir_path.length() );
      String source_file_name_wo_ext = source_file_name_from_dir.substring( 0, source_file_name_from_dir.lastIndexOf( '.' ) );

      //now get the respective compiled file for the source file
      String compiled_file_name_from_dir = source_file_name_wo_ext + ".class";
      String compiled_file_name = compile_dir_path + compiled_file_name_from_dir;
      File compiled_file = new File( compiled_file_name );

      //not found, probably not compiled before, beautify
      if( compiled_file.exists() == false )
         return true;

      //since we put <pretty/> before <javac/>, <javac/> sets modification
      //date to a date bigger than that of source file when compiling it
      if( compiled_file.lastModified() > source_file.lastModified() )
         return false;

      return true;
   }

   /**
     *  Ensure we have a consistent and legal set of attributes, and set any
     *  internal flags necessary based on different combinations of attributes.
     *
     *@exception  BuildException  Description of the Exception
     */
    protected void validateAttributes() throws BuildException {
        if (filesets.size() == 0) {
            throw new BuildException("Specify at least one fileset.");
        }

        //possibly some other attributes: overwrite/destDir/etc
    }
}
//  EOF
