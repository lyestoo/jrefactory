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

import java.util.*;
import java.io.*;
import java.text.*;
import org.acm.seguin.awt.ExceptionPrinter;
import org.acm.seguin.awt.TextExceptionPrinter;

/**
 *@author     Ara Abrahamian (ara_e@email.com)
 *@created    June 21, 2001
 *@version    $Revision: 1.4 $
 */
public final class CVSUtil {
    private HashMap entries;

    private static TimeZone tz;


    /**
     *  Constructor for the CVSUtil object
     */
    public CVSUtil() {
        entries = new HashMap();
    }


    /**
     *  Gets the fileModified attribute of the CVSUtil object
     *
     *@param  file  Description of the Parameter
     *@return       The fileModified value
     */
    public boolean isFileModified(File file) {
        CVSEntry entry = (CVSEntry) entries.get(file.toString().replace(File.separatorChar, '/'));

        if (entry == null) {
            //either a new file or the Entries file not loaded yet

            //try loading the Entries file
            entry = loadEntriesFileFor(file);

            if (entry == null) {
                //new file, not yet placed at cvs

                return true;
            }
        }

        return !entry.equalsTime(file.lastModified());
    }


    /**
     *  Description of the Method
     *
     *@param  file  Description of the Parameter
     *@return       Description of the Return Value
     */
    private CVSEntry loadEntriesFileFor(File file) {
        File workingDirectory = file.getParentFile();
        int linenum = 0;
        String line = null;
        BufferedReader in = null;
        File entriesFile = new File(workingDirectory + "/CVS/Entries");

        if (!entriesFile.exists()) {
            return null;
        }

        try {
            in = new BufferedReader(new FileReader(entriesFile));

            for (linenum = 1; ; ++linenum) {
                try {
                    line = in.readLine();
                }
                catch (IOException ex) {
                    line = null;
                    break;
                }

                if (line == null) {
                    break;
                }

                if (line.startsWith("/")) {
                    try {
                        CVSEntry entry = CVSEntry.parseEntryLine(workingDirectory, line);

                        entries.put(entry.getFileName(), entry);
                    }
                    catch (ParseException ex) {
                        System.err.println("Bad 'Entries' line " + linenum + ", '" + line + "' - " + ex.getMessage());
                    }
                }
            }
        }
        catch (IOException ex) {
            in = null;
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException ex) {
                }
            }
        }

        return (CVSEntry) entries.get(file.toString().replace(File.separatorChar, '/'));
    }


    /**
     *  Description of Class
     *
     *@author     unknown
     *@created    October 18, 2001
     */
    public static class CVSEntry {
        private Date date;
        private String fileName;


        /**
         *  Sets the timestamp attribute of the CVSEntry object
         *
         *@param  timeStamp  The new timestamp value
         */
        public void setTimestamp(String timeStamp) {
            String tstamp = new String(timeStamp);
            String conflict = null;

            if (tstamp.length() < 1) {
                this.date = null;
            }
            else if (tstamp.startsWith("+")) {
                // We have received a "+conflict" format, which
                // typically only comes from the server.
                conflict = tstamp.substring(1);

                if (conflict.equals("=")) {
                    // In this case, the server is indicating that the
                    // file is "going to be equal" once the 'Merged' handling
                    // is completed. To retain the "inConflict" nature of
                    // the entry, we will simply set the conflict to an
                    // empty string (not null), as the conflict will be
                    // set very shortly as a result of the 'Merged' handling.
                    //
                    conflict = "";
                }
            }
            else {
                int index = tstamp.indexOf('+');

                if (index < 0) {
                    this.date = null;
                }
                else {
                    // The "timestamp+conflict" case.
                    // This should <em>only</em> comes from an Entries
                    // file, and should never come from the server.
                    conflict = tstamp.substring(index + 1);
                    tstamp = tstamp.substring(0, index);

                    this.date = null;
                    // signal need to parse!

                    if (tstamp.equals("Result of merge")) {
                        // REVIEW should we always set to conflict?
                        // If timestamp is empty, use the conflict...
                        if ((timeStamp == null || timeStamp.length() == 0) && conflict.length() > 0) {
                            timeStamp = conflict;
                        }
                    }
                    else {
                        timeStamp = tstamp;
                    }
                }
            }

            // If tsCache is set to null, we need to update it...
            if (this.date == null && timeStamp.length() > 0) {
                try {
                    this.date = parseTimestamp(timeStamp);
                }
                catch (ParseException ex) {
                    this.date = null;
                }
            }
        }


        /**
         *  Gets the date attribute of the CVSEntry object
         *
         *@return    The date value
         */
        private Date getDate() {
            return date;
        }


        /**
         *  Gets the fileName attribute of the CVSEntry object
         *
         *@return    The fileName value
         */
        public String getFileName() {
            return fileName;
        }


        /**
         *  Description of the Method
         *
         *@param  src  Description of the Parameter
         *@return      Description of the Return Value
         */
        public boolean equals(Object src) {
            if (src instanceof CVSEntry) {
                return fileName.equals(((CVSEntry) src).getFileName());
            }
            else {
                return false;
            }
        }


        /**
         *  Determines if this timestamp is considered equivalent to the time
         *  represented by the parameter we are passed. Note that we allow up
         *  to, but not including, one second of time difference, since Java
         *  allows millisecond time resolution while CVS stores second
         *  resolution timestamps. Further, we allow the resolution difference
         *  on either side of the second because we can not be sure of the
         *  rounding.
         *
         *@param  time  Description of the Parameter
         *@return       Description of the Return Value
         */
        public boolean equalsTime(long time) {
            //System.out.println("time="+time);
            //System.out.println("date.getTime()="+date.getTime());
            if( date == null )
               return false;
            else
               return (date.getTime() > time) ? ((date.getTime() - time) < 1000) : ((time - date.getTime()) < 1000);
        }


        /**
         *  Description of the Method
         *
         *@return    Description of the Return Value
         */
        public int hashCode() {
            return fileName.hashCode();
        }


        /**
         *  Description of the Method
         *
         *@param  toker  Description of the Parameter
         *@return        Description of the Return Value
         */
        private static String parseAToken(StringTokenizer toker) {
            String token = null;

            try {
                token = toker.nextToken();
            }
            catch (NoSuchElementException ex) {
                token = null;
            }

            return token;
        }


        /**
         *  Description of the Method
         *
         *@param  parent_dir          Description of the Parameter
         *@param  parseLine           Description of the Parameter
         *@return                     Description of the Return Value
         *@exception  ParseException  Description of the Exception
         */
        public static CVSEntry parseEntryLine(File parent_dir, String parseLine) throws ParseException {
            String token = null;
            String nameToke = null;
            String versionToke = null;
            String conflictToke = null;
            String optionsToke = null;
            String tagToke = null;
            StringTokenizer toker = new StringTokenizer(parseLine, "/", true);
            int tokeCount = toker.countTokens();

            if (tokeCount < 6) {
                throw new ParseException("not enough tokens in entries line "
                         + "(min 6, parsed " + tokeCount + ")",
                        0);
            }

            token = parseAToken(toker);
            //the starting slash

            nameToke = parseAToken(toker);

            if (nameToke == null) {
                throw new ParseException("could not parse entry name", 0);
            }
            else if (nameToke.equals("/")) {
                throw new ParseException("entry has an empty name", 0);
            }
            else {
                token = parseAToken(toker);

                if (token == null || !token.equals("/")) {
                    throw new ParseException("could not parse version's starting slash", 0);
                }
            }

            versionToke = parseAToken(toker);

            if (versionToke == null) {
                throw new ParseException("out of tokens getting version field",
                        0);
            }
            else if (versionToke.equals("/")) {
                versionToke = "";
            }
            else {
                token = parseAToken(toker);

                if (token == null || !token.equals("/")) {
                    throw new ParseException("could not parse conflict's starting slash", 0);
                }
            }

            conflictToke = parseAToken(toker);

            if (conflictToke == null) {
                throw new ParseException("out of tokens getting conflict field", 0);
            }
            else if (conflictToke.equals("/")) {
                conflictToke = "";
            }
            else {
                token = parseAToken(toker);

                if (token == null || !token.equals("/")) {
                    throw new ParseException("could not parse options' starting slash", 0);
                }
            }

            optionsToke = parseAToken(toker);

            if (optionsToke == null) {
                throw new ParseException("out of tokens getting options field", 0);
            }
            else if (optionsToke.equals("/")) {
                optionsToke = "";
            }
            else {
                token = parseAToken(toker);

                if (token == null || !token.equals("/")) {
                    throw new ParseException("could not parse tag's starting slash", 0);
                }
            }

            tagToke = parseAToken(toker);

            if (tagToke == null || tagToke.equals("/")) {
                tagToke = "";
            }

            CVSEntry entry = new CVSEntry();

            nameToke = parent_dir.toString() + "/" + nameToke;
            entry.fileName = nameToke.replace(File.separatorChar, '/');
            entry.setTimestamp(conflictToke);

            return entry;
        }


        /**
         *  Description of the Method
         *
         *@param  source              Description of the Parameter
         *@return                     Description of the Return Value
         *@exception  ParseException  Description of the Exception
         */
        public Date parseTimestamp(String source) throws ParseException {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.US);

            dateFormat.setTimeZone(CVSUtil.tz);

            Date result = dateFormat.parse(source, new ParsePosition(0));

            if (result == null) {
                throw new ParseException("invalid timestamp '" + source + "'", 0);
            }

            return result;
        }


        /**
         *  Description of the Method
         *
         *@return    Description of the Return Value
         */
        public String toString() {
            return "fileName=" + fileName + " date=" + date;
        }
    }

    static {
        tz = TimeZone.getTimeZone("GMT");
        ExceptionPrinter.register(new TextExceptionPrinter());
    }
}
//  EOF
