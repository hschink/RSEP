package org.iti.rsbp.extension.rs;

import java.util.List;

import org.eclipse.core.resources.IMarker;

public interface IRestrictedSemanticsSnapshotComparisonResult {
	void addResult(IMarker marker);
	
	void addAllResults(List<IMarker> markers);
	
	List<IMarker> getResults();
	
	void setResults(List<IMarker> marker);
}
