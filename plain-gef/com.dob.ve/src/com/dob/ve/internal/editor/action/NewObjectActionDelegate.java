package com.dob.ve.internal.editor.action;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionDelegate;

import com.dob.ve.abstractmodel.DobXmlElement;
import com.dob.ve.internal.editor.DobEditDomain;
import com.dob.ve.internal.editor.factory.CreationInfo;
import com.dob.ve.internal.editor.util.CreateUtil;

/**
 * @author idanilov
 *
 */
public class NewObjectActionDelegate extends ActionDelegate
		implements IObjectActionDelegate, IExecutableExtension {

	protected IStructuredSelection selection;
	protected IWorkbenchPart wbPart;
	protected String initStr;

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		wbPart = targetPart;
	}

	public void selectionChanged(IAction action, ISelection sel) {
		// Test to see if action is enabled, if it is, that means we've already passed the enablesFor test.
		if (action.isEnabled()) {
			if (sel instanceof IStructuredSelection) {
				if (initStr != null) {
					this.selection = (IStructuredSelection) sel;
				} else {
					action.setEnabled(false);
				}
			} else {
				action.setEnabled(false);
			}
		}
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		EditPart firstEP = (EditPart) selection.getFirstElement();
		CreateRequest req = CreateUtil.createRequest((DobXmlElement) firstEP.getModel(),
				new CreationInfo(initStr));
		Command cmd = firstEP.getCommand(req);
		if (cmd != null && cmd.canExecute()) {
			EditDomain domain = DobEditDomain.getEditDomain(firstEP);
			domain.getCommandStack().execute(cmd);
			EditPartViewer viewer = firstEP.getViewer();
			selectAddedObject(viewer, req.getNewObject());
		}
	}

	private void selectAddedObject(EditPartViewer viewer, Object model) {
		Object editpart = viewer.getEditPartRegistry().get(model);
		if (editpart instanceof EditPart) {
			//Force the new object to get positioned in the viewer. 
			viewer.flush();
			viewer.select((EditPart) editpart);
		}
	}

	public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
			throws CoreException {
		if (data instanceof String) {
			initStr = (String) data;
		}
	}

}
