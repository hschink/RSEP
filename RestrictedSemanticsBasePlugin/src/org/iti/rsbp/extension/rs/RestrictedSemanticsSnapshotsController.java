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
