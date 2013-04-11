package de.tp82.laufometer.core;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import de.tp82.laufometer.util.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Thorsten Platz
 */
public class TickImportHelper {

	public static List<Date> extractTicks(String lineSeparatedTicks) {
		Iterable<String> tickStrings = Splitter
				.onPattern("\r?\n")
				.trimResults()
				.omitEmptyStrings()
				.split(lineSeparatedTicks);

		List<Date> ticks = Lists.newArrayList();
		DateFormat dateFormatter = DateUtils.IMPORT_FORMAT;

		List<String> errors = Lists.newArrayList();
		for(String tickString : tickStrings) {
			try {
				Date tick = dateFormatter.parse(tickString);
				ticks.add(tick);
			} catch (ParseException e) {
				String error = "Error while converting string into date: " + e;
				errors.add(error);
			}
		}

		if(!errors.isEmpty())
			throw new TickImportException(errors);

		return ticks;
	}

	public static class TickImportException extends RuntimeException {
		private List<String> errors;

		public TickImportException(List<String> errors) {
			Preconditions.checkNotNull(errors);
			Preconditions.checkArgument(!errors.isEmpty());
			this.errors = errors;
		}

		public List<String> getErrors() {
			return Collections.unmodifiableList(errors);
		}
	}
}
