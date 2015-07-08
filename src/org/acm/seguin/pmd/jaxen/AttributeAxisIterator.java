package org.acm.seguin.pmd.jaxen;

import org.acm.seguin.parser.Node;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;


/**
 * Description of the Class
 *
 *@author    mikea
 *@created   September 19, 2003
 */
public class AttributeAxisIterator implements Iterator {

   private final static Object[] EMPTY_OBJ_ARRAY = new Object[0];
   private Object currObj;
   private Method[] methods;
   private int position;
   private Node node;

   /**
    * Constructor for the AttributeAxisIterator object
    *
    *@param contextNode  Description of the Parameter
    */
   public AttributeAxisIterator(Node contextNode) {
      this.node = contextNode;
      this.methods = contextNode.getClass().getMethods();
      // FIXME? MRA this is a hack to get these attributes in the right order
      java.util.List methodsList = new java.util.ArrayList(java.util.Arrays.asList(this.methods));
      java.util.List newMA = new java.util.ArrayList();
      for (Iterator i = methodsList.iterator(); i.hasNext(); ) {
         Method method = (Method) i.next();
         String name = method.getName();
         Class returnType = method.getReturnType();
         if ((method.getParameterTypes().length == 0) && (Void.TYPE != returnType)) {
            if (name.equals("getBeginLine")) {
               i.remove();
               newMA.add(method);
            }
         }
      }
      
      for (Iterator i = methodsList.iterator(); i.hasNext(); ) {
         Method method = (Method) i.next();
         String name = method.getName();
         Class returnType = method.getReturnType();
         if ((method.getParameterTypes().length == 0) && (Void.TYPE != returnType)) {
            if (name.equals("getBeginColumn")) {
               i.remove();
               newMA.add(method);
            }
         }
      }
      for (Iterator i = methodsList.iterator(); i.hasNext(); ) {
         Method method = (Method) i.next();
         String name = method.getName();
         Class returnType = method.getReturnType();
         if ((method.getParameterTypes().length == 0) && (Void.TYPE != returnType)) {
            if (name.equals("getEndLine")) {
               i.remove();
               newMA.add(method);
            }
         }
      }
      for (Iterator i = methodsList.iterator(); i.hasNext(); ) {
         Method method = (Method) i.next();
         String name = method.getName();
         Class returnType = method.getReturnType();
         if ((method.getParameterTypes().length == 0) && (Void.TYPE != returnType)) {
            if (name.equals("getEndColumn")) {
               i.remove();
               newMA.add(method);
            }
         }
      }
      newMA.addAll(methodsList);
      this.methods = (Method[]) newMA.toArray(this.methods);
      this.position = 0;
      this.currObj = getNextAttribute();
   }

   /**
    * Description of the Method
    *
    *@return   Description of the Return Value
    */
   public Object next() {
      if (currObj == null) {
         throw new IndexOutOfBoundsException();
      }
      Object ret = currObj;
      currObj = getNextAttribute();
      return ret;
   }

   /**
    * Description of the Method
    *
    *@return   Description of the Return Value
    */
   public boolean hasNext() {
      return currObj != null;
   }

   /**
    * Description of the Method
    */
   public void remove() {
      throw new UnsupportedOperationException();
   }

   /**
    * Gets the nextAttribute attribute of the AttributeAxisIterator object
    *
    *@return   The nextAttribute value
    */
   private Attribute getNextAttribute() {
      while (position < methods.length) {
         Method method = methods[position];
         try {
            if (isAttribute(method)) {
               Class returnType = method.getReturnType();
               if (Boolean.TYPE == returnType
                     || String.class == returnType
                     || Integer.TYPE == returnType) {
                  Attribute attribute = getAttribute(node, method);
                  if (attribute != null) {
                     return attribute;
                  }
               }
            }
         } catch (IllegalAccessException e) {
            e.printStackTrace();
         } catch (InvocationTargetException e) {
            e.printStackTrace();
         } finally {
            position++;
         }
      }
      return null;
   }

   /**
    * Gets the attribute attribute of the AttributeAxisIterator object
    *
    *@param node                           Description of the Parameter
    *@param method                         Description of the Parameter
    *@return                               The attribute value
    *@exception IllegalAccessException     Description of the Exception
    *@exception InvocationTargetException  Description of the Exception
    */
   protected Attribute getAttribute(Node node, Method method)
          throws IllegalAccessException, InvocationTargetException {
      String name = method.getName();
      name = truncateMethodName(name);
      Object value = method.invoke(node, EMPTY_OBJ_ARRAY);
      if (value != null) {
         if (value instanceof String) {
            return new Attribute(node, name, (String) value);
         } else {
            return new Attribute(node, name, String.valueOf(value));
         }
      } else {
         return null;
      }
   }

   /**
    * Description of the Method
    *
    *@param name  Description of the Parameter
    *@return      Description of the Return Value
    */
   protected String truncateMethodName(String name) {
      String truncatedName = null;
      if (name.startsWith("is")) {
         truncatedName = name.substring("is".length());
      } else if (name.startsWith("uses")) {
         truncatedName = name.substring("uses".length());
      } else if (name.startsWith("has")) {
         truncatedName = name.substring("has".length());
      } else if (name.startsWith("get")) {
         truncatedName = name.substring("get".length());
      } else {
         truncatedName = name;
      }
      return truncatedName;
   }

   /**
    * Gets the attribute attribute of the AttributeAxisIterator object
    *
    *@param method  Description of the Parameter
    *@return        The attribute value
    */
   protected boolean isAttribute(Method method) {
      String name = method.getName();
      Class returnType = method.getReturnType();
      return (method.getParameterTypes().length == 0)
            && (Void.TYPE != returnType)
            && !name.startsWith("jjt")
            && !name.equals("toString")
            && !name.equals("getScope")
            && !name.equals("getClass")
            && !name.equals("getFinallyBlock")
            && !name.equals("getCatchBlocks")
            && !name.equals("getTypeNameNode")
            && !name.equals("getImportedNameNode")
            && !name.equals("hashCode")
            // && !name.equals("getImage")// FIXME? MRA
            && !name.equals("hasAnyChildren")  // MRA This attribute should not be visible to XPath
            && !name.equals("isRequired")      // MRA This attribute should not be visible to XPath
            && !name.equals("getName");        // MRA This attribute should not be visible to XPath
   }
}
