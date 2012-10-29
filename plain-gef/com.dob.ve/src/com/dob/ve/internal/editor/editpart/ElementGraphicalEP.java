package com.dob.ve.internal.editor.editpart;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.dob.ve.abstractmodel.DobXmlModelEvent;
import com.dob.ve.internal.editor.action.DobActionFilter;
import com.dob.ve.internal.editor.directedit.GraphicalDirectEditManager;
import com.dob.ve.internal.editor.factory.LabelProviderFactory;
import com.dob.ve.internal.editor.figure.ElementFigure;
import com.dob.ve.internal.editor.gef.DirectEditCellEditorLocator;
import com.dob.ve.internal.editor.policy.DobComponentEditPolicy;
import com.dob.ve.internal.editor.policy.DobCopyEditPolicy;
import com.dob.ve.internal.editor.policy.DobDirectEditEditPolicy;
import com.dob.ve.internal.editor.property.BasePropertySource;
import com.dob.ve.internal.model.IFooModelProperty;

/**
 * @author idanilov
 * 
 */
public abstract class ElementGraphicalEP extends BaseGraphicalEP
		implements IDobElementContextMenuContributor {

	protected Label tooltipFigure;

	public ElementGraphicalEP() {
		super();
		tooltipFigure = new Label();
	}

	@Override
	public Object getAdapter(Class key) {
		if (key == IActionFilter.class) {
			return getActionFilter();
		}
		return super.getAdapter(key);
	}

	protected IActionFilter getActionFilter() {
		return DobActionFilter.INSTANCE;
	}

	@Override
	public ElementFigure getFigure() {
		return (ElementFigure) super.getFigure();
	}

	@Override
	protected void createEditPolicies() {
		//allow delete and orphan.
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DobComponentEditPolicy());
		//allow resize/move.
		installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE, new ResizableEditPolicy());
		//allow direct edit.
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new DobDirectEditEditPolicy());
		//allow copy.
		installEditPolicy(DobCopyEditPolicy.COPY_ROLE, new DobCopyEditPolicy());
	}

	@Override
	public void activate() {
		super.activate();
		getFigure().setToolTip(tooltipFigure);
	}

	@Override
	public void deactivate() {
		getFigure().setToolTip(null);
		super.deactivate();
	}

	@Override
	protected void refreshVisuals() {
		refreshTooltip();
		refreshBounds();
	}

	@Override
	protected void refreshVisuals(DobXmlModelEvent e) {
		String propName = e.getPropertyName();
		if (IFooModelProperty.ID.equals(propName)) {
			refreshTooltip();
		}
		if (IFooModelProperty.BOUNDS.equals(propName)) {
			refreshBounds();
		}
	}

	public void refreshTooltip() {
		tooltipFigure.setText(getTooltipString());
	}

	protected void refreshBounds() {
		Rectangle b = (Rectangle) getElement().getOProperty(IFooModelProperty.BOUNDS);
		//System.err.println("b: " + b);
		ElementFigure fig = getFigure();
		fig.setD2Constraint(b);
	}

	protected String getTooltipString() {
		return LabelProviderFactory.getInstance().getLabelProvider(getModel()).getText(getModel());
	}

	public void performRequest(Request request) {
		if (RequestConstants.REQ_DIRECT_EDIT.equals(request.getType())) {
			performDirectEdit();
		} else {
			super.performRequest(request);
		}
	}

	protected void performDirectEdit() {
		IPropertyDescriptor pd = BasePropertySource.getDescriptorForID(propertySource,
				IFooModelProperty.ID);
		if (pd != null) {
			GraphicalDirectEditManager directEditMan = new GraphicalDirectEditManager(this,
					TextCellEditor.class, new DirectEditCellEditorLocator(getFigure()),
					IFooModelProperty.ID);
			directEditMan.show();
		}
	}

}
