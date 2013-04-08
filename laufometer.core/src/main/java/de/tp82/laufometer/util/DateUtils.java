package de.tp82.laufometer.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author Thorsten Platz
 */
public class DateUtils {
	public static final String ISO_8601_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ssZ";
	public static final DateFormat ISO_8601_FORMAT = new SimpleDateFormat(ISO_8601_FORMAT_STRING);

	public static final String IMPORT_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	public static final DateFormat IMPORT_FORMAT = new SimpleDateFormat(IMPORT_FORMAT_STRING);
}
