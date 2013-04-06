package de.tp82.laufometer.web.persistence.dbo;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

/**
 * @author Thorsten Platz
 */
public class PMF {
	private static final PersistenceManagerFactory pmfInstance =
			JDOHelper.getPersistenceManagerFactory("transactions-optional");

	private PMF() {}

	public static PersistenceManagerFactory get() {
		return pmfInstance;
	}

}
