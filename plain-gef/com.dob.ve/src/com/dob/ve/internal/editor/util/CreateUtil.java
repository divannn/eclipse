package com.dob.ve.internal.editor.util;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.CreationFactory;

import com.dob.ve.abstractmodel.DobXmlElement;
import com.dob.ve.internal.editor.factory.CreationInfo;
import com.dob.ve.internal.editor.factory.CreationModelFactory;

/**
 * @author idanilov
 *
 */
public class CreateUtil {

	public static CreateRequest createRequest(final DobXmlElement parent, final CreationInfo ci) {
		CreateRequest result = new CreateRequest();
		result.setFactory(getFactory(ci));
		//setting location is not important here - it will be overriden in getCreateCommand(CreateRequest).
		result.setLocation(getLocation(parent));
		result.setSize(getSize(parent));
		return result;
	}

	public static CreationFactory getFactory(final CreationInfo ci) {
		return new CreationModelFactory(ci);
	}

	private static Point getLocation(DobXmlElement parent) {
		return new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
	}

	private static Dimension getSize(DobXmlElement parent) {
		return ID2FigureDefaults.DEFAULT_BOUNDS.getSize();
	}

}
