package com.dob.ve.internal.editor.util;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author idanilov
 *
 */
public interface ID2FigureDefaults {

	/**
	 * Draw2d contraint used as display bounds for boundless beans (pixels).
	 */
	Rectangle DYSPLAY_CONSTRAINT = new Rectangle(0, 0, 640, 480);

	/**
	 * Draw2d default size for bean (pixels).
	 */
	Rectangle DEFAULT_BOUNDS = new Rectangle(0, 0, 50, 50);

}
