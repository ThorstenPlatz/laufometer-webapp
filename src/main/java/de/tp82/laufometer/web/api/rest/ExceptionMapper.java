package de.tp82.laufometer.web.api.rest;

import com.sun.jersey.api.ParamException;
import de.tp82.laufometer.web.api.rest.model.ErrorResult;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * @author Thorsten Platz
 */
@Component
@Provider
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Throwable> {
	@Override
	public Response toResponse(Throwable throwable) {

		/*
			Jersey returns 404 for invalid query/form/path parameters. Bad request (400) would be more
			appropriate in this case.
		 */
		if (throwable instanceof ParamException) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(new ErrorResult("Invalid Parameter!", throwable.getMessage()))
					.build();
		}

		return Response
				.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(new ErrorResult("Unexpected error!", throwable.getMessage()))
				.build();
	}

}
