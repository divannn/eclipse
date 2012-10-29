package com.dob.ve.internal.editor.figure.elem;

import org.eclipse.swt.graphics.Color;

import com.dob.ve.internal.editor.figure.ElementFigure;

/**
 * @author idanilov
 *
 */
public class RectangleFigure extends ElementFigure {

	private Color bgColor;

	public RectangleFigure() {
		super();
		setOpaque(true);
	}

	public void setBGColor(final Color color) {
		if (color != null) {
			if (bgColor != null) {
				bgColor.dispose();
			}
			bgColor = color;
			setBackgroundColor(bgColor);
		}
	}

}
