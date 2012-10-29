package com.dob.property.editor;

import org.eclipse.jface.viewers.LabelProvider;

/**
 * @author idanilov
 *
 */
public class BooleanLabelProvider extends LabelProvider {

	public String getText(Object element) {
		if (element instanceof Boolean) {
			return ((Boolean) element).booleanValue() ? "true" : "false";
		}
		return super.getText(element);
	}

}
