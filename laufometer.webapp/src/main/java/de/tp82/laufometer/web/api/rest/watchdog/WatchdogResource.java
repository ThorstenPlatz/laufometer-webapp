package de.tp82.laufometer.web.api.rest.watchdog;

import com.google.appengine.repackaged.com.google.common.base.Preconditions;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import de.tp82.laufometer.core.WatchdogService;
import de.tp82.laufometer.model.watchdog.Watchdog;
import de.tp82.laufometer.persistence.WatchdogDAO;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Thorsten Platz
 */
public class WatchdogResource {
	private static final Logger LOG = Logger.getLogger(WatchdogResource.class.getName());

	@Autowired
	protected WatchdogDAO watchdogDAO;

	@Autowired
	protected WatchdogService watchdogService;

	protected void registerKeepaliveInternal(String clientId) {
		Preconditions.checkNotNull(Strings.emptyToNull(clientId), "Parameter clientId is mandatory!");

		if(LOG.isLoggable(Level.FINE)) {
			LOG.fine("Received keepalive ping from clientId='" + clientId + "'");
		}

		Watchdog watchdog = watchdogDAO.getWatchdog(clientId);

		Optional<Date> pingReceived = Optional.of(DateTime.now().toDate());
		watchdog.setLastPing(pingReceived);
		watchdog.setLastCheck(pingReceived);
		watchdog.setLastCheckResult(Optional.of(true));

		watchdogDAO.save(watchdog);
	}
}
