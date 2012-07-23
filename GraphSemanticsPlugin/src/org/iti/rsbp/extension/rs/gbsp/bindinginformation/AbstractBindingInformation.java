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
package org.iti.rsbp.extension.rs.gbsp.bindinginformation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.iti.ast.MarkerUtils;
import org.iti.ast.ParserUtils;
import org.iti.rsbp.extension.rs.RestrictedSemanticsRefactoringParticipantArguments;
import org.iti.rsbp.extension.rs.gbsp.bindinginformation.method.call.MethodInvocationBindingInformation;
import org.iti.rsbp.extension.rs.gbsp.bindinginformation.variable.access.FieldDeclarationAccessBindingInformation;
import org.iti.rsbp.extension.rs.gbsp.bindinginformation.variable.access.SingleVariableDeclarationAccessBindingInformation;
import org.iti.rsbp.extension.rs.gbsp.bindinginformation.variable.access.VariableDeclarationFragmentAccessBindingInformation;
import org.iti.rsbp.extension.rs.gbsp.bindinginformation.variable.update.FieldDeclarationUpdateBindingInformation;
import org.iti.rsbp.extension.rs.gbsp.bindinginformation.variable.update.SingleVariableDeclarationUpdateBindingInformation;
import org.iti.rsbp.extension.rs.gbsp.bindinginformation.variable.update.VariableDeclarationFragmentUpdateBindingInformation;
import org.iti.rsbp.extension.rs.gbsp.model.IEdge;
import org.iti.rsbp.extension.rs.gbsp.model.INode;
import org.iti.rsbp.extension.rs.gbsp.model.INodeComparisonResult;

public abstract class AbstractBindingInformation implements IBindingInformation {
	
	public final static String COMPILATION_UNIT = "CompilationUnit";
	public final static String NODE = "Node";
	public final static String EXPRESSION = "Expression";
	public final static String POSITION = "Position";
	public final static String STATE = "State";
	public final static String MARKER = "Marker";
	
	public static IBindingInformation bindingInformationFactory(Object declaration, BindingInformationType type) {
		
		if (declaration == null)
			throw new NullPointerException("declaration must not be null!");
		
		switch (type) {
			case ACCESS:
				if (declaration instanceof SingleVariableDeclaration) {
					return new SingleVariableDeclarationAccessBindingInformation((SingleVariableDeclaration)declaration);					
				} else if (declaration instanceof VariableDeclarationFragment)
					return new VariableDeclarationFragmentAccessBindingInformation((VariableDeclarationFragment)declaration);
				else if (declaration instanceof FieldDeclaration)
					return new FieldDeclarationAccessBindingInformation((FieldDeclaration)declaration);
				else if (declaration instanceof SimpleName)
					return bindingInformationFactory(((SimpleName)declaration).getParent(), type);
		
			case UPDATE:
				if (declaration instanceof SingleVariableDeclaration) {
					return new SingleVariableDeclarationUpdateBindingInformation((SingleVariableDeclaration)declaration);					
				} else if (declaration instanceof VariableDeclarationFragment)
					return new VariableDeclarationFragmentUpdateBindingInformation((VariableDeclarationFragment)declaration);
				else if (declaration instanceof FieldDeclaration)
					return new FieldDeclarationUpdateBindingInformation((FieldDeclaration)declaration);
				else if (declaration instanceof SimpleName)
					return bindingInformationFactory(((SimpleName)declaration).getParent(), type);
				
			case INVOCATION:
				if (declaration instanceof MethodDeclaration) {
					return new MethodInvocationBindingInformation((MethodDeclaration)declaration);
				}
		}
		
		
		throw new UnsupportedOperationException("Type is not handled!");
	}
	
	@Override
	public abstract String getIdentifier();

	@Override
	public abstract ASTNode getDeclaringNode();

	@Override
	public abstract void setDeclaringNode(ASTNode node);
	
	@Override
	public abstract INode BuildModel(List<ASTNode> references);

	@Override
	public void initMarkers() {
		List<ASTNode> references = getReferences();

		setRoot(BuildModel(references));
		
		BuildMarker(getRoot());
	}

	private void BuildMarker(INode root) {
		CompilationUnit cu = ParserUtils.getCompilationUnit(root.getExpression());
		IResource resource = cu.getJavaElement().getResource();

		IMarker marker = MarkerUtils.createMarker(resource, getDeclarationMarkerId());
		setMarkerInformation(cu, marker, root.getExpression(), null, "", IMarker.SEVERITY_INFO, 0);
		root.setAttribute(MARKER, marker);
		
		for (IEdge edge : root.getEdges()) {
			cu = ParserUtils.getCompilationUnit(edge.getTail().getExpression());
			resource = cu.getJavaElement().getResource();
			
			marker = MarkerUtils.createMarker(resource, getMarkerId());
			setMarkerInformation(cu, marker, edge.getTail().getExpression(), null, "", IMarker.SEVERITY_INFO, 0);
			edge.getTail().setAttribute(MARKER, marker);
		}
	}

	private void setMarkerInformation(CompilationUnit cu, IMarker marker,
			ASTNode node, ASTNode expected, String info, int severity, int position) {
		
			ASTNode expression = getExpression(node);
			ASTNode expectedExpression = getExpectedExpression(expected);
			
			if (expression != null) {
				String text = getExpressionText(expression, expectedExpression, info);
				
				MarkerUtils.setAttribute(marker, IMarker.CHAR_START, expression.getStartPosition());
				MarkerUtils.setAttribute(marker, IMarker.CHAR_END, expression.getStartPosition() + expression.getLength());
				MarkerUtils.setAttribute(marker, IMarker.MESSAGE, text);
				
				MarkerUtils.setAttribute(marker, EXPRESSION, expression);
			} else if (MarkerUtils.getMarkerType(marker) == getDeclarationMarkerId() || MarkerUtils.getMarkerType(marker) == getDeclarationErrorMarkerId()) {
				ASTNode declaration = getDeclaration();
				ASTNode expectedDeclaration = getExpectedDeclaration(expected);
				
				if (declaration != null) {
					String text = getDeclarationText(declaration, expectedDeclaration, info);
					
					MarkerUtils.setAttribute(marker, IMarker.CHAR_START, getDeclarationStartPosition());
					MarkerUtils.setAttribute(marker, IMarker.CHAR_END, getDeclarationEndPosition());
					MarkerUtils.setAttribute(marker, IMarker.MESSAGE, text);
				}
			}
			
			MarkerUtils.setAttribute(marker, getBindingType(), this);
			MarkerUtils.setAttribute(marker, COMPILATION_UNIT, cu);
			MarkerUtils.setAttribute(marker, NODE, (expected == null) ? node : expected);
			MarkerUtils.setAttribute(marker, POSITION, position);
			MarkerUtils.setAttribute(marker, IMarker.SEVERITY, severity);
	}

	protected abstract ASTNode getExpression(ASTNode node);

	protected abstract ASTNode getExpectedExpression(ASTNode expected);

	protected abstract String getExpressionText(ASTNode expression,
			ASTNode expectedExpression, String additionalInformation);

	protected abstract ASTNode getDeclaration();

	protected abstract ASTNode getExpectedDeclaration(ASTNode expected);

	protected abstract String getDeclarationText(ASTNode declaration,
			ASTNode expectedDeclaration, String additionalInformation);
	
	protected abstract int getDeclarationStartPosition();
	
	protected abstract int getDeclarationEndPosition();

	protected abstract List<ASTNode> getReferences();

	@Override
	public void updateMarkers() {
		
		ASTNode declarationNode = updateDeclaration();
		
		if (declarationNode != null) {
			setDeclaringNode(declarationNode);
			
			List<ASTNode> references = getReferences();
			
			INode updatedRoot = BuildModel(references);
			
			INodeComparisonResult result = getRoot().compareTo(updatedRoot);
			
			CompilationUnit cu = ParserUtils.getCompilationUnit(declarationNode);
			IResource resource = cu.getJavaElement().getResource();
			
			deleteMarkersWithState(STATE, NodeState.NEW);
			
			for (INode unchangedNode : result.getUnchangedNodes()) {
				IMarker marker = (IMarker)unchangedNode.getAttribute(MARKER);
				
				cu = ParserUtils.getCompilationUnit(unchangedNode.getExpression());
				resource = cu.getJavaElement().getResource();
				
				if (marker.exists() && MarkerUtils.getMarkerType(marker).compareTo(getErrorMarkerId()) == 0) {
					MarkerUtils.deleteMarker(marker);
					
					marker = MarkerUtils.createMarker(resource, getMarkerId());
					unchangedNode.setAttribute(MARKER, marker);
					setMarkerInformation(cu, marker, unchangedNode.getExpression(), unchangedNode.getExpression(), "", IMarker.SEVERITY_INFO, 0);
				} else if (marker.exists() && MarkerUtils.getMarkerType(marker).compareTo(getDeclarationErrorMarkerId()) == 0) {
					MarkerUtils.deleteMarker(marker);
					
					marker = MarkerUtils.createMarker(resource, getDeclarationMarkerId());
					unchangedNode.setAttribute(MARKER, marker);
					setMarkerInformation(cu, marker, unchangedNode.getExpression(), unchangedNode.getExpression(), "", IMarker.SEVERITY_INFO, 0);
				}
			}
			
			for (INode modifiedNode : result.getModifiedNodes().keySet()) {
				IMarker marker = (IMarker)modifiedNode.getAttribute(MARKER);
				MarkerUtils.deleteMarker(marker);
				
				cu = ParserUtils.getCompilationUnit(modifiedNode.getExpression());
				resource = cu.getJavaElement().getResource();
				
				if (modifiedNode == getRoot())
					marker = MarkerUtils.createMarker(resource, getDeclarationErrorMarkerId());
				else
					marker = MarkerUtils.createMarker(resource, getErrorMarkerId());
				
				modifiedNode.setAttribute(MARKER, marker);
				setMarkerInformation(cu, marker, result.getModifiedNodes().get(modifiedNode).getExpression(), modifiedNode.getExpression(), "", IMarker.SEVERITY_WARNING, 0);
			}
			
			for (INode missingNode : result.getMissingNodes()) {
				IMarker marker = (IMarker)missingNode.getAttribute(MARKER);
				
				MarkerUtils.deleteMarker(marker);
				
				String markerId = getMarkerIdForUnprocessedNode(missingNode.getExpression());
				String text = getTextForUnprocessedNode(missingNode.getExpression());
				
				cu = ParserUtils.getCompilationUnit(missingNode.getExpression());
				resource = cu.getJavaElement().getResource();
				
				marker = MarkerUtils.createMarker(resource, markerId);
				missingNode.setAttribute(MARKER, marker);
				setMarkerInformation(cu, marker, missingNode.getExpression(), null, text, IMarker.SEVERITY_WARNING, 0);
			}
			
			for (INode newNode : result.getNewNodes()) {
				String markerId = getMarkerIdForNewNode(newNode.getExpression());
				String text = getTextForNewNode(newNode.getExpression());
				
				cu = ParserUtils.getCompilationUnit(newNode.getExpression());
				resource = cu.getJavaElement().getResource();
				
				IMarker marker = MarkerUtils.createMarker(resource, markerId);
				setMarkerInformation(cu, marker, newNode.getExpression(), null, text, IMarker.SEVERITY_WARNING, 0);
				MarkerUtils.setAttribute(marker, STATE, NodeState.NEW);
			}
			
		} else {
			String title = "Error processing variable updates!";
			String message = "An error occured during processing variable updates for [" + getIdentifier() + "]!";
			
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			MessageDialog.openInformation(shell, title, message);
		}
	}

	private void deleteMarkersWithState(String attribute, NodeState state) {
		List<IMarker> markers = getMarkers(getBindingType());
		
		for (IMarker marker : markers) {
			if (MarkerUtils.getMarkerAttribute(marker, attribute) == state)
				MarkerUtils.deleteMarker(marker);
		}
	}

	@Override
	public void deleteMarkers() {
		String bindingType = getBindingType();
		
		List<IMarker> markers = getMarkers(bindingType);
		
		for (IMarker marker : markers)
			MarkerUtils.setAttribute(marker, bindingType, null);
		
		MarkerUtils.deleteMarkers(markers);
	}
	
	protected abstract String getBindingType();

	protected List<IMarker> getMarkers(String bindingType) {
		IProject project = ParserUtils.getProject(getDeclaration());
		
		String markerId = getMarkerId();
		String declarationMarkerId = getDeclarationMarkerId();
		String errorMarkerId = getErrorMarkerId();
		String declarationErrorMarkerId = getDeclarationErrorMarkerId();
		
		List<IMarker> markers = new ArrayList<>();
		List<IMarker> validMarkers = new ArrayList<>();

		markers.addAll(MarkerUtils.getMarkersInProject(project, markerId));
		markers.addAll(MarkerUtils.getMarkersInProject(project, declarationMarkerId));
		
		markers.addAll(MarkerUtils.getMarkersInProject(project, errorMarkerId));
		markers.addAll(MarkerUtils.getMarkersInProject(project, declarationErrorMarkerId));
		
		for (IMarker marker : markers) {
			IBindingInformation bindingInformation = (IBindingInformation)MarkerUtils.getMarkerAttribute(marker, bindingType);
			
			if (bindingInformation.getIdentifier().compareTo(getIdentifier()) == 0)
				validMarkers.add(marker);
		}
		
		return validMarkers;
	}
	
	protected abstract ASTNode updateDeclaration();

	protected abstract String getMarkerId();

	protected abstract String getDeclarationMarkerId();

	protected abstract String getErrorMarkerId();

	protected abstract String getDeclarationErrorMarkerId();
	
	protected abstract String compare(ASTNode actual, ASTNode expected);
	
	protected abstract String getMarkerIdForUnprocessedNode(ASTNode node);
	
	protected abstract String getTextForUnprocessedNode(ASTNode node);
	
	protected abstract String getMarkerIdForNewNode(ASTNode node);
	
	protected abstract String getTextForNewNode(ASTNode node);

	@Override
	public abstract void prepareRefactoring(
			RestrictedSemanticsRefactoringParticipantArguments refactoringArguments);

	@Override
	public void dispose() {
		deleteMarkers();
	}
}
