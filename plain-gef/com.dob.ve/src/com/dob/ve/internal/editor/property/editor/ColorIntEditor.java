package com.dob.ve.internal.editor.property.editor;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Allows editing "int" as RGB triplex.
 * @author idanilov
 *
 */

public class ColorIntEditor extends DialogCellEditor {

	public ColorIntEditor(Composite aComposite) {
		super(aComposite);
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		Object result = null;
		ColorDialog dialog = new ColorDialog(cellEditorWindow.getShell());
		Object value = getValue();
		int cValue = 0;
		if (value != null) {
			cValue = Integer.parseInt(value.toString());
		}
		dialog.setRGB(int2rgb(cValue));
		RGB selected = dialog.open();
		//RGB selected = dialog.getRGB();
		//System.err.println(">>> " + selected);
		if (selected != null) {
			result = rgb2int(selected);
		}
		return result;
	}

	public static RGB int2rgb(final int i) {
		long l = Long.parseLong(Integer.toHexString(i), 16);
		return new RGB((int) ((l & 0xFF0000) >> 16), (int) ((l & 0xFF00) >> 8), (int) (l & 0xFF));
	}

	public static int rgb2int(final RGB rgb) {
		return ((rgb.red << 16) & 0xFF0000) | (rgb.green << 8 & 0xFF00) | rgb.blue & 0xFF;
	}

	protected void updateContents(Object value) {
		Object val = value;
		if (value != null) {
			Integer i = (Integer) value;
			val = ColorIntLabelProvider.convert2String(int2rgb(i.intValue()));
		}
		super.updateContents(val);
	}

}
