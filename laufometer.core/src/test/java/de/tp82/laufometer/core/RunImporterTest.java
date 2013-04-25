package de.tp82.laufometer.core;

import com.google.common.collect.Lists;
import de.tp82.laufometer.model.RunCreator;
import de.tp82.laufometer.model.run.Run;
import de.tp82.laufometer.model.run.SingleRun;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * @author Thorsten Platz
 */
public class RunImporterTest {
	private RunImporter runImporter;

	private RunRepository runRepoMock;


	private final double distancePerTick = 0.6;

	private final double minSpeed = 0.02;
	private final double maxSpeed = 2.6;

	private final double maxTickInterval = distancePerTick/minSpeed;
	private final double minTickInterval = distancePerTick/maxSpeed;

	@Before
	public void setUp() throws Exception {
		runRepoMock = mock(RunRepository.class);

		runImporter = new RunImporter();
		ReflectionTestUtils.setField(runImporter, "distancePerTick", distancePerTick);
		ReflectionTestUtils.setField(runImporter, "minSpeed", minSpeed);
		ReflectionTestUtils.setField(runImporter, "maxSpeed", maxSpeed);
		ReflectionTestUtils.setField(runImporter, "runRepository", runRepoMock);
	}

	@Test
	public void testRunDetection() throws Exception {
		RunCreator creator = new RunCreator();

		List<Run> separateRuns = Lists.newArrayList();

		DateTime begin = DateTime.parse("2012-01-03T12:00:00Z");
		Run previousRun = null;
		for(int i=0; i<12; ++i) {
			if(previousRun != null) {
				begin = new DateTime(previousRun.getEnd());
				// seperate the runs
				begin = begin.plus( Math.round(maxTickInterval * 1000) + 1);
			}
			previousRun = creator.create(begin.toDate(), (minSpeed+maxSpeed)/2, new Duration(10*1000));
			separateRuns.add(previousRun);
		}

		List<Date> ticks = Lists.newArrayList();
		for(Run run : separateRuns) {
			ticks.addAll(run.getTicks());
		}

		List<SingleRun> detectedRuns = runImporter.detectRuns(ticks);

		assertEquals(12, detectedRuns.size());

	}
}
