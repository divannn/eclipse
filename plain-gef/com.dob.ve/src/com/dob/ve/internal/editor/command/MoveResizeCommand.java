package com.dob.ve.internal.editor.command;

import org.eclipse.draw2d.geometry.Rectangle;

import com.dob.ve.abstractmodel.DobXmlElement;
import com.dob.ve.internal.model.FooModelUtils;

/**
 * @author idanilov
 *
 */
public class MoveResizeCommand extends BaseCommand {

	private DobXmlElement bean;
	private Rectangle constraint;
	private Rectangle oldConstraint;

	public MoveResizeCommand() {
		setLabel("Move/resize");
		setDebugLabel("DOB move/resize command");
	}

	public void setModel(Object m) {
		if (m instanceof DobXmlElement) {
			bean = (DobXmlElement) m;
			oldConstraint = FooModelUtils.getBounds(bean);
		}
	}

	public void setConstraint(Rectangle r) {
		constraint = r;
	}

	@Override
	public void execute() {
		if (canExecute()) {
			FooModelUtils.setBounds(bean, constraint);
		}
	}

	@Override
	public void undo() {
		FooModelUtils.setBounds(bean, oldConstraint);
	}

	@Override
	public boolean canExecute() {
		return bean != null && constraint != null;
	}

}
