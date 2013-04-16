package de.tp82.laufometer.core;

import com.google.common.collect.Lists;
import de.tp82.laufometer.model.run.CompositeRun;
import de.tp82.laufometer.model.run.Run;
import de.tp82.laufometer.util.BeanProvider;
import org.joda.time.DateTime;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Thorsten Platz
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RunDayGrouper {
	public static final long DAILY = 60 * 60 * 24;
	public static final long HOURLY = 60 * 60;
	public static final long QUARTERLY = 60 * 15;

	private long groupInterval;
	private List<Set<Run>> runGroups;

	private RunDayGrouper() {
	}

	public List<Run> groupRuns(Iterable<Run> runs) {
		List<Run> groupedRuns = Lists.newArrayList();

		for(Run run : runs)
			insertIntoGroup(run);

		for(Set<Run> group : runGroups) {
			CompositeRun groupRun = CompositeRun.from(group);
			groupedRuns.add(groupRun);
		}

		return groupedRuns;
	}

	public int getNumberOfGroups() {
		return (int) (DAILY / groupInterval);
	}

	public int getGroupIndex(Date refTime) {
		long secondsInDay = new DateTime(refTime).getSecondOfDay();
		return (int) (secondsInDay / groupInterval);
	}

	private void insertIntoGroup(Run run) {
		int groupIndex = getGroupIndex(run);
		runGroups.get(groupIndex).add(run);
	}

	private int getGroupIndex(Run run) {
		Date refTime = run.getBegin();
		return getGroupIndex(refTime);
	}

	public static RunDayGrouper create(long groupInterval) {
		RunDayGrouper instance = BeanProvider.getBean(RunDayGrouper.class);
		instance.groupInterval = groupInterval;
		return instance;
	}

}
