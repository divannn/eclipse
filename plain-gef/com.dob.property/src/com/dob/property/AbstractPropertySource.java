package com.dob.property;

import org.eclipse.ui.views.properties.IPropertySource2;

/**
 * @author idanilov
 *
 */
public abstract class AbstractPropertySource
		implements IPropertySource2 {

	public Object getEditableValue() {
		return this;
	}

	public Object getPropertyValue(Object id) {
		return null;
	}

	public void setPropertyValue(Object id, Object value) {

	}

	public void resetPropertyValue(Object id) {

	}

	public boolean isPropertySet(Object id) {
		return false;
	}

	public boolean isPropertyResettable(Object id) {
		return false;
	}

}
