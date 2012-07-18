package org.iti.rsbp.extension.rs.gbsp.model;

import java.util.List;
import java.util.Map;

public interface INodeComparisonResult {
	
	List<INode> getNewNodes();
	
	List<INode> getMissingNodes();
	
	Map<INode, INode> getModifiedNodes();
	
	List<INode> getUnchangedNodes();
}
