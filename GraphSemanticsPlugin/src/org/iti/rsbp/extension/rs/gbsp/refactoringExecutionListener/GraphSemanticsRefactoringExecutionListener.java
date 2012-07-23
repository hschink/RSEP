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
package org.iti.rsbp.extension.rs.gbsp.refactoringExecutionListener;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.NodeFinder;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringDescriptorProxy;
import org.eclipse.ltk.core.refactoring.history.IRefactoringExecutionListener;
import org.eclipse.ltk.core.refactoring.history.IRefactoringHistoryService;
import org.eclipse.ltk.core.refactoring.history.RefactoringExecutionEvent;
import org.iti.ast.ParserUtils;
import org.iti.rsbp.controller.RestrictedSemanticsController;
import org.iti.rsbp.extension.rs.RestrictedSemanticsRefactoringParticipantArguments;
import org.iti.rsbp.extension.rs.gbsp.GraphSemanticsRenameFieldRefactoringArguments;
import org.iti.rsbp.extension.rs.gbsp.GraphSemanticsRenameVariableRefactoringArguments;
import org.iti.rsbp.extension.rs.gbsp.bindinginformation.IBindingInformation;

public class GraphSemanticsRefactoringExecutionListener implements
		IRefactoringExecutionListener {

	private static List<IBindingInformation> bindingsInRefactoring = new ArrayList<>();
	
	private String originalIdentifier = "";
	private IBindingInformation bindingInformation = null;
	private RestrictedSemanticsRefactoringParticipantArguments refactoringArguments = null;
	
	public static boolean isBindingInRefactoring(IBindingInformation bindingInformation) {
		return bindingsInRefactoring.contains(bindingInformation);
	}
	
	public GraphSemanticsRefactoringExecutionListener(IBindingInformation bindingInformation,
			RestrictedSemanticsRefactoringParticipantArguments refactoringArguments) {
		this.originalIdentifier = bindingInformation.getIdentifier();
		this.bindingInformation = bindingInformation;
		this.refactoringArguments = refactoringArguments;
		
		bindingsInRefactoring.add(bindingInformation);
	}
	
	@Override
	public void executionNotification(RefactoringExecutionEvent event) {
		
		switch (event.getEventType()) {
			case RefactoringExecutionEvent.PERFORMED:
			case RefactoringExecutionEvent.UNDONE:
				processRefactoringResult(event.getDescriptor());
				RestrictedSemanticsController.updateNodeIdentifier(originalIdentifier, bindingInformation.getIdentifier());
				unregisterRefactoring();
				break;
				
			default:
				bindingInformation.deleteMarkers();
		}
	}

	private void processRefactoringResult(RefactoringDescriptorProxy descriptorProxy) {
		
		if (refactoringArguments instanceof GraphSemanticsRenameVariableRefactoringArguments)
			processRenameVariableRefactoring((GraphSemanticsRenameVariableRefactoringArguments)refactoringArguments);
		else if (refactoringArguments instanceof GraphSemanticsRenameFieldRefactoringArguments)
			processRenameFieldRefactoring((GraphSemanticsRenameFieldRefactoringArguments)refactoringArguments);
	}

	private void processRenameVariableRefactoring(GraphSemanticsRenameVariableRefactoringArguments refactoringArguments) {
		ASTNode node = findNewDeclaringNode(refactoringArguments);
		
		bindingInformation.setDeclaringNode(node);
		
		bindingInformation.updateMarkers();
	}

	private void processRenameFieldRefactoring(
			GraphSemanticsRenameFieldRefactoringArguments refactoringArguments) {
		ASTNode node = findNewDeclaringNode(refactoringArguments);
		
		bindingInformation.setDeclaringNode(node);
		
		bindingInformation.updateMarkers();
		
	}

	private class FindVariableVisitor extends ASTVisitor {

		public ASTNode node = null;
		
		private String name = null;
		
		public FindVariableVisitor(String name) {
			this.name = name;
		}
		
		@Override
		public boolean visit(SingleVariableDeclaration node) {
			if (node.getName().getIdentifier().compareTo(name) == 0) {
				this.node = node;
				return true;
			}
			
			return super.visit(node);
		}
		
		@Override
		public boolean visit(SimpleName node) {
			if (node.getIdentifier().compareTo(name) == 0 && node.getParent() instanceof VariableDeclarationFragment) {
				this.node = node.getParent();
				return true;
			}
			
			return super.visit(node);
		}
	}
	
	private ASTNode findNewDeclaringNode(
			GraphSemanticsRenameVariableRefactoringArguments renameArguments) {
		IMethod method = renameArguments.parentMethod;
		ICompilationUnit unit = method.getCompilationUnit();
		
		CompilationUnit cu = ParserUtils.parse(unit);
		ASTNode methodNode = null;
		try {
			methodNode = NodeFinder.perform(cu, method.getSourceRange());
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FindVariableVisitor visitor = new FindVariableVisitor(renameArguments.targetName);
		
		methodNode.accept(visitor);
		
		visitor.node.setProperty(IBindingInformation.ICOMPILATION_PROPERTY, unit);
		
		return visitor.node;
	}
	
	private class FindFieldVisitor extends ASTVisitor {

		public ASTNode node = null;
		
		private String name = null;
		
		public FindFieldVisitor(String name) {
			this.name = name;
		}

		@Override
		public boolean visit(FieldDeclaration node) {
			// TODO Auto-generated method stub
			return super.visit(node);
		}

		@Override
		public boolean visit(SimpleName node) {
			if (node.getIdentifier().compareTo(name) == 0 && node.getParent() instanceof VariableDeclarationFragment) {
				this.node = node.getParent();
				return true;
			}
			
			return super.visit(node);
		}
	}
	
	private ASTNode findNewDeclaringNode(
			GraphSemanticsRenameFieldRefactoringArguments renameArguments) {
		ICompilationUnit unit = renameArguments.element.getCompilationUnit();
		CompilationUnit cu = ParserUtils.parse(unit);
		
		FindFieldVisitor visitor = new FindFieldVisitor(renameArguments.targetName);
		
		cu.accept(visitor);
		
		visitor.node.setProperty(IBindingInformation.ICOMPILATION_PROPERTY, unit);
		
		return visitor.node;
	}

	private void unregisterRefactoring() {		
		IRefactoringHistoryService service = RefactoringCore.getHistoryService();
		
		service.removeExecutionListener(this);
		
		bindingsInRefactoring.remove(bindingInformation);
	}
}
