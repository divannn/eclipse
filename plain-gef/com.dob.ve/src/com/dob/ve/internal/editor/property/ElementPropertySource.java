package com.dob.ve.internal.editor.property;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.dob.property.BooleanPropertyDescriptor;
import com.dob.property.StringPropertyDescriptor;
import com.dob.property.editor.BooleanLabelProvider;
import com.dob.ve.abstractmodel.DobXmlElement;
import com.dob.ve.internal.model.FooModelUtils;
import com.dob.ve.internal.model.IFooModelProperty;

/**
 * @author idanilov
 *
 */
public abstract class ElementPropertySource extends BasePropertySource {

	public static final String MODEL_CAT = "Model";
	public static final String NON_MODEL_CAT = "Non model";

	public ElementPropertySource(DobXmlElement e) {
		super(e);
	}

	@Override
	protected List<IPropertyDescriptor> createPropertyDescriptors() {
		List<IPropertyDescriptor> result = new ArrayList<IPropertyDescriptor>();
		PropertyDescriptor idPD = new StringPropertyDescriptor(IFooModelProperty.ID, "Id");
		idPD.setCategory(MODEL_CAT);
		result.add(idPD);

		PropertyDescriptor boundsPD = new BoundsPropertyDescriptor(IFooModelProperty.BOUNDS,
				"Bounds");
		boundsPD.setCategory(MODEL_CAT);
		result.add(boundsPD);

		PropertyDescriptor dnPD = new PropertyDescriptor(IFooModelProperty.DISPLAY_NAME,
				"Display name (read only)");
		dnPD.setAlwaysIncompatible(true);
		dnPD.setCategory(NON_MODEL_CAT);
		result.add(dnPD);

		PropertyDescriptor visiblePD = new BooleanPropertyDescriptor(IFooModelProperty.VISIBLE,
				"Show in editor");
		visiblePD.setCategory(NON_MODEL_CAT);
		//show image to indicate "non-model" property.
		visiblePD.setLabelProvider(new BooleanLabelProvider() {

			@Override
			public Image getImage(Object element) {
				return PlatformUI.getWorkbench().getSharedImages().getImage(
						ISharedImages.IMG_OBJ_ELEMENT);
			}
		});
		result.add(visiblePD);

		return result;
	}

	@Override
	public Object getPropertyValue(Object propId) {
		if (IFooModelProperty.ID.equals(propId)) {
			return FooModelUtils.getId(elem);
		}
		if (IFooModelProperty.DISPLAY_NAME.equals(propId)) {
			return elem.getOProperty(IFooModelProperty.DISPLAY_NAME);
		}
		if (IFooModelProperty.BOUNDS.equals(propId)) {
			//return FooModelUtils.getBounds(elem);
			return new BoundsPropertySource(elem);
		}
		if (IFooModelProperty.VISIBLE.equals(propId)) {
			return FooModelUtils.isVisible(elem);
		}
		return super.getPropertyValue(propId);
	}

	/*@Override
	 public void setPropertyValue(Object propId, Object value) {
	 if (IFooModelProperty.ID.equals(propId)) {
	 FooModelUtils.setId(elem, value.toString().trim());
	 } else if (IFooModelProperty.BOUNDS.equals(propId)) {
	 FooModelUtils.setBounds(elem, (Rectangle) value);
	 } else if (IFooModelProperty.VISIBLE.equals(propId)) {
	 FooModelUtils.setVisible(elem, (Boolean) value);
	 } else {
	 super.setPropertyValue(propId, value);
	 }
	 }*/

}
