package de.tp82.laufometer.web.controllers;

import com.google.common.base.Optional;
import de.tp82.laufometer.core.RunRepository;
import de.tp82.laufometer.model.run.Run;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Thorsten Platz
 */
@Controller
@RequestMapping(value = "/run")
public class RunController {
	@Autowired
	private RunRepository runRepository;

	@RequestMapping("/")
	public String indexRuns(ModelMap model) {
		Optional<Run> oldestRun = runRepository.findOldestRun();

		List<Run> runs;
		if(oldestRun.isPresent()) {
			runs = runRepository.findRuns(oldestRun.get().getBegin(), Optional.<Date>absent());
		} else
			runs = Collections.emptyList();

		model.put("runs", runs);
		return "run/listRuns";
	}


	@RequestMapping("/calendar")
	public String calendarRuns(ModelMap model) {
		return "run/calendarRuns";
	}

	@RequestMapping("/administration")
	public String administerRuns(ModelMap model) {
		Optional<Run> oldest = runRepository.findOldestRun();
		Optional<Run> newest = runRepository.findLatestRun();

		model.put("oldestRun", oldest.orNull());
		model.put("newestRun", newest.orNull());

		return "run/runAdministration";
	}

	@RequestMapping(value="/delete", method = RequestMethod.POST)
	public String deleteRuns(ModelMap model,
	                         @RequestParam("from") String fromString,
	                         @RequestParam("to") String toString) {
		ActionResult result;
		try {
			DateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");

			Date from = dateFormatter.parse(fromString);
			Date to = dateFormatter.parse(toString);

			int numRunsDeleted = runRepository.delete(from, to);

			result = ActionResult
					.success(numRunsDeleted + " runs deleted.")
					.next("..")
					.previous(ActionResult.NAVIGATION_NONE)
					.build();
		} catch (Exception exc) {
			result = ActionResult
					.error("Error while deleting runs: " + exc)
					.previous("..")
					.build();
		}
		model.put("result", result);

		return "generic/actionResult";
	}
}
