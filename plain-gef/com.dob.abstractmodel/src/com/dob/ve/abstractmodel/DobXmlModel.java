package com.dob.ve.abstractmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Intermediate structure that used during parsing of source VE XML file.
 * Caches information extracted from XML.
 * Can be used for multi traveresing during building VE model. 
 * @author idanilov
 *
 */
public class DobXmlModel extends DobXmlElement {

	void fireModelRemove(DobXmlElement child) {
		DobXmlModelEvent ev = new DobXmlModelEvent(this, child, true);
		List<IDobModelChangeListener> cloned = new ArrayList<IDobModelChangeListener>(listeners);
		for (IDobModelChangeListener n : cloned) {
			n.removed(ev);
		}
	}

	void fireModelAdd(DobXmlElement child) {
		DobXmlModelEvent ev = new DobXmlModelEvent(this, child, true);
		List<IDobModelChangeListener> cloned = new ArrayList<IDobModelChangeListener>(listeners);
		for (IDobModelChangeListener n : cloned) {
			n.added(ev);
		}
	}

}
