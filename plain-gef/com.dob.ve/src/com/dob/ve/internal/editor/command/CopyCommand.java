package com.dob.ve.internal.editor.command;

import org.eclipse.gef.ui.actions.Clipboard;

import com.dob.ve.abstractmodel.Copier;
import com.dob.ve.abstractmodel.DobXmlElement;

/**
 * @author idanilov
 *
 */
public class CopyCommand extends BaseCommand {

	private DobXmlElement source;

	public CopyCommand() {
		setLabel("Copy");
		setDebugLabel("DOB copy command");
	}

	public void setSource(Object s) {
		if (s instanceof DobXmlElement) {
			source = (DobXmlElement) s;
		}
	}

	@Override
	public void execute() {
		if (canExecute()) {
			DobXmlElement copy = new Copier(source).copy();
			Clipboard.getDefault().setContents(copy);
		}
	}

	@Override
	public boolean canExecute() {
		return source != null;
	}

	@Override
	public boolean canUndo() {
		return false;
	}

}
