package de.tp82.laufometer.web.api.rest.watchdog;

import de.tp82.laufometer.model.watchdog.Watchdog;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author Thorsten Platz
 */
@Service
@Path(value="/web/watchdog")
@Produces(MediaType.TEXT_HTML)
public class WatchdogWebResource extends WatchdogResource {
	private static final Logger LOG = Logger.getLogger(WatchdogWebResource.class.getName());

	@GET
	@Path("/check")
	public Response check() {
		Set<Watchdog> watchdogs = watchdogService.check ();
		return Response.ok().entity("checked watchdogs").build();
	}

	@GET
	@Path("/keepalive")
	public Response registerKeepaliveGET(@QueryParam("clientId") String clientId) {
		registerKeepaliveInternal(clientId);
		return Response.ok().build();
	}

}
