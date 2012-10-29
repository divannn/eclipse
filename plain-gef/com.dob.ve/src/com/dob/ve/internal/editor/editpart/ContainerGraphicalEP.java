package com.dob.ve.internal.editor.editpart;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;

import com.dob.ve.internal.editor.figure.ContainerFigure;
import com.dob.ve.internal.editor.policy.ContainerXYLayoutPolicy;
import com.dob.ve.internal.editor.util.ContainerRestrictionConstant;

/**
 * @author idanilov
 *
 */
public abstract class ContainerGraphicalEP extends ElementGraphicalEP
		implements IDobElementContextMenuContributor {

	public ContainerGraphicalEP() {
		super();
	}

	@Override
	protected IFigure createFigure() {
		return new ContainerFigure();
	}

	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		//allow create, paste, layout children.
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new ContainerXYLayoutPolicy(
				ContainerRestrictionConstant.ALLOW_ALL));
	}

}
