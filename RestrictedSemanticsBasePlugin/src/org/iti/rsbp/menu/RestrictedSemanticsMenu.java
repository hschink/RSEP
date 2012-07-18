package org.iti.rsbp.menu;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.statushandlers.StatusManager;
import org.iti.ast.ParserUtils;
import org.iti.rsbp.controller.RestrictedSemanticsController;
import org.iti.rsbp.extension.rs.IRestrictedSemantics;
import org.iti.rsbp.extension.rs.IRestrictedSemanticsOption;

public class RestrictedSemanticsMenu extends ContributionItem {
	
	private class RestrictedSemanticsMenuSelectionAdapter extends SelectionAdapter {
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			if (e.getSource() instanceof MenuItem && ((MenuItem)e.getSource()).getData() instanceof RestrictedSemanticsMenuData) {
				RestrictedSemanticsMenuData data = (RestrictedSemanticsMenuData)((MenuItem)e.getSource()).getData();
				IRestrictedSemanticsOption option = data.getOption();

				RestrictedSemanticsController.processNodeSelection(option);
			}

			super.widgetSelected(e);
		}
	}
	
	@Override
	public void fill(Menu menu, int index) {
		ASTNode node = ParserUtils.getSelectedAstNode();
		IProject selectedProject = ParserUtils.getSelectedProject();
		
		MenuItem restrictedSemanticsMenuItem = new MenuItem(menu, SWT.CASCADE, index);
		restrictedSemanticsMenuItem.setText("Restricted Semantics");
		
		Menu submenu = new Menu(restrictedSemanticsMenuItem);
		restrictedSemanticsMenuItem.setMenu(submenu);
		
		for (IRestrictedSemantics rs : RestrictedSemanticsController.getRestrictedSemantics()) {
			IRestrictedSemanticsOption[] options = RestrictedSemanticsController.getRestrictedSemanticsOptions(rs);
			
			MenuItem subItem = new MenuItem(submenu, (options.length == 0) ? SWT.PUSH : SWT.CASCADE);
			subItem.setText(rs.getId());
			
			log(IStatus.INFO, "Add option [" + rs.getId() + "] to menu...");
			
			if (options.length > 0) {
				submenu = new Menu(subItem);
				subItem.setMenu(submenu);
				
				for (IRestrictedSemanticsOption option : options) {
					if (option.isValidForNode(selectedProject, node)) {
						log(IStatus.INFO, "Handle [" + node + "] for option [" + option.getId() + "].");
						
						MenuItem optionItem = new MenuItem(submenu, SWT.CHECK);
						optionItem.setText(option.toString());
						optionItem.setData(new RestrictedSemanticsMenuData(rs, option));
						optionItem.setSelection(isOptionCheckedForSelectedNode(option));
						
						optionItem.addSelectionListener(new RestrictedSemanticsMenuSelectionAdapter());
					} else {
						log(IStatus.INFO, "Cannot handle [" + node + "] for option [" + option.getId() + "].");
					}
				}
			}
			else {
				subItem.addSelectionListener(new RestrictedSemanticsMenuSelectionAdapter());
			}
		}
	}
	
	private static void log(int severity, String message) {
		Status status= new Status(severity, "org.iti.rsbp", message);
		StatusManager.getManager().handle(status, StatusManager.LOG);
	}

	private boolean isOptionCheckedForSelectedNode(IRestrictedSemanticsOption option) {
		return RestrictedSemanticsController.isOptionAvailableForNode(option);
	}
}
