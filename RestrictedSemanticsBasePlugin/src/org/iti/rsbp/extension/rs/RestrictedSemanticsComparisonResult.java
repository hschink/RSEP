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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IMarker;

public class RestrictedSemanticsComparisonResult implements
		IRestrictedSemanticsSnapshotComparisonResult {

	private List<IMarker> markers = null;
	
	public RestrictedSemanticsComparisonResult() {
		this.markers = new ArrayList<>();
	}
	
	public RestrictedSemanticsComparisonResult(List<IMarker> marker) {
		this.markers = marker;
	}

	@Override
	public void addResult(IMarker marker) {
		checkListExists();
		
		markers.add(marker);		
	}

	private void checkListExists() {
		if (markers == null) {
			this.markers = new ArrayList<>();
		}
	}

	@Override
	public void addAllResults(List<IMarker> markers) {
		checkListExists();
		
		markers.addAll(markers);	
	}
	
	@Override
	public List<IMarker> getResults() {
		return markers;
	}

	@Override
	public void setResults(List<IMarker> markers) {
		this.markers = markers;
	}
}
