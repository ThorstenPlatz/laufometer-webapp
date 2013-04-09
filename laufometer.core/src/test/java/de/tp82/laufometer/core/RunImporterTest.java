package de.tp82.laufometer.core;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author Thorsten Platz
 */
public class RunImporterTest {

	private RunImporter runImporter;

	@Before
	public void setUp() throws Exception {
		runImporter = new RunImporter();
		ReflectionTestUtils.setField(runImporter, "distancePerTick", 0.6);
	}

	@Test
	public void testImportTicksAsRuns() throws Exception {

	}
}
