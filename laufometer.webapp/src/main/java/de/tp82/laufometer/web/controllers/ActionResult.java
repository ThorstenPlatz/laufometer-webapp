package de.tp82.laufometer.web.controllers;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 * @author Thorsten Platz
 */
public class ActionResult {
	public static final String NAVIGATION_HISTORY_BACK = "javascript:history.back();";
	public static final String NAVIGATION_NONE = "";

	private Type type;
	private Optional<String> message = Optional.absent();

	private String next = null;
	private String previous = null;

	private ActionResult(Type type) {
		Preconditions.checkNotNull(type);

		this.type = type;
	}

	public String getMessage() {
		return message.or("");
	}

	private void setMessage(Optional<String> message) {
		this.message = message;
	}

	public Type getType() {
		return type;
	}

	public boolean isSuccess() {
		return type == Type.SUCCESS;
	}

	public boolean isError() {
		return type == Type.ERROR;
	}

	public static enum Type {
		SUCCESS, ERROR
	}

	public String getNext() {
		return next;
	}

	private void setNext(String next) {
		this.next = next;
	}

	public String getPrevious() {
		return previous;
	}

	private void setPrevious(String previous) {
		this.previous = previous;
	}

	public static ActionResultBuilder success(String message) {
		return new ActionResultBuilder(Type.SUCCESS)
				.message(message)
				.previous(NAVIGATION_HISTORY_BACK);
	}

	public static ActionResultBuilder success() {
		return new ActionResultBuilder(Type.SUCCESS)
				.previous(NAVIGATION_HISTORY_BACK);
	}

	public static ActionResultBuilder error(String message) {
		return new ActionResultBuilder(Type.ERROR)
				.message(message)
				.previous(NAVIGATION_HISTORY_BACK);
	}

	public static class ActionResultBuilder {
		private ActionResult result;

		private ActionResultBuilder(Type type) {
			result = new ActionResult(type);
		}

		public ActionResultBuilder message(String message) {
			result.setMessage(Optional.of(message));
			return this;
		}

		public ActionResultBuilder next(String next) {
			result.setNext(next);
			return this;
		}

		public ActionResultBuilder previous(String previous) {
			result.setPrevious(previous);
			return this;
		}

		public ActionResult build() {
			return result;
		}
	}
}
