package de.tp82.laufometer.persistence.mock;

import com.google.appengine.repackaged.com.google.common.collect.Maps;
import com.google.common.base.Optional;
import de.tp82.laufometer.RunDAO;
import de.tp82.laufometer.model.Run;

import java.util.List;
import java.util.Map;

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
	public void save(List<Run> runs) {
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
}
