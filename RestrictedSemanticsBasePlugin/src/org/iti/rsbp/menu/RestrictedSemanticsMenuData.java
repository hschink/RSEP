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
