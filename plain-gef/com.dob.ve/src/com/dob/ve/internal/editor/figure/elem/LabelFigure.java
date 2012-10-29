package com.dob.ve.internal.editor.figure.elem;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.swt.graphics.Color;

import com.dob.ve.internal.editor.figure.ElementFigure;

/**
 * @author idanilov
 *
 */
public class LabelFigure extends ElementFigure {

	private Label textFigure;

	public LabelFigure() {
		setLayoutManager(new BorderLayout());
		textFigure = new Label();
		add(textFigure, BorderLayout.CENTER);
	}

	public void setText(final String text) {
		textFigure.setText(text);
	}

	public void setColor(final Color color) {
		if (color != null) {
			Color oldColor = textFigure.getForegroundColor();
			if (oldColor != null) {
				oldColor.dispose();
			}
			textFigure.setForegroundColor(color);
		}
	}

}
