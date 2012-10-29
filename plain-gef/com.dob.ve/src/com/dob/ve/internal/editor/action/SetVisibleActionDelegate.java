package com.dob.ve.internal.editor.action;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionDelegate;

import com.dob.ve.abstractmodel.DobXmlElement;
import com.dob.ve.internal.editor.DobEditDomain;
import com.dob.ve.internal.editor.command.SetPropertyCommand;
import com.dob.ve.internal.model.FooModelUtils;
import com.dob.ve.internal.model.IFooModelProperty;

/**
 * @author idanilov
 *
 */
public class SetVisibleActionDelegate extends ActionDelegate
		implements IObjectActionDelegate {

	//private IWorkbenchPart part;
	private IStructuredSelection selection;

	public void selectionChanged(IAction action, ISelection sel) {
		//test to see if action is enabled, if it is, that means we've already passed the enablesFor test.
		if (action.isEnabled()) {
			if (sel instanceof IStructuredSelection) {
				IStructuredSelection s = (IStructuredSelection) sel;
				if (s.size() == 1) {
					this.selection = s;
					//System.err.println(">>>selection: " + s.getFirstElement());
					action.setChecked(calculateChecked());
				} else {
					action.setEnabled(false);
				}
			} else {
				action.setEnabled(false);
			}
		}
	}

	private boolean calculateChecked() {
		boolean result = false;
		Object first = selection.getFirstElement();
		if (first instanceof EditPart) {
			EditPart ep = (EditPart) first;
			Object bean = ep.getModel();
			if (bean instanceof DobXmlElement) {
				result = FooModelUtils.isVisible(((DobXmlElement) bean)).booleanValue();
			}
		}
		return result;
	}

	public void run(IAction action) {
		EditPart first = (EditPart) selection.getFirstElement();
		DobXmlElement bean = (DobXmlElement) first.getModel();
		boolean isVisible = FooModelUtils.isVisible(bean).booleanValue();

		SetPropertyCommand cmd = new SetPropertyCommand();
		cmd.setModel(bean);
		cmd.setKey(IFooModelProperty.VISIBLE);
		cmd.setValue(new Boolean(!isVisible));

		EditDomain ed = DobEditDomain.getEditDomain(first);
		CommandStack cs = ed.getCommandStack();
		cs.execute(cmd);
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		//part = targetPart;
	}

}
