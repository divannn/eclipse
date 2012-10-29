package com.dob.ve.internal.editor.figure;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;

/**
 * @author idanilov
 *
 */
public abstract class BaseFigure extends Figure {


	@Override
	//make border part of client area.
	public Insets getInsets() {
		return IFigure.NO_INSETS;
	}

}
