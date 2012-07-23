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
