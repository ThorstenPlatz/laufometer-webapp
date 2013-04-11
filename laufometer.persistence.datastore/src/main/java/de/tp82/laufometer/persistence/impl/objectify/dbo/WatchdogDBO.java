package de.tp82.laufometer.persistence.impl.objectify.dbo;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import de.tp82.laufometer.model.watchdog.Watchdog;

import java.util.Date;
import java.util.List;

/**
 * @author Thorsten Platz
 */
@Entity
public class WatchdogDBO {
	@Id
	private String key;
	private String recipient;
	private Date lastPing;
	private Date lastCheck;
	private Boolean lastCheckResult;

	private WatchdogDBO() {
	}

	private WatchdogDBO(String key, String recipient,
	                   Date lastPing,
	                   Date lastCheck, Boolean lastCheckResult) {
		this.key = key;
		this.recipient = recipient;
		this.lastPing = lastPing;
		this.lastCheck = lastCheck;
		this.lastCheckResult = lastCheckResult;
	}

	public String getKey() {
		return key;
	}

	public String getRecipient() {
		return recipient;
	}

	public Date getLastPing() {
		return lastPing;
	}

	public Date getLastCheck() {
		return lastCheck;
	}

	public Boolean getLastCheckResult() {
		return lastCheckResult;
	}

	public static Watchdog toWatchdog(WatchdogDBO dbo) {
		Preconditions.checkNotNull(dbo);

		return Watchdog.WatchdogBuilder
				.createFor(dbo.getKey())
				.recipient(dbo.getRecipient())
				.lastPing(dbo.getLastPing())
				.lastCheck(dbo.getLastCheck())
				.lastCheckResult(dbo.getLastCheckResult())
				.build();
	}

	public static Iterable<Watchdog> toWatchdogs(Iterable<WatchdogDBO> dbos) {
		List<Watchdog> watchdogs = Lists.newArrayList();

		for(WatchdogDBO dbo : dbos)
			watchdogs.add(toWatchdog(dbo));

		return watchdogs;
	}

	public static WatchdogDBO from(Watchdog watchdog) {
		Preconditions.checkNotNull(watchdog);

		return new WatchdogDBO(watchdog.getClientId(), watchdog.getNotificationRecepient(),
				watchdog.getLastPing().orNull(),
				watchdog.getLastCheck().orNull(), watchdog.getLastCheckResult().orNull());
	}
}
