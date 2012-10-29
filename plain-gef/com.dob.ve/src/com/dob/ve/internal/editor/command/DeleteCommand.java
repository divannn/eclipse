package com.dob.ve.internal.editor.command;

import com.dob.ve.abstractmodel.DobXmlElement;

/**
 * @author idanilov
 *
 */
public class DeleteCommand extends BaseCommand {

	private DobXmlElement parent;
	private DobXmlElement child;
	private int childIndex;

	public DeleteCommand() {
		setLabel("Delete");
		setDebugLabel("DOB delete command");
	}

	public void setParent(Object par) {
		if (par instanceof DobXmlElement) {
			parent = (DobXmlElement) par;
		}
	}

	public void setChild(Object ch) {
		if (ch instanceof DobXmlElement) {
			child = (DobXmlElement) ch;
		}
	}

	/*	public void setChildIndex(int childIndex) {
	 this.childIndex = childIndex;
	 }*/

	@Override
	public void execute() {
		//trick - get index at the moment of execution (and don't check childIndex in canXXX()).
		//this allows avoid calculatation of undo deletion order if several children are deleted from one parent.
		childIndex = parent.childIndex(child);
		if (canExecute()) {
			//System.err.println("del redo : " + FooModelUtils.getId(child) + " " + childIndex);
			parent.removeChild(child);
		}
	}

	@Override
	public boolean canExecute() {
		return can();
	}

	@Override
	public void undo() {
		if (canUndo()) {
			//System.err.println("del undo : " + FooModelUtils.getId(child) + " " + childIndex);
			parent.addChild(child, childIndex);
		}
	}

	@Override
	public boolean canUndo() {
		return can();
	}

	protected boolean can() {
		return parent != null && child != null;
	}
}
