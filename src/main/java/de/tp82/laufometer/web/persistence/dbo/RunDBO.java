package de.tp82.laufometer.web.persistence.dbo;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.tp82.laufometer.web.model.Run;
import de.tp82.laufometer.web.persistence.RunTicks;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:thorsten.platz@capgemini.com">Thorsten Platz</a>
 */
@PersistenceCapable
public class RunDBO implements RunTicks {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private List<Date> ticks;

	@Persistent
	private Date begin;

	@Persistent
	private Date end;

	public RunDBO(List<Date> ticks) {
		Preconditions.checkNotNull(ticks);
		Preconditions.checkArgument(!ticks.isEmpty());
		Preconditions.checkNotNull(ticks.get(0));
		Preconditions.checkNotNull(ticks.get(ticks.size()-1));

		this.ticks = ticks;
		begin = ticks.get(0);
		end = ticks.get(ticks.size()-1);
	}

	@Override
	public List<Date> getTicks() {
		return ticks;
	}

	public static RunDBO from(Run run) {
		return new RunDBO(run.getTicks());
	}

	public static List<RunDBO> from(List<Run> runs) {
		List<RunDBO> dbos = Lists.newArrayList();
		for(Run run : runs)
			dbos.add(RunDBO.from(run));
		return dbos;
	}

	@Override
	public String toString() {
		return "RunDBO(key=" + key + ", #ticks=" + ticks.size() + ")";
	}
}
