/*
 *  Author:  Chris Seguin
 *
 *  This software has been developed under the copyleft
 *  rules of the GNU General Public License.  Please
 *  consult the GNU General Public License for more
 *  details about use and distribution of this software.
 */
package org.acm.seguin.refactor.type;

import java.io.*;
import java.util.*;
import org.acm.seguin.parser.ast.*;
import org.acm.seguin.parser.ast.ModifierHolder;
import org.acm.seguin.refactor.AddImportTransform;
import org.acm.seguin.refactor.ComplexTransform;
import org.acm.seguin.refactor.Refactoring;
import org.acm.seguin.refactor.RefactoringException;
import org.acm.seguin.refactor.method.AddConcreteMethod;
import org.acm.seguin.summary.*;
import org.acm.seguin.summary.query.*;

/**
 *  Refactoring that extracts the interface from the dialog
 *
 *@author     Grant Watson
 *@created    November 27, 2000
 */
public class ExtractInterfaceRefactoring extends Refactoring {
	private String m_interfaceName;
	private String m_packageName;
	private Vector m_summaryList = new Vector();
	private ComplexTransform m_complexTransform;


	/**
	 *  Constructor for the ExtractInterfaceRefactoring object
	 */
	protected ExtractInterfaceRefactoring()
	{
		m_complexTransform = getComplexTransform();
	}


	/**
	 *  Sets the interface name for the new interface. If the name contains a
	 *  package name, then the package name is also set.
	 *
	 *@param  interfaceName  The new InterfaceName value
	 */
	public void setInterfaceName(String interfaceName)
	{
		if (interfaceName.indexOf('.') != -1) {
			m_packageName = interfaceName.substring(0, interfaceName.lastIndexOf('.'));
			m_interfaceName = interfaceName.substring(interfaceName.lastIndexOf('.') + 1);
		}
		else {
			m_interfaceName = interfaceName;
		}
	}


	/**
	 *  Sets the PackageName attribute of the ExtractInterfaceRefactoring object
	 *
	 *@param  packageName  The new PackageName value
	 */
	public void setPackageName(String packageName)
	{
		m_packageName = packageName;
	}


	/**
	 *  Gets the Description attribute of the ExtractInterfaceRefactoring object
	 *
	 *@return    The Description value
	 */
	public String getDescription()
	{
		return "Extract Interface.";
	}


	/**
	 *  Gets the ID attribute of the ExtractInterfaceRefactoring object
	 *
	 *@return    The ID value
	 */
	public int getID()
	{
		return EXTRACT_INTERFACE;
	}


	/**
	 *  Adds a class that will implement the new interface
	 *
	 *@param  packageName  The feature to be added to the ImplementingClass
	 *      attribute
	 *@param  className    The feature to be added to the ImplementingClass
	 *      attribute
	 */
	public void addImplementingClass(String packageName, String className)
	{
		TypeSummary summary = GetTypeSummary.query(PackageSummary.getPackageSummary(packageName), className);
		addImplementingClass(summary);
	}


	/**
	 *  Adds a feature to the ImplementingClass attribute of the
	 *  ExtractInterfaceRefactoring object
	 *
	 *@param  summary  The feature to be added to the ImplementingClass attribute
	 */
	public void addImplementingClass(TypeSummary summary)
	{
		if (summary != null) {
			m_summaryList.addElement(summary);
		}
	}


	/**
	 *  Description of the Method
	 *
	 *@exception  RefactoringException  Description of Exception
	 */
	protected void preconditions() throws RefactoringException
	{
		if (m_interfaceName == null) {
			throw new RefactoringException("Interface name is not specified");
		}
		if (m_summaryList.size() == 0) {
			throw new RefactoringException("Unable to find type to extract interface from");
		}
	}


	/**
	 *  this performs the refactoring
	 */
	protected void transform()
	{
		File newFile = createInterfaceFile();
		// Add declarations of the common methods to the interface
		Vector methodSummaries = getMethodSummaries();
		for (int i = 0; i < methodSummaries.size(); i++) {
			MethodSummary ms = (MethodSummary) methodSummaries.elementAt(i);
			m_complexTransform.add(new AddConcreteMethod(ms));
		}
		// Add necessary import statements to support parameter and return types
		Iterator importTypes = getImportTypes(methodSummaries);
		while ((importTypes != null) && (importTypes.hasNext())) {
			TypeDeclSummary decl = (TypeDeclSummary) importTypes.next();
			TypeSummary type = GetTypeSummary.query(decl);
			// If the type is not found, don't attempt to add an import statement
			if (type != null) {
				m_complexTransform.add(new AddImportTransform(type));
			}
		}
		m_complexTransform.apply(newFile, newFile);
		/*
		 *  Delete the backup file for the intermediate new interface file to
		 *  ensure that an 'undo' does not recover it.
		 */
		newFile = new File(newFile.getAbsolutePath() + ".0");
		newFile.delete();
		addInterfaceToClasses();
	}


	/**
	 *  Gets a list of public method summaries that are common to all classes for
	 *  which an interface is being extracted.
	 *
	 *@return    The MethodSummaries value
	 */
	private Vector getMethodSummaries()
	{
		Vector firstClassMethods = new Vector();
		// Add all relevant methods from the first class.
		TypeSummary ts = (TypeSummary) m_summaryList.elementAt(0);
		Iterator methods = ts.getMethods();
		while (methods.hasNext()) {
			MethodSummary ms = (MethodSummary) methods.next();
			//ModifierHolder mh = ms.getModifiers();
			/*
			 *  Include only public, non-static, non-constructor methods.
			 *  Private and protected methods are not allowed in interfaces and
			 *  methods that are package-protected in an interface need to be
			 *  implemented by public methods in implementing classes (I think).
			 */
			if (ms.isPublic() && (!ms.isConstructor()) && (!ms.isStatic())) {
				// synchronized modifier is not allowed for interfaces.
				ms.setSynchronized(false);
				firstClassMethods.addElement(ms);
			}
		}
		return commonMethods(firstClassMethods);
	}


	/**
	 *  Gets a list of the TypeDeclSummaries for the return types and parameters
	 *  in the list of MethodSummaries supplied.
	 *
	 *@param  methodSummaries  Description of Parameter
	 *@return                  The ImportTypes value
	 */
	private Iterator getImportTypes(Vector methodSummaries)
	{
		HashMap importTypes = new HashMap();
		for (int i = 0; i < methodSummaries.size(); i++) {
			MethodSummary ms = (MethodSummary) methodSummaries.elementAt(i);
			// Add return type to list
			TypeDeclSummary retType = ms.getReturnType();
			String typeName = retType.getName();
			if ((!(typeName.equals("void"))) && (importTypes.get(typeName) == null)) {
				importTypes.put(typeName, retType);
			}
			Iterator params = ms.getParameters();
			// Add parameter types to list
			while ((params != null) && (params.hasNext())) {
				VariableSummary vs = (VariableSummary) params.next();
				TypeDeclSummary param = vs.getTypeDecl();
				typeName = param.getName();
				if (importTypes.get(typeName) == null) {
					importTypes.put(typeName, param);
				}
			}
			// Add exception types to list
			Iterator exceptions = ms.getExceptions();
			while ((exceptions != null) && (exceptions.hasNext())) {
				TypeDeclSummary exception = (TypeDeclSummary) exceptions.next();
				typeName = exception.getName();
				if (importTypes.get(typeName) == null) {
					importTypes.put(typeName, exception);
				}
			}
		}
		return importTypes.values().iterator();
	}


	/**
	 *  Adds the name of the newly created interface to the implements clause of
	 *  each class selected for the refactoring.
	 */
	private void addInterfaceToClasses()
	{
		for (int i = 0; i < m_summaryList.size(); i++) {
			TypeSummary ts = (TypeSummary) m_summaryList.elementAt(i);
			FileSummary fileSummary = (FileSummary) ts.getParent();
			File file = fileSummary.getFile();
			ASTName interfaceName = new ASTName();

			String currentPackageName = ts.getPackageSummary().getName();
			/*
			 *  If the interface package differs from the class package, then
			 *  specify the interface package name
			 */
			if ((m_packageName.length() > 0) && !(currentPackageName.equals(m_packageName))) {
				interfaceName.fromString(m_packageName + "." + m_interfaceName);
			}
			else {
				interfaceName.fromString(m_interfaceName);
			}
			m_complexTransform.clear();
			// Very Important so we don't re-apply the interface transforms
			m_complexTransform.add(new AddImplementedInterfaceTransform(interfaceName));

			if (!m_packageName.equals(currentPackageName)) {
				m_complexTransform.add(new AddImportTransform(interfaceName));
			}
			m_complexTransform.apply(file, new File(file.getAbsolutePath()));
		}
	}


	/**
	 *  Eliminates methods that don't occurr in all classes and returns the
	 *  resulting Vector of common methods.
	 *
	 *@param  initialMethods  Description of Parameter
	 *@return                 Description of the Returned Value
	 */
	private Vector commonMethods(Vector initialMethods)
	{
		Vector result = new Vector();
		for (int i = 0; i < initialMethods.size(); i++) {
			boolean keep = true;
			outerloop :
			for (int j = 1; j < m_summaryList.size(); j++) {
				TypeSummary ts = (TypeSummary) m_summaryList.elementAt(j);
				Iterator methods = ts.getMethods();
				while (methods.hasNext()) {
					MethodSummary ms = (MethodSummary) methods.next();
					if (ms.equals((MethodSummary) initialMethods.elementAt(i))) {
						continue outerloop;
					}
				}
				keep = false;
			}
			if (keep) {
				MethodSummary ms = (MethodSummary) initialMethods.elementAt(i);
				result.addElement(initialMethods.elementAt(i));
			}
		}
		return result;
	}


	/**
	 *  Creates a new interface file.
	 *
	 *@return    Description of the Returned Value
	 */
	private File createInterfaceFile()
	{
		File newFile = null;
		TypeSummary ts = (TypeSummary) m_summaryList.elementAt(0);
		PackageSummary ps = ts.getPackageSummary();

		if (m_packageName == null) {
			m_packageName = ps.getName();
		}
		CreateNewInterface cni = new CreateNewInterface(ts, m_packageName, m_interfaceName);
		try {
			newFile = cni.run();
		}
		catch (RefactoringException re) {
			re.printStackTrace();
			return null;
		}
		m_complexTransform.createFile(newFile);
		return newFile;
	}
}
