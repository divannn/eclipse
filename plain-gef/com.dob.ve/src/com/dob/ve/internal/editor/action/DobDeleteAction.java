package com.dob.ve.internal.editor.action;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.ui.IWorkbenchPart;

import com.dob.ve.internal.editor.DobPropertySheetEntry;
import com.dob.ve.internal.editor.gef.EditPartUtil;

/**
 * @author idanilov
 *
 */
public class DobDeleteAction extends DeleteAction {

	public DobDeleteAction(IWorkbenchPart part) {
		super(part);
	}

	@Override
	//overriden to create command only for top level beans -
	//no sense to delete child of parent what is being deleted.
	public Command createDeleteCommand(List objects) {
		if (objects.isEmpty()) {
			return null;
		}
		if (!(objects.get(0) instanceof EditPart)) {
			return null;
		}

		GroupRequest deleteReq = new GroupRequest(RequestConstants.REQ_DELETE);
		deleteReq.setEditParts(objects);
		CompoundCommand result = new CompoundCommand(
		/*GEFMessages.DeleteAction_ActionDeleteCommandName*/);//will take label from 1st added command.
		List<EditPart> topLevelBeans = EditPartUtil.getSelectionWithoutDependants(objects);
		for (int i = 0; i < topLevelBeans.size(); i++) {
			EditPart object = topLevelBeans.get(i);
			Command cmd = object.getCommand(deleteReq);
			if (cmd != null) {
				result.add(cmd);
			}
		}
		if (result.size() > 1) {
			result.setLabel(result.getLabel() + DobPropertySheetEntry.MULTI_SUFFIX);
		}
		return result;
	}

}
