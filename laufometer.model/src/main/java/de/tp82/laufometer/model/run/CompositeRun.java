package de.tp82.laufometer.model.run;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @author Thorsten Platz
 */
public class CompositeRun implements Run {
	private List<Run> runs;

	private CompositeRun(Iterable<Run> runs) {
		Preconditions.checkNotNull(runs);
		this.runs = Lists.newArrayList(runs);
		Preconditions.checkArgument(!this.runs.isEmpty());

		Collections.sort(this.runs, new Comparator<Run>() {
			@Override
			public int compare(Run o1, Run o2) {
				return o1.getBegin().compareTo(o2.getBegin());
			}
		} );
	}

	public Date getBegin() {
		return runs.get(0).getBegin();
	}

	public Date getEnd() {
		return runs.get(runs.size()-1).getEnd();
	}

	@Override
	public double getDistance() {
		double distance = 0;
		for(Run run : runs)
			distance += run.getDistance();

		return distance;
	}

	@Override
	public double getDuration() {
		double duration = 0;
		for(Run run : runs)
			duration += run.getDuration();
		return duration;
	}

	@Override
	public double getAverageSpeed() {
		return getDistance() / getDuration();
	}

	@Override
	public List<Date> getTicks() {
		List<Date> ticks = Lists.newArrayList();
		for(Run run : runs)
			ticks.addAll(run.getTicks());
		return ticks;
	}

	public static CompositeRun from(Iterable<Run> runs) {
		return new CompositeRun(runs);
	}
}
