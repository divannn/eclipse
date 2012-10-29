package com.dob.property;

import java.util.ArrayList;
import java.util.List;

/**
 * @author idanilov
 *
 */
public final class ComboBoxData {

	public List<String> labels = new ArrayList<String>();
	public List<Object> values = new ArrayList<Object>();

	public String[] getLabels() {
		return labels.toArray(new String[labels.size()]);
	}

	public Object[] getValues() {
		return values.toArray();
	}

}
