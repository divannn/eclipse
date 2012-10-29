package com.dob.ve.internal.editor.editpart.elem;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;

/**
 * Just t
 * @author idanilov
 *
 */
public class Label2GraphicalEP extends LabelGraphicalEP {

	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		NonResizableEditPolicy ep = new NonResizableEditPolicy();
		//uncomment to forbid move also.
		//ep.setDragAllowed(false);
		installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE, ep);
	}
}
