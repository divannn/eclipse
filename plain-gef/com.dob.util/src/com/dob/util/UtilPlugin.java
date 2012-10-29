package com.dob.util;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author idanilov
 *
 */
public class UtilPlugin extends AbstractUIPlugin {

	private static UtilPlugin plugin;

	public UtilPlugin() {
		plugin = this;
	}

	public static String getId() {
		return getDefault().getBundle().getSymbolicName();
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	public static UtilPlugin getDefault() {
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

	/*public File getPluginFolder() throws IOException {
	 URL url = getBundle().getEntry("/");
	 url = FileLocator.toFileURL(url);
	 return new File(url.getPath());
	 }*/

}
