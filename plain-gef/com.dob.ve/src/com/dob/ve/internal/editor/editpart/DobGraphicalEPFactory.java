package com.dob.ve.internal.editor.editpart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.dob.ve.abstractmodel.DobXmlElement;
import com.dob.ve.internal.DobVEPlugin;
import com.dob.ve.internal.model.FooModelUtils;

/**
 * @author idanilov
 *
 */
public class DobGraphicalEPFactory
		implements EditPartFactory {

	private static final String GP_PACKAGE = "com.dob.ve.internal.editor.editpart.elem";
	private static final String GP_SUFFIX = "GraphicalEP";

	public EditPart createEditPart(EditPart context, Object model) {
		EditPart result = null;
		if (model instanceof DobXmlElement) {
			DobXmlElement dobElem = (DobXmlElement) model;
			String type = FooModelUtils.getType(dobElem);
			//System.err.println("1 " + type);
			String graphicalEPClassName = GP_PACKAGE + "." + type + GP_SUFFIX;
			try {
				Class graphicalEPClazz = Class.forName(graphicalEPClassName);
				//System.err.println("2 " + graphicalEPClazz);
				try {
					result = (EditPart) graphicalEPClazz.newInstance();
				} catch (Exception e) {
					DobVEPlugin.warning("Unable to instantiate graphical edit part class: "
							+ graphicalEPClassName);
				}
			} catch (ClassNotFoundException cnfe) {
				DobVEPlugin.warning("Unable to find graphical edit part class: "
						+ graphicalEPClassName);
			}
			if (result != null) {
				result.setModel(dobElem);
			}
		}
		return result;
	}

}
