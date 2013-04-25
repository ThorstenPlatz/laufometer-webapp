package de.tp82.laufometer.model.run;

import com.google.common.base.Preconditions;

import java.util.Comparator;
import java.util.Date;

/**
 * @author Thorsten Platz
 */
public interface Run extends RunTickProvider {
	Date getBegin();

	Date getEnd();

	double getDistance();

	double getDuration();

	double getAverageSpeed();

	public static class RunBeginComparator implements Comparator<Run> {
		@Override
		public int compare(Run o1, Run o2) {
			Preconditions.checkNotNull(o1);
			Preconditions.checkNotNull(o2);

			return o1.getBegin().compareTo(o2.getBegin());
		}
	}
}
