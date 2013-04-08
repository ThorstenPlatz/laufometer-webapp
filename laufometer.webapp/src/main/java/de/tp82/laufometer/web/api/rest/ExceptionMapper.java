package de.tp82.laufometer.web.api.rest;

import com.google.common.collect.Maps;
import com.sun.jersey.api.ParamException;
import com.sun.jersey.api.view.Viewable;
import de.tp82.laufometer.util.ExceptionHandling;
import de.tp82.laufometer.web.api.rest.model.ErrorResult;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Thorsten Platz
 */
@Component
@Provider
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Throwable> {
	private final static Logger LOG = Logger.getLogger(ExceptionMapper.class.getName());

	@Context
	private HttpHeaders httpHeaders;

	@Override
	public Response toResponse(Throwable throwable) {
		if(LOG.isLoggable(Level.WARNING)) {
			final String stackTracke = ExceptionHandling.getStacktrace(throwable);
			LOG.warning("Catching uncaught exception: " + throwable + ". Stacktrace follows:\n" + stackTracke);
		}

		/*
			Jersey returns 404 for invalid query/form/path parameters. Bad request (400) would be more
			appropriate in this case.
		 */
		if (throwable instanceof ParamException) {
			Object errorEntity = new ErrorResult("Invalid Parameter!", throwable.getMessage());
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(getEntityForProducedMimeType(errorEntity))
					.build();
		}

		Object errorEntity = new ErrorResult("Unexpected error!", throwable);
		return Response
				.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(getEntityForProducedMimeType(errorEntity))
				.build();
	}

	private Object getEntityForProducedMimeType(Object entity) {
		Object resultEntitiy = null;
		List<MediaType> acceptableMediaTypes = httpHeaders.getAcceptableMediaTypes();
		if(!acceptableMediaTypes.isEmpty()) {
			MediaType mostPreferredType = acceptableMediaTypes.get(0);
			if(mostPreferredType.isCompatible(MediaType.TEXT_HTML_TYPE)) {
				Map<String, Object> model = Maps.newHashMap();
				model.put("result", entity);
				Viewable resultView = new Viewable("/views/error", model);
				resultEntitiy = resultView;
			}
		}

		if(resultEntitiy == null)
			resultEntitiy = entity;

		return resultEntitiy;
	}

}
