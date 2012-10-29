package com.dob.ve.internal.editor.editpart.elem;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.views.properties.IPropertySource;

import com.dob.ve.abstractmodel.DobXmlModelEvent;
import com.dob.ve.internal.editor.editpart.ElementGraphicalEP;
import com.dob.ve.internal.editor.figure.elem.LabelFigure;
import com.dob.ve.internal.editor.property.editor.ColorIntEditor;
import com.dob.ve.internal.editor.property.elem.LabelPropertySource;
import com.dob.ve.internal.model.IFooModelProperty;

/**
 * @author idanilov
 *
 */
public class LabelGraphicalEP extends ElementGraphicalEP {

	@Override
	protected IFigure createFigure() {
		return new LabelFigure();
	}

	@Override
	protected IPropertySource createPropertySource() {
		return new LabelPropertySource(getElement());
	}

	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		refreshText();
		refreshColor();
	}

	@Override
	protected void refreshVisuals(DobXmlModelEvent e) {
		String propName = e.getPropertyName();
		if (IFooModelProperty.TEXT.equals(propName)) {
			refreshText();
		}
		if (IFooModelProperty.FG_COLOR.equals(propName)) {
			refreshColor();
		}
		super.refreshVisuals(e);
	}

	private void refreshText() {
		Object value = getElement().getOProperty(IFooModelProperty.TEXT);
		LabelFigure f = (LabelFigure) getFigure();
		if (value == null) {
			value = "";
		}
		f.setText(value.toString());
	}

	private void refreshColor() {
		Object value = getElement().getOProperty(IFooModelProperty.FG_COLOR);
		LabelFigure f = (LabelFigure) getFigure();
		Color color = new Color(getDisplay(), 0, 0, 0);
		if (value instanceof Integer) {
			int fillColor = ((Integer) value).intValue();
			RGB rgb = ColorIntEditor.int2rgb(fillColor);
			color = new Color(getDisplay(), rgb);
		}
		f.setColor(color);
	}

}
