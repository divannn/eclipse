package ide.custom;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.internal.WorkbenchWindow;

/**
 * @author idanilov
 *
 */
public class MenuCustomizer {

	static class HiddenItem {

		String idBeforeThis;
		IContributionItem item;

		HiddenItem(String idBeforeThis, IContributionItem item) {
			this.idBeforeThis = idBeforeThis;
			this.item = item;
		}
	}

	public static void initializeMenus() {
		final WorkbenchWindow window = (WorkbenchWindow) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		MenuManager manager = window.getMenuManager();
		IContributionItem aicontributionitem[] = manager.getItems();
		int j = 0;
		for (int k = aicontributionitem.length; j < k; j++) {
			IContributionItem item = aicontributionitem[j];
			if (item instanceof MenuManager) {
				MenuManager mgr = (MenuManager) item;
				if ("window".equals(item.getId())) {
					mgr.remove("openNewWindow");
					mgr.remove("selectWorkingSets");
					mgr.remove("openPerspective");
					mgr.remove("savePerspective");
					mgr.remove("closePerspective");
					mgr.remove("closeAllPerspectives");
					mgr.remove("editActionSets");
					mgr.remove("shortcuts");
					IContributionItem contrib = mgr.remove("preferences");
					if (contrib instanceof ActionContributionItem) {
						final IAction preferences = ((ActionContributionItem) contrib).getAction();
						mgr.add(new ActionContributionItem((IAction) Proxy.newProxyInstance(
								preferences.getClass().getClassLoader(),
								new Class[] { org.eclipse.jface.action.IAction.class },
								new InvocationHandler() {

									public Object invoke(Object proxy, Method method, Object args[])
											throws Throwable {
										if ("runWithEvent".equals(method.getName())
												|| "run".equals(method.getName())) {
											PreferenceDialog prefdlg = PreferencesUtil
													.createPreferenceDialogOn(
															Display.getCurrent().getActiveShell(),
															"com.nokia.tools.s60.preferences.S60UICustomizationGeneralPreferencePage",
															new String[] {
																	"com.nokia.tools.s60.preferences.S60UICustomizationGeneralPreferencePage",
																	"com.nokia.tools.s60.preferences.ExternalToolsPreferencePage",
																	"com.nokia.tools.theme.s60.ui.preferences.ThirdPartyIconsPrefPage",
																	"com.nokia.tools.s60.preferences.PluginHandlingPreferencePage",
																	"com.nokia.tools.s60.preferences.ComponentStorePrefPage" },
															null);
											PreferenceManager pManager = prefdlg
													.getPreferenceManager();
											pManager.remove("org.eclipse.ant.ui.AntPreferencePage");
											pManager
													.remove("org.eclipse.update.internal.ui.preferences.MainPreferencePage");
											pManager
													.remove("org.eclipse.jdt.ui.preferences.JavaBasePreferencePage");
											pManager
													.remove("org.eclipse.debug.ui.DebugPreferencePage");
											pManager.remove("org.eclipse.team.ui.TeamPreferences");
											prefdlg.open();
											return null;
										} else {
											return method.invoke(preferences, args);
										}
									}
								})));
					}
				}
				if ("file".equals(item.getId())) {
					mgr.remove("new");
					mgr.remove("revert");
					mgr.remove("move");
					mgr.remove("rename");
					mgr.remove("refresh");
					IContributionItem fileItems[] = mgr.getItems();
					for (int i = 0; i < fileItems.length; i++)
						if ("properties".equals(fileItems[i].getId())) {
							mgr.remove(fileItems[i]);
							if (i > 0 && (fileItems[i - 1] instanceof Separator)) {
								mgr.remove(fileItems[i - 1]);
								if (i > 1 && (fileItems[i - 2] instanceof Separator))
									mgr.remove(fileItems[i - 2]);
							}
						}

					IContributionItem contrib = mgr.find("import");
					if (contrib instanceof ActionContributionItem) {
						final IAction importing = ((ActionContributionItem) contrib).getAction();
						mgr.replaceItem("import", new ActionContributionItem((IAction) Proxy
								.newProxyInstance(importing.getClass().getClassLoader(),
										new Class[] { org.eclipse.jface.action.IAction.class },
										new InvocationHandler() {

											public Object invoke(Object proxy, Method method,
													Object args[]) throws Throwable {
												if ("runWithEvent".equals(method.getName())
														|| "run".equals(method.getName())) {
													//[ID] tmp.													
													//ImportAction action = new ImportAction();
													//action.run();
													return null;
												} else {
													return method.invoke(importing, args);
												}
											}

										})));
					}
					IContributionItem contrib2 = mgr.find("export");
					if (contrib instanceof ActionContributionItem) {
						final IAction exporting = ((ActionContributionItem) contrib2).getAction();
						mgr.replaceItem("export", new ActionContributionItem((IAction) Proxy
								.newProxyInstance(exporting.getClass().getClassLoader(),
										new Class[] { org.eclipse.jface.action.IAction.class },
										new InvocationHandler() {

											public Object invoke(Object proxy, Method method,
													Object args[]) throws Throwable {
												if ("runWithEvent".equals(method.getName())
														|| "run".equals(method.getName())) {
													//[ID] tmp.													
													//ExportAction action = new ExportAction();
													//action.run();
													return null;
												} else {
													return method.invoke(exporting, args);
												}
											}

										})));
					}
				}
				if ("edit".equals(item.getId())) {
					mgr.remove("cut");
					mgr.remove("selectall");
					mgr.remove("find");
				}
				if ("help".equals(item.getId())) {
					mgr.remove(CHEAT_SHEETS_ID);
					IContributionItem contrib = mgr.remove("about");
					if (contrib instanceof ActionContributionItem) {
						final IAction about = ((ActionContributionItem) contrib).getAction();
						mgr.add(new ActionContributionItem((IAction) Proxy.newProxyInstance(about
								.getClass().getClassLoader(),
								new Class[] { org.eclipse.jface.action.IAction.class },
								new InvocationHandler() {

									public Object invoke(Object proxy, Method method, Object args[])
											throws Throwable {
										if ("runWithEvent".equals(method.getName())
												|| "run".equals(method.getName())) {
											//[ID] tmp.							
											//(new CustomAboutDialog(window.getShell())).open();
											return null;
										} else {
											return method.invoke(about, args);
										}
									}

								})));
					}
				}
			}
		}

	}

	public static void hideExternMenus() {
		WorkbenchWindow window = (WorkbenchWindow) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		MenuManager manager = window.getMenuManager();
		IContributionItem items[] = manager.getItems();
		for (int i = 0; i < items.length; i++)
			if (HIDDEN_MENU_IDS.contains(items[i].getId())) {
				String idBeforeThis = i <= 0 ? null : items[i - 1].getId();
				IContributionItem item = manager.remove(items[i]);
				if (item != null)
					hiddenItems.add(new HiddenItem(idBeforeThis, item));
			}

		manager.update(true);
	}

	public static void restoreExternMenus() {
		WorkbenchWindow window = (WorkbenchWindow) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		MenuManager manager = window.getMenuManager();
		for (Iterator i = hiddenItems.iterator(); i.hasNext(); i.remove()) {
			HiddenItem item = (HiddenItem) i.next();
			try {
				manager.insertAfter(item.idBeforeThis, item.item);
			} catch (Exception _ex) {
			}
		}

		manager.update(true);
	}

	private static final String FILE_ID = "file";
	private static final String WINDOW_ID = "window";
	private static final String NEWWINDOW_ID = "openNewWindow";
	private static final String WORKINGSET_ID = "selectWorkingSets";
	private static final String OPENPERSPECTIVE_ID = "openPerspective";
	private static final String SAVEPERSPECTIVE_ID = "savePerspective";
	private static final String CLOSEPERSPECTIVE_ID = "closePerspective";
	private static final String CLOSEALLPERPECTIVE_ID = "closeAllPerspectives";
	private static final String CUSTOMIZEPERSPECTIVE_ID = "editActionSets";
	private static final String NAVIGATION_ID = "shortcuts";
	private static final String PREFERENCES_ID = "preferences";
	private static final String NEW_ID = "new";
	private static final String IMPORT_ID = "import";
	private static final String EXPORT_ID = "export";
	private static final String REVERT_ID = "revert";
	private static final String MOVE_ID = "move";
	private static final String RENAME_ID = "rename";
	private static final String REFRESH_ID = "refresh";
	private static final String PROPERTIES_ID = "properties";
	private static final String EDIT_ID = "edit";
	private static final String CUT_ID = "cut";
	private static final String SELECTALL_ID = "selectall";
	private static final String FIND_ID = "find";
	private static final String HELP_ID = "help";
	private static final String ABOUT_ID = "about";
	private static final String GENERAL_PREFERENCES_ID = "com.nokia.tools.s60.preferences.S60UICustomizationGeneralPreferencePage";
	private static final String EXTERNALTOOLS_PREFERENCES_ID = "com.nokia.tools.s60.preferences.ExternalToolsPreferencePage";
	private static final String THIRDPARTYICONS_PREFERENCES_ID = "com.nokia.tools.theme.s60.ui.preferences.ThirdPartyIconsPrefPage";
	private static final String PLUGINHANDLING_PREFERENCES_ID = "com.nokia.tools.s60.preferences.PluginHandlingPreferencePage";
	private static final String COMPONENTSTORE_PREFERENCES_ID = "com.nokia.tools.s60.preferences.ComponentStorePrefPage";
	private static final String ANT_PREFERENCES_ID = "org.eclipse.ant.ui.AntPreferencePage";
	private static final String UPDATE_PREFERENCES_ID = "org.eclipse.update.internal.ui.preferences.MainPreferencePage";
	private static final String JAVA_PREFERENCES_ID = "org.eclipse.jdt.ui.preferences.JavaBasePreferencePage";
	private static final String RUN_DEBUG_ID = "org.eclipse.debug.ui.DebugPreferencePage";
	private static final String TEAM_ID = "org.eclipse.team.ui.TeamPreferences";
	private static final String CHEAT_SHEETS_ID = "org.eclipse.ui.cheatsheets.actions.CheatSheetHelpMenuAction";

	private static final Set HIDDEN_MENU_IDS;
	private static List hiddenItems = new ArrayList();

	static {
		HIDDEN_MENU_IDS = new HashSet();
		HIDDEN_MENU_IDS.add("org.eclipse.jdt.ui.refactoring.menu");
		HIDDEN_MENU_IDS.add("navigate");
		HIDDEN_MENU_IDS.add("org.eclipse.search.menu");
		HIDDEN_MENU_IDS.add("project");
		HIDDEN_MENU_IDS.add("additions");
		HIDDEN_MENU_IDS.add("org.eclipse.ui.run");
	}
}
