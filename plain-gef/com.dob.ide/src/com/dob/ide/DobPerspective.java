package com.dob.ide;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * DOB Perspective which is opened during Eclipse IDE start-up.
 * Only this perspective can be opened. 
 * Switch to other perspectives (IDE default) is supressed.
 * @author idanilov
 *
 */
public class DobPerspective
		implements IPerspectiveFactory {

	public static final String ID = "dobPerspective";

	//adds DOB views into layout.
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		IFolderLayout leftFolder = layout.createFolder("left", IPageLayout.LEFT, 0.25F, editorArea);
		//"Navigator View" on the left.
		leftFolder.addView(IPageLayout.ID_RES_NAV);
		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.7F, editorArea);
		//"Properties View" on the bottom.
		bottom.addView(IPageLayout.ID_PROP_SHEET);
		bottom.addView(IPageLayout.ID_PROBLEM_VIEW);
		bottom.addView("org.eclipse.pde.runtime.LogView");//tmp.
		//"Outline View" on the right.
		layout.addView(IPageLayout.ID_OUTLINE, IPageLayout.LEFT, 0.25F, editorArea);

		initMenuShortcuts(layout);
	}

	/**
	 * Adds default menu shortcuts
	 * @param layout
	 */
	protected void initMenuShortcuts(IPageLayout layout) {
		//show "Outline View" menu in Window->Show View.
		layout.addShowViewShortcut(IPageLayout.ID_RES_NAV);
		layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
		layout.addShowViewShortcut(IPageLayout.ID_PROP_SHEET);
		layout.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
		//show "New DOB Project" menu in "Navigator View" context menu->New.
		//layout.addNewWizardShortcut(NewDobProjectWizard.ID);
	}

}
