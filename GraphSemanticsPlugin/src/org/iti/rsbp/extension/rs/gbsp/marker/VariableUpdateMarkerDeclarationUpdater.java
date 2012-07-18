package org.iti.rsbp.extension.rs.gbsp.marker;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.ui.texteditor.IMarkerUpdater;
import org.iti.rsbp.extension.rs.gbsp.bindinginformation.IBindingInformation;
import org.iti.rsbp.extension.rs.gbsp.bindinginformation.variable.update.AbstractVariableUpdateBindingInformation;
import org.iti.rsbp.extension.rs.gbsp.utils.VariableUtils;

public class VariableUpdateMarkerDeclarationUpdater implements IMarkerUpdater {

	public VariableUpdateMarkerDeclarationUpdater() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getMarkerType() {
		return AbstractVariableUpdateBindingInformation.DECLARATION_MARKER_ID;
	}

	@Override
	public String[] getAttribute() {
		return null;
	}

	@Override
	public boolean updateMarker(IMarker marker, IDocument document,
			Position position) {

		IBindingInformation bindingInformation = VariableUtils.getBindingInformation(marker, AbstractVariableUpdateBindingInformation.VARIABLE_UPDATE_BINDING_INFORMATION);

		if (bindingInformation != null)
			bindingInformation.updateMarkers();
		
		return true;
	}

}
