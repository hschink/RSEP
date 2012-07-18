package org.iti.rsbp.extension.rs.gbsp.bindinginformation.method.call;

import org.iti.rsbp.extension.rs.gbsp.bindinginformation.AbstractBindingInformation;


public abstract class AbstractInvocationBindingInformation extends AbstractBindingInformation {

public final static String METHOD_CALL_BINDING_INFORMATION = "MethodCallBindingInformation"; 
	
	public final static String DECLARATION_MARKER_ID = "org.iti.rsbp.extension.rs.gbsp.rbspMethodCallMarkerDeclaration";
	public final static String DECLARATION_ERROR_MARKER_ID = "org.iti.rsbp.extension.rs.gbsp.rbspMethodCallMarkerDeclarationError";
	public final static String MARKER_ID = "org.iti.rsbp.extension.rs.gbsp.rbspMethodCallMarker";
	public final static String ERROR_MARKER_ID = "org.iti.rsbp.extension.rs.gbsp.rbspMethodCallMarkerError";
	
	public final static String COMPILATION_UNIT = "MethodCallCompilationUnit";
	public final static String NODE = "MethodCallNode";
	public final static String EXPRESSION = "MethodCallExpression";
	public final static String POSITION = "MethodCallPosition";
	
	@Override
	protected String getBindingType() {
		return METHOD_CALL_BINDING_INFORMATION;
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
