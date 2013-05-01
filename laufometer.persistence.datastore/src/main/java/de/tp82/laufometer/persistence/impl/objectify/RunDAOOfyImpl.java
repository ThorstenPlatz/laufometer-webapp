package de.tp82.laufometer.persistence.impl.objectify;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import de.tp82.laufometer.model.run.RunInterval;
import de.tp82.laufometer.persistence.RunIntervalDAO;
import de.tp82.laufometer.persistence.impl.objectify.dbo.RunIntervalDBO;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static de.tp82.laufometer.persistence.impl.objectify.OfyService.ofy;
/**
 * @author Thorsten Platz
 */
@Service
public class RunDAOOfyImpl implements RunIntervalDAO {
	private static final Logger LOG = Logger.getLogger(RunDAOOfyImpl.class.getName());

	@Override
	public Optional<RunInterval> findLatestRun() {
		List<RunIntervalDBO> runs = ofy().load().type(RunIntervalDBO.class).limit(1).order("-intervalBegin").list();

		if(runs.isEmpty())
			return Optional.absent();
		else
			return Optional.of(RunIntervalDBO.toRunInterval(runs.get(0)));
	}

	@Override
	public Optional<RunInterval> findOldestRun() {
		List<RunIntervalDBO> runs = ofy().load().type(RunIntervalDBO.class).limit(1).order("intervalBegin").list();

		if(runs.isEmpty())
			return Optional.absent();
		else
			return Optional.of(RunIntervalDBO.toRunInterval(runs.get(0)));
	}

	@Override
	public void save(Set<RunInterval> runs) {
		if(LOG.isLoggable(Level.INFO))
			LOG.info("Storing runs: " + runs);

		Iterable<RunIntervalDBO> dbos = RunIntervalDBO.from(runs);
		ofy().save().entities(dbos);
	}

	@Override
	public List<RunInterval> findRuns(Date from, Date to) {

		// find runs that begin in the given interval
		Query<RunIntervalDBO> beginningRuns = ofy().load()
				.type(RunIntervalDBO.class)
				.filter("begin >= ", from)
				.filter("begin <= ", to);
		Set<Key<RunIntervalDBO>> beginningRunKeys = Sets.newHashSet(beginningRuns.keys());

		// find runs that end in the given interval
		Query<RunIntervalDBO> endingRuns = ofy().load()
				.type(RunIntervalDBO.class)
				.filter("intervalEnd >= ", from)
				.filter("intervalEnd <= ", to);
		Set<Key<RunIntervalDBO>> endingRunKeys = Sets.newHashSet(endingRuns.keys());

		// combine beginning and ending runs and eliminate duplicates
		Set<Key<RunIntervalDBO>> keys = Sets.newHashSet(beginningRunKeys);
		keys.addAll(endingRunKeys);

		// load and return entities for the previous filtered keys
		Map<Key<RunIntervalDBO>, RunIntervalDBO> results = ofy().load().keys(keys);
		List<RunInterval> runs = Lists.newArrayList(RunIntervalDBO.toRunIntervals(results.values()));

		Collections.sort(runs, new RunInterval.RunIntervalBeginComparator());

		for(RunInterval run : runs) {
			Preconditions.checkArgument(!run.getIntervalEnd().before(from));
			Preconditions.checkArgument(!run.getIntervalBegin().after(to) );
		}

		return runs;
	}

	@Override
	public void delete(Set<RunInterval> runs) {
		Iterable<RunIntervalDBO> dbos = RunIntervalDBO.from(runs);
		ofy().delete().entities(dbos);
	}
}
