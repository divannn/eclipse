package com.dob.property;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.dob.property.editor.BooleanCellEditor;
import com.dob.property.editor.BooleanLabelProvider;

/**
 * @author idanilov
 *
 */
public class BooleanPropertyDescriptor extends PropertyDescriptor {

	protected static final BooleanLabelProvider LABEL_PROVIDER = new BooleanLabelProvider(); // Need only one, they are not descriptor specific.

	public BooleanPropertyDescriptor(Object propertyID, String propertyDisplayname) {
		super(propertyID, propertyDisplayname);
		setLabelProvider(LABEL_PROVIDER); // The default provider, this can be overridden by just setting in a different value after creating descriptor.		
	}

	public CellEditor createPropertyEditor(Composite parent) {
		CellEditor editor = new BooleanCellEditor(parent);
		ICellEditorValidator v = getValidator();
		if (v != null) {
			editor.setValidator(v);
		}
		return editor;
	}

}
