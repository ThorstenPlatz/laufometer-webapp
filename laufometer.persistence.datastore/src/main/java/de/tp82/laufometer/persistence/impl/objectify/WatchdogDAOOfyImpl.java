package de.tp82.laufometer.persistence.impl.objectify;

import de.tp82.laufometer.model.watchdog.Watchdog;
import de.tp82.laufometer.persistence.EntityNotFoundException;
import de.tp82.laufometer.persistence.WatchdogDAO;
import de.tp82.laufometer.persistence.impl.objectify.dbo.WatchdogDBO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static de.tp82.laufometer.persistence.impl.objectify.OfyService.ofy;

/**
 * @author Thorsten Platz
 */
@Service
public class WatchdogDAOOfyImpl implements WatchdogDAO {
	private static final Logger LOG = Logger.getLogger(WatchdogDAOOfyImpl.class.getName());

	@Override
	public void save(Watchdog watchdog) {
		ofy().save().entity(WatchdogDBO.from(watchdog)).now();
	}

	@Override
	public Watchdog getWatchdog(String clientId) {
		WatchdogDBO dbo = ofy().load().type(WatchdogDBO.class).id(clientId).get();
		if(dbo == null)
			throw new EntityNotFoundException(clientId);
		return WatchdogDBO.toWatchdog(dbo);
	}

	@Override
	public Iterable<Watchdog> findAllWatchdogs() {
		List<WatchdogDBO> dbos = ofy().load().type(WatchdogDBO.class).list();
		return WatchdogDBO.toWatchdogs(dbos);
	}

	@Override
	public void delete(String clientId) {
		ofy().delete().type(WatchdogDBO.class).id(clientId);
	}
}
