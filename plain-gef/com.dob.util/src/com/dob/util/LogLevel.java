package com.dob.util;

/**
 * @author idanilov
 *
 */
public class LogLevel
		implements Comparable<LogLevel> {

	public static final String L_UNKNOWN = "unknown";
	public static final String L_DEBUG = "debug";
	public static final String L_INFO = "info";
	public static final String L_WARNING = "warning";
	public static final String L_ERROR = "error";

	public static final LogLevel DEBUG = create(L_DEBUG);
	public static final LogLevel INFO = create(L_INFO);
	public static final LogLevel WARNING = create(L_WARNING);
	public static final LogLevel ERROR = create(L_ERROR);

	private int value;
	private String level;

	public static LogLevel create(final String level) {
		LogLevel result = new LogLevel(L_UNKNOWN, Integer.MAX_VALUE);
		if (L_DEBUG.equalsIgnoreCase(level)) {
			result = new LogLevel(level, 100);
		} else if (L_INFO.equalsIgnoreCase(level)) {
			result = new LogLevel(level, 200);
		} else if (L_WARNING.equalsIgnoreCase(level)) {
			result = new LogLevel(level, 300);
		} else if (L_ERROR.equalsIgnoreCase(level)) {
			result = new LogLevel(level, 400);
		}
		return result;
	}

	public int compareTo(LogLevel o) {
		if (o == null) {
			return 1;
		}
		if (value > o.value) {
			return 1;
		} else if (value == o.value) {
			return 0;
		} else {
			return -1;
		}
	}

	private LogLevel(final String l, final int v) {
		value = v;
		level = l;
	}

	public String getLevel() {
		return level;
	}

	public int getValue() {
		return value;
	}

}
