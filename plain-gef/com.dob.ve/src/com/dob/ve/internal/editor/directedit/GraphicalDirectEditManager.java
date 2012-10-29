package com.dob.ve.internal.editor.directedit;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;

import com.dob.ve.abstractmodel.DobXmlElement;
import com.dob.ve.internal.editor.editpart.BaseGraphicalEP;

/**
 * @author idanilov
 *
 */
public class GraphicalDirectEditManager extends DirectEditManager {

	public GraphicalDirectEditManager(GraphicalEditPart source, Class editorType,
			CellEditorLocator locator, Object feature) {
		super(source, editorType, locator, feature);
	}

	@Override
	protected void initCellEditor() {
		BaseGraphicalEP ep = (BaseGraphicalEP) getEditPart();
		DobXmlElement model = ep.getElement();
		Object value = model.getOProperty((String) getDirectEditFeature());
		if (value == null) {
			value = "";//text cell editor doesn't accept nulls.
		}
		CellEditor cellEditor = getCellEditor();
		cellEditor.setValue(value);
	}

}
