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
package org.iti.rsbp.extension.rs.gbsp.bindinginformation.variable.update;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.history.IRefactoringExecutionListener;
import org.eclipse.ltk.core.refactoring.history.IRefactoringHistoryService;
import org.iti.rsbp.extension.rs.RestrictedSemanticsRefactoringParticipantArguments;
import org.iti.rsbp.extension.rs.gbsp.model.INode;
import org.iti.rsbp.extension.rs.gbsp.refactoringExecutionListener.GraphSemanticsRefactoringExecutionListener;

public class SingleVariableDeclarationUpdateBindingInformation extends
		AbstractVariableUpdateBindingInformation {

	private SingleVariableDeclaration node;
	
	private INode root;
	
	public SingleVariableDeclarationUpdateBindingInformation(SingleVariableDeclaration node) {
		setDeclaringNode(node);
	}
	
	@Override
	public String getIdentifier() {
		if (node != null)
			return node.resolveBinding().getKey();
		
		return "";
	}

	@Override
	public ASTNode getDeclaringNode() {
		return node;
	}

	@Override
	public void setDeclaringNode(ASTNode node) {
		this.node = (SingleVariableDeclaration)node;
	}
	
	@Override
	public INode getRoot() {
		return root;
	}
	
	@Override
	public void setRoot(INode root) {
		this.root = root;
	}

	@Override
	public void prepareRefactoring(
		RestrictedSemanticsRefactoringParticipantArguments refactoringArguments) {

		IRefactoringExecutionListener refactoringListener = new GraphSemanticsRefactoringExecutionListener(this, refactoringArguments);
		
		IRefactoringHistoryService service = RefactoringCore.getHistoryService();
		
		service.addExecutionListener(refactoringListener);
	}

	@Override
	protected int getDeclarationStartPosition() {
		return node.getStartPosition();
	}

	@Override
	protected int getDeclarationEndPosition() {
		return node.getStartPosition() + node.getLength();
	}
}
