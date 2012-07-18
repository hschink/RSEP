package org.iti.rsbp.extension.rs.gbsp.bindinginformation.variable.access;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.history.IRefactoringExecutionListener;
import org.eclipse.ltk.core.refactoring.history.IRefactoringHistoryService;
import org.iti.rsbp.extension.rs.RestrictedSemanticsRefactoringParticipantArguments;
import org.iti.rsbp.extension.rs.gbsp.model.INode;
import org.iti.rsbp.extension.rs.gbsp.refactoringExecutionListener.GraphSemanticsRefactoringExecutionListener;

public class SingleVariableDeclarationAccessBindingInformation extends
	AbstractVariableAccessBindingInformation {

	private SingleVariableDeclaration node;
	
	private INode root;
	
	public SingleVariableDeclarationAccessBindingInformation(SingleVariableDeclaration node) {
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
