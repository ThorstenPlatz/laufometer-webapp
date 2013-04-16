package de.tp82.laufometer.web.api.rest.run.model;

import java.util.List;

/**
 * @author Thorsten Platz
 */
public class RunCalendarEvents {
	public List<RunCalendarEvent> events;

	public RunCalendarEvents(List<RunCalendarEvent> events) {
		this.events = events;
	}
}
