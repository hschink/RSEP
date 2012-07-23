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
package org.iti.rsbp.extension.rs.gbsp.utils;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.iti.ast.ParserUtils;
import org.iti.rsbp.extension.rs.IRestrictedSemanticsSnapshot;
import org.iti.rsbp.extension.rs.gbsp.GraphSemantics.GraphSemanticsOptionType;
import org.iti.rsbp.extension.rs.gbsp.GraphSemanticsSnapshot;
import org.iti.rsbp.extension.rs.gbsp.bindinginformation.AbstractBindingInformation;
import org.iti.rsbp.extension.rs.gbsp.bindinginformation.IBindingInformation;

public class MethodUtils {
	public static IRestrictedSemanticsSnapshot createMethodAccessSnapshot(IProject project,
			ASTNode node, GraphSemanticsOptionType graphSemanticsOptionType) {
		IRestrictedSemanticsSnapshot result = null;
		IBindingInformation bindingInformation = null;
		
		List<ICompilationUnit> compilationUnits = ParserUtils.getCompilationUnitsOfNature(project, 
				"org.eclipse.jdt.core.javanature");
		
		System.out.println(node);
		
		MethodDeclaration methodDeclaration = null;
		
		if (node instanceof MethodDeclaration)
			methodDeclaration = getMethodDeclaration(compilationUnits, ((MethodDeclaration)node).resolveBinding());
		else if (node.getParent() instanceof MethodDeclaration)
			methodDeclaration = getMethodDeclaration(compilationUnits, ((MethodDeclaration)node.getParent()).resolveBinding());
		else if (node instanceof MethodInvocation)
			methodDeclaration = getMethodDeclaration(compilationUnits, (MethodInvocation)node);
		else if (node.getParent() instanceof MethodInvocation)
			methodDeclaration = getMethodDeclaration(compilationUnits, (MethodInvocation)node.getParent());
		else 
			getMethodDeclaration(compilationUnits, node);
		
		if (methodDeclaration != null) {
			
			bindingInformation = AbstractBindingInformation.bindingInformationFactory(methodDeclaration, org.iti.rsbp.extension.rs.gbsp.bindinginformation.BindingInformationType.INVOCATION);
			result = new GraphSemanticsSnapshot(bindingInformation);
		}
		
		return result;
	}
	
	private static MethodDeclaration getMethodDeclaration(List<ICompilationUnit> compilationUnits, MethodInvocation node) {
		
		MethodInvocation methodInvocation = (MethodInvocation)node;
		
		IMethodBinding binding = methodInvocation.resolveMethodBinding();
		
		IMethodBinding declarationBinding = binding.getMethodDeclaration();

		return getMethodDeclaration(compilationUnits, declarationBinding);
	}

	private static MethodDeclaration getMethodDeclaration(List<ICompilationUnit> compilationUnits, ASTNode node) {
		
		while (node != null && !(node instanceof MethodDeclaration)) {
			node = node.getParent();
		}
		
		if (node != null) {
			MethodDeclaration methodDeclaration = (MethodDeclaration)node;
			
			IMethodBinding declarationBinding = methodDeclaration.resolveBinding();
			
			return getMethodDeclaration(compilationUnits, declarationBinding);
		}
		
		return null;
	}

	private static MethodDeclaration getMethodDeclaration(List<ICompilationUnit> compilationUnits, IMethodBinding declarationBinding) {
		for (ICompilationUnit compilationUnit : compilationUnits) {
			CompilationUnit cu = ParserUtils.parse(compilationUnit);
			MethodDeclaration declarationNode = (MethodDeclaration)cu.findDeclaringNode(declarationBinding.getKey());
			
			if (declarationNode != null) {
				try
				{
					declarationNode.setProperty(IBindingInformation.ICOMPILATION_PROPERTY, compilationUnit);
				} catch (NullPointerException ex) {
					System.out.println("Eeek!!!");
				}
				
				return declarationNode;
			}
		}
		
		return null;
	}
	
	public static ASTNode getMethodDeclaration(String identifier) {
		IProject project = ParserUtils.getSelectedProject();
		
		List<ICompilationUnit> compilationUnits = ParserUtils.getCompilationUnitsOfNature(project, 
				"org.eclipse.jdt.core.javanature");
		
		for (ICompilationUnit compilationUnit : compilationUnits) {
			CompilationUnit cu = ParserUtils.parse(compilationUnit);
			
			ASTNode node = cu.findDeclaringNode(identifier);
			
			if (node != null)
			{
				node.setProperty(IBindingInformation.ICOMPILATION_PROPERTY, compilationUnit);
				
				return node;
			}
		}
		
		return null;
	}

	public static boolean isMethodInvocation(ASTNode node) {
		return (node instanceof MethodInvocation);
	}

	public static ASTNode getInvocationExpression(ASTNode node) {
		if (node instanceof MethodInvocation) {
			MethodInvocation methodInvocation = (MethodInvocation)node;
			
			return methodInvocation.getName();
		}
		
		return null;
	}

	public static ASTNode updateMethodDeclaration(String identifier) {
		ASTNode node = getMethodDeclaration(identifier);
		
		if (node == null) {
			System.err.println("Method declaration node must not be null!");
			return null;
		}
		
		return node;
	}
	
	
}
