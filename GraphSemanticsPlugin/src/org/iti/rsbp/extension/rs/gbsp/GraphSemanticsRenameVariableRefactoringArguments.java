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
