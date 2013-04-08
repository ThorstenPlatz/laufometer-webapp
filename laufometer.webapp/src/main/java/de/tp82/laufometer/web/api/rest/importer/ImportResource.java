package de.tp82.laufometer.web.api.rest.importer;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.jersey.api.view.Viewable;
import de.tp82.laufometer.util.DateUtils;
import de.tp82.laufometer.util.ExceptionHandling;
import de.tp82.laufometer.web.api.rest.importer.model.ImportResult;
import de.tp82.laufometer.core.RunImporter;
import de.tp82.laufometer.model.Run;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Thorsten Platz
 */
@Service
@Path(value="/import")
@Produces(MediaType.APPLICATION_JSON)
public class ImportResource {
	private static final Logger LOG = Logger.getLogger(ImportResource.class.getName());

	@Autowired
	private RunImporter runImporter;

	private ImportResult importTicksInternal(String multipleTickStrings, boolean skipKnownTicks) {
		Preconditions.checkNotNull(multipleTickStrings, "Parameter ticks is mandatory!");
		Preconditions.checkArgument(!multipleTickStrings.isEmpty(), "Parameter ticks cannot be empty!");

		Stopwatch importDuration = new Stopwatch();
		importDuration.start();

		Iterable<String> tickStrings = Splitter
				.onPattern("\r?\n")
				.trimResults()
				.omitEmptyStrings()
				.split(multipleTickStrings);

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

		List<Run> importedRuns = Collections.emptyList();
		try {
			importedRuns = runImporter.importTicksAsRuns(ticks, skipKnownTicks);
		} catch (Exception exc) {
			if(LOG.isLoggable(Level.WARNING)) {
				String stacktrace = ExceptionHandling.getStacktrace(exc);
				LOG.warning("Error during import: " + exc + ". Stacktrace follows:\n" + stacktrace);
			}
			errors.add("Error during import: " + exc);
		}
		importDuration.stop();
		return new ImportResult(importedRuns, ticks,
				importDuration.elapsed(TimeUnit.SECONDS), errors);
	}

	@POST
	@Path(value = "/ticks")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response importTicks(@FormParam(value = "ticks") String multipleTickStrings,
	                            @QueryParam(value="skipKnownTicks") @DefaultValue(value = "true") boolean skipKnownTicks) {
		ImportResult result = importTicksInternal(multipleTickStrings, skipKnownTicks);
		if(result.getErrors().isEmpty())
			return Response.ok().entity(result).build();
		else
			return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
	}

	@POST
	@Path(value = "/ticks")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_HTML)
	public Response importTicksForHtmlView(@FormParam(value = "ticks") String multipleTickStrings,
	                                       @QueryParam(value="skipKnownTicks") @DefaultValue(value = "true") boolean skipKnownTicks) {
		ImportResult result = importTicksInternal(multipleTickStrings, skipKnownTicks);

		Map<String, Object> model = Maps.newHashMap();
		model.put("result", result);

		Viewable resultView = new Viewable("/views/uploadedTicks", model);

		if(result.getErrors().isEmpty())
			return Response.ok(resultView).build();
		else
			return Response.status(Response.Status.BAD_REQUEST).entity(resultView).build();

	}
}
