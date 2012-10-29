package com.dob.property;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.dob.property.editor.NumberCellEditor;
import com.dob.property.editor.NumberLabelProvider;

/**
 * @author idanilov
 *
 */
public class NumberPropertyDescriptor extends PropertyDescriptor {

	protected static final NumberLabelProvider LABEL_PROVIDER = new NumberLabelProvider(); // Need only one, they are not descriptor specific.

	protected int type;

	public NumberPropertyDescriptor(Object propertyID, String propertyDisplayname) {
		super(propertyID, propertyDisplayname);
		setLabelProvider(LABEL_PROVIDER); // The default provider, this can be overridden by just setting in a different value after creating descriptor.
	}

	public void setEditorType(int editorType) {
		this.type = editorType;
	}

	public CellEditor createPropertyEditor(Composite parent) {
		NumberCellEditor result = new NumberCellEditor(parent);
		result.setType(type);
		ICellEditorValidator v = getValidator();
		if (v != null) {
			result.setValidator(v);
		}
		return result;
	}

}
