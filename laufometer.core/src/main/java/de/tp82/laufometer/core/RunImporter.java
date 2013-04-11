package de.tp82.laufometer.core;

import com.google.common.collect.Sets;
import com.google.common.base.Optional;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import de.tp82.laufometer.model.run.Run;
import de.tp82.laufometer.model.run.RunTickProvider;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Thorsten Platz
 */
@Service
public class RunImporter {
	private static final Logger LOG = Logger.getLogger(RunImporter.class.getName());

	@Value(value = "${laufometer.import.run.distancePerTick}")
	private double distancePerTick;

	@Value(value = "${laufometer.import.run.minSpeed}")
	private double minSpeed;

	@Value(value = "${laufometer.import.run.maxSpeed}")
	private double maxSpeed;

	private double minTickInterval;
	private double maxTickInterval;

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
					+ "- maxTickInterval=" + maxTickInterval);
	}

	public List<Run> importTicksAsRuns(List<Date> ticks, boolean skipKnownTicks) {
		Stopwatch importDuration = new Stopwatch();
		importDuration.start();

		if(LOG.isLoggable(Level.INFO))
			LOG.info("Importing " + ticks.size() + " ticks...");

		if(skipKnownTicks)
			skipKnownTicks(ticks);
		List<Run> runs = detectRuns(ticks);

		runRepository.store(Sets.newHashSet(runs));

		importDuration.stop();

		if(LOG.isLoggable(Level.INFO))
			LOG.info("Imported " + runs.size() + " runs in " + importDuration.toString() + ".");

		return Collections.unmodifiableList(runs);

	}

	/**
	 * Remove ticks from the beginning of the list if they are older than the beginning of the
	 * last known run. Then they are already imported.
	 * @param ticks ticks to import
	 */
	private void skipKnownTicks(List<Date> ticks) {
		Optional<Run> latestRun = runRepository.findLatestRun();
		if(latestRun.isPresent()) {
			int initialTicks = ticks.size();

			Iterator<Date> itr = ticks.listIterator();
			while(itr.hasNext()) {
				Date tick = itr.next();
				if(tick.before(latestRun.get().getBegin()))
					itr.remove();
				else
					break;
			}
			if(LOG.isLoggable(Level.FINE))
				LOG.fine("Skipped " + (ticks.size() - initialTicks) + " ticks before latest run: " + latestRun);
		} else {
			if(LOG.isLoggable(Level.FINE))
				LOG.fine("No previous run exists in the repository.");
		}
	}

	private List<Run> detectRuns(List<Date> ticks) {
		List<Run> runs = Lists.newArrayList();

		long maxTickDistanceMillis = Math.round(maxTickInterval * 1000);
		Duration maxTickDistance = new Duration(maxTickDistanceMillis);

		DateTime runBegin = new DateTime(ticks.get(0));
		List<Date> runTicks = Lists.newArrayList();
		for(Date tick : ticks) {

			DateTime tickTime = new DateTime(tick);
			DateTime latestNextTick = runBegin.plus(maxTickDistance);

			if(tickTime.isBefore(latestNextTick))
				runTicks.add(tick);
			else {
				Run run = createRun(runTicks);
				runs.add(run);

				runBegin = tickTime;
				runTicks = Lists.newArrayList();
			}
		}
		if(!runTicks.isEmpty()) {
			Run run = createRun(runTicks);
			runs.add(run);
		}

		return runs;
	}

	private Run createRun(List<Date> runTicks) {
		Run run = Run.fromRunTicks(new RunTickContainer(runTicks));

		if(LOG.isLoggable(Level.INFO))
			LOG.info("Detected run: " + run);
		return run;
	}

	private class RunTickContainer implements RunTickProvider {
		private List<Date> runTicks;

		private RunTickContainer(List<Date> runTicks) {
			this.runTicks = runTicks;
		}

		@Override
		public List<Date> getTicks() {
			return runTicks;
		}
	}
}
