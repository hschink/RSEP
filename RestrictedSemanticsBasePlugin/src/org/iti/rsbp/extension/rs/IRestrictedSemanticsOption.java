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
package org.iti.rsbp.extension.rs;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.dom.ASTNode;

public interface IRestrictedSemanticsOption {
	String getId();

	boolean isValidForNode(IProject selectedProject, ASTNode node);
	
	IRestrictedSemantics getRestrictedSemantics();
	
	IRestrictedSemanticsSnapshot createAstSnapshot(IProject project, ASTNode node);
}
