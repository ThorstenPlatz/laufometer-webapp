package de.tp82.laufometer.web.api.rest.importer;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import de.tp82.laufometer.core.RunImporter;
import de.tp82.laufometer.core.RunRepository;
import de.tp82.laufometer.core.TickImportHelper;
import de.tp82.laufometer.model.run.SingleRun;
import de.tp82.laufometer.util.ExceptionHandling;
import de.tp82.laufometer.web.api.rest.importer.model.ImportResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Thorsten Platz
 */
@Service
@Path("/{audience}/tick")
public class ImportResource {
	private static final Logger LOG = Logger.getLogger(ImportResource.class.getName());

	@Autowired
	private RunRepository runRepository;

	@Autowired
	private RunImporter runImporter;

	private ImportResult importTicksInternal(String multipleTickStrings, boolean skipKnownTicks) {
		Preconditions.checkNotNull(multipleTickStrings, "Parameter ticks is mandatory!");
		Preconditions.checkArgument(!multipleTickStrings.isEmpty(), "Parameter ticks cannot be empty!");

		Stopwatch importDuration = new Stopwatch();
		importDuration.start();

		List<Date> ticks = Collections.emptyList();
		List<SingleRun> importedRuns = Collections.emptyList();

		List<String> errors = Lists.newArrayList();
		try {
			ticks = TickImportHelper.extractTicks(multipleTickStrings);
			importedRuns = runImporter.importTicksAsRuns(ticks, skipKnownTicks);
		} catch(TickImportHelper.TickImportException exc) {
			errors.addAll(exc.getErrors());
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
	@Path("/import/{clientId}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response importTicks(@FormParam("ticks") String multipleTickStrings,
	                            @PathParam("clientId") String clientId,
	                            @QueryParam("skipKnownTicks") @DefaultValue(value = "true") boolean skipKnownTicks) {
		ImportResult result = importTicksInternal(multipleTickStrings, skipKnownTicks);
		if(result.getErrors().isEmpty())
			return Response.ok().entity(result).build();
		else
			return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
	}


	@GET
	@Path("/list")
	public Response getAllTicks() {
		Calendar aLongTimeAgo = Calendar.getInstance();
		aLongTimeAgo.set(2000, Calendar.JANUARY, 1);
		List<SingleRun> runs = runRepository.findRuns(aLongTimeAgo.getTime(), Optional.<Date>absent());

		List<Date> ticks = Lists.newArrayList();
		for(SingleRun run : runs)
			ticks.addAll(run.getTicks());

		Collections.sort(ticks);

		return Response.ok(ticks).build();
	}
}
