package com.dob.ve.internal.editor.editpart.elem;

import org.eclipse.ui.views.properties.IPropertySource;

import com.dob.ve.internal.editor.editpart.ContainerGraphicalEP;
import com.dob.ve.internal.editor.property.elem.HolderPropertySource;

/**
 * @author idanilov
 *
 */
public class HolderGraphicalEP extends ContainerGraphicalEP {

	@Override
	protected IPropertySource createPropertySource() {
		return new HolderPropertySource(getElement());
	}

}
