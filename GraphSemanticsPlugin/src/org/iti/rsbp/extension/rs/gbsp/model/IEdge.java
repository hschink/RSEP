package org.iti.rsbp.extension.rs.gbsp.model;

public interface IEdge {
	
	String getKind();
	
	INode getHead();
	void setHead(INode head);
	
	INode getTail();
	void setTail(INode tail);
}
