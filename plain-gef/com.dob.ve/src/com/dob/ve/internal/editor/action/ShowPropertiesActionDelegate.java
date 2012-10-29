package com.dob.ve.internal.editor.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionDelegate;

import com.dob.ve.internal.DobVEPlugin;

/**
 * @author idanilov
 *
 */
public class ShowPropertiesActionDelegate extends ActionDelegate
		implements IObjectActionDelegate {

	private IWorkbenchPart part;

	public void run(IAction action) {
		try {
			part.getSite().getPage().showView(IPageLayout.ID_PROP_SHEET);
		} catch (PartInitException e) {
			DobVEPlugin.error("Failed to open Properties View", e);
		}
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		part = targetPart;
	}

}
