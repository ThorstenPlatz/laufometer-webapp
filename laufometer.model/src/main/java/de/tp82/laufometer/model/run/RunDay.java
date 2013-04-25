package de.tp82.laufometer.model.run;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.joda.time.LocalDate;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Thorsten Platz
 */
public class RunDay implements Run {
	private Date begin;
	private Date end;

	private Run run;

	private RunDay(RunTickProvider tickProvider, Date day) {
		Preconditions.checkNotNull(tickProvider);
		Preconditions.checkArgument(!tickProvider.getTicks().isEmpty());
		Preconditions.checkNotNull(day);

		begin = new LocalDate(day).toDate();

		LocalDate theEnd = new LocalDate(day);
		theEnd = theEnd.plusDays(1);
		end = new Date(theEnd.toDate().getTime() - 1000);

		List<Date> ticksForDay = Lists.newArrayList();

		for(Date tick : tickProvider.getTicks()) {
			if(tick.after(begin) && tick.before(end))
				ticksForDay.add(tick);
		}

		if(ticksForDay.isEmpty())
			throw new IllegalArgumentException("No valid ticks for given date " + day + " found!");

		this.run = SingleRun.fromRunTicks(ticksForDay);
	}

	@Override
	public Date getBegin() {
		return begin;
	}

	@Override
	public Date getEnd() {
		return end;
	}

	@Override
	public double getDistance() {
		return run.getDistance();
	}

	@Override
	public double getDuration() {
		return run.getDuration();
	}

	@Override
	public double getAverageSpeed() {
		return run.getAverageSpeed();
	}

	@Override
	public List<Date> getTicks() {
		return run.getTicks();
	}

	public static RunDay from(RunTickProvider tickProvider, Date day) {
		return new RunDay(tickProvider, day);
	}

	public static List<RunDay> from(RunTickProvider tickProvider) {
		Preconditions.checkNotNull(tickProvider);
		Preconditions.checkArgument(!tickProvider.getTicks().isEmpty());

		List<RunDay> runDays = Lists.newArrayList();

		List<Date> ticks = tickProvider.getTicks();
		Collections.sort(ticks);

		List<Date> ticksForDay = Lists.newArrayList();

		Date dayBegin = new LocalDate(ticks.get(0)).toDate();
		LocalDate theNextDay = new LocalDate(dayBegin);
		theNextDay = theNextDay.plusDays(1);
		Date nextDayBegin = theNextDay.toDate();

		for(Date tick : ticks) {
			if(tick.after(dayBegin) && tick.before(nextDayBegin))
				ticksForDay.add(tick);
			else {
				RunTickProvider tickzForDay = RunTickProvider.SimpleTickProvider.from(ticksForDay);
				RunDay runDay = RunDay.from(tickzForDay, dayBegin);
				runDays.add(runDay);

				ticksForDay = Lists.newArrayList();
				ticksForDay.add(tick);

				dayBegin = new LocalDate(tick).toDate();

				theNextDay = new LocalDate(dayBegin);
				theNextDay = theNextDay.plusDays(1);
				nextDayBegin = theNextDay.toDate();
			}
		}
		if(!ticksForDay.isEmpty())
			runDays.add(RunDay.from(RunTickProvider.SimpleTickProvider.from(ticksForDay), dayBegin));

		return runDays;
	}
}
