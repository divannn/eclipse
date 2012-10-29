package com.dob.property.editor;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * @author idanilov
 *
 */
public class StringCellEditor extends TextCellEditor {

	public StringCellEditor(Composite parent) {
		super(parent);
	}

	@Override
	protected void doSetValue(Object value) {
		//check for null value. Bypass assert in super.
		if (value == null) {
			value = "";
		}
		super.doSetValue(value);
	}

}
