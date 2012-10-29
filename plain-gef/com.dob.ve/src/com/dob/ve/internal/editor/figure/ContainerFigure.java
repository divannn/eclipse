package com.dob.ve.internal.editor.figure;

import org.eclipse.draw2d.XYLayout;

/**
 * @author idanilov
 *
 */
public class ContainerFigure extends ElementFigure {

	public ContainerFigure() {
		super();
		XYLayout layout = new XYLayout();
		setLayoutManager(layout);
	}

}
