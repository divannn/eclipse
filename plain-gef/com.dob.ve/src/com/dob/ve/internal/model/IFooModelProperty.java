package com.dob.ve.internal.model;

/**
 * @author idanilov
 *
 */
public interface IFooModelProperty {

	String ID = "id";
	/**
	 * @readonly
	 */
	String TYPE = "type";
	/**
	 * @readonly
	 */
	String DISPLAY_NAME = "displayName";
	String BOUNDS = "bounds";

	String DOUBLE_PROP = "double";
	String INT_PROP = "int";
	String STR_PROP = "str";
	
	String TEXT = "text";
	String FG_COLOR = "fgColor";
	String BG_COLOR = "bgColor";

	/**
	 * Non model property that allows to show/hide any element on diagram.
	 */
	String VISIBLE = "visible";

}
