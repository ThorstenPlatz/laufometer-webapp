package de.tp82.laufometer.web.controllers;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.List;

/**
 * @author Thorsten Platz
 */
@Controller
@RequestMapping(value = "/ticks")
public class TicksController {

	@Deprecated
	@RequestMapping(method = RequestMethod.GET)
	public String listAllTicks(ModelMap model) {
		List<Date> ticks = Lists.newArrayList();

		for(int i=0; i<10; ++i) {
			ticks.add(new Date());
		}
		model.put("ticks", ticks);

		return "listTicks";
	}

	@RequestMapping(value="/upload")
	public String uploadTicks() {
		return "uploadTicks";
	}
}
