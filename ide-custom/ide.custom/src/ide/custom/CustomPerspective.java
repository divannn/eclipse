package ide.custom;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * Should be replaced by real perspective.
 * @author idanilov
 *
 */
public class CustomPerspective
		implements IPerspectiveFactory {

	public static final String ID = "ide.custom.perspective";

	public void createInitialLayout(IPageLayout layout) {
		//[ID] tmp.		
		//		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		//		Boolean stateMaximized = Boolean.valueOf(shell.getMaximized());
		//		Point currentSize = shell.getSize();
		//		shell.setSize(1024, 768);
		//		if (!stateMaximized.booleanValue()) {
		//			shell.setSize(currentSize);
		//		}
		//		shell.setMaximized(stateMaximized.booleanValue());

		String editorArea = layout.getEditorArea();
		IFolderLayout leftFolder = layout.createFolder("left", IPageLayout.LEFT, 0.25F, editorArea);
		//"Navigator View" on the left.
		leftFolder.addView(IPageLayout.ID_RES_NAV);
		//tmp.
		//leftFolder.addView(DOBExplorerView.ID);
		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.7F, editorArea);
		//"Properties View" on the bottom.
		bottom.addView(IPageLayout.ID_PROP_SHEET);
		//"ErrorLog" View.
		bottom.addView("org.eclipse.pde.runtime.LogView");
		//"Outline View" on the right.
		layout.addView(IPageLayout.ID_OUTLINE, IPageLayout.RIGHT, 0.75F, editorArea);

		initMenuShortcuts(layout);
	}

	/**
	 * Adds default menu shortcuts
	 * @param layout
	 */
	protected void initMenuShortcuts(IPageLayout layout) {
		//tmp.
		//layout.addShowViewShortcut(DOBExplorerView.ID);
		//show "Outline View" menu in Window->Show View.
		layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
		layout.addShowViewShortcut(IPageLayout.ID_PROP_SHEET);
		layout.addNewWizardShortcut("someWizardId1");
	}

}
