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
package org.iti.rsbp.extension.rs.gbsp.bindinginformation.variable.access;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.iti.ast.ParserUtils;
import org.iti.rsbp.extension.rs.gbsp.model.Edge;
import org.iti.rsbp.extension.rs.gbsp.model.IEdge;
import org.iti.rsbp.extension.rs.gbsp.model.INode;
import org.iti.rsbp.extension.rs.gbsp.model.Node;
import org.iti.rsbp.extension.rs.gbsp.utils.VariableUtils;

public abstract class AbstractVariableAccessBindingInformation extends AbstractAccessBindingInformation {
	
	@Override
	protected List<ASTNode> getReferences() {
		List<ASTNode> references = ParserUtils.getReferences(getDeclaringNode());
		List<ASTNode> variableAccessReferences = getVariableUpdateReferences(references);
		
		return variableAccessReferences;
	}
	
	private List<ASTNode> getVariableUpdateReferences(List<ASTNode> references) {
		List<ASTNode> variableAccessReferences = new ArrayList<>();
		
		for (ASTNode node : references) {
			if (VariableUtils.isVariableAccess(node))
				variableAccessReferences.add(node);
		}
		
		return variableAccessReferences;
	}
	
	@Override
	public INode BuildModel(List<ASTNode> references) {
		INode root = new Node("Root", "VARIABLE", getDeclaration());
		int nodeCounter = 0;
		
		for (ASTNode astNode : references) {
			INode leaf = new Node("EXPRESSION_" + nodeCounter, "EXPRESSION", getExpression(astNode));
			IEdge edge = new Edge("ACCESSES", root, leaf);
			
			root.addEdge(edge);
			
			++nodeCounter;
		}
		
		return root;
	}

	@Override
	protected ASTNode getExpression(ASTNode node) {
		return VariableUtils.getAccessExpression(node);
	}

	@Override
	protected ASTNode getExpectedExpression(ASTNode expected) {
		return (expected == null) ? null : VariableUtils.getAccessExpression(expected);
	}

	@Override
	protected String getExpressionText(ASTNode expression,
			ASTNode expectedExpression, String additionalInformation) {
		
		String text = "Access Expression: ";
		
		text += (expectedExpression == null) ? expression.toString() : expectedExpression.toString();
		
		if (additionalInformation != "")
			text += "\n\n" + additionalInformation;
		
		return text;
	}

	@Override
	protected String getDeclarationText(ASTNode declaration,
			ASTNode expectedDeclaration, String additionalInformation) {
		
		String text = "Parameter Declaration: ";
		
		text += (expectedDeclaration == null) ? declaration.toString() : expectedDeclaration.toString();
							
		if (additionalInformation != "")
			text += "\n\n" + additionalInformation;
		
		return text;
	}

	@Override
	protected ASTNode getDeclaration() {
		return VariableUtils.getVariableUpdateAstNode(getDeclaringNode());
	}

	@Override
	protected ASTNode getExpectedDeclaration(ASTNode expected) {
		return VariableUtils.getVariableUpdateAstNode(expected);
	}
	
	@Override
	protected ASTNode updateDeclaration() {
		return VariableUtils.updateVariableDeclaration(getIdentifier());
	}
	
	@Override
	protected String compare(ASTNode actual, ASTNode expected) {
		
		return "";
	}
	
	@Override
	protected String getMarkerIdForUnprocessedNode(ASTNode node) {
		return ERROR_MARKER_ID;
	}
	
	@Override
	protected String getTextForUnprocessedNode(ASTNode node) {
		return String.format("CHANGE: Update of variable/field [%1$s] missing!", node);
	}
	
	@Override
	protected String getMarkerIdForNewNode(ASTNode node) {
		return ERROR_MARKER_ID;
	}
	
	@Override
	protected String getTextForNewNode(ASTNode node) {
		return String.format("CHANGE: Additional variable/field access [%1$s]!", node);
	}
}
