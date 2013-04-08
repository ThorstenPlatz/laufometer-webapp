package de.tp82.laufometer.web.api.rest.model;

import com.google.common.base.Preconditions;

import java.util.Collections;
import java.util.List;

/**
 * @author Thorsten Platz
 */
public class ErrorResult {
	private String message;
	private List<?> info;

	public ErrorResult(String message) {
		this.message = message;
		info = Collections.emptyList();
	}

	public ErrorResult(String message, Object info) {
		Preconditions.checkNotNull(message);
		Preconditions.checkNotNull(info);

		this.message = message;
		this.info = Collections.singletonList(info);
	}

	public ErrorResult(String message, List<?> info) {
		Preconditions.checkNotNull(message);
		Preconditions.checkNotNull(info);

		this.message = message;
		this.info = info;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<?> getInfo() {
		return info;
	}

	public void setInfo(List<?> info) {
		this.info = info;
	}
}
