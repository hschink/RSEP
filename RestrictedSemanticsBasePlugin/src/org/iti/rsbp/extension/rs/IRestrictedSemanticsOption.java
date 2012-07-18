package org.iti.rsbp.extension.rs;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.dom.ASTNode;

public interface IRestrictedSemanticsOption {
	String getId();

	boolean isValidForNode(IProject selectedProject, ASTNode node);
	
	IRestrictedSemantics getRestrictedSemantics();
	
	IRestrictedSemanticsSnapshot createAstSnapshot(IProject project, ASTNode node);
}
