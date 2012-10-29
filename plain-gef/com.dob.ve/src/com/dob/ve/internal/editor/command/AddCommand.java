package com.dob.ve.internal.editor.command;

import com.dob.ve.abstractmodel.DobXmlElement;

/**
 * @author idanilov
 *
 */
public class AddCommand extends BaseCommand {

	private DobXmlElement child;
	private DobXmlElement parent;
	private int ñhildIndex;

	public AddCommand() {
		setLabel("Add Child");
		setDebugLabel("DOB add (in tree) command");
	}

	public void setChild(Object ch) {
		if (ch instanceof DobXmlElement) {
			child = (DobXmlElement) ch;
		}
	}

	public void setParent(Object par) {
		if (par instanceof DobXmlElement) {
			parent = (DobXmlElement) par;
		}
	}

	public void setChildIndex(int ind) {
		ñhildIndex = ind;
	}

	@Override
	public void execute() {
		if (canExecute()) {
			parent.addChild(child, ñhildIndex);
		}
	}

	@Override
	public void undo() {
		if (canUndo()) {
			parent.removeChild(child);
		}
	}

	protected boolean can() {
		return parent != null && child != null;
	}

	@Override
	public boolean canExecute() {
		return can() && (ñhildIndex >= 0 && ñhildIndex <= parent.getChildren().size());
	}

	@Override
	public boolean canUndo() {
		return can();
	}

}
