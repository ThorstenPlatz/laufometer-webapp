package de.tp82.laufometer.core.importer;

import com.google.common.base.Optional;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import de.tp82.laufometer.core.RunRepository;
import de.tp82.laufometer.model.run.RunInterval;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Thorsten Platz
 */
@Service
public class RunImporter {
	private static final Logger LOG = Logger.getLogger(RunImporter.class.getName());

	@Value("${laufometer.import.run.distancePerTick}")
	private double distancePerTick;

	@Value("${laufometer.import.run.minSpeed}")
	private double minSpeed;

	@Value("${laufometer.import.run.maxSpeed}")
	private double maxSpeed;

	private double minTickInterval;
	private double maxTickInterval;

	@Value("${laufometer.import.run.interval.length}")
	private double intervalLength;

	@Autowired
	RunRepository runRepository;

	@PostConstruct
	private void init() {
		minTickInterval = distancePerTick/maxSpeed;
		maxTickInterval = distancePerTick/minSpeed;

		if(LOG.isLoggable(Level.INFO))
			LOG.info("Initialized using \n"
					+ "- distancePerTick=" + distancePerTick + "\n"
					+ "- minSpeed=" + minSpeed + "\n"
					+ "- maxSpeed=" + maxSpeed + "\n"
					+ "- minTickInterval=" + minTickInterval + "\n"
					+ "- maxTickInterval=" + maxTickInterval + "\n"
					+ "- interval length=" + intervalLength);
	}

	public List<RunInterval> importTicksAsRuns(List<Date> ticks, boolean skipKnownTicks) {
		Stopwatch importDuration = new Stopwatch();
		importDuration.start();

		if(LOG.isLoggable(Level.INFO))
			LOG.info("Importing " + ticks.size() + " ticks...");

		preprocess(ticks);

		// filter out ticks from the past
		if(skipKnownTicks)
			skipKnownTicks(ticks);

		List<RunInterval> intervals;
		if(ticks.isEmpty())
			intervals = Collections.emptyList();
		else
			intervals = detectIntervals(ticks);

		if(!intervals.isEmpty())
			runRepository.store(Sets.newHashSet(intervals));

		importDuration.stop();

		if(LOG.isLoggable(Level.INFO))
			LOG.info("Imported " + intervals.size() + " runs in " + importDuration.elapsed(TimeUnit.SECONDS) + " seconds.");

		return intervals;

	}

	private void preprocess(List<Date> ticks) {
		Collections.sort(ticks);
	}

	/**
	 * Remove ticks from the beginning of the list if they are older than the beginning of the
	 * last known run. Then they are already imported.
	 * @param ticks ticks to import
	 */
	private void skipKnownTicks(List<Date> ticks) {
		Optional<RunInterval> latestRun = runRepository.findLatestRun();
		if(latestRun.isPresent()) {
			int initialTicks = ticks.size();

			Iterator<Date> itr = ticks.listIterator();
			while(itr.hasNext()) {
				Date tick = itr.next();
				if(tick.before(latestRun.get().getIntervalEnd()))
					itr.remove();
				else
					break;
			}
			if(LOG.isLoggable(Level.INFO))
				LOG.info("Skipped " + (initialTicks - ticks.size()) + " ticks before latest run: " + latestRun.get());
		} else {
			if(LOG.isLoggable(Level.INFO))
				LOG.info("No previous run exists in the repository.");
		}
	}

	private List<RunInterval> detectIntervals(List<Date> ticks) {
		Date firstTick = ticks.get(0);
		LocalDate firstMidnight = new LocalDate(firstTick);

		Date lastTick = ticks.get(ticks.size()-1);
		LocalDate lastMidnight = new LocalDate(lastTick).plusDays(1);

		Interval timeSpan = new Interval(firstMidnight.toDate().getTime(), lastMidnight.toDate().getTime());
		long numDaysInImport = timeSpan.toDuration().getStandardDays();

		long ticksFirstDayBegin = firstMidnight.toDate().getTime();
		long intervalSize = (long) this.intervalLength * 1000;
		long numIntervalsPerDay = 1000 * 60 * 60 * 24 / intervalSize;

		int numTotalIntervals = (int) (numDaysInImport * numIntervalsPerDay);

		// init interval containers
		List<List<Date>> ticksPerInterval = Lists.newArrayListWithCapacity(numTotalIntervals);
		for(int i=0; i<numTotalIntervals; ++i) {
			List<Date> intervalTicks = Lists.newArrayList();
			ticksPerInterval.add(intervalTicks);
		}

		// sort every tick into an interval container
		for(Date tick : ticks) {
			long tickTime = tick.getTime();

			int intervalIndex = (int) ((tickTime - ticksFirstDayBegin) / intervalSize);

			List<Date> intervalTicks = ticksPerInterval.get(intervalIndex);
			intervalTicks.add(tick);
		}

		// create RunIntervals from each non empty container
		List<RunInterval> intervals = Lists.newArrayList();
		for(int i=0; i<numTotalIntervals; ++i) {
			List<Date> intervalTicks = ticksPerInterval.get(i);
			if(intervalTicks.isEmpty())
				continue;


			long intervalBegin = ticksFirstDayBegin + i*intervalSize;
			long intervalEnd = ticksFirstDayBegin + (i+1)*intervalSize - 1;

			// determine run distance & duration
			double distance = 0;
			double duration = 0;
			List<RunInterval> singleRuns = detectSingleRuns(intervalTicks);
			for(RunInterval run : singleRuns) {
				distance += run.getRunDistance();
				duration += run.getRunDuration();
			}

			intervals.add(
					new RunInterval(new Date(intervalBegin), new Date(intervalEnd), duration, distance));
		}

		return intervals;
	}



	private List<RunInterval> detectSingleRuns(List<Date> ticks) {
		List<RunInterval> runs = Lists.newArrayList();

		long maxTickDistanceMillis = Math.round(maxTickInterval * 1000);
		Duration maxTickDistance = new Duration(maxTickDistanceMillis);

		DateTime runBegin = new DateTime(ticks.get(0));
		DateTime latestNextTick = runBegin.plus(maxTickDistance);
		List<Date> runTicks = Lists.newArrayList();
		for(Date tick : ticks) {
			DateTime tickTime = new DateTime(tick);

			if(tickTime.isBefore(latestNextTick)) {
				runTicks.add(tick);
				latestNextTick = tickTime.plus(maxTickDistance);
			} else {
				if(!runTicks.isEmpty()) {
					RunInterval run = createRun(runTicks);
					runs.add(run);
				}

				// reset to collect a new run
				runBegin = tickTime;
				latestNextTick = runBegin.plus(maxTickDistance);
				runTicks = Lists.newArrayList();
			}
		}
		if(!runTicks.isEmpty()) {
			RunInterval run = createRun(runTicks);
			runs.add(run);
		}

		return runs;
	}

	private RunInterval createRun(List<Date> runTicks) {
		Date firstTick = runTicks.get(0);
		Date lastTick = runTicks.get(runTicks.size()-1);

		long durationMsecs = lastTick.getTime() - firstTick.getTime();
		double distance = this.distancePerTick * runTicks.size();

		RunInterval run = new RunInterval(firstTick, lastTick, durationMsecs/1000.0, distance);

		if(LOG.isLoggable(Level.INFO))
			LOG.info("Detected run: " + run);
		return run;
	}
}
