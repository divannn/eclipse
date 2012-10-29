package com.dob.ve.abstractmodel;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author idanilov
 *
 */
public class Copier {

	private DobXmlElement source;

	public Copier(DobXmlElement s) {
		source = s;
	}

	public DobXmlElement copy() {
		DobXmlElement result = new DobXmlElement();

		Map<String, Object> properties = source.getProperties();
		Set<String> keys = properties.keySet();
		for (String nextKey : keys) {
			Object nextValue = properties.get(nextKey);
			result.setOProperty(nextKey, nextValue);
			//System.err.println(">> " + nextKey + "   " + nextValue);
		}

		List<DobXmlElement> children = source.getChildren();
		for (DobXmlElement nextChild : children) {
			DobXmlElement nextChildCopy = new Copier(nextChild).copy();
			result.addChild(nextChildCopy);
		}
		return result;
	}

}
