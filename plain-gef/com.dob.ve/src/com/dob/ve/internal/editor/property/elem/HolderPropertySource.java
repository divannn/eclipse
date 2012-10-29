package com.dob.ve.internal.editor.property.elem;

import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.dob.property.ComboBoxData;
import com.dob.property.NumberPropertyDescriptor;
import com.dob.property.ObjectComboboxPropertyDescriptor;
import com.dob.property.editor.NumberCellEditor;
import com.dob.ve.abstractmodel.DobXmlElement;
import com.dob.ve.internal.editor.property.ElementPropertySource;
import com.dob.ve.internal.model.IFooModelProperty;
import com.dob.ve.internal.model.IFooPropertyDefaults;

/**
 * @author idanilov
 *
 */
public class HolderPropertySource extends ElementPropertySource {

	public static final String EMPTY_LABEL = "";
	public static final String EMPTY_VALUE = IFooPropertyDefaults.STR_PROP;

	public HolderPropertySource(DobXmlElement e) {
		super(e);
	}

	@Override
	protected List<IPropertyDescriptor> createPropertyDescriptors() {
		List<IPropertyDescriptor> result = super.createPropertyDescriptors();

		//int property - just for demo.
		NumberPropertyDescriptor intPD = new NumberPropertyDescriptor(IFooModelProperty.INT_PROP,
				"Int property");
		intPD.setEditorType(NumberCellEditor.INTEGER);
		intPD.setCategory(MODEL_CAT);
		result.add(intPD);

		//double property - just for demo.
		NumberPropertyDescriptor doublePD = new NumberPropertyDescriptor(
				IFooModelProperty.DOUBLE_PROP, "Double property");
		doublePD.setEditorType(NumberCellEditor.DOUBLE);
		doublePD.setCategory(MODEL_CAT);
		result.add(doublePD);

		//combobox editor property - just for demo.
		ComboBoxData comboData = createComboItems();
		ObjectComboboxPropertyDescriptor strPD = new ObjectComboboxPropertyDescriptor(
				IFooModelProperty.STR_PROP, "String property", comboData.getLabels(), comboData
						.getValues());
		strPD.setCategory(MODEL_CAT);
		result.add(strPD);
		return result;
	}

	@Override
	public Object getPropertyValue(Object propId) {
		if (IFooModelProperty.INT_PROP.equals(propId)
				|| IFooModelProperty.DOUBLE_PROP.equals(propId)
				|| IFooModelProperty.STR_PROP.equals(propId)) {
			return elem.getOProperty((String) propId);
		}
		return super.getPropertyValue(propId);
	}

	private static ComboBoxData createComboItems() {
		ComboBoxData result = new ComboBoxData();
		//empty item.
		result.labels.add(EMPTY_LABEL);
		result.values.add(EMPTY_VALUE);
		result.labels.add("label1");
		result.values.add("value1");
		result.labels.add("label2");
		result.values.add("value2");
		return result;
	}

	/*@Override
	 public void setPropertyValue(Object propId, Object value) {
	 if (IFooModelProperty.INT_PROP.equals(propId)
	 || IFooModelProperty.DOUBLE_PROP.equals(propId)) {
	 elem.setOProperty((String) propId, value);
	 } else {
	 super.setPropertyValue(propId, value);
	 }
	 }*/

}
