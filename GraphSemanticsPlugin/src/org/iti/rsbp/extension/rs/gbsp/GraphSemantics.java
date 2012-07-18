package org.iti.rsbp.extension.rs.gbsp;

import java.util.ArrayList;
import java.util.List;

import org.iti.rsbp.extension.rs.IRestrictedSemantics;
import org.iti.rsbp.extension.rs.IRestrictedSemanticsOption;

public class GraphSemantics implements IRestrictedSemantics {

	public enum GraphSemanticsOptionType {
		Multi,
		VariableUpdate,
		VariableAccess,
		MethodInvocation
	}
	
	private GraphSemanticsOption[] options = new GraphSemanticsOption[] {};
	
	public GraphSemantics() {
		List<GraphSemanticsOption> options = new ArrayList<>();
		
		for (GraphSemanticsOptionType option : GraphSemanticsOptionType.values()) {
			options.add(new GraphSemanticsOption(this, option));
		}
		
		this.options = options.toArray(new GraphSemanticsOption[] {});
	}
	
	@Override
	public String getId() {
		return "Graph-based Semantics";
	}

	@Override
	public IRestrictedSemanticsOption[] getOptions() {
		return options;
	}

	@Override
	public String toString() {
		return getId();
	}
}
