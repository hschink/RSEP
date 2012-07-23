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
package org.iti.rsbp.menu;

import org.iti.rsbp.extension.rs.IRestrictedSemantics;
import org.iti.rsbp.extension.rs.IRestrictedSemanticsOption;

public class RestrictedSemanticsMenuData {

	private IRestrictedSemantics restrictedSemantics = null;
	
	public IRestrictedSemantics getRestrictedSemantics() {
		return restrictedSemantics;
	}

	public void setRestrictedSemantics(IRestrictedSemantics restrictedSemantics) {
		this.restrictedSemantics = restrictedSemantics;
	}

	private IRestrictedSemanticsOption option = null;

	public IRestrictedSemanticsOption getOption() {
		return option;
	}

	public void setOption(IRestrictedSemanticsOption option) {
		this.option = option;
	}
	
	public RestrictedSemanticsMenuData(IRestrictedSemantics restrictedSemantics,
			IRestrictedSemanticsOption option) {
		
		this.restrictedSemantics = restrictedSemantics;
		this.option = option;
	}
}
