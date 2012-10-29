package com.dob.ve.internal.editor.command;

import com.dob.ve.abstractmodel.DobXmlElement;

/**
 * Reorders single child within parent. Used in Outline tree.
 * @author idanilov
 *
 */
public class ReorderCommand extends BaseCommand {

	private DobXmlElement child;
	private DobXmlElement parent;
	private int oldChildIndex;
	private int newChildIndex;

	public ReorderCommand() {
		setLabel("Reorder Child");
		setDebugLabel("DOB reorder in container (in tree) command");
	}

	public void setParent(Object par) {
		if (par instanceof DobXmlElement) {
			parent = (DobXmlElement) par;
		}
	}

	public void setOldChildIndex(int ind) {
		oldChildIndex = ind;
	}

	public void setNewChildIndex(int ind) {
		newChildIndex = ind;
	}

	public void setChild(Object ch) {
		if (ch instanceof DobXmlElement) {
			child = (DobXmlElement) ch;
		}
	}

	@Override
	public void execute() {
		if (canExecute()) {
			parent.removeChild(child);
			parent.addChild(child, newChildIndex);
		}
	}

	@Override
	public void undo() {
		if (canUndo()) {
			parent.removeChild(child);
			parent.addChild(child, oldChildIndex);
		}
	}

	protected boolean can() {
		return parent != null && parent != null && child != null;
	}

	protected boolean checkIndex() {
		return oldChildIndex != newChildIndex;
	}

	@Override
	public boolean canExecute() {
		return can() && checkIndex()
				&& (newChildIndex >= 0 && newChildIndex < parent.getChildren().size());
	}

	@Override
	public boolean canUndo() {
		return can() && checkIndex()
				&& (oldChildIndex >= 0 && oldChildIndex < parent.getChildren().size());
	}

}
