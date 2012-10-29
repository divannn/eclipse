package com.dob.ve.internal.editor.policy;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;

/**
 * Applicable for any bean.
 * Forwards MOVE request to parent.
 * XXX: Maybe move functionality to {@link DobComponentEditPolicy}.
 * @author idanilov
 *
 */
public class DobTreeMoveEditPolicy extends AbstractEditPolicy {

	public Command getCommand(Request req) {
		if (RequestConstants.REQ_MOVE.equals(req.getType())) {
			//System.err.println(">>> " + req.getType() + "  " + req.getClass());
			return getMoveCommand((ChangeBoundsRequest) req);
		}
		return null;
	}

	//called when edit part moved within its parent.
	//only one child can be reordered at time.
	protected Command getMoveCommand(ChangeBoundsRequest req) {
		EditPart parentEP = getHost().getParent();
		if (parentEP != null) {
			List editParts = req.getEditParts();
			//System.err.println("size: " + editParts.size());
			if (editParts != null && editParts.size() == 1) {
				//				DobXmlElement h = (DobXmlElement) getHost().getModel();
				//				DobXmlElement f = (DobXmlElement) ((EditPart) req.getEditParts().get(0)).getModel();
				//				System.err.println("---h: " + h.getOProperty("type"));
				//				System.err.println("---f: " + f.getOProperty("type"));
				ChangeBoundsRequest request = new ChangeBoundsRequest(
						RequestConstants.REQ_MOVE_CHILDREN);
				request.setEditParts(getHost());
				request.setLocation(req.getLocation());
				return parentEP.getCommand(request);
			}
		}
		return UnexecutableCommand.INSTANCE;
	}

}