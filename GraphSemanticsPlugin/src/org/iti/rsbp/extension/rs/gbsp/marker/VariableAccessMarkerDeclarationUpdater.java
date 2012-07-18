package org.iti.rsbp.extension.rs.gbsp.marker;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.ui.texteditor.IMarkerUpdater;
import org.iti.rsbp.extension.rs.gbsp.bindinginformation.IBindingInformation;
import org.iti.rsbp.extension.rs.gbsp.bindinginformation.variable.access.AbstractAccessBindingInformation;
import org.iti.rsbp.extension.rs.gbsp.utils.VariableUtils;

public class VariableAccessMarkerDeclarationUpdater implements IMarkerUpdater {

	public VariableAccessMarkerDeclarationUpdater() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getMarkerType() {
		return AbstractAccessBindingInformation.DECLARATION_MARKER_ID;
	}

	@Override
	public String[] getAttribute() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateMarker(IMarker marker, IDocument document,
			Position position) {
		
		IBindingInformation bindingInformation = VariableUtils.getBindingInformation(marker, AbstractAccessBindingInformation.VARIABLE_ACCESS_BINDING_INFORMATION);

		if (bindingInformation != null)
			bindingInformation.updateMarkers();
		
		return true;
	}

}
