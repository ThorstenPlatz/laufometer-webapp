package de.tp82.laufometer.persistence.mock;

import com.google.appengine.repackaged.com.google.common.collect.Maps;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import de.tp82.laufometer.model.run.SingleRun;
import de.tp82.laufometer.persistence.RunDAO;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Thorsten Platz
 */
public class MapBackedRepository implements RunDAO {

	private final static Map<String, SingleRun> storage = Maps.newHashMap();

	@Override
	public Optional<SingleRun> findLatestRun() {
		SingleRun latest = null;
		for(SingleRun run : storage.values()) {
			if(latest == null)
				latest = run;
			else if(run.getBegin().after(latest.getBegin()))
				latest = run;
		}

		return Optional.fromNullable(latest);
	}

	@Override
	public Optional<SingleRun> findOldestRun() {
		SingleRun oldest = null;
		for(SingleRun run : storage.values()) {
			if(oldest == null)
				oldest = run;
			else if(run.getBegin().before(oldest.getBegin()))
				oldest = run;
		}

		return Optional.fromNullable(oldest);
	}

	@Override
	public void save(Set<SingleRun> runs) {
		for(SingleRun run : runs) {
			storage.put(run.getId(), run);
		}
	}

	@Override
	public SingleRun getRun(String runId) {
		SingleRun run = storage.get(runId);
		if(run == null)
			throw new IllegalArgumentException("Run with id='" + runId + "' not found!");
		else
			return run;
	}

	public List<SingleRun> findRuns(Date from, Date to) {
		List<SingleRun> runs = Lists.newArrayList();

		for(SingleRun run : storage.values()) {
			Date begin = run.getBegin();
			Date end = run.getEnd();

			if(end.after(from) && end.before(to)
					|| begin.after(from) && begin.before(to))
				runs.add(run);
		}

		return runs;
	}

}