package org.iti.rsbp.extension.rs.gbsp.bindinginformation.variable.update;

import org.iti.rsbp.extension.rs.gbsp.bindinginformation.AbstractBindingInformation;


public abstract class AbstractUpdateBindingInformation extends AbstractBindingInformation {

	public final static String VARIABLE_UPDATE_BINDING_INFORMATION = "VariableUpdateBindingInformation"; 
	
	public final static String DECLARATION_MARKER_ID = "org.iti.rsbp.extension.rs.gbsp.rbspVariableUpdateMarkerDeclaration";
	public final static String DECLARATION_ERROR_MARKER_ID = "org.iti.rsbp.extension.rs.gbsp.rbspVariableUpdateMarkerDeclarationError";
	public final static String MARKER_ID = "org.iti.rsbp.extension.rs.gbsp.rbspVariableUpdateMarker";
	public final static String ERROR_MARKER_ID = "org.iti.rsbp.extension.rs.gbsp.rbspVariableUpdateMarkerError";
	
	@Override
	protected String getBindingType() {
		return VARIABLE_UPDATE_BINDING_INFORMATION;
	}

	@Override
	protected String getMarkerId() {
		return MARKER_ID;
	}

	@Override
	protected String getDeclarationMarkerId() {
		return DECLARATION_MARKER_ID;
	}

	@Override
	protected String getErrorMarkerId() {
		return ERROR_MARKER_ID;
	}

	@Override
	protected String getDeclarationErrorMarkerId() {
		return DECLARATION_ERROR_MARKER_ID;
	}
}
