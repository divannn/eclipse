package ide.custom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IExtension;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.registry.ViewRegistry;
import org.eclipse.ui.views.IViewDescriptor;

/**
 * Removes unwanted views from {@link ViewRegistry}.
 * Uses private Eclipse UI API to hack default behaviour.
 * @author idanilov
 *
 */
public class ViewRemover {

	private static final Set<String> VIEW_IDS = new HashSet<String>();
	static {
		VIEW_IDS.add("org.eclipse.ant.ui.views.AntView");
		VIEW_IDS.add("org.eclipse.debug.ui.BreakpointView");
		VIEW_IDS.add("org.eclipse.debug.ui.DebugView");
		VIEW_IDS.add("org.eclipse.debug.ui.ExpressionView");
		VIEW_IDS.add("org.eclipse.debug.ui.MemoryView");
		VIEW_IDS.add("org.eclipse.debug.ui.RegisterView");
		VIEW_IDS.add("org.eclipse.debug.ui.VariableView");
		VIEW_IDS.add("org.eclipse.gef.ui.palette_view");
		VIEW_IDS.add("org.eclipse.help.ui.HelpView");
		VIEW_IDS.add("org.eclipse.jdt.callhierarchy.view");
		VIEW_IDS.add("org.eclipse.jdt.debug.ui.DisplayView");
		VIEW_IDS.add("org.eclipse.jdt.junit.ResultView");
		VIEW_IDS.add("org.eclipse.jdt.ui.JavadocView");
		VIEW_IDS.add("org.eclipse.jdt.ui.MembersView");
		VIEW_IDS.add("org.eclipse.jdt.ui.PackageExplorer");
		VIEW_IDS.add("org.eclipse.jdt.ui.PackagesView");
		VIEW_IDS.add("org.eclipse.jdt.ui.ProjectsView");
		VIEW_IDS.add("org.eclipse.jdt.ui.SourceView");
		VIEW_IDS.add("org.eclipse.jdt.ui.TypeHierarchy");
		VIEW_IDS.add("org.eclipse.jdt.ui.TypesView");
		//VIEWS_ID.add("org.eclipse.pde.runtime.LogView");
		//VIEW_IDS.add("org.eclipse.pde.runtime.RegistryBrowser");
		VIEW_IDS.add("org.eclipse.pde.ui.DependenciesView");
		VIEW_IDS.add("org.eclipse.pde.ui.PluginsView");
		VIEW_IDS.add("org.eclipse.search.SearchResultView");
		VIEW_IDS.add("org.eclipse.search.ui.views.SearchView");
		VIEW_IDS.add("org.eclipse.team.ccvs.ui.AnnotateView");
		VIEW_IDS.add("org.eclipse.team.ccvs.ui.EditorsView");
		VIEW_IDS.add("org.eclipse.team.ccvs.ui.RepositoriesView");
		VIEW_IDS.add("org.eclipse.team.sync.views.SynchronizeView");
		VIEW_IDS.add("org.eclipse.team.ui.GenericHistoryView");
		VIEW_IDS.add("org.eclipse.ui.browser.view");
		VIEW_IDS.add("org.eclipse.ui.cheatsheets.views.CheatSheetView");
		VIEW_IDS.add("org.eclipse.ui.console.ConsoleView");
		//VIEW_IDS.add("org.eclipse.ui.internal.introview");
		VIEW_IDS.add("org.eclipse.ui.navigator.ProjectExplorer");
		VIEW_IDS.add("org.eclipse.ui.views.BookmarkView");
		VIEW_IDS.add("org.eclipse.ui.views.ProblemView");
		VIEW_IDS.add("org.eclipse.ve.internal.java.codegen.editorpart.BeansList");
		VIEW_IDS.add("org.eclipse.ve.internal.java.codegen.editorpart.XMLViewPart");
	}

	static void removeViews() {
		/* doesn't work.
		 * IExtensionRegistry reg = Platform.getExtensionRegistry();
		 IExtensionPoint ep = reg.getExtensionPoint("org.eclipse.ui.views");

		 IExtension[] extensions = ep.getExtensions();
		 for (int i = 0; i < extensions.length; ++i) {
		 IExtension nextE = extensions[i];
		 for (IConfigurationElement n : nextE.getConfigurationElements()) {
		 System.err.println(n.getAttribute(ID_A));
		 if (VIEWS_ID.contains(n.getAttribute(ID_A))) {
		 reg.removeExtension(nextE, new Object());//falls here.
		 }
		 }
		 }*/

		List toRemove = new ArrayList();
		ViewRegistry vr = (ViewRegistry) PlatformUI.getWorkbench().getViewRegistry();
		IViewDescriptor[] views = vr.getViews();
		for (IViewDescriptor n : views) {
			//System.err.println(">>> " + n.getId());
			if (VIEW_IDS.contains(n.getId())) {
				toRemove.add(n);
			}
		}
		if (toRemove.size() > 0) {
			vr.removeExtension((IExtension) null, toRemove.toArray());
		}
	}

}
