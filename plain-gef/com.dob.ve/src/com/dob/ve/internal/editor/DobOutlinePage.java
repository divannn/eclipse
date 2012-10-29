package com.dob.ve.internal.editor;

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.draw2d.parts.Thumbnail;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.PageBook;

import com.dob.util.PluginLogger;
import com.dob.util.UiUtils;
import com.dob.ve.internal.DobVEPlugin;
import com.dob.ve.internal.IImageConstant;
import com.dob.ve.internal.editor.editpart.ContentsTreeEP;
import com.dob.ve.internal.editor.editpart.DobTreeEPFactory;

/**
 * Trivial implementation for editor's ouline.
 * Almost all is borrowed from JavaVisualEditorOutlinePage with small customization.
 * @see JavaVisualEditorOutlinePage
 * @author idanilov
 *
 */
class DobOutlinePage extends ContentOutlinePage {

	private final static String CONTEXT_MENU_ID = "dob.visualeditor.outline.contextmenu";

	private static final String TITLE = "Collapse All";
	private static final String TOOL_TIP = "Collapse All";

	/**
	 * Parent editor which creates this outline.
	 */
	private DobVisualEditor editorPart;

	/**
	 * Main control.
	 */
	private PageBook pageBook;
	/**
	 * Tree control from TreeViewer.
	 */
	private Control outline;
	/**
	 * Overview control.
	 */
	private Canvas overview;
	private Thumbnail thumbnail;

	/**
	 * View toolbar action to hide/shoe overview. 
	 */
	private ShowOverviewAction showOverviewAction;
	/**
	 * View toolbar action to collapse viewer tree.
	 */
	private CollapseAllAction collapseAllAction;

	private static final PluginLogger LOGGER = DobVEPlugin.getLogger(DobVEPlugin.LOGGER_EDITOR);

	public DobOutlinePage(DobVisualEditor part, EditPartViewer viewer) {
		super(viewer);
		editorPart = part;
	}

	@Override
	public void init(IPageSite pageSite) {
		LOGGER.debug(this, "Initializing outline page");
		super.init(pageSite);
		//add actions onto view toolbar.
		IActionBars actionBars = pageSite.getActionBars();
		IToolBarManager tbm = actionBars.getToolBarManager();
		collapseAllAction = new CollapseAllAction();
		tbm.add(collapseAllAction);
		showOverviewAction = new ShowOverviewAction();
		tbm.add(showOverviewAction);
	}

	@Override
	public void dispose() {
		LOGGER.debug(this, "Disposing outline page");
		editorPart.getSelectionSynchronizer().removeViewer(getViewer());
		editorPart.getEditDomain().removeViewer(getViewer());
		super.dispose();
	}

	@Override
	public Control getControl() {
		return pageBook;
	}

	@Override
	public void createControl(Composite parent) {
		LOGGER.debug(this, "Creating outline page control");
		pageBook = new PageBook(parent, SWT.NONE);
		//clear referece in editor when main control is disposed.
		pageBook.addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				editorPart.clearContentOutlinePage();
			}
		});
		outline = getViewer().createControl(pageBook);
		editorPart.getEditDomain().addViewer(getViewer());

		//set key habdler for tree viewer.
		getViewer().setKeyHandler(createKeyHandler());

		//add tree viewer into selection synchronizer.
		editorPart.getSelectionSynchronizer().addViewer(getViewer());

		//set editpart factory.
		getViewer().setEditPartFactory(new DobTreeEPFactory());
		//set contents.
		getViewer().setContents(new ContentsTreeEP(null));
		if (editorPart.isReady()) {//all editor stuff initialized here.
			Object modelRoot = editorPart.getFooModel();
			if (modelRoot != null) {
				setViewerContent(modelRoot);
			}
		}

		//set context menu provider.
		ContextMenuProvider menuMgr = new DobVisualEditorContextMenuProvider(getViewer(),
				editorPart.getActionRegistry());

		//menuMgr.setRemoveAllWhenShown(true);
		getViewer().setContextMenu(menuMgr);
		getSite().registerContextMenu(CONTEXT_MENU_ID, menuMgr, getViewer());

		showOverview(showOverviewAction.isChecked());
	}

	/**
	 * @return key handler for common actions.
	 */
	protected KeyHandler createKeyHandler() {
		KeyHandler result = new KeyHandler();
		result.put(KeyStroke.getPressed(SWT.DEL, SWT.DEL, 0), editorPart.getActionRegistry()
				.getAction(ActionFactory.DELETE.getId()));
		result.put(KeyStroke.getPressed(SWT.F2, 0), editorPart.getActionRegistry().getAction(
				GEFActionConstants.DIRECT_EDIT));
		return result;
	}

	@Override
	public EditPartViewer getViewer() {
		return super.getViewer();
	}

	//sets vewer content.
	//can be called in 2 places:
	//1) during editor setup process - if this Outline pane already created.
	//2) during Outline page creation - if editor already set up.
	void setViewerContent(Object o) {
		Tree tree = (Tree) outline;
		tree.setRedraw(false);
		EditPart rootEP = getViewer().getContents();
		rootEP.deactivate();
		rootEP.setModel(o);
		rootEP.activate();
		rootEP.refresh();

		//collapse all and show first.
		TreeItem[] children = tree.getItems();
		if (children.length > 0) {
			collapseAll(tree);
			tree.showItem(children[0]);
		}
		tree.setRedraw(true);
	}

	protected void initializeOverview() {
		overview = new Canvas(pageBook, SWT.NONE);
		LightweightSystem lws = new LightweightSystem(overview);
		ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart) editorPart
				.getGraphicalViewer().getRootEditPart();
		thumbnail = new ScrollableThumbnail((Viewport) root.getFigure());
		thumbnail.setSource(root.getLayer(LayerConstants.PRINTABLE_LAYERS));
		lws.setContents(thumbnail);
	}

	/**
	 * Toggle view mode between overview and ouline tree. 
	 * @param show
	 */
	protected void showOverview(boolean show) {
		if (show) {
			if (overview == null) {
				initializeOverview();
			}
			collapseAllAction.setEnabled(false);
			thumbnail.setVisible(true);
			pageBook.showPage(overview);
		} else {
			if (overview != null) {
				thumbnail.setVisible(false);
			}
			pageBook.showPage(outline);
			collapseAllAction.setEnabled(true);
		}
	}

	@Override
	//register retargetable actions.
	public void setActionBars(IActionBars actionBars) {
		super.setActionBars(actionBars);
		actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), editorPart
				.getAction(ActionFactory.UNDO.getId()));
		actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), editorPart
				.getAction(ActionFactory.REDO.getId()));
		actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), editorPart
				.getAction(ActionFactory.DELETE.getId()));
		actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(), editorPart
				.getAction(ActionFactory.COPY.getId()));
		actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), editorPart
				.getAction(ActionFactory.PASTE.getId()));
	}

	private void collapseAll(Tree tree) {
		collapse(tree.getItems());
	}

	private void collapse(final TreeItem[] items) {
		for (int i = 0; i < items.length; i++) {
			TreeItem item = items[i];
			item.setExpanded(false);
			collapse(item.getItems());
		}
	}

	/**
	 * Copy of JavaVisualEditorOutlinePage#ShowOverviewAction.
	 * @author idanilov
	 *
	 */
	private class ShowOverviewAction extends Action {

		public ShowOverviewAction() {
			super("Show Overview", IAction.AS_CHECK_BOX);
			setImageDescriptor(DobVEPlugin.getDescriptor(IImageConstant.OVERVIEW));
			//new			
			//			setChecked(CDEPlugin.getPlugin().getPluginPreferences().getBoolean(
			//					CDEPlugin.PREF_SHOW_OVERVIEW_KEY));
		}

		public void run() {
			if (isChecked()) {
				setText("Show Outline"); //$NON-NLS-1$
				setToolTipText("Switch to outline view"); //$NON-NLS-1$
			} else {
				setText("Show Overview"); //$NON-NLS-1$
				setToolTipText("Switch to overview"); //$NON-NLS-1$
			}
			showOverview(isChecked());
			//new			
			//			CDEPlugin.getPlugin().getPluginPreferences().setValue(CDEPlugin.PREF_SHOW_OVERVIEW_KEY,
			//					isChecked());
		}

	}

	/**
	 * Copy of JavaVisualEditorOutlinePage#CollapseAllAction.
	 * @author idanilov
	 *
	 */
	private class CollapseAllAction extends Action {

		public CollapseAllAction() {
			super(TITLE, IAction.AS_PUSH_BUTTON);
			setToolTipText(TOOL_TIP);
			setImageDescriptor(UiUtils.IMG_COLLAPSE_ALL);
			setDisabledImageDescriptor(UiUtils.IMG_COLLAPSE_ALL_DISABLED);
		}

		public void run() {
			collapseAll((Tree) outline);
		}

	}
}
