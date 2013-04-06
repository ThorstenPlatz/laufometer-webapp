package de.tp82.laufometer.web.model;

import com.google.appengine.repackaged.com.google.common.base.Preconditions;
import de.tp82.laufometer.web.persistence.RunTicks;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Representation of a single run.
 *
 * @author Thorsten Platz
 */
public class Run {
	//TODO tp: make configurable
	/**
	 * Distance per tick in meters [m]; this equates to the wheels inner circumference.
	 */
	private static final double DISTANCE_PER_TICK = 0.5969;

	private List<Date> ticks;
	private int first;
	private int last;

	private Run(RunTicks runTicks) {
		Preconditions.checkNotNull(runTicks);

		ticks = runTicks.getTicks();
		first = 0;
		last = ticks.size() - 1;
	}

	public Date getBegin() {
		return ticks.get(first);
	}

	public Date getEnd() {
		return ticks.get(last);
	}

	public List<Date> getTicks() {
		return ticks;
	}

	/**
	 * Returns the distance in meters [m].
	 * @return distance of this run
	 */
	public double getDistance() {
		return ticks.size()-1 * DISTANCE_PER_TICK;
	}

	/**
	 * Returns the duration in seconds [s].
	 * @return duration of this run
	 */
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
		return "Run(begin=" + begin + ", end=" + end + ", dist=" + dist + " [m], duration=" + dur + " [s])";
	}

	public static Run fromRunTicks(RunTicks ticks) {
		return new Run(ticks);
	}
}
