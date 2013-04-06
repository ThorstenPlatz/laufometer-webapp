package de.tp82.laufometer.web.persistence;

import java.util.Date;
import java.util.List;

/**
 * Provides all the ticks (timestamps) for a single run.
 *
 * @author Thorsten Platz
 */
public interface RunTicks {

	/**
	 * Returns the ticks for the run in time ascending order.
	 * @return non-null and non-empty list of sorted ticks
	 */
	List<Date> getTicks();
}
