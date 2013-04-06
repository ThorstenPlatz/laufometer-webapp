package de.tp82.laufometer.web.api.rest.importer;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import de.tp82.laufometer.web.DateUtils;
import de.tp82.laufometer.web.api.rest.model.ErrorResult;
import de.tp82.laufometer.web.core.RunImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @author Thorsten Platz
 */
@Service
@Path(value="/import")
@Produces(MediaType.APPLICATION_JSON)
public class ImportResource {
	@Autowired
	private RunImporter runImporter;

	@GET
	@Path(value = "/hello")
	@Produces(MediaType.TEXT_PLAIN)
	public String helloText() {
		return "Hello TEXT!";
	}

	@GET
	@Path(value = "/hello")
	@Produces(MediaType.APPLICATION_JSON)
	public String helloJson() {
		return "Hello JSON!";
	}

	@POST
	@Path(value = "/ticks")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response importTicks(@FormParam(value = "ticks") String multipleTickStrings) {
		Preconditions.checkNotNull(multipleTickStrings);
		Preconditions.checkArgument(!multipleTickStrings.isEmpty(), "Parameter ticks cannot be empty!");

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
				String error = "Converting string '" + tickString
						+ "' into date resulted in error: " + e;
				errors.add(error);
			}
		}

			try {
				runImporter.importTicksAsRuns(ticks);
			} catch (Exception exc) {
				errors.add("Error during import: " + exc);
			}

		if (!errors.isEmpty()) {
			ErrorResult errorResult = new ErrorResult("Errors occurred during import!", errors);
			return Response.status(Response.Status.BAD_REQUEST).entity(errorResult).build();
		} else
			return Response.ok().build();
	}
}
