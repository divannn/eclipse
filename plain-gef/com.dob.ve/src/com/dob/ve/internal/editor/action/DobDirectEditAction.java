package com.dob.ve.internal.editor.action;

import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.ui.IEditorPart;

/**
 * @author idanilov
 *
 */
public class DobDirectEditAction extends DirectEditAction {

	public DobDirectEditAction(IEditorPart editor) {
		super(editor);
	}

	@Override
	protected void init() {
		super.init();
		String label = "Set ID";
		setText(label);
		setToolTipText(label);
	}

}
