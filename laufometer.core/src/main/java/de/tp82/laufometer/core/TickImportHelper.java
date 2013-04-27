package de.tp82.laufometer.core;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.io.Closeables;
import com.google.common.net.MediaType;
import de.tp82.laufometer.util.DateUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Thorsten Platz
 */
public class TickImportHelper {
	private static final Logger LOG = Logger.getLogger(TickImportHelper.class.getName());

	public static List<Date> extractTicks(InputStream inputStream, MediaType contentType) {
		try {
			String lineSeparatedTicks;

			if(contentType.is(MediaType.ANY_TEXT_TYPE) || contentType.is(MediaType.OCTET_STREAM))
				lineSeparatedTicks = readTicksFromTextFile(inputStream);
			else if(contentType.is(MediaType.ZIP))
				lineSeparatedTicks = readTicksFromZipFile(inputStream);
			else
				throw new IllegalArgumentException("Unsupported content type: " + contentType);

			return extractTicks(lineSeparatedTicks);
		} catch(IOException ioExc) {
			throw Throwables.propagate(ioExc);
		}
	}

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

	private static String readTicksFromTextFile(InputStream inputStream) throws IOException {
		/*
		 * From http://stackoverflow.com/a/5445161:
		 * The reason it works is because Scanner iterates over tokens in the stream, and in this case
		 * we separate tokens using "beginning of the input boundary" (\A) thus giving us only one token
		 * for the entire contents of the stream.
		 */
		java.util.Scanner s = new java.util.Scanner(inputStream, "UTF-8").useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	private static String readTicksFromZipFile(InputStream inputStream) throws IOException {
		StringBuilder lineSeparatedTicks = new StringBuilder();

		ZipInputStream zin = new ZipInputStream(inputStream);
		try {
			int numFilesInZipArchive = 0;
			ZipEntry entry = zin.getNextEntry();
			while(entry != null) {
				++numFilesInZipArchive;
				String filename = entry.getName();
				try {
					lineSeparatedTicks.append(readTicksFromTextFile(zin));
				} catch (Exception exc) {
					LOG.warning("Error reading " + filename + " files from uploaded zip archive.");
				} finally {
					zin.closeEntry();
				}
				entry = zin.getNextEntry();
			}

			if(LOG.isLoggable(Level.INFO))
				LOG.info("Read " + numFilesInZipArchive + " from uploaded zip archive.");

			return lineSeparatedTicks.toString();
		} catch (IOException exc) {
			throw Throwables.propagate(exc);
		} finally {
			Closeables.closeQuietly(zin);
		}
	}
}
