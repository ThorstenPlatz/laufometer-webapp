package de.tp82.laufometer.web.core;

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
public class RunImporter {
	@Autowired
	RunRepositoryDBOImpl runRepository;

	public void importTicksAsRuns(List<Date> ticks) {
		List<Run> runs = detectRuns(ticks);
		runRepository.store(RunDBO.from(runs));

	}

	private List<Run> detectRuns(List<Date> ticks) {
		throw new IllegalStateException("Not yet implemented.");
	}
}
