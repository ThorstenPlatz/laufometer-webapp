package de.tp82.laufometer.web.controllers;

import com.google.common.base.Optional;
import de.tp82.laufometer.core.RunRepository;
import de.tp82.laufometer.model.run.Run;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Thorsten Platz
 */
@Controller
@RequestMapping(value = "/run/")
public class RunController {
	@Autowired
	private RunRepository runRepository;

	@RequestMapping(value="")
	public String indexWatchdogs(ModelMap model) {
		Optional<Run> oldestRun = runRepository.findOldestRun();

		List<Run> runs;
		if(oldestRun.isPresent()) {
			runs = runRepository.findRuns(oldestRun.get().getBegin(), Optional.<Date>absent());
		} else
			runs = Collections.emptyList();

		model.put("runs", runs);
		return "run/listRuns";
	}
}
