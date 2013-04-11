package de.tp82.laufometer.web.api.rest.watchdog;

import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

/**
 * @author Thorsten Platz
 */
@Service
@Path(value="/rest/watchdog")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WatchdogRestResource extends WatchdogResource {
	private static final Logger LOG = Logger.getLogger(WatchdogRestResource.class.getName());

	@GET
	@Path("/check")
	public Response check() {
		watchdogService.check();
		return Response.ok().build();
	}

	@Deprecated
	@GET
	@Path("/keepalive")
	public Response registerKeepaliveGET(@QueryParam("clientId") String clientId) {
		registerKeepaliveInternal(clientId);
		return Response.ok().build();
	}

	@POST
	@Path("/keepalive")
	public Response registerKeepalivePOST(@FormParam("clientId") String clientId) {
		registerKeepaliveInternal(clientId);
		return Response.ok().build();
	}
}
