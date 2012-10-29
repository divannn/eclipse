package com.dob.ve.internal.editor.policy;

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;

import com.dob.ve.internal.editor.util.IContainerRestriction;

/**
 * For contents graphical part behaviour can be customized.
 * @author idanilov
 *
 */
public class ContentsXYEditPolicy extends AbstractDobXYEditPolicy {

	public ContentsXYEditPolicy(IContainerRestriction r) {
		super(r);
	}

	@Override
	public Command getCommand(Request request) {
		Object model = getHost().getModel();
		//model is null for first invokation.
		//see new ContentsGraphicalEP(null) usage.
		if (model == null) {
			return null;
		}
		return super.getCommand(request);
	}

}
