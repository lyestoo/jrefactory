/*
 *  ConstructorCallsOverridableMethodRule.java
 *  dnoyeb@users.sourceforge.net
 *  Created on February 5, 2003, 1:54 PM
 */
package org.acm.seguin.pmd.rules;

import org.acm.seguin.pmd.RuleContext;
import org.acm.seguin.parser.ast.ASTArguments;
import org.acm.seguin.parser.ast.ASTClassDeclaration;
import org.acm.seguin.parser.ast.ASTCompilationUnit;
import org.acm.seguin.parser.ast.ASTConstructorDeclaration;
import org.acm.seguin.parser.ast.ASTExplicitConstructorInvocation;
import org.acm.seguin.parser.ast.ASTInterfaceDeclaration;
import org.acm.seguin.parser.ast.ASTLiteral;
import org.acm.seguin.parser.ast.ASTMethodDeclarator;
import org.acm.seguin.parser.ast.ASTName;
import org.acm.seguin.parser.ast.ASTNestedClassDeclaration;
import org.acm.seguin.parser.ast.ASTNestedInterfaceDeclaration;
import org.acm.seguin.parser.ast.ASTPrimaryExpression;
import org.acm.seguin.parser.ast.ASTPrimaryPrefix;
import org.acm.seguin.parser.ast.ASTPrimarySuffix;
import org.acm.seguin.parser.ast.ASTUnmodifiedClassDeclaration;
import org.acm.seguin.parser.ast.ASTExpression;
import org.acm.seguin.parser.ast.AccessNode;
import org.acm.seguin.parser.Node;
import org.acm.seguin.parser.ast.SimpleNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  Searches through all methods and constructors called from constructors. It marks as dangerous any call to
 *  overridable methods from non-private constructors. It marks as dangerous any calls to dangerous private constructors
 *  from non-private constructors.
 *
 *@author    CL Gilbert (dnoyeb@users.sourceforge.net)
 *@todo      match parameter types. Agressive strips off any package names. Normal compares the names as is.
 *@todo      What about interface declarations which can have internal classes
 */
public final class ConstructorCallsOverridableMethodRule extends org.acm.seguin.pmd.AbstractRule {

   /**  1 package per class.  */
   private final List evalPackages = new ArrayList();

   private final static NullEvalPackage nullEvalPackage = new NullEvalPackage();


   ////////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////////
   //The Visited Methods

   /**
    *  Work on each file independently.
    *
    *@param  node  Description of Parameter
    *@param  data  Description of Parameter
    *@return       Description of the Returned Value
    */
   public Object visit(ASTCompilationUnit node, Object data) {
      clearEvalPackages();
      //		try {
      return super.visit(node, data);
      //		}
      //		catch(Exception e){
      //			e.printStackTrace();
      //		}
   }

   //for testing only

   //	public Object visit(ASTPackageDeclaration node, Object data){
   //		System.out.println("package= " + ((ASTName)node.jjtGetFirstChild()).getImage());
   //		return super.visit(node,data);
   //	}

   /**
    *  This check must be evaluated independelty for each class. Inner classses get their own EvalPackage in order to
    *  perform independent evaluation.
    *
    *@param  node  Description of Parameter
    *@param  data  Description of Parameter
    *@return       Description of the Returned Value
    */
   public Object visit(ASTClassDeclaration node, Object data) {
      return visitClassDec(node, data);
   }


   /**
    *  Description of the Method
    *
    *@param  node  Description of Parameter
    *@param  data  Description of Parameter
    *@return       Description of the Returned Value
    */
   public Object visit(ASTNestedClassDeclaration node, Object data) {
      return visitClassDec(node, data);
   }


   /**
    *  Description of the Method
    *
    *@param  node  Description of Parameter
    *@param  data  Description of Parameter
    *@return       Description of the Returned Value
    */
   public Object visit(ASTInterfaceDeclaration node, Object data) {
      putEvalPackage(nullEvalPackage);
      Object o = super.visit(node, data);
      //interface may have inner classes, possible? if not just skip whole interface
      removeCurrentEvalPackage();
      return o;
   }


   /**
    *  Description of the Method
    *
    *@param  node  Description of Parameter
    *@param  data  Description of Parameter
    *@return       Description of the Returned Value
    */
   public Object visit(ASTNestedInterfaceDeclaration node, Object data) {
      putEvalPackage(nullEvalPackage);
      Object o = super.visit(node, data);
      //interface may have inner classes, possible? if not just skip whole interface
      removeCurrentEvalPackage();
      return o;
   }


   /**
    *  Non-private constructor's methods are added to a list for later safety evaluation. Non-private constructor's
    *  calls on private constructors are added to a list for later safety evaluation. Private constructors are added to
    *  a list so their safety to be called can be later evaluated. Note: We are not checking private constructor's calls
    *  on non-private constructors because all non-private constructors will be evaluated for safety anyway. This means
    *  we wont flag a private constructor as unsafe just because it calls an unsafe public constructor. We want to show
    *  only 1 instance of an error, and this would be 2 instances of the same error.
    *
    *@param  node  Description of Parameter
    *@param  data  Description of Parameter
    *@return       Description of the Returned Value
    *@todo         eliminate the redundency
    */
   public Object visit(ASTConstructorDeclaration node, Object data) {
      if (!(getCurrentEvalPackage() instanceof NullEvalPackage)) {
         //only evaluate if we have an eval package for this class
         List calledMethodsOfConstructor = new ArrayList();
         ConstructorHolder ch = new ConstructorHolder(node);
         addCalledMethodsOfNode(node, calledMethodsOfConstructor, getCurrentEvalPackage().m_ClassName);
         if (!node.isPrivate()) {
            //these calledMethods are what we will evaluate for being called badly
            getCurrentEvalPackage().calledMethods.addAll(calledMethodsOfConstructor);
            //these called private constructors are what we will evaluate for being called badly
            //we add all constructors invoked by non-private constructors
            //but we are only interested in the private ones.  We just can't tell the difference here
            ASTExplicitConstructorInvocation eci = ch.getASTExplicitConstructorInvocation();
            if (eci != null && "this".equals(eci.getImage())) {
               //&& eci.isThis()) {
               getCurrentEvalPackage().calledConstructors.add(ch.getCalledConstructor());
            }
         } else {
            //add all private constructors to list for later evaluation on if they are safe to call from another constructor
            //store this constructorHolder for later evaluation
            getCurrentEvalPackage().allPrivateConstructorsOfClass.put(ch, calledMethodsOfConstructor);
         }
      }
      return super.visit(node, data);
   }


   /**
    *  Create a MethodHolder to hold the method. Store the MethodHolder in the Map as the key Store each method called
    *  by the current method as a List in the Map as the Object
    *
    *@param  node  Description of Parameter
    *@param  data  Description of Parameter
    *@return       Description of the Returned Value
    */
   public Object visit(ASTMethodDeclarator node, Object data) {
      if (!(getCurrentEvalPackage() instanceof NullEvalPackage)) {
         //only evaluate if we have an eval package for this class
         AccessNode parent = (AccessNode) node.jjtGetParent();
         MethodHolder h = new MethodHolder(node);
         if (!parent.isPrivate() && !parent.isStatic() && !parent.isFinal()) {
            h.setDangerous(true);
            //this method is overridable
         }
         List l = new ArrayList();
         addCalledMethodsOfNode((SimpleNode) parent, l, getCurrentEvalPackage().m_ClassName);
         getCurrentEvalPackage().allMethodsOfClass.put(h, l);
      }
      return super.visit(node, data);
   }
   //could use java.util.Stack


   /**
    *  Gets the CurrentEvalPackage attribute of the ConstructorCallsOverridableMethodRule object
    *
    *@return    The CurrentEvalPackage value
    */
   private EvalPackage getCurrentEvalPackage() {
      return (EvalPackage) evalPackages.get(evalPackages.size() - 1);
   }


   /**
    *  Adds and evaluation package and makes it current
    *
    *@param  ep  Description of Parameter
    */
   private void putEvalPackage(EvalPackage ep) {
      evalPackages.add(ep);
   }


   /**  Description of the Method */
   private void removeCurrentEvalPackage() {
      evalPackages.remove(evalPackages.size() - 1);
   }


   /**  Description of the Method */
   private void clearEvalPackages() {
      evalPackages.clear();
   }


   /**
    *  This check must be evaluated independelty for each class. Inner classses get their own EvalPackage in order to
    *  perform independent evaluation.
    *
    *@param  node  Description of Parameter
    *@param  data  Description of Parameter
    *@return       Description of the Returned Value
    */
   private Object visitClassDec(AccessNode node, Object data) {
      String className = ((ASTUnmodifiedClassDeclaration) node.jjtGetFirstChild()).getImage();
      //	System.out.println("Class is " + className);
      //evaluate each level independently
      if (!node.isFinal() && !node.isStatic()) {
         putEvalPackage(new EvalPackage(className));
      } else {
         //System.out.println("NullEvalPackage!");
         putEvalPackage(nullEvalPackage);
      }
      //store any errors caught from other passes.
      if (node instanceof ASTClassDeclaration) {
         super.visit((ASTClassDeclaration) node, data);
      } else {
         super.visit((ASTNestedClassDeclaration) node, data);
      }
      //skip this class if it has no evaluation package
      if (!(getCurrentEvalPackage() instanceof NullEvalPackage)) {
         //evaluate danger of all methods in class
         while (evaluateDangerOfMethods(getCurrentEvalPackage().allMethodsOfClass) == true) {
            ;
         }
         //evaluate danger of constructors
         evaluateDangerOfConstructors1(getCurrentEvalPackage().allPrivateConstructorsOfClass, getCurrentEvalPackage().allMethodsOfClass.keySet());
         while (evaluateDangerOfConstructors2(getCurrentEvalPackage().allPrivateConstructorsOfClass) == true) {
            ;
         }

         //get each method called on this object from a non-private constructor, if its dangerous flag it
         for (Iterator it = getCurrentEvalPackage().calledMethods.iterator(); it.hasNext(); ) {
            MethodInvocation meth = (MethodInvocation) it.next();
            //check against each dangerous method in class
            for (Iterator it2 = getCurrentEvalPackage().allMethodsOfClass.keySet().iterator(); it2.hasNext(); ) {
               MethodHolder h = (MethodHolder) it2.next();
               if (h.isDangerous()) {
                  String methName = h.getASTMethodDeclarator().getImage();
                  int count = h.getASTMethodDeclarator().getParameterCount();
                  if (meth.getName().equals(methName) && (meth.getArgumentCount() == count)) {
                     //bad call
                     RuleContext ctx = (RuleContext) data;
                     ctx.getReport().addRuleViolation(createRuleViolation(ctx, meth.getASTPrimaryExpression().getBeginLine()));
                  }
               }
            }
         }
         //get each unsafe private constructor, and check if its called from any non private constructors
         for (Iterator privConstIter = getCurrentEvalPackage().allPrivateConstructorsOfClass.keySet().iterator(); privConstIter.hasNext(); ) {
            ConstructorHolder ch = (ConstructorHolder) privConstIter.next();
            if (ch.isDangerous()) {
               //if its dangerous check if its called from any non-private constructors
               //System.out.println("visitClassDec Evaluating dangerous constructor with " + ch.getASTConstructorDeclaration().getParameterCount() + " params");
               int paramCount = ch.getASTConstructorDeclaration().getParameterCount();
               for (Iterator calledConstIter = getCurrentEvalPackage().calledConstructors.iterator(); calledConstIter.hasNext(); ) {
                  ConstructorInvocation ci = (ConstructorInvocation) calledConstIter.next();
                  //System.out.println("called constructor ci.getArgumentCount()="+ci.getArgumentCount());
                  if (ci.getArgumentCount() == paramCount) {
                     //match name  super / this !?
                     //System.out.println("adding report");
                     RuleContext ctx = (RuleContext) data;
                     ctx.getReport().addRuleViolation(createRuleViolation(ctx, ci.getASTExplicitConstructorInvocation().getBeginLine()));
                  }
               }
            }
         }
      }
      //finished evaluating this class, move up a level
      removeCurrentEvalPackage();
      return data;
   }


   /**
    *  Check the methods called on this class by each of the methods on this class. If a method calls an unsafe method,
    *  mark the calling method as unsafe. This changes the list of unsafe methods which necessitates another pass. Keep
    *  passing until you make a clean pass in which no methods are changed to unsafe. For speed it is possible to limit
    *  the number of passes. Impossible to tell type of arguments to method, so forget method matching on types. just
    *  use name and num of arguments. will be some false hits, but oh well.
    *
    *@param  classMethodMap  Description of Parameter
    *@return                 Description of the Returned Value
    *@todo                   investigate limiting the number of passes through config.
    */
   private boolean evaluateDangerOfMethods(Map classMethodMap) {
      //check each method if it calls overridable method
      boolean found = false;
      for (Iterator methodsIter = classMethodMap.keySet().iterator(); methodsIter.hasNext(); ) {
         MethodHolder h = (MethodHolder) methodsIter.next();
         List calledMeths = (List) classMethodMap.get(h);
         for (Iterator calledMethsIter = calledMeths.iterator(); calledMethsIter.hasNext() && (h.isDangerous() == false); ) {
            //if this method matches one of our dangerous methods, mark it dangerous
            MethodInvocation meth = (MethodInvocation) calledMethsIter.next();
            //System.out.println("Called meth is " + meth);
            for (Iterator innerMethsIter = classMethodMap.keySet().iterator(); innerMethsIter.hasNext(); ) {
               //need to skip self here h == h3
               MethodHolder h3 = (MethodHolder) innerMethsIter.next();
               if (h3.isDangerous()) {
                  String matchMethodName = h3.getASTMethodDeclarator().getImage();
                  int matchMethodParamCount = h3.getASTMethodDeclarator().getParameterCount();
                  //System.out.println("matchint " + matchMethodName + " to " + meth.getName());
                  if (matchMethodName.equals(meth.getName()) && (matchMethodParamCount == meth.getArgumentCount())) {
                     h.setDangerous(true);
                     found = true;
                     break;
                  }
               }
            }
         }
      }
      return found;
   }


   /**
    *  marks constructors dangerous if they call any dangerous methods Requires only a single pass as methods are
    *  already marked
    *
    *@param  classConstructorMap  Description of Parameter
    *@param  evaluatedMethods     Description of Parameter
    *@todo                        optimize by having methods already evaluated somehow!?
    */
   private void evaluateDangerOfConstructors1(Map classConstructorMap, Set evaluatedMethods) {
      //check each constructor in the class
      for (Iterator constIter = classConstructorMap.keySet().iterator(); constIter.hasNext(); ) {
         ConstructorHolder ch = (ConstructorHolder) constIter.next();
         if (!ch.isDangerous()) {
            //if its not dangerous then evaluate if it should be
            //if it calls dangerous method mark it as dangerous
            List calledMeths = (List) classConstructorMap.get(ch);
            //check each method it calls
            for (Iterator calledMethsIter = calledMeths.iterator(); calledMethsIter.hasNext() && !ch.isDangerous(); ) {
               //but thee are diff objects which represent same thing but were never evaluated, they need reevaluation
               MethodInvocation meth = (MethodInvocation) calledMethsIter.next();
               //CCE
               String methName = meth.getName();
               int methArgCount = meth.getArgumentCount();
               //check each of the already evaluated methods: need to optimize this out
               for (Iterator evaldMethsIter = evaluatedMethods.iterator(); evaldMethsIter.hasNext(); ) {
                  MethodHolder h = (MethodHolder) evaldMethsIter.next();
                  if (h.isDangerous()) {
                     String matchName = h.getASTMethodDeclarator().getImage();
                     int matchParamCount = h.getASTMethodDeclarator().getParameterCount();
                     if (methName.equals(matchName) && (methArgCount == matchParamCount)) {
                        ch.setDangerous(true);
                        //System.out.println("evaluateDangerOfConstructors1 setting dangerous constructor with " + ch.getASTConstructorDeclaration().getParameterCount() + " params");
                        break;
                     }
                  }
               }
            }
         }
      }
   }


   /**
    *  Constructor map should contain a key for each private constructor, and maps to a List which contains all called
    *  constructors of that key. marks dangerous if call dangerous private constructor we ignore all non-private
    *  constructors here. That is, the map passed in should not contain any non-private constructors. we return boolean
    *  in order to limit the number of passes through this method but it seems as if we can forgo that and just process
    *  it till its done.
    *
    *@param  classConstructorMap  Description of Parameter
    *@return                      Description of the Returned Value
    */
   private boolean evaluateDangerOfConstructors2(Map classConstructorMap) {
      //System.out.println("evaluateDangerOfConstructors2 map="+classConstructorMap);
      boolean found = false;
      //triggers on danger state change
      //check each constructor in the class
      for (Iterator constIter = classConstructorMap.keySet().iterator(); constIter.hasNext(); ) {
         ConstructorHolder ch = (ConstructorHolder) constIter.next();
         ConstructorInvocation calledC = ch.getCalledConstructor();
         if (calledC == null || ch.isDangerous()) {
            continue;
         }
         //if its not dangerous then evaluate if it should be
         //if it calls dangerous constructor mark it as dangerous
         int cCount = calledC.getArgumentCount();
         for (Iterator innerConstIter = classConstructorMap.keySet().iterator(); innerConstIter.hasNext() && !ch.isDangerous(); ) {
            //forget skipping self because that introduces another check for each, but only 1 hit
            ConstructorHolder h2 = (ConstructorHolder) innerConstIter.next();
            if (h2.isDangerous()) {
               int matchConstArgCount = h2.getASTConstructorDeclaration().getParameterCount();
               if (matchConstArgCount == cCount) {
                  ch.setDangerous(true);
                  found = true;
                  //System.out.println("evaluateDangerOfConstructors2 setting dangerous constructor with " + ch.getASTConstructorDeclaration().getParameterCount() + " params");
               }
            }
         }
      }
      return found;
   }


   /**
    *  ASTPrimaryPrefix has name in child node of ASTName
    *
    *@param  node  Description of Parameter
    *@return       The NameFromPrefix value
    */
   private static String getNameFromPrefix(ASTPrimaryPrefix node) {
      String name = "";
      //should only be 1 child, if more I need more knowledge
      if (node.jjtGetNumChildren() == 1) {
         //safety check
         Node nnode = node.jjtGetFirstChild();
         if (nnode instanceof ASTName) {
            //just as easy as null check and it should be an ASTName anyway
            name = ((ASTName) nnode).getImage();
         } else if (nnode instanceof ASTExpression) {
            //FIXME? not sure this is correct
            List expressions = new ArrayList();
            node.findChildrenOfType(ASTName.class, expressions);
            if (expressions.size() > 0) {
               name = ((ASTName) expressions.get(0)).getImage();
            }
         }
      }
      return name;
   }


   /**
    *  ASTPrimarySuffix has name in itself
    *
    *@param  node  Description of Parameter
    *@return       The NameFromSuffix value
    */
   private static String getNameFromSuffix(ASTPrimarySuffix node) {
      return node.getImage();
   }



   ////////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////////
   //Helper methods to process visits

   /**
    *  Adds a feature to the CalledMethodsOfNode attribute of the ConstructorCallsOverridableMethodRule class
    *
    *@param  node           The feature to be added to the CalledMethodsOfNode attribute
    *@param  calledMethods  The feature to be added to the CalledMethodsOfNode attribute
    *@param  className      The feature to be added to the CalledMethodsOfNode attribute
    */
   private static void addCalledMethodsOfNode(AccessNode node, List calledMethods, String className) {
      List expressions = new ArrayList();
      node.findChildrenOfType(ASTPrimaryExpression.class, expressions, false);
      addCalledMethodsOfNodeImpl(expressions, calledMethods, className);
   }


   /**
    *  Adds all methods called on this instance from within this Node.
    *
    *@param  node           The feature to be added to the CalledMethodsOfNode attribute
    *@param  calledMethods  The feature to be added to the CalledMethodsOfNode attribute
    *@param  className      The feature to be added to the CalledMethodsOfNode attribute
    */
   private static void addCalledMethodsOfNode(SimpleNode node, List calledMethods, String className) {
      List expressions = new ArrayList();
      node.findChildrenOfType(ASTPrimaryExpression.class, expressions);
      addCalledMethodsOfNodeImpl(expressions, calledMethods, className);
   }


   /**
    *  Adds a feature to the CalledMethodsOfNodeImpl attribute of the ConstructorCallsOverridableMethodRule class
    *
    *@param  expressions    The feature to be added to the CalledMethodsOfNodeImpl attribute
    *@param  calledMethods  The feature to be added to the CalledMethodsOfNodeImpl attribute
    *@param  className      The feature to be added to the CalledMethodsOfNodeImpl attribute
    */
   private static void addCalledMethodsOfNodeImpl(List expressions, List calledMethods, String className) {
      for (Iterator it = expressions.iterator(); it.hasNext(); ) {
         ASTPrimaryExpression ape = (ASTPrimaryExpression) it.next();
         MethodInvocation meth = findMethod(ape, className);
         if (meth != null) {
            //System.out.println("Adding call " + methName);
            calledMethods.add(meth);
         }
      }
   }


   /**
    *@param  node       Description of Parameter
    *@param  className  Description of Parameter
    *@return            A method call on the class passed in, or null if no method call is found.
    *@todo              Need a better way to match the class and package name to the actual method being called.
    */
   private static MethodInvocation findMethod(ASTPrimaryExpression node, String className) {
      if (node.jjtGetNumChildren() > 0
            && node.jjtGetFirstChild().jjtGetNumChildren() > 0
            && node.jjtGetFirstChild().jjtGetFirstChild() instanceof ASTLiteral) {
         return null;
      }
      MethodInvocation meth = MethodInvocation.getMethod(node);
      boolean found = false;
      //		if(meth != null){
      //			meth.show();
      //		}
      if (meth != null) {
         //if it's a call on a variable, or on its superclass ignore it.
         if ((meth.getReferenceNames().size() == 0) && !meth.isSuper()) {
            //if this list does not contain our class name, then its not referencing our class
            //this is a cheezy test... but it errs on the side of less false hits.
            List packClass = meth.getQualifierNames();
            if (packClass.size() > 0) {
               for (Iterator it = packClass.iterator(); it.hasNext(); ) {
                  String name = (String) it.next();
                  if (name.equals(className)) {
                     found = true;
                     break;
                  }
               }
            } else {
               found = true;
            }
         }
      }
      if (found) {
         return meth;
      } else {
         return null;
      }
   }


   /**
    *  Description of the Class
    *
    *@author    Mike Atkinson
    */
   private final class ConstructorInvocation {
      private ASTExplicitConstructorInvocation m_Eci;
      private String name;
      private int count = 0;


      /**
       *  Constructor for the ConstructorInvocation object
       *
       *@param  eci  Description of Parameter
       */
      public ConstructorInvocation(ASTExplicitConstructorInvocation eci) {
         m_Eci = eci;
         List l = new ArrayList();
         eci.findChildrenOfType(ASTArguments.class, l);
         if (l.size() > 0) {
            ASTArguments aa = (ASTArguments) l.get(0);
            count = aa.getArgumentCount();
         }
         name = eci.getImage();
      }


      /**
       *  Gets the ASTExplicitConstructorInvocation attribute of the ConstructorInvocation object
       *
       *@return    The ASTExplicitConstructorInvocation value
       */
      public ASTExplicitConstructorInvocation getASTExplicitConstructorInvocation() {
         return m_Eci;
      }


      /**
       *  Gets the ArgumentCount attribute of the ConstructorInvocation object
       *
       *@return    The ArgumentCount value
       */
      public int getArgumentCount() {
         return count;
      }


      /**
       *  Gets the Name attribute of the ConstructorInvocation object
       *
       *@return    The Name value
       */
      public String getName() {
         return name;
      }
   }


   /**
    *  Description of the Class
    *
    *@author    Mike Atkinson
    */
   private final class MethodHolder {
      private ASTMethodDeclarator m_Amd;
      private boolean m_Dangerous = false;


      /**
       *  Constructor for the MethodHolder object
       *
       *@param  amd  Description of Parameter
       */
      public MethodHolder(ASTMethodDeclarator amd) {
         m_Amd = amd;
      }


      /**
       *  Sets the Dangerous attribute of the MethodHolder object
       *
       *@param  dangerous  The new Dangerous value
       */
      public void setDangerous(boolean dangerous) {
         m_Dangerous = dangerous;
      }


      /**
       *  Gets the ASTMethodDeclarator attribute of the MethodHolder object
       *
       *@return    The ASTMethodDeclarator value
       */
      public ASTMethodDeclarator getASTMethodDeclarator() {
         return m_Amd;
      }


      /**
       *  Gets the Dangerous attribute of the MethodHolder object
       *
       *@return    The Dangerous value
       */
      public boolean isDangerous() {
         return m_Dangerous;
      }
   }


   /**
    *  Description of the Class
    *
    *@author    Mike Atkinson
    */
   private final class ConstructorHolder {
      private ASTConstructorDeclaration m_Cd;
      private boolean m_Dangerous = false;
      private ConstructorInvocation m_Ci;
      private boolean m_CiInitialized = false;


      /**
       *  Constructor for the ConstructorHolder object
       *
       *@param  cd  Description of Parameter
       */
      public ConstructorHolder(ASTConstructorDeclaration cd) {
         m_Cd = cd;
      }


      /**
       *  Sets the Dangerous attribute of the ConstructorHolder object
       *
       *@param  dangerous  The new Dangerous value
       */
      public void setDangerous(boolean dangerous) {
         m_Dangerous = dangerous;
      }


      /**
       *  Gets the ASTConstructorDeclaration attribute of the ConstructorHolder object
       *
       *@return    The ASTConstructorDeclaration value
       */
      public ASTConstructorDeclaration getASTConstructorDeclaration() {
         return m_Cd;
      }


      /**
       *  Gets the CalledConstructor attribute of the ConstructorHolder object
       *
       *@return    The CalledConstructor value
       */
      public ConstructorInvocation getCalledConstructor() {
         if (m_CiInitialized == false) {
            initCI();
         }
         return m_Ci;
      }


      /**
       *  Gets the ASTExplicitConstructorInvocation attribute of the ConstructorHolder object
       *
       *@return    The ASTExplicitConstructorInvocation value
       */
      public ASTExplicitConstructorInvocation getASTExplicitConstructorInvocation() {
         ASTExplicitConstructorInvocation eci = null;
         if (m_CiInitialized == false) {
            initCI();
         }
         if (m_Ci != null) {
            eci = m_Ci.getASTExplicitConstructorInvocation();
         }
         return eci;
      }


      /**
       *  Gets the Dangerous attribute of the ConstructorHolder object
       *
       *@return    The Dangerous value
       */
      public boolean isDangerous() {
         return m_Dangerous;
      }


      /**  Description of the Method */
      private void initCI() {
         List expressions = new ArrayList();
         m_Cd.findChildrenOfType(ASTExplicitConstructorInvocation.class, expressions);
         //only 1...
         if (expressions.size() > 0) {
            ASTExplicitConstructorInvocation eci = (ASTExplicitConstructorInvocation) expressions.get(0);
            m_Ci = new ConstructorInvocation(eci);
            //System.out.println("Const call " + eci.getImage()); //super or this???
         }
         m_CiInitialized = true;
      }
   }


   /**
    *  2: method(); ASTPrimaryPrefix ASTName image = "method" ASTPrimarySuffix *ASTArguments 3: a.method();
    *  ASTPrimaryPrefix -> ASTName image = "a.method" ??? ASTPrimarySuffix -> () ASTArguments 3: this.method();
    *  ASTPrimaryPrefix -> this image=null //MRA image=="this" ASTPrimarySuffix -> method ASTPrimarySuffix -> ()
    *  ASTArguments super.method(); ASTPrimaryPrefix -> image = "method" //MRA ASTPrimaryPrefix -> image =
    *  "super.method" ASTPrimarySuffix -> image = null ASTArguments -> super.a.method(); ASTPrimaryPrefix -> image = "a"
    *  //MRA ASTPrimaryPrefix -> image = "super.a" ASTPrimarySuffix -> image = "method" ASTPrimarySuffix -> image = null
    *  ASTArguments -> 4: this.a.method(); ASTPrimaryPrefix -> image = null ASTPrimarySuffix -> image = "a"
    *  ASTPrimarySuffix -> image = "method" ASTPrimarySuffix -> ASTArguments 4: ClassName.this.method();
    *  ASTPrimaryPrefix ASTName image = "ClassName" ASTPrimarySuffix -> this image=null ASTPrimarySuffix -> image =
    *  "method" ASTPrimarySuffix -> () ASTArguments 5: ClassName.this.a.method(); ASTPrimaryPrefix ASTName image =
    *  "ClassName" ASTPrimarySuffix -> this image=null ASTPrimarySuffix -> image="a" ASTPrimarySuffix -> image="method"
    *  ASTPrimarySuffix -> () ASTArguments 5: Package.ClassName.this.method(); ASTPrimaryPrefix ASTName image
    *  ="Package.ClassName" ASTPrimarySuffix -> this image=null ASTPrimarySuffix -> image="method" ASTPrimarySuffix ->
    *  () ASTArguments 6: Package.ClassName.this.a.method(); ASTPrimaryPrefix ASTName image ="Package.ClassName"
    *  ASTPrimarySuffix -> this image=null ASTPrimarySuffix -> a ASTPrimarySuffix -> method ASTPrimarySuffix -> ()
    *  ASTArguments 5: OuterClass.InnerClass.this.method(); ASTPrimaryPrefix ASTName image = "OuterClass.InnerClass"
    *  ASTPrimarySuffix -> this image=null ASTPrimarySuffix -> method ASTPrimarySuffix -> () ASTArguments 6:
    *  OuterClass.InnerClass.this.a.method(); ASTPrimaryPrefix ASTName image = "OuterClass.InnerClass" ASTPrimarySuffix
    *  -> this image=null ASTPrimarySuffix -> a ASTPrimarySuffix -> method ASTPrimarySuffix -> () ASTArguments
    *  OuterClass.InnerClass.this.a.method().method().method(); ASTPrimaryPrefix ASTName image = "OuterClass.InnerClass"
    *  ASTPrimarySuffix -> this image=null ASTPrimarySuffix -> a image='a' ASTPrimarySuffix -> method image='method'
    *  ASTPrimarySuffix -> () image=null ASTArguments ASTPrimarySuffix -> method image='method' ASTPrimarySuffix -> ()
    *  image=null ASTArguments ASTPrimarySuffix -> method image='method' ASTPrimarySuffix -> () image=null ASTArguments
    *  3..n: Class.InnerClass[0].InnerClass[n].this.method(); ASTPrimaryPrefix ASTName image = "Class[0]..InnerClass[n]"
    *  ASTPrimarySuffix -> image=null ASTPrimarySuffix -> method ASTPrimarySuffix -> () ASTArguments super.aMethod();
    *  ASTPrimaryPrefix -> aMethod ASTPrimarySuffix -> () Evaluate right to left
    *
    *@author    Mike Atkinson
    */
   private static class MethodInvocation {
      private String m_Name;
      private ASTPrimaryExpression m_Ape;
      private List m_ReferenceNames;
      private List m_QualifierNames;
      private int m_ArgumentSize;
      private boolean m_Super;


      /**
       *  Constructor for the MethodInvocation object
       *
       *@param  ape             Description of Parameter
       *@param  qualifierNames  Description of Parameter
       *@param  referenceNames  Description of Parameter
       *@param  name            Description of Parameter
       *@param  argumentSize    Description of Parameter
       *@param  superCall       Description of Parameter
       */
      private MethodInvocation(ASTPrimaryExpression ape, List qualifierNames, List referenceNames, String name, int argumentSize, boolean superCall) {
         m_Ape = ape;
         m_QualifierNames = qualifierNames;
         m_ReferenceNames = referenceNames;
         m_Name = name;
         m_ArgumentSize = argumentSize;
         m_Super = superCall;
      }


      /**
       *  Gets the Super attribute of the MethodInvocation object
       *
       *@return    The Super value
       */
      public boolean isSuper() {
         return m_Super;
      }


      /**
       *  Gets the Name attribute of the MethodInvocation object
       *
       *@return    The Name value
       */
      public String getName() {
         return m_Name;
      }


      /**
       *  Gets the ArgumentCount attribute of the MethodInvocation object
       *
       *@return    The ArgumentCount value
       */
      public int getArgumentCount() {
         return m_ArgumentSize;
      }


      /**
       *  Gets the ReferenceNames attribute of the MethodInvocation object
       *
       *@return    The ReferenceNames value
       */
      public List getReferenceNames() {
         return m_ReferenceNames;
         //new ArrayList(variableNames);
      }


      /**
       *  Gets the QualifierNames attribute of the MethodInvocation object
       *
       *@return    The QualifierNames value
       */
      public List getQualifierNames() {
         return m_QualifierNames;
      }


      /**
       *  Gets the ASTPrimaryExpression attribute of the MethodInvocation object
       *
       *@return    The ASTPrimaryExpression value
       */
      public ASTPrimaryExpression getASTPrimaryExpression() {
         return m_Ape;
      }


      /**  Description of the Method */
      public void show() {
         System.out.println("<MethodInvocation>");
         List pkg = getQualifierNames();
         System.out.println("  <Qualifiers>");
         for (Iterator it = pkg.iterator(); it.hasNext(); ) {
            String name = (String) it.next();
            System.out.println("    " + name);
         }
         System.out.println("  </Qualifiers>");
         System.out.println("  <Super>" + isSuper() + "</Super>");
         List vars = getReferenceNames();
         System.out.println("  <References>");
         for (Iterator it = vars.iterator(); it.hasNext(); ) {
            String name = (String) it.next();
            System.out.println("    " + name);
         }
         System.out.println("  </References>");
         System.out.println("  <Name>" + getName() + "</Name>");
         System.out.println("</MethodInvocation>");
      }


      /**
       *  Gets the Method attribute of the MethodInvocation class
       *
       *@param  node  Description of Parameter
       *@return       The Method value
       */
      public static MethodInvocation getMethod(ASTPrimaryExpression node) {
         MethodInvocation meth = null;
         int i = node.jjtGetNumChildren();
         if (i > 1) {
            //should always be at least 2, probably can eliminate this check
            //start at end which is guaranteed, work backwards
            Node lastNode = node.jjtGetChild(i - 1);
            if ((lastNode.jjtGetNumChildren() == 1) && (lastNode.jjtGetFirstChild() instanceof ASTArguments)) {
               //could be ASTExpression for instance 'a[4] = 5';
               //start putting method together
               //					System.out.println("Putting method together now");
               List varNames = new ArrayList();
               List packagesAndClasses = new ArrayList();
               //look in JLS for better name here;
               String methodName = null;
               ASTArguments args = (ASTArguments) lastNode.jjtGetFirstChild();
               int numOfArguments = args.getArgumentCount();
               boolean superFirst = false;
               int thisIndex = -1;

               FIND_SUPER_OR_THIS:
                {
                  //search all nodes except last for 'this' or 'super'.  will be at: (node 0 | node 1 | nowhere)
                  //this is an ASTPrimarySuffix with a null image and does not have child (which will be of type ASTArguments)
                  //this is an ASTPrimaryPrefix with a null image and an ASTName that has a null image
                  //super is an ASTPrimarySuffix with a null image and does not have child (which will be of type ASTArguments)
                  //super is an ASTPrimaryPrefix with a non-null image
                  for (int x = 0; x < i - 1; x++) {
                     Node child = node.jjtGetChild(x);
                     if (child instanceof ASTPrimarySuffix) {
                        //check suffix type match
                        ASTPrimarySuffix child2 = (ASTPrimarySuffix) child;
                        //								String name = getNameFromSuffix((ASTPrimarySuffix)child);
                        //								System.out.println("found name suffix of : " + name);
                        //MRA if (child2.getImage() == null && child2.jjtGetNumChildren() == 0) {
                        if ("this".equals(child2.getImage())) {
                           thisIndex = x;
                           break;
                        }
                        //could be super, could be this.  currently we cant tell difference so we miss super when
                        //XYZ.ClassName.super.method();
                        //still works though.
                     } else if (child instanceof ASTPrimaryPrefix) {
                        //check prefix type match
                        ASTPrimaryPrefix child2 = (ASTPrimaryPrefix) child;
                        //MRA if (getNameFromPrefix(child2) == null) {
                        //MRA     if (child2.getImage() == null) {
                        //MRA         thisIndex = x;
                        //MRA         break;
                        //MRA     } else {//happens when super is used [super.method(): image = 'method']
                        //MRA         superFirst = true;
                        //MRA         thisIndex = x;
                        //MRA         //the true super is at an unusable index because super.method() has only 2 nodes [method=0,()=1]
                        //MRA         //as opposed to the 3 you might expect and which this.method() actually has. [this=0,method=1.()=2]
                        //MRA         break;
                        //MRA     }
                        //MRA }
                        if ("this".equals(child2)) {
                           thisIndex = x;
                           break;
                        } else if (child2.getImage() != null && child2.getImage().startsWith("super.")) {
                           superFirst = true;
                           thisIndex = x;
                           break;
                           //the true super is at an unusable index because super.method() has only 2 nodes [method=0,()=1]
                           //as opposed to the 3 you might expect and which this.method() actually has. [this=0,method=1.()=2]
                        }
                     }
                     //							else{
                     //								System.err.println("Bad Format error"); //throw exception, quit evaluating this compilation node
                     //							}
                  }
               }

               if (thisIndex != -1) {
                  //						System.out.println("Found this or super: " + thisIndex);
                  //Hack that must be removed if and when the patters of super.method() begins to logically match the rest of the patterns !!!
                  if (superFirst) {
                     //this is when super is the first node of statement.  no qualifiers, all variables or method
                     //							System.out.println("super first");
                     FIRSTNODE:
                      {
                        ASTPrimaryPrefix child = (ASTPrimaryPrefix) node.jjtGetFirstChild();
                        String name = child.getImage();
                        //special case
                        if (i == 2) {
                           //last named node = method name
                           methodName = name;
                        } else {
                           //not the last named node so its only var name
                           varNames.add(name);
                        }
                     }
                     OTHERNODES:
                      {
                        //variables
                        for (int x = 1; x < i - 1; x++) {
                           Node child = node.jjtGetChild(x);
                           ASTPrimarySuffix ps = (ASTPrimarySuffix) child;
                           if (ps.isArguments() == false) {
                              String name = ((ASTPrimarySuffix) child).getImage();
                              if (x == i - 2) {
                                 //last node
                                 methodName = name;
                              } else {
                                 //not the last named node so its only var name
                                 varNames.add(name);
                              }
                           }
                        }
                     }
                  } else {
                     //not super call
                     FIRSTNODE:
                      {
                        if (thisIndex == 1) {
                           //qualifiers in node 0
                           ASTPrimaryPrefix child = (ASTPrimaryPrefix) node.jjtGetFirstChild();
                           String toParse = getNameFromPrefix(child);
                           //									System.out.println("parsing for class/package names in : " + toParse);
                           java.util.StringTokenizer st = new java.util.StringTokenizer(toParse, ".");
                           while (st.hasMoreTokens()) {
                              packagesAndClasses.add(st.nextToken());
                           }
                        }
                     }
                     OTHERNODES:
                      {
                        //other methods called in this statement are grabbed here
                        //this is at 0, then no Qualifiers
                        //this is at 1, the node 0 contains qualifiers
                        for (int x = thisIndex + 1; x < i - 1; x++) {
                           //everything after this is var name or method name
                           ASTPrimarySuffix child = (ASTPrimarySuffix) node.jjtGetChild(x);
                           if (child.isArguments() == false) {
                              //skip the () of method calls
                              String name = getNameFromSuffix(child);
                              //										System.out.println("Found suffix: " + name);
                              if (x == i - 2) {
                                 methodName = name;
                              } else {
                                 varNames.add(name);
                              }
                           }
                        }
                     }
                  }
               } else {
                  //if no this or super found, everything is method name or variable
                  //System.out.println("no this found:");
                  FIRSTNODE:
                   {
                     //variable names are in the prefix + the first method call [a.b.c.x()]
                     ASTPrimaryPrefix child = (ASTPrimaryPrefix) node.jjtGetFirstChild();
                     String toParse = getNameFromPrefix(child);
                     //							System.out.println("parsing for var names in : " + toParse);
                     java.util.StringTokenizer st = new java.util.StringTokenizer(toParse, ".");
                     while (st.hasMoreTokens()) {
                        String value = st.nextToken();
                        if (!st.hasMoreTokens()) {
                           if (i == 2) {
                              //if this expression is 2 nodes long, then the last part of prefix is method name
                              methodName = value;
                           } else {
                              varNames.add(value);
                           }
                        } else {
                           //variable name
                           varNames.add(value);
                        }
                     }
                  }
                  OTHERNODES:
                   {
                     //other methods called in this statement are grabbed here
                     for (int x = 1; x < i - 1; x++) {
                        ASTPrimarySuffix child = (ASTPrimarySuffix) node.jjtGetChild(x);
                        if (child.isArguments() == false) {
                           String name = getNameFromSuffix(child);
                           if (x == i - 2) {
                              methodName = name;
                           } else {
                              varNames.add(name);
                           }
                        }
                     }
                  }
               }
               meth = new MethodInvocation(node, packagesAndClasses, varNames, methodName, numOfArguments, superFirst);
            }
         }
         return meth;
      }
   }


   /**
    *  1 package per class. holds info for evaluating a single class.
    *
    *@author    Mike Atkinson
    */
   private static class EvalPackage {

      /**  Description of the Field */
      public String m_ClassName;
      /**  Description of the Field */
      public List calledMethods;
      /**  Description of the Field */
      public Map allMethodsOfClass;

      /**  Description of the Field */
      public List calledConstructors;
      /**  Description of the Field */
      public Map allPrivateConstructorsOfClass;


      /**  Constructor for the EvalPackage object */
      public EvalPackage() { }


      /**
       *  Constructor for the EvalPackage object
       *
       *@param  className  Description of Parameter
       */
      public EvalPackage(String className) {
         m_ClassName = className;
         calledMethods = new ArrayList();
         //meths called from constructor
         allMethodsOfClass = new HashMap();
         calledConstructors = new ArrayList();
         //all constructors called from constructor
         allPrivateConstructorsOfClass = new HashMap();
      }
   }


   /**
    *  Description of the Class
    *
    *@author    Mike Atkinson
    */
   private final static class NullEvalPackage extends EvalPackage {
      /**  Constructor for the NullEvalPackage object */
      public NullEvalPackage() {
         m_ClassName = "";
         calledMethods = Collections.EMPTY_LIST;
         allMethodsOfClass = Collections.EMPTY_MAP;
         calledConstructors = Collections.EMPTY_LIST;
         allPrivateConstructorsOfClass = Collections.EMPTY_MAP;
      }
   }
}

