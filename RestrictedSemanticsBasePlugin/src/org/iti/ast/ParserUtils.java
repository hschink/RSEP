/*******************************************************************************
 * Copyright (c) 2012 Hagen Schink.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hagen Schink - initial API and implementation
 ******************************************************************************/
package org.iti.ast;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NodeFinder;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

public class ParserUtils {
	
	public enum AstNodeType {
		CLASS,
		FIELD,
		METHOD,
		VARIABLE,
		UNKNOWN
	}
	
	public static CompilationUnit parse(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		return (CompilationUnit) parser.createAST(null); // parse
	}
	
	public static List<ICompilationUnit> getCompilationUnitsOfNature(IProject project,
			String natureName) {
		List<ICompilationUnit> compilationUnits = new ArrayList<>();
		
		try {
			compilationUnits= tryGetCompilationUnitsOfNature(project, natureName);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		return compilationUnits;
	}

	private static List<ICompilationUnit> tryGetCompilationUnitsOfNature(IProject project,
			String natureName)
			throws CoreException {
		
		List<ICompilationUnit> compilationUnits = new ArrayList<>();
		
		if (project.hasNature(natureName)) {
			IJavaProject javaProject = JavaCore.create(project);
			
			for (IPackageFragment packageFragment : javaProject.getPackageFragments()) {
				if (packageFragment.getKind() == IPackageFragmentRoot.K_SOURCE)
					for (ICompilationUnit unit : packageFragment.getCompilationUnits()) {
						compilationUnits.add(unit);
					}
			}
		}
		
		return compilationUnits;
	}
	
	public static IProject getSelectedProject() {
		IEditorPart editor =  PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		IJavaElement element = JavaUI.getEditorInputJavaElement(editor.getEditorInput());		
		
		return (IProject)element.getJavaProject().getResource();
	}
	
	public static ASTNode getSelectedAstNode() {
		ASTNode node = null;
		IEditorPart editor =  PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		IJavaElement element = JavaUI.getEditorInputJavaElement(editor.getEditorInput());
		ICompilationUnit cu = (ICompilationUnit) element;
		CompilationUnit unit = ParserUtils.parse(cu);
		
		if (editor instanceof ITextEditor) {
		  ISelectionProvider selectionProvider = ((ITextEditor)editor).getSelectionProvider();
		  ISelection selection = selectionProvider.getSelection();
		  if (selection instanceof ITextSelection) {
		    ITextSelection textSelection = (ITextSelection)selection;
		    
		    node = NodeFinder.perform(unit, textSelection.getOffset(), textSelection.getLength());
		  }
		}
		
		return node;
	}
	
	public static AstNodeType getAstNodeType(ASTNode node) {
		for (AstNodeType type : AstNodeType.values()) {
			if (nodeIsReferencingTypeOf(node, type))
				return type;
		}
		
		return AstNodeType.UNKNOWN;
	}

	private static boolean nodeIsReferencingTypeOf(ASTNode node,
			AstNodeType type) {
		switch (type) {
			case CLASS:
				return nodeIsReferencingClassType(node);
				
			case METHOD:
				return nodeIsReferencingMethodType(node);
				
			case VARIABLE:
				return nodeIsReferencingVariableType(node);
		
			default:
				break;
		}

		return false;
	}

	private static boolean nodeIsReferencingClassType(ASTNode node) {
		boolean isReferencingClassType = true;
		
		isReferencingClassType &= node instanceof TypeDeclaration;
		isReferencingClassType &= node instanceof SimpleType;
		
		return isReferencingClassType;
	}

	private static boolean nodeIsReferencingMethodType(ASTNode node) {
		boolean isReferencingMethodType = true;
		
		isReferencingMethodType &= node instanceof MethodDeclaration;
		isReferencingMethodType &= node instanceof MethodInvocation;
		
		return isReferencingMethodType;
	}

	private static boolean nodeIsReferencingVariableType(ASTNode node) {
		boolean isReferencingVariableType = true;
		
		if (node instanceof VariableDeclarationFragment) {
			isReferencingVariableType &= node.getNodeType() == ASTNode.VARIABLE_DECLARATION_FRAGMENT;
			isReferencingVariableType &= node.getNodeType() != ASTNode.FIELD_DECLARATION;
		} else if (node instanceof SimpleName) {
			IBinding binding = ((SimpleName) node).resolveBinding();
			
			isReferencingVariableType &= binding instanceof IVariableBinding;
		}
		
		return isReferencingVariableType;
	}

	public static CompilationUnit getCompilationUnit(ASTNode node) {
		ASTNode n = node;
		
		while (n != null) {
			if (n instanceof CompilationUnit)
				return (CompilationUnit)n;
			
			n = n.getParent();
		}
		
		return null;
	}
	
	public static List<ASTNode> getReferences(ASTNode node) {
		switch (node.getNodeType()) {
			case ASTNode.VARIABLE_DECLARATION_FRAGMENT:
				return getVariableReferences(node, ((VariableDeclarationFragment)node).resolveBinding().getKey());
			
			case ASTNode.SINGLE_VARIABLE_DECLARATION:
				return getVariableReferences(node, ((SingleVariableDeclaration)node).resolveBinding().getKey());
				
			case ASTNode.METHOD_DECLARATION:
				return getMethodInvocations((MethodDeclaration)node);
		}
		
		return new ArrayList<ASTNode>();
	}

	private static List<ASTNode> getVariableReferences(ASTNode node, String key) {
		FindVariableReferencesVisitor visitor = new FindVariableReferencesVisitor(key);
		List<ASTNode> nodes = getDefiningType(node);
		
		for (ASTNode n : nodes)			
			n.accept(visitor);
		
		return visitor.getReferences();
	}

	private static List<ASTNode> getDefiningType(ASTNode node) {
		List<ASTNode> compilationUnits = new ArrayList<>();
		ASTNode parent = node.getParent();

		while (parent != null) {
			if (parent instanceof MethodDeclaration) {
				compilationUnits.add(parent.getParent());
				break;
			}
			else if (parent instanceof CompilationUnit) {
				List<ICompilationUnit> units = getCompilationUnitsOfNature(ParserUtils.getSelectedProject(), "org.eclipse.jdt.core.javanature");
				
				for (ICompilationUnit c : units)
					compilationUnits.add(parse(c));
				
				break;
			}
			
			parent = parent.getParent();
		}
		
		return compilationUnits;
	}

	private static List<ASTNode> getMethodInvocations(MethodDeclaration node) {
		String key = node.resolveBinding().getKey();
		
		List<ICompilationUnit> units = getCompilationUnitsOfNature(ParserUtils.getSelectedProject(), "org.eclipse.jdt.core.javanature");
		
		FindMethodInvocationVisitor visitor = new FindMethodInvocationVisitor(key);
		
		for (ICompilationUnit unit : units) {
			CompilationUnit cu = ParserUtils.parse(unit);
			
			cu.accept(visitor);
		}
		
		return visitor.getReferences();
	}

	public static IProject getProject(ASTNode node) {
		return getCompilationUnit(node).getJavaElement().getJavaProject().getProject();
	}
	
//	private static int performIMethodSearch(IMethod method)
//            throws CoreException {
//        SearchPattern pattern = SearchPattern.createPattern(method,
//                IJavaSearchConstants.REFERENCES);
//        IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
//        MySearchRequestor requestor = new MySearchRequestor();
//        SearchEngine searchEngine = new SearchEngine();
//        searchEngine.search(pattern, new SearchParticipant[] { SearchEngine
//                .getDefaultSearchParticipant() }, scope, requestor, null);
//        return requestor.getNumberOfCalls();
//    }
}
