package com.dob.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * String utilites.
 * @author idanilov
 *
 */
public class StringUtils {

	private StringUtils() {
	}

	public static boolean isEmpty(String s) {
		if (s == null)
			return true;
		return s.trim().length() < 1;
	}

	public static boolean equals(String s1, String s2) {
		if (isEmpty(s1) && isEmpty(s2))
			return true;
		else
			return s1 != null ? s1.equals(s2) : s2 == null;
	}

	public static String dumpThrowable(Throwable e) {
		if (e == null)
			return null;
		if (e.getCause() != null)
			e = e.getCause();
		StringWriter writer = new StringWriter();
		e.printStackTrace(new PrintWriter(writer));
		return writer.toString();
	}
}
