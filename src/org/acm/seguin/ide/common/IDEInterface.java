/*
    ====================================================================
    The JRefactory License, Version 1.0
    Copyright (c) 2001 JRefactory.  All rights reserved.
    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions
    are met:
    1. Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.
    2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in
    the documentation and/or other materials provided with the
    distribution.
    3. The end-user documentation included with the redistribution,
    if any, must include the following acknowledgment:
    "This product includes software developed by the
    JRefactory (http://www.sourceforge.org/projects/jrefactory)."
    Alternately, this acknowledgment may appear in the software itself,
    if and wherever such third-party acknowledgments normally appear.
    4. The names "JRefactory" must not be used to endorse or promote
    products derived from this software without prior written
    permission. For written permission, please contact seguin@acm.org.
    5. Products derived from this software may not be called "JRefactory",
    nor may "JRefactory" appear in their name, without prior written
    permission of Chris Seguin.
    THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
    WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
    OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
    DISCLAIMED.  IN NO EVENT SHALL THE CHRIS SEGUIN OR
    ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
    SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
    LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
    USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
    ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
    OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
    OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
    SUCH DAMAGE.
    ====================================================================
    This software consists of voluntary contributions made by many
    individuals on behalf of JRefactory.  For more information on
    JRefactory, please see
    <http://www.sourceforge.org/projects/jrefactory>.
  */
package org.acm.seguin.ide.common;

import java.awt.Frame;
import java.io.IOException;

import org.acm.seguin.ide.common.options.PropertiesFile;

/**
 *  Description of the Interface
 *
 *@author    Mike Atkinson
 */
public interface IDEInterface {
   /**
    *  Debugging message urgency. Should be used for messages only useful when debugging a problem.
    *
    *@since    JRefactory 2.8.02
    */
   public final static int DEBUG = 1;

   /**
    *  Message urgency. Should be used for messages which give more detail than notices.
    *
    *@since    JRefactory 2.8.02
    */
   public final static int MESSAGE = 3;

   /**
    *  Notice urgency. Should be used for messages that directly affect the user.
    *
    *@since    JRefactory 2.8.02
    */
   public final static int NOTICE = 5;

   /**
    *  Warning urgency. Should be used for messages that warrant attention.
    *
    *@since    JRefactory 2.8.02
    */
   public final static int WARNING = 7;

   /**
    *  Error urgency. Should be used for messages that signal a failure.
    *
    *@since    JRefactory 2.8.02
    */
   public final static int ERROR = 9;


   /**
    *  Gets the IDEProperty attribute of the IDEInterface object
    *
    *@param  prop  Description of Parameter
    *@return       The IDEProperty value
    */
   public String getIDEProperty(String prop);


   /**
    *  Gets the IDEProperty attribute of the IDEInterface object
    *
    *@param  prop   Description of Parameter
    *@param  deflt  Description of Parameter
    *@return        The IDEProperty value
    */
   public String getIDEProperty(String prop, String deflt);


   /**
    *  Gets the IDEProjects attribute of the IDEInterface object
    *
    *@param  parent  Description of Parameter
    *@return         The IDEProjects value
    */
   public String[] getIDEProjects(Frame parent);


   /**
    *  Description of the Method
    *
    *@param  parent  Description of Parameter
    */
   public void showWaitCursor(Frame parent);


   /**
    *  Description of the Method
    *
    *@param  parent  Description of Parameter
    */
   public void hideWaitCursor(Frame parent);


   /**
    *  Description of the Method
    *
    *@param  urgency  Description of Parameter
    *@param  source   Description of Parameter
    *@param  message  Description of Parameter
    */
   public void log(int urgency, Object source, Object message);


   /**
    *  Gets the Properties attribute of the IDEInterface object
    *
    *@param  type     Description of Parameter
    *@param  project  Description of Parameter
    *@return          The Properties value
    */
   public PropertiesFile getProperties(String type, String project);


   /**
    *  Description of the Method
    *
    *@param  view      Description of Parameter
    *@param  fileName  The new Buffer value
    */
   public void setBuffer(Frame view, Object fileName);


   /**
    *  Description of the Method
    *
    *@param  view   Description of Parameter
    *@param  start  The new Selection value
    *@param  end    The new Selection value
    */
   public void setSelection(Frame view, Object buffer, int start, int end);


   /**
    *  Description of the Method
    *
    *@param  view             Description of Parameter
    *@exception  IOException  Description of Exception
    */
   public void cpdBuffer(Frame view, Object buffer) throws IOException;


   /**
    *  Description of the Method
    *
    *@param  view             Description of Parameter
    *@exception  IOException  Description of Exception
    */
   public void cpdAllOpenBuffers(Frame view) throws IOException;


   /**
    *  Description of the Method
    *
    *@param  view             Description of Parameter
    *@param  recursive        Description of Parameter
    *@exception  IOException  Description of Exception
    */
   public void cpdDir(Frame view, boolean recursive) throws IOException;


   /**
    *  Description of the Method
    *
    *@param  view             Description of Parameter
    *@param  fileName         Description of Parameter
    *@return                  Description of the Returned Value
    *@exception  IOException  Description of Exception
    */
   public Object openFile(Frame view, String fileName) throws IOException;



   /**
    *  Description of the Method
    *
    *@param  buffer  Description of Parameter
    *@param  begin   Description of Parameter
    *@return         The BeginLine value
    */
   public int getLineStartOffset(Object buffer, int begin);


   /**
    *  Description of the Method
    *
    *@param  buffer  Description of Parameter
    *@param  end     Description of Parameter
    *@return         The LineEndOffset value
    */
   public int getLineEndOffset(Object buffer, int end);



   /**
    *  Description of the Method
    *
    *@param  view   Description of Parameter
    *@param  start  Description of Parameter
    */
   public void moveCaretPosition(Frame view, Object buffer, int start);


   /**
    *  Description of the Method
    *
    *@param  runnable  Description of Parameter
    */
   public void runInAWTThread(Runnable runnable);


   /**
    *  Description of the Method
    *
    *@param  view  Description of Parameter
    *@return       The Text value
    */
   public String getText(Frame view, Object buffer);


   /**
    *  Description of the Method
    *
    *@param  view  Description of Parameter
    */
   public void checkBuffer(Frame view, Object buffer);


   /**
    *  Description of the Method
    *
    *@param  view  Description of Parameter
    */
   public void checkAllOpenBuffers(Frame view);


   /**
    *  Description of the Method
    *
    *@param  view       Description of Parameter
    *@param  recursive  Description of Parameter
    */
   public void checkDirectory(Frame view, boolean recursive);


   /**
    *  Description of the Method
    *
    *@param  view    Description of Parameter
    *@param  buffer  Description of Parameter
    */
   public void goToBuffer(Frame view, Object buffer);


   /**
    *  Description of the Method
    *
    *@param  buffer  Description of Parameter
    *@return         The LineCount value
    */
   public int getLineCount(Object buffer);



   /**
    *  Description of the Method
    *
    *@param  view    Description of Parameter
    *@param  buffer  Description of Parameter
    *@return         The ProjectName value
    */
   public String getProjectName(Frame view, Object buffer);


   /**
    *  Description of the Method
    *
    *@param  view    Description of Parameter
    *@param  buffer  Description of Parameter
    */
   public void saveProperties();

   /**
    *  Description of the Method
    *
    * @param  buffer  Description of Parameter
    * @return         The File path for this buffer
    */
   public String getFilePathForBuffer(Object buffer);


}

