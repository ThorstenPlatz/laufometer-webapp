package de.tp82.laufometer.web.api.rest.importer.model;

import de.tp82.laufometer.model.run.Run;

import java.util.Date;
import java.util.List;

/**
 * @author Thorsten Platz
 */
public class ImportResult {
	private int numberOfImportedRuns;
	private int numberOfImportedTicks;
	private Date importTimeFrameBegin;
	private Date importTimeFrameEnd;
	private long importDuration;
	private List<String> errors;

	public ImportResult(List<Run> runs, List<Date> ticks, long duration, List<String> errors) {
		numberOfImportedRuns = runs.size();
		numberOfImportedTicks = ticks.size();
		if(!ticks.isEmpty()) {
			importTimeFrameBegin = ticks.get(0);
			importTimeFrameEnd = ticks.get(ticks.size()-1);
		}
		importDuration = duration;
		this.errors = errors;
	}

	public int getNumberOfImportedRuns() {
		return numberOfImportedRuns;
	}

	public int getNumberOfImportedTicks() {
		return numberOfImportedTicks;
	}

	public Date getImportTimeFrameBegin() {
		return importTimeFrameBegin;
	}

	public Date getImportTimeFrameEnd() {
		return importTimeFrameEnd;
	}

	public long getImportDuration() {
		return importDuration;
	}

	public List<String> getErrors() {
		return errors;
	}
}
