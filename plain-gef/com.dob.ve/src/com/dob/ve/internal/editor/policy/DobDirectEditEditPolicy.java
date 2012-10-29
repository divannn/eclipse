package com.dob.ve.internal.editor.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import com.dob.ve.internal.editor.command.SetPropertyCommand;

/**
 * @author idanilov
 *
 */
public class DobDirectEditEditPolicy extends DirectEditPolicy {

	@Override
	protected Command getDirectEditCommand(DirectEditRequest request) {
		Object newValue = request.getCellEditor().getValue();
		SetPropertyCommand result = new SetPropertyCommand();
		result.setModel(getHost().getModel());
		//System.err.println("Ll " + request.getDirectEditFeature());
		result.setKey(request.getDirectEditFeature());
		result.setValue(newValue);
		return result;
	}

	@Override
	protected void showCurrentEditValue(DirectEditRequest request) {
	}

}
