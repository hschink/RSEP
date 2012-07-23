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

import java.util.List;

import org.eclipse.core.resources.IMarker;

public interface IRestrictedSemanticsSnapshotComparisonResult {
	void addResult(IMarker marker);
	
	void addAllResults(List<IMarker> markers);
	
	List<IMarker> getResults();
	
	void setResults(List<IMarker> marker);
}
