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

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.dom.ASTNode;
import org.iti.rsbp.extension.rs.IRestrictedSemanticsSnapshot;
import org.iti.rsbp.extension.rs.RestrictedSemanticsRefactoringParticipantArguments;
import org.iti.rsbp.extension.rs.gbsp.bindinginformation.IBindingInformation;

public class GraphSemanticsSnapshot implements IRestrictedSemanticsSnapshot {

	private ASTNode node = null;
	
	private IProject project = null;
	
	private IBindingInformation bindingInformation = null;
	
	public GraphSemanticsSnapshot(IBindingInformation bindingInformation) {
		this.bindingInformation = bindingInformation;
	}

	@Override
	public ASTNode getAstNode() {
		return node;
	}
	
	@Override
	public IProject getProject() {
		return project;
	}

	@Override
	public void setProject(IProject project) {
		this.project = project;
	}
	
	@Override
	public String getFullQualifiedAstNodeName() {
		return bindingInformation.getIdentifier();
	}
	
	@Override
	public void initMarkers() {
		if (bindingInformation != null) {
			bindingInformation.initMarkers();
		}
	}
	
	@Override
	public void updateMarkers() {
		if (bindingInformation != null)
			bindingInformation.updateMarkers();
	}
	
	@Override
	public void deleteMarkers() {
		if (bindingInformation != null)
			bindingInformation.deleteMarkers();
	}
	
	@Override
	public void prepareRefactoring(RestrictedSemanticsRefactoringParticipantArguments refactoringArguments) {
		if (bindingInformation != null)
			bindingInformation.prepareRefactoring(refactoringArguments);
	}
	
	@Override
	public void dispose() {
		if (bindingInformation != null)
			bindingInformation.dispose();
	}
}
