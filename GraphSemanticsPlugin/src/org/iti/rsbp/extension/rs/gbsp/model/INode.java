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
