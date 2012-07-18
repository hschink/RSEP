package org.iti.rsbp.extension.rs.gbsp;

import org.eclipse.jdt.core.IField;

public class GraphSemanticsRenameFieldRefactoringArguments extends
		GraphSemanticsLocalVariableRenameRefactoringParticipantArguments {
	
	public IField element = null;
	public String targetName = null;
	
	public GraphSemanticsRenameFieldRefactoringArguments(IField element, String targetName) {
		this.element = element;
		this.targetName = targetName;
	}
}
