package de.tp82.laufometer.web.controllers;

import com.google.common.base.Optional;
import de.tp82.laufometer.core.RunRepository;
import de.tp82.laufometer.model.run.RunInterval;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Thorsten Platz
 */
@Controller
@RequestMapping(value = "/run")
public class RunController {
	private final static DateFormat DAY_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	private RunRepository runRepository;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String indexRuns(ModelMap model, @RequestParam("day") String dayString) {
		try {
			Date day = DAY_FORMAT.parse(dayString);
			LocalDate theDay = new LocalDate(day.getTime());

			Date dayBegin = theDay.toDateTimeAtStartOfDay().toDate();
			Date dayEnd = theDay.plusDays(1).toDateTimeAtStartOfDay().minusSeconds(1).toDate();

			List<RunInterval> runs = runRepository.findRuns(dayBegin, Optional.<Date>of(dayEnd));

			model.put("day", day);
			model.put("runs", runs);
			model.put("totalRunOfDay", RunInterval.from(runs));
			return "run/listRuns";

		} catch (Exception exc) {
			ActionResult result = ActionResult
					.error("Error while loading runs: " + exc)
					.build();
			model.put("result", result);
			return "generic/actionResult";
		}
	}


	@RequestMapping("/calendar")
	public String calendarRuns(ModelMap model) {
		return "run/calendarRuns";
	}

	@RequestMapping("/administration")
	public String administerRuns(ModelMap model) {
		Optional<RunInterval> oldest = runRepository.findOldestRun();
		Optional<RunInterval> newest = runRepository.findLatestRun();

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
			Date from = DAY_FORMAT.parse(fromString);
			Date to = DAY_FORMAT.parse(toString);

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
