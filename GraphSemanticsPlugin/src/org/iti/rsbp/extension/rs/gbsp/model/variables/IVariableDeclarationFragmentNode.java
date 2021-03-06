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
package org.iti.rsbp.extension.rs.gbsp.model.variables;

import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.iti.rsbp.extension.rs.gbsp.model.INode;

public interface IVariableDeclarationFragmentNode extends INode {

	VariableDeclarationFragment getVariableDeclarationFragment();
	void setVariableDeclarationFragment(VariableDeclarationFragment variableDeclarationFragment);
}
