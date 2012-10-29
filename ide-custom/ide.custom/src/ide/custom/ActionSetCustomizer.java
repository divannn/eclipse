package ide.custom;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.Perspective;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.WorkbenchWindow;
import org.eclipse.ui.internal.registry.ActionSetRegistry;
import org.eclipse.ui.internal.registry.IActionSetDescriptor;

/**
 * @author idanilov
 *
 */
public class ActionSetCustomizer {

	public static void turnOffActionSets() {
		WorkbenchWindow window = (WorkbenchWindow) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		if (window == null)
			return;
		WorkbenchPage page = (WorkbenchPage) window.getActivePage();
		if (page == null)
			return;
		Perspective perspective = page.getActivePerspective();
		List descs = new ArrayList();
		ActionSetRegistry reg = WorkbenchPlugin.getDefault().getActionSetRegistry();
		reg.getActionSets();
		IActionSetDescriptor aiactionsetdescriptor[] = perspective.getAlwaysOnActionSets();
		int i = 0;
		for (int j = aiactionsetdescriptor.length; i < j; i++) {
			IActionSetDescriptor desc = aiactionsetdescriptor[i];
			String as[] = FILTERED_ACTIONSETS;
			int k = 0;
			for (int l = as.length; k < l; k++) {
				String filtered = as[k];
				if (!filtered.equals(desc.getId()))
					continue;
				descs.add(desc);
				break;
			}

		}

		perspective.turnOffActionSets((IActionSetDescriptor[]) descs
				.toArray(new IActionSetDescriptor[descs.size()]));
		window.updateActionSets();
	}

	private static final String FILTERED_ACTIONSETS[] = {
			"org.eclipse.ui.edit.text.actionSet.navigation",
			"org.eclipse.ui.externaltools.ExternalToolsSet",
			"org.eclipse.ui.edit.text.actionSet.openExternalFile",
			"org.eclipse.ui.edit.text.actionSet.convertLineDelimitersTo",
			"org.eclipse.wst.server.ui.internal.webbrowser.actionSet",
			"org.eclipse.update.ui.softwareUpdates", "org.eclipse.ui.actionSet.keyBindings",
			"org.eclipse.ui.NavigateActionSet", "org.eclipse.search.searchActionSet",
			"org.eclipse.ui.WorkingSetActionSet" };

}
