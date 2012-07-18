package org.iti.rsbp.extension.rs.gbsp.utils;

import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.iti.ast.ContainsSimpleNameVisitor;
import org.iti.ast.ParserUtils;
import org.iti.rsbp.extension.rs.IRestrictedSemanticsSnapshot;
import org.iti.rsbp.extension.rs.gbsp.GraphSemantics.GraphSemanticsOptionType;
import org.iti.rsbp.extension.rs.gbsp.GraphSemanticsSnapshot;
import org.iti.rsbp.extension.rs.gbsp.bindinginformation.AbstractBindingInformation;
import org.iti.rsbp.extension.rs.gbsp.bindinginformation.IBindingInformation;

public class VariableUtils {

	public static ASTNode getUpdateExpression(ASTNode node) {

		if (node instanceof SimpleName || node instanceof VariableDeclarationStatement || node.getParent() instanceof Assignment)
			return getUpdateExpression(node.getParent());
		else if (node instanceof VariableDeclarationFragment) {
			VariableDeclarationFragment fragment = (VariableDeclarationFragment)node;
			ASTNode initializer = fragment.getInitializer();
			
			return initializer;
		}
		else if (node instanceof PostfixExpression || node instanceof PrefixExpression)
			return node;
		else if (node instanceof Assignment) {
			Assignment assigment = (Assignment)node;
			
			return assigment.getRightHandSide();
		}
		
		return null;
	}

	public static ASTNode getVariableUpdateAstNode(ASTNode node) {
		if (node instanceof VariableDeclarationFragment || node instanceof SingleVariableDeclaration || node == null)
			return node;
		
		return getVariableUpdateAstNode(node.getParent());
	}
	
	public static ASTNode getDeclaringVariableNode(List<ICompilationUnit> compilationUnits, SimpleName simpleName) {
		IVariableBinding variableBinding = getVariableBinding(simpleName);
		
		if (variableBinding != null) {
			IVariableBinding declarationBinding = variableBinding.getVariableDeclaration();

			for (ICompilationUnit compilationUnit : compilationUnits) {
				CompilationUnit cu = ParserUtils.parse(compilationUnit);
				ASTNode declarationNode = cu.findDeclaringNode(declarationBinding.getKey());
				
				if (declarationBinding != null) {
					declarationNode.setProperty(IBindingInformation.ICOMPILATION_PROPERTY, compilationUnit);
					
					return declarationNode;
				}
			}
		}
		
		return null;
	}
	
	public static IVariableBinding getVariableBinding(SimpleName simpleName) {
		IBinding binding = simpleName.resolveBinding();
		
		if (binding instanceof IVariableBinding)
			return (IVariableBinding)binding;
		
		return null;
	}
	
	public static IRestrictedSemanticsSnapshot createVariableUpdateSnapshot(IProject project,
			ASTNode node, GraphSemanticsOptionType graphSemanticsOptionType) {
		IRestrictedSemanticsSnapshot result = null;
		IBindingInformation bindingInformation = null;
		
		List<ICompilationUnit> compilationUnits = ParserUtils.getCompilationUnitsOfNature(project, 
				"org.eclipse.jdt.core.javanature");
		
		SimpleName simpleName = getSimpleNameOfVariableOrFieldNode(node);
		
		if (simpleName != null) {
			ASTNode declarationNode = getDeclaringVariableNode(compilationUnits, simpleName);
			
			switch (graphSemanticsOptionType) {
				case VariableAccess:
					bindingInformation = AbstractBindingInformation.bindingInformationFactory(declarationNode, org.iti.rsbp.extension.rs.gbsp.bindinginformation.BindingInformationType.ACCESS);
					break;
			
				case VariableUpdate:
					bindingInformation = AbstractBindingInformation.bindingInformationFactory(declarationNode, org.iti.rsbp.extension.rs.gbsp.bindinginformation.BindingInformationType.UPDATE);
					break;
					
				default:
					break;
			}
			
			switch (graphSemanticsOptionType) {
				case VariableUpdate:
					result = new GraphSemanticsSnapshot(bindingInformation);
					break;
					
				case VariableAccess:
					result = new GraphSemanticsSnapshot(bindingInformation);
					break;
					
				default:
					break;
			}
		}
		
		return result;
	}
	
	private static SimpleName getSimpleNameOfVariableOrFieldNode(ASTNode node) {		
		if (node instanceof VariableDeclarationStatement) {
			return getVariableDeclarationName((VariableDeclarationStatement)node);
		}
		else if (node instanceof VariableDeclarationFragment) {
			VariableDeclarationFragment fragment = (VariableDeclarationFragment)node;
			VariableDeclarationExpression expression = (VariableDeclarationExpression) fragment.getParent();
			
			for (Object n : expression.fragments())
				if (n instanceof SimpleName)
					return (SimpleName)n;
			
		} else if (node instanceof SimpleName) {
			return (SimpleName)node;
		}
		
		return null;
	}
	
	private static SimpleName getVariableDeclarationName(VariableDeclarationStatement node) {		
		for (Object o : node.fragments())
			if (o instanceof VariableDeclarationFragment)
				return ((VariableDeclarationFragment)o).getName();
		
		return null;
	}
	
	public static IBindingInformation getBindingInformation(IMarker marker, String bindingAttributeId) {
		IBindingInformation bindingInformation = null;
		
		try {
			bindingInformation = (IBindingInformation)marker.getAttribute(bindingAttributeId);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		return bindingInformation;
	}
	
	public static ASTNode getVariableDeclaration(String identifier) {
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
	
	public static ASTNode updateVariableDeclaration(String identifier) {
		ASTNode node = VariableUtils.getVariableDeclaration(identifier);
		
		if (node == null) {
			System.err.println("Variable Update declaration node must not be null!");
			return null;
		}
		
		return node;
	}

	public static boolean isVariableUpdate(ASTNode node) {
		if (node instanceof SimpleName && ((SimpleName)node).resolveBinding() instanceof IVariableBinding) {		
			ASTNode parent = node.getParent();
			
			if (parent instanceof VariableDeclarationFragment) {
				VariableDeclarationFragment fragment = (VariableDeclarationFragment)parent;
				
				if (fragment.getInitializer() != null)
					return false;
			}
			else if (parent instanceof PostfixExpression || parent instanceof PrefixExpression)
				return true;
			else if (parent instanceof Assignment) {
				Assignment assigment = (Assignment)parent;
				ASTNode assignee = assigment.getLeftHandSide();
				
				if (assignee == node)
					return true;
			}
		}
		
		return false;
	}

	public static boolean isVariableAccess(ASTNode node) {
		if (node instanceof SimpleName && ((SimpleName)node).resolveBinding() instanceof IVariableBinding) {		
			ASTNode parent = node.getParent();
			
			return isVariableAccess(node, parent);
		}
		
		return false;
	}

	private static boolean isVariableAccess(ASTNode node, ASTNode parent) {
		
		if (parent == null)
			return false;
		else if (parent instanceof Assignment) {
			Assignment assigment = (Assignment)parent;
			Expression expression = assigment.getRightHandSide();
			ContainsSimpleNameVisitor visitor = new ContainsSimpleNameVisitor(node);
			
			expression.accept(visitor);
			
			return visitor.isNodeFound();
		} else if (parent instanceof MethodInvocation) {
			MethodInvocation methodInvocation = (MethodInvocation)parent;
			List<?> expressions = methodInvocation.arguments();
			ContainsSimpleNameVisitor visitor = new ContainsSimpleNameVisitor(node);
			
			for (Object o : expressions)
			{
				if (o instanceof ASTNode) {
					ASTNode parameter = (ASTNode)o;
					parameter.accept(visitor);
				
					if (visitor.isNodeFound())
						return true;
				}
			}
			
			return false;
		}
		
		return isVariableAccess(node, parent.getParent());
	}

	public static ASTNode getAccessExpression(ASTNode node) {
		if (node instanceof SimpleName)
			return node.getParent();
		
		return node;
	}
}
