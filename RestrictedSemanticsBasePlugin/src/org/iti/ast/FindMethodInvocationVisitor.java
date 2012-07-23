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

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class FindMethodInvocationVisitor extends ASTVisitor {

	private List<ASTNode> references = new ArrayList<>();
	
	public List<ASTNode> getReferences() {
		return references;
	}
	
	private String key = null;
	
	public FindMethodInvocationVisitor(String key) {
		if (key == null || key == "")
			throw new IllegalArgumentException("key must not be null or empty!");
		
		this.key = key;
	}

	@Override
	public boolean visit(MethodInvocation node) {
		IBinding binding = node.resolveMethodBinding();
		
		addReference(node, binding);
		
		return super.visit(node);
	}

	private void addReference(ASTNode node, IBinding binding) {
		if (binding instanceof IMethodBinding) {
			if (binding.getKey().compareTo(key) == 0) {
				references.add(node);
			}
		}
	}
}
