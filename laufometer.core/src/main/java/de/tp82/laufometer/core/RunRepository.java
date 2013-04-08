package de.tp82.laufometer.core;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import de.tp82.laufometer.RunDAO;
import de.tp82.laufometer.model.Run;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Thorsten Platz
 */
@Service
public class RunRepository {
	@Autowired
	private RunDAO db;

	public Optional<Run> findLatestRun() {
		return db.findLatestRun();
	}

	public List<Run> findRuns(Date from, Optional<Date> to) {
		Preconditions.checkNotNull(from);
		Preconditions.checkNotNull(to);

		Date until = to.isPresent() ? to.get() : new Date();
		Preconditions.checkArgument(from.before(until), "from must be before to");

		//TODO tp: implement
		throw new IllegalStateException("Not yet implemented!");
	}

	public void store(List<Run> runs) {
		db.save(runs);
	}

}
