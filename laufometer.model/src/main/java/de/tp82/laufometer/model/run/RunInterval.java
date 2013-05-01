package de.tp82.laufometer.model.run;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static de.tp82.laufometer.util.FormattingUtils.DateFormatting.ISO_8601_FORMAT;
import static de.tp82.laufometer.util.FormattingUtils.NumberFormatting.DECIMAL_FORMAT;

/**
 * @author Thorsten Platz
 */
public class RunInterval {
	private final Date intervalBegin;
	private final Date intervalEnd;
	private final double runDuration;
	private final double runDistance;

	public RunInterval(Date intervalBegin, Date intervalEnd, double runDuration, double runDistance) {
		this.intervalBegin = intervalBegin;
		this.intervalEnd = intervalEnd;
		this.runDuration = runDuration;
		this.runDistance = runDistance;
	}

	public Date getIntervalBegin() {
		return intervalBegin;
	}

	public Date getIntervalEnd() {
		return intervalEnd;
	}

	public double getRunDuration() {
		return runDuration;
	}

	public double getRunDistance() {
		return runDistance;
	}

	public double getIntervalLength() {
		long msecs = intervalEnd.getTime() - intervalBegin.getTime();
		return msecs/1000.0;
	}

	public double getAverageSpeed() {
		return getRunDistance() / getRunDuration();
	}

	public static RunInterval from(Iterable<RunInterval> intervals) {
		List<RunInterval> others = Lists.newArrayList(intervals);
		Collections.sort(others, new RunIntervalBeginComparator());

		RunInterval first = others.get(0);
		RunInterval last = others.get(others.size()-1);

		double duration = 0;
		double distance = 0;

		for(RunInterval interval : others) {
			duration += interval.getRunDuration();
			distance += interval.getRunDistance();
		}

		return new RunInterval(first.getIntervalBegin(), last.getIntervalEnd(), duration, distance);
	}

	public static class RunIntervalBeginComparator implements Comparator<RunInterval> {
		@Override
		public int compare(RunInterval o1, RunInterval o2) {
			Preconditions.checkNotNull(o1);
			Preconditions.checkNotNull(o2);

			return o1.getIntervalBegin().compareTo(o2.getIntervalBegin());
		}
	}

	@Override
	public String toString() {
		return "RunInterval{" +
				"intervalBegin=" + ISO_8601_FORMAT.format(intervalBegin) +
				", intervalEnd=" + ISO_8601_FORMAT.format(intervalEnd) +
				", runDuration=" + DECIMAL_FORMAT.format(runDuration) +
				", runDistance=" + DECIMAL_FORMAT.format(runDistance) +
				'}';
	}
}
