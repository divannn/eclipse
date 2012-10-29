package com.dob.ve.abstractmodel;

import java.util.EventObject;

/**
 * @author idanilov
 *
 */
public class DobXmlModelEvent extends EventObject {

	private DobXmlElement child;
	private String propertyName;
	/**
	 * Means that notification came from model (not from specific parent element).
	 */
	private boolean modelChange;

	public DobXmlModelEvent(final DobXmlElement source, final DobXmlElement c) {
		this(source, c, false);
	}

	public DobXmlModelEvent(final DobXmlElement source, final DobXmlElement c, final boolean model) {
		super(source);
		child = c;
		modelChange = model;
	}

	public boolean isModelChange() {
		return modelChange;
	}

	public DobXmlModelEvent(final DobXmlElement source, final String pName) {
		super(source);
		propertyName = pName;
	}

	@Override
	public DobXmlElement getSource() {
		return (DobXmlElement) super.getSource();
	}

	public DobXmlElement getChild() {
		return child;
	}

	public String getPropertyName() {
		return propertyName;
	}

}
