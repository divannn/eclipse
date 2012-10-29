package com.dob.ve.abstractmodel;

/**
 * @author idanilov
 *
 */
public interface IDobModelChangeListener {

	void changed(DobXmlModelEvent e);

	void added(DobXmlModelEvent e);

	void removed(DobXmlModelEvent e);

}
