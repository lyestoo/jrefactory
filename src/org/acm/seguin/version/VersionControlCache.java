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
package org.acm.seguin.version;

import java.util.Hashtable;

/**
 *  Cache of files - so that it doesn't take so long to get the menu
 *
 *@author     Chris Seguin
 *@created    June 17, 1999
 */
public class VersionControlCache {
    //  Instance Variables
    private Hashtable cache;

    /**
     *  Description of the Field
     */
    public final static int ADD = 0;
    /**
     *  Description of the Field
     */
    public final static int CHECK_IN = 1;
    /**
     *  Description of the Field
     */
    public final static int CHECK_OUT = 2;
    /**
     *  Description of the Field
     */
    public final static int CHECK_IN_PROGRESS = 3;
    /**
     *  Description of the Field
     */
    public final static int CHECK_OUT_PROGRESS = 4;
    /**
     *  Description of the Field
     */
    public final static int ADD_PROGRESS = 5;

    //  Class Variables
    private static VersionControlCache ssc = null;


    /**
     *  Constructor for the VersionControlCache object
     */
    protected VersionControlCache() {
        cache = new Hashtable();
    }


    /**
     *  Looks up a file in the cache
     *
     *@param  filename  Description of Parameter
     *@return           The InCache value
     */
    public boolean isInCache(String filename) {
        return cache.get(filename) != null;
    }


    /**
     *  Looks up a file in the cache
     *
     *@param  filename  Description of Parameter
     *@return           Description of the Returned Value
     */
    public int lookup(String filename) {
        Integer stored = (Integer) cache.get(filename);
        if (stored == null) {
            return ADD;
        }

        return stored.intValue();
    }


    /**
     *  Add to the cache
     *
     *@param  filename  the name of the file
     *@param  type      the state
     */
    public void add(String filename, int type) {
        cache.put(filename, new Integer(type));
    }


    /**
     *  Return the cache
     *
     *@return    the cache
     */
    public static VersionControlCache getCache() {
        if (ssc == null) {
            init();
        }

        return ssc;
    }


    /**
     *  Create a source safe cache
     */
    private static synchronized void init() {
        if (ssc == null) {
            ssc = new VersionControlCache();
        }
    }
}
