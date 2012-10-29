package ide.custom;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author idanilov
 *
 */
public class IdeCustomizerPlugin extends AbstractUIPlugin {

	private static IdeCustomizerPlugin plugin;

	public static String getId() {
		return getDefault().getBundle().getSymbolicName();
	}

	public IdeCustomizerPlugin() {
		plugin = this;
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		PerspectiveHackManager.getInstance().init();
	}

	public void stop(BundleContext context) throws Exception {
		PerspectiveHackManager.getInstance().deinit();
		plugin = null;
		super.stop(context);
	}

	public static IdeCustomizerPlugin getDefault() {
		return plugin;
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(getDefault().getBundle()
				.getSymbolicName(), path);
	}

}
