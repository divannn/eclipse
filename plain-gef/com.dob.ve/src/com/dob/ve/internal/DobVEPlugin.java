package com.dob.ve.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.dob.util.PluginLogger;
import com.dob.ve.internal.editor.EditorImages;

/**
 * @author idanilov
 *
 */
public class DobVEPlugin extends AbstractUIPlugin {

	public static final String LOGGER_EDITOR = "/debug/editor";

	private static DobVEPlugin plugin;
	private static Map<String, PluginLogger> loggers = new HashMap<String, PluginLogger>();

	//private BundleContext context;

	public DobVEPlugin() {
		super();
		plugin = this;
	}

	/**
	 * @return OSGi plugin identifier
	 */
	public static String getId() {
		return getDefault().getBundle().getSymbolicName();
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		//this.context = context;
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		EditorImages.dispose();
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static DobVEPlugin getDefault() {
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

	public static PluginLogger getLogger(final String logger) {
		PluginLogger result = loggers.get(logger);
		if (result == null) {
			result = new PluginLogger(getDefault(), logger);
			loggers.put(logger, result);
		}
		return result;
	}

	//images--------------------------------------------------------------------

	public static Image getImage(final String key) {
		return getDefault().getImageRegistry().get(key);
	}

	public static ImageDescriptor getDescriptor(final String key) {
		return getDefault().getImageRegistry().getDescriptor(key);
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		super.initializeImageRegistry(reg);
		reg.put(IImageConstant.OVERVIEW, getImageDescriptorForPath(IImageConstant.OVERVIEW));
		reg.put(IImageConstant.SNAPTOGRID, getImageDescriptorForPath(IImageConstant.SNAPTOGRID));
		reg.put(IImageConstant.SNAPTOGEOM, getImageDescriptorForPath(IImageConstant.SNAPTOGEOM));

		reg.put(IImageConstant.VISIBLE_OVR, getImageDescriptorForPath(IImageConstant.VISIBLE_OVR));
	}

	public static ImageDescriptor getImageDescriptorForPath(final String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(getId(), path);
	}

}
