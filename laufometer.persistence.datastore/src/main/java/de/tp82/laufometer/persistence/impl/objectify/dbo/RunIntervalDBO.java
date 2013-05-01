package de.tp82.laufometer.persistence.impl.objectify.dbo;

import com.google.common.collect.Lists;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import de.tp82.laufometer.model.run.RunInterval;

import java.util.Date;
import java.util.List;

/**
 * @author Thorsten Platz
 */
@Entity
public class RunIntervalDBO {
	@Id
	private String id;

	@Index
	private Date intervalBegin;

	@Index
	private Date intervalEnd;

	private double runDuration;

	private double runDistance;

	private RunIntervalDBO() {}

	private RunIntervalDBO(RunInterval runInterval) {
		intervalBegin = runInterval.getIntervalBegin();
		intervalEnd = runInterval.getIntervalEnd();
		runDistance = runInterval.getRunDistance();
		runDuration = runInterval.getRunDuration();

		String hexedBeginTime = Long.toHexString(intervalBegin.getTime());
		id = "interval:" + hexedBeginTime;
	}

	public String getId() {
		return id;
	}

	public Date getIntervalBegin() {
		return intervalBegin;
	}

	public Date getIntervalEnd() {
		return intervalEnd;
	}

	public double getRunDistance() {
		return runDistance;
	}

	public double getRunDuration() {
		return runDuration;
	}

	public static RunInterval toRunInterval(RunIntervalDBO dbo) {
		return new RunInterval(dbo.getIntervalBegin(), dbo.getIntervalEnd(),
				dbo.getRunDuration(), dbo.getRunDistance());
	}

	public static List<RunInterval> toRunIntervals(Iterable<RunIntervalDBO> dbos) {
		List<RunInterval> runIntervals = Lists.newArrayList();
		for(RunIntervalDBO dbo : dbos)
			runIntervals.add(toRunInterval(dbo));
		return runIntervals;
	}

	public static RunIntervalDBO from(RunInterval runInterval) {
		return new RunIntervalDBO(runInterval);
	}

	public static List<RunIntervalDBO> from(Iterable<RunInterval> runIntervals) {
		List<RunIntervalDBO> dbos = Lists.newArrayList();
		for(RunInterval runInterval : runIntervals)
			dbos.add(from(runInterval));
		return dbos;
	}
}
