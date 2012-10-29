package com.dob.ve.internal.editor;

import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.views.properties.PropertySheetPage;

/**
 * @author idanilov
 *
 */
public class DobPropertySheetPage extends PropertySheetPage {

	/**
	 * Parent editor which creates this page.
	 */
	private DobVisualEditor editorPart;

	public DobPropertySheetPage(DobVisualEditor part) {
		editorPart = part;
	}

	@Override
	//allow undo/redo from "Properties" view.
	public void setActionBars(IActionBars actionBars) {
		super.setActionBars(actionBars);
		actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), editorPart
				.getAction(ActionFactory.UNDO.getId()));
		actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), editorPart
				.getAction(ActionFactory.REDO.getId()));
	}

}
