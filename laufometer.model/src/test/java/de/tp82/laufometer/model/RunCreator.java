package de.tp82.laufometer.model;

import com.google.common.collect.Lists;
import de.tp82.laufometer.model.run.SingleRun;
import org.joda.time.Duration;

import java.util.Date;
import java.util.List;

/**
 * @author Thorsten Platz
 */
public class RunCreator {
	// in [m/s]
	private double distancePerTick;

	public RunCreator() {
		this.distancePerTick = 0.6;
	}

	public RunCreator(double distancePerTick) {
		this.distancePerTick = distancePerTick;
	}

	public double getIntervalForSpeedInKmpH(double speed) {
		return getIntervalForSpeedInMpS(speed / 3.6);
	}

	public double getIntervalForSpeedInMpS(double speed) {
		return distancePerTick / speed;
	}

	public SingleRun create(Date begin, double avgSpeedInMpS, double distance) {
		Duration runDuration = new Duration(Math.round(distance/ avgSpeedInMpS));
		return create(begin, avgSpeedInMpS, runDuration);
	}

	public SingleRun create(Date begin, double avgSpeedInMpS, Duration runDuration) {
		Date end = new Date(begin.getTime() + runDuration.getMillis());
		Duration tickInterval = new Duration(Math.round(getIntervalForSpeedInMpS(avgSpeedInMpS) * 1000));
		return create(begin, end, tickInterval);
	}

	public SingleRun create(Date begin, Date end, int tickCount) {
		long first = begin.getTime();
		long last = end.getTime();

		long interval = (last - first) / tickCount;

		return createRun(begin, interval, tickCount);
	}

	public SingleRun create(Date begin, Date end, Duration intervalBetweenTicks) {
		long first = begin.getTime();
		long last = end.getTime();
		long interval = intervalBetweenTicks.getMillis();

		long steps = (last - first) / interval;

		return createRun(begin, interval, (int)steps);
	}

	public SingleRun create(Date begin, Duration intervalBetweenTicks, int tickCount) {
		return createRun(begin, intervalBetweenTicks.getMillis(), tickCount);
	}

	private SingleRun createRun(Date begin, long intervalBetweenTicks, int tickCount) {
		List<Date> ticks = Lists.newArrayList();
		long timestamp = begin.getTime();
		for(int i=0; i< tickCount; ++i) {
			Date tick = new Date(timestamp);
			ticks.add(tick);
			timestamp += intervalBetweenTicks;
		}
		return SingleRun.fromRunTicks(ticks);
	}
}
