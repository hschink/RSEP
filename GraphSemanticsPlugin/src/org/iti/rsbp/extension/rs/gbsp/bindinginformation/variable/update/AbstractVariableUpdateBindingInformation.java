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
package org.iti.rsbp.extension.rs.gbsp.bindinginformation.variable.update;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.iti.ast.ParserUtils;
import org.iti.rsbp.extension.rs.gbsp.model.Edge;
import org.iti.rsbp.extension.rs.gbsp.model.IEdge;
import org.iti.rsbp.extension.rs.gbsp.model.INode;
import org.iti.rsbp.extension.rs.gbsp.model.Node;
import org.iti.rsbp.extension.rs.gbsp.utils.VariableUtils;

public abstract class AbstractVariableUpdateBindingInformation extends AbstractUpdateBindingInformation {
	
	@Override
	protected List<ASTNode> getReferences() {
		List<ASTNode> references = ParserUtils.getReferences(getDeclaringNode());
		List<ASTNode> variableUpdatepdateReferences = getVariableUpdateReferences(references);
		
		return variableUpdatepdateReferences;
	}
	
	private List<ASTNode> getVariableUpdateReferences(List<ASTNode> references) {
		List<ASTNode> variableUpdatepdateReferences = new ArrayList<>();
		
		for (ASTNode node : references) {
			if (VariableUtils.isVariableUpdate(node))
				variableUpdatepdateReferences.add(node);
		}
		
		return variableUpdatepdateReferences;
	}
	
	@Override
	public INode BuildModel(List<ASTNode> references) {
		INode root = new Node("Root", "VARIABLE", getDeclaration());
		int nodeCounter = 0;
		
		for (ASTNode astNode : references) {
			INode leaf = new Node("EXPRESSION_" + nodeCounter, "EXPRESSION", getExpression(astNode));
			IEdge edge = new Edge("UPDATES", root, leaf);
			
			root.addEdge(edge);
			
			++nodeCounter;
		}
		
		return root;
	}

	@Override
	protected ASTNode getExpression(ASTNode node) {
		return VariableUtils.getUpdateExpression(node);
	}

	@Override
	protected ASTNode getExpectedExpression(ASTNode expected) {
		return (expected == null) ? null : VariableUtils.getUpdateExpression(expected);
	}

	@Override
	protected String getExpressionText(ASTNode expression,
			ASTNode expectedExpression, String additionalInformation) {
		
		String text = "Update Expression: ";
		
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
		
		if (expected != null) {
			if (expected instanceof VariableDeclarationFragment
					&& actual instanceof VariableDeclarationFragment) {
				
				return compare((VariableDeclarationFragment)expected, (VariableDeclarationFragment)actual);
			} else if (expected instanceof SimpleName
					&& actual instanceof SimpleName) {
				
				return compare((SimpleName)expected, (SimpleName)actual);
			}
		}
		
		return "";
	}

	public static String compare(VariableDeclarationFragment expected, VariableDeclarationFragment actual) {
		String result = "";
		
		if (expected.getInitializer() != null || actual.getInitializer() != null)
		{
			if (expected.getInitializer() != null && actual.getInitializer() != null) {
				if (expected.getInitializer().toString().compareTo(actual.getInitializer().toString()) != 0)
					result = "CHANGE: Initializer changed -- [" + expected.getInitializer().toString() + "]";
			}
			else if (expected.getInitializer() == null)
				result = "CHANGE: No initializer expected!";
			else
				result = "CHANGE: Initializer expected -- [" + expected.getInitializer().toString() + "]";
		}
		
		return result;
	}

	private static String compare(SimpleName expected, SimpleName actual) {
		String result = "";
		ASTNode expectedParent = expected.getParent();
		ASTNode actualParent = actual.getParent();
		
		if (expectedParent instanceof Assignment && actualParent instanceof Assignment) {
			Assignment expectedAssigment = (Assignment)expectedParent;
			Assignment actualAssignment = (Assignment)actualParent;
			
			if (actualAssignment.getLeftHandSide().toString().compareTo(expectedAssigment.getLeftHandSide().toString()) != 0)
				result += String.format("CHANGE: Value assigned to a different variable/field than %1$s!", expectedAssigment.getLeftHandSide());
			
			if (actualAssignment.getRightHandSide().toString().compareTo(expectedAssigment.getRightHandSide().toString()) != 0)
				result += ((result == "") ? "" : "\n\n") + "CHANGE: Different value assigned to the variable/field!";
		}
		
		return result;
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
		return String.format("CHANGE: Additional variable/field update [%1$s]!", node);
	}
}
