package com.dob.ve.internal.editor.figure;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author idanilov
 *
 */
public abstract class ElementFigure extends BaseFigure {

	public ElementFigure() {
		super();
		setBorder(new LineBorder(ColorConstants.lightGray, 1));
	}

	public void setD2Constraint(Rectangle r) {
		IFigure parentFig = getParent();
		if (parentFig != null) {
			parentFig.setConstraint(this, r);
		}
	}

}
