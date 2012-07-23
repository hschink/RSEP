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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeComparisonResult implements INodeComparisonResult {

	private List<INode> newNodes = new ArrayList<>();
	private List<INode> missingNodes = new ArrayList<>();
	private Map<INode, INode> modifiedNodes = new HashMap<>();
	private List<INode> unchangedNodes = new ArrayList<>();
	
	public NodeComparisonResult() { }
	
	public NodeComparisonResult(List<INode> newNodes,
			List<INode> missingNodes,
			Map<INode, INode> modifiedNodes,
			List<INode> unchangedNodes) {
		
		if (newNodes != null)
			this.newNodes = newNodes;
		
		if (missingNodes != null)
			this.missingNodes = missingNodes;
		
		if (modifiedNodes != null)
			this.modifiedNodes = modifiedNodes;
		
		if (unchangedNodes != null)
			this.unchangedNodes = unchangedNodes;
	}
	
	@Override
	public List<INode> getNewNodes() {
		return newNodes;
	}

	@Override
	public List<INode> getMissingNodes() {
		return missingNodes;
	}

	@Override
	public Map<INode, INode> getModifiedNodes() {
		return modifiedNodes;
	}

	@Override
	public List<INode> getUnchangedNodes() {
		return unchangedNodes;
	}

}
