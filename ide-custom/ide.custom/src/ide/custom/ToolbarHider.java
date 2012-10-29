package ide.custom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.custom.CBanner;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * @author idanilov
 *
 */
public class ToolbarHider {

	static class RemovedGroup {
		String beforeId;
		IContributionItem item;
	}

	protected static void hidePerspectiveBar() {
		CBanner banner = getBanner();
		if (banner == null)
			return;
		if (banner.getRight() != null) {
			perspectiveBar = banner.getRight();
			perspectiveBar.setVisible(false);
			banner.setRight(null);
			toggleCoolbarCustomizePerspectiveCommand(false);
		}
	}

	protected static void restorePerspectiveBar() {
		CBanner banner = getBanner();
		if (banner == null)
			return;
		if (banner.getRight() == null && perspectiveBar != null) {
			perspectiveBar.setVisible(true);
			banner.setRight(perspectiveBar);
			perspectiveBar = null;
			toggleCoolbarCustomizePerspectiveCommand(true);
		}
	}

	private static void toggleCoolbarCustomizePerspectiveCommand(final boolean visible) {
		CoolBar coolBar = getMainCoolBar();
		if (coolBar.getMenu() != null)
			coolBar.getMenu().addMenuListener(new MenuAdapter() {

				public void menuShown(MenuEvent e) {
					Menu menu = (Menu) e.widget;
					if (!visible) {
						MenuItem items[] = menu.getItems();
						if (ToolbarHider.coolbarContextMenuItem == null)
							ToolbarHider.coolbarContextMenuItem = (IContributionItem) items[items.length - 1]
									.getData();
						if (items.length > 1)
							items[items.length - 1].dispose();
					} else if (ToolbarHider.coolbarContextMenuItem != null)
						ToolbarHider.coolbarContextMenuItem.fill(menu, 1);
				}

			});
	}

	protected static void hideExternToolBar() {
		setVisibleForListedIds(false);
		CoolBar coolBar = getMainCoolBar();
		if (coolBar == null)
			return;
		CoolItem acoolitem[] = coolBar.getItems();
		int i = 0;
		for (int j = acoolitem.length; i < j; i++) {
			CoolItem item = acoolitem[i];
			if (!item.isDisposed() && (item.getData() instanceof ToolBarContributionItem)) {
				ToolBarContributionItem toolbaritem = (ToolBarContributionItem) item.getData();
				if (shouldRemoveGroup(toolbaritem)) {
					CoolBarManager mngr = (CoolBarManager) toolbaritem.getParent();
					if (mngr == null) {
						mngr = ((ApplicationWindow) PlatformUI.getWorkbench()
								.getActiveWorkbenchWindow()).getCoolBarManager();
						toolbaritem.setParent(mngr);
						mngr.refresh();
					}
					IContributionItem remitem = mngr.remove(toolbaritem);
					if (remitem != null)
						removedItems.add(remitem);
					mngr.update(true);
				}
			}
		}

	}

	private static void setVisibleForListedIds(boolean visible) {
		CoolBar coolBar = getMainCoolBar();
		if (coolBar == null)
			return;
		CoolItem acoolitem[] = coolBar.getItems();
		int j = 0;
		for (int k = acoolitem.length; j < k; j++) {
			CoolItem item = acoolitem[j];
			if (item.getData() instanceof ToolBarContributionItem) {
				ToolBarContributionItem toolbaritem = (ToolBarContributionItem) item.getData();
				IToolBarManager toolBarManager = toolbaritem.getToolBarManager();
				IContributionItem items[] = toolBarManager.getItems();
				for (int i = 0; i < items.length; i++)
					if (shouldRemove(items[i]))
						if ((items[i] instanceof Separator) && !visible) {
							RemovedGroup removed = new RemovedGroup();
							removed.beforeId = i != 0 ? items[i - 1].getId() : null;
							removed.item = items[i];
							removedGroups.add(removed);
							toolBarManager.remove(items[i]);
						} else {
							items[i].setVisible(visible);
						}

				if (visible) {
					for (Iterator iterator = removedGroups.iterator(); iterator.hasNext();) {
						RemovedGroup group = (RemovedGroup) iterator.next();
						if (group.beforeId != null)
							toolBarManager.insertAfter(group.beforeId, group.item);
						else
							toolBarManager.add(group.item);
					}

					removedGroups.clear();
				}
			}
		}

	}

	private static boolean shouldRemove(IContributionItem item) {
		return idsToRem.contains(item.getId());
	}

	private static boolean shouldRemoveGroup(IContributionItem item) {
		return groupsToRem.contains(item.getId());
	}

	public static CoolBar getMainCoolBar() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		Control ctrl[] = shell.getChildren();
		Control ctrl1[] = ((Composite) ctrl[0]).getChildren();
		Control ctrl2[] = ((Composite) ctrl1[0]).getChildren();
		Control acontrol[] = ctrl2;
		int i = 0;
		for (int j = acontrol.length; i < j; i++) {
			Control coolBar = acontrol[i];
			if (coolBar instanceof CoolBar)
				return (CoolBar) coolBar;
		}

		return null;
	}

	protected static CBanner getBanner() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		Control ctrl[] = shell.getChildren();
		Control ctr = (CBanner) ctrl[0];
		if (ctr != null && (ctr instanceof CBanner))
			return (CBanner) ctr;
		else
			return null;
	}

	protected static void restoreExternToolBar() {
		setVisibleForListedIds(true);
		CoolBar coolBar = getMainCoolBar();
		if (coolBar == null)
			return;
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() instanceof ApplicationWindow) {
			IContributionManager coolbarmanager = ((ApplicationWindow) PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow()).getCoolBarManager();
			IContributionItem item;
			for (Iterator iterator = removedItems.iterator(); iterator.hasNext(); coolbarmanager
					.add(item))
				item = (IContributionItem) iterator.next();

			((CoolBarManager) coolbarmanager).update(true);
			removedItems.clear();
		}
	}

	private static List removedItems = new ArrayList();
	private static List removedGroups = new ArrayList();
	private static ArrayList groupsToRem;
	private static ArrayList idsToRem;
	private static Control perspectiveBar = null;
	private static IContributionItem coolbarContextMenuItem = null;

	static {
		groupsToRem = new ArrayList();
		idsToRem = new ArrayList();
		idsToRem.add("newWizardDropDown");
		idsToRem.add("build.group");
		idsToRem.add("build");
		idsToRem.add("build.ext");
		groupsToRem.add("org.eclipse.debug.ui.launchActionSet");
		groupsToRem.add("org.eclipse.ui.workbench.navigate");
		groupsToRem.add("org.eclipse.search.searchActionSet");
		groupsToRem.add("org.eclipse.ui.edit.text.actionSet.navigation");
		groupsToRem.add("org.eclipse.wst.server.ui.internal.webbrowser.actionSet");
		groupsToRem.add("org.eclipse.ui.WorkingSetActionSet");
	}

}
