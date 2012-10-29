package com.dob.ve.internal.editor.editpart;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.dob.util.AbstractLabelDecorator;
import com.dob.util.OverlayImageDescriptor;
import com.dob.ve.abstractmodel.DobXmlElement;
import com.dob.ve.internal.DobVEPlugin;
import com.dob.ve.internal.IImageConstant;
import com.dob.ve.internal.editor.EditorImages;
import com.dob.ve.internal.editor.action.DobActionFilter;
import com.dob.ve.internal.editor.directedit.TreeDirectEditManager;
import com.dob.ve.internal.editor.factory.LabelProviderFactory;
import com.dob.ve.internal.editor.policy.DobComponentEditPolicy;
import com.dob.ve.internal.editor.policy.DobCopyEditPolicy;
import com.dob.ve.internal.editor.policy.DobDirectEditEditPolicy;
import com.dob.ve.internal.editor.policy.DobTreeMoveEditPolicy;
import com.dob.ve.internal.editor.property.BasePropertySource;
import com.dob.ve.internal.model.FooModelUtils;
import com.dob.ve.internal.model.IFooModelProperty;

/**
 * Base class for leaf bean. I.e. that cannot contain other beans.
 * @author idanilov
 *
 */
public abstract class ElementTreeEP extends BaseTreeEP
		implements IDobElementContextMenuContributor {

	protected ILabelDecorator labelDecorator;
	protected ILabelProvider labelProvider;
	protected TreeDirectEditManager directEditManager;

	public ElementTreeEP() {
		super();
		labelDecorator = new VisibilityLabelDecorator();
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
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DobComponentEditPolicy());
		installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE, new DobTreeMoveEditPolicy());
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new DobDirectEditEditPolicy());
		installEditPolicy(DobCopyEditPolicy.COPY_ROLE, new DobCopyEditPolicy());
		directEditManager = new TreeDirectEditManager(getViewer());
	}

	public void performRequest(Request request) {
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT) {
			performDirectEdit();
		} else {
			super.performRequest(request);
		}
	}

	protected void performDirectEdit() {
		IPropertyDescriptor pd = BasePropertySource.getDescriptorForID(propertySource,
				IFooModelProperty.ID);
		if (directEditManager != null && pd != null) {
			directEditManager.performDirectEdit(this, pd);
		}
	}

	protected ILabelProvider getLabelProvider() {
		if (labelProvider == null) {
			ILabelProvider mainLabelProvider = LabelProviderFactory.getInstance().getLabelProvider(
					getModel());
			labelProvider = new DecoratingLabelProvider(mainLabelProvider, labelDecorator);

		}
		return labelProvider;
	}

	@Override
	protected Image getImage() {
		return getLabelProvider().getImage(getModel());
	}

	@Override
	protected String getText() {
		return getLabelProvider().getText(getModel());
	}

	private static class VisibilityLabelDecorator extends AbstractLabelDecorator {

		public Image decorateImage(Image image, Object element) {
			DobXmlElement e = (DobXmlElement) element;
			if (!FooModelUtils.isVisible(e).booleanValue()) {
				OverlayImageDescriptor overlayDesc = new OverlayImageDescriptor(image, DobVEPlugin
						.getDescriptor(IImageConstant.VISIBLE_OVR),
						OverlayImageDescriptor.LOCATION.BOTTOM_LEFT);
				return EditorImages.getImage(overlayDesc);
			}
			return null;
		}

		public String decorateText(String text, Object element) {
			return null;
		}

	}

}
