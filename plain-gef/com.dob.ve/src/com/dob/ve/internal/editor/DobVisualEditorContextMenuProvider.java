package com.dob.ve.internal.editor;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionFactory;

/**
 * Builds editor context menu.
 * @author idanilov
 *
 */
public class DobVisualEditorContextMenuProvider extends ContextMenuProvider {

	/**
	 * Unwanted action ids. Filter them from editor and outline context menu.
	 */
	public static final Set<String> FORBIDDEN_CONTEXT_MENU_ACTION_IDS = new HashSet<String>();
	static {
		FORBIDDEN_CONTEXT_MENU_ACTION_IDS.add("org.eclipse.ve.java.editorpart.container.setlayout");
	}

	private ActionRegistry actionRegistry;

	public DobVisualEditorContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
		super(viewer);
		actionRegistry = registry;
	}

	public void buildContextMenu(IMenuManager menuManager) {
		menuManager.add(new Separator(IWorkbenchActionConstants.NEW_GROUP));//here will be contributed all "New" action delegates.

		GEFActionConstants.addStandardActionGroups(menuManager);
		IAction action = actionRegistry.getAction(ActionFactory.UNDO.getId());//GEFActionConstants.UNDO
		menuManager.appendToGroup(GEFActionConstants.GROUP_UNDO, action);
		action = actionRegistry.getAction(ActionFactory.REDO.getId());//GEFActionConstants.REDO
		menuManager.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

		action = actionRegistry.getAction(GEFActionConstants.DIRECT_EDIT);
		menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

		action = actionRegistry.getAction(ActionFactory.DELETE.getId());
		menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

		//new.
		//		action = actionRegistry.getAction(OpenInEditorAction.ID);
		//		menuManager.appendToGroup(GEFActionConstants.GROUP_VIEW, action);
		action = actionRegistry.getAction(ActionFactory.COPY.getId());
		menuManager.appendToGroup(GEFActionConstants.GROUP_COPY, action);
		action = actionRegistry.getAction(ActionFactory.PASTE.getId());
		menuManager.appendToGroup(GEFActionConstants.GROUP_COPY, action);

	}

	@Override
	//forbids default VE context menu items.
	protected boolean allowItem(IContributionItem itemToAdd) {
		if (FORBIDDEN_CONTEXT_MENU_ACTION_IDS.contains(itemToAdd.getId())) {
			return false;
		}
		return super.allowItem(itemToAdd);
	}

}