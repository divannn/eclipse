package com.dob.ve.internal.editor.editpart.elem;

import org.eclipse.ui.views.properties.IPropertySource;

import com.dob.ve.internal.editor.editpart.ContainerTreeEP;
import com.dob.ve.internal.editor.property.elem.HolderPropertySource;

/**
 * @author idanilov
 *
 */
public class HolderTreeEP extends ContainerTreeEP {

	@Override
	protected IPropertySource createPropertySource() {
		return new HolderPropertySource(getElement());
	}
	
}
