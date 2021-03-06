package de.tp82.laufometer.web.controllers;

import com.google.common.collect.Lists;
import com.google.common.net.MediaType;
import de.tp82.laufometer.core.RunRepository;
import de.tp82.laufometer.core.TickImportHelper;
import de.tp82.laufometer.core.importer.RunImporter;
import de.tp82.laufometer.model.run.RunInterval;
import de.tp82.laufometer.util.ExceptionHandling;
import org.gmr.web.multipart.GMultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
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
	                                @RequestParam(value="ticksFile", required = false) GMultipartFile ticksFile,
	                                @RequestParam(value="ticksText", required = false) String ticksText,
	                                @RequestParam(value="skipKnownTicks", required = false, defaultValue = "true") boolean skipKnownTicks,
	                                HttpServletRequest request) {
		ActionResult result;
		try {
			List<Date> ticks = Lists.newArrayList();
			if(ticksFile != null) {
				ticks.addAll(TickImportHelper.extractTicks(ticksFile.getInputStream(),
						MediaType.parse(ticksFile.getContentType())));
			}
			if(ticksText != null)
				ticks.addAll(TickImportHelper.extractTicks(ticksText));

			List<RunInterval> importedRuns = runImporter.importTicksAsRuns(ticks, skipKnownTicks);

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

	@RequestMapping(value="/upload", method = RequestMethod.GET)
	public String uploadTicksForm() {
		return "tick/uploadTicks";
	}
}
