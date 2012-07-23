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
package org.iti.rsbp.extension.rs;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;

public class RestrictedSemanticsSnapshotsController {
	
	private static Map<String, ASTNode> astNodes = new HashMap<>();
	
	private static Map<String, IRestrictedSemanticsSnapshot> snapshots = new HashMap<>();
	
	public static boolean existsInitialSnapshot(String fullQualifiedAstNodeName) {
		return astNodes.containsKey(fullQualifiedAstNodeName) && snapshots.containsKey(fullQualifiedAstNodeName);
	}
	
	public static void addInitialSnapshot(IRestrictedSemanticsSnapshot snapshot) {
		if (snapshot == null)
			throw new NullPointerException("Snapshot must not be null!");
		
		astNodes.put(snapshot.getFullQualifiedAstNodeName(), snapshot.getAstNode());
		snapshots.put(snapshot.getFullQualifiedAstNodeName(), snapshot);
		
		snapshot.updateMarkers();
	}
}
