package com.dob.ve.internal.editor.gef;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PopUpHelper;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.widgets.Control;

/**
 * @author idanilov
 *
 */
public class CursorHelper extends PopUpHelper {

	public CursorHelper(Control c) {
		super(c);
	}

	public void dispose() {
		getShell().dispose();
	}

	public void showCursorFigure(IFigure cursorFigure, int x, int y) {
		if (cursorFigure != null) {
			getLightweightSystem().setContents(cursorFigure);
			Dimension cursorSize = cursorFigure.getPreferredSize();
			setShellBounds(x, y, cursorSize.width, cursorSize.height);
			show();
		}
	}

	protected void hookShellListeners() {
	}

}
