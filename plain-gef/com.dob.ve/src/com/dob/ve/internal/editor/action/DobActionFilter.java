package com.dob.ve.internal.editor.action;

import org.eclipse.gef.EditPart;
import org.eclipse.ui.IActionFilter;

import com.dob.ve.abstractmodel.DobXmlElement;
import com.dob.ve.internal.model.IFooModelProperty;

/**
 * @author idanilov
 *
 */
public class DobActionFilter
		implements IActionFilter {

	public static final DobActionFilter INSTANCE = new DobActionFilter();

	public final static String TYPE_STRING = "TYPE", //$NON-NLS-1$
			PROPERTY_STRING = "PROPERTY"; //$NON-NLS-1$

	public boolean testAttribute(Object target, String name, String value) {
		if (!(target instanceof EditPart)) {
			return false;
		}
		EditPart ep = (EditPart) target;
		Object model = ep.getModel();
		if (!(model instanceof DobXmlElement)) {
			return false;
		}
		DobXmlElement elem = (DobXmlElement) model;
		if (name.equals(PROPERTY_STRING)) {
			Object v = elem.getOProperty(value);
			return v != null;
		}
		if (name.equals(TYPE_STRING)) {
			Object v = elem.getOProperty(IFooModelProperty.TYPE);
			return v != null && v.equals(value);
		}
		return false;
	}

}
