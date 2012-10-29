package com.dob.ve.internal.editor.command;

import org.eclipse.draw2d.geometry.Rectangle;

import com.dob.ve.abstractmodel.DobXmlElement;
import com.dob.ve.internal.model.FooModelUtils;

/**
 * @author idanilov
 *
 */
public class CreateCommand extends BaseCommand {

	private DobXmlElement parent;
	private DobXmlElement child;
	private Rectangle constraint;
	private int childIndex;

	public CreateCommand() {
		setLabel("Create");
		setDebugLabel("DOB create command");
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

	public void setConstraint(Rectangle r) {
		constraint = r;
	}

	public void setChildIndex(int ind) {
		childIndex = ind;
	}

	@Override
	public void execute() {
		if (canExecute()) {
			FooModelUtils.setBounds(child, constraint);
			parent.addChild(child, childIndex);
		}
	}

	@Override
	public void undo() {
		if (canUndo()) {
			parent.removeChild(child);
		}
	}

	protected boolean can() {
		return parent != null && child != null && constraint != null;
	}

	@Override
	public boolean canExecute() {
		return can() && (childIndex >= 0 && childIndex <= parent.getChildren().size());
	}

	@Override
	public boolean canUndo() {
		return can();
	}

}
