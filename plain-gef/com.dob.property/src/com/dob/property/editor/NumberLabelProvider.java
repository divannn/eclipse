package com.dob.property.editor;

import java.text.NumberFormat;

import org.eclipse.jface.viewers.LabelProvider;

/**
 * @author idanilov
 *
 */
public class NumberLabelProvider extends LabelProvider {

	NumberFormat fFormatter = NumberFormat.getInstance();

	public String getText(Object element) {
		if (element instanceof Number) {
			return fFormatter.format(element);
		}
		return super.getText(element);
	}

}
