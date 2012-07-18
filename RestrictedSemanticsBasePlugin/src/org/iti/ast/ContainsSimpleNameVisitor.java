package org.iti.ast;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.SimpleName;

public class ContainsSimpleNameVisitor extends ASTVisitor {

	private ASTNode node = null;
	
	private boolean nodeFound = false;

	public boolean isNodeFound() {
		return nodeFound;
	}

	public ContainsSimpleNameVisitor(ASTNode node) {
		this.node = node;
	}

	@Override
	public boolean visit(SimpleName node) {
		if (node == this.node) {
			nodeFound = true;
			return true;
		}
		
		return super.visit(node);
	}
}
