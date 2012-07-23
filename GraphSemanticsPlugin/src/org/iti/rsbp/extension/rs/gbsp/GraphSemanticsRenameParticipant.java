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
package org.iti.rsbp.extension.rs.gbsp;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.NodeFinder;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RenameArguments;
import org.eclipse.ltk.core.refactoring.participants.RenameParticipant;
import org.iti.ast.ParserUtils;
import org.iti.rsbp.controller.RestrictedSemanticsController;
import org.iti.rsbp.extension.rs.RestrictedSemanticsRefactoringParticipantArguments;

public class GraphSemanticsRenameParticipant extends RenameParticipant {

	@Override
	protected boolean initialize(Object element) {
		RenameArguments arguments = getArguments();
		String identifier = null;
		RestrictedSemanticsRefactoringParticipantArguments refactoringArguments = null;
		
		if (element instanceof ILocalVariable) {
			ILocalVariable variable = (ILocalVariable)element;
			IMethod method = (IMethod)variable.getParent();
			String targetName = arguments.getNewName();			
			refactoringArguments = new GraphSemanticsRenameVariableRefactoringArguments(variable, method, targetName);
			
			identifier = getIdentifier(variable);
		} else if (element instanceof IField) {
			IField field = (IField)element;
			String targetName = arguments.getNewName();
			refactoringArguments = new GraphSemanticsRenameFieldRefactoringArguments(field, targetName);
			
			identifier = getIdentifier(field);
		}
		
		if (identifier != null && refactoringArguments != null)
			RestrictedSemanticsController.informSnapshotsAboutRefactoringOfIdentifier(identifier, refactoringArguments);
		
		return false;
	}

	private String getIdentifier(ILocalVariable variable) {
		IMethod method = (IMethod)variable.getDeclaringMember();
		ICompilationUnit unit = method.getCompilationUnit();
		CompilationUnit root = ParserUtils.parse(unit);
		
		ASTNode node = null;
		
		try {
			node = NodeFinder.perform(root, variable.getSourceRange());
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (node != null)
		{
			if (node instanceof VariableDeclarationStatement)
				for (Object o : ((VariableDeclarationStatement)node).fragments())
					if (o instanceof VariableDeclarationFragment)
						return ((VariableDeclarationFragment)o).getName().resolveBinding().getKey();
			
			if (node instanceof SingleVariableDeclaration)
				return ((SingleVariableDeclaration)node).resolveBinding().getKey();
		}
			
			
		return null;
	}
	
	private String getIdentifier(IField field) {
		ICompilationUnit unit = field.getCompilationUnit();
		CompilationUnit root = ParserUtils.parse(unit);
		
		ASTNode node = null;
		
		try {
			node = NodeFinder.perform(root, field.getSourceRange());
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (node != null && node instanceof FieldDeclaration)
			for (Object o : ((FieldDeclaration)node).fragments())
				if (o instanceof VariableDeclarationFragment)
					return ((VariableDeclarationFragment)o).getName().resolveBinding().getKey();
			
		return null;
	}

	@Override
	public String getName() {
		return "org.iti.rbsp.extension.rs.gbsp.GraphSemanticsRenameParticipant";
	}

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws OperationCanceledException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException,
			OperationCanceledException {
		// TODO Auto-generated method stub
		return null;
	}

}
