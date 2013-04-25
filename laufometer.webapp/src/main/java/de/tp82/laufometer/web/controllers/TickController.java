package de.tp82.laufometer.web.controllers;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import de.tp82.laufometer.core.RunImporter;
import de.tp82.laufometer.core.RunRepository;
import de.tp82.laufometer.core.TickImportHelper;
import de.tp82.laufometer.model.run.Run;
import de.tp82.laufometer.util.ExceptionHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Thorsten Platz
 */
@Controller
@RequestMapping(value = "/tick")
public class TickController {
	private static final Logger LOG = Logger.getLogger(TickController.class.getName());

	@Autowired
	RunRepository runRepository;

	@Autowired
	RunImporter runImporter;

	@RequestMapping(value="/upload", method = RequestMethod.POST)
	public String uploadTicksAction(ModelMap model,
	                                @RequestParam(value="ticksFile", required = false) MultipartFile ticksFile,
	                                @RequestParam(value="ticksText", required = false) String ticksText) {
		ActionResult result;
		try {
			List<Date> ticks = Collections.emptyList();
			if(ticksFile != null)
				//TODO tp: implement; see http://viralpatel.net/blogs/spring-mvc-multiple-file-upload-example/
				ticks = Collections.emptyList();

			if(ticksText != null)
				ticks = TickImportHelper.extractTicks(ticksText);

			List<Run> importedRuns = runImporter.importTicksAsRuns(ticks, true);

			result = ActionResult
					.success("Imported " + importedRuns.size() + " runs.")
					.next("../")
					.previous(ActionResult.NAVIGATION_NONE)
					.build();

		} catch (Exception exc) {
			if(LOG.isLoggable(Level.WARNING)) {
				String stacktrace = ExceptionHandling.getStacktrace(exc);
				LOG.warning("Error during import: " + exc + ". Stacktrace follows:\n" + stacktrace);
			}
			result = ActionResult
					.error("Error during import: " + exc)
					.build();
		}
		model.put("result", result);

		return "generic/actionResult";
	}



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

		return "tick/listTicks";
	}

	@RequestMapping(value="/upload", method = RequestMethod.GET)
	public String uploadTicksForm() {
		return "tick/uploadTicks";
	}

}
