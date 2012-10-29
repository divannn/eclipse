package com.dob.ve.internal.editor.action;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;

import com.dob.ve.internal.editor.gef.IRequestConstants2;

/**
 * @author idanilov
 *
 */
public class DobCopyAction extends SelectionAction {

	private Command copyCommand;

	//private EditDomain editDomain;

	public DobCopyAction(IWorkbenchPart part) {
		super(part);
		setId(ActionFactory.COPY.getId());
		setText("Copy");
	}

	@Override
	protected boolean calculateEnabled() {
		List selection = getSelectedObjects();
		if (selection.size() == 1) {
			Object first = selection.get(0);
			if (first instanceof EditPart) {
				EditPart selectedEditPart = (EditPart) first;
				//editDomain = DobEditDomain.getEditDomain(selectedEditPart);
				Request copyRequest = new Request(IRequestConstants2.REQ_COPY);
				copyCommand = selectedEditPart.getCommand(copyRequest);
				if (copyCommand != null) {
					return copyCommand.canExecute();
				}
			}
		}
		return false;
	}

	@Override
	public void run() {
		//editDomain.getCommandStack().execute(copyCommand);
		copyCommand.execute();//do not run through commandStack.
	}

}
