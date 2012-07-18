package org.iti.rsbp.extension.rs;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.dom.ASTNode;

public interface IRestrictedSemanticsSnapshot {

	ASTNode getAstNode();
	
	String getFullQualifiedAstNodeName();
	
	IProject getProject();
	
	void setProject(IProject project);
	
	void initMarkers();
	
	void updateMarkers();
	
	void deleteMarkers();

	void prepareRefactoring(RestrictedSemanticsRefactoringParticipantArguments refactoringArguments);
	
	void dispose();
}
