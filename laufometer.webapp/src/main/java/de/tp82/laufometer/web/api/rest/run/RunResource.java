package de.tp82.laufometer.web.api.rest.run;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import de.tp82.laufometer.core.RunRepository;
import de.tp82.laufometer.core.importer.RunImporter;
import de.tp82.laufometer.model.run.RunInterval;
import de.tp82.laufometer.util.FormattingUtils.DateFormatting;
import de.tp82.laufometer.web.api.rest.run.model.RunCalendarEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Thorsten Platz
 */
@Service
@Path("/{audience}/run")
public class RunResource {
	private static final Logger LOG = Logger.getLogger(RunResource.class.getName());

	@Autowired
	private RunRepository runRepository;

	@Autowired
	private RunImporter runImporter;

	@GET
	@Path("/calendar/group")
	@Produces(MediaType.APPLICATION_JSON)
	public List<RunCalendarEvent> getRuns(@QueryParam("start") long fromTimestamp,
	                         @QueryParam("end") long toTimestamp,
	                         @QueryParam("groupInterval") long groupInterval) {

		Date from = new Date(fromTimestamp * 1000);
		Date to = new Date(toTimestamp * 1000);

		List<RunInterval> runs = runRepository.findRuns(from, Optional.of(to));

		// summarize the runIntervals into bigger groups
		List<RunInterval> groups = Lists.newArrayList();

		List<RunInterval> intervalsForGroup = Lists.newArrayList();
		long groupLength = 0;
		for(RunInterval run : runs) {
			if(groupLength + run.getIntervalLength() <= groupInterval) {
				intervalsForGroup.add(run);
				groupLength += run.getIntervalLength();
			} else {
				RunInterval group = RunInterval.from(intervalsForGroup);
				groups.add(group);

				intervalsForGroup = Lists.newArrayList();
				groupLength = 0;
			}
		}

		List<RunCalendarEvent> events = RunCalendarEvent.from(groups);

		if(LOG.isLoggable(Level.FINE))
			LOG.fine("Providing " + events.size() + " events from "
					+ DateFormatting.ISO_8601_FORMAT.format(from) + "(" + from.getTime() + ")"
					+ " to " + DateFormatting.ISO_8601_FORMAT.format(to) + "(" + to.getTime() + ") grouped by "
					+ groupInterval + " seconds.");

		return events;
	}

}
