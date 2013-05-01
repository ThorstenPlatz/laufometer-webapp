package de.tp82.laufometer.web.api.rest.watchdog;

import com.google.appengine.repackaged.com.google.common.base.Preconditions;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import de.tp82.laufometer.core.importer.WatchdogService;
import de.tp82.laufometer.model.watchdog.Watchdog;
import de.tp82.laufometer.persistence.WatchdogDAO;
import de.tp82.laufometer.util.ExceptionHandling;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Thorsten Platz
 */
@Service
@Produces(MediaType.APPLICATION_JSON)
@Path("/{audience}/watchdog")
public class WatchdogResource {
	private static final Logger LOG = Logger.getLogger(WatchdogResource.class.getName());

	@Autowired
	protected WatchdogDAO watchdogDAO;

	@Autowired
	protected WatchdogService watchdogService;

	@GET
	@Path("/check/")
	public Set<Watchdog> checkAll() {
		return watchdogService.check();
	}

	@GET
	@Path("/check/{clientId}")
	public Response check(@PathParam("clientId") String clientId) {
		final Watchdog watchdog = watchdogDAO.getWatchdog(clientId);
		watchdogService.check(watchdog);

		return Response
				.ok()
				.entity(new Object() {
							boolean isAlive = watchdog.isAlive();
						})
				.build();
	}

	@GET
	@Path("/keepalive/{clientId}")
	public Response registerKeepalive(@PathParam("clientId") String clientId) {
		try {
			registerKeepaliveInternal(clientId);
			return Response.ok().build();
		} catch(Exception exc) {
			LOG.warning("Error during keepalive registration: " + exc + ". Stacktrace follows:\n"
					+ ExceptionHandling.getStacktrace(exc));
			return Response.serverError().entity("Error registering keepalive: " + exc).build();
		}
	}


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
