package de.tp82.laufometer;

import com.google.common.base.Optional;
import de.tp82.laufometer.model.Run;

import java.util.List;

/**
 * DAO for accessing runs.
 *
 * @author Thorsten Platz
 */
public interface RunDAO {

	/**
	 * Finds the latest run in the data repository.
	 *
	 * @return the latest run if at least one run was already stored in the data repository
	 */
	 Optional<Run> findLatestRun();

	/**
	 * Stores the given Runs in the data repository. Store multiple runs at once if possible,
	 * to increase performance.
	 *
	 * @param runs the runs to be persisted
	 */
	void save(List<Run> runs);

	/**
	 * Returns the run for the given ID.
	 *
	 * @param runId the run's ID
	 *
	 * @return the run identified by the given ID
	 *
	 * @throws IllegalArgumentException if the given ID does not exist
	 */
	Run getRun(String runId);

}
