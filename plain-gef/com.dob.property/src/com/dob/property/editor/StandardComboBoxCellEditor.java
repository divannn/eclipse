package com.dob.property.editor;

import java.text.MessageFormat;

import org.eclipse.swt.widgets.Composite;


/**
 * @author idanilov
 *
 */
public class StandardComboBoxCellEditor extends ObjectComboBoxCellEditor {

	protected Object[] fItems;

	public StandardComboBoxCellEditor(Composite parent) {
		super(parent);
	}

	public StandardComboBoxCellEditor(Composite parent, String[] displayStrings, Object[] items) {
		super(parent, displayStrings);
		fItems = items;
	}

	public void setItems(String[] displayStrings, Object[] items) {
		fItems = items;
		setItems(displayStrings);
	}

	/**
	 * Return an error message if this is not a valid value.
	 * This can be overridden if a specific message should
	 * be returned instead of the default one.
	 * This default implementation will simply see if it
	 * is one of the items in fItems or if it is null. If
	 * it isn't it will return a generic invalid value message.
	 */
	protected String isCorrectObject(Object value) {
		if (value == null || doGetIndex(value) != NO_SELECTION)
			return null;

		return MessageFormat.format("The value is invalid. {0}", new Object[] { value });
	}

	/**
	 * Subclassed need to implement returning the object
	 * that the index represents. This is called when
	 * editing and a selection from the combobox is sent
	 * in and we need to send the object that it represents
	 * up to the validators. The index to convert will be
	 * passed in.
	 */
	protected Object doGetObject(int index) {
		return (fItems != null && index >= 0 && index < fItems.length) ? fItems[index] : null;
	}

	/**
	 * The object is being passed in, return
	 * the index to be used in the editor.
	 *
	 * It should return sNoSelection if the value can't be
	 * converted to a index. The errormsg will have
	 * already been set in this case.
	 */
	protected int doGetIndex(Object value) {
		if (fItems != null) {
			for (int i = 0; i < fItems.length; i++) {
				if (fItems[i] == null)
					if (value == null)
						return i;
					else
						;
				else if (fItems[i].equals(value))
					return i;
			}
		}

		return NO_SELECTION;
	}
}
