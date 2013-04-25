package de.tp82.laufometer.persistence.impl.objectify;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import de.tp82.laufometer.model.run.Run;
import de.tp82.laufometer.persistence.EntityNotFoundException;
import de.tp82.laufometer.persistence.RunDAO;
import de.tp82.laufometer.persistence.impl.objectify.dbo.RunDBO;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static de.tp82.laufometer.persistence.impl.objectify.OfyService.ofy;
/**
 * @author Thorsten Platz
 */
@Service
public class RunDAOOfyImpl implements RunDAO {
	private static final Logger LOG = Logger.getLogger(RunDAOOfyImpl.class.getName());

	@Override
	public Optional<Run> findLatestRun() {
		List<RunDBO> runs = ofy().load().type(RunDBO.class).limit(1).order("-begin").list();

		if(runs.isEmpty())
			return Optional.absent();
		else
			return Optional.of(RunDBO.toRun(runs.get(0)));
	}

	@Override
	public Optional<Run> findOldestRun() {
		List<RunDBO> runs = ofy().load().type(RunDBO.class).limit(1).order("begin").list();

		if(runs.isEmpty())
			return Optional.absent();
		else
			return Optional.of(RunDBO.toRun(runs.get(0)));
	}

	@Override
	public void save(Set<Run> runs) {
		if(LOG.isLoggable(Level.INFO))
			LOG.info("Storing runs: " + runs);

		Iterable<RunDBO> dbos = RunDBO.from(runs);
		ofy().save().entities(dbos);
	}

	@Override
	public Run getRun(String runId) {
		RunDBO dbo = ofy().load().type(RunDBO.class).id(runId).get();
		if(dbo == null)
			throw new EntityNotFoundException(runId);

		return RunDBO.toRun(dbo);
	}

	@Override
	public List<Run> findRuns(Date from, Date to) {

		// find runs that begin in the given interval
		Query<RunDBO> beginningRuns = ofy().load()
				.type(RunDBO.class)
				.filter("begin >= ", from)
				.filter("begin <= ", to);
		Set<Key<RunDBO>> beginningRunKeys = Sets.newHashSet(beginningRuns.keys());

		// find runs that end in the given interval
		Query<RunDBO> endingRuns = ofy().load()
				.type(RunDBO.class)
				.filter("end >= ", from)
				.filter("end <= ", to);
		Set<Key<RunDBO>> endingRunKeys = Sets.newHashSet(endingRuns.keys());

		// combine beginning and ending runs and eliminate duplicates
		Set<Key<RunDBO>> keys = Sets.newHashSet(beginningRunKeys);
		keys.addAll(endingRunKeys);

		// load and return entities for the previous filtered keys
		Map<Key<RunDBO>, RunDBO> results = ofy().load().keys(keys);
		List<Run> runs = Lists.newArrayList(RunDBO.toRuns(results.values()));

		Collections.sort(runs, new Run.RunBeginComparator());

		for(Run run : runs) {
			Preconditions.checkArgument(!run.getEnd().before(from));
			Preconditions.checkArgument(!run.getBegin().after(to) );
		}

		return runs;
	}

	@Override
	public void delete(Set<Run> runs) {
		Iterable<RunDBO> dbos = RunDBO.from(runs);
		ofy().delete().entities(dbos);
	}
}
