package de.tp82.laufometer.core;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import de.tp82.laufometer.model.run.SingleRun;
import de.tp82.laufometer.persistence.RunDAO;
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
	private RunDAO db;

	public Optional<SingleRun> findOldestRun() {
		return db.findOldestRun();
	}

	public Optional<SingleRun> findLatestRun() {
		return db.findLatestRun();
	}

	public List<SingleRun> findRuns(Date from, Optional<Date> to) {
		Preconditions.checkNotNull(from);
		Preconditions.checkNotNull(to);

		Date until = to.isPresent() ? to.get() : new Date();
		Preconditions.checkArgument(from.before(until), "from must be before to");

		return db.findRuns(from, until);
	}

	public void store(Set<SingleRun> runs) {
		db.save(runs);
	}

}
