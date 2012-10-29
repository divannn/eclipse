package com.dob.util;

import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

/**
 * @author idanilov
 *
 */
public abstract class AbstractLabelDecorator
		implements ILabelDecorator {

	public Image decorateImage(Image image, Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	public String decorateText(String text, Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

}
