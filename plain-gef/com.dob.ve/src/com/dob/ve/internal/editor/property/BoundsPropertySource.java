package com.dob.ve.internal.editor.property;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.dob.property.NumberPropertyDescriptor;
import com.dob.property.editor.NumberCellEditor;
import com.dob.ve.abstractmodel.DobXmlElement;
import com.dob.ve.internal.model.FooModelUtils;
import com.dob.ve.internal.model.IFooModelProperty;

/**
 * Auxilary property source used to allow editing "bounds" property granularly : x,y,w,h.
 * @author idanilov
 *
 */
public class BoundsPropertySource extends BasePropertySource {

	private static final String X = "x";
	private static final String Y = "y";
	private static final String W = "w";
	private static final String H = "h";

	public BoundsPropertySource(DobXmlElement e) {
		super(e);
	}

	@Override
	public Object getEditableValue() {
		//return super.getEditableValue();
		return FooModelUtils.getBounds(elem);
	}

	@Override
	protected List<IPropertyDescriptor> createPropertyDescriptors() {
		List<IPropertyDescriptor> result = new ArrayList<IPropertyDescriptor>();
		NumberPropertyDescriptor xPD = new NumberPropertyDescriptor(X, X);
		xPD.setEditorType(NumberCellEditor.INTEGER);
		result.add(xPD);

		NumberPropertyDescriptor yPD = new NumberPropertyDescriptor(Y, Y);
		yPD.setEditorType(NumberCellEditor.INTEGER);
		result.add(yPD);

		NumberPropertyDescriptor wPD = new NumberPropertyDescriptor(W, W);
		wPD.setEditorType(NumberCellEditor.INTEGER);
		result.add(wPD);

		NumberPropertyDescriptor hPD = new NumberPropertyDescriptor(H, H);
		hPD.setEditorType(NumberCellEditor.INTEGER);
		result.add(hPD);
		return result;
	}

	@Override
	public Object getPropertyValue(Object id) {
		Rectangle bounds = FooModelUtils.getBounds(elem);
		if (X.equals(id)) {
			return bounds.x;
		} else if (Y.equals(id)) {
			return bounds.y;
		} else if (W.equals(id)) {
			return bounds.width;
		} else if (H.equals(id)) {
			return bounds.height;
		}
		return super.getPropertyValue(id);
	}

	@Override
	public CompoundCommand setValue(Object model, Object id, Object value) {
		Rectangle curBoundsValue = new Rectangle(FooModelUtils.getBounds(elem));
		int intValue = Integer.parseInt(value.toString());
		if (X.equals(id)) {
			curBoundsValue.x = intValue;
		} else if (Y.equals(id)) {
			curBoundsValue.y = intValue;
		} else if (W.equals(id)) {
			curBoundsValue.width = intValue;
		} else if (H.equals(id)) {
			curBoundsValue.height = intValue;
		}
		return super.setValue(model, IFooModelProperty.BOUNDS, curBoundsValue);
	}

	public CompoundCommand resetValue(Object model, Object id) {
		Rectangle defaultValue = (Rectangle) FooModelUtils.getDefaultValue((DobXmlElement) model,
				IFooModelProperty.BOUNDS);
		Rectangle curBoundsValue = new Rectangle(FooModelUtils.getBounds(elem));
		if (X.equals(id)) {
			curBoundsValue.x = defaultValue.x;
		} else if (Y.equals(id)) {
			curBoundsValue.y = defaultValue.y;
		} else if (W.equals(id)) {
			curBoundsValue.width = defaultValue.width;
		} else if (H.equals(id)) {
			curBoundsValue.height = defaultValue.height;
		}
		return resetValue(model, IFooModelProperty.BOUNDS, curBoundsValue);
	}
}
