package com.dob.ve.internal.editor.property.elem;

import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.dob.ve.abstractmodel.DobXmlElement;
import com.dob.ve.internal.editor.property.ColorIntPropertyDescriptor;
import com.dob.ve.internal.editor.property.ElementPropertySource;
import com.dob.ve.internal.model.IFooModelProperty;

/**
 * @author idanilov
 *
 */
public class RectanglePropertySource extends ElementPropertySource {

	public RectanglePropertySource(DobXmlElement e) {
		super(e);
	}

	@Override
	protected List<IPropertyDescriptor> createPropertyDescriptors() {
		List<IPropertyDescriptor> result = super.createPropertyDescriptors();

		PropertyDescriptor colorPD = new ColorIntPropertyDescriptor(IFooModelProperty.BG_COLOR,
				"BG color");
		colorPD.setCategory(MODEL_CAT);
		result.add(colorPD);
		return result;
	}

	@Override
	public Object getPropertyValue(Object propId) {
		if (IFooModelProperty.BG_COLOR.equals(propId)) {
			return elem.getOProperty((String) propId);
		}
		return super.getPropertyValue(propId);
	}

	/*@Override
	 public void setPropertyValue(Object propId, Object value) {
	 if (IFooModelProperty.BG_COLOR.equals(propId)) {
	 elem.setOProperty((String) propId, value);
	 } else {
	 super.setPropertyValue(propId, value);
	 }
	 }*/

}
