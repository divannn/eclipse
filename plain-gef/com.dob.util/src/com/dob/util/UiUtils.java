package com.dob.util;

import java.awt.Image;
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;

import org.eclipse.core.internal.runtime.IRuntimeConstants;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * @author idanilov
 *
 */
public class UiUtils {

	public static final IStatus ERROR_STATUS = new Status(IStatus.OK, IRuntimeConstants.PI_RUNTIME,
			IStatus.ERROR, "Error", null);
	public static final ImageDescriptor IMG_COLLAPSE_ALL = getUIImageDescriptor("elcl16/collapseall.gif"); //$NON-NLS-1$
	public static final ImageDescriptor IMG_COLLAPSE_ALL_DISABLED = getUIImageDescriptor("dlcl16/collapseall.gif"); //$NON-NLS-1$

	private UiUtils() {
	}

	/**
	 * @return active WB window
	 */
	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow result = workbench.getActiveWorkbenchWindow();
		if (result == null) {
			int cnt = workbench.getWorkbenchWindowCount();
			if (cnt > 0) {
				return workbench.getWorkbenchWindows()[0];
			}
		}
		return result;
	}

	public static ImageDescriptor getUIImageDescriptor(String relativePath) {
		URL url = Platform.getBundle(PlatformUI.PLUGIN_ID).getEntry("icons/full/" + relativePath); //$NON-NLS-1$
		return url != null ? ImageDescriptor.createFromURL(url) : ImageDescriptor
				.getMissingImageDescriptor();
	}

	public static java.awt.Dimension getImageSize(final String targetFullPath) {
		java.awt.Dimension result = null;
		if (targetFullPath != null) {
			File f = new File(targetFullPath);
			if (f.exists()) {
				Image i = new ImageIcon(targetFullPath).getImage();
				result = getImageSize(i);
				i.flush();
			}
		}
		return result;
	}

	public static java.awt.Dimension getImageSize(final Image img) {
		java.awt.Dimension result = null;
		if (img != null) {
			int w = img.getWidth(null);
			int h = img.getHeight(null);
			if (w > 0 && h > 0) {
				result = new java.awt.Dimension(w, h);
			}
		}
		return result;
	}

}
