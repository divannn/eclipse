package com.dob.ve.internal.editor.policy;

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;

import com.dob.ve.internal.editor.command.CopyCommand;
import com.dob.ve.internal.editor.gef.IRequestConstants2;

/**
 * @author idanilov
 *
 */
public class DobCopyEditPolicy extends AbstractEditPolicy {

	public static final String COPY_ROLE = "CopyPolicy";

	public Command getCommand(Request request) {
		if (IRequestConstants2.REQ_COPY.equals(request.getType())) {
			return getCopyCommand();
		}
		return super.getCommand(request);
	}

	protected Command getCopyCommand() {
		CopyCommand result = new CopyCommand();
		result.setSource(getHost().getModel());
		return result;
	}

}
