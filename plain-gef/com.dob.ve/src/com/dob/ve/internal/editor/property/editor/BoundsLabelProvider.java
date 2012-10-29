package com.dob.ve.internal.editor.property.editor;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.viewers.LabelProvider;

/**
 * @author idanilov
 *
 */
public class BoundsLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof Rectangle)
			return BoundsCellEditor.toString((Rectangle) element);

		return super.getText(element);
	}
	
}
