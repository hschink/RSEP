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
package org.iti.rsbp.extension.rs.gbsp.marker;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.ui.texteditor.IMarkerUpdater;
import org.iti.rsbp.extension.rs.gbsp.bindinginformation.IBindingInformation;
import org.iti.rsbp.extension.rs.gbsp.bindinginformation.method.call.MethodInvocationBindingInformation;
import org.iti.rsbp.extension.rs.gbsp.utils.VariableUtils;

public class MethodCallMarkerDeclarationUpdater implements IMarkerUpdater {

	public MethodCallMarkerDeclarationUpdater() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getMarkerType() {
		return MethodInvocationBindingInformation.DECLARATION_MARKER_ID;
	}

	@Override
	public String[] getAttribute() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateMarker(IMarker marker, IDocument document,
			Position position) {
		
		IBindingInformation bindingInformation = VariableUtils.getBindingInformation(marker, MethodInvocationBindingInformation.METHOD_CALL_BINDING_INFORMATION);

		if (bindingInformation != null)
			bindingInformation.updateMarkers();
		
		return true;
	}
}
