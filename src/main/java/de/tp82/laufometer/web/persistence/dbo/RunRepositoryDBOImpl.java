package de.tp82.laufometer.web.persistence.dbo;

import com.google.common.base.Preconditions;
import com.google.common.base.Optional;
import org.springframework.stereotype.Service;

import javax.jdo.PersistenceManager;
import java.util.Date;
import java.util.List;

/**
 * @author Thorsten Platz
 */
@Service
public class RunRepositoryDBOImpl {

	/**
	 * Returns all runs that occurred in the specified interval.
	 * Runs that are partially contained in the interval are completely included.
	 * <br/><br/>
	 * Example:<br/>
	 * <ul>
	 * <li>IB, IE: interval begin, interval end</li>
	 * <li>RB, RE: run begin, run end</li>
	 * </ul>
	 * <pre>
	 *     --------------------------------------> time
	 *           IB_______________________IE
	 *       RB_______RE   RB_____RE  RB_____RE
	 *
	 * </pre>
	 * This will result in a list of three runs. The first run begins before the interval
	 * and the last ends after the interval.
	 *
	 * @param from Begin of the interval.
	 * @param to End of the interval. If end is not specified, the current time is used.
	 * @return runs in time ascending order
	 */
	public List<RunDBO> getRuns(Date from, Optional<Date> to) {
		Preconditions.checkNotNull(from);
		Preconditions.checkNotNull(to);

		Date until = to.isPresent() ? to.get() : new Date();
		Preconditions.checkArgument(from.before(until), "from must be before to");

		//TODO tp: implement
		throw new IllegalStateException("Not yet implemented!");
	}

	/**
	 * Stores the given run in the repository.
	 * @param runs non-null list of runs.
	 */
	public void store(List<RunDBO> runs) {
		Preconditions.checkNotNull(runs);

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			for(RunDBO run : runs) {
				pm.makePersistent(run);
			}
		} finally {
			pm.close();
		}
	}
}
