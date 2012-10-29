package com.dob.ve.internal.editor.property;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.ColorPropertyDescriptor;

import com.dob.ve.internal.editor.property.editor.ColorIntEditor;
import com.dob.ve.internal.editor.property.editor.ColorIntLabelProvider;

/**
 * @author idanilov
 *
 */
public class ColorIntPropertyDescriptor extends ColorPropertyDescriptor {

	private static final ILabelProvider LP = new ColorIntLabelProvider();

	public ColorIntPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
		setLabelProvider(LP);
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		return new ColorIntEditor(parent);
	}

}
