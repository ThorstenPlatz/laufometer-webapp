package de.tp82.laufometer.web.controllers;

import com.google.common.collect.Lists;
import de.tp82.laufometer.model.watchdog.Watchdog;
import de.tp82.laufometer.persistence.EntityNotFoundException;
import de.tp82.laufometer.persistence.WatchdogDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.logging.Logger;

/**
 * @author Thorsten Platz
 */
@Controller
@RequestMapping(value = "/watchdog/")
public class WatchdogController {
	private static final Logger LOG = Logger.getLogger(WatchdogController.class.getName());

	@Autowired
	private WatchdogDAO watchdogDAO;

	@RequestMapping(value="")
	public String indexWatchdogs(ModelMap model) {
		List<Watchdog> watchdogs = Lists.newArrayList(watchdogDAO.findAllWatchdogs());
		model.put("watchdogs", watchdogs);
		return "watchdog/listWatchdogs";
	}

	@RequestMapping(value="create", method = RequestMethod.GET)
	public String createWatchdogForm() {
		return "watchdog/createWatchdog";
	}

	@RequestMapping(value="create", method = RequestMethod.POST)
	public String createWatchdogAction(ModelMap model,
	                                   @RequestParam(value = "clientId") String clientId,
	                                   @RequestParam(value = "recipient") String recipient) {
		ActionResult result;
		try {
			Watchdog watchdog = Watchdog.WatchdogBuilder
					.createFor(clientId)
					.recipient(recipient)
					.build();


			try {
				watchdogDAO.getWatchdog(clientId);
				throw new IllegalArgumentException("Watchdog with clientId='" + clientId + "' already exists!");
			} catch (EntityNotFoundException entityNotFound) {
				// ok
			}

			watchdogDAO.save(watchdog);

			result = ActionResult
					.success("Watchdog created.")
					.next("edit/clientId=" + clientId)
					.previous(ActionResult.NAVIGATION_NONE)
					.build();
		} catch (Exception exc) {
			result = ActionResult
					.error("Error while creating watchdog: " + exc)
					.build();
		}
		model.put("result", result);

		return "generic/actionResult";
	}

	@RequestMapping(value="edit/{clientId}", method = RequestMethod.GET)
	public String editWatchdogForm(ModelMap model,
	                               @PathVariable(value = "clientId") String clientId) {
		ActionResult result;
		try {
			Watchdog watchdog = watchdogDAO.getWatchdog(clientId);
			model.put("watchdog", watchdog);

			result = ActionResult
					.success()
					.build();
		} catch (Exception exc) {
			result = ActionResult
					.error("Error while loading watchdog: " + exc)
					.build();
		}
		model.put("result", result);
		return "watchdog/editWatchdog";
	}

	@RequestMapping(value="edit/{clientId}", method = RequestMethod.POST)
	public String editWatchdogAction(ModelMap model,
	                                 @PathVariable(value = "clientId") String clientId,
	                                 @RequestParam(value = "recipient") String recipient) {
		ActionResult result;
		try {
			Watchdog watchdog = watchdogDAO.getWatchdog(clientId);
			watchdog.setNotificationRecepient(recipient);

			watchdogDAO.save(watchdog);

			result = ActionResult
					.success("Watchdog updated.")
					.next("..")
					.build();
		} catch (Exception exc) {
			result = ActionResult
					.error("Error while updating watchdog: " + exc)
					.build();
		}
		model.put("result", result);

		return "generic/actionResult";
	}

	@RequestMapping(value="delete/{clientId}", method = RequestMethod.POST)
	public String deleteWatchdogAction(ModelMap model,
	                                 @PathVariable(value = "clientId") String clientId) {
		ActionResult result;
		try {
			watchdogDAO.delete(clientId);

			result = ActionResult
					.success("Watchdog deleted.")
					.next("..")
					.previous(ActionResult.NAVIGATION_NONE)
					.build();
		} catch (Exception exc) {
			result = ActionResult
					.error("Error while updating watchdog: " + exc)
					.previous("..")
					.build();
		}
		model.put("result", result);

		return "generic/actionResult";
	}
}
