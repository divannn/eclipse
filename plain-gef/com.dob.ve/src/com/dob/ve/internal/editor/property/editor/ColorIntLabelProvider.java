package com.dob.ve.internal.editor.property.editor;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.RGB;

/**
 * @author idanilov
 *
 */
public class ColorIntLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof Integer) {
			Integer i = (Integer) element;
			return convert2String(ColorIntEditor.int2rgb(i.intValue()));
		}
		return super.getText(element);
	}

	public static String convert2String(final RGB color) {
		return color.red + "," + color.green + "," + color.blue;
	}

}
