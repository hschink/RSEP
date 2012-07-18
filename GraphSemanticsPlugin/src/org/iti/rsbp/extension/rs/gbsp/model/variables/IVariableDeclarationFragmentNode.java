package org.iti.rsbp.extension.rs.gbsp.model.variables;

import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.iti.rsbp.extension.rs.gbsp.model.INode;

public interface IVariableDeclarationFragmentNode extends INode {

	VariableDeclarationFragment getVariableDeclarationFragment();
	void setVariableDeclarationFragment(VariableDeclarationFragment variableDeclarationFragment);
}
