package com.dob.util;

import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;

/**
 * @author idanilov
 *
 */
public class OverlayImageDescriptor extends CompositeImageDescriptor {

	private Image baseImage;
	private Point sizeOfImage;
	private ImageDescriptor overlay;
	private LOCATION location;

	public enum LOCATION {
		TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
	}

	public OverlayImageDescriptor(Image baseImage, ImageDescriptor overlay, LOCATION loc) {
		this.baseImage = baseImage;
		this.overlay = overlay;
		location = loc;
		sizeOfImage = new Point(baseImage.getBounds().width, baseImage.getBounds().height);
	}

	protected void drawCompositeImage(int width, int height) {
		ImageData bg = baseImage.getImageData();
		drawImage(bg, 0, 0);
		drawOverlay();
	}

	private void drawOverlay() {
		Point size = getSize();
		ImageData data = overlay.getImageData();
		switch (location) {
			case TOP_LEFT:
				drawImage(data, 0, 0);
				break;
			case TOP_RIGHT:
				drawImage(data, size.x - data.width, 0);
				break;
			case BOTTOM_LEFT:
				drawImage(data, 0, size.y - data.height);
				break;
			case BOTTOM_RIGHT:
				drawImage(data, size.x - data.width, size.y - data.height);
				break;
			default:
				break;
		}
	}

	protected Point getSize() {
		return sizeOfImage;
	}

	@Override
	//needed for caching descriptor.
	public int hashCode() {
		return baseImage.hashCode() + overlay.hashCode() + sizeOfImage.hashCode()
				+ location.hashCode();
	}

	@Override
	//needed for caching descriptor.
	public boolean equals(Object obj) {
		if (obj == null || !OverlayImageDescriptor.class.equals(obj.getClass())) {
			return false;
		}
		OverlayImageDescriptor other = (OverlayImageDescriptor) obj;
		return baseImage.equals(other.baseImage) && sizeOfImage.equals(other.sizeOfImage)
				&& overlay.equals(other.overlay) && location == other.location;
	}

}
