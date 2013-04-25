package de.tp82.laufometer.model.run;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Representation of a single run.
 *
 * @author Thorsten Platz
 */
public class SingleRun implements RunTickProvider, Run {
	//TODO tp: make configurable
	/**
	 * Distance per tick in meters [m]; this equates to the wheels inner circumference.
	 */
	private final double DISTANCE_PER_TICK = 0.5969;

	private final String id;

	private final List<Date> ticks;
	private final int firstTick;
	private final int lastTick;

	private SingleRun(List<Date> runTicks)  {
		Preconditions.checkNotNull(runTicks);
		Preconditions.checkArgument(!runTicks.isEmpty());

		ticks = Lists.newArrayList(runTicks);
		firstTick = 0;
		lastTick = ticks.size() - 1;

		long beginTime = getBegin().getTime();
		String hexedBeginTime = Long.toHexString(beginTime);
		id = "Run:" + hexedBeginTime;
	}

	@Override
	public Date getBegin() {
		return ticks.get(firstTick);
	}

	@Override
	public Date getEnd() {
		return ticks.get(lastTick);
	}

	@Override
	public List<Date> getTicks() {
		return Collections.unmodifiableList(ticks);
	}

	public String getId() {
		return id;
	}

	/**
	 * Returns the distance in meters [m].
	 * @return distance of this run
	 */
	@Override
	public double getDistance() {
		return ticks.size()-1 * DISTANCE_PER_TICK;
	}

	/**
	 * Returns the duration in seconds [s].
	 * @return duration of this run
	 */
	@Override
	public double getDuration() {
		DateTime begin = new DateTime(getBegin());
		DateTime end = new DateTime(getEnd());
		Interval interval = new Interval(begin, end);
		return interval.toDuration().getMillis() / 1000.0;
	}

	/**
	 * Returns the average speed in meters per second [m/s]
	 * @return the average speed of this run
	 */
	@Override
	public double getAverageSpeed() {
		return getDistance() / getDuration();
	}

	@Override
	public String toString() {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String begin = dateFormatter.format(getBegin());
		String end = dateFormatter.format(getEnd());

		DecimalFormat numberFormatter = new DecimalFormat( "###.0" );
		String dur = numberFormatter.format(getDuration());
		String dist = numberFormatter.format(getDistance());
		return "Run{begin=" + begin + ", end=" + end + ", dist=" + dist + " [m], duration=" + dur + " [s]}";
	}

	public static SingleRun fromRunTicks(RunTickProvider tickProvider) {
		return new SingleRun(tickProvider.getTicks());
	}

	public static SingleRun fromRunTicks(List<Date> ticks) {
		return new SingleRun(ticks);
	}


}
