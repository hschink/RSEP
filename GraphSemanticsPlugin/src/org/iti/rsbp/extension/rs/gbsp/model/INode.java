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
package org.iti.rsbp.extension.rs.gbsp.model;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;

public interface INode {
	
	String getIdentifier();
	
	String getKinde();
	
	ASTNode getExpression();
	void setExpression(ASTNode expression);
	
	void addEdge(IEdge edge);
	
	List<IEdge> getEdges();
	
	Object getAttribute(String attributeName);
	void setAttribute(String attributeName, Object attribute);
	
	INodeComparisonResult compareTo(INode node);
}
