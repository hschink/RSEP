package org.iti.rsbp.extension.rs.gbsp.model.variables;

import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.iti.rsbp.extension.rs.gbsp.model.INode;

public interface ISingleVariableDeclaration extends INode {
	
	SingleVariableDeclaration getSingleVariableDeclaration();
	void setSingleVariableDeclaration(SingleVariableDeclaration singleVariableDeclaration);
}
