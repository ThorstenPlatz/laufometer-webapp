package de.tp82.laufometer.web.api.rest.run.model;

import com.google.common.collect.Lists;
import de.tp82.laufometer.model.run.RunInterval;
import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

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


	public static RunCalendarEvent from(RunInterval run) {
		RunCalendarEvent event = new RunCalendarEvent();

		event.start = run.getIntervalBegin().getTime()/1000;
		event.end = run.getIntervalEnd().getTime()/1000;

		DecimalFormat formatter = new DecimalFormat( "#0.0" );
		String distance = formatter.format(run.getRunDistance()) + " [m]";
		String velocity = formatter.format(run.getAverageSpeed() * 3.6) + " [km/h]";

		Duration duration = new Duration(Math.round(run.getRunDuration()*1000));

		String durationString;
		PeriodFormatter durationFormat;
		if(duration.isLongerThan(new Duration(24*60*60*1000))) {
			durationFormat = new PeriodFormatterBuilder()
				.printZeroAlways()
				.appendHours()
				.appendSeparator(":")
				.appendMinutes()
				.toFormatter();
			durationString= durationFormat.print(duration.toPeriod()) + " [h:m]";
		} else {
			durationFormat = new PeriodFormatterBuilder()
					.printZeroAlways()
					.appendSeparator(":")
					.appendMinutes()
					.appendSeparator(":")
					.appendSeconds()
					.toFormatter();
			durationString = durationFormat.print(duration.toPeriod()) + " [m:s]";
		}

		event.title = distance + " / " + durationString + " / " + velocity;

		event.url = "";

		return event;
	}

	public static List<RunCalendarEvent> from(Iterable<RunInterval> runs) {
		List<RunCalendarEvent> events = Lists.newArrayList();
		for(RunInterval run : runs)
			events.add(from(run));
		return events;
	}
}
