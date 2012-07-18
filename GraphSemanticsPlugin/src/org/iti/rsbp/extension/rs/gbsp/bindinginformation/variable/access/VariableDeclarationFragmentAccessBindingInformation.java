package org.iti.rsbp.extension.rs.gbsp.bindinginformation.variable.access;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.history.IRefactoringExecutionListener;
import org.eclipse.ltk.core.refactoring.history.IRefactoringHistoryService;
import org.iti.rsbp.extension.rs.RestrictedSemanticsRefactoringParticipantArguments;
import org.iti.rsbp.extension.rs.gbsp.model.INode;
import org.iti.rsbp.extension.rs.gbsp.refactoringExecutionListener.GraphSemanticsRefactoringExecutionListener;

public class VariableDeclarationFragmentAccessBindingInformation extends
	AbstractVariableAccessBindingInformation {

	private VariableDeclarationFragment node;
	
	private INode root;
	
	public VariableDeclarationFragmentAccessBindingInformation(VariableDeclarationFragment node) {
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
		this.node = (VariableDeclarationFragment)node;
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
	protected String getDeclarationText(ASTNode declaration,
			ASTNode expectedDeclaration, String additionalInformation) {
		
		String text = "Variable Declaration: ";
		
		text += (expectedDeclaration == null) ? declaration.getParent().toString() : expectedDeclaration.getParent().toString();
							
		if (additionalInformation != "")
			text += "\n\n" + additionalInformation;
		
		return text;
	}

	@Override
	protected int getDeclarationStartPosition() {
		return node.getParent().getStartPosition();
	}

	@Override
	protected int getDeclarationEndPosition() {
		return node.getParent().getStartPosition() + node.getParent().getLength();
	}
}
