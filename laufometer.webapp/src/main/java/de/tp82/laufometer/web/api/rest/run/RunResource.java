package de.tp82.laufometer.web.api.rest.run;

import com.google.common.collect.Maps;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import de.tp82.laufometer.core.RunImporter;
import de.tp82.laufometer.core.RunRepository;
import de.tp82.laufometer.model.run.CompositeRun;
import de.tp82.laufometer.model.run.Run;
import de.tp82.laufometer.model.run.SingleRun;
import de.tp82.laufometer.util.DateUtils;
import de.tp82.laufometer.web.api.rest.run.model.RunCalendarEvent;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

		List<Run> runs = Lists.newArrayList();
		runs.addAll(runRepository.findRuns(from, Optional.of(to)));

		/**
		 * 1 - Collect all Ticks from all Runs
		 * 2 - Group these Ticks into intervals (align intervals at day begins (midnight)
		 * 3 - Once the ticks are sorted into intervals, detect runs inside each interval
		 * 4 - build a single composite run from theses runs per interval
		 */


		// 1 - Collect all Ticks from all Runs
		List<Date> ticks = Lists.newArrayList();
		for(Run run : runs)
				ticks.addAll(run.getTicks());
		Collections.sort(ticks);


		// 2 - Group these Ticks into intervals
		// we assume groupInterval < 24h && 24h mod groupInterval = 0

		// fromDayBegin time: 00:00:00
		DateTime fromDayBegin = new DateTime(from);
		fromDayBegin.minusSeconds(fromDayBegin.getSecondOfDay());

		// toDayEnd time: 23:59:59
		DateTime toDayEnd = new DateTime(to);
		toDayEnd.plusDays(1);
		toDayEnd.minusSeconds(toDayEnd.getSecondOfDay());
		toDayEnd.minusSeconds(1);

		long beginTimestamp = fromDayBegin.getMillis()/1000;
		long endTimestamp = toDayEnd.getMillis()/1000;
		long numIntervals = (endTimestamp - beginTimestamp)/groupInterval;

		if(LOG.isLoggable(Level.FINE)) {
			LOG.fine("Generating " + numIntervals + " intervals between "
					+ fromDayBegin.toDate() + "(" + beginTimestamp + ") and "
					+ toDayEnd.toDate() + "(" + endTimestamp + ") using groupInterval = " + groupInterval);
		}

		Map<Long, List<Date>> ticksPerInterval = Maps.newHashMap();
		for(long index = 0; index < numIntervals; ++index) {
			List<Date> intervalTicks = Lists.newArrayList();
			ticksPerInterval.put(index, intervalTicks);
		}

		for(Date tick : ticks) {
			long timestamp = tick.getTime()/1000;
			if(timestamp < beginTimestamp) {
				if(LOG.isLoggable(Level.FINEST))
				LOG.finest("tick ("   + ") is before begin (" + new Date(beginTimestamp*1000) + ")");
			}

			long intervalIndex = (timestamp-beginTimestamp) / groupInterval;
			if(intervalIndex < 0 || intervalIndex >= numIntervals) {
				if(LOG.isLoggable(Level.WARNING))
					LOG.warning("tick (" + new Date(timestamp*1000) + ") has a calculated index of "
							+ intervalIndex + " and is therefore ignored.");
			} else {
				List<Date> intervalTicks = ticksPerInterval.get(intervalIndex);
				intervalTicks.add(tick);
			}
		}


		// 3 - Once the ticks are sorted into intervals, detect runs inside each interval

		List<Run> oneRunPerInterval = Lists.newArrayList();

		for(List<Date> intervalTicks : ticksPerInterval.values()) {
			if(intervalTicks.isEmpty())
				continue;
			List<SingleRun> singleRunsInInterval = runImporter.detectRuns(intervalTicks);
			List<Run> runsInInterval = Lists.newArrayListWithCapacity(singleRunsInInterval.size());
			runsInInterval.addAll(singleRunsInInterval);

			// 4 - build a single composite run from theses runs per interval
			Run runForInterval = CompositeRun.from(runsInInterval);
			oneRunPerInterval.add(runForInterval);
		}

		List<RunCalendarEvent> events = RunCalendarEvent.from(oneRunPerInterval);

		if(LOG.isLoggable(Level.FINE))
			LOG.fine("Providing " + events.size() + " events from "
					+ DateUtils.ISO_8601_FORMAT.format(from) + "(" + from.getTime() + ")"
					+ " to " + DateUtils.ISO_8601_FORMAT.format(to) + "(" + to.getTime() + ") grouped by "
					+ groupInterval + " seconds.");

		return events;
	}
}
