package com.dob.ve.internal.editor.directedit;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.dob.ve.internal.editor.command.SetPropertyCommand;
import com.dob.ve.internal.editor.gef.AbstractTreeDirectEditManager;

/**
 * @author idanilov
 *
 */
public class TreeDirectEditManager extends AbstractTreeDirectEditManager {

	public TreeDirectEditManager(EditPartViewer v) {
		super(v);
	}

	protected Command getDirectEditCommand(Object newValue, EditPart ep,
			IPropertyDescriptor property) {
		//IPropertySource ps = (IPropertySource) ep.getAdapter(IPropertySource.class);
		SetPropertyCommand result = new SetPropertyCommand();
		result.setModel(ep.getModel());
		result.setKey(property.getId());
		result.setValue(newValue);
		return result;
	}

}
