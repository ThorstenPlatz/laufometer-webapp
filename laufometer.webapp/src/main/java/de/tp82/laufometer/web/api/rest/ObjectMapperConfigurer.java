package de.tp82.laufometer.web.api.rest;

import de.tp82.laufometer.util.DateUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.stereotype.Service;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * Configure Jacksons JSON generation.
 *
 * @author Thorsten Platz
 */
@Service
@Provider
public class ObjectMapperConfigurer implements ContextResolver<ObjectMapper> {
	private final ObjectMapper objectMapper;

	public ObjectMapperConfigurer() {
		objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.configure(SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING, true);
		objectMapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
		objectMapper.configure(SerializationConfig.Feature.WRITE_EMPTY_JSON_ARRAYS, true);
		objectMapper.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);
		objectMapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);
		objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
		objectMapper.setDateFormat(DateUtils.ISO_8601_FORMAT);
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return objectMapper;
	}

}
