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
package org.iti.rsbp.extension.rs.gbsp.bindinginformation.variable.access;

import org.iti.rsbp.extension.rs.gbsp.bindinginformation.AbstractBindingInformation;


public abstract class AbstractAccessBindingInformation extends AbstractBindingInformation {

public final static String VARIABLE_ACCESS_BINDING_INFORMATION = "VariableAccessBindingInformation"; 
	
	public final static String DECLARATION_MARKER_ID = "org.iti.rsbp.extension.rs.gbsp.rbspVariableAccessMarkerDeclaration";
	public final static String DECLARATION_ERROR_MARKER_ID = "org.iti.rsbp.extension.rs.gbsp.rbspVariableAccessMarkerDeclarationError";
	public final static String MARKER_ID = "org.iti.rsbp.extension.rs.gbsp.rbspVariableAccessMarker";
	public final static String ERROR_MARKER_ID = "org.iti.rsbp.extension.rs.gbsp.rbspVariableAccessMarkerError";
	
	public final static String COMPILATION_UNIT = "VariableAccessCompilationUnit";
	public final static String NODE = "VariableAccessNode";
	public final static String EXPRESSION = "VariableAccessExpression";
	public final static String POSITION = "VariableAccessPosition";
	
	@Override
	protected String getBindingType() {
		return VARIABLE_ACCESS_BINDING_INFORMATION;
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
