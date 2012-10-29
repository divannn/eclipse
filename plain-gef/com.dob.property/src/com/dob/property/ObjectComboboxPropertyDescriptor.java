package com.dob.property;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;

import com.dob.property.editor.StandardComboBoxCellEditor;

/**
 * @author idanilov
 *
 */
public class ObjectComboboxPropertyDescriptor extends ComboBoxPropertyDescriptor {

	private String[] labels;
	private Object[] values;

	public ObjectComboboxPropertyDescriptor(Object id, String displayName, String[] labelsArray,
			Object[] valuesArray) {
		super(id, displayName, labelsArray);
		labels = labelsArray;
		values = valuesArray;
		setLabelProvider(new LabelProvider());
	}

	public void setLabels(String[] labels) {
		this.labels = labels;
	}

	public void setValues(Object[] values) {
		this.values = values;
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		return new StandardComboBoxCellEditor(parent, labels, values);
	}
	
}