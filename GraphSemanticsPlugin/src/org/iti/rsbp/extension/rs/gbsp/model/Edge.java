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

public class Edge implements IEdge {

	private String kind;
	private INode head;
	private INode tail;
	
	public Edge(String kind, INode head, INode tail)
	{
		this.kind = kind;
		this.head = head;
		this.tail = tail;
	}
	
	@Override
	public String getKind() {
		return kind;
	}

	@Override
	public INode getHead() {
		return head;
	}

	@Override
	public void setHead(INode head) {
		this.head = head;
	}

	@Override
	public INode getTail() {
		return tail;
	}

	@Override
	public void setTail(INode tail) {
		this.tail = tail;
	}

}
