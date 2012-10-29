package com.dob.ve.internal.editor;

import java.util.HashMap;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.ui.IEditorPart;

/**
 * @author idanilov
 *
 */
public class DobEditDomain extends DefaultEditDomain {

	protected HashMap genericData;

	public DobEditDomain(IEditorPart editorPart) {
		super(editorPart);
		genericData = new HashMap(3);
	}

	/**
	 * Get data for the specified key. Return null if the
	 * key is not set.
	 */
	public Object getData(Object key) {
		return genericData.get(key);
	}

	/**
	 * Set the data for the specified key.
	 */
	public void setData(Object key, Object data) {
		genericData.put(key, data);
	}

	/**
	 * Remove the data for the specified key.
	 * Return the old value. Return null if not set.
	 */
	public Object removeData(Object key) {
		return genericData.remove(key);
	}

	public void dispose() {
		genericData.clear();
	}

	public static DobEditDomain getEditDomain(EditPart ep) {
		return (DobEditDomain) ep.getRoot().getViewer().getEditDomain();
	}

}
