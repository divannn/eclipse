package com.dob.ve.internal.editor.factory;

import org.eclipse.gef.requests.CreationFactory;

import com.dob.ve.abstractmodel.DobXmlElement;

/**
 * @author idanilov
 *
 */
public class PasteModelFactory
		implements CreationFactory {

	private DobXmlElement type;

	public PasteModelFactory(DobXmlElement t) {
		type = t;
	}

	public Object getNewObject() {
		return type;
	}

	public Object getObjectType() {
		return type;
	}

}
