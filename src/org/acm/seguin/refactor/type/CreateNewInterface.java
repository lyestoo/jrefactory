package org.acm.seguin.refactor.type;

import java.io.File;
import java.io.IOException;
import org.acm.seguin.parser.Node;
import org.acm.seguin.parser.ast.*;
import org.acm.seguin.pretty.PrettyPrintFile;
import org.acm.seguin.refactor.RefactoringException;
import org.acm.seguin.summary.FileSummary;
import org.acm.seguin.summary.PackageSummary;
import org.acm.seguin.summary.Summary;
import org.acm.seguin.summary.TypeDeclSummary;
import org.acm.seguin.summary.TypeSummary;
import org.acm.seguin.summary.query.GetTypeSummary;
import org.acm.seguin.summary.query.SamePackage;

/**
 *  This object creates an interface from nothing. It is responsible for
 *  building up the parse tree from scratch to create a new interface.
 *
 *@author     Grant Watson
 *@created    November 28, 2000
 */
public class CreateNewInterface {
	private File m_rootDir;
	private String m_interfaceName;
	private String m_packageName;


	/**
	 *  Constructor for the CreateNewInterface object
	 *
	 *@param  interfaceName  Description of Parameter
	 *@param  packageName    Description of Parameter
	 *@param  rootDir        Description of Parameter
	 */
	public CreateNewInterface(File rootDir, String packageName, String interfaceName) {
		m_rootDir = rootDir;
		m_packageName = packageName;
		m_interfaceName = interfaceName;
	}


	/**
	 *  Constructor for the CreateNewInterface object
	 */
	CreateNewInterface() {
		m_interfaceName = null;
		m_packageName = null;
	}


	/**
	 *  Creates the the designated class
	 *
	 *@return                           Description of the Returned Value
	 *@exception  RefactoringException  Description of Exception
	 */
	public File run() throws RefactoringException {
		if (m_packageName == null) {
			throw new RefactoringException("No package name specified");
		}

		if (m_interfaceName == null) {
			throw new RefactoringException("No interface name specified");
		}

		//  Create the AST
		ASTCompilationUnit root = new ASTCompilationUnit(0);

		//  Create the package statement
		ASTPackageDeclaration packDecl = createPackageDeclaration();
		root.jjtAddChild(packDecl, 0);

		ASTName parentName = new ASTName(0);

		//  Create the class
		ASTTypeDeclaration td = createTypeDeclaration();
		root.jjtAddChild(td, 1);

		//  Print this new one
		File dest = print(getFullPath(), m_interfaceName, root);
		return dest;
	}


	/**
	 *  Gets the FullPath of the type summary
	 *
	 *@return    The FullPath value
	 */
	String getFullPath() {
		String result = m_packageName + "." + m_interfaceName;
		result = result.replace('.', '/');
		result = m_rootDir.toString() + File.separator + result;
		return result;
	}


	/**
	 *  Creates the package declaration
	 *
	 *@return    the package declaration
	 */
	ASTPackageDeclaration createPackageDeclaration() {
		ASTPackageDeclaration packDecl = new ASTPackageDeclaration(0);
		ASTName packName = new ASTName(0);
		packName.fromString(m_packageName);
		packDecl.jjtAddChild(packName, 0);

		return packDecl;
	}


	/**
	 *  Creates the type declaration
	 *
	 *@return    the modified class
	 */
	ASTTypeDeclaration createTypeDeclaration() {
		ASTTypeDeclaration td = new ASTTypeDeclaration(0);
		ASTInterfaceDeclaration id = createModifiedClass();
		td.jjtAddChild(id, 0);

		return td;
	}


	/**
	 *  Creates the modified class
	 *
	 *@return    the modified class
	 */
	ASTInterfaceDeclaration createModifiedClass() {
		ASTInterfaceDeclaration id = new ASTInterfaceDeclaration(0);
		id.addModifier("public");
		ASTUnmodifiedInterfaceDeclaration uid = createClassBody(m_interfaceName);
		id.jjtAddChild(uid, 0);
		return id;
	}


	/**
	 *  Creates the body. The protection level is package so it can be easily
	 *  tested.
	 *
	 *@param  parentName  Description of Parameter
	 *@return             the class
	 */
	ASTUnmodifiedInterfaceDeclaration createClassBody(String parentName) {
		ASTUnmodifiedInterfaceDeclaration uid = new ASTUnmodifiedInterfaceDeclaration(0);
		uid.setName(parentName);
		ASTInterfaceBody ib = new ASTInterfaceBody(0);
		uid.jjtAddChild(ib, 0);
		return uid;
	}


	/**
	 *  Prints the file
	 *
	 *@param  fullpath    Description of Parameter
	 *@param  parentName  Description of Parameter
	 *@param  root        Description of Parameter
	 *@return             Description of the Returned Value
	 */
	File print(String fullpath, String parentName, SimpleNode root) {
		File parent = (new File(fullpath)).getParentFile();
		// Create directory if it doesn't exist
		if (!parent.exists()) {
			parent.mkdir();
		}
		File destFile = new File(parent, parentName + ".java");

		try {
			(new PrettyPrintFile()).apply(destFile, root);
		}
		catch (Throwable thrown) {
			thrown.printStackTrace(System.out);
		}

		return destFile;
	}


	/**
	 *  Gets the package summary
	 *
	 *@param  base  Description of Parameter
	 *@return       the package summary
	 */
	private PackageSummary getPackageSummary(Summary base) {
		Summary current = base;
		while (!(current instanceof PackageSummary)) {
			current = current.getParent();
		}
		return (PackageSummary) current;
	}


	/**
	 *  Gets the SameParent attribute of the AddAbstractParent object
	 *
	 *@param  one  Description of Parameter
	 *@param  two  Description of Parameter
	 *@return      The SameParent value
	 */
	private boolean isSameParent(TypeSummary one, TypeSummary two) {
		if (isObject(one)) {
			return isObject(two);
		}

		if (isObject(two)) {
			return false;
		}

		return one.equals(two);
	}


	/**
	 *  Gets the Object attribute of the AddAbstractParent object
	 *
	 *@param  item  Description of Parameter
	 *@return       The Object value
	 */
	private boolean isObject(TypeSummary item) {
		if (item == null) {
			return true;
		}

		if (item.getName().equals("Object")) {
			return true;
		}

		return false;
	}
}
