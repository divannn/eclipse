package com.dob.ve.abstractmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple object that can contain properties and other objects.
 * Represents VE XML model.
 * @author idanilov
 *
 */
public class DobXmlElement {

	private Map<String, Object> properties;
	private List<DobXmlElement> children;
	private DobXmlElement parent;
	protected ArrayList<IDobModelChangeListener> listeners;

	public DobXmlElement() {
		properties = new HashMap<String, Object>();
		children = new ArrayList<DobXmlElement>();
		listeners = new ArrayList<IDobModelChangeListener>();
	}

	public void dispose() {
		listeners.clear();
		properties.clear();
		for (DobXmlElement n : children) {
			n.dispose();
		}
		children.clear();
	}

	public DobXmlModel getModel() {
		DobXmlElement current = this;
		do {
			if (current instanceof DobXmlModel) {
				return (DobXmlModel) current;
			}
		} while ((current = current.getParent()) != null);
		return null;
	}

	public void addChangeListener(IDobModelChangeListener l) {
		if (l != null) {
			listeners.add(l);
		}
	}

	public void removeChangeListener(IDobModelChangeListener l) {
		if (l != null) {
			listeners.remove(l);
		}
	}

	void fireAdd(final DobXmlElement child) {
		DobXmlModelEvent ev = new DobXmlModelEvent(this, child);
		List<IDobModelChangeListener> cloned = new ArrayList<IDobModelChangeListener>(listeners);
		for (IDobModelChangeListener n : cloned) {
			n.added(ev);
		}
	}

	void fireRemove(final DobXmlElement child) {
		DobXmlModelEvent ev = new DobXmlModelEvent(this, child);
		List<IDobModelChangeListener> cloned = new ArrayList<IDobModelChangeListener>(listeners);
		for (IDobModelChangeListener n : cloned) {
			n.removed(ev);
		}
	}

	void fireChange(String key) {
		DobXmlModelEvent ev = new DobXmlModelEvent(this, key);
		List<IDobModelChangeListener> cloned = new ArrayList<IDobModelChangeListener>(listeners);
		for (IDobModelChangeListener n : cloned) {
			n.changed(ev);
		}
	}

	/**
	 * @return list of objects's children
	 */
	public List<DobXmlElement> getChildren() {
		return Collections.unmodifiableList(children);
	}

	public void addChild(final DobXmlElement e) {
		addChild(e, children.size());
	}

	public void addChild(final DobXmlElement e, final int index) {
		if (e == this) {
			throw new IllegalArgumentException("Cannot add element to itself");
		}
		if (!children.contains(e)) {
			children.add(index, e);
			e.setParent(this);
			fireAdd(e);
			//in some situations it is useful to have notification that element was just added to model (not parent).
			DobXmlModel m = getModel();
			if (m != null) {
				m.fireModelAdd(e);
			}
		}
	}

	public void removeChild(final DobXmlElement e) {
		boolean removed = children.remove(e);
		if (removed) {
			DobXmlModel m = getModel();
			e.setParent(null);
			fireRemove(e);
			//in some situations it is useful to have notification that element was just removed from model (not parent).
			if (m != null) {
				m.fireModelRemove(e);
			}
		}
	}

	public int childIndex(final DobXmlElement e) {
		return children.indexOf(e);
	}

	public DobXmlElement getParent() {
		return parent;
	}

	void setParent(final DobXmlElement parent) {
		this.parent = parent;
	}

	public Map<String, Object> getProperties() {
		return Collections.unmodifiableMap(properties);
	}

	/**
	 * @param key
	 * @return property by key
	 */
	public Object getOProperty(final String key) {
		return properties.get(key);
	}

	/**
	 * @param key
	 * @param value
	 */
	public void setOProperty(final String key, final Object value) {
		Object oldValue = properties.get(key);
		//do not set the same value.
		if ((value == oldValue) || (value != null && value.equals(oldValue))) {
			return;
		}
		properties.put(key, value);
		DobXmlModel m = getModel();
		if (m != null) {//not added to parent yet.
			fireChange(key);
		}
	}

}
