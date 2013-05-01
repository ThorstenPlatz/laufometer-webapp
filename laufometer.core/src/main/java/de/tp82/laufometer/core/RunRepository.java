package de.tp82.laufometer.core;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import de.tp82.laufometer.model.run.RunInterval;
import de.tp82.laufometer.persistence.RunIntervalDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Thorsten Platz
 */
@Service
public class RunRepository {
	@Autowired
	private RunIntervalDAO db;

	public Optional<RunInterval> findOldestRun() {
		return db.findOldestRun();
	}

	public Optional<RunInterval> findLatestRun() {
		return db.findLatestRun();
	}

	public List<RunInterval> findRuns(Date from, Optional<Date> to) {
		Preconditions.checkNotNull(from);
		Preconditions.checkNotNull(to);

		Date until = to.isPresent() ? to.get() : new Date();
		Preconditions.checkArgument(from.before(until), "from must be before to");

		return db.findRuns(from, until);
	}

	public void store(Set<RunInterval> runs) {
		db.save(runs);
	}

	public int delete(Date from, Date to) {
		List<RunInterval> runsToDelete = findRuns(from, Optional.of(to));
		db.delete(Sets.newHashSet(runsToDelete));
		return runsToDelete.size();
	}

}
