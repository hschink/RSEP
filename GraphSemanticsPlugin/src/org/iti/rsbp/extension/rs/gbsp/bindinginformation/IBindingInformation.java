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
package org.iti.rsbp.extension.rs.gbsp.bindinginformation;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.iti.rsbp.extension.rs.RestrictedSemanticsRefactoringParticipantArguments;
import org.iti.rsbp.extension.rs.gbsp.model.INode;

public interface IBindingInformation {

	public final static String ICOMPILATION_PROPERTY = "ICOMPILATION_PROPERTY";
	
	String getIdentifier();
	
	ASTNode getDeclaringNode();
	
	void setDeclaringNode(ASTNode node);
	
	INode BuildModel(List<ASTNode> references);
	
	INode getRoot();
	
	void setRoot(INode root);
	
	void initMarkers();
	
	void updateMarkers();
	
	void deleteMarkers();

	void prepareRefactoring(RestrictedSemanticsRefactoringParticipantArguments refactoringArguments);
	
	void dispose();
}
