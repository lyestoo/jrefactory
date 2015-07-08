/*
 *  User: tom
 *  Date: Jul 9, 2002
 *  Time: 1:18:38 PM
 */
package org.acm.seguin.ide.common.options;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Set;
import java.util.HashSet;


import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import org.acm.seguin.ide.common.IDEPlugin;

import org.acm.seguin.ide.common.options.PropertiesFile;

import org.acm.seguin.pmd.Rule;
import org.acm.seguin.pmd.RuleSet;
import org.acm.seguin.pmd.RuleSetFactory;
import org.acm.seguin.pmd.RuleSetNotFoundException;

/**
 *  Description of the Class
 *
 *@author     jiger.p
 *@created    April 22, 2003
 */
public class SelectedRules {

   private static Set notFoundRulesets = new HashSet();
   // Rule -> JCheckBox
   private Map defaultRules = null;
   private Map rules = new TreeMap(new Comparator() {
            public int compare(Object o1, Object o2) {
               Rule r1 = (Rule)o1;
               Rule r2 = (Rule)o2;
               return r1.getName().compareTo(r2.getName());
            }
         });


   private PropertiesFile props;


   /**
    *  Constructor for the SelectedRules object
    *
    *@param  project                       Description of Parameter
    *@param  parent                        Description of Parameter
    *@exception  RuleSetNotFoundException  Description of the Exception
    */
   public SelectedRules(String project, java.awt.Component parent) throws RuleSetNotFoundException {
      if ("default".equals(project)) {
         notFoundRulesets = new HashSet();
      } else {
         if (defaultRules!=null) { 
            rules.putAll(defaultRules);
         }
      }
      
      props = IDEPlugin.getProperties("pmd", project);
      RuleSetFactory rsf = new RuleSetFactory();
      for (Iterator i = rsf.getRegisteredRuleSets(); i.hasNext(); ) {
         addRuleSet2Rules((RuleSet)i.next());
      }


      //Load project modified RuleSets if any.
      java.io.File dir = org.acm.seguin.util.FileSettings.getRefactorySettingsRoot();
      dir = new java.io.File(dir, "projects");
      dir = new java.io.File(dir, project);
      if (dir.exists()) {
         dir = new java.io.File(dir, "rules");
         if (!dir.exists()) {
            dir.mkdir();
         }
         java.io.File[] files = dir.listFiles();
         for (int i=0; i<files.length; i++) {
            try {
               String rulesetName= files[i].getCanonicalPath();
               if (rulesetName.toLowerCase().endsWith(".xml")) {
                  addRuleSet2Rules(rsf.createRuleSet(new java.io.FileInputStream(files[i])));
               }
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      }

      //Load custom RuleSets if any.
      if (props.isLocalProperty("pmd.path")) {
         String customRuleSetPath = props.getString("pmd.path");
         if (!(customRuleSetPath == null)) {
            StringTokenizer strtok = new StringTokenizer(customRuleSetPath, ",");
            while (strtok.hasMoreTokens()) {
               String rulesetName = strtok.nextToken();
               if (rulesetName!=null && !rulesetName.equals("<none>")) {
                  try {
                     addRuleSet2Rules(rsf.createRuleSet(rulesetName));
                  } catch (Exception e) {
                     if (notFoundRulesets.add(rulesetName)) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(parent, "Can't load custom ruleset \"" + rulesetName + "\"", "JavaStyle", JOptionPane.ERROR_MESSAGE);
                     }
                  }
               }
            }
         }
      }
      if ("default".equals(project)) {
         defaultRules = rules;
      }
   }


   /**
    *  Gets the rule attribute of the SelectedRules object
    *
    *@param  candidate  Description of the Parameter
    *@return            The rule value
    */
   public Rule getRule(JCheckBox candidate) {
      for (Iterator i = rules.keySet().iterator(); i.hasNext(); ) {
         Rule rule = (Rule)i.next();
         JCheckBox box = (JCheckBox)rules.get(rule);
         if (box.equals(candidate)) {
            return rule;
         }
      }
      throw new RuntimeException("Couldn't find a rule that mapped to the passed in JCheckBox " + candidate);
   }


   /**
    *  Description of the Method
    *
    *@param  key  Description of the Parameter
    *@return      Description of the Return Value
    */
   public JCheckBox get(Object key) {
      return (JCheckBox)rules.get(key);
   }


   /**
    *  Gets the allBoxes attribute of the SelectedRules object
    *
    *@return    The allBoxes value
    */
   public Object[] getAllBoxes() {
      Object[] foo = new Object[rules.size()];
      int idx = 0;
      for (Iterator i = rules.values().iterator(); i.hasNext(); ) {
         foo[idx] = i.next();
         idx++;
      }
      return foo;
   }


   /**
    *  Gets the selectedRules attribute of the SelectedRules object
    *
    *@return    The selectedRules value
    */
   public RuleSet getSelectedRules() {
      RuleSet newRuleSet = new RuleSet();
      for (Iterator i = rules.keySet().iterator(); i.hasNext(); ) {
         Rule rule = (Rule)i.next();
         if (get(rule).isSelected()) {
            newRuleSet.addRule(rule);
         }
      }
      return newRuleSet;
   }


   /**
    *  Description of the Method
    *
    *@return    Description of the Return Value
    */
   public int size() {
      return rules.size();
   }


   /**  Description of the Method */
   public void save() {
      for (Iterator i = rules.keySet().iterator(); i.hasNext(); ) {
         Rule rule = (Rule)i.next();
         props.setString(rule.getName(), Boolean.toString(get(rule).isSelected()));
      }
      props.save();
   }


   /**
    *  Description of the Method
    *
    *@param  name  Description of the Parameter
    *@return       Description of the Return Value
    */
   private JCheckBox createCheckBox(String name) {
      JCheckBox box = new JCheckBox(name);
      try {
         box.setSelected(props.getBoolean(name));
      } catch (Exception e) {
         box.setSelected(true);
      }
      return box;
   }


   /**
    *  Adds a feature to the RuleSet2Rules attribute of the SelectedRules object
    *
    *@param  rs  The feature to be added to the RuleSet2Rules attribute
    */
   private void addRuleSet2Rules(RuleSet rs) {
      for (Iterator j = rs.getRules().iterator(); j.hasNext(); ) {
         Rule rule = (Rule)j.next();
         rules.remove(rule);  // FIXME? this shouldn't be necessary?!?!
         rules.put(rule, createCheckBox(rule.getName()));
      }
   }
}

