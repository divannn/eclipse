package com.dob.ve.internal.editor;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.AlignmentRetargetAction;
import org.eclipse.gef.ui.actions.CopyRetargetAction;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.PasteRetargetAction;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;

import com.dob.ve.internal.DobVEPlugin;
import com.dob.ve.internal.IImageConstant;

/**
 * DOB editor action bars contributor.
 * Provides common actions for all instances of one edtitor. 
 * @author idanilov
 *
 */
public class DobVisualEditorActionBarContributor extends ActionBarContributor {

	/** 
	 * Adds GEF retargetable actions: unde/redo/delete etc.
	 */
	protected void buildActions() {
		addRetargetAction(new ZoomInRetargetAction());
		addRetargetAction(new ZoomOutRetargetAction());

		addRetargetAction(new UndoRetargetAction());
		addRetargetAction(new RedoRetargetAction());

		addRetargetAction(new DeleteRetargetAction());

		addRetargetAction(new CopyRetargetAction());
		addRetargetAction(new PasteRetargetAction());

		addRetargetAction(new AlignmentRetargetAction(PositionConstants.LEFT));
		addRetargetAction(new AlignmentRetargetAction(PositionConstants.RIGHT));
		addRetargetAction(new AlignmentRetargetAction(PositionConstants.TOP));
		addRetargetAction(new AlignmentRetargetAction(PositionConstants.BOTTOM));

		RetargetAction snap2grid = new RetargetAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY,
				GEFMessages.ToggleGrid_Label, IAction.AS_CHECK_BOX);
		snap2grid.setImageDescriptor(DobVEPlugin.getDescriptor(IImageConstant.SNAPTOGRID));
		addRetargetAction(snap2grid);

		RetargetAction snap2geom = new RetargetAction(GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY,
				GEFMessages.ToggleSnapToGeometry_Label, IAction.AS_CHECK_BOX);
		snap2geom.setImageDescriptor(DobVEPlugin.getDescriptor(IImageConstant.SNAPTOGEOM));
		addRetargetAction(snap2geom);
	}

	protected void declareGlobalActionKeys() {
	}

	@Override
	public void contributeToMenu(IMenuManager menuManager) {
		super.contributeToMenu(menuManager);
		//MenuManager viewMenu = new MenuManager("ZOOM");
		//viewMenu.add(getAction(GEFActionConstants.ZOOM_IN));
		//viewMenu.add(getAction(GEFActionConstants.ZOOM_OUT));
		//menuManager.insertAfter(IWorkbenchActionConstants.M_EDIT,viewMenu); //where the menu should placed on the menu bar
	}

	public void contributeToToolBar(IToolBarManager tbm) {
		//		String[] zoomStrings = new String[] { ZoomManager.FIT_ALL, ZoomManager.FIT_HEIGHT,
		//				ZoomManager.FIT_WIDTH };
		ZoomComboContributionItem zoomComboContrib = new ZoomComboContributionItem(getPage()/*, zoomStrings*/);
		tbm.add(zoomComboContrib);
		tbm.add(getAction(GEFActionConstants.ZOOM_IN));
		tbm.add(getAction(GEFActionConstants.ZOOM_OUT));
		tbm.add(new Separator());

		tbm.add(getAction(ActionFactory.UNDO.getId()));
		tbm.add(getAction(ActionFactory.REDO.getId()));

		//tbm.add(getAction(ActionFactory.COPY.getId()));
		//tbm.add(getAction(ActionFactory.PASTE.getId()));
		//tbm.add(getAction(ActionFactory.DELETE.getId()));

		tbm.add(getAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY));
		tbm.add(getAction(GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY));
		tbm.add(new Separator());

		tbm.add(getAction(GEFActionConstants.ALIGN_LEFT));
		tbm.add(getAction(GEFActionConstants.ALIGN_RIGHT));
		tbm.add(getAction(GEFActionConstants.ALIGN_TOP));
		tbm.add(getAction(GEFActionConstants.ALIGN_BOTTOM));
	}

}
