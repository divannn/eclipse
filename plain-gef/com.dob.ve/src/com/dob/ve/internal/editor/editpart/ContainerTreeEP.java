package com.dob.ve.internal.editor.editpart;

import org.eclipse.gef.EditPolicy;

import com.dob.ve.internal.editor.policy.DobTreeContainerEditPolicy;
import com.dob.ve.internal.editor.util.ContainerRestrictionConstant;

/**
 * @author idanilov
 *
 */
public abstract class ContainerTreeEP extends ElementTreeEP
		implements IDobElementContextMenuContributor {

	public ContainerTreeEP() {
		super();
	}

	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		//allow create, paste, add/orphan, reorder(move in parent) children.
		installEditPolicy(EditPolicy.TREE_CONTAINER_ROLE, new DobTreeContainerEditPolicy(
				ContainerRestrictionConstant.ALLOW_ALL));
	}

}
