package com.dob.ve.internal.editor.gef;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;

/**
 * All taken from ToolUtilities. ToolUtilities works only with GraphicalEditPart - 
 * so code changed to work with tree edit parts also.
 * @see org.eclipse.gef.tools.ToolUtilities
 * @author idanilov
 *
 */
public final class EditPartUtil {

	private EditPartUtil() {
	}

	/**
	 * Returns a list containing the top level selected edit parts based on the viewer's
	 * selection.
	 * @param viewer the viewer
	 * @return the selection excluding dependants
	 */
	public static List<EditPart> getSelectionWithoutDependants(EditPartViewer viewer) {
		List selectedParts = viewer.getSelectedEditParts();
		List<EditPart> result = new ArrayList<EditPart>();
		for (int i = 0; i < selectedParts.size(); i++) {
			EditPart editpart = (EditPart) selectedParts.get(i);
			if (!isAncestorContainedIn(selectedParts, editpart)) {
				result.add(editpart);
			}
		}
		return result;
	}

	/**
	 * Returns a list containing the top level selected edit parts based on the passed in
	 * list of selection.
	 * @param selectedParts the complete selection
	 * @return the selection excluding dependants
	 */
	public static List<EditPart> getSelectionWithoutDependants(List selectedParts) {
		List<EditPart> result = new ArrayList<EditPart>();
		for (int i = 0; i < selectedParts.size(); i++) {
			EditPart editpart = (EditPart) selectedParts.get(i);
			if (!isAncestorContainedIn(selectedParts, editpart))
				result.add(editpart);
		}
		return result;
	}

	private static boolean isAncestorContainedIn(Collection c, EditPart ep) {
		ep = ep.getParent();
		while (ep != null) {
			if (c.contains(ep)) {
				return true;
			}
			ep = ep.getParent();
		}
		return false;
	}

}
