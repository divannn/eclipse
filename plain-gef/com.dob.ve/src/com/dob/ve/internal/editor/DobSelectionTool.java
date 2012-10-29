package com.dob.ve.internal.editor;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.tools.SelectionTool;

/**
 * Handles ALT+left mouse down to select parent edit part of current selected edit part.
 * Applicable if selection size is 1.
 * @author idanilov
 *
 */
public class DobSelectionTool extends SelectionTool {

	@Override
	protected boolean handleButtonDown(int button) {
		//System.err.println("LLL " + getLocation());
		boolean result = false;
		if (getCurrentInput().isAltKeyDown() && getCurrentInput().isMouseButtonDown(1)) {
			EditPartViewer viewer = getCurrentViewer();
			if (viewer instanceof GraphicalViewer) {
				List selectedObjects = viewer.getSelectedEditParts();
				if (selectedObjects.size() == 1) {
					GraphicalEditPart selected = (GraphicalEditPart) viewer.getFocusEditPart();
					Point p = getLocation().getCopy();
					//System.err.println("DDD1 " + p);
					IFigure selectedFigure = selected.getFigure();
					selectedFigure.translateToRelative(p);
					//System.err.println("DDD2 " + figure.getBounds());
					boolean clickInsideSelected = selectedFigure.containsPoint(p);

					if (clickInsideSelected) {
						EditPart parent = selected.getParent();
						if (parent != null) {
							viewer.select(parent);
							result = true;
							//System.err.println("PARENT SELECTED");
						}
					}
				}
			}
		} else {
			result = super.handleButtonDown(button);
		}
		return result;
	}

}
