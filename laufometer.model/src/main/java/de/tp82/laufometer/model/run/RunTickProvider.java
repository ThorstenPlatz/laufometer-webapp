package de.tp82.laufometer.model.run;

import com.google.common.base.Preconditions;

import java.util.Date;
import java.util.List;

/**
 * Provides all the ticks (timestamps) for a single run.
 *
 * @author Thorsten Platz
 */
public interface RunTickProvider {

	/**
	 * Returns the ticks for the run in time ascending order.
	 * @return non-null and non-empty list of sorted ticks
	 */
	List<Date> getTicks();

	public static class SimpleTickProvider implements RunTickProvider {
		private List<Date> ticks;

		private SimpleTickProvider(List<Date> ticks) {
			Preconditions.checkNotNull(ticks);
			Preconditions.checkArgument(!ticks.isEmpty());

			this.ticks = ticks;
		}

		public List<Date> getTicks() {
			return ticks;
		}

		public static RunTickProvider from(List<Date> ticks) {
			return new SimpleTickProvider(ticks);
		}
	}
}
