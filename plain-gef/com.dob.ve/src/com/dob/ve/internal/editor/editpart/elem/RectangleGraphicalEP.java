package com.dob.ve.internal.editor.editpart.elem;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.views.properties.IPropertySource;

import com.dob.ve.abstractmodel.DobXmlModelEvent;
import com.dob.ve.internal.editor.editpart.ElementGraphicalEP;
import com.dob.ve.internal.editor.figure.elem.RectangleFigure;
import com.dob.ve.internal.editor.property.editor.ColorIntEditor;
import com.dob.ve.internal.editor.property.elem.RectanglePropertySource;
import com.dob.ve.internal.model.IFooModelProperty;

/**
 * @author idanilov
 *
 */
public class RectangleGraphicalEP extends ElementGraphicalEP {

	@Override
	protected IFigure createFigure() {
		return new RectangleFigure();
	}

	@Override
	protected IPropertySource createPropertySource() {
		return new RectanglePropertySource(getElement());
	}

	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		refreshBGColor();
	}

	@Override
	protected void refreshVisuals(DobXmlModelEvent e) {
		String propName = e.getPropertyName();
		if (IFooModelProperty.BG_COLOR.equals(propName)) {
			refreshBGColor();
		}
		super.refreshVisuals(e);
	}

	private void refreshBGColor() {
		Object value = getElement().getOProperty(IFooModelProperty.BG_COLOR);
		Color color = new Color(getDisplay(), 0, 0, 0);
		if (value != null) {
			int fillColor = (Integer) value;
			RGB rgb = ColorIntEditor.int2rgb(fillColor);
			color = new Color(getDisplay(), rgb);
		}
		RectangleFigure fig = (RectangleFigure) getFigure();
		fig.setBGColor(color);
	}

}
