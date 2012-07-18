package org.iti.rsbp.extension.rs.gbsp.bindinginformation.variable.update;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.iti.rsbp.extension.rs.RestrictedSemanticsRefactoringParticipantArguments;
import org.iti.rsbp.extension.rs.gbsp.model.INode;

public class FieldDeclarationUpdateBindingInformation extends
		AbstractVariableUpdateBindingInformation {

	private FieldDeclaration node;
	
	private INode root;
	
	public FieldDeclarationUpdateBindingInformation(FieldDeclaration node) {
		setDeclaringNode(node);
	}
	
	@Override
	public String getIdentifier() {
		if (node != null) {
			for (Object o : node.fragments())
				if (o instanceof VariableDeclarationFragment)
					return ((VariableDeclarationFragment)o).getName().resolveBinding().getKey();
		}
		
		return "";
	}

	@Override
	public ASTNode getDeclaringNode() {
		return node;
	}

	@Override
	public void setDeclaringNode(ASTNode node) {
		this.node = (FieldDeclaration)node;
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
		
		// TODO
	}

	@Override
	protected String getDeclarationText(ASTNode declaration,
			ASTNode expectedDeclaration, String additionalInformation) {
		
		String text = "Field Declaration: ";
		
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
