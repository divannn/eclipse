package com.dob.ve.internal.editor.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.TreeContainerEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import com.dob.ve.internal.editor.util.IContainerRestriction;

/**
 * @author idanilov
 *
 */
public abstract class BaseTreeContainerEditPolicy extends TreeContainerEditPolicy {

	protected IContainerRestriction restriction;

	@Override
	protected Command getAddCommand(ChangeBoundsRequest request) {
		return null;
	}

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		return null;
	}

	@Override
	protected Command getMoveChildrenCommand(ChangeBoundsRequest request) {
		return null;
	}

}
