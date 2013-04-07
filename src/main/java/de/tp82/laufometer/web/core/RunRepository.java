package de.tp82.laufometer.web.core;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import de.tp82.laufometer.web.model.Run;
import de.tp82.laufometer.web.persistence.dbo.RunDBO;
import de.tp82.laufometer.web.persistence.dbo.RunRepositoryDBOImpl;
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
	private RunRepositoryDBOImpl db;

	public Optional<Run> findLatestRun() {
		Optional<RunDBO> dbo = db.findLatestRun();
		if(dbo.isPresent())
			return Optional.of(Run.fromRunTicks(dbo.get()));
		else
			return Optional.absent();
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
		db.store(RunDBO.from(runs));
	}

}
