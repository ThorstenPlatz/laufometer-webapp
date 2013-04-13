package de.tp82.laufometer.core;

import com.google.appengine.repackaged.com.google.common.collect.Sets;
import com.google.common.base.*;
import de.tp82.laufometer.model.watchdog.Watchdog;
import de.tp82.laufometer.persistence.WatchdogDAO;
import de.tp82.laufometer.util.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Thorsten Platz
 */
@Service
public class WatchdogService {
	private static final Logger LOG = Logger.getLogger(WatchdogService.class.getName());

	@Value("${laufometer.watchdog.keepalive.ping.interval}")
	private long pingInterval;

	@Autowired
	private WatchdogDAO watchdogRepository;

	public Watchdog check(Watchdog watchdog) {
		Preconditions.checkNotNull(watchdog);

		DateTime checkTime = DateTime.now();
		return performCheck(watchdog, checkTime);
	}

	public Set<Watchdog> check() {
		Stopwatch stopwatch = new Stopwatch();
		stopwatch.start();

		DateTime checkTime = DateTime.now();
		DateTime pingDeadline = checkTime.minus(getPingInterval());

		if(LOG.isLoggable(Level.INFO))
			LOG.info("Executing watchdog check for keepalive ping deadline: "
					+ DateUtils.ISO_8601_FORMAT.format(pingDeadline.toDate()));

		Iterable<Watchdog> watchdogs = watchdogRepository.findAllWatchdogs();

		int numberOfWatchdogs = 0;
		for(Watchdog watchdog : watchdogs) {
			++numberOfWatchdogs;

			performCheck(watchdog, checkTime);
		}

		stopwatch.stop();

		if(LOG.isLoggable(Level.INFO))
			LOG.info("Checked " + numberOfWatchdogs + " watchdogs in " + stopwatch.elapsed(TimeUnit.SECONDS) + " seconds.");

		return Collections.unmodifiableSet(Sets.newHashSet(watchdogs));
	}

	private Watchdog performCheck(Watchdog watchdog, DateTime checkTime) {
		DateTime pingDeadline = checkTime.minus(getPingInterval());

		boolean isAlive;
		if(watchdog.getLastPing().isPresent()) {
			DateTime lastPing = new DateTime(watchdog.getLastPing().get());
			isAlive = lastPing.isAfter(pingDeadline);
		} else {
			isAlive = false;
		}

		watchdog.setLastCheck(Optional.of(checkTime.toDate()));
		watchdog.setLastCheckResult(Optional.of(isAlive));

		watchdogRepository.save(watchdog);

		if(!isAlive)
			sendNotification(watchdog, pingDeadline.toDate());

		if(LOG.isLoggable(Level.FINER)) {
			LOG.finer("Checked watchdog: " + watchdog);
		}

		return watchdog;
	}

	private Duration getPingInterval() {
		// pingInterval is in seconds, but here we need millis
		return new Duration(pingInterval*1000);
	}

	private Set<InternetAddress> getRecipients(Watchdog watchdog) {
		Set<InternetAddress> recipients;

		String recipientString = watchdog.getNotificationRecepient();
		if(Strings.emptyToNull(recipientString) == null)
			return Collections.emptySet();

		Iterable<String> recipientStrings = Splitter.on(";")
				.trimResults()
				.omitEmptyStrings()
				.split(recipientString);

		recipients = Sets.newHashSet();
		for(String recipient : recipientStrings) {
			try {
				recipients.add(new InternetAddress(recipient));
			} catch (AddressException invalidAddress) {
				if(LOG.isLoggable(Level.WARNING))
					LOG.warning("Invalid e-mail address found for clientId='"
							+ watchdog.getClientId() + "': '" + recipient + "'. Error is: " + invalidAddress);
			}
		}

		return recipients;
	}

	private void sendNotification(Watchdog watchdog, Date pingDeadline) {
		Set<InternetAddress> recipients = getRecipients(watchdog);
		if(recipients.isEmpty())
			return;

		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		String msgSubject = "Missing Keepalive for " + watchdog.getClientId();
		String lastKeepalive = "";
		if(watchdog.getLastPing().isPresent())
			lastKeepalive = "Last keepalive ping is from "
					+ DateUtils.ISO_8601_FORMAT.format(watchdog.getLastPing().get());
		else
			lastKeepalive = "Never received a keepalive ping.";
		String msgBody = lastKeepalive + ".\n"
				+ "Ping deadline was: " + DateUtils.ISO_8601_FORMAT.format(pingDeadline) + ".\n"
				+ "Checking for keepalive pings every " + getPingInterval() + " seconds.";

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("thorsten.platz@gmail.com", "Laufometer Watchdog"));
			msg.addRecipient(Message.RecipientType.TO,
					new InternetAddress(watchdog.getNotificationRecepient(), "Watchdog Recepient " + watchdog.getClientId()));
			msg.setSubject(msgSubject);
			msg.setText(msgBody);
			Transport.send(msg);

		} catch (Exception exc) {
			if(LOG.isLoggable(Level.WARNING)) {
				LOG.warning("Error during sending of notification for Watchdog " + watchdog);
			}
		}
	}
}

