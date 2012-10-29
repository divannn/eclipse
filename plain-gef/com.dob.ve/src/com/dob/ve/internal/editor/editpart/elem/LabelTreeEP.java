package com.dob.ve.internal.editor.editpart.elem;

import org.eclipse.ui.views.properties.IPropertySource;

import com.dob.ve.internal.editor.editpart.ElementTreeEP;
import com.dob.ve.internal.editor.property.elem.LabelPropertySource;

/**
 * @author idanilov
 *
 */
public class LabelTreeEP extends ElementTreeEP {

	@Override
	protected IPropertySource createPropertySource() {
		return new LabelPropertySource(getElement());
	}

}
