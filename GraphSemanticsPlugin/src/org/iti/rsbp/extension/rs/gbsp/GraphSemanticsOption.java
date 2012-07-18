package org.iti.rsbp.extension.rs.gbsp;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.dom.ASTNode;
import org.iti.rsbp.extension.rs.IRestrictedSemantics;
import org.iti.rsbp.extension.rs.IRestrictedSemanticsOption;
import org.iti.rsbp.extension.rs.IRestrictedSemanticsSnapshot;
import org.iti.rsbp.extension.rs.gbsp.GraphSemantics.GraphSemanticsOptionType;
import org.iti.rsbp.extension.rs.gbsp.utils.MethodUtils;
import org.iti.rsbp.extension.rs.gbsp.utils.VariableUtils;

public class GraphSemanticsOption implements IRestrictedSemanticsOption {
	
	private GraphSemantics graphSemantics = null;
	private GraphSemanticsOptionType graphSemanticsOptionType = GraphSemanticsOptionType.VariableUpdate;
	
	public GraphSemanticsOption(GraphSemantics graphSemantics, GraphSemanticsOptionType graphSemanticsOptionType) {
		this.graphSemantics = graphSemantics;
		this.graphSemanticsOptionType = graphSemanticsOptionType;
	}
	
	@Override
	public String getId() {
		return graphSemanticsOptionType.toString();
	}
	
	@Override
	public boolean isValidForNode(IProject project, ASTNode node) {
		boolean isValid = false;
		
		try {
			IRestrictedSemanticsSnapshot snapshot = this.createAstSnapshot(project, node);
			
			isValid = snapshot != null;
		} catch (Exception ex) { }
		
		
		return isValid;
	}
	
	@Override
	public IRestrictedSemantics getRestrictedSemantics() {
		return graphSemantics;
	}

	@Override
	public IRestrictedSemanticsSnapshot createAstSnapshot(IProject project, ASTNode node) {
		
		return createAstSnapshot(project, node, graphSemanticsOptionType);
	}
	
	public static IRestrictedSemanticsSnapshot createAstSnapshot(IProject project,
			ASTNode node, GraphSemanticsOptionType graphSemanticsOptionType) {
		
		IRestrictedSemanticsSnapshot result = null;

		switch (graphSemanticsOptionType)
		{
			case VariableUpdate:				
			case VariableAccess:
				result = VariableUtils.createVariableUpdateSnapshot(project, node, graphSemanticsOptionType);
				break;
				
			case MethodInvocation:
				result = MethodUtils.createMethodAccessSnapshot(project, node, graphSemanticsOptionType);
				break;
		
			default:
				break;
		}
		
		return result;
	}

	@Override
	public String toString() {
		return graphSemanticsOptionType.toString();
	}
}
