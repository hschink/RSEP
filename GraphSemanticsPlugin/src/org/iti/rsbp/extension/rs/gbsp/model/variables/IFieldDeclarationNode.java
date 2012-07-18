package org.iti.rsbp.extension.rs.gbsp.model.variables;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.iti.rsbp.extension.rs.gbsp.model.INode;

public interface IFieldDeclarationNode extends INode {
	
	FieldDeclaration getFieldDeclaration();
	void setFieldDeclaration();
}
