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
public class DobTreeEPFactory
		implements EditPartFactory {

	private static final String TP_PACKAGE = "com.dob.ve.internal.editor.editpart.elem";
	private static final String TP_SUFFIX = "TreeEP";

	public EditPart createEditPart(EditPart context, Object model) {
		EditPart result = null;
		if (model instanceof DobXmlElement) {
			DobXmlElement bean = (DobXmlElement) model;
			String type = FooModelUtils.getType(bean);

			String graphicalEPClassName = TP_PACKAGE + "." + type + TP_SUFFIX;
			try {
				Class graphicalEPClazz = Class.forName(graphicalEPClassName);
				try {
					result = (EditPart) graphicalEPClazz.newInstance();
				} catch (Exception e) {
					DobVEPlugin.error("Unable to instantiate tree edit part class: "
							+ graphicalEPClassName, e);
				}
			} catch (ClassNotFoundException cnfe) {
				DobVEPlugin.warning("Unable to find tree edit part class: " + graphicalEPClassName);
			}
			if (result != null) {
				result.setModel(bean);
			}
		}
		return result;
	}

}
