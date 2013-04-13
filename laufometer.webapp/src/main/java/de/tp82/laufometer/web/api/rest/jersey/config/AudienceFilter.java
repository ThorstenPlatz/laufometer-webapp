package de.tp82.laufometer.web.api.rest.jersey.config;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import de.tp82.laufometer.web.api.rest.Audience;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Thorsten Platz
 */
public class AudienceFilter implements ContainerRequestFilter {
	public static final Logger logger = Logger.getLogger(AudienceFilter.class.getName());

	@Override
	public ContainerRequest filter(ContainerRequest request) {

		// Check the version part of the URL.
		List<PathSegment> pathSegments = request.getPathSegments();

		if (pathSegments.size() < 1) {
			logger.warning("Path did not contain any segments. Unable to determine audience.");
			Response response = Response.status(Response.Status.NOT_FOUND)
					.entity("Missing audience in path!")
					.build();
			throw new WebApplicationException(response);
		}

		String audienceSegment = pathSegments.get(0).getPath();

		Audience audience;
		try {
			audience = Audience.from(audienceSegment);
			if(logger.isLoggable(Level.FINER))
				logger.finer("Detected audience for request: " + audience);
			return request;
		} catch(Exception exc) {
			logger.warning("Invalid audience in request path: " + audienceSegment);
			Response response = Response.status(Response.Status.NOT_FOUND)
					.entity("You specified an invalid audience: " + audienceSegment + ".")
					.build();
			throw new WebApplicationException(response);
		}


	}
}