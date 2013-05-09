package de.tp82.laufometer.model.run;

import com.google.common.collect.Lists;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Thorsten Platz
 */
public class RunIntervalGrouper {
	private long intervalLength;

	public RunIntervalGrouper(long intervalLength) {
		this.intervalLength = intervalLength;
	}


	public List<RunInterval> group(List<RunInterval> runs) {
		if(runs.isEmpty())
			return Collections.emptyList();

		Collections.sort(runs, new RunInterval.RunIntervalBeginComparator());

		// analyze runs to import and find out how many interval groups we'll get
		LocalDate firstMidnight = new LocalDate(runs.get(0).getIntervalBegin());
		LocalDate lastMidnight = new LocalDate(runs.get(runs.size()-1).getIntervalEnd()).plusDays(1);

		Interval timeSpan = new Interval(firstMidnight.toDate().getTime(), lastMidnight.toDate().getTime());
		long numDaysInImport = timeSpan.toDuration().getStandardDays();

		long firstDayBegin = firstMidnight.toDate().getTime();
		long intervalSize = intervalLength * 1000;
		long numIntervalsPerDay = 1000 * 60 * 60 * 24 / intervalSize;

		int numTotalIntervals = (int) (numDaysInImport * numIntervalsPerDay);

		// init interval containers
		List<List<RunInterval>> runsPerInterval = Lists.newArrayListWithCapacity(numTotalIntervals);
		for(int i=0; i<numTotalIntervals; ++i) {
			List<RunInterval> intervalRuns = Lists.newArrayList();
			runsPerInterval.add(intervalRuns);
		}

		// sort every tick into an interval container
		for(RunInterval run : runs) {
			long tickTime = run.getIntervalBegin().getTime();

			int intervalIndex = (int) ((tickTime - firstDayBegin) / intervalSize);
			if(intervalIndex < 0 || intervalIndex >= numTotalIntervals)
				continue;

			List<RunInterval> intervalRuns = runsPerInterval.get(intervalIndex);
			intervalRuns.add(run);
		}

		// create RunIntervals from each non empty container
		List<RunInterval> intervals = Lists.newArrayList();
		for(int i=0; i<numTotalIntervals; ++i) {
			List<RunInterval> intervalRuns = runsPerInterval.get(i);
			if(intervalRuns.isEmpty())
				continue;


			long intervalBegin = firstDayBegin + i*intervalSize;
			long intervalEnd = firstDayBegin + (i+1)*intervalSize - 1;
			RunInterval combinedRuns = RunInterval.from(intervalRuns);
			RunInterval intervalGroup = new RunInterval(new Date(intervalBegin), new Date(intervalEnd),
					combinedRuns.getRunDuration(), combinedRuns.getRunDistance());

			intervals.add(intervalGroup);
		}

		return intervals;
	}
}
