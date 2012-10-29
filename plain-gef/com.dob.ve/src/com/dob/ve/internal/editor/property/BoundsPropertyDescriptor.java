package com.dob.ve.internal.editor.property;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.dob.ve.internal.editor.property.editor.BoundsCellEditor;
import com.dob.ve.internal.editor.property.editor.BoundsLabelProvider;

/**
 * @author idanilov
 *
 */
public class BoundsPropertyDescriptor extends PropertyDescriptor {

	protected static final BoundsLabelProvider LP = new BoundsLabelProvider();

	public BoundsPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
		setLabelProvider(LP);
	}

	public CellEditor createPropertyEditor(Composite parent) {
		CellEditor editor = new BoundsCellEditor(parent);
		ICellEditorValidator v = getValidator();
		if (v != null) {
			editor.setValidator(v);
		}
		return editor;
	}
	
}
