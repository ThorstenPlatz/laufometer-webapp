package de.tp82.laufometer.persistence.impl.objectify.dbo;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import de.tp82.laufometer.model.run.SingleRun;
import de.tp82.laufometer.model.run.RunTickProvider;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Thorsten Platz
 */
@Entity
public class RunDBO implements RunTickProvider {
	@Id
	private String key;

	@Index
	private Date begin;

	@Index
	private Date end;

	private List<Date> ticks;

	/**
	 * Prevent direct instance creation using new, but does not bother Objectify.
	 */
	private RunDBO() {}

	private RunDBO(String id, RunTickProvider runTickProvider) {
		Preconditions.checkNotNull(Strings.emptyToNull(id));
		Preconditions.checkNotNull(runTickProvider);
		Preconditions.checkNotNull(runTickProvider.getTicks());
		Preconditions.checkArgument(runTickProvider.getTicks().size() > 0);

		key = id;
		ticks = Lists.newArrayList(runTickProvider.getTicks());

		begin = ticks.get(0);
		end = ticks.get(ticks.size()-1);
	}

	public String getKey() {
		return key;
	}

	public Date getBegin() {
		return begin;
	}

	public Date getEnd() {
		return end;
	}

	@Override
	public List<Date> getTicks() {
		return Collections.unmodifiableList(ticks);
	}

	public static RunDBO from(SingleRun run) {
		Preconditions.checkNotNull(run);

		RunDBO dbo = new RunDBO(run.getId(), run);
		return dbo;
	}

	public static Iterable<RunDBO> from(Iterable<SingleRun> runs) {
		Set<RunDBO> dbos = Sets.newHashSet();
		for(SingleRun run : runs)
			dbos.add(from(run));
		return dbos;
	}

	public static SingleRun toRun(RunDBO dbo) {
		SingleRun run = SingleRun.fromRunTicks(dbo);
		assert run.getId().equals(dbo.getKey());
		return run;
	}

	public static Iterable<SingleRun> toRuns(Iterable<RunDBO> dbos) {
		List<SingleRun> runs = Lists.newArrayList();
		for(RunDBO dbo : dbos)
			runs.add(toRun(dbo));
		return runs;
	}
}