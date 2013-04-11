package de.tp82.laufometer.model.watchdog;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.util.Date;

/**
 * @author Thorsten Platz
 */
public class Watchdog {
	private String clientId;
	private Optional<Date> lastPing;
	private String notificationRecepient;
	private Optional<Date> lastCheck;
	private Optional<Boolean> lastCheckResult;

	private Watchdog(String clientId) {
		this.clientId = clientId;

		lastPing = Optional.absent();
		lastCheck = Optional.absent();
		lastCheckResult = Optional.absent();
	}

	public String getClientId() {
		return clientId;
	}

	public Optional<Date> getLastPing() {
		return lastPing;
	}

	public String getNotificationRecepient() {
		return notificationRecepient;
	}

	public void setNotificationRecepient(String notificationRecepient) {
		this.notificationRecepient = notificationRecepient;
	}

	public void setLastPing(Optional<Date> lastPing) {
		this.lastPing = lastPing;
	}

	public Optional<Date> getLastCheck() {
		return lastCheck;
	}

	public void setLastCheck(Optional<Date> lastCheck) {
		this.lastCheck = lastCheck;
	}

	public Optional<Boolean> getLastCheckResult() {
		return lastCheckResult;
	}

	public void setLastCheckResult(Optional<Boolean> lastCheckResult) {
		this.lastCheckResult = lastCheckResult;
	}

	@Override
	public String toString() {
		return "Watchdog{" +
				"clientId='" + clientId + '\'' +
				", lastPing=" + lastPing +
				", notificationRecepient='" + notificationRecepient + '\'' +
				", lastCheck=" + lastCheck +
				", lastCheckResult=" + lastCheckResult +
				'}';
	}

	public static class WatchdogBuilder {
		private Watchdog watchdog;

		public static WatchdogBuilder createFor(String clientId) {
			return new WatchdogBuilder(clientId);
		}

		public WatchdogBuilder(String clientId) {
			watchdog = new Watchdog(clientId);
		}

		public WatchdogBuilder recipient(String recipient) {
			Preconditions.checkNotNull(Strings.emptyToNull(recipient));
			watchdog.setNotificationRecepient(recipient);
			return this;
		}

		public WatchdogBuilder lastPing(Date lastPing) {
			watchdog.setLastPing(Optional.fromNullable(lastPing));
			return this;
		}

		public WatchdogBuilder lastCheck(Date lastCheck) {
			watchdog.setLastCheck(Optional.fromNullable(lastCheck));
			return this;
		}

		public WatchdogBuilder lastCheckResult(Boolean wasAlive) {
			watchdog.setLastCheckResult(Optional.fromNullable(wasAlive));
			return this;
		}

		public Watchdog build() {
			return watchdog;
		}
	}
}
