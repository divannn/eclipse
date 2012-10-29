package com.dob.ve.internal.editor.command;

import com.dob.ve.abstractmodel.DobXmlElement;

/**
 * @author idanilov
 *
 */
public class SetPropertyCommand extends BaseCommand {

	private DobXmlElement model;
	private Object key;
	private Object value;
	private Object oldValue;

	public SetPropertyCommand() {
		this("Set Property");
	}

	public SetPropertyCommand(String name) {
		setLabel(name);
		setDebugLabel("DOB " + getLabel() + " command");
	}

	public void setModel(Object m) {
		if (m instanceof DobXmlElement) {
			model = (DobXmlElement) m;
		}
	}

	public void setKey(Object k) {
		if (k instanceof String) {
			key = k;
		}
	}

	public void setValue(Object v) {
		value = v;
		oldValue = model.getOProperty((String) key);
	}

	@Override
	public void execute() {
		if (canExecute()) {
			model.setOProperty((String) key, value);
		}
	}

	@Override
	public void undo() {
		if (canUndo()) {
			model.setOProperty((String) key, oldValue);
		}
	}

	@Override
	public boolean canExecute() {
		return model != null && key != null && !isSameValue();
	}

	public boolean canUndo() {
		return model != null && key != null;
	}

	private boolean isSameValue() {
		return (value == oldValue) || (value != null && value.equals(oldValue));
	}

}
