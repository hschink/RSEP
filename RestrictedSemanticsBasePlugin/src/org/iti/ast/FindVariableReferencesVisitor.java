package org.iti.ast;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

public class FindVariableReferencesVisitor extends ASTVisitor {

	private List<ASTNode> references = new ArrayList<>();
	
	public List<ASTNode> getReferences() {
		return references;
	}
	
	private String key = null;
	
	public FindVariableReferencesVisitor(String key) {
		if (key == null || key == "")
			throw new IllegalArgumentException("key must not be null or empty!");
		
		this.key = key;
	}

	@Override
	public boolean visit(SingleVariableDeclaration node) {
		IBinding binding = node.resolveBinding();
		
		addReference(node, binding);
		
		return super.visit(node);
	}

	@Override
	public boolean visit(SimpleName node) {
		IBinding binding = node.resolveBinding();
		
		addReference(node, binding);
		
 		return super.visit(node);
	}

	private void addReference(ASTNode node, IBinding binding) {
		if (binding instanceof IVariableBinding) {
			if (binding.getKey().compareTo(key) == 0) {
				references.add(node);
			}
		}
	}
}
