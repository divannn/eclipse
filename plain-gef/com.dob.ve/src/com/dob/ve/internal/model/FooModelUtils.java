package com.dob.ve.internal.model;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.geometry.Rectangle;

import com.dob.ve.abstractmodel.DobXmlElement;
import com.dob.ve.internal.editor.util.ID2FigureDefaults;

/**
 * @author idanilov
 *
 */
public class FooModelUtils {

	private static Map<String, Map<String, Object>> defaultProps;

	private static Map<String, Map<String, Object>> initDefaultProperties() {
		Map<String, Map<String, Object>> result = new HashMap<String, Map<String, Object>>();
		//Holder.
		Map<String, Object> props = new HashMap<String, Object>();
		props.put(IFooModelProperty.BOUNDS, ID2FigureDefaults.DEFAULT_BOUNDS);
		props.put(IFooModelProperty.VISIBLE, IFooPropertyDefaults.VISIBLE_PROP);
		props.put(IFooModelProperty.INT_PROP, IFooPropertyDefaults.INT_PROP);
		props.put(IFooModelProperty.DOUBLE_PROP, IFooPropertyDefaults.DOUBLE_PROP);
		props.put(IFooModelProperty.STR_PROP, IFooPropertyDefaults.STR_PROP);
		result.put(IFooModelType.HOLDER, props);

		//Rectangle.
		props = new HashMap<String, Object>();
		props.put(IFooModelProperty.BOUNDS, ID2FigureDefaults.DEFAULT_BOUNDS);
		props.put(IFooModelProperty.VISIBLE, IFooPropertyDefaults.VISIBLE_PROP);
		props.put(IFooModelProperty.BG_COLOR, IFooPropertyDefaults.BG_COLOR);
		result.put(IFooModelType.RECTANGLE, props);

		//Label.
		props = new HashMap<String, Object>();
		props.put(IFooModelProperty.BOUNDS, ID2FigureDefaults.DEFAULT_BOUNDS);
		props.put(IFooModelProperty.VISIBLE, IFooPropertyDefaults.VISIBLE_PROP);
		props.put(IFooModelProperty.FG_COLOR, IFooPropertyDefaults.FG_COLOR);
		result.put(IFooModelType.LABEL, props);

		//Label2.
		result.put(IFooModelType.LABEL2, props);
		return result;
	}

	public static Map<String, Object> getDefaultValues(final DobXmlElement bean) {
		return getDefaultValues(getType(bean));
	}

	/**
	 * @param type
	 * @return map of default values for specified bean type
	 */
	public static Map<String, Object> getDefaultValues(final String type) {
		if (defaultProps == null) {
			defaultProps = initDefaultProperties();
		}
		return defaultProps.get(type);
	}

	public static Object getDefaultValue(final DobXmlElement bean, final String propName) {
		return getDefaultValue(getType(bean), propName);
	}

	/**
	 * @param type
	 * @param propName
	 * @return default value for specified bean type and property name
	 */
	public static Object getDefaultValue(final String type, final String propName) {
		Object result = null;
		if (type != null) {
			Map props = getDefaultValues(type);
			if (props != null) {
				result = props.get(propName);
			}
		}
		return result;
	}

	public static boolean isResizable(final DobXmlElement bean) {
		return isResizable(getType(bean));
	}

	public static boolean isResizable(final String type) {
		return !IFooModelType.LABEL2.equals(type);
	}

	//shortcuts----------------------------------------
	public static void setId(final DobXmlElement bean, final String id) {
		bean.setOProperty(IFooModelProperty.ID, id);
	}

	public static String getId(final DobXmlElement bean) {
		return (String) bean.getOProperty(IFooModelProperty.ID);
	}

	public static void setType(final DobXmlElement bean, final String type) {
		bean.setOProperty(IFooModelProperty.TYPE, type);
	}

	public static String getType(final DobXmlElement bean) {
		return (String) bean.getOProperty(IFooModelProperty.TYPE);
	}

	public static Boolean isVisible(final DobXmlElement bean) {
		Boolean result = (Boolean) bean.getOProperty(IFooModelProperty.VISIBLE);
		if (result == null) {
			result = Boolean.FALSE;
		}
		return result;
	}

	public static void setVisible(final DobXmlElement bean, final Boolean v) {
		bean.setOProperty(IFooModelProperty.VISIBLE, v);
	}

	public static Rectangle getBounds(final DobXmlElement bean) {
		return (Rectangle) bean.getOProperty(IFooModelProperty.BOUNDS);
	}

	public static void setBounds(final DobXmlElement bean, final Rectangle b) {
		bean.setOProperty(IFooModelProperty.BOUNDS, b);
	}

}
