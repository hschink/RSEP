package org.iti.rsbp.extension.rs.gbsp.model.methods;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.iti.rsbp.extension.rs.gbsp.model.INode;

public interface IMethodDeclarationNode extends INode {

	MethodDeclaration getMethodDeclaration();
	void setMethodDeclaration(MethodDeclaration methodDeclaration);
}
