package de.tp82.laufometer.persistence.mock;

import com.google.appengine.repackaged.com.google.common.collect.Maps;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import de.tp82.laufometer.model.run.Run;
import de.tp82.laufometer.persistence.RunDAO;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Thorsten Platz
 */
public class MapBackedRepository implements RunDAO {

	private final static Map<String, Run> storage = Maps.newHashMap();

	@Override
	public Optional<Run> findLatestRun() {
		Run latest = null;
		for(Run run : storage.values()) {
			if(latest == null)
				latest = run;
			else if(run.getBegin().after(latest.getBegin()))
				latest = run;
		}

		return Optional.fromNullable(latest);
	}

	@Override
	public void save(Set<Run> runs) {
		for(Run run : runs) {
			storage.put(run.getId(), run);
		}
	}

	@Override
	public Run getRun(String runId) {
		Run run = storage.get(runId);
		if(run == null)
			throw new IllegalArgumentException("Run with id='" + runId + "' not found!");
		else
			return run;
	}

	public List<Run> findRuns(Date from, Date to) {
		List<Run> runs = Lists.newArrayList();

		for(Run run : storage.values()) {
			Date begin = run.getBegin();
			Date end = run.getEnd();

			if(end.after(from) && end.before(to)
					|| begin.after(from) && begin.before(to))
				runs.add(run);
		}

		return runs;
	}

}