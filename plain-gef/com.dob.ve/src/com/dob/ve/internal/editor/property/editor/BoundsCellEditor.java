package com.dob.ve.internal.editor.property.editor;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.widgets.Composite;

import com.dob.property.editor.ObjectCellEditor;

/**
 * @author idanilov
 *
 */
public class BoundsCellEditor extends ObjectCellEditor {

	public static String ITEM_DELIM = ",";

	public BoundsCellEditor(Composite parent) {
		super(parent);
	}

	@Override
	protected Object doGetObject(String value) {
		return fromString(value);
	}

	@Override
	protected String doGetString(Object value) {
		return (value instanceof Rectangle) ? toString((Rectangle) value) : null;
	}

	@Override
	protected String isCorrectObject(Object value) {
		return (value instanceof Rectangle) ? null : "Value is not a Rectangle";
	}

	@Override
	protected String isCorrectString(String value) {
		try {
			fromString(value);
		} catch (NumberFormatException nfe) {
			return nfe.getMessage();
		}
		return null;
	}

	public static String toString(final Rectangle rect) {
		return rect.x + ITEM_DELIM + rect.y + ITEM_DELIM + rect.width + ITEM_DELIM + rect.height;
	}

	public static Rectangle fromString(final String str) throws NumberFormatException {
		Rectangle result = null;
		String errMsg = "Invalid format. Expected: x,y,w,h";
		String errMsg2 = "Invalid format. Width must be positive";
		String errMsg3 = "Invalid format. Height must be positive";

		//String trimmed = str.trim();
		String trimmed = str;
		if (trimmed == null || trimmed.length() == 0) {
			throw new NumberFormatException(errMsg);
		}
		String[] intStrings = str.split(ITEM_DELIM, -2);
		//System.err.println("ints : " + Arrays.toString(intStrings));
		if (intStrings == null || intStrings.length != 4) {
			throw new NumberFormatException(errMsg);
		}
		int[] values = new int[intStrings.length];
		int i = 0;
		for (String nextIntStr : intStrings) {
			//String nextTrimmed = nextInt.trim();
			String nextTrimmed = nextIntStr;
			if (nextTrimmed == null || nextTrimmed.length() == 0) {
				throw new NumberFormatException(errMsg);
			}
			try {
				int nextIntVal = Integer.parseInt(nextIntStr.trim());//trim, because Integer doesn't stripe spaces (Double removes spaces).
				values[i++] = nextIntVal;
			} catch (NumberFormatException nfe) {
				NumberFormatException newNfe = new NumberFormatException(errMsg);
				newNfe.initCause(nfe);
				throw newNfe;
			}
		}
		if (values[2] <= 0) {
			throw new NumberFormatException(errMsg2);
		}
		if (values[3] <= 0) {
			throw new NumberFormatException(errMsg3);
		}
		result = new Rectangle();
		result.x = values[0];
		result.y = values[1];
		result.width = values[2];
		result.height = values[3];
		return result;
	}

}
