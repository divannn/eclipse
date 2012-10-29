package com.dob.property;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.dob.property.editor.StringCellEditor;


/**
 * @author idanilov
 *
 */
public class StringPropertyDescriptor extends TextPropertyDescriptor {

	public StringPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
	}
	
	public CellEditor createPropertyEditor(Composite parent) {
        CellEditor editor = new StringCellEditor(parent);
        if (getValidator() != null) {
			editor.setValidator(getValidator());
		}
        return editor;
    }

}
