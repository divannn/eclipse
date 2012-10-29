package com.dob.ve.internal.editor.policy;

import java.util.List;

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.GroupRequest;

import com.dob.ve.abstractmodel.DobXmlElement;
import com.dob.ve.internal.editor.command.DeleteCommand;

/**
 * @author idanilov
 *
 */
public class DobComponentEditPolicy extends ComponentEditPolicy {

	@Override
	public Command getCommand(Request request) {
		//allow only 1 child in manipulation.
		if (REQ_ORPHAN.equals(request.getType())) {
			ChangeBoundsRequest req = (ChangeBoundsRequest) request;
			List editParts = req.getEditParts();
			//System.err.println("edit parts: " + editParts.size());
			if (editParts != null && editParts.size() == 1) {
				return getOrphanCommand();
			}
			return UnexecutableCommand.INSTANCE;
		}
		return super.getCommand(request);
	}

	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		DeleteCommand result = new DeleteCommand();
		DobXmlElement parent = (DobXmlElement) getHost().getParent().getModel();
		DobXmlElement child = (DobXmlElement) getHost().getModel();
		result.setParent(parent);
		result.setChild(child);
		return result;
	}

}
