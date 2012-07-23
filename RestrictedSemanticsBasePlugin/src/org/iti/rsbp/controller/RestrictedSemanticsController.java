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
package org.iti.rsbp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jdt.core.dom.ASTNode;
import org.iti.ast.ParserUtils;
import org.iti.rsbp.extension.rs.IRestrictedSemantics;
import org.iti.rsbp.extension.rs.IRestrictedSemanticsOption;
import org.iti.rsbp.extension.rs.IRestrictedSemanticsSnapshot;
import org.iti.rsbp.extension.rs.RestrictedSemanticsRefactoringParticipantArguments;

public class RestrictedSemanticsController {
	
	public enum RestrictedSemanticsViewState {
		NoState,
		Start,
		Compare
	}

	private static final String IRESTRICTED_SEMANTICS_ID = "org.iti.rsbp.extension.rs";
	
	private static Map<IRestrictedSemantics, IRestrictedSemanticsOption[]> restrictedSemantics = new HashMap<>();
	
	public static Set<IRestrictedSemantics> getRestrictedSemantics() {
		return restrictedSemantics.keySet();
	}
	
	public static IRestrictedSemanticsOption[] getRestrictedSemanticsOptions(IRestrictedSemantics key) {
		return restrictedSemantics.get(key);
	}

	static {
		initializePlugins();
	}

	private static void initializePlugins() {		
		try {
			tryInitializePlugins();
		} catch (CoreException ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		}
	}

	private static void tryInitializePlugins() throws CoreException {
		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(IRESTRICTED_SEMANTICS_ID);
		
		for (IConfigurationElement e : config) {
			final Object o = e.createExecutableExtension("restrictedSemanticsImplementation");
			
			if (o instanceof IRestrictedSemantics) {
				ISafeRunnable runnable = new ISafeRunnable() {
					
					@Override
					public void handleException(Throwable exception) {
						System.out.println("Exception in client");
					}

					@Override
					public void run() throws Exception {
						IRestrictedSemantics rs = (IRestrictedSemantics)o;
						IRestrictedSemanticsOption[] options = rs.getOptions();
						
						restrictedSemantics.put(rs, options);
					}
				};
				
				SafeRunner.run(runnable);
			}
		}
	}

	private static HashMap<String, Map<String, IRestrictedSemanticsSnapshot>> selectedNodes = new HashMap<>();
	
	public static void processNodeSelection(IRestrictedSemanticsOption option) {
		ASTNode node = ParserUtils.getSelectedAstNode();
		IProject selectedProject = ParserUtils.getSelectedProject();
		
		if (node != null && selectedProject != null) {
			IRestrictedSemanticsSnapshot snapshot = option.createAstSnapshot(selectedProject, node);
			
			if (snapshot != null) {
				String optionId = option.getId();
				
				
				if (!selectedNodes.containsKey(optionId))
					selectedNodes.put(optionId, new HashMap<String, IRestrictedSemanticsSnapshot>());
				
				if (selectedNodes.get(optionId).containsKey(snapshot.getFullQualifiedAstNodeName()))
				{
					selectedNodes.get(optionId).remove(snapshot.getFullQualifiedAstNodeName());
					
					snapshot.dispose();
				}
				else
				{
					selectedNodes.get(optionId).put(snapshot.getFullQualifiedAstNodeName(), snapshot);
					
					snapshot.initMarkers();	
				}	
			}
		}
	}
	
	public static void informSnapshotsAboutRefactoringOfIdentifier(String identifier,
			RestrictedSemanticsRefactoringParticipantArguments refactoringArguments) {
		List<IRestrictedSemanticsSnapshot> affectedSnapshots = new ArrayList<>();
		
		for (String node : selectedNodes.keySet())
			if (selectedNodes.get(node).containsKey(identifier))
				affectedSnapshots.add(selectedNodes.get(node).get(identifier));
		
		for (IRestrictedSemanticsSnapshot affectedSnapshot : affectedSnapshots)
			affectedSnapshot.prepareRefactoring(refactoringArguments);			
	}
	
	public static void updateNodeIdentifier(String originalIdentifier, String newIdentifier) {
		for (String node : selectedNodes.keySet())
			if (selectedNodes.get(node).containsKey(originalIdentifier)) {
				IRestrictedSemanticsSnapshot snapshot = selectedNodes.get(node).get(originalIdentifier);
				
				selectedNodes.get(node).remove(originalIdentifier);
				selectedNodes.get(node).put(newIdentifier, snapshot);				
			}
	}

	public static boolean isOptionAvailableForNode(IRestrictedSemanticsOption option) {
		String optionId = option.getId();
		
		if (selectedNodes.containsKey(optionId)) {
			ASTNode node = ParserUtils.getSelectedAstNode();
			IProject selectedProject = ParserUtils.getSelectedProject();
			IRestrictedSemanticsSnapshot snapshot = option.createAstSnapshot(selectedProject, node);
			
			return selectedNodes.get(optionId).containsKey(snapshot.getFullQualifiedAstNodeName());
		}
		
		return false;
	}
}
