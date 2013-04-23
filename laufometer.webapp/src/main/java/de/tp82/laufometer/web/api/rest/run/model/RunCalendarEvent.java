package de.tp82.laufometer.web.api.rest.run.model;

import com.google.common.collect.Lists;
import de.tp82.laufometer.model.run.Run;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @author Thorsten Platz
 */
public class RunCalendarEvent {
	public String title;
	public boolean allDay = false;
	public long start;
	public long end;
	public String url;


	public static RunCalendarEvent from(Run run) {
		RunCalendarEvent event = new RunCalendarEvent();

		event.start = run.getBegin().getTime()/1000;
		event.end = run.getEnd().getTime()/1000;

		DecimalFormat formatter = new DecimalFormat( "#0.0" );
		String distance = formatter.format(run.getDistance()) + " [m]";
		String velocity = formatter.format(run.getAverageSpeed() * 3.6) + " [km/h]";

		event.title = distance + " / " + velocity;

		event.url = "";

		return event;
	}

	public static List<RunCalendarEvent> from(Iterable<Run> runs) {
		List<RunCalendarEvent> events = Lists.newArrayList();
		for(Run run : runs)
			events.add(from(run));
		return events;
	}
}