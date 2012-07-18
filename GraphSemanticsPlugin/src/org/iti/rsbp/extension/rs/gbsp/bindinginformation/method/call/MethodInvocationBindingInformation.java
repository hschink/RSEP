package org.iti.rsbp.extension.rs.gbsp.bindinginformation.method.call;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.iti.rsbp.extension.rs.RestrictedSemanticsRefactoringParticipantArguments;
import org.iti.rsbp.extension.rs.gbsp.model.Edge;
import org.iti.rsbp.extension.rs.gbsp.model.IEdge;
import org.iti.rsbp.extension.rs.gbsp.model.INode;
import org.iti.rsbp.extension.rs.gbsp.model.Node;

public class MethodInvocationBindingInformation extends
		AbstractMethodInvocationBindingInformation {

	private MethodDeclaration node;
	
	private INode root;
	
	public MethodInvocationBindingInformation(MethodDeclaration node) {
		setDeclaringNode(node);
	}
	
	@Override
	public String getIdentifier() {
		if (node != null) {
			return node.resolveBinding().getKey();
		}
		
		return "";
	}

	@Override
	public ASTNode getDeclaringNode() {
		return node;
	}

	@Override
	public void setDeclaringNode(ASTNode node) {
		this.node = (MethodDeclaration)node;
	}
	
	@Override
	public INode BuildModel(List<ASTNode> references) {
		INode root = new Node("Root", "METHOD", getDeclaration());
		int nodeCounter = 0;
		
		for (ASTNode astNode : references) {
			INode leaf = new Node("METHOD_CALL_" + nodeCounter, "METHOD_CALL", astNode);
			IEdge edge = new Edge("INVOKES", root, leaf);
			
			root.addEdge(edge);
			
			++nodeCounter;
		}
		
		return root;
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
		
		String text = "Method Declaration: ";
		
		text += (expectedDeclaration == null) ? getMethodDeclaration((MethodDeclaration)declaration) 
											  : getMethodDeclaration((MethodDeclaration)expectedDeclaration);
							
		if (additionalInformation != "")
			text += "\n\n" + additionalInformation;
		
		return text;
	}

	private String getMethodDeclaration(MethodDeclaration declaration) {
		String methodDeclaration = "";
		
		for (Object m : declaration.modifiers())
			methodDeclaration += m.toString() + " ";
		
		methodDeclaration += declaration.getReturnType2().toString() + " ";
		
		methodDeclaration += declaration.getName().toString() + "(";
		
		for (Object p : declaration.parameters())
			methodDeclaration += p.toString() + " ";
		
		methodDeclaration = methodDeclaration.trim() + ")";
		
		return methodDeclaration;
	}

	@Override
	protected int getDeclarationStartPosition() {
		if (node.modifiers().size() > 0)
			return ((ASTNode)node.modifiers().get(0)).getStartPosition();
		else
			return node.getName().getStartPosition();
	}
	
	private static final int COUNT_PARAMETER_BRACKETS = 2;

	@Override
	protected int getDeclarationEndPosition() {		
		if (node.parameters().size() > 0) {
			ASTNode n = (ASTNode)node.parameters().get(node.parameters().size() - 1);
			
			return n.getStartPosition() + n.getLength() + 1;
		} else {
			return node.getName().getStartPosition() + node.getName().getLength() + COUNT_PARAMETER_BRACKETS;
		}
	}
	
	@Override
	protected String getMarkerIdForUnprocessedNode(ASTNode node) {
		return ERROR_MARKER_ID;
	}
	
	@Override
	protected String getTextForUnprocessedNode(ASTNode node) {
		return String.format("CHANGE: Missing method invocation [%1$s]!", node);
	}
	
	@Override
	protected String getMarkerIdForNewNode(ASTNode node) {
		return ERROR_MARKER_ID;
	}
	
	@Override
	protected String getTextForNewNode(ASTNode node) {
		return String.format("CHANGE: New method invocation [%1$s]!", node);
	}
}
