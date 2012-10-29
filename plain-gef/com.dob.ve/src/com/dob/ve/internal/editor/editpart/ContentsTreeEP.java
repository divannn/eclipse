package com.dob.ve.internal.editor.editpart;

import org.eclipse.gef.EditPolicy;

import com.dob.ve.abstractmodel.DobXmlModel;
import com.dob.ve.internal.editor.policy.DobTreeContainerEditPolicy;
import com.dob.ve.internal.editor.util.ContainerRestrictionConstant;

/**
 * @author idanilov
 *
 */
public class ContentsTreeEP extends BaseTreeEP {

	public ContentsTreeEP(Object model) {
		super();
		setModel(model);
	}

	@Override
	protected DobXmlModel getElement() {
		return (DobXmlModel) getModel();
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.TREE_CONTAINER_ROLE, new DobTreeContainerEditPolicy(
				ContainerRestrictionConstant.ALLOW_ALL));
	}

}
