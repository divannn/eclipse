package com.dob.ve.internal.editor;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.AlignmentAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.RedoAction;
import org.eclipse.gef.ui.actions.ToggleGridAction;
import org.eclipse.gef.ui.actions.ToggleSnapToGeometryAction;
import org.eclipse.gef.ui.actions.UndoAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.progress.IWorkbenchSiteProgressService;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetPage;

import com.dob.util.PartListenerAdapter;
import com.dob.util.PluginLogger;
import com.dob.util.UiUtils;
import com.dob.ve.abstractmodel.DobXmlElement;
import com.dob.ve.abstractmodel.DobXmlModel;
import com.dob.ve.internal.DobVEPlugin;
import com.dob.ve.internal.editor.action.DobCopyAction;
import com.dob.ve.internal.editor.action.DobDeleteAction;
import com.dob.ve.internal.editor.action.DobDirectEditAction;
import com.dob.ve.internal.editor.action.DobPasteAction;
import com.dob.ve.internal.editor.editpart.ContentsGraphicalEP;
import com.dob.ve.internal.editor.editpart.DobGraphicalEPFactory;
import com.dob.ve.internal.editor.factory.CreationInfo;
import com.dob.ve.internal.editor.factory.CreationModelFactory;
import com.dob.ve.internal.editor.factory.LabelProviderFactory;
import com.dob.ve.internal.model.FooModelUtils;
import com.dob.ve.internal.model.IFooModelType;

/**
 * Main editor class. See plugin.xml where "editors" extension point declared for this class.
 * @author idanilov
 *
 */
public class DobVisualEditor extends GraphicalEditorWithFlyoutPalette
		implements CommandStackEventListener {

	public static final String ID = /*IDobIdeConstant.VISUAL_EDITOR_ID*/"dob.visualeditor";
	private final static String CONTEXT_MENU_ID = "dob.visualeditor.contextmenu";

	public static final String DXF_FILE = "file";

	/**
	 * Flag which is true when there are unsaved chanegs in editor.
	 */
	private boolean dirtyState;

	private Job setupJob;

	private PaletteRoot paletteRoot;
	private DobOutlinePage outlinePage;
	private IPropertySheetPage propertySheetPage;
	private LoadingFigureController loadingController;

	private ResourceChangeListener resourceListener;
	private IPartListener partListener;

	//when true - editor was asked to close.
	private boolean isDisposing;

	//when true - everything is set up (model imported).
	private boolean isReady;

	private DobXmlModel model;

	private static final PluginLogger LOGGER = DobVEPlugin.getLogger(DobVEPlugin.LOGGER_EDITOR);

	//Returns various parts of editor via standars IAdaptable mechanism.
	public Object getAdapter(Class adapterKey) {
		if (adapterKey == IContentOutlinePage.class) {
			return getContentOutlinePage();
		} else if (adapterKey == IPropertySheetPage.class) {
			return getPropertySheetPage();
		} else if (adapterKey == org.eclipse.gef.EditDomain.class) {
			return getEditDomain();
		} else if (adapterKey == ZoomManager.class) {
			return getGraphicalViewer().getProperty(ZoomManager.class.toString());
		} else {
			return super.getAdapter(adapterKey);
		}
	}

	/**
	 * @return VE edit domain
	 */
	public DobEditDomain getVEEditDomain() {
		return (DobEditDomain) super.getEditDomain();
	}

	/**
	 * Outline page for this editor.
	 */
	IContentOutlinePage getContentOutlinePage() {
		if (outlinePage == null) {
			outlinePage = createOutlinePage();
		}
		return outlinePage;
	}

	protected DobOutlinePage createOutlinePage() {
		return new DobOutlinePage(this, new TreeViewer());
	}

	void clearContentOutlinePage() {
		outlinePage = null;
	}

	protected IPropertySheetPage getPropertySheetPage() {
		if (propertySheetPage == null) {
			propertySheetPage = createPropertySheetPage();
		}
		return propertySheetPage;
	}

	protected IPropertySheetPage createPropertySheetPage() {
		PropertySheetPage result = new DobPropertySheetPage(this);
		result.setRootEntry(new DobPropertySheetEntry(getCommandStack()));
		return result;
	}

	@Override
	//just to be acessible by outline.
	protected DefaultEditDomain getEditDomain() {
		return super.getEditDomain();
	}

	@Override
	//just to be acessible by outline.
	protected SelectionSynchronizer getSelectionSynchronizer() {
		return super.getSelectionSynchronizer();
	}

	@Override
	//just to be acessible by ouline.
	protected GraphicalViewer getGraphicalViewer() {
		return super.getGraphicalViewer();
	}

	@Override
	//just to be acessible by ouline.
	protected ActionRegistry getActionRegistry() {
		return super.getActionRegistry();
	}

	@Override
	//Creates VE model.
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		if (input instanceof IFileEditorInput) {
			LOGGER.debug(this, "Initializing... Input: " + input);
			setInput(input);
			setPartName(input.getName());

			IFile file = getFile();
			resourceListener = new ResourceChangeListener();
			file.getWorkspace().addResourceChangeListener(resourceListener);

			DobEditDomain domain = createEditDomain();
			setEditDomain(domain);

			loadingController = new LoadingFigureController();

			loadingController.showLoadingFigure(true);

			setupJob = new SetupJob("Setting up editor " + input.getName() + " ...");
			//setupJob.schedule(01);
			IWorkbenchSiteProgressService service = (IWorkbenchSiteProgressService) getSite()
					.getAdapter(IWorkbenchSiteProgressService.class);
			service.schedule(setupJob, 01, true);

			initializeActionRegistry();

			getCommandStack().addCommandStackListener(this);
			getCommandStack().addCommandStackEventListener(this);

			getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);

			partListener = new PartListener();
			getSite().getWorkbenchWindow().getPartService().addPartListener(partListener);
		} else {
			throw new PartInitException("Expected IFileEditorInput editor input.");
		}
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		super.selectionChanged(part, selection);
		if (this.equals(getSite().getPage().getActiveEditor())) {
			if (selection instanceof IStructuredSelection) {
				setStatusMessage((IStructuredSelection) selection);
			}
		}
	}

	private void setStatusMessage(IStructuredSelection sel) {
		String msg = null;
		int selSize = sel.size();
		if (selSize > 1) {
			msg = selSize + " items selected";
		} else {
			Object first = sel.getFirstElement();
			if (first instanceof EditPart) {
				EditPart ep = (EditPart) first;
				Object m = ep.getModel();
				if (m instanceof DobXmlElement) {
					msg = FooModelUtils.getType((DobXmlElement) m);
				}
			}
		}
		getEditorSite().getActionBars().getStatusLineManager().setMessage(msg);
	}

	/**
	 * VE model for this editor. 
	 */
	public DobXmlModel getFooModel() {
		return model;
	}

	/**
	 * Creates edit domain and init part of VE related to it.
	 */
	protected DobEditDomain createEditDomain() {
		DobEditDomain result = new DobEditDomain(this);
		result.setData(DXF_FILE, getFile());
		return result;
	}

	/**
	 * @return project for this editor
	 */
	IProject getProject() {
		return getFile().getProject();
	}

	/**
	 * @return file that this editor works with
	 */
	IFile getFile() {
		IEditorInput input = getEditorInput();
		return ((IFileEditorInput) input).getFile();
	}

	@Override
	public void createPartControl(Composite parent) {
		LOGGER.debug(this, "Creating editor part control");
		super.createPartControl(parent);
		//initializeViewers();
		loadingController.startListener(getGraphicalViewer());
	}

	/**
	 * @param parent
	 * @return graphical viewer
	 */
	protected void createGraphicalViewer(final Composite parent) {
		super.createGraphicalViewer(parent);//registering viewer in domain is here.
		GraphicalViewer result = getGraphicalViewer();
		createDiagramActions(result);

		result.setEditPartFactory(new DobGraphicalEPFactory());
		ScalableFreeformRootEditPart rootEditPart = new ScalableFreeformRootEditPart();

		//zoom actions need zoom manager - so create them here (not in createActions()).
		createZoomActions(rootEditPart.getZoomManager());

		/*((ConnectionLayer) rootEditPart.getLayer(LayerConstants.CONNECTION_LAYER))
		 .setConnectionRouter(new ManhattanConnectionRouter());*/
		result.setRootEditPart(rootEditPart);

		//set contents - the same as standartd VE does.
		//set model to null on editor init. It will be set later in initializeViewers.
		result.setContents(new ContentsGraphicalEP(null));

		//set context menu.
		KeyHandler kh = new GraphicalViewerKeyHandler(result).setParent(createKeyHandler());
		result.setKeyHandler(kh);

		ContextMenuProvider menuProvider = new DobVisualEditorContextMenuProvider(result,
				getActionRegistry());
		result.setContextMenu(menuProvider);
		getEditorSite().registerContextMenu(CONTEXT_MENU_ID, menuProvider, result, false);

	}

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();

		//set default snap to geometry on.
		getGraphicalViewer().setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED, Boolean.TRUE);

		//set default grid off.
		getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, Boolean.FALSE);
		getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, Boolean.FALSE);
		getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_SPACING, new Dimension(10, 10));
	}

	/**
	 * @return key handler for common actions.
	 */
	private KeyHandler createKeyHandler() {
		KeyHandler result = new KeyHandler() {
			/* 4debug.
			 * @Override
			 public boolean keyPressed(KeyEvent event) {
			 return super.keyPressed(event);
			 }*/
		};
		IAction deleteAction = getAction(ActionFactory.DELETE.getId());
		result.put(KeyStroke.getPressed(SWT.DEL, 0), deleteAction);
		result.put(KeyStroke.getPressed('=', 61, SWT.CTRL), getAction(GEFActionConstants.ZOOM_IN));//Ctrl+Plus.
		result.put(KeyStroke.getPressed('-', 45, SWT.CTRL), getAction(GEFActionConstants.ZOOM_OUT));//Ctrl+Minus.
		result.put(KeyStroke.getPressed(SWT.F2, 0), getActionRegistry().getAction(
				GEFActionConstants.DIRECT_EDIT));
		return result;
	}

	private void createDiagramActions(final GraphicalViewer gv) {
		IAction showGridAction = new ToggleGridAction(gv);
		getActionRegistry().registerAction(showGridAction);

		IAction snap2geomAction = new ToggleSnapToGeometryAction(gv);
		getActionRegistry().registerAction(snap2geomAction);
	}

	/**
	 * Zoom action created as retargetable in DOBVisualEditorActionBarContributor.
	 * @param zm
	 */
	private void createZoomActions(final ZoomManager zm) {
		IAction zoomIn = new ZoomInAction(zm);
		getActionRegistry().registerAction(zoomIn);
		//getSite().getKeyBindingService().registerAction(zoomIn);//Ctrl+Plus.

		IAction zoomOut = new ZoomOutAction(zm);
		getActionRegistry().registerAction(zoomOut);
		//getSite().getKeyBindingService().registerAction(zoomOut);//Ctrl+Minus.
	}

	/**
	 * Init both graphical and tree viewers (actuallyoonly graphical viewer processed - tree viewer doesn't registered yet here).
	 */
	protected void initializeViewers() {
		if (isDisposing) {
			return;
		}

		setViewerContent(model);
		if (outlinePage != null) {
			outlinePage.setViewerContent(model);
		}
	}

	private void setViewerContent(final Object o) {
		EditPart rootEP = getGraphicalViewer().getContents();
		rootEP.deactivate();
		rootEP.setModel(o);
		rootEP.activate();
		rootEP.refresh();
	}

	@Override
	public void setFocus() {
		LOGGER.debug(this, "Setting focus");
		//focus on graphical editor.
		GraphicalViewer gViewer = getGraphicalViewer();
		if (gViewer != null) {
			gViewer.getControl().setFocus();
		}
	}

	@Override
	public boolean isDirty() {
		//return getCommandStack().isDirty();//from GraphicalEditor.
		return dirtyState;
	}

	/**
	 * Set the dirty state. Only signal the first time it goes dirty until
	 * it becomes clean.
	 */
	protected void setDirty(final boolean dirty) {
		// If being set clean, signal. Else if not already dirty, then signal.
		if (!dirty || !dirtyState) {
			dirtyState = dirty;
			firePropertyChange(PROP_DIRTY);
		}
	}

	@Override
	//doesn't not allow "save as" operation.
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		LOGGER.debug(this, "Saving editor");
		//operation that perform saving VE model to file.
		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {

			public void execute(final IProgressMonitor mon) throws CoreException,
					InvocationTargetException, InterruptedException {
				mon.beginTask("Saving editor input file", 10);
				try {
					//new 
					//veModelSupport.saveVEModel();
					setDirty(false);
				} finally {
					mon.done();
				}
			}
		};

		try {
			op.run(monitor);
			//getCommandStack().markSaveLocation();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		} catch (InvocationTargetException e) {
			Throwable eReal = e.getTargetException();
			IStatus status = null;
			if (eReal instanceof CoreException) {
				status = ((CoreException) eReal).getStatus();
			} else {
				status = new Status(IStatus.ERROR, DobVEPlugin.getId(), 0, "Save error", eReal);
			}
			DobVEPlugin.getDefault().getLog().log(status);
			//showErrorMessage(status.getMessage());
		}
	}

	@Override
	public void doSaveAs() {
		throw new RuntimeException("\"Save as\" is not implemented.");
	}

	@Override
	public void dispose() {
		isDisposing = true;
		LOGGER.debug(this, "Disposing editor");

		if (resourceListener != null) {
			((IFileEditorInput) getEditorInput()).getFile().getWorkspace()
					.removeResourceChangeListener(resourceListener);
			resourceListener = null;
		}

		if (partListener != null) {
			getSite().getWorkbenchWindow().getPartService().removePartListener(partListener);
			partListener = null;
		}

		clearContentOutlinePage();
		CommandStack cs = getCommandStack();
		if (cs != null) {
			cs.removeCommandStackListener(this);
			cs.removeCommandStackEventListener(this);
		}

		getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(this);
		getActionRegistry().dispose();

		boolean isSetupRunning = setupJob != null && setupJob.getState() != Job.NONE;
		if (isSetupRunning) {
			setupJob.cancel();
			final Display d = Display.getCurrent();
			Job cleanupJob = new Job("Cleaning editor ...") {

				protected IStatus run(IProgressMonitor monitor) {
					//wait until setup job is finished.
					while (true) {
						try {
							setupJob.join();
							break;
						} catch (InterruptedException ie) {
						}
					}
					//Slight possibility display is already disposed, that this occurred on shutdown, so don't bother with final dispose.
					if (!d.isDisposed()) {
						d.asyncExec(new Runnable() {

							public void run() {
								finalDispose();
							}
						});
					}
					return Status.OK_STATUS;
				}
			};
			cleanupJob.schedule();
		} else {
			finalDispose();
		}
		super.dispose();
	}

	private void finalDispose() {
		//dispose model.
		if (model != null) {
			model.dispose();
			model = null;
		}

		//dispose domain. 
		DobEditDomain domain = getVEEditDomain();
		if (domain != null) {
			domain.dispose();
		}
	}

	protected IAction getAction(final String actionID) {
		return getActionRegistry().getAction(actionID);
	}

	@Override
	protected void createActions() {
		LOGGER.debug(this, "Creating actions");
		ActionRegistry registry = getActionRegistry();
		IAction action;

		action = new UndoAction(this);
		registry.registerAction(action);
		getStackActions().add(action.getId());

		action = new RedoAction(this);
		registry.registerAction(action);
		getStackActions().add(action.getId());

		action = new DobDeleteAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new DobDirectEditAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart) this, PositionConstants.LEFT);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart) this, PositionConstants.RIGHT);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart) this, PositionConstants.TOP);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart) this, PositionConstants.BOTTOM);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		ISharedImages images = PlatformUI.getWorkbench().getSharedImages();
		DobPasteAction pasteBeanAction = new DobPasteAction(this);
		pasteBeanAction.setImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		registry.registerAction(pasteBeanAction);
		getSelectionActions().add(pasteBeanAction.getId());

		DobCopyAction copyBeanAction = new DobCopyAction(this);
		copyBeanAction.setImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		registry.registerAction(copyBeanAction);
		getSelectionActions().add(copyBeanAction.getId());
	}

	/*public void commandStackChanged(EventObject event) {
	 super.commandStackChanged(event);
	 }*/

	//update dirty state. 
	public void stackChanged(CommandStackEvent event) {
		if (event.isPostChangeEvent()) {
			setDirty(true);
		}
	}

	@Override
	protected PaletteRoot getPaletteRoot() {
		if (paletteRoot == null) {
			paletteRoot = createPaletteRoot();
		}
		return paletteRoot;
	}

	protected PaletteRoot createPaletteRoot() {
		PaletteRoot result = new PaletteRoot();
		PaletteGroup manipGroup = new PaletteGroup("Manipulation");
		result.add(manipGroup);

		SelectionToolEntry selectionToolEntry = new SelectionToolEntry();
		selectionToolEntry.setToolClass(DobSelectionTool.class);
		manipGroup.add(selectionToolEntry);
		manipGroup.add(new MarqueeToolEntry());

		PaletteDrawer visualsGroup = new PaletteDrawer("Components");
		ImageDescriptor smallIcon = EditorImages.getDescriptor("drawer-components.gif");
		visualsGroup.setSmallIcon(smallIcon);
		result.add(visualsGroup);

		String type = IFooModelType.HOLDER;
		smallIcon = LabelProviderFactory.getInstance().getImageDescriptor(type);
		visualsGroup.add(new CreationToolEntry(type, "Container", new CreationModelFactory(
				new CreationInfo(type)), smallIcon, null));

		type = IFooModelType.RECTANGLE;
		smallIcon = LabelProviderFactory.getInstance().getImageDescriptor(type);
		visualsGroup.add(new CreationToolEntry(type, "Leaf", new CreationModelFactory(
				new CreationInfo(type)), smallIcon, null));

		type = IFooModelType.LABEL;
		smallIcon = LabelProviderFactory.getInstance().getImageDescriptor(type);
		visualsGroup.add(new CreationToolEntry(type, "Leaf", new CreationModelFactory(
				new CreationInfo(type)), smallIcon, null));

		type = IFooModelType.LABEL2;
		visualsGroup.add(new CreationToolEntry(type, "Leaf. Non-resizable.",
				new CreationModelFactory(new CreationInfo(type)), smallIcon, null));

		result.setDefaultEntry(selectionToolEntry);
		return result;
	}

	/**
	 * Closes this editor.
	 * @param save
	 */
	protected void closeEditor(final boolean save) {
		LOGGER.debug(this, "Closing editor");
		getSite().getPage().closeEditor(this, save);
	}

	boolean isReady() {
		return isReady;
	}

	/**
	 * Listens to project close event to close editor.
	 * Taken from carbide-ui.
	 */
	private class ResourceChangeListener
			implements IResourceChangeListener, IResourceDeltaVisitor {

		public void resourceChanged(IResourceChangeEvent event) {
			IResourceDelta delta = event.getDelta();
			if (delta != null) {
				try {
					delta.accept(this);
				} catch (CoreException ce) {
					DobVEPlugin.error("Problem during resource event change handling", ce);
				}
			}

		}

		public boolean visit(IResourceDelta delta) throws CoreException {
			IFile file = getFile();
			if (delta == null || !delta.getResource().equals(file)) {
				return true;
			}
			Display display = getSite().getShell().getDisplay();
			//System.err.println("[file changed] - delta kind: " + delta.getKind() + ", flags:  " + delta.getFlags());
			if (delta.getKind() == IResourceDelta.REMOVED) {
				if ((IResourceDelta.MOVED_TO & delta.getFlags()) == 0) {//file was deleted.
					//System.err.println("[file changed] REMOVE");
					LOGGER.debug(DobVisualEditor.this, "Editor file removed");
					display.asyncExec(new Runnable() {

						public void run() {
							closeEditor(false);
						}
					});
				} else {//was moved or renamed.
					//System.err.println("[file changed] RENAME");
					LOGGER.debug(DobVisualEditor.this, "Editor file moved/renamed");
					display.asyncExec(new Runnable() {

						public void run() {
							Shell shell = getSite().getShell();
							String title = "File Change";
							String message = "Editor file has been changed. Editor will be closed.";
							String[] buttons = { "OK" };
							MessageDialog dialog = new MessageDialog(shell, title, null, message,
									MessageDialog.INFORMATION, buttons, 0);
							dialog.open();
							closeEditor(false);
						}
					});
				}
			}
			return false;
		}

	}

	/**
	 * For demo. Listens when this editor activated.
	 * @author idanilov
	 *
	 */
	private class PartListener extends PartListenerAdapter {

		@Override
		public void partActivated(IWorkbenchPart part) {
			if (part == DobVisualEditor.this) {
				LOGGER.debug(this, "Editor activated: " + getTitle());
				//System.err.println("Editor activated " + part.getTitle());
			}
		}

	}

	private DobXmlModel createEmptyXmlModel() {
		DobXmlModel result = new DobXmlModel();
		return result;
	}

	private DobXmlModel createXmlModel() throws CoreException {
		DobXmlModel result = createEmptyXmlModel();
		return result;

	}

	/**
	 * Performs initialization in separate thread.
	 * Starts beaninfo VM, target editor VM and imports content of model.
	 * @author idanilov
	 *
	 */
	private class SetupJob extends Job {

		public SetupJob(final String name) {
			super(name);
		}

		@Override
		protected IStatus run(final IProgressMonitor monitor) {
			IStatus result = Status.OK_STATUS;
			try {
				model = createXmlModel();

				isReady = true;
				//long e = System.currentTimeMillis();
				//System.err.println("g " + (e - s));
			} catch (CoreException cex) {
				DobVEPlugin.error("Failed to create VE model", cex);
			} finally {
				Display d = Display.getDefault();
				if (model != null) {//model loaded.
					d.asyncExec(new Runnable() {

						public void run() {
							//set contents into GEF viewers (graphical and tree).
							initializeViewers();

							end();
						}
					});
					d.asyncExec(new Runnable() {

						public void run() {
							end();
						}
					});
				} else {//errors appeared during loading.
					d.asyncExec(new Runnable() {

						public void run() {
							end();
						}
					});
					result = UiUtils.ERROR_STATUS;
				}
			}
			return result;
		}

		private void end() {
			loadingController.showLoadingFigure(false);
		}
	}

}