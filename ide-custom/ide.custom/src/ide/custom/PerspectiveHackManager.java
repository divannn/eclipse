package ide.custom;


import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener3;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;


/**
 * @author idanilov
 *
 */
public class PerspectiveHackManager
		implements IPerspectiveListener3 {

	public static PerspectiveHackManager getInstance() {
		if (instance == null)
			instance = new PerspectiveHackManager();
		return instance;
	}

	public void init() {
		ForcePerspectiveOnStart.getActiveWorkbenchWindow().addPerspectiveListener(this);
	}

	public void deinit() {
		IWorkbenchWindow window = ForcePerspectiveOnStart.getActiveWorkbenchWindow();
		if (window != null)
			window.removePerspectiveListener(this);
	}

	public void perspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor perspective) {
		if (CustomPerspective.ID.equals(perspective.getId()))
			initializePerspective();
		else
			deinitializePerspective();
	}

	public void perspectiveChanged(IWorkbenchPage page, IPerspectiveDescriptor perspective,
			String changeId) {
		if (CustomPerspective.ID.equals(perspective.getId()))
			initializePerspective();
	}

	public void perspectiveClosed(IWorkbenchPage iworkbenchpage,
			IPerspectiveDescriptor iperspectivedescriptor) {
	}

	public void perspectiveDeactivated(IWorkbenchPage iworkbenchpage,
			IPerspectiveDescriptor iperspectivedescriptor) {
	}

	public void perspectiveOpened(IWorkbenchPage page, IPerspectiveDescriptor perspective) {
		if (CustomPerspective.ID.equals(perspective.getId()))
			initializePerspective();
		else
			deinitializePerspective();
	}

	public void perspectiveSavedAs(IWorkbenchPage iworkbenchpage,
			IPerspectiveDescriptor iperspectivedescriptor,
			IPerspectiveDescriptor iperspectivedescriptor1) {
	}

	public void perspectiveChanged(IWorkbenchPage page, IPerspectiveDescriptor perspective,
			IWorkbenchPartReference partRef, String changeId) {
		if (CustomPerspective.ID.equals(perspective.getId()))
			initializePerspective();
		else
			deinitializePerspective();
	}

	protected static void initializePerspective() {
		ActionSetCustomizer.turnOffActionSets();
		ToolbarHider.hideExternToolBar();
		ToolbarHider.hidePerspectiveBar();
		MenuCustomizer.hideExternMenus();
	}

	public static void deinitializePerspective() {
		ToolbarHider.restorePerspectiveBar();
		ToolbarHider.restoreExternToolBar();
		MenuCustomizer.restoreExternMenus();
	}

	private static PerspectiveHackManager instance;

}
