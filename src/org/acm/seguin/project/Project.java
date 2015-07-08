/* ====================================================================
 * The JRefactory License, Version 1.0
 *
 * Copyright (c) 2003 JRefactory.  All rights reserved.
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
package org.acm.seguin.project;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import org.acm.seguin.util.FileSettings;

/**
 *  Hold data on the known projects.
 *
 *@author     Mike Atkinson
 *@created    August 29, 2003
 *@since      jrefactory 2.8.01
 *@version    $Id: Project.java,v 1.6 2003/10/10 16:45:00 mikeatkinson Exp $
 */
public class Project {
    private static Project[] knownProjects = new Project[0];
    private static Project currentProject = null;
    public static Project getCurrentProject() {
        return currentProject;
    }
    public static String getCurrentProjectName() {
        if (currentProject==null) {
            return null;
        }
        return currentProject.projectName;
    }
    public static void setCurrentProject(Project project) {
        currentProject = project;
    }

    public static void loadProjects() {
        List projects = new ArrayList();
        File projectsRoot = getProjectsRoot();
        File[] files = projectsRoot.listFiles();
        for (int i=0; i<files.length; i++) {
            if (files[i].isDirectory()) {
                File projFile = new File(files[i], "proj.settings");
                Project proj = new Project(files[i].getName());
            }
        }
        if (projects.size()>0) {
           knownProjects = (Project[])projects.toArray(new Project[projects.size()]);
        } else {
           knownProjects = new Project[] { new Project("default") };
        }
    }

    public static void storeProjects() {
        for (int i=0; i<knownProjects.length; i++) {
            try {
                knownProjects[i].settings.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static File getProjectsRoot() {
        File root = FileSettings.getRefactorySettingsRoot();
        File projDir = new File(root, "projects");
        if (!projDir.exists()) {
            projDir.mkdir();
        }
        return projDir;
    }


    public static Project[] getProjects() {
        return knownProjects;
    }
    
    public static Project getProject(String name) {
        for (int i=0; i<knownProjects.length; i++) {
            if (knownProjects[i].projectName.equals(name)) {
                return knownProjects[i];
            }
        }
        return null;
    }

    public static Project createProject(String name) {
        Project project = getProject(name);
        if (project==null) {
            project = new Project(name);
            Project[] pa = new Project[knownProjects.length+1];
            System.arraycopy(knownProjects, 0, pa, 0, knownProjects.length);
            pa[knownProjects.length] = project;
            knownProjects = pa;
        }
        return project;
    }

    private FileSettings settings = null;
    private String projectName = null;
    
    
    private Project(String projectName) {
        this.projectName = projectName;
        settings = FileSettings.getSettings(projectName, "Refactory", "proj");
        if (projectName==null || projectName.length()==0 || "default".equals(projectName)) {
           if (!settings.isLocalProperty("CLASSPATH")) {
              settings.setString("CLASSPATH", ".");
              settings.setString("BASE_DIR", "");
           }
        }
        /*
        File projectsRoot = getProjectsRoot();
        File projDir = new File(projectsRoot, projectName);
        if (!projDir.exists()) {
            projDir.mkdir();
        }
        File projFile = new File(projDir, "proj.settings");
        if (!projFile.exists()) {
            try {
                projFile.createNewFile();
                settings = new FileSettings(projFile);
                setProperty("CLASSPATH", ".");
                setProperty("BASE_DIR", "");
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            if (!"default".equals(name)) {
                File root = FileSettings.getRefactorySettingsRoot();
                File prettyFile = new File(projDir, "pretty.settings");
                try {
                    java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter(prettyFile));
                    pw.println("# project "+name+" pretty.settings");
                    pw.close();
                } catch (java.io.IOException e) {
                }
            }
        } else {
            settings = new FileSettings(projFile);
        }
        */
    }
    
    public String getProperty(String propName) {
        //System.out.println("Project.getProperty("+propName+")");
        if (settings != null) {
            return settings.getProperty(propName);
        }
        return null;
    }
    public String getProperty(String propName, String defaultValue) {
        //System.out.println("Project.getProperty("+propName+","+defaultValue+")");
        try {
           if (settings != null) {
               return settings.getProperty(propName, defaultValue);
           }
        } catch (org.acm.seguin.util.SettingNotFoundException e) {
        }
        return defaultValue;
    }
    
    public void setProperty(String propName, String value) {
        settings.getProperty(propName, value);
    }

    
    public String getClassPath() {
        return getProperty("CLASSPATH", "");
    }
    public void setClassPath(String classpath) {
        setProperty("CLASSPATH", classpath);
    }

    public String getBaseDir() {
        return getProperty("BASE_DIR", "");
    }
    public void setBaseDir(String baseDir) {
        setProperty("BASE_DIR", baseDir);
    }
 
    /**
     * Logs a message through the project object if one has been provided.
     *
     * @param message The message to log.
     *                Should not be <code>null</code>.
     *
     * @param priority The logging priority of the message.
     */
    protected void log(String message) {
       System.out.println(message);
    }
    public File resolveFile(String name) throws ProjectException {
        return new File(name);
    }
}
