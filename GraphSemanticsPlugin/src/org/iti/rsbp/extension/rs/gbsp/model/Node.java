package org.iti.rsbp.extension.rs.gbsp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;

public class Node implements INode {

	private String kind;
	private ASTNode astNode;
	private String identifier;
	
	private Map<String, Object> attributes = new HashMap<>();
			
	private List<IEdge> edges = new ArrayList<IEdge>();
	
	public Node(String identifier, String kind, ASTNode astNode) {
		this.kind = kind;
		this.astNode = astNode;
		this.identifier = identifier;
	}
	
	@Override
	public String getIdentifier() {
		return identifier;
	}
	
	@Override
	public String getKinde() {
		return kind;
	}

	@Override
	public ASTNode getExpression() {
		return astNode;
	}

	@Override
	public void setExpression(ASTNode astNode) {
		this.astNode = astNode;
	}
	
	@Override
	public void addEdge(IEdge edge) {
		edges.add(edge);
	}

	@Override
	public List<IEdge> getEdges() {
		return edges;
	}

	@Override
	public Object getAttribute(String attributeName) {
		return attributes.get(attributeName);
	}

	@Override
	public void setAttribute(String attributeName, Object attribute) {
		if (attribute == null)
			attributes.remove(attributeName);
		else
			attributes.put(attributeName, attribute);
	}

	@Override
	public INodeComparisonResult compareTo(INode node) {
		int nodePosition = 0;
		List<IEdge> modifiedOrRemovedEdges = new ArrayList<>();
		NodeComparisonResult comparisonResult = new NodeComparisonResult();
		
		List<IEdge> updatedEdges = new ArrayList<>(node.getEdges());
		
		int result = getExpression().toString().compareTo(node.getExpression().toString());
		
		if (result == 0)
			comparisonResult.getUnchangedNodes().add(this);
		else
			comparisonResult.getModifiedNodes().put(this, node);
		
		for (IEdge edge : getEdges()) {
			IEdge matchingEdge = findNextMatchingNodeByExpression(updatedEdges, nodePosition, edge);
			
			if (matchingEdge != null) {
				comparisonResult.getUnchangedNodes().add(edge.getTail());
				updatedEdges.remove(matchingEdge);
			} else {
				modifiedOrRemovedEdges.add(edge);
			}
		}
		
		for (IEdge edge : modifiedOrRemovedEdges) {
			IEdge matchingEdge = findMatchingEdgeByIdentifier(edge.getTail().getIdentifier(), updatedEdges);
			
			if (matchingEdge != null) {
				comparisonResult.getModifiedNodes().put(edge.getTail(), matchingEdge.getTail());
				updatedEdges.remove(matchingEdge);
			} else {
				comparisonResult.getMissingNodes().add(edge.getTail());
				updatedEdges.remove(matchingEdge);
			}
		}
		
		for (IEdge newEdge : updatedEdges)
			comparisonResult.getNewNodes().add(newEdge.getTail());
		
		return comparisonResult;
	}

	private IEdge findMatchingEdgeByIdentifier(String identifier, List<IEdge> updatedEdges) {
		IEdge matchingEdge = null;
		
		for (IEdge edge : updatedEdges) {
			if (edge.getTail().getIdentifier().compareTo(identifier) == 0) {
				matchingEdge = edge;
				break;
			}
		}
		
		return matchingEdge;
	}

	private int isExpressionMatching(IEdge edge, IEdge matchingEdge) {
		return edge.getTail().getExpression().toString().compareTo(matchingEdge.getTail().getExpression().toString());
	}
	
	private IEdge findNextMatchingNodeByExpression(List<IEdge> edges, int nodePosition, IEdge edge) {
		
		for (int x = nodePosition; x < edges.size(); x++)
			if (isExpressionMatching(edge, edges.get(x)) == 0)
				return edges.get(x);
		
		return null;
	}
}
