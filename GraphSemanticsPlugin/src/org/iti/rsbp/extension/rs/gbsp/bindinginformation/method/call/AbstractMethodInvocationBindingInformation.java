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
package org.iti.rsbp.extension.rs.gbsp.bindinginformation.method.call;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.iti.ast.ParserUtils;
import org.iti.rsbp.extension.rs.gbsp.utils.MethodUtils;
import org.iti.rsbp.extension.rs.gbsp.utils.VariableUtils;

public abstract class AbstractMethodInvocationBindingInformation extends AbstractInvocationBindingInformation {
	
	@Override
	protected List<ASTNode> getReferences() {
		List<ASTNode> references = ParserUtils.getReferences(getDeclaringNode());
		List<ASTNode> methodInvocationReferences = getMethodInvocationReferences(references);
		
		return methodInvocationReferences;
	}
	
	private List<ASTNode> getMethodInvocationReferences(List<ASTNode> references) {
		List<ASTNode> methodInvocationReferences = new ArrayList<>();
		
		for (ASTNode node : references) {
			if (MethodUtils.isMethodInvocation(node))
				methodInvocationReferences.add(node);
		}
		
		return methodInvocationReferences;
	}

	@Override
	protected ASTNode getExpression(ASTNode node) {
		return MethodUtils.getInvocationExpression(node);
	}

	@Override
	protected ASTNode getExpectedExpression(ASTNode expected) {
		return (expected == null) ? null : VariableUtils.getAccessExpression(expected);
	}

	@Override
	protected String getExpressionText(ASTNode expression,
			ASTNode expectedExpression, String additionalInformation) {
		
		String text = "Invocation Expression: ";
		
		text += (expectedExpression == null) ? expression.toString() : expectedExpression.toString();
		
		if (additionalInformation != "")
			text += "\n\n" + additionalInformation;
		
		return text;
	}

	@Override
	protected String getDeclarationText(ASTNode declaration,
			ASTNode expectedDeclaration, String additionalInformation) {
		
		String text = "Method Definition: ";
		
		text += (expectedDeclaration == null) ? declaration.toString() : expectedDeclaration.toString();
							
		if (additionalInformation != "")
			text += "\n\n" + additionalInformation;
		
		return text;
	}

	@Override
	protected ASTNode getDeclaration() {
		return getDeclaringNode();
	}

	@Override
	protected ASTNode getExpectedDeclaration(ASTNode expected) {
		return expected;
	}
	
	@Override
	protected ASTNode updateDeclaration() {
		return MethodUtils.updateMethodDeclaration(getIdentifier());
	}
	
	@Override
	protected String compare(ASTNode actual, ASTNode expected) {
		
		if (actual instanceof MethodDeclaration && expected instanceof MethodDeclaration)
			return compare((MethodDeclaration)actual, (MethodDeclaration)expected);
		else if (actual instanceof MethodInvocation && expected instanceof MethodInvocation)
			return compare((MethodInvocation)actual, (MethodInvocation)expected);
		
		return "";
	}
	
	private String compare(MethodDeclaration actual, MethodDeclaration expected) {
		String result = "";		
		boolean sameModifiers = actual.getModifiers() == expected.getModifiers();
		boolean sameReturnType = actual.getReturnType2().toString().compareTo(expected.getReturnType2().toString()) == 0;
		boolean sameParameters = compareParameterList(actual.parameters(), expected.parameters());
		
		if (!(sameModifiers && sameReturnType && sameParameters)) {
			result = "CHANGE: Method signature is modified!";
		}
		
		return result;
	}
	
	private String compare(MethodInvocation actual, MethodInvocation expected) {
		String result = "";		
		boolean sameArguments = compareParameterList(actual.arguments(), expected.arguments());
		
		if (!sameArguments) {
			result = "CHANGE: Argument list is modified!";
		}
		
		return result;
	}

	private boolean compareParameterList(List<?> actual,
			List<?> expected) {
		
		boolean sameParameters = actual.size() == expected.size();
		
		if (sameParameters) {
			for (int x = 0; x < actual.size(); x++) {
				sameParameters = actual.get(x).toString().compareTo(expected.get(x).toString()) == 0;
				
				if (!sameParameters)
					break;
			}
		}
		
		return sameParameters;
	}
}
