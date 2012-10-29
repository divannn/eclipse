package com.dob.ve.internal.editor.editpart;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.CompoundSnapToHelper;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;

import com.dob.ve.abstractmodel.DobXmlModel;
import com.dob.ve.internal.editor.figure.ContentsFigure;
import com.dob.ve.internal.editor.policy.ContentsXYEditPolicy;
import com.dob.ve.internal.editor.util.ContainerRestrictionConstant;

/**
 * @author idanilov
 *
 */
public class ContentsGraphicalEP extends BaseGraphicalEP {

	public ContentsGraphicalEP(Object model) {
		super();
		setModel(model);
	}

	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if (adapter == SnapToHelper.class) {
			List snapStrategies = new ArrayList();
			Boolean val = null;
			//[ID] rulers not used.
			/*Boolean val = (Boolean) getViewer()
			 .getProperty(RulerProvider.PROPERTY_RULER_VISIBILITY);
			 if (val != null && val.booleanValue()) {
			 snapStrategies.add(new SnapToGuides(this));
			 }*/
			val = (Boolean) getViewer().getProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED);
			if (val != null && val.booleanValue()) {
				snapStrategies.add(new SnapToGeometry(this));
			}
			val = (Boolean) getViewer().getProperty(SnapToGrid.PROPERTY_GRID_ENABLED);
			if (val != null && val.booleanValue()) {
				snapStrategies.add(new SnapToGrid(this));
			}
			if (snapStrategies.size() == 0) {
				return null;
			}
			if (snapStrategies.size() == 1) {
				return snapStrategies.get(0);
			}
			SnapToHelper ss[] = new SnapToHelper[snapStrategies.size()];
			for (int i = 0; i < snapStrategies.size(); i++) {
				ss[i] = (SnapToHelper) snapStrategies.get(i);
			}
			return new CompoundSnapToHelper(ss);
		}
		return super.getAdapter(adapter);
	}

	@Override
	public DobXmlModel getElement() {
		return (DobXmlModel) getModel();
	}

	protected IFigure createFigure() {
		ContentsFigure result = new ContentsFigure();
		result.setShowBounds(true);
		return result;
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new ContentsXYEditPolicy(
				ContainerRestrictionConstant.ALLOW_ALL));
		installEditPolicy("Snap Feedback", new SnapFeedbackPolicy()); //$NON-NLS-1$
	}

}
