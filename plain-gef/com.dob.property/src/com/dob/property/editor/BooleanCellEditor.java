package com.dob.property.editor;

import org.eclipse.swt.widgets.Composite;


/**
 * @author idanilov
 *
 */
public class BooleanCellEditor extends StandardComboBoxCellEditor {

	protected static final int TRUE_INDEX = 0, FALSE_INDEX = 1;

	public BooleanCellEditor(Composite parent) {
		super(parent, new String[] { "true", "false" },
				new Object[] { Boolean.TRUE, Boolean.FALSE });
	}

	/**
	 * Return an error message if this is not a valid boolean
	 */
	protected String isCorrectObject(Object value) {
		if (value == null || value instanceof Boolean) {
			return null;
		}
		return "The value is not a boolean.";
	}

}
