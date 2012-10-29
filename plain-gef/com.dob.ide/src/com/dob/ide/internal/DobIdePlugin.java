package com.dob.ide.internal;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * DOB IDE main plugin class.
 * @author idanilov
 *
 */
public class DobIdePlugin extends AbstractUIPlugin {

	private static DobIdePlugin plugin;

	public DobIdePlugin() {
		plugin = this;
	}

	public static String getId() {
		return getDefault().getBundle().getSymbolicName();
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static DobIdePlugin getDefault() {
		return plugin;
	}

	public static void info(String message) {
		log(new Status(IStatus.INFO, getId(), IStatus.INFO, message, null));
	}

	public static void warning(String message) {
		log(new Status(IStatus.WARNING, getId(), IStatus.WARNING, message, null));
	}

	public static void error(String message) {
		error(message, null);
	}

	public static void error(Throwable e) {
		error("", e);
	}

	public static void error(String message, Throwable e) {
		log(new Status(IStatus.ERROR, getId(), IStatus.ERROR, message, e));
	}

	public static void log(IStatus status) {
		getDefault().getLog().log(status);
	}

	//images---------------------------------------------

	public static Image getImage(final String key) {
		return getDefault().getImageRegistry().get(key);
	}

	public static ImageDescriptor getImageDescriptor(final String key) {
		return getDefault().getImageRegistry().getDescriptor(key);
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		super.initializeImageRegistry(reg);
	}

	public static ImageDescriptor getImageDescriptorForPath(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(getId(), path);
	}

}
