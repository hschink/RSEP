package org.iti.rsbp.extension.rs.gbsp;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.refactoring.participants.ChangeMethodSignatureArguments;
import org.eclipse.jdt.core.refactoring.participants.ChangeMethodSignatureParticipant;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;

public class GraphSemanticsChangeMethodSignatureParticipant extends
		ChangeMethodSignatureParticipant {

	@Override
	protected boolean initialize(Object element) {
		ChangeMethodSignatureArguments arguments = getArguments();
		
		arguments.getNewParameters();
		
		return false;
	}

	@Override
	public String getName() {
		return "org.iti.rbsp.extension.rs.gbsp.GraphSemanticsChangeMethodSignatureParticipant";
	}

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws OperationCanceledException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException,
			OperationCanceledException {
		// TODO Auto-generated method stub
		return null;
	}

}
