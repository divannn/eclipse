package com.dob.ve.internal.editor.figure;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.swt.SWT;

import com.dob.ve.internal.editor.util.ID2FigureDefaults;

/**
 * @author idanilov
 *
 */
public class ContentsFigure extends FreeformLayer {

	private boolean showBounds;

	public ContentsFigure() {
		//setBorder(new MarginBorder(20));
		setLayoutManager(/*new XYLayout()*/new FreeformLayout());
		//setBackgroundColor(ColorConstants.yellow);
		//setOpaque(true);
	}

	public void setShowBounds(boolean showBounds) {
		this.showBounds = showBounds;
	}

	@Override
	public void paint(Graphics graphics) {
		if (showBounds) {
			graphics.pushState();
			graphics.setForegroundColor(ColorConstants.gray);
			graphics.setLineStyle(SWT.LINE_DOT);
			graphics.drawRectangle(ID2FigureDefaults.DYSPLAY_CONSTRAINT);
			graphics.popState();
		}
		super.paint(graphics);
	}

}
