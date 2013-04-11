package de.tp82.laufometer.model;

import com.google.common.collect.Lists;
import de.tp82.laufometer.model.run.Run;
import de.tp82.laufometer.model.run.RunTickProvider;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Thorsten Platz
 */
public class RunComposer implements RunTickProvider {

	private List<Date> ticks = Lists.newArrayList();

	private Integer count = null;
	private Date from = null;
	private Date to = null;
	private Duration tickInterval = null;

	private boolean composed = false;

	private RunComposer() {
	}

	public void count(int count) {
		checkIfModifiable();
		this.count = count;
	}

	public void from(Date from) {
		checkIfModifiable();
		this.from = from;
	}

	public void to(Date to) {
		checkIfModifiable();
		this.to = to;
	}

	public void tickInterval(Duration tickInterval) {
		this.tickInterval = tickInterval;
	}

	public void addAll(RunTickProvider runTickProvider) {
		checkIfModifiable();
		this.ticks.addAll(runTickProvider.getTicks());
	}


	@Override
	public List<Date> getTicks() {
		compose();
		return Collections.unmodifiableList(ticks);
	}

	public Run build() {
		checkIfModifiable();
		compose();
		return Run.fromRunTicks(this);
	}

	private void compose() {
		if(composed)
			return;

		composed = true;
	}

	protected void createTicks() {
		validate();

		DateTime begin = new DateTime(from);
		DateTime end = null;
		Duration intervalBetweenTicks = null;
		Integer tickCount = null;


		if(to != null)
			end = new DateTime(to);


		ticks.add(begin.toDate());
		for(int i=1; i<=tickCount; ++i) {
			DateTime tick  = begin.plus(intervalBetweenTicks.getMillis() * i);
			ticks.add(tick.toDate());
		}

	}

	private void checkIfModifiable() {
		if(composed)
			throw new IllegalStateException("Composition completed. No further changes are allowed to this composer!");
	}

	private void validate() {
		if(from == null)
			validationFailed("Property 'from' must be defined");

		//TODO tp: fix this: es müssen immer exakt zwei properties definiert sein!
		String onlyOnePropertyAllowedMsg
				= "Exactly one of the properties 'to' or 'count' or 'tickInterval' must be defined.";
		if(to == null && count == null && tickInterval == null)
			validationFailed(onlyOnePropertyAllowedMsg);
		if(!(to != null ^ count != null ^ tickInterval != null))
			validationFailed(onlyOnePropertyAllowedMsg);
	}

	private void validationFailed(String message) {
		throw new IllegalStateException(message);
	}

	public static RunComposer create() {
		return new RunComposer();
	}
}
