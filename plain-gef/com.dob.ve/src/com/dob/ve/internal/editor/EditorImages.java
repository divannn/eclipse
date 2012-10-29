package com.dob.ve.internal.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.dob.ve.internal.DobVEPlugin;

/**
 * @author idanilov
 *
 */
public class EditorImages {

	public static final String GIF_EXT1 = ".gif";

	private final static String PATH = "icons/elem/";
	private static ImageRegistry imageRegistry;

	public static ImageRegistry getImageRegistry() {
		if (imageRegistry == null) {
			imageRegistry = createImageRegistry();
		}
		return imageRegistry;
	}

	protected static ImageRegistry createImageRegistry() {
		//If we are in the UI Thread use that
		if (Display.getCurrent() != null) {
			return new ImageRegistry(Display.getCurrent());
		}

		if (PlatformUI.isWorkbenchRunning()) {
			return new ImageRegistry(PlatformUI.getWorkbench().getDisplay());
		}

		//Invalid thread access if it is not the UI Thread 
		//and the workbench is not created.
		throw new SWTError(SWT.ERROR_THREAD_INVALID_ACCESS);
	}

	/**
	 * @param fileName just file name without prepending path. Example: Label.gif.
	 * @return image
	 */
	public static Image getImage(String fileName) {
		String filePath = getFullPath(fileName);
		Image result = getImageRegistry().get(filePath);
		if (result == null) {
			getImageRegistry().put(filePath, DobVEPlugin.getImageDescriptorForPath(filePath));
			result = getImageRegistry().get(filePath);
		}
		return result;
	}

	/**
	 * @param fileName just file name without prepending path. Example: Label.gif.
	 * @return image descriptor
	 */
	public static ImageDescriptor getDescriptor(String fileName) {
		String filePath = getFullPath(fileName);
		ImageDescriptor result = getImageRegistry().getDescriptor(filePath);
		if (result == null) {
			getImageRegistry().put(filePath, DobVEPlugin.getImageDescriptorForPath(filePath));
			result = getImageRegistry().getDescriptor(filePath);
		}
		return result;
	}

	//used for decorated images.
	public static Image getImage(ImageDescriptor desc) {
		if (desc == null) {
			return null;
		}
		String key = String.valueOf(desc.hashCode());
		Image result = getImageRegistry().get(key);
		if (result == null) {
			getImageRegistry().put(key, desc);
			result = getImageRegistry().get(key);
		}
		return result;
	}

	private static String getFullPath(String fileName) {
		return PATH + fileName;
	}

	public static void dispose() {
		if (imageRegistry != null) {
			imageRegistry.dispose();
			imageRegistry = null;
		}
	}

}
