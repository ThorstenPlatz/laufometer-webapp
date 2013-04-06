package de.tp82.laufometer.web.core;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import de.tp82.laufometer.web.model.Run;
import de.tp82.laufometer.web.persistence.RunTicks;
import de.tp82.laufometer.web.persistence.dbo.RunDBO;
import de.tp82.laufometer.web.persistence.dbo.RunRepositoryDBOImpl;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Date;
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
	RunRepositoryDBOImpl runRepository;

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

	public List<Run> importTicksAsRuns(List<Date> ticks) {
		Stopwatch importDuration = new Stopwatch();
		importDuration.start();

		if(LOG.isLoggable(Level.INFO))
			LOG.info("Importing " + ticks.size() + " ticks...");

		List<Run> runs = detectRuns(ticks);

		runRepository.store(RunDBO.from(runs));

		importDuration.stop();

		if(LOG.isLoggable(Level.INFO))
			LOG.info("Imported " + runs.size() + " runs in " + importDuration.toString() + ".");

		return Collections.unmodifiableList(runs);

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
				Run run = Run.fromRunTicks(new RunTickContainer(runTicks));
				runs.add(run);

				if(LOG.isLoggable(Level.INFO))
					LOG.info("Detected run: " + run);

				runBegin = tickTime;
				runTicks = Lists.newArrayList();
			}

		}

		return runs;
	}

	private class RunTickContainer implements RunTicks {
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
