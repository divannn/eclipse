package com.dob.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utilites for tracing and debugging.
 * @author idanilov
 *
 */
public class DebugUtils {

	private static final DateFormat FORMAT = new SimpleDateFormat("HH:mm:ss");

	private DebugUtils() {
	}

	public static void debug(String message) {
		debug(null, null, message);
	}

	//TODO:remove as will be not used.
	public static void debug(Object object, String message) {

	}

	public static void debug(String level, Object object, String message) {
		debug(level, object != null ? object.getClass() : null, message);
	}

	public static void debug(String level, Class clazz, String message) {
		StringBuffer b = new StringBuffer();
		if (level != null) {
			b.append("{").append(level).append("}");
		}
		b.append("[").append(FORMAT.format(new Date())).append("]");
		if (clazz != null) {
			b.append("<").append(clazz.getSimpleName()).append(">");
		}
		b.append(": ").append(message);
		System.out.println(b.toString());
	}

}
