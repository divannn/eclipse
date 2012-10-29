package com.dob.ve.internal.editor.editpart.elem;

import org.eclipse.ui.views.properties.IPropertySource;

import com.dob.ve.internal.editor.editpart.ElementTreeEP;
import com.dob.ve.internal.editor.property.elem.RectanglePropertySource;

/**
 * @author idanilov
 *
 */
public class RectangleTreeEP extends ElementTreeEP {

	@Override
	protected IPropertySource createPropertySource() {
		return new RectanglePropertySource(getElement());
	}

}
