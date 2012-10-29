package com.dob.util;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;


/**
 * @author idanilov
 *
 */
public class PluginLogger {

	protected Plugin plugin;
	protected String option;

	public PluginLogger(final Plugin p, final String o) {
		plugin = p;
		option = o;
	}

	public void debug(final Object o, final String msg) {
		if (isLoggable(LogLevel.DEBUG)) {
			DebugUtils.debug(LogLevel.DEBUG.getLevel(), o, msg);
		}
	}

	public void info(final Object o, final String msg) {
		if (isLoggable(LogLevel.INFO)) {
			DebugUtils.debug(LogLevel.INFO.getLevel(), o, msg);
		}
	}

	public void warning(final Object o, final String msg) {
		if (isLoggable(LogLevel.WARNING)) {
			DebugUtils.debug(LogLevel.WARNING.getLevel(), o, msg);
		}
	}

	public void error(final Object o, final String msg) {
		if (isLoggable(LogLevel.ERROR)) {
			DebugUtils.debug(LogLevel.ERROR.getLevel(), o, msg);
		}
	}

	private boolean isLoggable(final LogLevel level) {
		if (level == null) {
			return false;
		}
		String loggerValue = Platform.getDebugOption(plugin.getBundle().getSymbolicName() + option);
		LogLevel logLevel = LogLevel.create(loggerValue);
		return plugin.isDebugging() && level.compareTo(logLevel) >= 0;
	}

}
