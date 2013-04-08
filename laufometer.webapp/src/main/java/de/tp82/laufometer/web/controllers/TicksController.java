package de.tp82.laufometer.web.controllers;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import de.tp82.laufometer.core.RunRepository;
import de.tp82.laufometer.model.Run;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Thorsten Platz
 */
@Controller
@RequestMapping(value = "/ticks")
public class TicksController {

	@Autowired
	RunRepository runRepository;

	@Deprecated
	@RequestMapping(method = RequestMethod.GET)
	public String listAllTicks(ModelMap model) {

		Calendar aLongTimeAgo = Calendar.getInstance();
		aLongTimeAgo.set(2000, Calendar.JANUARY, 1);
		List<Run> runs = runRepository.findRuns(aLongTimeAgo.getTime(), Optional.<Date>absent());

		List<Date> ticks = Lists.newArrayList();
		for(Run run : runs)
			ticks.addAll(run.getTicks());

		Collections.sort(ticks);

		model.put("ticks", ticks);

		return "listTicks";
	}

	@RequestMapping(value="/upload")
	public String uploadTicks() {
		return "uploadTicks";
	}
}
