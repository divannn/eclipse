package com.dob.ve.internal.editor.gef;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

/**
 * @author idanilov
 *
 */
public class DirectEditCellEditorLocator
		implements CellEditorLocator {

	private IFigure figure;

	public DirectEditCellEditorLocator(IFigure f) {
		figure = f;
	}

	public void relocate(CellEditor cellEditor) {
		Text text = (Text) cellEditor.getControl();
		Point sel = text.getSelection();
		Point pref = text.computeSize(-1, -1);
		Rectangle rect = figure.getBounds().getCopy();
		figure.translateToAbsolute(rect);
		text.setBounds(rect.x, rect.y, pref.x + 1, pref.y + 1);
		text.setBackground(ColorConstants.white);
		text.setSelection(0);
		text.setSelection(sel);
	}

}
