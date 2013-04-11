package de.tp82.laufometer.persistence;

import de.tp82.laufometer.model.watchdog.Watchdog;

/**
 * @author Thorsten Platz
 */
public interface WatchdogDAO {

	void save(Watchdog watchdog);

	Watchdog getWatchdog(String clientId);

	Iterable<Watchdog> findAllWatchdogs();

	void delete(String clientId);
}
