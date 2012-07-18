package org.iti.rsbp.extension.rs.gbsp;

import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;

public class GraphSemanticsRenameVariableRefactoringArguments extends
		GraphSemanticsLocalVariableRenameRefactoringParticipantArguments {

	public ILocalVariable element = null;
	public IMethod parentMethod = null;
	public String targetName = null;
	
	public GraphSemanticsRenameVariableRefactoringArguments(ILocalVariable element, IMethod parentMethod, String targetName) {
		this.element = element;
		this.parentMethod = parentMethod;
		this.targetName = targetName;
	}
}
