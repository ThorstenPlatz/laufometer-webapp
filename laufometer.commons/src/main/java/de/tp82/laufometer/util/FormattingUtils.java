package de.tp82.laufometer.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * @author Thorsten Platz
 */
public class FormattingUtils {

	public static class DateFormatting {
		public static final String ISO_8601_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ssZ";
		public static final DateFormat ISO_8601_FORMAT = new SimpleDateFormat(ISO_8601_FORMAT_STRING);

		public static final String IMPORT_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ss.SSS";
		public static final DateFormat IMPORT_FORMAT;

		static {
			IMPORT_FORMAT = new SimpleDateFormat(IMPORT_FORMAT_STRING);
			//TODO tp: remove this and include timezone in import pattern
			IMPORT_FORMAT.setTimeZone(TimeZone.getTimeZone("CEST"));
		}
	}

	public static class NumberFormatting {
		public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat( "###.0" );
	}
}
